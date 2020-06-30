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
 * $Id: DocUnZipForm.java,v 20040220.6 2006/03/16 07:29:27 suved Exp $
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
 * Purpose: To store location and id options for inflating operation for items 
 *          selected.
 * @author             Jeetendra Prasad
 * @version            1.0
 * Date of creation:   27-04-2004
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class DocUnZipForm extends ActionForm  {
    private String hdnExtractToLocation;
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

    public String getHdnExtractToLocation() {
        return hdnExtractToLocation;
    }

    public void setHdnExtractToLocation(String newHdnExtractToLocation) {
        hdnExtractToLocation = newHdnExtractToLocation;
    }

    public Long[] getChkFolderDocIds() {
        return chkFolderDocIds;
    }

    public void setChkFolderDocIds(Long[] newChkFolderDocIds) {
        chkFolderDocIds = newChkFolderDocIds;
    }

}
