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
 * $Id: FolderNewForm.java,v 20040220.8 2006/03/16 07:05:18 suved Exp $
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
 * Purpose: To store folder name and description for new folder to be created.
 * @author             Rajan Kamal Gupta
 * @version            1.0
 * Date of creation:   09-01-2004
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class FolderNewForm extends ActionForm  {
  private String hdnFolderName;
  private String hdnFolderDesc;
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

  public String getHdnFolderName() {
      return hdnFolderName;
  }

  public void setHdnFolderName(String newHdnFolderName) {
      hdnFolderName = newHdnFolderName;
  }

  public String toString(){
      String strTemp = "";
      Logger logger = Logger.getLogger("DbsLogger");
      if(logger.getLevel() == Level.DEBUG){
          strTemp += "\n\thdnFolderName : " + hdnFolderName;
      }
      return strTemp;
  }

  public String getHdnFolderDesc() {
    return hdnFolderDesc;
  }

  public void setHdnFolderDesc(String newHdnFolderDesc) {
    hdnFolderDesc = newHdnFolderDesc;
  }
}
