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
 * $Id: JobListBean.java,v 20040220.8 2006/03/14 06:13:07 suved Exp $
 *****************************************************************************
 */ 
package dms.web.beans.scheduler;
/* Java API */
import java.util.Date;
/**
 *	Purpose: This class represents each row in job listing.
 *           It also provides getter and setter methods for its row elements.
 * @author              Mishra Maneesh
 * @version             1.0
 * 	Date of creation:   10-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class JobListBean
{
    public static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss z"; //also present in SchedulerListAction
    public String jobName;
    public String creatorName;
    public String jobType;
    public Date createTime;
    public Date executionTime;
    private String retrialCount;
    private String jobRetrialInterval;
    private String jobMaxCount;
    private String jobErrorMessage;

    /**
     * Purpose   : To create a Jobrow with the given parameters.
     * @param    jobName - Name of job.
     * @param    creatorName - Name of creator of job.    
     * @param    jobType - Type of job ie mail,fax or print.
     * @param    createTime - Creation time of job.    
     * @param    executionTime - Execution time of job.       
     */     
    public JobListBean(String jobName,String creatorName,String jobType,Date createTime,Date executionTime,String retrialCount,String jobRetrialInterval,String jobMaxCount,String jobErrorMessage){
        this.jobName=jobName;
        this.creatorName=creatorName;
        this.jobType=jobType;
        this.createTime=createTime;
        this.executionTime=executionTime;
        this.retrialCount=retrialCount;
        this.jobRetrialInterval=jobRetrialInterval;
        this.jobErrorMessage=jobErrorMessage;
        this.jobMaxCount=jobMaxCount;
    }

  /**
   * getter method for jobName
   * @return String jobName
   */
    public String getJobName(){
        return jobName;
    }

  /**
   * setter method for jobName
   * @param newJobName
   */
    public void setJobName(String newJobName){
        jobName = newJobName;
    }

  /**
   * getter method for creatorName 
   * @return String creatorName
   */
    public String getCreatorName(){
        return creatorName;
    }

  /**
   * setter method for creatorName
   * @param newCreatorName
   */
    public void setCreatorName(String newCreatorName){
        creatorName = newCreatorName;
    }

  /**
   * getter method for jobType
   * @return String jobType
   */
    public String getJobType(){
        return jobType;
    }

  /**
   * setter method for jobType
   * @param newJobType
   */
    public void setJobType(String newJobType){
        jobType = newJobType;
    }   

  /**
   * getter method for createTime
   * @return String createTime
   */
    public String getCreateTime(){
        return DateHelper.format(createTime,DATE_FORMAT);
    }

  /**
   * setter method for createTime
   * @param newCreateTime
   */
    public void setCreateTime(String newCreateTime){
        createTime =DateHelper.parse(newCreateTime,DATE_FORMAT);
    }

  /**
   * getter method for executionTime
   * @return String executionTime
   */
    public String getExecutionTime(){
        return DateHelper.format(executionTime,DATE_FORMAT);
    }

  /**
   * setter method for executionTime
   * @param newExecutionTime
   */
    public void setExecutionTime(String newExecutionTime){
        executionTime =DateHelper.parse(newExecutionTime,DATE_FORMAT);
    }

     public String toString(){
        return ("["+jobName+","+creatorName+","+jobType+","+createTime+","+executionTime+"]");
    }

  /**
   * getter method for retrialCount
   * @return String retrialCount
   */
    public String getRetrialCount(){
        return retrialCount;
    }

  /**
   * setter method for retrialCount
   * @param newRetrialCount
   */
    public void setRetrialCount(String newRetrialCount){
        retrialCount = newRetrialCount;
    }

  /**
   * getter method for jobRetrialInterval
   * @return String jobRetrialInterval
   */
    public String getJobRetrialInterval(){
        return jobRetrialInterval;
    }

  /**
   * setter method for jobRetrialInterval
   * @param newJobRetrialInterval
   */
    public void setJobRetrialInterval(String newJobRetrialInterval){
        jobRetrialInterval = newJobRetrialInterval;
    }

  /**
   * getter method for jobMaxCount
   * @return String jobMaxCount
   */
    public String getJobMaxCount(){
        return jobMaxCount;
    }

  /**
   * setter method for jobMaxCount
   * @param newJobMaxCount
   */
    public void setJobMaxCount(String newJobMaxCount){
        jobMaxCount = newJobMaxCount;
    }

  /**
   * getter method for jobErrorMessage
   * @return String jobErrorMessage
   */
    public String getJobErrorMessage(){
        return jobErrorMessage;
    }

  /**
   * setter method for jobErrorMessage
   * @param newJobErrorMessage
   */
    public void setJobErrorMessage(String newJobErrorMessage){
        jobErrorMessage = newJobErrorMessage;
    }
    
}
