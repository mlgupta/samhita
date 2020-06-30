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
 * $Id: NotificationListBean.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/**
 *	Purpose:             Bean to store details of a notification .
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class NotificationListBean {
  String notificationId;          /* represents notification id */
  String status;                  /* represents document status */
  String beginDate;               /* represents begin date of approval */
  String endDate;                 /* represents end date of approval */ 
  String subject;                 /* represents subject of approval */

  public NotificationListBean() {
  }

  /**
   * getter method for notificationId
   * @return String notificationId
   */
  public String getNotificationId() {
    return notificationId;
  }

  /**
   * setter method for notificationId
   * @param notificationId
   */
  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }

  /**
   * getter method for status
   * @return String status
   */
  public String getStatus() {
    return status;
  }

  /**
   * setter method for status
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * getter method for beginDate
   * @return String beginDate
   */
  public String getBeginDate() {
    return beginDate;
  }

  /**
   * setter method for beginDate
   * @param beginDate
   */
  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  /**
   * getter method for endDate
   * @return String endDate
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * setter method for endDate
   * @param endDate
   */
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /**
   * getter method for subject
   * @return String subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * setter method for subject 
   * @param subject
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }
}
