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
 * $Id: DocZipForm.java,v 20040220.4 2006/03/17 12:21:08 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/* Servlet API */
import javax.servlet.http.HttpServletRequest;
/* Struts API */
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To store the values of zipFileName and ids of items for deflation
 *  @author             Jeetendra Prasad
 *  @version            1.0 
 * 	Date of creation:   27-04-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DocZipForm extends ActionForm  {
    private transient String hdnZipFileName;
    private Long[] chkFolderDocIds;

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

    public String getHdnZipFileName() {
        return hdnZipFileName;
    }

    public void setHdnZipFileName(String newHdnZipFileName) {
        hdnZipFileName = newHdnZipFileName;
    }

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }
}
