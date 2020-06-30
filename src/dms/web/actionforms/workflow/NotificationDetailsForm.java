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
 * $Id: NotificationDetailsForm.java,v 1.6 2006/03/17 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.workflow;
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of notificationDetailsForm   
 *           in notification_details.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    02-05-2005
 * 	Last Modified by :   Suved Mishra     
 * 	Last Modified Date:  17-03-2006  
 */
public class NotificationDetailsForm extends ValidatorForm{
  /* member variables declaration goes here */
  private String txtNotificationName;         /* notification name */
  private String txtNotificationSent;         /* notification sent */
  private String txtNotificationSubject;      /* notification subject */
  private String txaNotificationComments;     /* notification comments */
  private String notificationId;              /* notification id */
  private String userAction;                  /* user action performed */
  private String itemType;                    /* item type for this notification */ 
  private String itemKey;                     /* item key for this notification */
  private String status;                      /* notification status */
  private String note;                        /* note attached by approver */

  /**
   * Purpose : Returns txtNotificationName.
   * @return String txtNotificationName
   */
  public String getTxtNotificationName() {
    return txtNotificationName;
  }

  /**
   * Purpose : Sets txtNotificationName.
   * @param txtNotificationName
   */
  public void setTxtNotificationName(String txtNotificationName) {
    this.txtNotificationName = txtNotificationName;
  }

  /**
   * Purpose : Returns txtNotificationSent.
   * @return String txtNotificationSent
   */
  public String getTxtNotificationSent() {
    return txtNotificationSent;
  }

  /**
   * Purpose : Sets txtNotificationSent.
   * @param txtNotificationSent
   */
  public void setTxtNotificationSent(String txtNotificationSent) {
    this.txtNotificationSent = txtNotificationSent;
  }

  /**
   * Purpose : Returns txtNotificationSubject.
   * @return String txtNotificationSubject
   */
  public String getTxtNotificationSubject() {
    return txtNotificationSubject;
  }

  /**
   * Purpose : Sets txtNotificationSubject.
   * @param txtNotificationSubject
   */
  public void setTxtNotificationSubject(String txtNotificationSubject) {
    this.txtNotificationSubject = txtNotificationSubject;
  }

  /**
   * Purpose : Returns txaNotificationComments.
   * @return String txaNotificationComments
   */
  public String getTxaNotificationComments() {
    return txaNotificationComments;
  }

  /**
   * Purpose : Sets txaNotificationComments.
   * @param txaNotificationComments
   */
  public void setTxaNotificationComments(String txaNotificationComments) {
    this.txaNotificationComments = txaNotificationComments;
  }

  /**
   * Purpose : Returns notificationId.
   * @return String notificationId
   */
  public String getNotificationId() {
    return notificationId;
  }

  /**
   * Purpose : Sets notificationId.
   * @param notificationId
   */
  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }

  /**
   * Purpose : Returns userAction.
   * @return String userAction
   */
  public String getUserAction() {
    return userAction;
  }

  /**
   * Purpose : Sets userAction.
   * @param userAction
   */
  public void setUserAction(String userAction) {
    this.userAction = userAction;
  }

  /**
   * Purpose : Returns itemType.
   * @return String itemType
   */
  public String getItemType() {
    return itemType;
  }

  /**
   * Purpose : Sets itemType.
   * @param itemType
   */
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  /**
   * Purpose : Returns itemKey.
   * @return String itemKey
   */
  public String getItemKey() {
    return itemKey;
  }

  /**
   * Purpose : Sets itemKey. 
   * @param itemKey
   */
  public void setItemKey(String itemKey) {
    this.itemKey = itemKey;
  }

  /**
   * Purpose : Returns status.
   * @return String status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Purpose : Sets status.
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Purpose : Returns note.
   * @return String note
   */
  public String getNote() {
    return note;
  }

  /**
   * Purpose : Sets note.
   * @param note
   */
  public void setNote(String note) {
    this.note = note;
  }
}