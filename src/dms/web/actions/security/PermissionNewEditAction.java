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
 * $Id: PermissionNewEditAction.java,v 20040220.16 2006/03/13 14:19:23 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security;
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlEntryDefinition;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.PermissionBundleNewEditForm;
import dms.web.actionforms.security.PermissionForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
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
 *	Purpose: To save permission.jsp with the specified Access Control Entry for ACL.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 
public class PermissionNewEditAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
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
    //initializing logger
    Logger logger = Logger.getLogger("DbsLogger");
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    ActionError editError=null;
    String permissionBundleName=null;
    String permissionBundleDescription=null;
    DbsTransaction newEditTransaction=null;
    String[] selectedPermissionBundle=null;
    String granteeType=null;
    String granteeName=null;
    String aclName=null;
    String radPermission=null;
    String isNew=null;
    String isNewAcl=null;
    String aceIndex=null;
    String[] selectedPermissionBundleOld=null;
    DbsPermissionBundle permissionBundleToAdd=null;
    DbsAccessControlEntryDefinition newEditAceDef=null;
    PermissionBundleNewEditForm permissionBundleNewEditFormOld = null;
    PermissionForm permissionBundleNewEditForm=null;
    try{ 
      logger.info("Entering PermissionNewEditAction now...");
      logger.debug("Saving data for Permission ");            
      logger.debug("Form Type="+form.getClass());
      permissionBundleNewEditForm = (PermissionForm)form;
      selectedPermissionBundle=(String[])PropertyUtils.getSimpleProperty(form, "lstSelectedPermissionBundle");
      granteeType=(String)PropertyUtils.getSimpleProperty(form, "granteeType");
      granteeName=(String)PropertyUtils.getSimpleProperty(form, "granteeName");
      aclName=(String)PropertyUtils.getSimpleProperty(form, "aclName");
      radPermission=(String)PropertyUtils.getSimpleProperty(form, "radPermission");
      isNew=(String)PropertyUtils.getSimpleProperty(form, "isNew");
      isNewAcl=(String)PropertyUtils.getSimpleProperty(form, "isNewAcl");            
      aceIndex=(String)PropertyUtils.getSimpleProperty(form, "index");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      newEditTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      DbsAccessControlList aclToEdit=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclName);            
      newEditAceDef=new DbsAccessControlEntryDefinition(dbsLibrarySession);            
      int totalCount =0;
      if(selectedPermissionBundle!=null){
        totalCount=selectedPermissionBundle.length;
      }
      for(int indexSelected=0;indexSelected<totalCount;indexSelected++){       
        permissionBundleToAdd=(DbsPermissionBundle)SearchUtil.findObject(dbsLibrarySession,DbsPermissionBundle.CLASS_NAME,selectedPermissionBundle[indexSelected]);
        newEditAceDef.addPermissionBundle(permissionBundleToAdd);
      }
      if(granteeType.equals("User")){
        DbsDirectoryUser userGrantee=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,granteeName);
        newEditAceDef.setGrantee(userGrantee);
      }else if(granteeType.equals("Group")){
        DbsDirectoryGroup groupGrantee=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,granteeName);
        newEditAceDef.setGrantee(groupGrantee);
      }else if(granteeType.equals("Acl")){
        DbsAccessControlList aclGrantee=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,granteeName);
      }
      if(radPermission.equals("1")){
        if(isNew.equals("false")){
          DbsAccessControlEntry aceToEdit=aclToEdit.getAccessControlEntrys(Integer.parseInt(aceIndex));
          aceToEdit.setAttribute(DbsAccessControlEntry.GRANTED_ATTRIBUTE,DbsAttributeValue.newAttributeValue(true));
          aclToEdit.updateAccessControlEntry(aceToEdit,newEditAceDef);
        }else{
          aclToEdit.grantAccess(newEditAceDef);
        }
      }else if(radPermission.equals("0")){
        if(isNew.equals("false")){
          DbsAccessControlEntry aceToEdit=aclToEdit.getAccessControlEntrys(Integer.parseInt(aceIndex));
          aceToEdit.setAttribute(DbsAccessControlEntry.GRANTED_ATTRIBUTE,DbsAttributeValue.newAttributeValue(false));
          aclToEdit.updateAccessControlEntry(aceToEdit,newEditAceDef);
        }else{
          aclToEdit.revokeAccess(newEditAceDef);
        }
      }        
      dbsLibrarySession.completeTransaction(newEditTransaction);
      logger.debug("Transaction completed");
      logger.debug("permissionBundleNewEditForm : " + permissionBundleNewEditForm);
      logger.debug("Data saved for Acl : " + aclName);
      newEditTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionNewEditAction... ");
      logger.error("Saving Permission Aborted."); 
      logger.error(dbsException.toString());
      if(dbsException.getErrorCode() == 30065){
        editError=new ActionError("errors.30065.insufficient.access.to.add.accessControlEntries");
      //editError=new ActionError("errors.30065.insufficient.access.to.add.accessControlEntries",dbsException.getErrorMessage());  
      }
      if(dbsException.getErrorCode() == 30040){
        editError=new ActionError("errors.30040.insufficient.access.to.update.SystemObj");
      //editError=new ActionError("errors.30065.insufficient.access.to.add.accessControlEntries",dbsException.getErrorMessage());  
      }else{
        editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      }
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionNewEditAction... ");
      logger.error("Saving Permission Aborted.");                  
      logger.error(exception.toString());
      editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(newEditTransaction!=null){
          dbsLibrarySession.abortTransaction(newEditTransaction);
          logger.debug("Transaction Aborted ");
        }
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in PermissionNewEditAction... ");
        logger.error(nestedException.toString());
      }
      newEditTransaction=null;
    }//end finally
    if (!errors.isEmpty()){
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    ActionMessages messages = new ActionMessages();
    if(isNewAcl.equals("true")){
      ActionMessage msg = new ActionMessage("acl.new.ok",aclName);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
    }else{
      if(isNew.equals("true")){
        ActionMessage msg = new ActionMessage("ace.new.ok",aclName);
        messages.add("message1", msg);
        httpSession.setAttribute("messages",messages);
      }else{
        ActionMessage msg = new ActionMessage("ace.edit.ok",aclName);
        messages.add("message1", msg);
        httpSession.setAttribute("messages",messages);  
      }
    }
    httpSession.setAttribute("radSelect",aclName);
    logger.info("Exiting PermissionNewEditAction now...");
    return mapping.findForward("success");
  }
}
