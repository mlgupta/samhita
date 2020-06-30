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
 * $Id: LogoutAction.java,v 20040220.6 2006/03/13 14:18:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.loginout; 
/* dms package references */
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Enable a user to logout
 *  @author             Jeetendra Prasad 
 *  @version            1.0
 * 	Date of creation:   16-02-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class LogoutAction extends Action  {
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
    logger.info("Entering LogoutAction now...");
    //variable declaration
    ExceptionBean exceptionBean = null;        
    HttpSession httpSession = null;        
    try{
      httpSession = request.getSession(false);
      logger.debug("httpSession id to be invalidated is: "+httpSession.getId());
      ServletContext context=request.getSession().getServletContext();
      logger.debug("httpSession id  removed from Context "+httpSession.getId());
      context.removeAttribute(((UserInfo)httpSession.getAttribute("UserInfo")).getUserID());
      httpSession.invalidate();
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in LogoutAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting LogoutAction now...");
    return mapping.findForward("success");
  }
}
    
