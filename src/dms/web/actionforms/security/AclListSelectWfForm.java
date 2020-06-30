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
 * $Id: AclListSelectWfForm.java,v 1.2 2006/03/17 06:39:07 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.security;
/* Struts API */
import org.apache.struts.action.ActionForm;
/**
 *	Purpose: To store the values of the html controls of
 *           AclListSelectWfForm in acl_list_select_wf.jsp
 *  @author             Suved Mishra
 *  @version            1.0
 * 	Date of creation:   15-12-2005
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class AclListSelectWfForm extends ActionForm  {
  /* member variables */
  private String txtSearchByAclName;   /* acl name to be searched */
  private String control;              /* form control of parent window */
  private String txtPageNo;            /* page number of acl list displayed */
  private String txtPageCount;         /* total number of pages for display */ 
  private String operation;            /* operation to be performed on list */ 
  private String systemAclParameter;   /* state if system acl is to be searched */ 
  public String[] chkSelect;           /* list of acl ids selected */ 
  
  /**
   * Purpose   :  Returns  txtAclName.
   * @return   :  String txtSearchByAclName
   */
  public String getTxtSearchByAclName() {
      return txtSearchByAclName;
  }

  /**
   * Purpose   : Sets the value of txtAclName.
   * @param    : newTxtAclName Value of txtAclName from the form
   */
  public void setTxtSearchByAclName(String newTxtSearchByAclName) {
      txtSearchByAclName = newTxtSearchByAclName;
  }

  /**
   * Purpose : Returns control.
   * @return : String control
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
   * Purpose : Gets the value of systemAclParameter.
   * @return String systemAclParameter
   */
  public String getSystemAclParameter() {
      return systemAclParameter;
  }

  /**
   * Purpose : Sets the value of systemAclParameter
   * @param newSystemAclParameter
   */
  public void setSystemAclParameter(String newSystemAclParameter){
      systemAclParameter = newSystemAclParameter;
  }

  /**
   * Purpose : Gets the value of chkSelect.
   * @return String [] chkSelect
   */
  public String[] getChkSelect() {
    return chkSelect;
  }

  /**
   * Purpose : Sets the value of chkSelect.
   * @param chkSelect
   */
  public void setChkSelect(String[] chkSelect) {
    this.chkSelect = chkSelect;
  }
}