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
 * $Id: SchedulerListAction.java,v 20040220.22 2006/05/19 06:23:53 suved Exp $
 *****************************************************************************
 */ 
package dms.web.actions.scheduler;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.scheduler.SchedulerListForm;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.scheduler.SchedulerConstants;
import dms.web.beans.scheduler.SearchJobs;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/* Quartz API */
import org.quartz.Scheduler;
/**
 *	Purpose: To display a list of mail and fax jobs for a given user.
 *  @author             Maneesh Mishra 
 *  @version            1.0
 * 	Date of creation:   02-04-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class SchedulerListAction extends Action {
    private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss"; //"MM/dd/yyyy HH:mm:ss z"; //also present in JobListBean
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
        UserInfo userInfo = null;
        HttpSession httpSession = null;        
        Scheduler jobScheduler=null;
        int numRecords=0;
        int pageNumber=1;
        int pageCount=0;
        String searchByUserName=null;
        String searchByJobType="ALL";
        String createFromDate=null;
        Date dCreateFromDate=null;
        String createToDate=null;
        Date dCreateToDate=null;
        String dispatchFromDate=null;
        Date dDispatchFromDate=null;
        String dispatchToDate=null;
        Date dDispatchToDate=null;
        Locale locale = getLocale(request);
        ArrayList jobs=new ArrayList();
        String retrial_count="0";
        // Validate the request parameters specified by the user
        SchedulerListForm schedulerForm=null;    
        ActionErrors errors = new ActionErrors();
        try{        
            logger.info("Entering SchedulerListAction now..."); 
            logger.debug("Fetching Job List ...");
            httpSession = request.getSession(false);
            userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            if(userInfo.isSystemAdmin() || userInfo.isAdmin())
            {
              request.setAttribute("isUserDisabled","false");
            }else
            {            
                httpSession.setAttribute("txtSearchByUserName",userInfo.getUserID());                 
                request.setAttribute("isUserDisabled","true");
            }
            UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
            numRecords= userPreferences.getRecordsPerPage();
            if(httpSession.getAttribute("pageNumber")!=null){
                pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
                httpSession.removeAttribute("pageNumber");
            }

            if(httpSession.getAttribute("messages")!=null){
                logger.debug("Saving action message in request stream");
                saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
                httpSession.removeAttribute("messages");
            } 
            if(httpSession.getAttribute("errors")!=null){
                logger.debug("Saving action errors in request stream");
                saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
                httpSession.removeAttribute("errors");
            }
            schedulerForm=new SchedulerListForm();
            dbsLibrarySession = userInfo.getDbsLibrarySession();
            ServletContext context=httpSession.getServletContext();
            jobScheduler=(Scheduler)context.getAttribute("scheduler");
            ActionMessages messages = new ActionMessages();
            ActionMessage msg =null;
            if(jobScheduler.isShutdown()){
                    schedulerForm.setIsSchedulerStopped("true");
                    schedulerForm.setJobType(SchedulerConstants.ALL_JOBS);
                    msg = new ActionMessage("scheduler.start");
                    messages.add("message1", msg);
                    saveMessages(request,messages);
                    
            }else{
                    schedulerForm.setIsSchedulerStopped("false");
                    if(httpSession.getAttribute("txtSearchByUserName")!=null){
                        searchByUserName=(String)httpSession.getAttribute("txtSearchByUserName");
                        httpSession.removeAttribute("txtSearchByUserName");
                        schedulerForm.setTxtSearchByUserName(searchByUserName);
                    }

                    
                    if(httpSession.getAttribute("txtCreateFromDate")!=null){
                        createFromDate=(String)httpSession.getAttribute("txtCreateFromDate");
                        dCreateFromDate=DateHelper.parse(createFromDate,DATE_FORMAT);
                        httpSession.removeAttribute("txtCreateFromDate");
                        schedulerForm.setTxtCreateFromDate(createFromDate);
                    }

                    if(httpSession.getAttribute("txtCreateToDate")!=null){
                        createToDate=(String)httpSession.getAttribute("txtCreateToDate");
                        dCreateToDate=DateHelper.parse(createToDate,DATE_FORMAT);
                        httpSession.removeAttribute("txtCreateToDate");
                        schedulerForm.setTxtCreateToDate(createToDate);
                    }

                    if(httpSession.getAttribute("txtDispatchFromDate")!=null){
                        dispatchFromDate=(String)httpSession.getAttribute("txtDispatchFromDate");
                        dDispatchFromDate=DateHelper.parse(dispatchFromDate,DATE_FORMAT);
                        httpSession.removeAttribute("txtDispatchFromDate");
                        schedulerForm.setTxtDispatchFromDate(dispatchFromDate);
                    }

                    if(httpSession.getAttribute("txtDispatchToDate")!=null){
                        dispatchToDate=(String)httpSession.getAttribute("txtDispatchToDate");
                        dDispatchToDate=DateHelper.parse(dispatchToDate,DATE_FORMAT);
                        httpSession.removeAttribute("txtDispatchToDate");
                        schedulerForm.setTxtDispatchToDate(dispatchToDate);
                    }
                    /* added by rajan on 14/07/2005 for jobTypeSearch starts here */
                    if(httpSession.getAttribute("cboSearchByJobType")!=null){
                        searchByJobType=(String)httpSession.getAttribute("cboSearchByJobType");
                        httpSession.removeAttribute("cboSearchByJobType");
                        
                        schedulerForm.setCboSearchByJobType(new String [] {searchByJobType});
                        schedulerForm.setJobType(schedulerForm.getCboSearchByJobType()[0]);
                    }else{
                      schedulerForm.setJobType(SchedulerConstants.ALL_JOBS);
                    }
                    /* added by rajan on 14/07/2005 for jobTypeSearch ends here */
                    schedulerForm.setTimezone(TimeZone.getDefault().getID());
                    //findJobs(Scheduler scheduler,String[] orderColumns,String creatorName,String jobType,Date fromCreateTime,Date toCreateTime,Date fromExecutionTime,Date toExecutionTime){
                    logger.debug("Time when search started "+new Date());
                    SearchJobs searchJobs=new SearchJobs();
                    jobs=searchJobs.findJobs(jobScheduler,new String[]{"executionTime"}/*null*/,searchByUserName,searchByJobType,dCreateFromDate,dCreateToDate,dDispatchFromDate,dDispatchToDate,pageNumber,numRecords);
                    pageCount=searchJobs.pageCount;
                    if(pageNumber>pageCount){
                    pageNumber=pageCount;
                }
                    
                  
            }
            
            /* commented by rajan on 14/07/2005 for jobTypeSearch starts here */
            /*if(httpSession.getAttribute("cboSearchByJobType")!=null){
              searchByJobType=(String)httpSession.getAttribute("cboSearchByJobType");
              httpSession.removeAttribute("cboSearchByJobType");
              schedulerForm.setCboSearchByJobType(new String[]{searchByJobType});
              logger.debug("schedulerForm.getCboSearchByJobType().length: "+schedulerForm.getCboSearchByJobType().length);
              for(int i=0;i<schedulerForm.getCboSearchByJobType().length;i++)
                logger.debug("schedulerForm.getCboSearchByJobType()["+i+"]: "+schedulerForm.getCboSearchByJobType()[i]);
              schedulerForm.setJobType(schedulerForm.getCboSearchByJobType()[0]);
            }else{
                schedulerForm.setCboSearchByJobType(new String[]{SchedulerConstants.ALL_JOBS});
                //logger.debug("schedulerForm.getCboSearchByJobType(): "+schedulerForm.getCboSearchByJobType());
                logger.debug("schedulerForm.getCboSearchByJobType().length: "+schedulerForm.getCboSearchByJobType().length);
                for(int i=0;i<schedulerForm.getCboSearchByJobType().length;i++)
                  logger.debug("schedulerForm.getCboSearchByJobType()["+i+"]: "+schedulerForm.getCboSearchByJobType()[i]);
                schedulerForm.setJobType(SchedulerConstants.ALL_JOBS);                    
            }*/

            
            schedulerForm.setTxtPageCount(new String().valueOf(pageCount));
            logger.debug("pageCount : " + pageCount);                        
            schedulerForm.setTxtPageNo(new String().valueOf(pageNumber));
            logger.debug("pageNumber : " + pageNumber);             
            request.setAttribute("jobs",jobs);
            logger.debug("schedulerForm : "+schedulerForm);
            request.setAttribute("SchedulerListForm",schedulerForm);
            logger.debug(jobs);            
            logger.debug("Number of records fetched : "+jobs.size());
            logger.debug("Fetching Job List Complete");
        }catch(Exception exception){
          logger.error("An Exception occurred in SchedulerListAction... ");
          logger.error("Fetching Job List Aborted");          
          logger.error(exception.toString());
          ActionError editError=new ActionError("errors.catchall",exception);
          errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }
        if(!errors.isEmpty()) {      
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        logger.info("Exiting SchedulerListAction now..."); 
        
        return mapping.findForward("success");
    }
}
