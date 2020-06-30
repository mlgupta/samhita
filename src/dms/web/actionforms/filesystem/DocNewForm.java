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
 * $Id: DocNewForm.java,v 1.2 2006/03/16 07:27:20 suved Exp $
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
 * Purpose: To store document name and type for creating new document.
 * @author             Rajan Kamal Gupta
 * @version            1.0
 * Date of creation:   30-05-2005
 * Last Modified by :     
 * Last Modified Date:    
 */ 
public class DocNewForm extends ActionForm {
  public String docType;
  public String docName;

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

  public String toString(){
    String strTemp = "";
    Logger logger = Logger.getLogger("DbsLogger");
    if(logger.getLevel() == Level.DEBUG){
        strTemp += "\n\tdocType : " + docType;
    }
    return strTemp;
  }

  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getDocName() {
    return docName;
  }

  public void setDocName(String docName) {
    this.docName = docName;
  }
}