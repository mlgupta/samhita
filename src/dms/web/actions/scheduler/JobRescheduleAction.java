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
 * $Id: JobRescheduleAction.java,v 20040220.15 2006/03/13 14:18:39 suved Exp $
 *****************************************************************************
 */ 
package dms.web.actions.scheduler;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
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
import org.apache.commons.beanutils.PropertyUtils;
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
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
/**
 *	Purpose: To reschedule selected job from scheduler job-list.
 *  @author             Maneesh Mishra 
 *  @version            1.0
 * 	Date of creation:   03-04-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class JobRescheduleAction extends Action {
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
    UserInfo userInfo = null;
    HttpSession httpSession = null;       
    Scheduler jobScheduler=null;
    String creator=null;
    Trigger newTrigger=null;
    String newTriggerName=null;
    String oldTriggerName=null;
    String triggerType=null;
    String jobName=null;
    String jobType=null;
    int day;
    int month;
    int year;
    int hours;
    int minutes;
    int seconds;
    String timezone;
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering JobRescheduleAction now..."); 
      oldTriggerName=(String)PropertyUtils.getSimpleProperty(form, "triggerName");
      triggerType=(String)PropertyUtils.getSimpleProperty(form, "triggerType");
      creator=(String)PropertyUtils.getSimpleProperty(form, "txtUser");
      jobName=(String)PropertyUtils.getSimpleProperty(form, "txtJobName");
      jobType=(String)PropertyUtils.getSimpleProperty(form, "txtJobType");
      day=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "day")).trim());
      month=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "month")).trim());
      year=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "year")).trim());
      hours=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "hours")).trim());
      minutes=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "minutes")).trim());
      seconds=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "seconds")).trim());
      timezone=((String)PropertyUtils.getSimpleProperty(form, "timezone")).trim();            httpSession = request.getSession(false); 
      ServletContext context=httpSession.getServletContext();
      if(context.getAttribute("scheduler")!=null){
        jobScheduler=(Scheduler)context.getAttribute("scheduler");
      }else{
          
      }
      logger.debug("Scheduler Acquired");
      TimeZone userTimeZone=TimeZone.getTimeZone(timezone);
      Date startTime=DateHelper.getDate(year,month,day,hours,minutes,seconds,userTimeZone);
      JobDataMap jobData=jobScheduler.getJobDetail(jobName,jobType).getJobDataMap();
      logger.debug("JobData Acquired");
      Date dateOfSubmission=(Date)jobData.get(SchedulerConstants.CREATE_TIME);
      logger.debug("Job Create Time"+dateOfSubmission);
     // String namePrefix=creator+DateHelper.format(dateOfSubmission,"HH:mm:ss--yyyy-MM-dd-z");            
      String namePrefix=creator+DateHelper.format(new Date(),"HH:mm:ss--yyyy-MM-dd-z");            
      newTriggerName=SchedulerConstants.TRIGGER_NAME_SUFFIX+namePrefix;
      if(startTime.compareTo(new Date())<=0){
        newTrigger=new SimpleTrigger(newTriggerName,triggerType);
      }else{
        newTrigger=new SimpleTrigger(newTriggerName,triggerType,startTime);               
      }
      jobData.put(SchedulerConstants.EXECUTION_TIME,startTime);
      logger.debug(timezone);
      jobData.put(SchedulerConstants.TIMEZONE,timezone);
      JobDetail jobDetail= jobScheduler.getJobDetail(jobName,jobType);
      jobDetail.setJobDataMap(jobData);
      newTrigger.setJobGroup(jobType);
      newTrigger.setJobName(jobName);
      //jobScheduler.rescheduleJob(oldTriggerName,triggerType,newTrigger);            
      jobScheduler.deleteJob(jobName,jobType);   
      jobScheduler.scheduleJob(jobDetail,newTrigger);
      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("job.reschedule.ok",jobName,jobType);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
    }catch(Exception exception){
      logger.error("An Exception occurred in JobRescheduleAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()) {      
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }   
    logger.info("Exiting JobRescheduleAction now...");
    return mapping.findForward("success");
  }
}
