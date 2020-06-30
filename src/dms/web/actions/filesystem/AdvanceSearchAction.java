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
 * $Id: AdvanceSearchAction.java,v 20040220.24 2006/03/01 07:10:11 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.web.actionforms.filesystem.AdvanceSearchForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
/* java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To perform search operation based on the search criteria specified
 *  @author              Rajan Kamal Gupta 
 *  @version             1.0
 * 	Date of creation:    25-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class AdvanceSearchAction extends Action  {
  public static final int CREATEDATE=1;
  public static final int LASTMODIFIEDDATE=2;
  public static final int ATLEAST=1;
  public static final int ATMOST=2;
  Logger logger; 
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //Initialize logger
    logger = Logger.getLogger("DbsLogger");
    logger.info("Entering AdvanceSearchAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm)form;
      FolderDocInfo folderDocInfo = (FolderDocInfo)
                                    httpSession.getAttribute("FolderDocInfo");
      logger.debug("advanceSearchForm : " + advanceSearchForm);
      folderDocInfo.setListingType(FolderDocInfo.SEARCH_LISTING);
      httpSession.setAttribute("advanceSearchForm",advanceSearchForm); 
      folderDocInfo.setPageNumber(1);
      logger.debug("folderDocInfo : " + folderDocInfo);
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in AdvanceSearchAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting AdvanceSearchAction now...");
    return mapping.findForward(forward);
  }
}
