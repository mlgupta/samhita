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
 * $Id: B4FolderDocSelectNewFolderAction.java,v 1.4 2006/03/01 08:10:48 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* Java API */
import java.io.IOException;
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
 *	Purpose: Action called b4 creating new folder in folder_doc_select.jsp     
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    22-06-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4FolderDocSelectNewFolderAction extends Action  {
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
    logger.info("Entering B4FolderDocSelectNewFolderAction now...");
    logger.info("Exiting B4FolderDocSelectNewFolderAction now...");
    return mapping.findForward("success");
  }
}


