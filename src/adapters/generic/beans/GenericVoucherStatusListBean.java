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
 * $Id: GenericVoucherStatusListBean.java,v 1.0 2006/06/23 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.generic.beans;
/**
 *	Purpose: To Populate the User List Table in erp_voucher_status_list.jsp
 *  @author              Suved Mishra
 *  @version             1.0
 * 	Date of creation:    23-06-2006
 * 	Last Modified by :
 * 	Last Modified Date:
 */
public class GenericVoucherStatusListBean  {
  private String erpVoucherStatus = null;       // represents voucher status
  private String dateInqueue = null;            // represents inQueue date
  private String dateInprocess = null;          // represents inProcess date
  private String dateProcessed = null;          // represents Processed date
  private String aclName = null;                // represents aclName
  private String docID;                         // represents docId as String
  private String docName;                       // represents docName

  /**
   * getter method for erpVoucherStatus
   * @return String erpVoucherStatus
   */
  public String getErpVoucherStatus() {
    return erpVoucherStatus;
  }

  /**
   * setter method for erpVoucherStatus
   * @param erpVoucherStatus
   */
  public void setErpVoucherStatus(String erpVoucherStatus) {
    this.erpVoucherStatus = erpVoucherStatus;
  }

  /**
   * getter method for dateInqueue
   * @return String dateInqueue
   */
  public String getDateInqueue() {
    return dateInqueue;
  }

  /**
   * setter method for dateInqueue
   * @param dateInqueue
   */
  public void setDateInqueue(String dateInqueue) {
    this.dateInqueue = dateInqueue;
  }

  /**
   * getter method for dateInprocess
   * @return String dateInprocess
   */
  public String getDateInprocess() {
    return dateInprocess;
  }

  /**
   * setter method for dateInprocess
   * @param dateInprocess
   */
  public void setDateInprocess(String dateInprocess) {
    this.dateInprocess = dateInprocess;
  }

  /**
   * getter method for dateProcessed
   * @return String dateProcessed
   */
  public String getDateProcessed() {
    return dateProcessed;
  }

  /**
   * setter method for dateProcessed
   * @param dateProcessed
   */
  public void setDateProcessed(String dateProcessed) {
    this.dateProcessed = dateProcessed;
  }

  /**
   * getter method for aclName
   * @return String aclName
   */
  public String getAclName() {
    return aclName;
  }

  /**
   * setter method for aclName
   * @param aclName
   */
  public void setAclName(String aclName) {
    this.aclName = aclName;
  }

  /**
   * getter method for docID
   * @return String docID
   */
  public String getDocID() {
    return docID;
  }

  /**
   * setter method for docID
   * @param docID
   */
  public void setDocID(String docID) {
    this.docID = docID;
  }

  /**
   * getter method for docName
   * @return String docName
   */
  public String getDocName() {
    return docName;
  }

  /**
   * setter method for docName
   * @param docName
   */
  public void setDocName(String docName) {
    this.docName = docName;
  }
}
