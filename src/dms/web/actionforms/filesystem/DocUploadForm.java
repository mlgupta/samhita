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
 * $Id: DocUploadForm.java,v 20040220.9 2006/03/16 07:31:16 suved Exp $
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
import org.apache.struts.upload.FormFile;
/**
 *	Purpose: To store the values of the html controls of DocUploadForm in 
 *           doc_upload.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0 
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DocUploadForm extends ActionForm {
    private FormFile fileOne;
    private String txtPath;
    private FormFile fileTwo;
    private FormFile fileThree;
    private String txaFileOneDesc;
    private String txaFileTwoDesc;
    private String txaFileThreeDesc;

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

    public String getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(String newTxtPath) {
        txtPath = newTxtPath;        
    }

    public FormFile getFileOne() {
        return fileOne;
    }

    public void setFileOne(FormFile newFileOne) {
        fileOne = newFileOne;
    }

    public FormFile getFileTwo() {
        return fileTwo;
    }

    public void setFileTwo(FormFile newFileTwo) {
        fileTwo = newFileTwo;
    }

    public FormFile getFileThree() {
        return fileThree;
    }

    public void setFileThree(FormFile newFileThree) {
        fileThree = newFileThree;
    }

    
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            strTemp += "\n\ttxtPath : " + txtPath;
        }

/*
        strTemp += "\n\tfileOne{{FileName},{FileSize},{Format}} : {{" + fileOne.getFileName() + "},{" + fileOne.getFileSize() + "},{" + fileOne.getContentType() + "}}"; 
        strTemp += "\n\tfileTwo{{FileName},{FileSize},{Format}} : {{" + fileTwo.getFileName() + "},{" + fileTwo.getFileSize() + "},{" + fileTwo.getContentType() + "}}";        
        strTemp += "\n\tfileThree{{FileName},{FileSize},{Format}} : {{" + fileThree.getFileName() + "},{" + fileThree.getFileSize() + "},{" + fileThree.getContentType() + "}}";
*/        
        return strTemp;
    }

    public String getTxaFileOneDesc() {
        return txaFileOneDesc;
    }

    public void setTxaFileOneDesc(String newTxaFileOneDesc) {
        txaFileOneDesc = newTxaFileOneDesc;
    }

    public String getTxaFileTwoDesc() {
        return txaFileTwoDesc;
    }

    public void setTxaFileTwoDesc(String newTxaFileTwoDesc) {
        txaFileTwoDesc = newTxaFileTwoDesc;
    }

    public String getTxaFileThreeDesc() {
        return txaFileThreeDesc;
    }

    public void setTxaFileThreeDesc(String newTxaFileThreeDesc) {
        txaFileThreeDesc = newTxaFileThreeDesc;
    }

}
