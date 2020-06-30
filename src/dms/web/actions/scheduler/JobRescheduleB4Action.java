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
 * $Id: JobRescheduleB4Action.java,v 20040220.13 2006/03/13 14:18:39 suved Exp $
 *****************************************************************************
 */ 
package dms.web.actions.scheduler;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.scheduler.RescheduleForm;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.scheduler.SchedulerConstants;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
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
import org.apache.struts.util.MessageResources;
/* Quartz API */
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
/**
 *	Purpose: To prepopulate reschedule_job.jsp with job specific details.
 *  @author             Maneesh Mishra 
 *  @version            1.0
 * 	Date of creation:   07-04-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class JobRescheduleB4Action extends Action {
  
  private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss"; //also present in JobListBean
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
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    RescheduleForm rescheduleForm=new RescheduleForm();
    Scheduler jobScheduler=null;
    Date createDate = null;
    Date executionDate = null;
    String creator=null;
    JobDataMap jobData=null;
    String timezone=null;
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering JobRescheduleB4Action now..."); 
      httpSession = request.getSession(false); 
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      ServletContext context=httpSession.getServletContext();
      if(context.getAttribute("scheduler")!=null){
        jobScheduler=(Scheduler)context.getAttribute("scheduler");
      }else{
          
      }
      String jobWithGroupName= (String)httpSession.getAttribute("radSelect");    
      String[] jobInf=new String(jobWithGroupName).split(" ");            
      Trigger trigger[] = jobScheduler.getTriggersOfJob(jobInf[1],jobInf[0]);
      if(trigger==null || trigger.length==0){
        ActionError editError=new ActionError("job.dispatch.reschedule");
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
        saveErrors(request, errors);
        return (mapping.getInputForward());
      }
      JobDetail jobDetail=jobScheduler.getJobDetail(jobInf[1],jobInf[0]);
      jobData=jobDetail.getJobDataMap();
      createDate=(Date)jobData.get(SchedulerConstants.CREATE_TIME);
      executionDate=trigger[0].getStartTime();
      //timezone=jobData.getString(SchedulerConstants.TIMEZONE);
      //executionDate=DateHelper.convertToTimezone(executionDate,TimeZone.getTimeZone(timezone));
      creator=jobData.getString(SchedulerConstants.JOB_CREATOR);
      rescheduleForm.setTriggerName(trigger[0].getName());
      rescheduleForm.setTriggerType(trigger[0].getGroup());
      rescheduleForm.setTxtJobName(jobInf[1]);
      rescheduleForm.setTxtJobType(jobInf[0]);
      rescheduleForm.setTxtUser(creator);            
      rescheduleForm.setTxtCreateDate(DateHelper.format(createDate,DATE_FORMAT));
      rescheduleForm.setYear(new String().valueOf(DateHelper.getYear(
                                                             executionDate)));
      rescheduleForm.setMonth(new String().valueOf(DateHelper.getMonth(
                                                              executionDate)));
      rescheduleForm.setDay(new String().valueOf(DateHelper.getDay(
                                                            executionDate)));
      rescheduleForm.setHours(new String().valueOf(DateHelper.getHour(
                                                              executionDate)));
      rescheduleForm.setMinutes(new String().valueOf(DateHelper.getMinute(
                                                                executionDate)));
      rescheduleForm.setSeconds(new String().valueOf(DateHelper.getSecond(
                                                                executionDate)));
      rescheduleForm.setTimezone(TimeZone.getDefault().getID());
      
      request.setAttribute("RescheduleForm",rescheduleForm);
    }catch(Exception exception){
      logger.error("An Exception occurred in JobRescheduleB4Action... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()) {      
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }   
    logger.info("Exiting JobRescheduleB4Action now..."); 
    return mapping.findForward("success");
  }
}
