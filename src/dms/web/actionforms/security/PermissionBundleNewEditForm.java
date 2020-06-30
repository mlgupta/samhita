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
 * $Id: PermissionBundleNewEditForm.java,v 20040220.4 2006/03/17 06:39:07 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.security;
 //Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of html controls of PermissionBundleNewEditForm 
 *           in permission_bundle_new.jsp and permission_bundle_edit.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class PermissionBundleNewEditForm extends ValidatorForm {
    private String txtPermissionBundleName;
    private String txaPermissionBundleDescription;
    private String [] lstAvailablePermission;
    private String [] lstSelectedPermission;

    /**
     * Purpose   : Returns txtPermissionBundleName.
     * @return   : String
     */
    public String getTxtPermissionBundleName() {
        return txtPermissionBundleName;
    }

    /**
     * Purpose   : Sets the value of txtPermissionBundleName.
     * @param    : newTxtPermissionBundleName Value of txtPermissionBundleName from the form
     */
    public void setTxtPermissionBundleName(String newTxtPermissionBundleName) {
        txtPermissionBundleName = newTxtPermissionBundleName;
    }

    /**
     * Purpose   : Returns txaPermissionBundleDescription.
     * @return   : String
     */
    public String getTxaPermissionBundleDescription() {
        return txaPermissionBundleDescription;
    }

    /**
     * Purpose   : Sets the value of txaPermissionBundleDescription.
     * @param    : newTxaPermissionBundleDescription Value of txaPermissionBundleDescription from the form
     */
    public void setTxaPermissionBundleDescription(String newTxaPermissionBundleDescription) {
        txaPermissionBundleDescription = newTxaPermissionBundleDescription;
    }

    /**
     * Purpose   : Returns lstAvailablePermission.
     * @return   : String Array
     */
    public String[] getLstAvailablePermission() {
        return lstAvailablePermission;
    }

    /**
     * Purpose   : Sets the value of lstAvailablePermission.
     * @param    : newLstAvailablePermission Value of lstAvailablePermission from the form
     */
    public void setLstAvailablePermission(String[] newLstAvailablePermission) {
        lstAvailablePermission = newLstAvailablePermission;
    }

    /**
     * Purpose   : Returns lstSelectedPermission.
     * @return   : String Array
     */
    public String[] getLstSelectedPermission() {
        return lstSelectedPermission;
    }

    /**
     * Purpose   : Sets the value of lstSelectedPermission.
     * @param    : newLstSelectedPermission Value of lstSelectedPermission from the form
     */
    public void setLstSelectedPermission(String[] newLstSelectedPermission) {
        lstSelectedPermission = newLstSelectedPermission;
    }
    
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            strTemp += "\n\ttxtPermissionBundleName : " + txtPermissionBundleName;
            strTemp += "\n\ttxaPermissionBundleDescription : " + txaPermissionBundleDescription;
        
            if(lstAvailablePermission != null){
                strArrayValues = "{";
                for(int index = 0; index < lstAvailablePermission.length; index++){
                    strArrayValues += " " + lstAvailablePermission[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstAvailablePermission : " + strArrayValues;
            }else{
                strTemp += "\n\tlstAvailablePermission : " + lstAvailablePermission;
            }

            if(lstSelectedPermission != null){
                strArrayValues = "{";
                for(int index = 0; index < lstSelectedPermission.length; index++){
                    strArrayValues += " " + lstSelectedPermission[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstSelectedPermission : " + strArrayValues;
            }else{
                strTemp += "\n\tlstSelectedPermission : " + lstSelectedPermission;
            }
        }
        return strTemp;
    }
}
