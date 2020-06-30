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
 * $Id: AclNewEditForm.java,v 20040220.6 2006/03/17 06:39:07 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.security;
//Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of
 *           AclNewEditForm in acl_edit.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class AclNewEditForm extends ValidatorForm {
  private String txtAclName;
  private String txaAclDescription;
  private String txtOwnerName;
  private String txtAccessControlList;
  private String cboUserGroup;
  private String txtAddUserGroupAcl;
  private String[] chkSelect;
  private String[] chkSelectAll;
  private String cboAclType;
  private String hdnPrefix;
  private String hdnAdapterName;
//    private boolean chkIsShared;

  /**
   * Purpose : Returns  txtAclName.
   * @return : String
   */   
  public String getTxtAclName() {
      return txtAclName;
  }

  /**
   * Purpose : Sets the value of txtAclName.
   * @param  : newTxtAclName Value of txtAclName from the form
   */
  public void setTxtAclName(String newTxtAclName) {
      txtAclName = newTxtAclName;
  }

  /**
   * Purpose :  Returns  txaAclDescription.
   * @return :  String
   */
  public String getTxaAclDescription() {
      return txaAclDescription;
  }

  /**
   * Purpose : Sets the value of txaAclDescription.
   * @param  : newTxaAclDescription Value of txaAclDescription from the form
   */
  public void setTxaAclDescription(String newTxaAclDescription) {
      txaAclDescription = newTxaAclDescription;
  }

  /**
   * Purpose : Returns  txtOwnerName.
   * @return : String
   */
  public String getTxtOwnerName() {
      return txtOwnerName;
  }

  /**
   * Purpose : Sets the value of txtOwnerName.
   * @param  : newTxtOwnerName Value of txtOwnerName from the form
   */
  public void setTxtOwnerName(String newTxtOwnerName) {
      txtOwnerName = newTxtOwnerName;
  }

  /**
   * Purpose : Returns  txtSecuringAclName.
   * @return : String
   */
  public String getTxtAccessControlList() {
      return txtAccessControlList;
  }

  /**
   * Purpose : Sets the value of txtSecuringAclName.
   * @param  : newTxtSecuringAclName Value of txtSecuringAclName from the form
   */
  public void setTxtAccessControlList(String newTxtAccessControlList) {
      txtAccessControlList = newTxtAccessControlList;
  }

  /**
   * Purpose : Returns  chkIsShared.
   * @return : Boolean
   */
/*    public boolean isChkIsShared() {
      return chkIsShared;
  }
*/
  /**
   * Purpose : Sets the value of chkIsShared.
   * @param  : newChkIsShared Value of chkIsShared from the form
   */
/*    public void setChkIsShared(boolean newChkIsShared) {
      chkIsShared = newChkIsShared;
  }
*/
  /**
   * Purpose : Returns  cboUserGroup.
   * @return : String
   */
  public String getCboUserGroup() {
      return cboUserGroup;
  }

  /**
   * Purpose : Sets the value of cboUserGroup.
   * @param  : newCboUserGroup Value of cboUserGroup from the form
   */
  public void setCboUserGroup(String newCboUserGroup) {
      cboUserGroup = newCboUserGroup;
  }

  /**
   * Purpose : Returns  txtAddUserGroupAcl.
   * @return : String
   */
  public String getTxtAddUserGroupAcl() {
      return txtAddUserGroupAcl;
  }

  /**
   * Purpose : Sets the value of txtAddUserGroupAcl.
   * @param  : newTxtAddUserGroupAcl Value of txtAddUserGroupAcl from the form
   */
  public void setTxtAddUserGroupAcl(String newTxtAddUserGroupAcl) {
      txtAddUserGroupAcl = newTxtAddUserGroupAcl;
  }

  /**
   * Purpose : Returns  chkSelect.
   * @return : String[]
   */
  public String[] getChkSelect() {
      return chkSelect;
  }

  /**
   * Purpose : Sets the value of chkSelect.
   * @param  : newChkSelect Value of chkSelect from the form
   */
  public void setChkSelect(String[] newChkSelect) {
      chkSelect = newChkSelect;
  }

  /**
   * Purpose : Returns chkSelectAll.
   * @return : String
   */
  public String[] getChkSelectAll() {
      return chkSelectAll;
  }

  /**
   * Purpose : Sets the value of chkSelectAll.
   * @param  : newChkSelectAll Value of chkSelectAll from the form
   */
  public void setChkSelectAll(String[] newChkSelectAll) {
      chkSelectAll = newChkSelectAll;
  }

  public String toString(){
      String strTemp = "";
      Logger logger = Logger.getLogger("DbsLogger");
      if(logger.getLevel() == Level.DEBUG){
          strTemp += "\n\ttxtAclName : " + txtAclName;
          strTemp += "\n\ttxaAclDescription : " + txaAclDescription;
          strTemp += "\n\ttxtOwnerName : " + txtOwnerName;
          strTemp += "\n\ttxtAccessControlList : " + txtAccessControlList;

          strTemp += "\n\tcboUserGroup : " + cboUserGroup;
          strTemp += "\n\ttxtAddUserGroupAcl : " + txtAddUserGroupAcl;
//          strTemp += "\n\tchkIsShared : " + chkIsShared;
      }       
      return strTemp;
  }

  /**
   * Purpose : Gets the value of cboAclType.
   * @return : String cboAclType
   */
  public String getCboAclType() {
    return cboAclType;
  }

  /**
   * Purpose : Sets the value of cboAclType.
   * @param  : cboAclType
   */
  public void setCboAclType(String cboAclType) {
    this.cboAclType = cboAclType;
  }

  /**
   * Purpose : Gets the value of hdnPrefix.
   * @return : String hdnPrefix
   */
  public String getHdnPrefix() {
    return hdnPrefix;
  }

  /**
   * Purpose : Sets the value of hdnPrefix.
   * @param  : hdnPrefix
   */
  public void setHdnPrefix(String hdnPrefix) {
    this.hdnPrefix = hdnPrefix;
  }

  /**
   * Purpose : Gets the value of hdnAdapterName.
   * @return : String hdnAdapterName
   */
  public String getHdnAdapterName() {
    return hdnAdapterName;
  }

  /**
   * Purpose : Sets the value of hdnAdapterName.
   * @param  : hdnAdapterName
   */
  public void setHdnAdapterName(String hdnAdapterName) {
    this.hdnAdapterName = hdnAdapterName;
  }
}
