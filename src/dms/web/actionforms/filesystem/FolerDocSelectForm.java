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
 * $Id: FolerDocSelectForm.java,v 1.3 2006/03/17 08:44:20 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/**
 *	Purpose: To Store FolderDocSelect control values
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   02-04-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

public class FolerDocSelectForm extends ActionForm  {
  private String folderDocument;

  public String getFolderDocument() {
    return folderDocument;
  }

  public void setFolderDocument(String newFolderDocument) {
    folderDocument = newFolderDocument;
  }

 
}
