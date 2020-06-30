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
 * $Id: FolderDocPropertyForm.java,v 20040220.6 2006/03/16 07:00:43 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/* Struts API */
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: To store options for displaying property of folder(s) and document(s).
 * @author             Rajan Kamal Gupta
 * @version            1.0
 * Date of creation:   09-01-2004
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class FolderDocPropertyForm extends ActionForm  {
    public static final byte ONE_DOCUMENT = 1;
    public static final byte ONLY_DOCUMENT = 2;
    public static final byte ONE_FOLDER = 3;
    public static final byte ONLY_FOLDER = 4;
    public static final byte FOLDER_AND_DOCUMENT = 5;
     
    private String txtFolderDocName;
    private Long[] hdnFolderDocIds;
    private String txtAclName;
    private Long[] chkFolderDocIds;
    private int hdnPropertyType;
    Boolean aclError;

    /**
     * Reset all properties to their default values.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /**
     * Validate all properties to their default values.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return ActionErrors A list of all errors found.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public String getTxtFolderDocName() {
        return txtFolderDocName;
    }

    public void setTxtFolderDocName(String newTxtFolderDocName) {
        txtFolderDocName = newTxtFolderDocName;
    }

    public Long[] getHdnFolderDocIds() {
        return hdnFolderDocIds;
    }

    public void setHdnFolderDocIds(Long[] newHdnFolderDocIds) {
        hdnFolderDocIds = newHdnFolderDocIds;
    }

    public String getTxtAclName() {
        return txtAclName;
    }

    public void setTxtAclName(String newTxtAclName) {
        txtAclName = newTxtAclName;
    }

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
        hdnFolderDocIds = newChkFolderDocIds;
    }

    public int getHdnPropertyType() {
        return hdnPropertyType;
    }

    public void setHdnPropertyType(int newHdnPropertyType) {
        hdnPropertyType = newHdnPropertyType;
    }

    public String toString(){
    
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            strTemp += "\n\ttxtFolderDocName : " + txtFolderDocName;

            if(hdnFolderDocIds != null){
                strArrayValues = "{";
                for(int index = 0; index < hdnFolderDocIds.length; index++){
                    strArrayValues += " " + hdnFolderDocIds[index];
                }
                strArrayValues += "}";
                strTemp += "\n\thdnFolderDocIds : " + strArrayValues;
            }else{
                strTemp += "\n\thdnFolderDocIds : " + hdnFolderDocIds;
            }
            
            strTemp += "\n\ttxtAclName : " + txtAclName;
            
            if(chkFolderDocIds != null){
                strArrayValues = "{";
                for(int index = 0; index < chkFolderDocIds.length; index++){
                    strArrayValues += " " + chkFolderDocIds[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tchkFolderDocIds : " + strArrayValues;
            }else{
                strTemp += "\n\tchkFolderDocIds : " + chkFolderDocIds;
            }

            switch (hdnPropertyType){
                case FolderDocPropertyForm.FOLDER_AND_DOCUMENT:
                    strTemp += "\n\thdnPropertyType : FolderDocPropertyForm.FOLDER_AND_DOCUMENT";
                    break;
                case FolderDocPropertyForm.ONE_DOCUMENT:
                    strTemp += "\n\thdnPropertyType : FolderDocPropertyForm.ONE_DOCUMENT";
                    break;
                case FolderDocPropertyForm.ONE_FOLDER:
                    strTemp += "\n\thdnPropertyType : FolderDocPropertyForm.ONE_FOLDER";
                    break;
                case FolderDocPropertyForm.ONLY_DOCUMENT:
                    strTemp += "\n\thdnPropertyType : FolderDocPropertyForm.ONLY_DOCUMENT";
                    break;
                case FolderDocPropertyForm.ONLY_FOLDER:
                    strTemp += "\n\thdnPropertyType : FolderDocPropertyForm.ONLY_FOLDER";
                    break;
                default:
                    strTemp += "\n\thdnPropertyType : " + hdnPropertyType;
            }
        }
        return strTemp;
    }

    public Boolean isAclError() {
        return aclError;
    }

    public void setAclError(Boolean newAclError) {
        aclError = newAclError;
    }

}
