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
 * $Id: FolderDocCopyMoveForm.java,v 20040220.13 2006/03/17 08:44:20 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
//Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of FolderDocCopyForm in 
 *           folder_doc_copy.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */ 
public class FolderDocCopyMoveForm extends ValidatorForm  {
    private Boolean hdnOverWrite;
    private String hdnTargetFolderPath;
    private Long[] chkFolderDocIds;
    private byte hdnActionType;

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }
    
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";

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

            strTemp += "\n\thdnTargetFolderPath : " + hdnTargetFolderPath;
            strTemp += "\n\thdnOverWrite : " + hdnOverWrite;
        }
        return strTemp;
    }

    public String getHdnTargetFolderPath() {
        return hdnTargetFolderPath;
    }

    public void setHdnTargetFolderPath(String newHdnTargetFolderPath) {
        hdnTargetFolderPath = newHdnTargetFolderPath;
    }

    public Boolean isHdnOverWrite() {
        return hdnOverWrite;
    }

    public void setHdnOverWrite(Boolean newHdnOverWrite) {
        hdnOverWrite = newHdnOverWrite;
    }

    public byte getHdnActionType() {
        return hdnActionType;
    }

    public void setHdnActionType(byte newHdnActionType) {
        hdnActionType = newHdnActionType;
    }
    
}
