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
 * $Id: PermissionForm.java,v 20040220.4 2006/03/17 06:39:07 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.security;
//Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of PermissionForm in 
 *           permission.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class PermissionForm extends ValidatorForm {
    private String radPermission;
    private String [] lstAvailablePermissionBundle;
    private String [] lstSelectedPermissionBundle;    
    private String [] lstAvailableAccessLevel;
    private String [] lstSelectedAccessLevel; 
    private String granteeType;
    private String granteeName;
    private String aclName;
    private String index;
    private String isNew;
    private String isNewAcl;
    
    /**
     * Purpose   : Returns lstAvailableAccessLevel.
     * @return   : String Array
     */
    public String[] getLstAvailableAccessLevel() {
        return lstAvailableAccessLevel;
    }

    /**
     * Purpose   : Sets the value of lstAvailableAccessLevel.
     * @param    : newLstAvailableAccessLevel Value of lstAvailableAccessLevel from the form
     */
    public void setLstAvailableAccessLevel(String[] newLstAvailableAccessLevel) {
        lstAvailableAccessLevel = newLstAvailableAccessLevel;
    }

    /**
     * Purpose   : Returns lstAvailablePermissionBundle.
     * @return   : String Array
     */
    public String[] getLstAvailablePermissionBundle() {
        return lstAvailablePermissionBundle;
    }

    /**
     * Purpose   : Sets the value of lstAvailablePermissionBundle.
     * @param    : newLstAvailablePermissionBundle Value of lstAvailablePermissionBundle from the form
     */
    public void setLstAvailablePermissionBundle(String[] newLstAvailablePermissionBundle) {
        lstAvailablePermissionBundle = newLstAvailablePermissionBundle;
    }

    /**
     * Purpose   : Returns lstSelectedAccessLevel.
     * @return   : String Array
     */
    public String[] getLstSelectedAccessLevel() {
        return lstSelectedAccessLevel;
    }

    /**
     * Purpose   : Sets the value of lstSelectedAccessLevel.
     * @param    : newLstSelectedAccessLevel Value of lstSelectedAccessLevel from the form
     */
    public void setLstSelectedAccessLevel(String[] newLstSelectedAccessLevel) {
        lstSelectedAccessLevel = newLstSelectedAccessLevel;
    }

    /**
     * Purpose   : Returns lstSelectedPermissionBundle.
     * @return   : String Array
     */
    public String[] getLstSelectedPermissionBundle() {
        return lstSelectedPermissionBundle;
    }

    /**
     * Purpose   : Sets the value of lstSelectedPermissionBundle.
     * @param    : newLstSelectedPermissionBundle Value of lstSelectedPermissionBundle from the form
     */
    public void setLstSelectedPermissionBundle(String[] newLstSelectedPermissionBundle) {
        lstSelectedPermissionBundle = newLstSelectedPermissionBundle;
    }

    /**
     * Purpose   : Returns radPermission.
     * @return   : String
     */
    public String getRadPermission() {
      return radPermission;
    }

    /**
     * Purpose : Sets the value of radPermission.
     * @param  : newRadPermission Value of radPermission from the form
     */
    public void setRadPermission(String newRadPermission) {
      radPermission = newRadPermission;
    }

    /**
     * Purpose   : Returns granteeType.
     * @return   : String
     */
    public String getGranteeType() {
        return granteeType;
    }

    /**
     * Purpose : Sets the value of radPermission.
     * @param  : newGranteeType Value of granteeType from the form
     */
    public void setGranteeType(String newGranteeType) {
        granteeType = newGranteeType;
    }

    /**
     * Purpose   : Returns granteeName.
     * @return   : String
     */
    public String getGranteeName() {
        return granteeName;
    }

    /**
     * Purpose : Sets the value of granteeName.
     * @param  : newGranteeName Value of granteeName from the form
     */
    public void setGranteeName(String newGranteeName) {
        granteeName = newGranteeName;
    }

    /**
     * Purpose   : Returns aclName.
     * @return   : String
     */
    public String getAclName() {
        return aclName;
    }

    /**
     * Purpose : Sets the value of aclName.
     * @param  : newAclName Value of aclName from the form
     */
    public void setAclName(String newAclName) {
        aclName = newAclName;
    }

    /**
     * Purpose   : Returns index.
     * @return   : String
     */
    public String getIndex() {
        return index;
    }

    /**
     * Purpose : Sets the value of index.
     * @param  : newIndex Value of index from the form
     */
    public void setIndex(String newIndex) {
        index = newIndex;
    }

    /**
     * Purpose   : Returns isNew.
     * @return   : String
     */
    public String getIsNew() {
        return isNew;
    }

    /**
     * Purpose : Sets the value of isNew.
     * @param  : newIsNew Value of isNew from the form
     */
    public void setIsNew(String newIsNew) {
        isNew = newIsNew;
    }

    /**
     * Purpose   : Returns isNewAcl.
     * @return   : String
     */
    public String getIsNewAcl() {
        return isNewAcl;
    }

    /**
     * Purpose : Sets the value of isNewAcl.
     * @param  : newIsNewAcl Value of isNewAcl from the form
     */
    public void setIsNewAcl(String newIsNewAcl) {
        isNewAcl = newIsNewAcl;
    }
        
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){

            String strArrayValues = "";
            strTemp += "\n\tradPermission : " + radPermission;

            if(lstAvailablePermissionBundle != null){
                strArrayValues = "{";
                for(int index = 0; index < lstAvailablePermissionBundle.length; index++){
                    strArrayValues += " " + lstAvailablePermissionBundle[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstAvailablePermissionBundle : " + strArrayValues;
            }else{
                strTemp += "\n\tlstAvailablePermissionBundle : " + lstAvailablePermissionBundle;
            }

            if(lstSelectedPermissionBundle != null){
                strArrayValues = "{";
                for(int index = 0; index < lstSelectedPermissionBundle.length; index++){
                    strArrayValues += " " + lstSelectedPermissionBundle[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstSelectedPermissionBundle : " + strArrayValues;
            }else{
                strTemp += "\n\tlstSelectedPermissionBundle : " + lstSelectedPermissionBundle;
            }

            if(lstAvailableAccessLevel != null){
                strArrayValues = "{";
                for(int index = 0; index < lstAvailableAccessLevel.length; index++){
                    strArrayValues += " " + lstAvailableAccessLevel[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstAvailableAccessLevel : " + strArrayValues;
            }else{
                strTemp += "\n\tlstAvailableAccessLevel : " + lstAvailableAccessLevel;
            }

            if(lstSelectedAccessLevel != null){
                strArrayValues = "{";
                for(int index = 0; index < lstSelectedAccessLevel.length; index++){
                    strArrayValues += " " + lstSelectedAccessLevel[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tlstSelectedAccessLevel : " + strArrayValues;
            }else{
                strTemp += "\n\tlstSelectedAccessLevel : " + lstSelectedAccessLevel;
            }

            strTemp += "\n\tgranteeType : " + granteeType; 
            strTemp += "\n\tgranteeName : " + granteeName; 
            strTemp += "\n\taclName : " + aclName; 
            strTemp += "\n\tindex : " + index; 
            strTemp += "\n\tisNew : " + isNew; 
            strTemp += "\n\tisNewAcl : " + isNewAcl; 
        }
        return strTemp;
    }
}
