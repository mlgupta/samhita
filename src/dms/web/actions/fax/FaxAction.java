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
 * $Id: FaxAction.java,v 20040220.13 2006/03/13 14:18:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.fax;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsExtendedUserProfile;
import dms.beans.DbsFileSystem;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.beans.filesystem.DocEventLogBean;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.scheduler.FileByteArray;
import dms.web.beans.scheduler.JobCreator;
import dms.web.beans.scheduler.JobScheduler;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/* Servlet API */
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
import org.quartz.Scheduler;
/**
 *	Purpose:             To send fax.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    02-07-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006 
 */
public class FaxAction extends Action {
  private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
  DbsLibrarySession dbsLibrarySession = null;
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    DbsDirectoryUser sender=null;
    String creator=null;
    String senderEmailAddress=null;
    String txtTo=null;
    String txtCompanyName=null;
    String txtFaxNumber=null;
    String txaComments=null;        
    String txtSendTime=null;
    String[] lstAttachment;        
    int day;
    int month;
    int year;
    int hours;
    int minutes;
    int seconds;
    String timezone; 
    String jobRetrialCount="0";
    String errorMesg="";
    String jobMaxCount=null;
    String jobRetrialInterval=null;
    String jobErrorMessage="NA";
    ActionErrors errors = new ActionErrors();
    ArrayList attachments=new ArrayList();
    DocEventLogBean docEventLogBean = new DocEventLogBean();
    try{
      logger.info("Entering FaxAction now...");        
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      DbsFileSystem fileSystem=new DbsFileSystem(dbsLibrarySession);
      /* obtain user's mail profile */
      DbsExtendedUserProfile emailProfile=SearchUtil.getEmailUserProfile(
                             dbsLibrarySession,dbsLibrarySession.getUser());
      creator=dbsLibrarySession.getUser().getName();
      if(emailProfile!=null){
        /* obtain mail address from mail profile */
        DbsAttributeValue mailAttr=emailProfile.getAttribute("EMAILADDRESS");
        if(mailAttr!=null){  
          senderEmailAddress= mailAttr.getString(dbsLibrarySession);
        }
      }
      /* fetch scheduler and faxtempdir from context */
      ServletContext context = httpSession.getServletContext();                    
      Scheduler sched=(Scheduler)context.getAttribute("scheduler");
      String faxTempDir=(String)context.getAttribute("faxtempdir");
      /* check for the scheduler status , is it up & working ? */
      if(sched.isShutdown()){
        /* prompt an error message if the scheduler is down */
        ActionError mailError=new ActionError("scheduler.stopped");
        errors.add(ActionErrors.GLOBAL_ERROR,mailError); 
      }else{
        /* create new fax jobs and schedule them for faxing */
        JobScheduler jobSched=new JobScheduler(sched);            
        /* obtain form specfic values */
        txtTo=((String)PropertyUtils.getSimpleProperty(form, "txtTo")).trim();
        txtCompanyName=((String)PropertyUtils.getSimpleProperty(form, "txtCompanyName")).trim();            
        txtFaxNumber=((String)PropertyUtils.getSimpleProperty(form, "txtFaxNumber")).trim();
        txaComments=((String)PropertyUtils.getSimpleProperty(form, "txaComments")).trim();                       
        logger.debug("Day is"+((String)PropertyUtils.getSimpleProperty(form, "day")).trim());
        day=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "day")).trim());
        month=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "month")).trim());
        year=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "year")).trim());
        hours=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "hours")).trim());
        minutes=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "minutes")).trim());
        seconds=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "seconds")).trim());
        timezone=((String)PropertyUtils.getSimpleProperty(form, "timezone")).trim();
        lstAttachment=((String[])PropertyUtils.getSimpleProperty(form, "lstAttachment"));
        /* for attachments create files from the cmsdk objects and add them to 
         * a file byte array */
        FileByteArray[] fileArr=null;
        if(lstAttachment!=null){
          fileArr=new FileByteArray[lstAttachment.length];
          for(int index = 0 ; index < lstAttachment.length ; index++){
            fileArr[index]=new FileByteArray();
            DbsPublicObject attachment=fileSystem.findPublicObjectByPath(
                                lstAttachment[index]).getResolvedPublicObject();
            InputStream fis=((DbsDocument)attachment).getContentStream();
            int fileSize=(int)((DbsDocument)attachment).getContentSize();
            byte[] fileBytes=new byte[fileSize];
            fis.read(fileBytes,0,fileSize);
            String fileName=((DbsDocument)attachment).getName();
            String mimeType=((DbsDocument)attachment).getFormat().getMimeType();
            fileArr[index].setFileBytes(fileBytes);
            fileArr[index].setFileName(fileName);
            fileArr[index].setMimeType(mimeType);
            attachments.add(fileArr[index]);
            /* log fax event for the document */
            docEventLogBean.logEvent(dbsLibrarySession,
                                    ((DbsDocument)attachment).getId(),
                                    "Doc scheduled for Faxing");
            /* it is mandatory to close the inputstream after use */
            fis.close();
          }
        }
        if(senderEmailAddress==null){
          senderEmailAddress=dbsLibrarySession.getUser().getName();
        }
        JobCreator jc = new JobCreator();
        TimeZone userTimeZone=TimeZone.getTimeZone(timezone);
        Date startTime=DateHelper.getDate(year,month,day,hours,minutes,seconds,
                                          userTimeZone);
        /* create a jobdatamap for the fax jobs */
        JobDataMap jobData=jc.createFaxData(txtTo,senderEmailAddress,
                              txtCompanyName,txtFaxNumber,txaComments,startTime,
                              timezone,dbsLibrarySession.getUser().getName(),
                              faxTempDir,jobRetrialCount,jobRetrialInterval,
                              jobMaxCount,jobErrorMessage,attachments);
        /* schedule the jobdatamap in the scheduler */
        jobSched.addJob(jobData);
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("fax.job.scheduled.ok");
        messages.add("message1", msg);
        saveMessages(request,messages);
      }           
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in FaxAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",
                                             dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in FaxAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    logger.info("Exiting FaxAction now...");        
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    return mapping.findForward("success");
  }
}
