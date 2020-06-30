/*
 *****************************************************************************
 *                       Confidentiality Information                         *
 *                                                                           *
 * This module is the confidential and proprietary information of            *
 * DBSentry Corp.; it is not to be copied, reproduced, or transmitted in any *
 * form, by any means, in whole or in part, nor is it to be used for any     *
 * purpose other than that for which it is expressly provided without the    *
 * written permission of DBSentry Corp.                                      *
 *                                                                           *
 * Copyright (c) 2004-2005 DBSentry Corp.  All Rights Reserved.              *
 *                                                                           *
 *****************************************************************************
 * $Id: UserPreferenceProfileB4Action.java,v 20040220.31 2006/03/13 14:18:32 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.preferences;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsExtendedUserProfile;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.preferences.UserPreferenceProfileForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.preference.UserPreferenceProfileBean;
import dms.web.beans.theme.Theme;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To populate UserPreferenceProfileBean and UserPreferenceProfileForm.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    20-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  02-03-2006 
 */
public class UserPreferenceProfileB4Action extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  /* member function to map int value to boolean & return it.*/
  private boolean int2bool(int intVal){
    boolean retval=false;
    if(intVal == 1)
      retval= false;
    if(intVal == 2)
      retval= true;
    return retval;
  }
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   * The code also takes into account user preferences for opening a document.  
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing logger
    Logger logger = Logger.getLogger("DbsLogger");  
    //initializing variables        
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ArrayList memberList=new ArrayList();
    UserPreferenceProfileForm userPreferenceProfileForm=new UserPreferenceProfileForm();        
    UserPreferences userPreferences=new UserPreferences();
    String[] credentialManager=null; 
    DbsDirectoryUser userToEdit=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    Theme theme = null;
    ArrayList themeList = null;
    int val=0;
    Boolean hideURLOption=new Boolean(false);
    Boolean URLOptionVisibility=new Boolean(true);
    ResultSet rsListWatchDocument = null;
    ConnectionBean connBean = null;
    Boolean tableDropped = new Boolean(false);
    try {
      logger.info("Entering UserPreferenceProfileB4Action now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();    
      if(!userInfo.isSystemAdmin()){
        hideURLOption=new Boolean(true);
      }
      String userName= dbsLibrarySession.getUser().getName();
      theme = new Theme(dbsLibrarySession);
      themeList= theme.listThemes();

      if(httpSession.getAttribute("messages")!=null){
        logger.debug("Saving action message in request stream");
        saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
        httpSession.removeAttribute("messages");
      } 
      if(httpSession.getAttribute("errors")!=null){
        logger.debug("Saving action errors in request stream");
        saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
        httpSession.removeAttribute("errors");
      }
    
      userToEdit=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,dbsLibrarySession.getUser().getName());
      if(userToEdit!=null){
        DbsPrimaryUserProfile userToEditProf=userToEdit.getPrimaryUserProfile();
        credentialManager=new String[1];
        credentialManager[0]=userToEdit.getCredentialManager();

        UserPreferenceProfileBean userPreferenceProfileBean=new UserPreferenceProfileBean();
        userPreferenceProfileBean.setUserName(userName);
        try{
          userPreferenceProfileBean.setAccessControlList(userToEdit.getAcl().getName());
        }catch(DbsException dbsException){
          if(dbsException.containsErrorCode(10200)){
            userPreferenceProfileBean.setAccessControlList("**Insufficient Access Right**");
          }else{
            throw dbsException;
          }
        }
        
        if (userToEdit.getAttribute(DbsDirectoryUser.DESCRIPTION_ATTRIBUTE).toString()!=null){
          userPreferenceProfileBean.setDescription(userToEdit.getAttribute(DbsDirectoryUser.DESCRIPTION_ATTRIBUTE).toString());
        }else{
          userPreferenceProfileBean.setDescription("**No Description found for " + userName + " **");
        }
        if(userToEditProf.getLanguage()!=null){
          userPreferenceProfileBean.setLanguage(userToEditProf.getLanguage().toString());
        }else{
          userPreferenceProfileBean.setLanguage("**Not Set**");
        }
        if(userToEditProf.getLocale()!=null){
          userPreferenceProfileBean.setLocale(userToEditProf.getLocale().toString());
        }else{
          userPreferenceProfileBean.setLocale("**Not Set**");
        }
        if(userToEditProf.getTimeZone() !=null){
          userPreferenceProfileBean.setTimeZone(userToEditProf.getTimeZone().getID().toString());
        }else{
          userPreferenceProfileBean.setTimeZone("**Not Set**");
        }
        if(userToEditProf.getCharacterSet()!=null){
          userPreferenceProfileBean.setCharacterSet(userToEditProf.getCharacterSet().toString());
        }else{
          userPreferenceProfileBean.setCharacterSet("**Not Set**");                
        }
        if(userToEditProf.getHomeFolder()!=null){
          userPreferenceProfileBean.setHomeFolder(userToEditProf.getHomeFolder().getAnyFolderPath());
        }else{
          userPreferenceProfileBean.setHomeFolder("**Not Set**");
        }
        DbsPropertyBundle defaultAcls=userToEditProf.getDefaultAcls();
        if(defaultAcls!=null){
          DbsProperty folderAclProp=defaultAcls.getProperty(DbsFolder.CLASS_NAME);
          DbsProperty documentAclProp=defaultAcls.getProperty(DbsDocument.CLASS_NAME);
          if(folderAclProp!=null){
            try{
              DbsAttributeValue dbsFolderAttrValue=folderAclProp.getValue();
              DbsAccessControlList defFolderAcl=(DbsAccessControlList)dbsFolderAttrValue.getPublicObject(dbsLibrarySession);
              userPreferenceProfileBean.setDefaultFolderAcl(defFolderAcl.getName());
            }catch(DbsException dbsException){
              if(dbsException.containsErrorCode(10200)){
                userPreferenceProfileBean.setDefaultFolderAcl("**Insufficient Access Right**");
              }else{
                throw dbsException;
              }
            }
          }
          if(documentAclProp!=null){
            try{
              DbsAttributeValue dbsDocumentAttrValue=documentAclProp.getValue();
              DbsAccessControlList defDocumentAcl=(DbsAccessControlList)dbsDocumentAttrValue.getPublicObject(dbsLibrarySession);
              userPreferenceProfileBean.setDefaultDocumentAcl(defDocumentAcl.getName());
            }catch(DbsException dbsException){
              if(dbsException.containsErrorCode(10200)) {
                  userPreferenceProfileBean.setDefaultDocumentAcl("**Insufficient Access Right**");
              }else{
                  throw dbsException;
              }
            }
          }
        }
        DbsExtendedUserProfile emailProfile=SearchUtil.getEmailUserProfile(dbsLibrarySession,userToEdit);
        if(emailProfile!=null){
          DbsAttributeValue mailAttr=emailProfile.getAttribute("EMAILADDRESS");
          if(mailAttr!=null){  
            userPreferenceProfileBean.setEmailAddress(mailAttr.getString(dbsLibrarySession));
          }else{
            userPreferenceProfileBean.setEmailAddress("**E-Mail Address not set**");
          }
          DbsAttributeValue mailAttrDir=emailProfile.getAttribute("MAILDIRECTORYLOCATION");

          if(!mailAttrDir.isNullValue()){
            DbsPublicObject mailPublicObject=mailAttrDir.getPublicObject(dbsLibrarySession);
            if(mailPublicObject instanceof  DbsFolder){
              DbsFolder mailFolder=(DbsFolder)mailPublicObject;
              userPreferenceProfileBean.setMailFolder(mailFolder.getAnyFolderPath());
            }
          }
        }
        if(userToEditProf.getContentQuota()!=null){
          logger.debug(userToEditProf.getContentQuota());
          if(userToEditProf.getContentQuota().isEnabled()){
            userPreferenceProfileBean.setStorage(new String().valueOf(userToEditProf.getContentQuota().getAllocatedStorage()/(1024*1024) + " MB"));
          }else{
            userPreferenceProfileBean.setStorage("Unlimited");
          }
        }else{
          userPreferenceProfileBean.setStorage("Unlimited");
        }
        request.setAttribute("userPreference",userPreferenceProfileBean);
        logger.debug("userPreference " + userPreferenceProfileBean);            
        DbsDirectoryGroup[] groupList=SearchUtil.listGroups(dbsLibrarySession);
        ArrayList groups=new ArrayList();
        for(int groupIndex=0 ; groupIndex < groupList.length ; groupIndex++){
          if(groupList[groupIndex].isDirectMember(userToEdit)){
            groups.add(groupList[groupIndex].getName());
          }
        }
        String [] memberGroups=new String[groups.size()];
        for(int memberIndex=0;memberIndex<groups.size();memberIndex++){
          memberGroups[memberIndex]=(String)groups.get(memberIndex);           
        }
        userPreferenceProfileForm.setCboGroup(memberGroups);
        }
        if ((dbsLibrarySession.getUser().getPropertyBundle())!=null){
          DbsPropertyBundle propertyBundleToEdit= dbsLibrarySession.getUser().getPropertyBundle();
          if(propertyBundleToEdit!=null){
            DbsProperty property=propertyBundleToEdit.getProperty("PermittedTreeAccessLevel");
            if (property!=null){
              DbsAttributeValue attrValue=property.getValue();
              userPreferenceProfileForm.setTxtPermittedTreeAccessLevel(attrValue.getInteger(dbsLibrarySession));                 
            }else{
              userPreferenceProfileForm.setTxtPermittedTreeAccessLevel(userPreferences.getTreeLevel());
            }
            property=propertyBundleToEdit.getProperty("ItemsToBeDisplayedPerPage");
            if (property!=null) {
              DbsAttributeValue attrValue=property.getValue();
              userPreferenceProfileForm.setTxtItemsToBeDisplayedPerPage(attrValue.getInteger(dbsLibrarySession)); 
            }else{
              userPreferenceProfileForm.setTxtItemsToBeDisplayedPerPage(userPreferences.getRecordsPerPage());
            }
            property=propertyBundleToEdit.getProperty("NavigationType");
            if (property!=null) {
              DbsAttributeValue attrValue=property.getValue();
              userPreferenceProfileForm.setCboNavigationType(attrValue.getInteger(dbsLibrarySession)); 
            }else{
              userPreferenceProfileForm.setCboNavigationType(userPreferences.getNavigationType());
            }
            /* code added on account of open doc option */
            property=propertyBundleToEdit.getProperty("OpenDocOption");                    
            if(property!=null){
              DbsAttributeValue attrValue=property.getValue();
              val=attrValue.getInteger(dbsLibrarySession);
              userPreferenceProfileForm.setBoolChkOpenDocInNewWin(int2bool(val));
              logger.debug("attrValue.getInteger(dbsLibrarySession): "+attrValue.getInteger(dbsLibrarySession));
              logger.debug("userPreferenceProfileForm.getBoolChkOpenDocInNewWin(): "+userPreferenceProfileForm.getBoolChkOpenDocInNewWin());
            }else{
              val=userPreferences.getChkOpenDocInNewWin();
              userPreferenceProfileForm.setBoolChkOpenDocInNewWin(int2bool(val));
              logger.debug("chkOpenDocInNewWin set by userPreferences: "+userPreferences.getChkOpenDocInNewWin());
              logger.debug("userPreferenceProfileForm.getBoolChkOpenDocInNewWin(): "+userPreferenceProfileForm.getBoolChkOpenDocInNewWin());
            }
            
            property=propertyBundleToEdit.getProperty("Theme");
            if (property!=null) {
              DbsAttributeValue attrValue=property.getValue();
              userPreferenceProfileForm.setCboSelectTheme(attrValue.getString(dbsLibrarySession)); 
            }
            property=propertyBundleToEdit.getProperty(DbsDirectoryUser.ENCRYPTION_ENABLED);
            if (property!=null) {
              DbsAttributeValue attrValue=property.getValue();
              boolean isEncryptionenabled=attrValue.getBoolean(dbsLibrarySession);
              if(isEncryptionenabled){
                request.setAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED,new Boolean(true));
              }else{
                request.setAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED,new Boolean(false));
              }
            }else{
              request.setAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED,new Boolean(false));
            }
            logger.debug((Boolean)request.getAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED));
          }
        }else{
          userPreferenceProfileForm.setTxtPermittedTreeAccessLevel(userPreferences.getTreeLevel());
          userPreferenceProfileForm.setTxtItemsToBeDisplayedPerPage(userPreferences.getRecordsPerPage());                
          userPreferenceProfileForm.setCboNavigationType(userPreferences.getNavigationType());
          val=userPreferences.getChkOpenDocInNewWin();
          /*added code */ 
          userPreferenceProfileForm.setBoolChkOpenDocInNewWin(int2bool(val));
          logger.debug("Setting BoolChkOpenDocInNewWin for the first time as : "+userPreferenceProfileForm.getBoolChkOpenDocInNewWin() );
          request.setAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED,new Boolean(false));
        }
        /*added code */ 
        String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");  
        CryptographicUtil cryptoUtil= new CryptographicUtil();
        if(cryptoUtil.getSystemKeyStoreFile(contextPath)){
          URLOptionVisibility=new Boolean(false);
        }
      
        try{
          /* obtain statement object */
          String query = null; 
          /* construct dynamic query string and execute */
          String relativePath =httpSession.getServletContext().getRealPath("/")+
                   "WEB-INF"+File.separator+"params_xmls"+File.separator+
                   "GeneralActionParam.xml";
          connBean = new ConnectionBean(relativePath);
          query = "select PO_ID from watch_pos where USER_ID = '"+userName+"' ";
          int startIndex=0;
          rsListWatchDocument= connBean.executeQuery(query,true);
          if (rsListWatchDocument!=null){
            ArrayList watchDocumentsName=new ArrayList();
            ArrayList watchDocumentsID=new ArrayList();
            while(rsListWatchDocument.next()){
              // docName obtained from id directly, changed by Suved,
              // exception thrown earlier for versioned documents.
              try {
                String docName= dbsLibrarySession.getPublicObject(new Long(rsListWatchDocument.getLong("PO_ID"))).getName();
                String docID=(String)(rsListWatchDocument.getString("PO_ID"));
                watchDocumentsName.add(docName);
                watchDocumentsID.add(docID);
              }catch (DbsException dbsEx) {
                logger.error("An Exception occurred in userPreferenceProfileB4Action... ");
                logger.error(dbsEx.toString());
              }
              startIndex++;
            }
  
            String [] lstWatchDocument=new String[watchDocumentsName.size()];
            for(int memberIndex=0;memberIndex<watchDocumentsName.size();memberIndex++){
              lstWatchDocument[memberIndex]=(String)watchDocumentsName.get(memberIndex);           
            }
            userPreferenceProfileForm.setLstWatchDocumentName(lstWatchDocument);
     
            String [] lstWatchDocumentID=new String[watchDocumentsID.size()];
            for(int memberIndex=0;memberIndex<watchDocumentsID.size();memberIndex++){
              lstWatchDocumentID[memberIndex]=(String)watchDocumentsID.get(memberIndex);           
            }
            userPreferenceProfileForm.setLstWatchDocumentID(lstWatchDocumentID);
          }
  
          logger.debug("Document List with Watch added by the user");
          ActionMessages actionMessages = new ActionMessages();
          ActionMessage actionMessage = new ActionMessage("msg.watch.added.successfully");
          actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          httpSession.setAttribute("ActionMessages",actionMessages);
  
        }catch (SQLException sqlEx) {
          ActionErrors actionErrors = new ActionErrors();
          ActionError actionError = new ActionError("errors.add.watch");
          actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
          httpSession.setAttribute("ActionErrors",actionErrors);
          logger.error("An Exception occurred in userPreferenceProfileB4Action... ");
          logger.error(sqlEx.toString());
          //sqlEx.printStackTrace();
        }finally{
          /* close statement and connection objects */
          connBean.closeStatement();
          connBean.closeConnection();          
        } 
        if( userPreferenceProfileForm.getLstWatchDocumentID() == null ){
          tableDropped = new Boolean(true);
          String [] errMsg = new String[1];
          errMsg[0] = new String("There seems to be some problem with the database.Your watch status cannnot be displayed .Kindly contact your System Administrator.");
          userPreferenceProfileForm.setLstWatchDocumentName(errMsg);
        }
        request.setAttribute("tableDropped",tableDropped);
        request.setAttribute("URLOptionVisibility",URLOptionVisibility);
        logger.debug("URLOptionVisibility: "+((Boolean)request.getAttribute("URLOptionVisibility")).booleanValue());
      
        request.setAttribute("userPreferenceProfileForm",userPreferenceProfileForm);
        request.setAttribute("themeList",themeList); 
        request.setAttribute("hideURLOption",hideURLOption);
        logger.debug("hideURLOption: "+((Boolean)request.getAttribute("hideURLOption")).booleanValue());
        logger.debug("userPreferenceProfileForm" + userPreferenceProfileForm);
      }catch(DbsException dbsException){
        logger.error("An Exception occurred in userPreferenceProfileB4Action... ");
        logger.error(dbsException.toString());
      }catch(Exception exception){
        logger.error("An Exception occurred in userPreferenceProfileB4Action... ");
        logger.error(exception.toString());
      }
      logger.info("Exiting userPreferenceProfileB4Action");
      return mapping.findForward("success");
  }
}

