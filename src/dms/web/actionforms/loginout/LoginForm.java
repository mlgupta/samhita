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
 * $Id: LoginForm.java,v 20040220.8 2006/03/16 07:39:18 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.loginout;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/* Struts API */
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store user credentials for validating user login
 *  @author             Mishra Maneesh
 *  @version            1.0
 * 	Date of creation:   07-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class LoginForm extends ValidatorForm  {
    private String userID;
    private String userPassword;

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String newUserID) {
        userID = newUserID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String newUserPassword) {
        userPassword = newUserPassword;
    }



}
