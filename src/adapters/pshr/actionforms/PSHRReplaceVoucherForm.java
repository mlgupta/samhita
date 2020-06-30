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
 * $Id: PSHRReplaceVoucherForm.java,v 1.1 2006/01/10  manish Exp $
 *****************************************************************************
 */
package adapters.pshr.actionforms;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Struts API */
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionForm;
/**
 * Purpose            : Form Bean to store user options when replacing voucher 
 *                      image.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 03-07-06
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class PSHRReplaceVoucherForm extends ActionForm  {
  private String txtPath;       // member variable for storing file path
  private FormFile frmFile;     // member variable for storing file object
  private String txaFileDesc;   // member variable for storing file description
  private String voucherId;     // member variable for storing voucher-id
  private String txtDocumentId; // member variable for storing document id 

/**
 * Reset all properties to their default values.
 * @param mapping The ActionMapping used to select this instance.
 * @param request The HTTP Request we are processing.
 */
  public void reset(ActionMapping mapping, HttpServletRequest request) {
      super.reset(mapping, request);
  }

  /**
   * getter method for form file object
   * @return FormFile 
   */
  public FormFile getFrmFile() {
    return frmFile;
  }

  /**
   * setter method for form file object
   * @param frmFile
   */
  public void setFrmFile(FormFile frmFile) {
    this.frmFile = frmFile;
  }

  /**
   * getter method for file description
   * @return String containing file description
   */
  public String getTxaFileDesc() {
    return txaFileDesc;
  }

  /**
   * setter method for file description
   * @param txaFileDesc
   */
  public void setTxaFileDesc(String txaFileDesc) {
    this.txaFileDesc = txaFileDesc;
  }

  /**
   * getter method for txtPath
   * @return String containing txtPath
   */
  public String getTxtPath() {
    return txtPath;
  }

  /**
   * setter method for txtPath
   * @param txtPath
   */
  public void setTxtPath(String txtPath) {
    this.txtPath = txtPath;
  }

  /**
   * getter method for file description
   * @return String containing file description
   */
  public String getVoucherId() {
    return voucherId;
  }

  /**
   * setter method for voucherId
   * @param voucherId
   */
  public void setVoucherId(String voucherId) {
    this.voucherId = voucherId;
  }

  /**
   * getter method for txtDocumentId
   * @return String containing txtDocumentId
   */
  public String getTxtDocumentId() {
    return txtDocumentId;
  }

  /**
   * setter method for txtDocumentId
   * @param txtDocumentId
   */
  public void setTxtDocumentId(String txtDocumentId) {
    this.txtDocumentId = txtDocumentId;
  }
}