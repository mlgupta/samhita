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
 * $Id: MyNotificationListForm.java,v 1.6 2006/03/17 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.workflow;
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of html controls of myNotificationListForm in  
 *           mynotification.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    30-04-2005   
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  17-03-2006  
 */
public class MyNotificationListForm extends ValidatorForm {
  /* member variables declaration goes here */
  private String radSelect;                 /* select a notification */
  private String operation;                 /* operation to be performed  */
  private String txtPageNo;                 /* current page number in list */
  private String txtPageCount;              /* total number of pages in list */
  private String txtSearchBeginDateFrom;    /* begin search from date */
  private String txtSearchBeginDateTo;      /* begin search to date */
  private String txtSearchEndDateFrom;      /* end search from date */
  private String txtSearchEndDateTo;        /* end search to date  */
  private String[] cboStatus;               /* status of notification to search */
  /**
   * Purpose : Returns radSelect.
   * @return : String radSelect
   */
  public String getRadSelect() {
      return radSelect;
  }

  /**
   * Purpose : Sets the value of radSelect.
   * @param  : newRadSelect Value of radSelect from the form
   */
  public void setRadSelect(String newRadSelect) {
      radSelect = newRadSelect;
  }

  /**
   * Purpose : Returns operation.
   * @return : String operation
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

  /**
   * Purpose : Returns txtPageNo.
   * @return : String txtPageNo
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
   * @return : String txtPageCount
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
   * Purpose : Returns txtSearchBeginDateFrom.
   * @return : String txtSearchBeginDateFrom
   */
  public String getTxtSearchBeginDateFrom() {
    return txtSearchBeginDateFrom;
  }

  /**
   * Purpose : Sets the value of txtSearchBeginDateFrom.
   * @param  : newTxtSearchBeginDateFrom Value of txtSearchBeginDateFrom from the form
   */
  public void setTxtSearchBeginDateFrom(String txtSearchBeginDateFrom) {
    this.txtSearchBeginDateFrom = txtSearchBeginDateFrom;
  }

  /**
   * Purpose : Returns txtSearchBeginDateTo.
   * @return : String txtSearchBeginDateTo
   */
  public String getTxtSearchBeginDateTo() {
    return txtSearchBeginDateTo;
  }

  /**
   * Purpose : Sets the value of txtSearchBeginDateTo.
   * @param  : newTxtSearchBeginDateTo Value of txtSearchBeginDateTo from the form
   */
  public void setTxtSearchBeginDateTo(String txtSearchBeginDateTo) {
    this.txtSearchBeginDateTo = txtSearchBeginDateTo;
  }

  /**
   * Purpose : Returns txtSearchEndDateFrom.
   * @return : String txtSearchEndDateFrom
   */
  public String getTxtSearchEndDateFrom() {
    return txtSearchEndDateFrom;
  }

  /**
   * Purpose : Sets the value of txtSearchEndDateFrom.
   * @param  : newTxtSearchEndDateFrom Value of txtSearchEndDateFrom from the form
   */
  public void setTxtSearchEndDateFrom(String txtSearchEndDateFrom) {
    this.txtSearchEndDateFrom = txtSearchEndDateFrom;
  }

  /**
   * Purpose : Returns txtSearchEndDateTo.
   * @return : String txtSearchEndDateTo
   */
  public String getTxtSearchEndDateTo() {
    return txtSearchEndDateTo;
  }
  
  /**
   * Purpose : Sets the value of txtSearchEndDateTo.
   * @param  : newTxtSearchEndDateTo Value of txtSearchEndDateTo from the form
   */
  public void setTxtSearchEndDateTo(String txtSearchEndDateTo) {
    this.txtSearchEndDateTo = txtSearchEndDateTo;
  }

  /**
   * Purpose : Returns cboStatus.
   * @return : String[] cboStatus
   */
  public String[] getCboStatus() {
    return cboStatus;
  }

  /**
   * Purpose : Sets the value of cboStatus.
   * @param  : cboStatus Value of cboStatus from the form
   */
  public void setCboStatus(String[] cboStatus) {
    this.cboStatus = cboStatus;
  }
}