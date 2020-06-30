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
 * $Id: PSFINVoucherReportForm.java,v 1.5 2006/01/10  manish Exp $
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
 *	Purpose:  To store the values of the html controls of PSFINVoucherReportForm in 
 *            psfin_voucher_report.jsp
 *  @author         : Rajan Kamal Gupta   
 *  @version        :
 * 	Date of creation: 2006/01/10   
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class PSFINVoucherReportForm extends ValidatorForm {
  private String txtFromDate;                // store from date 
  private String txtToDate;                  // store to date
  private String timezone;                   // store timezone
  private String txtTotalVouchers;           // store total nos. of vouchers
  private String txtInProcessVoucher;        // store nos. of InProcess vouchers
  private String txtProcessedVoucher;        // store nos. of Processed vouchers
  private String txtMultiple;                // store nos. of vouchers   
  private String[] cboVoucherZone;           // store array voucher zone
  private String voucherZone;                // store selected voucher zone
  private String txtInQueueVoucher;          // store nos. of InQueue vouchers
  private String operation;                  // stores relay operation 

  
  /**
   * getter method for voucher zones
   * @return String[] containing voucher zones
   */
  public String[] getCboVoucherZone(){
    return cboVoucherZone;
  }

  /**
   * setter method for voucher zones
   * @param cboVoucherZone
   */
  public void setCboVoucherZone(String[] cboVoucherZone){
    this.cboVoucherZone = cboVoucherZone;
  }

  /**
   * getter method for timezone
   * @return String containing timezone
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
   * getter method for txtFromDate
   * @return String containing txtFromDate
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
   * @return String containing txtToDate
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
   * getter method for txtTotalVouchers
   * @return String containing txtTotalVouchers
   */
  public String getTxtTotalVouchers(){
    return txtTotalVouchers;
  }

  /**
   * setter method for txtTotalVouchers
   * @param txtTotalVouchers
   */
  public void setTxtTotalVouchers(String txtTotalVouchers){
    this.txtTotalVouchers = txtTotalVouchers;
  }

  /**
   * getter method for txtInProcessVoucher
   * @return String containing txtInProcessVoucher
   */
  public String getTxtInProcessVoucher(){
    return txtInProcessVoucher;
  }

  /**
   * setter method for txtInProcessVoucher
   * @param txtInProcessVoucher
   */
  public void setTxtInProcessVoucher(String txtInProcessVoucher){
    this.txtInProcessVoucher = txtInProcessVoucher;
  }

  /**
   * getter method for txtProcessedVoucher
   * @return String containing txtProcessedVoucher
   */
  public String getTxtProcessedVoucher(){
    return txtProcessedVoucher;
  }

  /**
   * setter method for txtProcessedVoucher
   * @param txtProcessedVoucher
   */
  public void setTxtProcessedVoucher(String txtProcessedVoucher){
    this.txtProcessedVoucher = txtProcessedVoucher;
  }

  /**
   * getter method for txtMultiple
   * @return String containing txtMultiple
   */
  public String getTxtMultiple(){
    return txtMultiple;
  }

  /**
   * setter method for txtMultiple
   * @param txtMultiple
   */
  public void setTxtMultiple(String txtMultiple){
    this.txtMultiple = txtMultiple;
  }

  /**
   * getter method for voucherZone
   * @return String containing voucherZone
   */
  public String getVoucherZone() {
    return voucherZone;
  }

  /**
   * setter method for voucherZone
   * @param voucherZone
   */
  public void setVoucherZone(String voucherZone) {
    this.voucherZone = voucherZone;
  }

  /**
   * getter method for txtInQueueVoucher
   * @return String containing txtInQueueVoucher
   */
  public String getTxtInQueueVoucher() {
    return txtInQueueVoucher;
  }
  
  /**
   * setter method for txtInQueueVoucher
   * @param txtInQueueVoucher
   */
  public void setTxtInQueueVoucher(String txtInQueueVoucher) {
    this.txtInQueueVoucher = txtInQueueVoucher;
  }

  /**
   * getter method for operation
   * @return String containing operation
   */
  public String getOperation() {
    return operation;
  }
  
  /**
   * setter method for operation
   * @param operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
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
}