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
 * $Id: UserB4NewAction.java,v 20040220.11 2006/03/17 12:58:30 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/*dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.user.UserNewEditForm;
import dms.web.beans.user.UserInfo;
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
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To populate user_new.jsp with the default data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :   Suved Mishra   
 * 	Last Modified Date:  03-03-2006  
 */
public class UserB4NewAction extends Action {
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
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    String[] credentialManager=null; 
    ArrayList memberList=new ArrayList();
    UserNewEditForm userNewEditForm=new UserNewEditForm();
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering UserB4NewAction now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      credentialManager=new String[1];
      credentialManager[0]=dbsLibrarySession.getUser().getCredentialManager();
      userNewEditForm.setRadStatus("3");
      userNewEditForm.setTxtAccessControlList(dbsLibrarySession.getUser().getAcl().getName());
      userNewEditForm.setRadQuota("1");            
      userNewEditForm.setTxtQuota("30"); 
      userNewEditForm.setCboCredentialManager(credentialManager);
      userNewEditForm.setCboLanguage(new String[]{""});
      userNewEditForm.setCboCharacterSet(new String[]{""});
      userNewEditForm.setCboLocale(new String[]{""});
      userNewEditForm.setCboTimeZone(new String[]{""});      
      request.setAttribute("userNewEditForm",userNewEditForm);
      logger.debug("userNewEditForm : " + userNewEditForm); 
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in UserB4NewAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in UserB4NewAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting UserB4NewAction now...");
    return mapping.findForward("success");
  }
}
