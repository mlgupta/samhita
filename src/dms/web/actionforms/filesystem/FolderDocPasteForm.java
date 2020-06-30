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
 * $Id: FolderDocPasteForm.java,v 20040220.8 2006/03/16 06:58:29 suved Exp $
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
/**
 * Purpose: To store overwrite and paste options for paste operation for items 
 *          selected.
 * @author              Rajan Kamal Gupta
 * @version             1.0
 * Date of creation:   09-01-2004
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class FolderDocPasteForm extends ActionForm  {
    private boolean hdnOverWrite;
    private Integer radPasteOption = new Integer(0);

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

    public boolean isHdnOverWrite() {
        return hdnOverWrite;
    }

    public void setHdnOverWrite(boolean newHdnOverWrite) {
        hdnOverWrite = newHdnOverWrite;
    }

    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            strTemp += "\n\thdnOverWrite : " + hdnOverWrite;
        }
        return strTemp;
    }

    public Integer getRadPasteOption() {
        return radPasteOption;
    }

    public void setRadPasteOption(Integer newRadPasteOption) {
        radPasteOption = newRadPasteOption;
    }
    
}
