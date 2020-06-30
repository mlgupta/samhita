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
 * $Id: UserListSelectForm.java,v 20040220.4 2006/03/17 07:46:24 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.user; 
//Struts API
import org.apache.struts.action.ActionForm;
/**
 *	Purpose: To store the values of the html controls of userListSelectForm in  
 *           user_list_select.jsp
 *  @author             Sudheer Pujar
 *  @version            1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class UserListSelectForm extends ActionForm {
  private String[] chkSelect;           /* list of selected user's id */  
  private String control;               /* form control of parent window */
  private String txtSearchByUserName;   /* search a user by name */
  private String operation;             /* operation to be performed on a user */
  private String txtPageNo;             /* current page number of user list */  
  private String txtPageCount;          /* total number of pages for list */
 
  /**
   * Purpose : Returns  chkSelect array.
   * @return : String[] 
   */
  public String[] getChkSelect() {
      return chkSelect;
  }

  /**
   * Purpose : Sets the value of chkSelect array.
   * @param  : newChkSelect Value of chkSelect from the form
   */
  public void setChkSelect(String[] newChkSelect) {
      chkSelect = newChkSelect;
  }

  /**
   * Purpose : Returns txtSearchByUserName.
   * @return : String
   */
  public String getTxtSearchByUserName() {
      return txtSearchByUserName;
  }

  /**
   * Purpose : Sets the value of txtSearchByUserName.
   * @param  : newTxtSearchByUserName Value of txtSearchByUserName from the form
   */
  public void setTxtSearchByUserName(String newTxtSearchByUserName) {
      txtSearchByUserName = newTxtSearchByUserName;
  }

  /**
   * Purpose : Returns control.
   * @return : String
   */
  public String getControl() {
      return control;
  }

  /**
   * Purpose : Sets the value of control.
   * @param  : newControl Value of control from the form
   */
  public void setControl(String newControl) {
      control = newControl;
  }

  /**
   * Purpose : Returns txtPageNo.
   * @return : String
   */
  public String getTxtPageNo() {
      return txtPageNo;
  }

  /**
   * Purpose : Sets the value of txtPageNo.
   * @param  : newTxtPageNo Value of txtPageNo from the form
   */
  public void setTxtPageNo(String newTxtPageNo) {
      txtPageNo = newTxtPageNo;
  }

  /**
   * Purpose : Returns txtPageCount.
   * @return : String
   */
  public String getTxtPageCount() {
      return txtPageCount;
  }

  /**
   * Purpose : Sets the value of txtPageCount.
   * @param  : newTxtPageCount Value of txtPageCount from the form
   */
  public void setTxtPageCount(String newTxtPageCount) {
      txtPageCount = newTxtPageCount;
  }

  /**
   * Purpose : Returns operation.
   * @return : String
   */
  public String getOperation() {
      return operation;
  }

  /**
   * Purpose : Sets the value of operation.
   * @param  : newOperation Value of operation from the form
   */
  public void setOperation(String newOperation) {
      operation = newOperation;
  }
}
