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
 * $Id: ShowContentForm.java,v 1.3 2006/02/02 12:15:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actionforms;
/* Struts API */
import org.apache.struts.action.ActionForm;
/**
 * Purpose            : Action Form called to display newly generated key for a 
 *                      managed folder.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class ShowContentForm extends ActionForm  {
  private boolean keyManaged = true;   /* boolean keymanaged */
  private String managedFolderKey;     /* String managed folder key */

  /**
   * getter method whether key has been managed 
   * @return keyManaged
   */
  public boolean isKeyManaged() {
    return keyManaged;
  }

  /**
   * setter method for key managed
   * @param keyManaged
   */
  public void setKeyManaged(boolean keyManaged) {
    this.keyManaged = keyManaged;
  }

  /**
   * getter method for managed folder key
   * @return String managed folder key
   */
  public String getManagedFolderKey() {
    return managedFolderKey;
  }

  /**
   * setter method for managed folder key
   * @param managedFolderKey
   */
  public void setManagedFolderKey(String managedFolderKey) {
    this.managedFolderKey = managedFolderKey;
  }
}