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
 * $Id: UserB4EditAction.java,v 20040220.25 2006/03/13 14:17:25 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/*dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsContentQuota;
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
import dms.web.actionforms.user.UserNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/*java API */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To populate user_edit.jsp with the specified user data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    03-02-2004
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   03-03-2006 
 */
public class UserB4EditAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Locale locale = getLocale(request);       
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    String[] credentialManager=null; 
    DbsDirectoryUser userToEdit=null;
    String workFlowAclDefault="WORKFLOW_NOT_ENABLED";        
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    ArrayList memberList=new ArrayList();
    UserNewEditForm userNewEditForm=new UserNewEditForm();
    try{
      logger.info("Entering userB4EditAction now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();    
      String userName= (String)httpSession.getAttribute("radSelect");
      logger.debug("Fetching data for user : " + userName);
      userToEdit=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,userName);
      logger.debug(userToEdit);
      if(userToEdit!=null){
        DbsPrimaryUserProfile userToEditProf=userToEdit.getPrimaryUserProfile();
        credentialManager=new String[1];
        credentialManager[0]=userToEdit.getCredentialManager();
        userNewEditForm.setTxtUserName(userName); 
        userNewEditForm.setTxtAccessControlList(userToEdit.getAcl().getName());     
        userNewEditForm.setCboCredentialManager(credentialManager);
        userNewEditForm.setTxaDescription(userToEdit.getAttribute(DbsDirectoryUser.DESCRIPTION_ATTRIBUTE).toString());
        userNewEditForm.setCboLanguage(new String[]{userToEditProf.getLanguage()});            
        if(userToEditProf.getLocale()!=null){
          userNewEditForm.setCboLocale(new String[]{userToEditProf.getLocale().toString()});
        }
        if(userToEditProf.getTimeZone() !=null){
          userNewEditForm.setCboTimeZone(new String[]{userToEditProf.getTimeZone().getID()});
        }
        if(userToEditProf.getCharacterSet() !=null){
          userNewEditForm.setCboCharacterSet(new String[]{userToEditProf.getCharacterSet()});
        }
        if(userToEditProf.getHomeFolder()!=null){
          userNewEditForm.setTxtHomeFolder(userToEditProf.getHomeFolder().getAnyFolderPath());
        }
        if(userToEditProf.getContentQuota()!=null){
          DbsContentQuota userQuota=userToEditProf.getContentQuota();
          if(userQuota.isEnabled()){
            userNewEditForm.setRadQuota("1");            
            userNewEditForm.setTxtQuota(new String().valueOf((userQuota.getAllocatedStorage())/(1024*1024))); 
          }else{
            userNewEditForm.setRadQuota("2");            
          }
        }else{
          userNewEditForm.setRadQuota("2");            
        }
        if(userToEdit.isSystemAdminEnabled()){
          userNewEditForm.setRadStatus("1");
        }else if(userToEdit.isAdminEnabled()){
          userNewEditForm.setRadStatus("2");
        }else if(userToEdit.getOwner().getName().equals(userToEdit.getName())){
          userNewEditForm.setRadStatus("3");
        }
        DbsPropertyBundle defaultAcls=userToEditProf.getDefaultAcls();
        if(defaultAcls!=null){
          DbsProperty folderAclProp=defaultAcls.getProperty(DbsFolder.CLASS_NAME);
          DbsProperty documentAclProp=defaultAcls.getProperty(DbsDocument.CLASS_NAME); 
          DbsProperty workFlowAclProp=defaultAcls.getProperty("WORKFLOWACL"); 
          if(folderAclProp!=null){
            DbsAttributeValue dbsFolderAttrValue=folderAclProp.getValue();
            DbsAccessControlList defFolderAcl=(DbsAccessControlList)dbsFolderAttrValue.getPublicObject(dbsLibrarySession);        
            userNewEditForm.setTxtDefaultFolderAcl(defFolderAcl.getName());
          }
          if(documentAclProp!=null){
            DbsAttributeValue dbsDocumentAttrValue=documentAclProp.getValue();      
            DbsAccessControlList defDocumentAcl=(DbsAccessControlList)dbsDocumentAttrValue.getPublicObject(dbsLibrarySession);            
            userNewEditForm.setTxtDefaultDocumentAcl(defDocumentAcl.getName());
          }
          /* code added to account for WF ACL */
          if(workFlowAclProp!=null) {
            DbsAttributeValue dbsWorkFlowAttrValue=workFlowAclProp.getValue();
            try{
              userNewEditForm.setTxtWorkFlowAcl(new String[] {workFlowAclDefault});
              logger.debug("WF ACL is not null");
              DbsPublicObject [] dbsACLs = dbsWorkFlowAttrValue.getPublicObjectArray(dbsLibrarySession);
              int length = ( dbsACLs==null )?0:dbsACLs.length;
              if( length != 0 ){
                String [] finalWfAcls = new String[length];
                for( int index = 0; index < length; index++ ){
                  finalWfAcls[index] = dbsACLs[index].getName();
                }
                userNewEditForm.setTxtWorkFlowAcl(finalWfAcls);
              }
            }catch(DbsException dbsEx){           
              logger.error("An Exception occurred in UserB4EditAction... ");
              logger.debug("DbsException occurred while obtaining workFlowAcl.getName()...");
              logger.error(dbsEx.toString());
              dbsEx.printStackTrace();                        
            }catch(Exception aclNotFoundExcep){   
              /* when WfACL applied is accidently deleted ...*/
              logger.error("An Exception occurred in UserB4EditAction... ");
              logger.debug("Exception occurred while obtaining workFlowAcl.getName()...");
              ActionError error = new ActionError("user.workflowacl.notfound",userName);
              errors.add(ActionErrors.GLOBAL_ERROR,error);
              aclNotFoundExcep.printStackTrace();
            }
          }else{
            userNewEditForm.setTxtWorkFlowAcl(new String [] {workFlowAclDefault});
          }
          if(userToEditProf.getContentQuota()!=null){
            if(userToEditProf.getContentQuota().isEnabled()){
              userNewEditForm.setTxtQuota(new String().valueOf(userToEditProf.getContentQuota().getAllocatedStorage()/(1024*1024)));
            }
          }
        }
        DbsExtendedUserProfile emailProfile=SearchUtil.getEmailUserProfile(dbsLibrarySession,userToEdit);
        if(emailProfile!=null){
          DbsAttributeValue mailAttr=emailProfile.getAttribute("EMAILADDRESS");
          if(mailAttr!=null){  
            userNewEditForm.setTxtEmailAddress(mailAttr.getString(dbsLibrarySession));
          }
          DbsAttributeValue mailAttrDir=emailProfile.getAttribute("MAILDIRECTORYLOCATION");
          if(!mailAttrDir.isNullValue()){
            DbsPublicObject mailPublicObject=mailAttrDir.getPublicObject(dbsLibrarySession);
            if(mailPublicObject instanceof  DbsFolder){
              DbsFolder mailFolder=(DbsFolder)mailPublicObject;
              userNewEditForm.setTxtMailFolder(mailFolder.getAnyFolderPath());
            }
          }
        }
        DbsDirectoryGroup[] groupList=SearchUtil.listGroups(dbsLibrarySession);
        ArrayList groups=new ArrayList();
        for(int groupIndex=0 ; groupIndex < groupList.length ; groupIndex++){
          if(groupList[groupIndex].isDirectMember(userToEdit)){
            groups.add(groupList[groupIndex].getName());
          }
        }
        String [] memberGroups=new String[groups.size()];
        for(int memberIndex=0;memberIndex<groups.size();memberIndex++){
          memberGroups[memberIndex]=(String)groups.get(memberIndex)+" [G]";           
        }
        userNewEditForm.setLstGroupOrUserList(memberGroups);
        logger.debug("userNewEditForm : " + userNewEditForm);
        logger.debug("Fetched data for user : " + userName);
        request.setAttribute("userNewEditForm",userNewEditForm);
        httpSession.setAttribute("userNewEditFormOld",userNewEditForm);                
      }else{
        ActionError editError=new ActionError("errors.user.notfound",userName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);            
        httpSession.setAttribute("errors",errors);
        return mapping.findForward("failure");
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in UserB4EditAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in UserB4EditAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
    }
    logger.info("Exiting userB4EditAction now...");
    return mapping.findForward("success");
  }
}