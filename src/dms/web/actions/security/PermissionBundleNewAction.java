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
 * $Id: PermissionBundleNewAction.java,v 20040220.12 2006/03/13 14:07:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessLevel;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
import dms.beans.DbsPermissionBundleDefinition;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.PermissionBundleNewEditForm;
import dms.web.beans.user.UserInfo;
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
 *	Purpose: To save permission_bundle_new.jsp with the specified Permission Bundle data.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    25-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class PermissionBundleNewAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
 /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing Logger
    Logger logger = Logger.getLogger("DbsLogger");
    //initializing variables
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    String permissionBundleName=null;
    String permissionBundleDescription=null;
    DbsTransaction newTransaction=null;
    String[] selectedPermissions=null;
    DbsPermissionBundleDefinition permissionBundleDef=null;
    PermissionBundleNewEditForm permissionBundleNewEditForm;
    try{ 
      logger.info("Entering PermissionBundleNewAction now...");
      permissionBundleName = ((String)PropertyUtils.getSimpleProperty(form, "txtPermissionBundleName")).trim();  
      logger.debug("Saving data for New Permission Bundle : " + permissionBundleName);
      permissionBundleNewEditForm = (PermissionBundleNewEditForm)form;
      permissionBundleDescription=((String)PropertyUtils.getSimpleProperty(form, "txaPermissionBundleDescription")).trim();
      selectedPermissions=(String[])PropertyUtils.getSimpleProperty(form, "lstSelectedPermission");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      permissionBundleDef=new DbsPermissionBundleDefinition(dbsLibrarySession);
      DbsAccessLevel accessLevelToEdit=new DbsAccessLevel();
      newTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      int totalCount =0;
      if(selectedPermissions!=null){
        totalCount=selectedPermissions.length;
      }
      for(int indexSelected=0;indexSelected<totalCount;indexSelected++){
        long permission=accessLevelToEdit.getLong(selectedPermissions[indexSelected]);
        accessLevelToEdit.enableStandardPermission(permission); 
      }         
      permissionBundleDef.setAccessLevel(accessLevelToEdit);
      DbsPermissionBundle newPermissionBundle=dbsLibrarySession.createSystemObject(permissionBundleDef);
      newPermissionBundle.setAttribute(DbsPermissionBundle.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(permissionBundleName));
      newPermissionBundle.setAttribute(DbsPermissionBundle.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(permissionBundleDescription));
      dbsLibrarySession.completeTransaction(newTransaction);
      logger.debug("permissionBundleNewEditForm : " + permissionBundleNewEditForm);
      logger.debug("Data saved for Permission Bundle : " + permissionBundleName);
      logger.debug("Transaction completed");
      newTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionBundleNewAction... ");
      logger.error("Permission Bundle New Action Aborted.");                  
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError); 
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionBundleNewAction... ");
      logger.error("Permission Bundle New Action Aborted.");                  
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError); 
    }finally{
      try{
        if(newTransaction!=null){
          dbsLibrarySession.abortTransaction(newTransaction);
          logger.debug("Transaction aborted.");
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in PermissionBundleNewAction... ");
        logger.error(nestedException.toString());
      }
      newTransaction=null;
    }//end finally
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    ActionMessages messages = new ActionMessages();
    ActionMessage msg = new ActionMessage("permissionBundle.new.ok",permissionBundleName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);
    logger.info("Exiting PermissionBundleNewAction now...");
    return mapping.findForward("success");
  }
}
