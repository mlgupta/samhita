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
 * $Id: UserNewEditForm.java,v 20040220.9 2006/03/17 07:46:24 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.user;
//Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of 
 *           userNewEditForm in user_new.jsp and user_edit.jsp.
 *  @author             Sudheer Pujar
 *  @version            1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class UserNewEditForm extends ValidatorForm {
    /* member variables declaration goes here */
    private String txtAccessControlList;        /* ACL to be applied for the user */
    private String txtUserName;                 /* mandatory user name */
    private String txaDescription;              /* optional user description */  
    private String txtPassword;                 /* mandatory user password */
    private String txtConfirmPassword;          /* confirm user password */
    private String txtAddAGroup;                /* add this user to a group */
    private String txtQuota;                    /* allocated quota for this user */
    private String[] lstGroupOrUserList;        /* list of groups present */  
    private String[] cboCredentialManager;      /* credential manager for verifying credentials */
    private String radQuota;                    /* select quota allocation */
    private String radStatus="1";               /* by default user status is non admin */
    private String txtHomeFolder;               /* user's home folder name */
    private String txtMailFolder;               /* user's mail folder name */
    private String txtEmailAddress;             /* email id for the user */  
    private String txtDefaultDocumentAcl;       /* default acl to be applied on documents created by this user */  
    private String txtDefaultFolderAcl;         /* default acl to be applied on folders created by this user */
    private String[] cboCharacterSet;           /* list of character sets supported */
    private String[] cboLanguage;               /* list of languages supported */  
    private String[] cboLocale;                 /* list of locales supported */
    private String[] cboTimeZone;               /* list of timezones supported */
    private String[] txtWorkFlowAcl;            /* list of workflow acls supported */
    /**
     * Purpose : Returns cboCredentialManager.
     * @return : String
     */
    public String[] getCboCredentialManager() {
        return cboCredentialManager;
    }

    /**
     * Purpose : Sets the value of cboCredentialManager.
     * @param  : newCboCredentialManager Value of cboCredentialManager from the form
     */
    public void setCboCredentialManager(String[] newCboCredentialManager) {
        cboCredentialManager = newCboCredentialManager;
    }

    /**
     * Purpose : Returns lstGroupList.
     * @return : String
     */
    public String[] getLstGroupOrUserList() {
        return lstGroupOrUserList;
    }

    /**
     * Purpose : Sets the value of lstGroupList.
     * @param  : newLstGroupList Value of lstGroupList from the form
     */
    public void setLstGroupOrUserList(String[] newLstGroupOrUserList) {
        lstGroupOrUserList = newLstGroupOrUserList;
    }

    /**
     * Purpose : Returns radQuota.
     * @return : String
     */
    public String getRadQuota() {
        return radQuota;
    }

    /**
     * Purpose : Sets the value of radQuota.
     * @param  : newRadQuota Value of radQuota from the form
     */
    public void setRadQuota(String newRadQuota) {
        radQuota = newRadQuota;
    }

    /**
     * Purpose : Returns radStatus.
     * @return : String
     */
    public String getRadStatus() {
        return radStatus;
    }

    /**
     * Purpose : Sets the value of radStatus.
     * @param  : newRadStatus Value of radStatus from the form
     */
    public void setRadStatus(String newRadStatus) {
        radStatus = newRadStatus;
    }

    /**
     * Purpose : Returns txaDescription.
     * @return : String
     */
    public String getTxaDescription() {
        return txaDescription;
    }

    /**
     * Purpose : Sets the value of txaDescription.
     * @param  : newTxaDescription Value of txaDescription from the form
     */
    public void setTxaDescription(String newTxaDescription) {
        txaDescription = newTxaDescription;
    }

    /**
     * Purpose : Returns txtAccessControlList.
     * @return : String
     */
    public String getTxtAccessControlList() {
        return txtAccessControlList;
    }

    /**
     * Purpose : Sets the value of txtAccessControlList.
     * @param  : newTxtAccessControlList Value of txtAccessControlList from the form
     */
    public void setTxtAccessControlList(String newTxtAccessControlList) {
        txtAccessControlList = newTxtAccessControlList;
    }
    
    /**
     * Purpose : Returns txtAddAGroup.
     * @return : String
     */
    public String getTxtAddAGroup() {
        return txtAddAGroup;
    }

    /**
     * Purpose : Sets the value of txtAddAGroup.
     * @param  : newTxtAddAGroup Value of txtAddAGroup from the form
     */
    public void setTxtAddAGroup(String newTxtAddAGroup) {
        txtAddAGroup = newTxtAddAGroup;
    }

    /**
     * Purpose : Returns txtConfirmPassword.
     * @return : String
     */
    public String getTxtConfirmPassword() {
        return txtConfirmPassword;
    }
    
    /**
     * Purpose : Sets the value of txtConfirmPassword.
     * @param  : newTxtConfirmPassword Value of txtConfirmPassword from the form
     */
    public void setTxtConfirmPassword(String newTxtConfirmPassword) {
        txtConfirmPassword = newTxtConfirmPassword;
    }

    /**
     * Purpose : Returns txtPassword.
     * @return : String
     */
    public String getTxtPassword() {
        return txtPassword;
    }

    /**
     * Purpose : Sets the value of txtPassword.
     * @param  : newTxtPassword Value of txtPassword from the form
     */
    public void setTxtPassword(String newTxtPassword) {
        txtPassword = newTxtPassword;
    }

    /**
     * Purpose : Returns txtUserName.
     * @return : String
     */

    public String getTxtUserName() {
        return txtUserName;
    }

    /**
     * Purpose : Sets the value of txtUserName.
     * @param  : newTxtUserName Value of txtUserName from the form
     */
    public void setTxtUserName(String newTxtUserName) {
        txtUserName = newTxtUserName;
    }

    /**
     * Purpose : Returns txtQuota.
     * @return : String
     */
    public String getTxtQuota() {
        return txtQuota;
    }

    /**
     * Purpose : Sets the value of txtQuota.
     * @param  : newTxtQuota Value of txtQuota from the form
     */
    public void setTxtQuota(String newTxtQuota) {
        txtQuota = newTxtQuota;
    }

    /**
     * Purpose : Returns cboCharacterSet.
     * @return : String
     */
    public String[] getCboCharacterSet() {
        return cboCharacterSet;
    }

    /**
     * Purpose : Sets the value of cboCharacterSet.
     * @param  : newCboCharacterSet Value of cboCharacterSet from the form
     */
    public void setCboCharacterSet(String[] newCboCharacterSet) {
        cboCharacterSet = newCboCharacterSet;
    }

    /**
     * Purpose : Returns cboLanguage.
     * @return : String
     */
    public String[] getCboLanguage() {
        return cboLanguage;
    }

    /**
     * Purpose : Sets the value of cboLanguage.
     * @param  : newCboLanguage Value of cboLanguage from the form
     */
    public void setCboLanguage(String[] newCboLanguage) {
        cboLanguage = newCboLanguage;
    }

    /**
     * Purpose : Returns cboLocale.
     * @return : String
     */
    public String[] getCboLocale() {
        return cboLocale;
    }

    /**
     * Purpose : Sets the value of cboLocale.
     * @param  : newCboLocale Value of cboLocale from the form
     */
    public void setCboLocale(String[] newCboLocale) {
        cboLocale = newCboLocale;
    }

    /**
     * Purpose : Returns cboTimeZone.
     * @return : String
     */
    public String[] getCboTimeZone() {
        return cboTimeZone;
    }

    /**
     * Purpose : Sets the value of cboTimeZone.
     * @param  : newCboTimeZone Value of cboTimeZone from the form
     */
    public void setCboTimeZone(String[] newCboTimeZone) {
        cboTimeZone = newCboTimeZone;
    }

    /**
     * Purpose : Returns txtDefaultDocumentAcl.
     * @return : String
     */
    public String getTxtDefaultDocumentAcl() {
        return txtDefaultDocumentAcl;
    }

    /**
     * Purpose : Sets the value of txtDefaultDocumentAcl.
     * @param  : newTxtDefaultDocumentAcl Value of txtDefaultDocumentAcl from the form
     */
    public void setTxtDefaultDocumentAcl(String newTxtDefaultDocumentAcl) {
        txtDefaultDocumentAcl = newTxtDefaultDocumentAcl;
    }

    /**
     * Purpose : Returns txtDefaultFolderAcl.
     * @return : String
     */
    public String getTxtDefaultFolderAcl() {
        return txtDefaultFolderAcl;
    }

    /**
     * Purpose : Sets the value of txtDefaultFolderAcl.
     * @param  : newTxtDefaultFolderAcl Value of txtDefaultFolderAcl from the form
     */
    public void setTxtDefaultFolderAcl(String newTxtDefaultFolderAcl) {
        txtDefaultFolderAcl = newTxtDefaultFolderAcl;
    }

    /**
     * Purpose : Returns txtEmailAddress.
     * @return : String
     */
    public String getTxtEmailAddress() {
        return txtEmailAddress;
    }

    /**
     * Purpose : Sets the value of txtEmailAddress.
     * @param  : newTxtEmailAddress Value of txtEmailAddress from the form
     */
    public void setTxtEmailAddress(String newTxtEmailAddress) {
        txtEmailAddress = newTxtEmailAddress;
    }

    /**
     * Purpose : Returns txtMailFolder.
     * @return : String
     */
    public String getTxtMailFolder() {
        return txtMailFolder;
    }

    /**
     * Purpose : Sets the value of txtMailFolder.
     * @param  : newTxtMailFolder Value of txtMailFolder from the form
     */
    public void setTxtMailFolder(String newTxtMailFolder) {
        txtMailFolder = newTxtMailFolder;
    }

    /**
     * Purpose : Returns txtHomeFolder.
     * @return : String
     */
    public String getTxtHomeFolder() {
        return txtHomeFolder;
    }

    /**
     * Purpose : Sets the value of txtHomeFolder.
     * @param  : newTxtHomeFolder Value of txtHomeFolder from the form
     */
    public void setTxtHomeFolder(String newTxtHomeFolder) {
        txtHomeFolder = newTxtHomeFolder;
    }

    /**
     * Purpose : Returns txtWorkFlowAcl.
     * @return : String
     */
    public String[] getTxtWorkFlowAcl() {
    return txtWorkFlowAcl;
    }

    /**
     * Purpose : Sets the value of txtWorkFlowAcl.
     * @param  : newTxtWorkFlowAcl Value of txtWorkFlowAcl from the form
     */  
    public void setTxtWorkFlowAcl(String[] txtWorkFlowAcl) {
    this.txtWorkFlowAcl = txtWorkFlowAcl;
    }
     
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            strTemp += "\n\ttxtAccessControlList : " + txtAccessControlList;
            strTemp += "\n\ttxtUserName : " + txtUserName;
            strTemp += "\n\ttxaDescription : " + txaDescription;
            strTemp += "\n\ttxtPassword : " + txtPassword;

            strTemp += "\n\ttxtConfirmPassword : " + txtConfirmPassword;
            strTemp += "\n\ttxtAddAGroup : " + txtAddAGroup;
            strTemp += "\n\ttxtQuota : " + txtQuota;
        
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

            if(cboCredentialManager != null){
                strArrayValues = "{";
                for(int index = 0; index < cboCredentialManager.length; index++){
                    strArrayValues += " " + cboCredentialManager[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboCredentialManager : " + strArrayValues;
            }else{
                strTemp += "\n\tcboCredentialManager : " + cboCredentialManager;
            }
        
            strTemp += "\n\tradQuota : " + radQuota;
            strTemp += "\n\tradStatus : " + radStatus;
            strTemp += "\n\ttxtHomeFolder : " + txtHomeFolder;
            strTemp += "\n\ttxtMailFolder : " + txtMailFolder;

            strTemp += "\n\ttxtEmailAddress : " + txtEmailAddress;
            strTemp += "\n\ttxtDefaultDocumentAcl : " + txtDefaultDocumentAcl;
            strTemp += "\n\ttxtDefaultFolderAcl : " + txtDefaultFolderAcl;
            //strTemp += "\n\ttxtWorkFlowAcl: "+txtWorkFlowAcl;
            if( txtWorkFlowAcl!=null && txtWorkFlowAcl.length > 0 ){
              strTemp += "\n\ttxtWorkFlowAcl: ";
              for( int index = 0; index < txtWorkFlowAcl.length; index++ ){
                strTemp += txtWorkFlowAcl[index];
                if( !(index == txtWorkFlowAcl.length - 1) ){
                  strTemp += ",";
                }
              }
            }else{
              strTemp += "\n\ttxtWorkFlowAcl: WORKFLOW_NOT_ENABLED";
            }

            if(cboCharacterSet != null){
                strArrayValues = "{";
                for(int index = 0; index < cboCharacterSet.length; index++){
                    strArrayValues += " " + cboCharacterSet[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboCharacterSet : " + strArrayValues;
            }else{
                strTemp += "\n\tcboCharacterSet : " + cboCharacterSet;
            }

            if(cboLanguage != null){
                strArrayValues = "{";
                for(int index = 0; index < cboLanguage.length; index++){
                    strArrayValues += " " + cboLanguage[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboLanguage : " + strArrayValues;
            }else{
                strTemp += "\n\tcboLanguage : " + cboLanguage;
            }

            if(cboLocale != null){
                strArrayValues = "{";
                for(int index = 0; index < cboLocale.length; index++){
                    strArrayValues += " " + cboLocale[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboLocale : " + strArrayValues;
            }else{
                strTemp += "\n\tcboLocale : " + cboLocale;
            }

            if(cboTimeZone != null){
                strArrayValues = "{";
                for(int index = 0; index < cboTimeZone.length; index++){
                    strArrayValues += " " + cboTimeZone[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboTimeZone : " + strArrayValues;
            }else{
                strTemp += "\n\tcboTimeZone : " + cboTimeZone;
            }
        }
        return strTemp;
    }
}
