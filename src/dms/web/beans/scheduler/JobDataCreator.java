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
 * $Id: JobDataCreator.java,v 20040220.10 2006/03/14 06:13:07 suved Exp $
 *****************************************************************************
 */ 
package dms.web.beans.scheduler;
/* Java API */
import java.util.ArrayList;
import java.util.Date;
/* Quartz API */
import org.quartz.JobDataMap;
/**
 *	Purpose: This class is used to create JobDataMap for MAIL and FAX jobs.Each job has its
 *           its own JobDataMap which contains properties as to,from,cc,bcc,attachments in case of
 *           MAIL jobs and fax no. ,creator and file to fax in case of FAX jobs.
 *           
 * @author              Mishra Maneesh
 * @version             1.0
 * 	Date of creation:   10-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class JobDataCreator 
{
   public JobDataMap createMailData(Date startTime,ArrayList fileList)
    {   String message="This is just a sample message for testing of scheduler.";
        JobDataMap mailData=new JobDataMap();
        mailData.put(SchedulerConstants.JOB_CREATOR,"maneesh");
        mailData.put(SchedulerConstants.JOB_TYPE,"MAIL");
        mailData.put(SchedulerConstants.EXECUTION_TIME,startTime);
        mailData.put(SchedulerConstants.CREATE_TIME,new Date());
        mailData.put(SchedulerConstants.PROP_SMTP_HOST,"linux.dbsentry.com");
        mailData.put(SchedulerConstants.PROP_RECIPIENT,"system@linux.dbsentry.com");
        mailData.put(SchedulerConstants.PROP_SENDER,"guest@linux.dbsentry.com");
        mailData.put(SchedulerConstants.PROP_SUBJECT,"subject for test scheduler");
        mailData.put(SchedulerConstants.PROP_MESSAGE,message);
        mailData.put(SchedulerConstants.PROP_ATTACHMENTS,fileList);        
        return mailData;
    }

    public JobDataMap createPrintData()
    {
        return null;
    }

    public JobDataMap createFaxData(Date startTime,ArrayList fileList)
    {
        JobDataMap faxData=new JobDataMap();
        faxData.put(SchedulerConstants.JOB_CREATOR,"maneesh");
        faxData.put(SchedulerConstants.JOB_TYPE,"FAX");
        faxData.put(SchedulerConstants.EXECUTION_TIME,startTime);
        faxData.put(SchedulerConstants.CREATE_TIME,new Date());
        faxData.put(SchedulerConstants.PROP_FAX_NUMBER,"3119867");        
        faxData.put(SchedulerConstants.PROP_SENDER,"guest@linux.dbsentry.com");        
        faxData.put(SchedulerConstants.PROP_ATTACHMENTS,fileList);        
        return faxData;
    }
    
}
