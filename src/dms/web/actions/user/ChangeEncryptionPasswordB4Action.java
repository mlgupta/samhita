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
 * $Id: ChangeEncryptionPasswordB4Action.java,v 20040220.5 2006/03/13 14:19:04 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.user.ChangeEncryptionPasswordForm;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
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
 *	Purpose: To display change_encryption_passwrod.jps.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    15-04-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */

public class ChangeEncryptionPasswordB4Action extends Action{
  DbsLibraryService dbsLibraryService = null;
  DbsCleartextCredential dbsCleartextCredential = null;
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
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    Logger logger = null;
    logger = Logger.getLogger("DbsLogger");
    ChangeEncryptionPasswordForm changeEncryptionPasswordForm=new ChangeEncryptionPasswordForm();        
    String password=null;
    String confirmPassword=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try {
      logger.info("Entering ChangeEncryptionPasswordB4Action now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      changeEncryptionPasswordForm.setTxtPassword("");
      changeEncryptionPasswordForm.setTxtConfirmPassword("");
      request.setAttribute("changeEncryptionPasswordForm",changeEncryptionPasswordForm);
    }catch(Exception exception){
      logger.error("An Exception occurred in ChangeEncryptionPasswordB4Action... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting ChangeEncryptionPasswordB4Action now...");
    return mapping.findForward("success");
  }
}
