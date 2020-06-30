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
 * $Id: StatusBarAction.java,v 1.0 2006/05/05 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* Java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose           : To display messages in flat_status_bar.jsp
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 05-05-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class StatusBarAction extends Action  {
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
    String target = "success";
    logger.info("Entering StatusBarAction now...");
    HttpSession httpSession = request.getSession(false);

    ActionErrors actionErrors = (ActionErrors)httpSession.getAttribute("ActionErrors");
    ActionMessages actionMessages = (ActionMessages)httpSession.getAttribute("ActionMessages");
    /* display action errors if any */
    if(actionErrors != null){
      logger.debug("Saving action error in request stream");
      saveErrors(request,actionErrors);
      httpSession.removeAttribute("ActionErrors");
    }
    /* display action messages if any */
    if( actionMessages != null ){
      logger.debug("Saving action message in request stream");
      saveMessages(request,actionMessages);
      httpSession.removeAttribute("ActionMessages");
    }
    logger.info("Exiting StatusBarAction now...");
    return mapping.findForward(target);
  }

}