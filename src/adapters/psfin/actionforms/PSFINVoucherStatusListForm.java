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
 * $Id: PSFINVoucherStatusListForm.java,v 1.12 2006/03/17 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.psfin.actionforms;
// Servlet API
import javax.servlet.http.HttpServletRequest;
 //Struts API
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of PSFINVoucherStatusListForm 
 *            in psfin_voucher_status_list.jsp
 *  @author              Rajan Kamal Gupta        
 *  @version             1.0
 * 	Date of creation:    09-01-2006
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  17-03-2006  
 */

public class PSFINVoucherStatusListForm extends ValidatorForm {
  private String operation;          // typeof operation to to performed
  private String txtPageNo;          // page number for the record set displayed
  private String txtPageCount;       // total number of pages to display
  private String txtFromDate;        // a datewise search criteria viz:from date 
  private String txtToDate;          // a datewise search criteria viz:to date
  private String timezone;           // timezone 
  private String voucherStatus;      // default or selected voucher status
  private String voucherZone;        // default or selected voucher zone
  private String[] cboInvoiceStatus; // collection of types of invoice status
  private String[] cboInvoiceZone;   // collection of zones
  private String[] radSelect;        // selected documentIds
  private String hdnType;            // selected voucher zone
  
  /**
   * getter method for operation
   * @return String operation
   */
  public String getOperation(){
    return operation;
  }

  /**
   * setter method for operation
   * @param operation
   */
  public void setOperation(String operation){
    this.operation = operation;
  }

  /**
   * getter method for radSelect
   * @return String[] radSelect
   */
  public String[] getRadSelect(){
    return radSelect;
  }

  /**
   * setter method for radSelect
   * @param radSelect
   */
  public void setRadSelect(String[] radSelect){
    this.radSelect = radSelect;
  }

  /**
   * getter method for txtPageCount
   * @return String txtPageCount
   */
  public String getTxtPageCount(){
    return txtPageCount;
  }

  /**
   * setter method for txtPageCount
   * @param txtPageCount
   */
  public void setTxtPageCount(String txtPageCount){
    this.txtPageCount = txtPageCount;
  }

  /**
   * getter method for txtPageNo
   * @return String txtPageNo
   */
  public String getTxtPageNo(){
    return txtPageNo;
  }

  /**
   * setter method for txtPageNo
   * @param txtPageNo
   */
  public void setTxtPageNo(String txtPageNo){
    this.txtPageNo = txtPageNo;
  }

  /**
   * getter method for cboInvoiceStatus
   * @return String[] cboInvoiceStatus
   */
  public String[] getCboInvoiceStatus(){
    return cboInvoiceStatus;
  }

  /**
   * setter method for cboInvoiceStatus
   * @param cboInvoiceStatus
   */
  public void setCboInvoiceStatus(String[] cboInvoiceStatus){
    this.cboInvoiceStatus = cboInvoiceStatus;
  }

  /**
   * getter method for cboInvoiceZone
   * @return String[] cboInvoiceZone
   */
  public String[] getCboInvoiceZone(){
    return cboInvoiceZone;
  }

  /**
   * setter method for cboInvoiceZone
   * @param cboInvoiceZone
   */
  public void setCboInvoiceZone(String[] cboInvoiceZone){
    this.cboInvoiceZone = cboInvoiceZone;
  }

  /**
   * getter method for txtFromDate
   * @return String txtFromDate
   */
  public String getTxtFromDate(){
    return txtFromDate;
  }

  /**
   * setter method for txtFromDate
   * @param txtFromDate
   */
  public void setTxtFromDate(String txtFromDate){
    this.txtFromDate = txtFromDate;
  }

  /**
   * getter method for txtToDate
   * @return String txtToDate
   */
  public String getTxtToDate(){
    return txtToDate;
  }

  /**
   * setter method for txtToDate
   * @param txtToDate
   */
  public void setTxtToDate(String txtToDate){
    this.txtToDate = txtToDate;
  }

  /**
   * getter method for timezone
   * @return String timezone
   */
  public String getTimezone(){
    return timezone;
  }

  /**
   * setter method for timezone
   * @param timezone
   */
  public void setTimezone(String timezone){
    this.timezone = timezone;
  }

  /**
   * getter method for voucherStatus
   * @return String voucherStatus
   */
  public String getVoucherStatus(){
    return voucherStatus;
  }

  /**
   * setter method for voucherStatus
   * @param voucherStatus
   */
  public void setVoucherStatus(String voucherStatus){
    this.voucherStatus = voucherStatus;
  }

  /**
   * getter method for voucherZone
   * @return String voucherZone
   */
  public String getVoucherZone(){
    return voucherZone;
  }

  /**
   * setter method for voucherZone
   * @param voucherZone
   */
  public void setVoucherZone(String voucherZone){
    this.voucherZone = voucherZone;
  }

  /**
  * Reset all properties to their default values.
  * @param mapping The ActionMapping used to select this instance.
  * @param request The HTTP Request we are processing.
  */
  public void reset(ActionMapping mapping, HttpServletRequest request){
      super.reset(mapping, request);
  }

  /**
   * Validate all properties to their default values.
   * @param mapping The ActionMapping used to select this instance.
   * @param request The HTTP Request we are processing.
   * @return ActionErrors A list of all errors found.
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
      return super.validate(mapping, request);
  }
  
  /**
   * getter method for hdnType
   * @return String hdnType
   */
  public String getHdnType() {
    return hdnType;
  }

  /**
   * setter method for hdnType
   * @param hdnType
   */
  public void setHdnType(String hdnType) {
    this.hdnType = hdnType;
  }
}