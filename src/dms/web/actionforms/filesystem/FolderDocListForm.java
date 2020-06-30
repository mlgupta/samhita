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
 * $Id: FolderDocListForm.java,v 20040220.12 2006/05/19 06:24:27 suved Exp $
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
 *	Purpose: To store the values of the html controls of FolderDocListForm in 
 *           folder_doc_list.jsp
 * @author              Jeetendra Prasad
 * @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class FolderDocListForm extends ActionForm  {
    private Long[] chkFolderDocIds;
    private String txtAddress;
    private Long currentFolderId;
    private Long parentFolderId;
    private Long documentId;
    private int txtPageNo;
    private Integer overWrite;
    boolean blnDownload;
    public String hdnWfName;
    //Just dummy of no use
  //  private byte hdnActionType;

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

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String newTxtAddress) {
        txtAddress = newTxtAddress;
    }

    public Long getCurrentFolderId() {
        return currentFolderId;
    }

    public void setCurrentFolderId(Long newCurrentFolderId) {
        currentFolderId = newCurrentFolderId;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long newParentFolderId) {
        parentFolderId = newParentFolderId;
    }

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long newDocumentId) {
        documentId = newDocumentId;
    }

    public int getTxtPageNo() {
        return txtPageNo;
    }

    public void setTxtPageNo(int newTxtPageNo) {
        txtPageNo = newTxtPageNo;
    }


/*    public byte getHdnActionType() {
        return hdnActionType;
    }

    public void setHdnActionType(byte newHdnActionType) {
        hdnActionType = newHdnActionType;
    }
*/
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
        
            strTemp += "\n\ttxtAddress : " + txtAddress;
            strTemp += "\n\tcurrentFolderId : " + currentFolderId;
            strTemp += "\n\tparentFolderId : " + parentFolderId;
            strTemp += "\n\tdocumentId : " + documentId;
            strTemp += "\n\ttxtPageNo : " + txtPageNo;
    //        strTemp += "\n\thdnActionType : " + hdnActionType;
        }
        return strTemp;
    }

    public Integer getOverWrite() {
        return overWrite;
    }

    public void setOverWrite(Integer newOverWrite) {
        overWrite = newOverWrite;
    }

    public boolean isBlnDownload() {
        return blnDownload;
    }

    public void setBlnDownload(boolean newBlnDownload) {
        blnDownload = newBlnDownload;
    }

  public String getHdnWfName() {
    return hdnWfName;
  }

  public void setHdnWfName(String hdnWfName) {
    this.hdnWfName = hdnWfName;
  }

}
