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
 * $Id: DocHistoryListForm.java,v 20040220.12 2006/03/17 08:44:20 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Struts API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/** 
 *	Purpose: To store the values of the html controls of DocHistoryListForm in 
 *           doc_history_list.jsp
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DocHistoryListForm extends ActionForm {
    private Long radDocId;
    private Long[] chkFolderDocIds;
    private String documentName;
    private int txtHistoryPageNo;
    private int txtHistoryPageCount;
    
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

    public Long getRadDocId() {
        return radDocId;
    }

    public void setRadDocId(Long newRadDocId) {
        radDocId = newRadDocId;
    }

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String newDocumentName) {
        documentName = newDocumentName;
    }

    public int getTxtHistoryPageCount() {
        return txtHistoryPageCount;
    }

    public void setTxtHistoryPageCount(int newTxtHistoryPageCount) {
        txtHistoryPageCount = newTxtHistoryPageCount;
    }

    public int getTxtHistoryPageNo() {
        return txtHistoryPageNo;
    }

    public void setTxtHistoryPageNo(int newTxtHistoryPageNo) {
        txtHistoryPageNo = newTxtHistoryPageNo;
    }

    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            String strArrayValues = "";
            strTemp += "\n\tradDocId : " + radDocId;

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
            strTemp += "\n\tchkFolderDocIds : " + strArrayValues;
        
            strTemp += "\n\tdocumentName : " + documentName;
            strTemp += "\n\ttxtHistoryPageNo : " + txtHistoryPageNo;
            strTemp += "\n\ttxtHistoryPageCount : " + txtHistoryPageCount;
        }
        return strTemp;
    }


}
