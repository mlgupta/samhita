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
 * $Id: DocLogBean.java,v 1.6 2006/03/20 12:09:33 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 * Purpose                : Bean used to store individual log entry of a document  
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 15-02-2005
 * Last Modified by       :
 * Last Modification Date :
 */
public class DocLogBean  {

  private String event; /* concatination of userId,timeStamp,actionPerformed */
  private String userId;                /* name of user */
  private String timeStamp;             /* timestamp ofthe activity performed */
  private String actionPerformed;       /* activity performed */
  private String trimActionPerformed;   /* limit activity performed for display */
  
  /**
   * getter method for event
   * @return String event
   */
  public String getEvent(){
    return event;
  }

  /**
   * setter method for event
   * @param newEvent
   */
  public void setEvent(String newEvent){
    event = newEvent;
  }
  
  /**
   * getter method for userId
   * @return String userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * setter method for userId
   * @param userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * getter method for timeStamp
   * @return String timeStamp
   */
  public String getTimeStamp() {
    return timeStamp;
  }

  /**
   * setter method for timeStamp
   * @param timeStamp
   */
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  /**
   * getter method for actionPerformed
   * @return String actionPerformed
   */
  public String getActionPerformed() {
    return actionPerformed;
  }

  /**
   * setter method for actionPerformed
   * @param actionPerformed
   */
  public void setActionPerformed(String actionPerformed) {
    this.actionPerformed = actionPerformed;
  }

  /**
   * getter method for trimActionPerformed
   * @return String trimActionPerformed
   */
  public String getTrimActionPerformed() {
    return trimActionPerformed;
  }

  /**
   * setter method for trimActionPerformed
   * @param trimActionPerformed
   */
  public void setTrimActionPerformed(String trimActionPerformed) {
    this.trimActionPerformed = trimActionPerformed;
  }
}
