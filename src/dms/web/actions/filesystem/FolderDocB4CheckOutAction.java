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
 * $Id: FolderDocB4CheckOutAction.java,v 20040220.7 2006/03/17 12:37:20 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.web.actionforms.filesystem.FolderDocCheckOutForm;
/* Java API */
import java.io.IOException;
/* Servlet API */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Action called to display doc_checkout.jsp for a versioned document 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   03-04-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class FolderDocB4CheckOutAction extends Action  {
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
    logger.info("Entering FolderDocB4CheckOutAction now...");
    FolderDocCheckOutForm folderDocCheckOutForm = (FolderDocCheckOutForm)form;
    logger.debug("folderDocCheckOutForm : " + folderDocCheckOutForm);
    logger.info("Exiting FolderDocB4CheckOutAction now...");
    return mapping.findForward("success");
  }
}
