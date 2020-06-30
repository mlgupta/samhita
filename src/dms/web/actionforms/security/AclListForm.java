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
 * $Id: AclListForm.java,v 20040220.4 2006/03/17 06:39:07 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.security;
 //Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of
 *           AclListForm in acl_list.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class AclListForm extends ValidatorForm {
    private String txtSearchByAclName;
    private String radSelect;
    private String operation;
    private String txtPageNo;
    private String txtPageCount;

    /**
     * Purpose   :  Returns  txtAclName.
     * @return   :  String
     */
    public String getTxtSearchByAclName() {
        return txtSearchByAclName;
    }

    /**
     * Purpose   : Sets the value of txtAclName.
     * @param    : newTxtAclName Value of txtAclName from the form
     */
    public void setTxtSearchByAclName(String newTxtSearchByAclName) {
        txtSearchByAclName = newTxtSearchByAclName;
    }

    /**
     * Purpose : Returns  radSelect.
     * @return : String
     */
    public String getRadSelect() {
        return radSelect;
    }

    /**
     * Purpose : Sets the value of radSelect.
     * @param  : newRadSelect Value of radSelect from the form
     */
    public void setRadSelect(String newRadSelect) {
        radSelect = newRadSelect;
    }

    /**
     * Purpose : Returns operation.
     * @return : String
     */
    public String getOperation(){
        return operation;
    }

    /**
     * Purpose : Sets the value of operation.
     * @param  : newOperation Value of operation from the form
     */
    public void setOperation(String newOperation){
        operation = newOperation;
    }

    /**
     * Purpose : Returns txtPageNo.
     * @return : String
     */
    public String getTxtPageNo(){
        return txtPageNo;
    }

    /**
     * Purpose : Sets the value of txtPageNo.
     * @param  : newTxtPageNo Value of txtPageNo from the form
     */
    public void setTxtPageNo(String newTxtPageNo){
        txtPageNo = newTxtPageNo;
    }

    /**
     * Purpose : Returns txtPageCount.
     * @return : String
     */
    public String getTxtPageCount(){
        return txtPageCount;
    }

    /**
     * Purpose : Sets the value of txtPageCount.
     * @param  : newTxtPageCount Value of txtPageCount from the form
     */
    public void setTxtPageCount(String newTxtPageCount){
        txtPageCount = newTxtPageCount;
    }
}
