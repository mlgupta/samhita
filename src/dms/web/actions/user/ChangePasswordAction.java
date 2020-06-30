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
 * $Id: ChangePasswordAction.java,v 20040220.10 2006/03/17 14:19:04 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.user.ChangePasswordForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To change the password for the logged in user.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class ChangePasswordAction extends Action {
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
    Logger logger = null;
    logger = Logger.getLogger("DbsLogger");
    ChangePasswordForm changePasswordForm=new ChangePasswordForm();
    DbsDirectoryUser userToEdit=null;
    String[] credentialManager=null;
    String userName=null;
    String password=null;
    String confirmPassword=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try {
      logger.info("Entering ChangePasswordAction now...");
      logger.debug("Initializing Variable ...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      userName=dbsLibrarySession.getUser().getName();
  
      userToEdit=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,userName);
      password=((String)PropertyUtils.getSimpleProperty(form, "txtPassword")).trim();
      confirmPassword=((String)PropertyUtils.getSimpleProperty(form, "txtConfirmPassword")).trim();
              
      String distinguishedName = userToEdit.getDistinguishedName();
      String credentialManagerName = userToEdit.getCredentialManager();
  
      Vector passwordParams = new Vector();
      passwordParams.addElement(credentialManagerName);
      passwordParams.addElement(distinguishedName);
      passwordParams.addElement(password);
      passwordParams.addElement(null);
      dbsLibrarySession.invokeServerMethod("DYNCredentialManagerSetPassword", passwordParams);
      logger.debug("Password Changed for user : " + userName);
  
      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("change.userPassword.success",userName);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);        
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in ChangePasswordAction... ");
      logger.error(dbsException.toString());
    }catch(Exception exception) {
      logger.error("An Exception occurred in ChangePasswordAction... ");
      logger.error(exception.toString());
    }
    logger.info("Exiting ChangePasswordAction now...");
    return mapping.findForward("success");
  }
}
