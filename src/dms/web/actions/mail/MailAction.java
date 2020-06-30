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
 * $Id: MailAction.java,v 20040220.17 2006/03/13 14:16:14 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.mail;
// dms package references
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
//Java API
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
//Servlet API
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
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
// Quartz API
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
/**
 *	Purpose: To send mails.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    03-04-2004       
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   02-03-2006 
 */
public class MailAction extends Action {
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
    String txtCc=null;
    String txtBCc=null;
    String txtSubject=null;
    String txaMail=null;
    String txtSendTime=null;
    String[] lstAttachment;
    String jobRetrialCount="0";
    String errorMesg="";
    String jobMaxCount=null;
    String jobRetrialInterval=null;
    String jobErrorMessage="NA";
    int day;
    int month;
    int year;
    int hours;
    int minutes;
    int seconds;
    String timezone;        
    ActionErrors errors = new ActionErrors();
    ArrayList attachments=new ArrayList();
    DocEventLogBean docEventLogBean = new DocEventLogBean();
    try{
      logger.info("Entering MailAction now...");
      httpSession = request.getSession(false);
      jobMaxCount=(String)httpSession.getServletContext().getAttribute("jobRetrialCount");
      jobRetrialInterval=(String)httpSession.getServletContext().getAttribute("jobRetrialInterval");
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      DbsFileSystem fileSystem=new DbsFileSystem(dbsLibrarySession);
      DbsExtendedUserProfile emailProfile=SearchUtil.getEmailUserProfile(dbsLibrarySession,dbsLibrarySession.getUser());
      creator=dbsLibrarySession.getUser().getName();
      if(emailProfile!=null){
        DbsAttributeValue mailAttr=emailProfile.getAttribute("EMAILADDRESS");
        if(mailAttr!=null){  
          senderEmailAddress= mailAttr.getString(dbsLibrarySession);
          if(senderEmailAddress!=null){
            ServletContext context = httpSession.getServletContext();
            String smtpHost=(String)context.getAttribute("smtpHost");
            Scheduler sched=(Scheduler)context.getAttribute("scheduler");
            if(sched.isShutdown()){
              ActionError mailError=new ActionError("scheduler.stopped");
              errors.add(ActionErrors.GLOBAL_ERROR,mailError); 
            }else{
              JobScheduler jobSched=new JobScheduler(sched);            
      
              txtTo=((String)PropertyUtils.getSimpleProperty(form, "txtTo")).trim();
              txtCc=((String)PropertyUtils.getSimpleProperty(form, "txtCc")).trim();            
              txtBCc=((String)PropertyUtils.getSimpleProperty(form, "txtBCc")).trim();
              txtSubject=((String)PropertyUtils.getSimpleProperty(form, "txtSubject")).trim();
              txaMail=((String)PropertyUtils.getSimpleProperty(form, "txaMail")).trim();
              logger.debug("Day is"+((String)PropertyUtils.getSimpleProperty(form, "day")).trim());
              day=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "day")).trim());
              month=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "month")).trim());
              year=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "year")).trim());
              hours=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "hours")).trim());
              minutes=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "minutes")).trim());
              seconds=Integer.parseInt(((String)PropertyUtils.getSimpleProperty(form, "seconds")).trim());
              timezone=((String)PropertyUtils.getSimpleProperty(form, "timezone")).trim();
              String[] cc=txtCc.split(",");
              String[] bcc=txtBCc.split(",");
              for(int index=0 ; index < cc.length ; index++){
                cc[index]=cc[index].trim();
              }
              for(int index=0 ; index < bcc.length ; index++){
                bcc[index]=bcc[index].trim();
              }
              lstAttachment=((String[])PropertyUtils.getSimpleProperty(form, "lstAttachment"));
              FileByteArray[] fileArr=null;
              if(lstAttachment!=null){
                fileArr=new FileByteArray[lstAttachment.length];
                for(int index = 0 ; index < lstAttachment.length ; index++){
                  fileArr[index]=new FileByteArray();
                  DbsPublicObject attachment=fileSystem.findPublicObjectByPath(lstAttachment[index]).getResolvedPublicObject();
                  InputStream fileInputStream=((DbsDocument)attachment).getContentStream();
                  int fileSize=(int)((DbsDocument)attachment).getContentSize();
                  byte[] fileBytes=new byte[fileSize];
                  fileInputStream.read(fileBytes,0,fileSize);
                  String fileName=((DbsDocument)attachment).getName();
                  String mimeType=((DbsDocument)attachment).getFormat().getMimeType();
                  fileArr[index].setFileBytes(fileBytes);
                  fileArr[index].setFileName(fileName);
                  fileArr[index].setMimeType(mimeType);
                  attachments.add(fileArr[index]);
                  docEventLogBean.logEvent(dbsLibrarySession,((DbsDocument)attachment).getId(),"Doc scheduled for Mailing");
                  fileInputStream.close();
                }
              }
              JobCreator jc = new JobCreator();
              //Date startTime=DateHelper.parse(txtSendTime,DATE_FORMAT);
              TimeZone userTimeZone=TimeZone.getTimeZone(timezone);
              Date startTime=DateHelper.getDate(year,month,day,hours,minutes,seconds,userTimeZone);
              /*if(startTime.compareTo(new Date())<=0){
                  startTime=null;
              }*/
              JobDataMap jobData=jc.createMailData(txtTo,senderEmailAddress,txtCc,txtBCc,txtSubject,txaMail,creator,smtpHost,startTime,timezone,jobRetrialCount,jobRetrialInterval,jobMaxCount,jobErrorMessage,attachments);
              jobSched.addJob(jobData);
              ActionMessages messages = new ActionMessages();
              ActionMessage msg = new ActionMessage("mail.job.scheduled.ok");
              messages.add("message1", msg);
              saveMessages(request,messages);
            }
          }else{
            ActionError mailError=new ActionError("email.address.notfound",creator);
            errors.add(ActionErrors.GLOBAL_ERROR,mailError);
          }
        }else{
          ActionError mailError=new ActionError("email.address.notfound",creator);
          errors.add(ActionErrors.GLOBAL_ERROR,mailError);
        }
      }else{
        ActionError mailError=new ActionError("email.profile.notfound",creator);
        errors.add(ActionErrors.GLOBAL_ERROR,mailError);
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in MailAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in MailAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting MailAction now...");
    return mapping.findForward("success");
  }
}
