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
 * $Id: FolderDocNavigateAction.java,v 20040220.13 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
/* Java API */
import java.io.IOException;
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
 *	Purpose: To navigate across a list of folder(s) and doc(s) displaying fixed
 *           number of records at a time. 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   23-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocNavigateAction extends Action  {
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
    logger.info("Entering FolderDocNavigateAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    int pageNumber = 1;
    HttpSession httpSession = null;
    try{
      logger.debug("Navigating to different page");
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("folderDocListForm : " + folderDocListForm);
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      pageNumber = folderDocListForm.getTxtPageNo();
      logger.debug("pageNumber : " + pageNumber);
      folderDocInfo.setPageNumber(pageNumber);
      folderDocInfo.setNoReloadTree(true);
      httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");
      logger.debug("Navigating to different page complete");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocNavigateAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocNavigateAction now...");
    return mapping.findForward(forward);
  }
}
