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
 * $Id: AclPrefixBean.java,v 1.3 2006/01/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.security;
/**
 *	Purpose:             Bean to store prefixes for various types acls .
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    13-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class AclPrefixBean  {
  
  private String adapterName;   /* represents adapter name */
  private String aclPrefix;     /* represents acl prefix for corresponding 
                                 * adapter name */

  public AclPrefixBean() {
  }

  /**
   * getter method for adapterName
   * @return String adapterName
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
   * getter method for aclPrefix
   * @return String aclPrefix
   */
  public String getAclPrefix() {
    return aclPrefix;
  }

  /**
   * setter method for aclPrefix
   * @param aclPrefix
   */
  public void setAclPrefix(String aclPrefix) {
    this.aclPrefix = aclPrefix;
  }
}