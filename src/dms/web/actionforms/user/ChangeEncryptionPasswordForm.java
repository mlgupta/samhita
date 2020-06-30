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
 * $Id: ChangeEncryptionPasswordForm.java,v 20040220.3 2006/03/17 07:46:24 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.user; 
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of 
 *           userListEditForm in change_password.jsp
 * @author              Maneesh Mishra
 * @version             1.0
 * 	Date of creation:   11-02-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class ChangeEncryptionPasswordForm extends ValidatorForm{
  /* member variables declaration goes here */
  private String txtPassword;             /* new password */
  private String txtConfirmPassword;      /* confirm new password */
  private String txtOldPassword;          /* old (viz : current ) password */
  /**
   * Purpose : Returns txtPassword.
   * @return String txtPassword
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
   * @return String txtConfirmPassword
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

  /**
   * Purpose : Returns the value of txtOldPassword
   * @return String txtOldPassword
   */
  public String getTxtOldPassword(){
    return txtOldPassword;
  }

  /**
   * Purpose : Sets the value of txtOldPassword
   * @param newTxtOldPassword Value of txtOldPassword from the form
   */
  public void setTxtOldPassword(String newTxtOldPassword){
    txtOldPassword = newTxtOldPassword;
  }
}
