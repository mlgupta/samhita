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
 * $Id: DocLogListBean.java,v 1.5 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose:             Bean used to list log entries for a document.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    10-04-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
class DocLogList  {
  private String user;          /* user for logging */
  private String docLogDate;    /* date of log activity */

  /**
   * Purpose : Returns the user.
   * @return : String
   */
  public String getUser() {
    return user;
  }

  /**
   * Purpose : Sets the user.
   * @param  : newUser Value of user 
   */
  public void setUser(String newUser) {
    user = newUser;
  }

  /**
   * Purpose : Returns the createdDate.
   * @return : String
   */
  public String getDocLogDate() {
    return docLogDate;
  }

  /**
   * Purpose : Sets the createdDate.
   * @param  : newCreatedDate Value of createdDate 
   */
  public void setDocLogDate(String docLogDate) {
    this.docLogDate = docLogDate;
  }
}
