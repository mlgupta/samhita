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
 * $Id: AdapterWebtopForm.java,v 1.0 2006/07/04 17:26:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actionforms;
/* Struts API */
import org.apache.struts.action.ActionForm;
/** 
 * Purpose            : Form to populate enabled adapters in adapter_webtop.jsp
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 04-07-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class AdapterWebtopForm extends ActionForm  {

  private String[] cboEnabledAdapters; /* list of enabled adapters */

  /**
   * getter method for list of enabled adapters
   * @return String[] containing cboEnabledAdapters
   */
  public String[] getCboEnabledAdapters() {
    return cboEnabledAdapters;
  }

  /**
   * setter method for list of enabled adapters
   * @param cboEnabledAdapters
   */
  public void setCboEnabledAdapters(String[] cboEnabledAdapters) {
    this.cboEnabledAdapters = cboEnabledAdapters;
  }
}