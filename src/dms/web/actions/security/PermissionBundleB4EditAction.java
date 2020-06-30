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
 * $Id: PermissionBundleB4EditAction.java,v 20040220.11 2006/03/13 14:07:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessLevel;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
import dms.web.actionforms.security.PermissionBundleNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
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
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To populate permission_edit.jsp with the default data.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    12-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class PermissionBundleB4EditAction extends Action {
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
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ArrayList availableAccessLevelList=new ArrayList();
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try {
      logger.info("Entering PermissionBundleB4EditAction now...");
      httpSession = request.getSession(false);
      String permissionBundleName = (String)httpSession.getAttribute("radSelect");
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      DbsPermissionBundle permissionBundleToEdit=(DbsPermissionBundle)SearchUtil.findObject(dbsLibrarySession,DbsPermissionBundle.CLASS_NAME,permissionBundleName);
      if (permissionBundleToEdit!=null) {
        logger.debug("Permission Bundle to edit : "+permissionBundleToEdit.getName());
        String[] accLevelAvailable=DbsAccessLevel.getAllDefinedStandardPermissionNames(dbsLibrarySession);
        DbsAccessLevel dbsAccessLevel=permissionBundleToEdit.getAccessLevel();
        String[] accLevelSelected=dbsAccessLevel.getEnabledStandardPermissionNames(dbsLibrarySession);
        ArrayList accessLevelList=new ArrayList();
        boolean isAvailable=false;
        for(int indexAvailable=0;indexAvailable<accLevelAvailable.length;indexAvailable++) {
          isAvailable=false;
          for(int indexSelected=0;indexSelected<accLevelSelected.length;indexSelected++) {
            if(accLevelAvailable[indexAvailable].equals(accLevelSelected[indexSelected])){
              isAvailable=true;
            }
          }
          if(!isAvailable){    
            accessLevelList.add(accLevelAvailable[indexAvailable]);
          }
        }
        String[] newaccessAvailable=new String[accessLevelList.size()];
        for(int indexNew=0;indexNew<accessLevelList.size();indexNew++){
          newaccessAvailable[indexNew]=(String)accessLevelList.get(indexNew);
        }
        PermissionBundleNewEditForm permissionBundleNewEditForm=new PermissionBundleNewEditForm();
        permissionBundleNewEditForm.setTxtPermissionBundleName(permissionBundleToEdit.getName());
        permissionBundleNewEditForm.setTxaPermissionBundleDescription(permissionBundleToEdit.getAttribute(DbsPermissionBundle.DESCRIPTION_ATTRIBUTE).toString());
        permissionBundleNewEditForm.setLstAvailablePermission(newaccessAvailable);
        permissionBundleNewEditForm.setLstSelectedPermission(accLevelSelected);
        request.setAttribute("permissionBundleNewEditForm",permissionBundleNewEditForm);
        httpSession.setAttribute("permissionBundleNewEditFormOld",permissionBundleNewEditForm);
        logger.debug("Permission Bundle : " + permissionBundleNewEditForm);
      }else{
        ActionError editError=new ActionError("errors.permissionBundle.notfound",permissionBundleName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);            
        httpSession.setAttribute("errors",errors);
        return mapping.findForward("failure");
      }                 
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionBundleB4EditAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionBundleB4EditAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {      
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting PermissionBundleB4EditAction now...");            
    return mapping.findForward("success");
  }
}
