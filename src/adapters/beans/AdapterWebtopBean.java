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
 * $Id: AdapterWebtopBean.java,v 1.4 2006/02/02 12:29:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/** 
 * Purpose: Bean to set enabled adapters prefix to display in adapter_webtop.jsp  
 * @author : Rajan Kamal Gupta
 * @version: 1.0
 * Date of Creation   : 09-01-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class AdapterWebtopBean {
  private String adapterPrefixName;           // prefix of the adapter
  private String adapterName;                 // name of the adapter
  private String adapterStatusListActionName; // name of the statusListAction for the adapter

  /**
   * getter method for adapterPrefixName
   * @return String adapterPrefixName 
   */
  public String getAdapterPrefixName()  {
    return adapterPrefixName;
  }

  /**
   * setter method for adapterPrefixName
   * @param adapterPrefixName
   */
  public void setAdapterPrefixName(String adapterPrefixName)  {
    this.adapterPrefixName = adapterPrefixName;
  }

  /**
   * getter method for adapterName
   * @return adapterName
   */
  public String getAdapterName() {
    return adapterName;
  }

  /**
   * setter method for adapterName
   * @param adapterName
   */
  public void setAdapterName(String adapterName) {
    this.adapterName = adapterName;
  }

  /**
   * getter method for adapterStatusListActionName
   * @return adapterStatusListActionName
   */
  public String getAdapterStatusListActionName() {
    return adapterStatusListActionName;
  }

  /**
   * setter method for adapterStatusListActionName
   * @param adapterStatusListActionName
   */
  public void setAdapterStatusListActionName(String adapterStatusListActionName) {
    this.adapterStatusListActionName = adapterStatusListActionName;
  }
}