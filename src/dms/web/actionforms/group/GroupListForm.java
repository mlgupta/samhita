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
 * $Id: GroupListForm.java,v 20040220.4 2006/03/16 07:35:38 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.group;  
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of
 *           groupListForm in group_list.jsp
 *  @author             Mishra Maneesh
 *  @version            1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GroupListForm extends ValidatorForm  {
    private String radSelect;
    private String txtSearchByGroupName;
    private String operation;
    private String txtPageNo;
    private String txtPageCount;
   
    /**
     * Purpose   : Returns  radGroupSelect.
     * @return   : String
     */
    public String getRadSelect() {
        return radSelect;
    }

    /**
     * Purpose   : Sets the value of radGroupSelect.
     * @param    : newRadGroupSelect Value of radGroupSelect from the form
     */
    public void setRadSelect(String newRadSelect) {
        radSelect = newRadSelect;
    }

    /**
     * Purpose   : Returns txtSearchByGroupName.
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
     * Purpose   : Returns operation.
     * @return   : String
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Purpose   : Sets the value of operation.
     * @param    : newOperation Value of operation from the form
     */
    public void setOperation(String newOperation) {
        operation = newOperation;
    }

    /**
     * Purpose   : Returns txtPageNo.
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
     * Purpose   : Returns txtPageCount.
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
}
