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
 * $Id: DocNothingAction.java,v 1.3 2006/03/16 06:25:28 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.web.actionforms.filesystem.LinkDetailsForm;
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
 * Purpose                : A dummy action.
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 15-12-2005
 * Last Modified by       : 
 * Last Modification Date : 
 */
public class DocNothingAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.debug("Entering DocNothingAction now...");
    
    LinkDetailsForm linkDetailsForm=(LinkDetailsForm)form;
    
    try{
      //logger.debug("linkDetailsForm: "+linkDetailsForm);
      logger.debug("Exiting DocNothingAction now...");
    }catch(Exception ex){
      logger.debug("Error!!!");
      ex.printStackTrace();
    }
    return null;
  }

}