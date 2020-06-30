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
 * $Id: MyNotificationB4Form.java,v 1.2 2006/03/17 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.workflow;
/* Struts API */
import org.apache.struts.action.ActionForm;
/**
 *	Purpose: Dummy form used for NotificationB4Action
 *  @author              Maneesh Mishra
 *  @version             1.0
 * 	Date of creation:    30-04-2005   
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  17-03-2006  
 */
public class MyNotificationB4Form extends ActionForm  {
  /* member variables declaration goes here */
  boolean isNotification = true;      /* dummy variable */
  
  /**
   * Purpose : Returns isNotification.
   * @return boolean isNotification
   */
  public boolean getIsNotification(){
    return isNotification;
  }

  /**
   * Purpose : Sets the value of isNotification.
   * @param isNotification
   */
  public void setIsNotification(boolean isNotification){
    this.isNotification = isNotification;
  }
}