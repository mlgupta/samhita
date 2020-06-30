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
 * $Id: ThemeListForm.java,v 20040220.4 2006/03/17 07:05:17 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.theme;
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of themeListForm in  
 *           theme_list.jsp 
 *  @author             Rajan Gupta
 *  @version            1.0
 * 	Date of creation:   25-02-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class ThemeListForm extends ValidatorForm  {
  private String operation;               /* operation to be performed */
  private String radSelect;               /* select the theme */
  private String txtPageNo="1";           /* page number for theme listing */
  private String txtSearchByThemeName;    /* theme name to search */  
  private String txtPageCount="1";        /* total number of pages for listing */

  /**
   * Purpose : Getter method for operation.
   * @return : String operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Purpose : Setter method for operation.
   * @param  : newOperation
   */
  public void setOperation(String newOperation) {
    operation = newOperation;
  }

  /**
   * Purpose : Getter method for radSelect.
   * @return : String radSelect
   */
  public String getRadSelect() {
    return radSelect;
  }

  /**
   * Purpose : Setter method for radSelect.
   * @param  : newRadSelect
   */
  public void setRadSelect(String newRadSelect) {
    radSelect = newRadSelect;
  }

  /**
   * Purpose : Setter method for txtPageNo.
   * @return : String txtPageNo
   */
  public String getTxtPageNo() {
    return txtPageNo;
  }

  /**
   * Purpose : Setter method for txtPageNo.
   * @param  : newTxtPageNo
   */
  public void setTxtPageNo(String newTxtPageNo) {
    txtPageNo = newTxtPageNo;
  }

  /**
   * Purpose : Getter method for txtSearchByThemeName.
   * @return : String txtSearchByThemeName
   */
  public String getTxtSearchByThemeName() {
    return txtSearchByThemeName;
  }

  /**
   * Purpose : Setter method for txtSearchbyThemeName.
   * @param  : newTxtSearchByThemeName
   */
  public void setTxtSearchByThemeName(String newTxtSearchByThemeName) {
    txtSearchByThemeName = newTxtSearchByThemeName;
  }

  /**
   * Purpose : Getter method for txtPageCount.
   * @return : String txtPageCount
   */
  public String getTxtPageCount() {
    return txtPageCount;
  }

  /**
   * Purpose : Setter method for txtPageCount.
   * @param  : newTxtPageCount
   */
  public void setTxtPageCount(String newTxtPageCount) {
    txtPageCount = newTxtPageCount;
  }

}
