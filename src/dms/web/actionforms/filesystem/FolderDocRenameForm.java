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
 * $Id: FolderDocRenameForm.java,v 20040220.5 2006/03/16 07:01:41 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
 //Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of
 *  FolderDocRenameForm in folder_doc_rename.jsp
 * 
 * @author              Rajan Kamal Gupta
 * @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class FolderDocRenameForm extends ValidatorForm  {
    private Long[] txtId;
    private String[] txtNewName;
    private String[] txtNewDesc;

    public Long[] getTxtId() {
        return txtId;
    }

    public void setTxtId(Long[] newTxtId) {
        txtId = newTxtId;
    }

    public String[] getTxtNewName() {
        return txtNewName;
    }

    public void setTxtNewName(String[] newTxtNewName) {
        txtNewName = newTxtNewName;
    }

    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            
            if(txtId != null){
                strArrayValues = "{";
                for(int index = 0; index < txtId.length; index++){
                    strArrayValues += " " + txtId[index];
                }
                strArrayValues += "}";
                strTemp += "\n\ttxtId : " + strArrayValues;
            }else{
                strTemp += "\n\ttxtId : " + txtId;
            }

            if(txtNewName != null){
                strArrayValues = "{";
                for(int index = 0; index < txtNewName.length; index++){
                    strArrayValues += " " + txtNewName[index];
                }
                strArrayValues += "}";
                strTemp += "\n\ttxtNewName : " + strArrayValues;
            }else{
                strTemp += "\n\ttxtNewName : " + txtNewName;
            }
        }
        return strTemp;
    }

  public String[] getTxtNewDesc() {
    return txtNewDesc;
  }

  public void setTxtNewDesc(String[] newTxtNewDesc) {
    txtNewDesc = newTxtNewDesc;
  }

}
