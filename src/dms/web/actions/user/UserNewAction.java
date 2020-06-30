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
 * $Id: UserNewAction.java,v 20040220.28 2006/03/13 14:17:25 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCollection;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsExtendedUserProfile;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsFolderPathResolver;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsTransaction;
import dms.beans.DbsUserManager;
import dms.beans.DbsValueDefault;
import dms.web.actionforms.user.UserNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To save user_new.jsp with the specified group data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class UserNewAction extends Action {
    public static final int SYSTEM_ADMIN=1;
    public static final int ADMIN=2;    
    public static final int NON_ADMIN_CAN_CHANGE_PASSWORD=3;    
//    public static final int NON_ADMIN_CANT_CHANGE_PASSWORD=4;  
    public static final String PROP_BUNDLE_ACL_NAME="Public";  
    public static final String QUOTA_LIMITED="1";  
    DbsLibrarySession dbsLibrarySession = null;

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Initialize logger
        Logger logger = Logger.getLogger("DbsLogger");
        Locale locale = getLocale(request);
        UserInfo userInfo = null;
        HttpSession httpSession = null;

        String userName=null;
        String password;
        String accessControlListName=null;
        String defFolderAclName=null;
        String defDocumentAclName=null;
        String userDescription=null;
        String status=null;
        String quota=null;
        String userHomeFolder=null;
        String userMailFolder=null;
        String emailAddress=null;
        boolean isHomeFolderCreated=false;
        boolean isMailFolderCreated=false;
        String[] groups=null;
        String[] credentialManager=null;
        String[] defLanguage=null;
        String[] defCharset=null;
        String[] defTimezone=null;
        String[] defLocale=null;
        String [] workFlowAcls = null;
        
        DbsDirectoryUser newUser=null;
        Object userExists=null;
        DbsAccessControlList userAclToApply=null;
        DbsFolder createdHomeFolder=null;
        DbsFolder createdMailFolder=null;
        DbsUserManager userManager=null;
        DbsTransaction newTransaction=null;
        DbsPropertyBundle defaultAcls=null;
        DbsExtendedUserProfile emailProfile=null;
        DbsValueDefault userValueDefault=null;
        DbsAccessControlList homeFolderAcl=null;
        DbsAccessControlList mailFolderAcl=null;
        DbsAccessControlList inboxAcl=null;
        DbsFileSystem fileSystem=null;
        String inboxName=null;
        String isQuotaLimited="";
        long newUserId=0;
        boolean dbUserToBeDeleted=false;
        String workFlowAclName=null;        
        String workFlowAclDefault="WORKFLOW_NOT_ENABLED";        
        UserNewEditForm userNewEditForm=null;
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();
        Hashtable options=new Hashtable();
        ConnectionBean dbConnection=new ConnectionBean(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml");
        Statement userStatement=null;
        try{  
            logger.info("Entering UserNewAction now...");
            userName = (String)PropertyUtils.getSimpleProperty(form, "txtUserName");
            userName=userName.toUpperCase();
            password = ((String)PropertyUtils.getSimpleProperty(form, "txtPassword")).trim();
            try{
              userStatement=dbConnection.getStatement();
              userStatement.execute("create user "+userName+" identified by "+password);
              try{
                  userStatement.execute("grant connect,resource to "+userName);
                  
              }catch(Exception grantException){
              userStatement.execute("drop user "+userName);
              logger.error("Error in creating database user");
              logger.error(grantException.getMessage());
              grantException.printStackTrace();              
              throw grantException;              
            }
            }catch(Exception dbException){
              logger.error("Error in creating database user");
              logger.error(dbException.getMessage());
              dbException.printStackTrace();
              throw dbException;              
            }
            logger.debug("Saving data for new user : " + userName);
            logger.debug("Initializing Variable ...");        
            httpSession = request.getSession(false);
            userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            dbsLibrarySession = userInfo.getDbsLibrarySession();
            //newUserId=(SearchUtil.getId(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,userName));     
            DbsCollection userCollection=dbsLibrarySession.getDirectoryUserCollection();
            try{
                userExists=userCollection.getItems(userName);
            }catch(DbsException dbEx){
                if(dbEx.containsErrorCode(12214)){
                    //do nothing when the user is not existing
                }else{
                    throw dbEx;
                }
            }
            userNewEditForm = (UserNewEditForm)form;            
            if(!(userExists instanceof DbsDirectoryUser)){
                newTransaction=dbsLibrarySession.beginTransaction();
                logger.debug("Transaction started");
                logger.debug("userNewEditForm " + userNewEditForm);
                
                status = ((String)PropertyUtils.getSimpleProperty(form, "radStatus")).trim();
          
                accessControlListName=((String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList")).trim();
                defFolderAclName=((String)PropertyUtils.getSimpleProperty(form, "txtDefaultFolderAcl")).trim();  
           
                defDocumentAclName=((String)PropertyUtils.getSimpleProperty(form, "txtDefaultDocumentAcl")).trim();
                quota=((String)PropertyUtils.getSimpleProperty(form, "txtQuota")).trim();
                isQuotaLimited=((String)PropertyUtils.getSimpleProperty(form, "radQuota")).trim();
                
                userHomeFolder=((String)PropertyUtils.getSimpleProperty(form, "txtHomeFolder")).trim();
                userMailFolder=((String)PropertyUtils.getSimpleProperty(form, "txtMailFolder")).trim();
           
                userAclToApply=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,accessControlListName);               
                groups=(String[])PropertyUtils.getSimpleProperty(form,"lstGroupOrUserList");
                credentialManager=(String[])PropertyUtils.getSimpleProperty(form,"cboCredentialManager");          
                defLanguage=(String[])PropertyUtils.getSimpleProperty(form,"cboLanguage");          
                defCharset=(String[])PropertyUtils.getSimpleProperty(form,"cboCharacterSet");          
                defTimezone=(String[])PropertyUtils.getSimpleProperty(form,"cboTimeZone");          
                defLocale=(String[])PropertyUtils.getSimpleProperty(form,"cboLocale");  
                userDescription=(String)PropertyUtils.getSimpleProperty(form,"txaDescription"); 
                emailAddress=((String)PropertyUtils.getSimpleProperty(form,"txtEmailAddress")).trim(); 
                workFlowAcls=((String[])PropertyUtils.getSimpleProperty(form,"txtWorkFlowAcl")); 

                httpSession = request.getSession(false);
                userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
                dbsLibrarySession = userInfo.getDbsLibrarySession();        
                userManager=new DbsUserManager(dbsLibrarySession);   
                if(Integer.parseInt(status)==SYSTEM_ADMIN){
                    options.put(DbsUserManager.SYSTEM_ADMIN_ENABLED,DbsAttributeValue.newAttributeValue(true));                  
                    options.put(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(true));                    
                }else if(Integer.parseInt(status)==ADMIN){
                    options.put(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(true));
                }else if(Integer.parseInt(status)==NON_ADMIN_CAN_CHANGE_PASSWORD){
                    options.put(DbsUserManager.CAN_CHANGE_PASSWORD, DbsAttributeValue.newAttributeValue(true));
/*                }else if(Integer.parseInt(status)==NON_ADMIN_CANT_CHANGE_PASSWORD){
                    options.put(DbsUserManager.CAN_CHANGE_PASSWORD,DbsAttributeValue.newAttributeValue(true));
*/                }     
                
                userValueDefault=(DbsValueDefault)SearchUtil.findObject(dbsLibrarySession,DbsValueDefault.CLASS_NAME,"IFS.ADK.CreateUserDefinitions");
                DbsAttributeValue userDefDefinitionAttr=userValueDefault.getPropertyValue();
                DbsPropertyBundle createDefPropBundle=(DbsPropertyBundle)userDefDefinitionAttr.getPublicObject(dbsLibrarySession);
                fileSystem=new DbsFileSystem(dbsLibrarySession);
                if(userHomeFolder!=null && (!userHomeFolder.equals(""))){
                    options.put(DbsUserManager.HAS_HOME_FOLDER,DbsAttributeValue.newAttributeValue(true));                    
                    DbsFolderPathResolver fpr=new DbsFolderPathResolver(dbsLibrarySession);            
                    fpr.setRootfolder();            
                    DbsFolder rootFolder=(DbsFolder)fpr.findPublicObjectByPath("/");
                    String homeFolderAclId=(createDefPropBundle.getProperty("HomeFolderAcl")).getValue().getString(dbsLibrarySession);
                    homeFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(homeFolderAclId));
                    try{
                        createdHomeFolder=(DbsFolder)fpr.findPublicObjectByPath(userHomeFolder);
                    }catch(DbsException folderFindException){
                        
                        if(folderFindException.containsErrorCode(30619)){
                            createdHomeFolder=fileSystem.createFolder(userHomeFolder,rootFolder,true,null);
                        }else{
                            dbUserToBeDeleted=true;
                            throw folderFindException;
                        }
                    }
                    isHomeFolderCreated=true;
//                    logger.debug("Created Folder"+createdHomeFolder.getName());
                }

                
                if(quota!=null && (!quota.equals("")) && isQuotaLimited.equals(QUOTA_LIMITED)){
                    options.put(DbsUserManager.CONTENT_QUOTA_ENABLED,DbsAttributeValue.newAttributeValue(true));
                    options.put(DbsUserManager.CONTENT_QUOTA_ALLOCATED_STORAGE, DbsAttributeValue.newAttributeValue((Long.parseLong(quota))*(1024*1024)));
                }else{
                    options.put(DbsUserManager.CONTENT_QUOTA_ENABLED,DbsAttributeValue.newAttributeValue(false));
                }

                if(emailAddress!=null && (!emailAddress.equals(""))){
                    options.put(DbsUserManager.HAS_EMAIL,DbsAttributeValue.newAttributeValue(true));
                    try{
                        options.put(DbsUserManager.EMAIL_ADDRESS,DbsAttributeValue.newAttributeValue(emailAddress));
                    }catch(DbsException dbsEmailException){
                        if(dbsEmailException.containsErrorCode(30010)){
                            ActionError editError=new ActionError("errors.email.unique.30010",emailAddress);
                            errors.add(ActionErrors.GLOBAL_ERROR,editError);
                        }
                        throw dbsEmailException;
                    }
                }
                newUser=userManager.createUser(userName,password,options);
                newUser.setAcl(userAclToApply);
                newUser.setAttribute(DbsDirectoryUser.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(userDescription));
                newUser.setCredentialManager(credentialManager[0]);
                emailProfile=SearchUtil.getEmailUserProfile(dbsLibrarySession,newUser);
                                
                if(groups!=null){
                    logger.debug("groups.length");
                    for(int index=0;index < groups.length ; index++){
                        DbsDirectoryGroup dbsGroup=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groups[index].substring(0,(groups[index].length()-4)));
                        dbsGroup.addMember((DbsDirectoryUser)newUser);
//                        logger.debug("User "+ newUser.getName() + " added to group "+ dbsGroup.getName());
                    }
                }
                DbsPrimaryUserProfile profile=newUser.getPrimaryUserProfile();
 
                if(defFolderAclName!=null && (!defFolderAclName.equals(""))){
                    DbsAccessControlList defFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,defFolderAclName);
                    newUser.putProperty(DbsFolder.CLASS_NAME,DbsAttributeValue.newAttributeValue(defFolderAcl));
                }else{
                    String defFolderAclId=(createDefPropBundle.getProperty("DefaultAclsBundleAcl")).getValue().getString(dbsLibrarySession);
                    DbsAccessControlList defFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(defFolderAclId));
                    newUser.putProperty(DbsFolder.CLASS_NAME,DbsAttributeValue.newAttributeValue(defFolderAcl));
                }
        
                if(defDocumentAclName!=null && (!defDocumentAclName.equals(""))){
                    DbsAccessControlList defDocumentAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,defDocumentAclName);
                    newUser.putProperty(DbsDocument.CLASS_NAME,DbsAttributeValue.newAttributeValue(defDocumentAcl));
                }else{
                    String defDocumentAclId=(createDefPropBundle.getProperty("DefaultAclsBundleAcl")).getValue().getString(dbsLibrarySession);
                    DbsAccessControlList defDocumentAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(defDocumentAclId));
                    newUser.putProperty(DbsDocument.CLASS_NAME,DbsAttributeValue.newAttributeValue(defDocumentAcl));
                }
                
                if( workFlowAcls != null &&  workFlowAcls.length !=0 ){
                  DbsAccessControlList [] dbsAcls = new DbsAccessControlList[workFlowAcls.length];
                  for( int index = 0; index < workFlowAcls.length; index++ ){
                    workFlowAclName = workFlowAcls[index];
                    DbsAccessControlList workFlowAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,workFlowAclName);
                    dbsAcls[index] = workFlowAcl;
                  }
                  newUser.putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(dbsAcls));
                }else{
                  String [] wfAcls = new String [1];
                  wfAcls[0] = workFlowAclDefault;
                  newUser.putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(wfAcls));
                }
                if(newUser.getPropertyBundle()!=null){
                    DbsPropertyBundle defaultBundle=newUser.getPropertyBundle();
                     DbsAccessControlList propBundleAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,PROP_BUNDLE_ACL_NAME);
                     //defaultBundle.setAcl(propBundleAcl);
                     //System.out.println(propBundleAcl.getAcl().getName());
                    profile.setDefaultAcls(defaultBundle);
                }

                if(isHomeFolderCreated){
                   profile.setHomeFolder(createdHomeFolder);
                   createdHomeFolder.setOwnerByName(userName);
                   createdHomeFolder.setAcl(homeFolderAcl);
                }

                if(userMailFolder!=null && (!userMailFolder.equals(""))){
                    if(emailProfile !=null){
                        options.put(DbsUserManager.HAS_EMAIL,DbsAttributeValue.newAttributeValue(true));
                        DbsFolderPathResolver fpr=new DbsFolderPathResolver(dbsLibrarySession);   
                        fpr.setRootfolder();
                        DbsFolder rootFolder=(DbsFolder)fpr.findPublicObjectByPath("/");
                        String mailFolderAclId=(createDefPropBundle.getProperty("EmailSubfolderAcl")).getValue().getString(dbsLibrarySession);
                        mailFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(mailFolderAclId));
                        inboxName=createDefPropBundle.getProperty("InboxName").getValue().getString(dbsLibrarySession);
                        String inboxAclId=createDefPropBundle.getProperty("InboxAcl").getValue().getString(dbsLibrarySession);
                        inboxAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(inboxAclId));
                        try{
                            createdMailFolder=(DbsFolder)fpr.findPublicObjectByPath(userMailFolder);        
                        }catch(DbsException folderFindException){
                            if(folderFindException.containsErrorCode(30619)){
                                createdMailFolder=fileSystem.createFolder(userMailFolder,rootFolder,true,mailFolderAcl);                  
                            }else{
                                throw folderFindException;
                            }
                        }
                        isMailFolderCreated=true;
                    }
                }

                if(isMailFolderCreated){
                   emailProfile.setAttribute("MAILDIRECTORYLOCATION",DbsAttributeValue.newAttributeValue(createdMailFolder));
                   createdMailFolder.setOwnerByName(userName); 
                   DbsFolder inboxFolder=fileSystem.createFolder(inboxName,createdMailFolder,true,inboxAcl);
                   inboxFolder.setOwnerByName(userName);
                }

                if(!defLanguage[0].equals("UnspecifiedOptionValue")){
                    profile.setLanguage(defLanguage[0]);
                }

                if(!defCharset[0].equals("UnspecifiedOptionValue")){
                    profile.setCharacterSet(defCharset[0]);
                }

                if(!defTimezone[0].equals("UnspecifiedOptionValue")){
                    profile.setTimeZone(TimeZone.getTimeZone(defTimezone[0]));
                }

                if(!defLocale[0].equals("UnspecifiedOptionValue")){
                    if(defLocale[0].length()==5){
                        profile.setLocale(new Locale(defLocale[0].substring(0,2),defLocale[0].substring(3)));
                    }else if(defLocale[0].length()==2){
                        profile.setLocale(new Locale(defLocale[0].substring(0,2),""));
                    }
                }
                
                dbsLibrarySession.completeTransaction(newTransaction);
                
                logger.debug("Transaction completed");
                logger.debug("Data saved for user : " + userName);            
                newTransaction=null;
                }else{
                    ActionError createError=new ActionError("errors.user.unique",userName);
                    errors.add(ActionErrors.GLOBAL_ERROR,createError);
                }
            }catch(DbsException dbsException){
                logger.error("An Exception occurred in UserNewAction... ");
                logger.error(dbsException.toString());
                if(dbsException.containsErrorCode(32609)){
                    ActionError editError=new ActionError("errors.group.32609",userHomeFolder);
                    errors.add(ActionErrors.GLOBAL_ERROR,editError);
                }else if(dbsException.containsErrorCode(10172)){
                    ActionError editError=new ActionError("errors.user.10172",userName);
                    errors.add(ActionErrors.GLOBAL_ERROR,editError);  
                }else if(dbsException.containsErrorCode(30010)){
                    ActionError editError=new ActionError("errors.email.unique.30010",emailAddress);
                    errors.add(ActionErrors.GLOBAL_ERROR,editError);
                }else{
                    ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
                    errors.add(ActionErrors.GLOBAL_ERROR,editError);
                }
                try{
                    userStatement.execute("drop user "+userName);
                }catch(Exception dbExcep){
                  dbExcep.printStackTrace();
                }
            }catch(Exception exception){
                logger.error("An Exception occurred in UserNewAction... ");
                logger.error(exception.toString());
                ActionError editError=new ActionError("errors.catchall",exception);
                errors.add(ActionErrors.GLOBAL_ERROR,editError);
                try{
                    userStatement.execute("drop user "+userName);
                }catch(Exception dbExcep){
                  dbExcep.printStackTrace();
                }
            }finally{
                try{
                    if(newTransaction!=null){
                        logger.debug("Transaction Aborted for user ");
                        if(newUser!=null){
                            newUser.free();
                        }
                        dbsLibrarySession.abortTransaction(newTransaction);
                    }
                    dbConnection.closeStatement();
                    dbConnection.closeConnection();
                }catch(DbsException nestedException){
                    logger.error("An Exception occurred in UserNewAction... ");
                    logger.error(nestedException.toString());
                }
            }//end finally
            if (!errors.isEmpty()) {
                logger.debug("User could not be created");
                saveErrors(request, errors);
                return (mapping.getInputForward());
            }
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("user.new.ok",userName);
            messages.add("message1", msg);
            httpSession.setAttribute("messages",messages);
            logger.info("Exiting UserNewAction now...");
            return mapping.findForward("success");
        }
}
