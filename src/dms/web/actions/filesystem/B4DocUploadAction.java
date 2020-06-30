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
 * $Id: B4DocUploadAction.java,v 20040220.7 2006/03/01 07:58:05 suved Exp $
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: B4 Action for fetching doc_upload_new.jsp.    
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    02-05-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocUploadAction extends Action  {
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
    logger.info("Entering B4DocUploadAction now...");
    logger.info("Serving JSP for Upload");
    ActionMessages actionMessages = new ActionMessages();
    ActionMessage actionMessage = new ActionMessage("msg.SelectFileToUpload");
    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
    saveMessages(request,actionMessages);
    logger.info("Serving JSP for Upload Complete");
    logger.info("Entering B4DocUploadAction now...");
    return mapping.findForward("success");
  }
}
