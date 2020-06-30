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
 * $Id: AdapterPreferenceForm.java,v 1.4 2006/02/02 12:11:35 IST suved Exp $
 *****************************************************************************
 */
package adapters.actionforms;
/* Struts API */
import org.apache.struts.action.ActionForm;
/**
 * Purpose            : Action Form called to display adapter preferences.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 17-01-06
 * Last Modified Date : 19-01-06
 * Last Modified By   : Suved Mishra
 */

public class AdapterPreferenceForm extends ActionForm  {
  private boolean enableLookUp;          /* checkbox to enable folder tree look up */
  private String hdnPrefix;              /* adapter prefix used */ 
  private String txtFolderPath;          /* managed folder path */
  private String[] lstAvailableAdapters; /* set of available adapters */
  private String[] lstEnabledAdapters;   /* set of enabled adapters */
  private String[] cboEnabledAdapters;   /* enabled adapters for combo */  
  private String newScreenKey;           /* set a new key for a screen */

  /**
   * getter method for list of available adapters
   * @return String[] containing lstAvailableAdapters
   */
  public String[] getLstAvailableAdapters() {
    return lstAvailableAdapters;
  }

  /**
   * setter method for list of available adapters
   * @param lstAvailableAdapters
   */
  public void setLstAvailableAdapters(String[] lstAvailableAdapters) {
    this.lstAvailableAdapters = lstAvailableAdapters;
  }

  /**
   * getter method for list of enabled adapters
   * @return String[] containing lstEnabledAdapters
   */
  public String[] getLstEnabledAdapters() {
    return lstEnabledAdapters;
  }

  /**
   * setter method for list of enabled adapters
   * @param lstEnabledAdapters
   */
  public void setLstEnabledAdapters(String[] lstEnabledAdapters) {
    this.lstEnabledAdapters = lstEnabledAdapters;
  }
  
  public String toString(){
    String strTemp = null;
    strTemp += "\n\tEnabledAdapters : ";
    int size = (lstEnabledAdapters == null)?0:lstEnabledAdapters.length;
    for( int index = 0; index < size; index++ ){
      strTemp += lstEnabledAdapters[index];
      strTemp +=",";
    }
    strTemp += "\n\tAvailabeAdapters : ";
    size = ( lstAvailableAdapters == null )?0:lstAvailableAdapters.length;
    for( int index = 0; index < size; index++ ){
      strTemp += lstAvailableAdapters[index];
      strTemp +=",";
    }
    return strTemp;
  }

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

  /**
   * getter method for managed folder path
   * @return String containing txtFolderPath
   */
  public String getTxtFolderPath() {
    return txtFolderPath;
  }

  /**
   * setter method for managed folder path
   * @param txtFolderPath
   */
  public void setTxtFolderPath(String txtFolderPath) {
    this.txtFolderPath = txtFolderPath;
  }

  /**
   * getter method for check box status
   * @return boolean containing check box status
   */
  public boolean getEnableLookUp() {
    return enableLookUp;
  }

  /**
   * setter method for check box status
   * @param enableLookUp
   */
  public void setEnableLookUp(boolean enableLookUp) {
    this.enableLookUp = enableLookUp;
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

  /**
   * getter method for newScreenKey
   * @return String newScreenKey
   */
  public String getNewScreenKey() {
    return newScreenKey;
  }

  /**
   * setter method for newScreenKey
   * @param newScreenKey
   */
  public void setNewScreenKey(String newScreenKey) {
    this.newScreenKey = newScreenKey;
  }
}