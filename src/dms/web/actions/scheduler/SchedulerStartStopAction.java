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
 * $Id: SchedulerStartStopAction.java,v 20040220.9 2006/03/13 14:18:39 suved Exp $
 *****************************************************************************
 */ 
package dms.web.actions.scheduler;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.scheduler.RescheduleForm;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/* Quartz API */
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
/**
 *	Purpose: To start/stop scheduler.
 *  @author             Maneesh Mishra 
 *  @version            1.0
 * 	Date of creation:   05-04-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class SchedulerStartStopAction extends Action {

  private static String START_ACTION="Start";
  DbsLibraryService dbsLibraryService = null;
  DbsCleartextCredential dbsCleartextCredential = null;
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    RescheduleForm rescheduleForm=new RescheduleForm();
    Scheduler jobScheduler=null;  
    String action=null;
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering SchedulerStartStopAction now..."); 
      httpSession = request.getSession(false); 
      if(httpSession.getAttribute("action")!=null){
        action=(String)httpSession.getAttribute("action");
        httpSession.removeAttribute("action");
      }
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      ServletContext context=httpSession.getServletContext();
      if(context.getAttribute("scheduler")!=null){
        jobScheduler=(Scheduler)context.getAttribute("scheduler");
      }else{
          
      }
      ActionMessages messages = new ActionMessages();
      ActionMessage msg =null;
      if(action.equals(START_ACTION)){
        context.removeAttribute("scheduler");
        String schedulerPath=(String)context.getAttribute("schedulerPath");
        StdSchedulerFactory stFact= new StdSchedulerFactory(schedulerPath);
        Scheduler sched=stFact.getScheduler();
        sched.start();
        context.setAttribute("scheduler",sched);
        msg = new ActionMessage("scheduler.start.ok");
      }else{
        jobScheduler.shutdown();
        msg = new ActionMessage("scheduler.stop.ok");
      }
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
    }catch(Exception exception){
      logger.error("An Exception occurred in SchedulerStartStopAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()){
      httpSession.setAttribute("errors",errors);
      return mapping.findForward("success");
    }  
    logger.info("Exiting SchedulerStartStopAction now..."); 
    return mapping.findForward("success");
  }
}
