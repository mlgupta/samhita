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
 * $Id: DocHistoryDetailAction.java,v 20040220.14 2006/03/13 14:19:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.DocHistoryListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DocumentHistoryDetail;
import dms.web.beans.filesystem.Version;
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
 *	Purpose: Action called to view a version's detail of a versioned document 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   06-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class DocHistoryDetailAction extends Action  {
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
    logger.info("Entering DocHistoryDetailAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DocumentHistoryDetail documentHistoryDetail = null;
    DbsPublicObject publicObject = null;
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      DocHistoryListForm docHistoryListForm = (DocHistoryListForm)form;
      logger.debug("docHistoryListForm : " + docHistoryListForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");

      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long docId = docHistoryListForm.getRadDocId();
      logger.debug("docId : " + docId);
      Long familyId = docHistoryListForm.getChkFolderDocIds()[0];
      logger.debug("familyId : " + familyId);

      publicObject = dbsLibrarySession.getPublicObject(familyId);
      logger.info("Family Name : " + publicObject.getName());
      // fetch version history
      Version version = new Version(dbsLibrarySession);
      documentHistoryDetail = version.getVersionedDocProperty(familyId,docId);
      request.setAttribute("documentHistoryDetail",documentHistoryDetail);

    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in DocHistoryDetailAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DocHistoryDetailAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting DocHistoryDetailAction now...");
    return mapping.findForward(forward);
  }
}
