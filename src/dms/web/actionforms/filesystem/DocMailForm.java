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
 * $Id: DocMailForm.java,v 20040220.3 2006/03/16 07:16:44 suved Exp $
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
 * Purpose: To store document id for mail operation for items selected.
 * @author             Maneesh Mishra
 * @version            1.0
 * Date of creation:   03-07-2004
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class DocMailForm extends ActionForm  {
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
