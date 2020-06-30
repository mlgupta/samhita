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
 * $Id ChangePasswordForm.java,v 20040220.4 2005/03/29 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.user; 
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of 
 *           userListEditForm in change_password.jsp
 *  @author             Rajan Kamal Gupta
 *  @version            1.0
 * 	Date of creation:   11-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class ChangePasswordForm extends ValidatorForm{
  /* member variables declaration goes here */
  private String txtUserName;             /* user name */
  private String txtPassword;             /* new password */
  private String txtConfirmPassword;      /* confirm new password */

  /**
   * Purpose : Returns txtUserName.
   * @return String
   */
  public String getTxtUserName() {
      return txtUserName;
  }

  /**
   * Purpose : Sets the value of txtUserName 
   * @param newTxtUserName Value of txtUserName from the form
   */
  public void setTxtUserName(String newTxtUserName) {
      txtUserName = newTxtUserName;
  }

  /**
   * Purpose : Returns txtPassword.
   * @return String
   */
  public String getTxtPassword() {
      return txtPassword;
  }

  /**
   * Purpose : Sets the value of txtPassword 
   * @param newTxtPassword Value of txtPassword from the form
   */
  public void setTxtPassword(String newTxtPassword) {
      txtPassword = newTxtPassword;
  }

  /**
   * Purpose : Returns txtConfirmPassword.
   * @return String
   */
  public String getTxtConfirmPassword() {
      return txtConfirmPassword;
  }

  /**
   * Purpose : Sets the value of txtConfirmPassword 
   * @param newTxtConfirmPassword Value of txtConfirmPassword from the form
   */
  public void setTxtConfirmPassword(String newTxtConfirmPassword) {
      txtConfirmPassword = newTxtConfirmPassword;
  }
}
