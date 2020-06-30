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
 * $Id: DocLogListForm.java,v 1.3 2006/03/16 07:16:05 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Struts API */
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: To store options for audit log operation for selected document.
 * @author             Suved Mishra
 * @version            1.0
 * Date of creation:   21-02-2005
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class DocLogListForm extends ActionForm  {

    private String txtSearchByUserName;
    private String cboSearchByLogType;
    private String operation;
    private String txtDocLogFromDate;
    private String txtDocLogToDate;
    private String txtPageNo;
    private String txtPageCount;
    private Long chkFolderDocIds;
    private String timezone;   
    /**
     * Purpose : Returns value of txtSearchByUserName
     * @return : String 
     */
    public String getTxtSearchByUserName() {
       return txtSearchByUserName;
    }

    /**
     * Purpose : Sets the value of txtSearchByUserName.
     * @param  : newTxtSearchByUserName Value of txtSearchByUserName from the form
     */
    public void setTxtSearchByUserName(String newTxtSearchByUserName) {
        txtSearchByUserName = newTxtSearchByUserName;
    }    
    
    /**
     * Purpose : Returns cboSearchByJobType.
     * @return : String 
     */
    public String getCboSearchByLogType() {
        return cboSearchByLogType;
    }

    /**
     * Purpose : Sets the value of cboSearchByJobType.
     * @param  : newCboSearchByJobType Value of cboSearchByJobType from the form
     */
   public void setCboSearchByLogType(String newCboSearchByLogType) {
        cboSearchByLogType = newCboSearchByLogType;
    }

    /**
     * Purpose : Returns value of operation
     * @return : String 
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Purpose : Sets the value of operation.
     * @param  : newOperation Value of operation from the form
     */
    public void setOperation(String newOperation) {
        operation = newOperation;
    }

    /**
     * Purpose : Returns value of txtCreatedFromDate
     * @return : String 
     */
    public String getTxtDocLogFromDate() {
      return txtDocLogFromDate;
    }

    /**
     * Purpose : Sets the value of txtCreatedFromDate.
     * @param  : newTxtCreatedFromDate Value of txtCreatedFromDate from the form
     */
    public void setTxtDocLogFromDate(String newTxtDocLogFromDate) {
      txtDocLogFromDate = newTxtDocLogFromDate;
    }

    /**
     * Purpose : Returns value of txtCreatedToDate
     * @return : String 
     */
    public String getTxtDocLogToDate() {
       return txtDocLogToDate;
    }

    /**
     * Purpose : Sets the value of txtCreatedToDate.
     * @param  : newTxtCreatedToDate Value of txtCreatedToDate from the form
     */
    public void setTxtDocLogToDate(String newTxtDocLogToDate) {
      txtDocLogToDate = newTxtDocLogToDate;
    }
    
    /**
     * Purpose : Returns value of txtPageCount
     * @return : String 
     */
    public String getTxtPageCount() {
        return txtPageCount;
    }

    /**
     * Purpose : Sets the value of txtPageCount.
     * @param  : newTxtPageCount Value of txtPageCount from the form
     */
    public void setTxtPageCount(String newTxtPageCount) {
        txtPageCount = newTxtPageCount;
    }

    /**
     * Purpose : Returns value of txtPageNo
     * @return : String 
     */
    public String getTxtPageNo() {
        return txtPageNo;
    }

    /**
     * Purpose : Sets the value of txtPageNo.
     * @param  : newTxtPageNo Value of txtPageNo from the form
     */
    public void setTxtPageNo(String newTxtPageNo) {
        txtPageNo = newTxtPageNo;
    }

    /**
     * Purpose : returns the value of chkFolderDocIds
     */

    public Long getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    /**
     * Purpose : Sets the value of docId.
     * @param  : newChkFolderDocIds Value of chkFolderDocIds from the form
     */

    public void setChkFolderDocIds(Long newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }

    /**
     * Purpose : returns the TimeZone.
     */

    public String getTimezone()
    {
        return timezone;
    }

    /**
     * Purpose : Sets the TimeZone.
     * @param  : newTimeZone
     */

    
    public void setTimezone(String newTimezone)
    {
        timezone = newTimezone;
    }

    public String toString(){
        String strTemp = "";
        strTemp += "\n\tchkFolderDocIds: "+chkFolderDocIds;
        strTemp += "\n\ttxtPageNo: "+txtPageNo;
        strTemp += "\n\ttxtPageCount: "+txtPageCount;
        strTemp += "\n\tcboSearchByLogType: ";
        strTemp += cboSearchByLogType;  
        strTemp += "\n\ttxtSearchByUserName: "+txtSearchByUserName;
        strTemp += "\n\toperation: "+operation;
        strTemp += "\n\ttxtDocLogFromDate: "+txtDocLogFromDate;
        strTemp += "\n\ttxtDocLogToDate: "+txtDocLogToDate;
        strTemp += "\n\ttimeZone: "+timezone;
        
        return strTemp;
    }
    
}
