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
 * $Id: B4DocDoNothingAction.java,v 1.4 2006/03/01 07:23:24 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.web.actionforms.filesystem.DocLogListForm;
/*java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Dummy Action for docViewLog.jsp     
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    21-02-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocDoNothingAction extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering B4DocDoNothingAction now...");
    DocLogListForm docLogListForm=(DocLogListForm)form;
    try{
      logger.debug("docLogListForm: "+docLogListForm);
    }catch(Exception ex){
      logger.error("An Exception occurred in B4DocDoNothingAction... ");
      logger.error(ex.toString());
      ex.printStackTrace();
    }
    logger.info("Exiting B4DocDoNothingAction now...");
    return null;
  }
}
