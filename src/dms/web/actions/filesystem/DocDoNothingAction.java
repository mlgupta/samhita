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
 * $Id: DocDoNothingAction.java,v 1.6 2006/03/16 06:30:03 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.web.actionforms.filesystem.ShowContentForm;
/*java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Dummy action called for doc_generate_url.jsp
 *  @author             Suved Mishra
 *  @version            1.0
 * 	Date of creation:   20-12-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006  
 */
public class DocDoNothingAction extends Action{
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering DocDoNothingAction now...");
    ShowContentForm showContentForm=null;
    try{
      showContentForm=(ShowContentForm)form;
      logger.debug("showContentForm: "+showContentForm);
    }catch(Exception ex){
      logger.error("An Exception occurred in DocDoNothingAction... ");
      logger.error(ex.toString());
      ex.printStackTrace();
    }
    logger.info("Exiting DocDoNothingAction now...");
    return null;
  }
  
}
