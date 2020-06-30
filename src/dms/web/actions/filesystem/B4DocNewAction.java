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
 * $Id: B4DocNewAction.java,v 1.2 2006/03/01 07:44:14 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.DocNewForm;
import dms.web.beans.exception.ExceptionBean;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: B4 Action for creating new document    
 *  @author              Rajan Kamal Gupta 
 *  @version             1.0
 * 	Date of creation:    30-05-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocNewAction extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering B4DocNewAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    String docType=null;
    DbsLibrarySession dbsLibrarySession = null;
    UserInfo userInfo = null;
    try{
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      DocNewForm docNewForm = (DocNewForm)form;
      if(docNewForm!=null){
        docType=docNewForm.getDocType();
      }
      logger.debug(docNewForm.getDocType());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4DocNewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting B4DocNewAction now...");
    return mapping.findForward(forward);
  }
}