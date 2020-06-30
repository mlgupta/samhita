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
 * $Id: GroupNewEditForm.java,v 20040220.7 2006/03/16 07:37:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.group; 
 //Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of
 *           groupNewEditForm in group_new.jsp and group_edit.jsp
 *  @author             Mishra Maneesh
 *  @version            1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GroupNewEditForm extends ValidatorForm {
    private String txtGroupName;
    private String txaDescription;
    private String txtAddAUser;
    private String[] lstGroupOrUserList;
    private String txtAccessControlList;
    private String radUserGroup="0";
    private String txtGroupOrUserName;

    /**
     * Purpose   : Returns  radContentQuotaType.
     * @return   : String
     */
    public String getTxtGroupName() {
        return txtGroupName;
    }
  
    /**
     * Purpose   : Sets the value of txtGroupName.
     * @param    : newTxtGroupName Value of txtGroupName from the form
     */
    public void setTxtGroupName(String newTxtGroupName){
        txtGroupName = newTxtGroupName;
    }

    /**
     * Purpose   :  Returns  txaDescription.
     * @return   : String
     */
    public String getTxaDescription(){
        return txaDescription;
    }

    /**
     * Purpose   : Sets the value of txaDescription.
     * @param    : newTxaDescription Value of txaDescription from the form
     */
    public void setTxaDescription(String newTxaDescription){
        txaDescription = newTxaDescription;
    }
  


  

    /**
     * Purpose   : Returns  txtAddAUser.
     * @return   : String
     */
    public String getTxtAddAUser(){
        return txtAddAUser;
    }

    /**
     * Purpose   : Sets the value of txtAddAUser.
     * @param    : newTxtAddAUser Value of txtAddAUser from the form
     */
    public void setTxtAddAUser(String newTxtAddAUser){
        txtAddAUser = newTxtAddAUser;
    }

    /**
     * Purpose   : Returns  lstUserList array.
     * @return String[]
     */
    public String[] getLstGroupOrUserList(){
        return lstGroupOrUserList;
    }

    /**
     * Purpose   : Sets  lstUserList array.
     * @param    : newLstUserList Value of lstUserList from the form
     */
    public void setLstGroupOrUserList(String[] newLstGroupOrUserList){
        lstGroupOrUserList = newLstGroupOrUserList;
    }

    /**
     * Purpose   : Returns  cboAccessControlList.
     * @return   : String
     */
    public String getTxtAccessControlList(){
          return txtAccessControlList;
    }
  
    /**
     * Purpose   : Sets the cboAccessControlList.
     * @param    : newCboAccessControlList Value of cboAccessControlList from the form
     */
    public void setTxtAccessControlList(String newTxtAccessControlList){
        txtAccessControlList = newTxtAccessControlList;
    }

    /**
     * Purpose   : Returns  radUserGroup.
     * @return   : String
     */
    public String getRadUserGroup(){
        return radUserGroup;
    }

    /**
     * Purpose   : Sets the radUserGroup.
     * @param    : newRadUserGroup Value of radUserGroup from the form
     */
    public void setRadUserGroup(String newRadUserGroup){
        radUserGroup = newRadUserGroup;
    }

    /**
     * Purpose   : Returns  txtGroupOrUserName.
     * @return   : String
     */
    public String getTxtGroupOrUserName() {
        return txtGroupOrUserName;
    }

    /**
     * Purpose   : Sets the txtGroupOrUserName.
     * @param    : newTxtGroupOrUserName Value of txtGroupOrUserName from the form
     */
    public void setTxtGroupOrUserName(String newTxtGroupOrUserName) {
        txtGroupOrUserName = newTxtGroupOrUserName;
    }

    public String toString(){

        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            strTemp += "\n\ttxtGroupName : " + txtGroupName;
            strTemp += "\n\ttxaDescription : " + txaDescription;
            strTemp += "\n\ttxtAddAUser : " + txtAddAUser;
        
            if(lstGroupOrUserList != null){
                strArrayValues = "{";
                for(int index = 0; index < lstGroupOrUserList.length; index++){
                    strArrayValues += " " + lstGroupOrUserList[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstGroupOrUserList : " + strArrayValues;
            }else{
                strTemp += "\n\tlstGroupOrUserList : " + lstGroupOrUserList;
            }
            strTemp += "\n\ttxtAccessControlList : " + txtAccessControlList;
            strTemp += "\n\tradUserGroup : " + radUserGroup;
            strTemp += "\n\ttxtGroupOrUserName : " + txtGroupOrUserName;
        }
        return strTemp;
    }
}
