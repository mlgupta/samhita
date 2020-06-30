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
 * $Id: UserEditAction.java,v 20040220.29 2006/03/13 14:17:25 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/*dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsContentQuota;
import dms.beans.DbsContentQuotaDefinition;
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
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
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
 *	Purpose: To save user_edit.jsp with the specified user data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class UserEditAction extends Action {
    public static final int SYSTEM_ADMIN=1;
    public static final int ADMIN=2;    
    public static final int NON_ADMIN_CAN_CHANGE_PASSWORD=3;    
//    public static final int NON_ADMIN_CANT_CHANGE_PASSWORD=4;    
    public static final String QUOTA_LIMITED="1";  
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

        DbsDirectoryUser userToEdit=null;
        DbsPrimaryUserProfile userProf=null;
        DbsTransaction editTransaction=null;
        DbsExtendedUserProfile emailProfile=null;
        String userName=null;
        DbsValueDefault userValueDefault=null;
        DbsAccessControlList homeFolderAcl=null;
        DbsAccessControlList mailFolderAcl=null;
        DbsAccessControlList inboxAcl=null;
        String inboxName=null;
        DbsFileSystem fileSystem=null;
        UserNewEditForm userNewEditForm=null;
        String isQuotaLimited="";
        String[] workFlowAclName=null;        
        String workFlowAclDefault="WORKFLOW_NOT_ENABLED";
        // Validate the request parameters specified by the user
        ActionErrors errors = new ActionErrors();  
    
        try{
            logger.info("Entering UserEditAction now...");
            httpSession = request.getSession(false);      
            userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            UserNewEditForm userNewEditFormOld=(UserNewEditForm)httpSession.getAttribute("userNewEditFormOld");
            userNewEditForm= (UserNewEditForm)form;
            dbsLibrarySession = userInfo.getDbsLibrarySession(); 
            userName=((String)PropertyUtils.getSimpleProperty(form, "txtUserName")).trim();      
            logger.debug("Saving data for edit user : " + userName);            
            String password=((String)PropertyUtils.getSimpleProperty(form, "txtPassword")).trim();
            String status=((String)PropertyUtils.getSimpleProperty(form, "radStatus")).trim();
            String accessControlListName=((String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList")).trim();
            String defFolderAclName=((String)PropertyUtils.getSimpleProperty(form, "txtDefaultFolderAcl")).trim();  
            String defDocumentAclName=((String)PropertyUtils.getSimpleProperty(form, "txtDefaultDocumentAcl")).trim();
//            String radQuota=((String)PropertyUtils.getSimpleProperty(form, "radQuota")).trim();
            String quota=((String)PropertyUtils.getSimpleProperty(form, "txtQuota")).trim();
            isQuotaLimited=((String)PropertyUtils.getSimpleProperty(form, "radQuota")).trim();
            String userHomeFolder=((String)PropertyUtils.getSimpleProperty(form, "txtHomeFolder")).trim();
            String userMailFolder=((String)PropertyUtils.getSimpleProperty(form, "txtMailFolder")).trim();
            String[] groups=(String[])PropertyUtils.getSimpleProperty(form,"lstGroupOrUserList");
            String[] credentialManager=(String[])PropertyUtils.getSimpleProperty(form,"cboCredentialManager");          
            String[] defLanguage=(String[])PropertyUtils.getSimpleProperty(form,"cboLanguage");          
            String[] defCharset=(String[])PropertyUtils.getSimpleProperty(form,"cboCharacterSet");          
            String[] defTimezone=(String[])PropertyUtils.getSimpleProperty(form,"cboTimeZone");          
            String[] defLocale=(String[])PropertyUtils.getSimpleProperty(form,"cboLocale");  
            String description=(String)PropertyUtils.getSimpleProperty(form,"txaDescription"); 
            String emailAddress=((String)PropertyUtils.getSimpleProperty(form,"txtEmailAddress")).trim(); 

            workFlowAclName=((String[])PropertyUtils.getSimpleProperty(form, "txtWorkFlowAcl"));           
            userToEdit=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,userName);
            userProf=userToEdit.getPrimaryUserProfile();
            editTransaction=dbsLibrarySession.beginTransaction();
            logger.debug("Transaction started");
            if(!password.equalsIgnoreCase("PasswordNotChanged123")){
                Vector passwordParams = new Vector();
                passwordParams.addElement(credentialManager[0]);
                passwordParams.addElement(userToEdit.getDistinguishedName());
                passwordParams.addElement(password);
                passwordParams.addElement(null);		
                dbsLibrarySession.invokeServerMethod("DYNCredentialManagerSetPassword", passwordParams);
            }
            if(!status.equals(userNewEditFormOld.getRadStatus())){
                if(Integer.parseInt(status)==SYSTEM_ADMIN){
                    userToEdit.setAttribute(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(true));
                    userToEdit.setAttribute(DbsUserManager.SYSTEM_ADMIN_ENABLED,DbsAttributeValue.newAttributeValue(true));                  
                }else if(Integer.parseInt(status)==ADMIN){
                    userToEdit.setAttribute(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(true));
                    userToEdit.setAttribute(DbsUserManager.SYSTEM_ADMIN_ENABLED,DbsAttributeValue.newAttributeValue(false));                                      
                }else if(Integer.parseInt(status)==NON_ADMIN_CAN_CHANGE_PASSWORD){
                    userToEdit.setAttribute(DbsUserManager.SYSTEM_ADMIN_ENABLED,DbsAttributeValue.newAttributeValue(false));
                    userToEdit.setAttribute(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(false));
                    userToEdit.setOwnerByName(userToEdit.getName());

/*                }else if(Integer.parseInt(status)==NON_ADMIN_CANT_CHANGE_PASSWORD){
                    userToEdit.setAttribute(DbsUserManager.SYSTEM_ADMIN_ENABLED,DbsAttributeValue.newAttributeValue(false));
                    userToEdit.setAttribute(DbsUserManager.ADMIN_ENABLED, DbsAttributeValue.newAttributeValue(false));                  
                    userToEdit.setOwnerByName(userToEdit.getName());
                    userToEdit.setOwnerByName(userToEdit.getCreator().getName());                    
                */}
            }
            if(!accessControlListName.equals(userNewEditFormOld.getTxtAccessControlList())) {
                DbsAccessControlList aclToApply=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,accessControlListName);
                userToEdit.setAcl(aclToApply);
            }
            if((!defFolderAclName.equals(userNewEditFormOld.getTxtDefaultFolderAcl())) && defFolderAclName!=null && (!defFolderAclName.equals(""))){
                DbsAccessControlList defFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,defFolderAclName);                              
                userToEdit.putProperty(DbsFolder.CLASS_NAME,DbsAttributeValue.newAttributeValue(defFolderAcl));
            }
            if((!defDocumentAclName.equals(userNewEditFormOld.getTxtDefaultDocumentAcl())) && defDocumentAclName!=null && (!defDocumentAclName.equals(""))){
                DbsAccessControlList defDocumentAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,defDocumentAclName);               
                userToEdit.putProperty(DbsDocument.CLASS_NAME,DbsAttributeValue.newAttributeValue(defDocumentAcl)); 
            }
            if( workFlowAclName != null &&  workFlowAclName.length !=0 ){
              DbsAccessControlList [] dbsAcls = new DbsAccessControlList[workFlowAclName.length];
              for( int index = 0; index < workFlowAclName.length; index++ ){
                DbsAccessControlList workFlowAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,workFlowAclName[index]);                   
                dbsAcls[index] = workFlowAcl;  
              }
              userToEdit.putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(dbsAcls));
            }else{
              userToEdit.putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(new String[]{workFlowAclDefault}));
            }
            if(userToEdit.getPropertyBundle()!=null){
              userProf.setDefaultAcls(userToEdit.getPropertyBundle());
            }
            userValueDefault=(DbsValueDefault)SearchUtil.findObject(dbsLibrarySession,DbsValueDefault.CLASS_NAME,"IFS.ADK.CreateUserDefinitions");
            DbsAttributeValue userDefDefinitionAttr=userValueDefault.getPropertyValue();
            DbsPropertyBundle createDefPropBundle=(DbsPropertyBundle)userDefDefinitionAttr.getPublicObject(dbsLibrarySession);
            fileSystem=new DbsFileSystem(dbsLibrarySession);
            if(!userHomeFolder.equals(userNewEditFormOld.getTxtHomeFolder()) && userHomeFolder!=null && (!userHomeFolder.equals(""))){
                DbsFolder createdHomeFolder=null;
                DbsFolderPathResolver fpr=new DbsFolderPathResolver(dbsLibrarySession);            
                fpr.setRootfolder();            
                DbsFolder rootFolder=(DbsFolder)fpr.findPublicObjectByPath("/"); 
                String homeFolderAclId=(createDefPropBundle.getProperty("HomeFolderAcl")).getValue().getString(dbsLibrarySession);
                homeFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(homeFolderAclId));
                try{
                    createdHomeFolder=(DbsFolder)fpr.findPublicObjectByPath(userHomeFolder);
                    createdHomeFolder.setOwnerByName(userName);
                    createdHomeFolder.setAcl(homeFolderAcl);
                    userProf.setHomeFolder(createdHomeFolder);
                }catch(DbsException folderFindException){
                    if(folderFindException.containsErrorCode(30619)){
                        createdHomeFolder=fileSystem.createFolder(userHomeFolder,rootFolder,true,null);
                        createdHomeFolder.setOwnerByName(userName);
                        createdHomeFolder.setAcl(homeFolderAcl);
                        userProf.setHomeFolder(createdHomeFolder);
                    }else{
                        throw folderFindException;
                    }
                }
            }  
            emailProfile=SearchUtil.getEmailUserProfile(dbsLibrarySession,userToEdit) ;
            if(!userMailFolder.equals(userNewEditFormOld.getTxtMailFolder()) && userMailFolder!=null && (!userMailFolder.equals(""))){
                if(emailProfile !=null){   
                    logger.debug("Entering Mail Folder Creation");
                    DbsFolder createdMailFolder=null;              
                    DbsFolderPathResolver fpr=new DbsFolderPathResolver(dbsLibrarySession);            
                    fpr.setRootfolder();            
                    DbsFolder rootFolder=(DbsFolder)fpr.findPublicObjectByPath("/");
                    String mailFolderAclId=(createDefPropBundle.getProperty("EmailSubfolderAcl")).getValue().getString(dbsLibrarySession);
                    mailFolderAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(mailFolderAclId));
                    inboxName=createDefPropBundle.getProperty("InboxName").getValue().getString(dbsLibrarySession);
                    String inboxAclId=createDefPropBundle.getProperty("InboxAcl").getValue().getString(dbsLibrarySession);
                    inboxAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,Long.parseLong(inboxAclId));
                    try{
                        createdMailFolder=fileSystem.createFolder(userMailFolder,rootFolder,true,mailFolderAcl);
                        createdMailFolder.setOwnerByName(userName);
                        emailProfile.setAttribute("MAILDIRECTORYLOCATION",DbsAttributeValue.newAttributeValue(createdMailFolder));
                        DbsFolder inboxFolder=fileSystem.createFolder(inboxName,createdMailFolder,true,inboxAcl);           
                        inboxFolder.setOwnerByName(userName);
                    }catch(DbsException folderFindException){
                        if(folderFindException.containsErrorCode(30619)){
                            createdMailFolder=fileSystem.createFolder(userMailFolder,rootFolder,true,mailFolderAcl);
                            createdMailFolder.setOwnerByName(userName);
                            emailProfile.setAttribute("MAILDIRECTORYLOCATION",DbsAttributeValue.newAttributeValue(createdMailFolder));
                            DbsFolder inboxFolder=fileSystem.createFolder(inboxName,createdMailFolder,true,inboxAcl);
                            inboxFolder.setOwnerByName(userName);
                        }else{
                            throw folderFindException;
                        }
                    }
                }
            } 
                       
            DbsContentQuota userQuota=userProf.getContentQuota();
            if((!quota.equals(userNewEditFormOld.getTxtQuota())) && quota!=null && (!quota.equals(""))){                   
                if(userQuota!=null && isQuotaLimited.equals(QUOTA_LIMITED)){
                   userQuota.setEnabled(true);
                   userQuota.setAllocatedStorage((Long.parseLong(quota))*(1024*1024));
                }else{
                   DbsContentQuotaDefinition quotaDef=new DbsContentQuotaDefinition(dbsLibrarySession);
                   quotaDef.setAttribute(DbsContentQuota.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(userToEdit.getName()+" CONTENT QUOTA"));
                   userQuota=(DbsContentQuota)dbsLibrarySession.createPublicObject(quotaDef);
                   userQuota.setAllocatedStorage((Long.parseLong(quota))*(1024*1024));
                   userProf.setContentQuota(userQuota);
                }
            }else if(quota==null || quota.equals("") && (!isQuotaLimited.equals(QUOTA_LIMITED))){
                if(userQuota!=null){               
                    userQuota.setAllocatedStorage(0);
                    userQuota.setEnabled(false);
                 }  
            }
            if(!credentialManager[0].equals(userNewEditFormOld.getCboCredentialManager())){
                userToEdit.setCredentialManager(credentialManager[0]);
            } 
            if(!defLanguage[0].equals(userNewEditFormOld.getCboLanguage())){
                userProf.setLanguage(defLanguage[0]);
            } 
            if(!defCharset[0].equals(userNewEditFormOld.getCboCharacterSet())){
                userProf.setCharacterSet(defCharset[0]);
            } 
            if(!defTimezone[0].equals(userNewEditFormOld.getCboTimeZone())){
                userProf.setTimeZone(TimeZone.getTimeZone(defTimezone[0]));
            } 
            if(!defLocale[0].equals(userNewEditFormOld.getCboLocale())){
                if(defLocale[0].length()==5){
                    userProf.setLocale(new Locale(defLocale[0].substring(0,2),defLocale[0].substring(3)));
                }else if(defLocale[0].length()==2){
                    userProf.setLocale(new Locale(defLocale[0].substring(0,2),""));            
                }
            } 
            if((!description.equals(userNewEditFormOld.getTxaDescription())) && description!=null && (!description.equals(""))) {
                userToEdit.setAttribute(DbsDirectoryUser.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(description));            
            } 
            if((!emailAddress.equals(userNewEditFormOld.getTxtEmailAddress())) && emailAddress!=null && (!emailAddress.equals(""))) {
                if(emailProfile !=null){  
                    try{
                        emailProfile.setAttribute(DbsUserManager.EMAIL_ADDRESS,DbsAttributeValue.newAttributeValue(emailAddress));
                    }catch(DbsException dbsExcep){
                        if(dbsExcep.containsErrorCode(30010)){                          
                            ActionError editError=new ActionError("errors.email.unique.30010",emailAddress);
                            errors.add(ActionErrors.GLOBAL_ERROR,editError);
                        }else
                        {
                            throw dbsExcep;
                        }
                    }
                }
            }
            String[] groupsOld=userNewEditFormOld.getLstGroupOrUserList();
            int totalCount =0;
            if(groups!=null){
               totalCount=groups.length;
            }
            int totalCountOld =0;
            if(groupsOld!=null){
                totalCountOld=groupsOld.length;
            }
                
            boolean isOnlyInOld=true; 
            for(int indexSelectedOld=0;indexSelectedOld<totalCountOld;indexSelectedOld++){
                isOnlyInOld=true;
                for(int indexSelected=0;indexSelected<totalCount;indexSelected++){
                    if(groupsOld[indexSelectedOld].equals(groups[indexSelected])){
                        isOnlyInOld=false;
                    }
                }
                if(isOnlyInOld){
                    DbsDirectoryGroup dbsGroup=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groupsOld[indexSelectedOld].substring(0,(groupsOld[indexSelectedOld].length()-4)));            
                    dbsGroup.removeMember((DbsDirectoryUser)userToEdit);            
                    logger.debug("User "+userToEdit.getName()+" successfully removed from group "+ dbsGroup.getName());
                }
            }
            boolean isOnlyInNew=true;
            for(int indexSelected=0;indexSelected<totalCount;indexSelected++){
            isOnlyInNew=true;
                for(int indexSelectedOld=0;indexSelectedOld<totalCountOld;indexSelectedOld++){
                    if(groups[indexSelected].equals(groupsOld[indexSelectedOld])){
                        isOnlyInNew=false;
                    }
                }
                if(isOnlyInNew){
                    DbsDirectoryGroup dbsGroup=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groups[indexSelected].substring(0,(groups[indexSelected].length()-4)));            
                    dbsGroup.addMember((DbsDirectoryUser)userToEdit);            
                    logger.debug("User "+userToEdit.getName()+" successfully added to group "+ dbsGroup.getName());
                }
            }
            dbsLibrarySession.completeTransaction(editTransaction);
            logger.debug("userNewEditForm : " + userNewEditForm);
            logger.debug("Data saved for user : " + userName);
            logger.debug("Transaction completed");
            editTransaction=null;
        }catch(DbsException dbsException){
            logger.error("An Exception occurred in UserEditAction... ");
            logger.error(dbsException.toString());
            ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }catch(Exception exception){
            logger.error("An Exception occurred in UserEditAction... ");
            logger.error(exception.toString());
            ActionError editError=new ActionError("errors.catchall",exception);
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }finally{
            try{
                if(editTransaction!=null){
                    dbsLibrarySession.abortTransaction(editTransaction);                 
                    logger.debug("Transaction aborted");                    
                }       
            }catch(DbsException nestedException){
              logger.error("An Exception occurred in UserEditAction... ");
              logger.error(nestedException.toString());
            }  
        }//end finally
        if (!errors.isEmpty()) {
          logger.debug("User not saved");
          saveErrors(request, errors);
          return (mapping.getInputForward());
        }
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("user.edit.ok",userName);
        messages.add("message1", msg);
        httpSession.setAttribute("messages",messages);
        logger.info("Exiting UserEditAction now...");
        return mapping.findForward("success");
    }
}
