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
 * $Id: ListManagedFoldersForm.java,v 1.3 2006/02/02 12:13:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actionforms;
/* Struts API */
import org.apache.struts.action.ActionForm;
/**
 * Purpose            : Action Form called to display managed folders for a 
 *                      given adapter.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class ListManagedFoldersForm extends ActionForm  {
  private String[] chkManagedFolderKeys; /* array of managed folder keys */ 
  private String hdnPrefix;              /* adapter prefix used */ 

  /**
   * getter method for array of managed folder keys
   * @return String[] containing chkManagedFolderKeys
   */
  public String[] getChkManagedFolderKeys() {
    return chkManagedFolderKeys;
  }

  /**
   * setter method for array of managed folder keys
   * @param chkManagedFolderKeys
   */
  public void setChkManagedFolderKeys(String[] chkManagedFolderKeys) {
    this.chkManagedFolderKeys = chkManagedFolderKeys;
  }

  /**
   * getter method for adapter prefix used
   * @return String containing hdnPrefix
   */
  public String getHdnPrefix() {
    return hdnPrefix;
  }

  /**
   * setter method for adapter prefix used
   * @param hdnPrefix
   */
  public void setHdnPrefix(String hdnPrefix) {
    this.hdnPrefix = hdnPrefix;
  }
}