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
 * $Id: GroupListSelectForm.java,v 20040220.4 2006/03/16 07:36:21 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.group; 
 //Struts API
import org.apache.struts.action.ActionForm;
/**
 *	Purpose: To store and retrive the values of the html controls of 
 *           groupListForm in group_list_select.jsp
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GroupListSelectForm extends ActionForm  {
    private String txtSearchByGroupName;
    private String[] chkSelect;
    private String control;
    private String txtPageNo;
    private String txtPageCount;
    private String operation;

    /**
     * Purpose   : Returns  txtSearchByGroupName.
     * @return   : String
     */
    public String getTxtSearchByGroupName() {
        return txtSearchByGroupName;
    }

    /**
     * Purpose   : Sets the value of txtSearchByGroupName.
     * @param    : newTxtSearchByGroupName Value of txtSearchByGroupName from the form
     */
    public void setTxtSearchByGroupName(String newTxtSearchByGroupName) {
        txtSearchByGroupName = newTxtSearchByGroupName;
    }

    /**
     * Purpose   : Returns  chkGroupListSelect array.
     * @return   : String[]
     */
    public String[] getChkSelect() {
        return chkSelect;
    }

    /**
     * Purpose   : Sets the value of chkGroupListSelect array.
     * @param    : newChkGroupListSelect Value of chkGroupListSelect array from the form
     */
    public void setChkSelect(String[] newChkSelect) {
        chkSelect = newChkSelect;
    }

    /**
     * Purpose   : Returns  control.
     * @return   : String
     */
    public String getControl(){
        return control;
    }

    /**
     * Purpose   : Sets the value of control.
     * @param    : newControl Value of control from the form
     */
    public void setControl(String newControl) {
        control = newControl;
    }

    /**
     * Purpose   : Returns txtPageNo array.
     * @return   : String
     */
    public String getTxtPageNo() {
        return txtPageNo;
    }

    /**
     * Purpose   : Sets the value of txtPageNo.
     * @param    : newTxtPageNo Value of txtPageNo from the form
     */
    public void setTxtPageNo(String newTxtPageNo) {
        txtPageNo = newTxtPageNo;
    }

    /**
     * Purpose   : Returns txtPageCount array.
     * @return   : String
     */
    public String getTxtPageCount() {
        return txtPageCount;
    }

    /**
     * Purpose   : Sets the value of txtPageCount.
     * @param    : newTxtPageCount Value of txtPageCount from the form
     */
    public void setTxtPageCount(String newTxtPageCount) {
        txtPageCount = newTxtPageCount;
    }

    /**
     * Purpose   : Returns operation array.
     * @return   : String
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Purpose   : Sets the value of txtPageCount.
     * @param    : newTxtPageCount Value of txtPageCount from the form
     */
    public void setOperation(String newOperation) {
        operation = newOperation;
    }
}
