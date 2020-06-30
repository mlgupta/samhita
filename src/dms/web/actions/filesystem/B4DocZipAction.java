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
 * $Id: B4DocZipAction.java,v 1.6 2006/03/01 08:06:30 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.DocZipForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
/* Java API */
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
 *	Purpose: Action called b4 deflating a file to display doc_zip.jsp     
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    16-06-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocZipAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering B4DocZipAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DbsDocument dbsDocument = null;
    HttpSession httpSession = null;
    UserInfo userInfo = null;
    DbsLibrarySession dbsLibrarySession = null;
    try{
      DocZipForm docZipForm = (DocZipForm)form;
      logger.debug("docZipForm : " + docZipForm);
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long docId = docZipForm.getChkFolderDocIds()[0];
      logger.debug("documentId : " + docId);
      String zipFleName = dbsLibrarySession.getPublicObject(docId).getName();
      logger.debug("zipFileName : " + zipFleName);
      int indexOfDot = zipFleName.lastIndexOf(".");
      if( indexOfDot == -1){
        indexOfDot = zipFleName.length();
      }
      docZipForm.setHdnZipFileName(zipFleName.substring(0,indexOfDot) + ".zip");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in B4DocZipAction... ");
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4DocZipAction... ");
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting B4DocZipAction now...");
    return mapping.findForward(forward);
  }
}
