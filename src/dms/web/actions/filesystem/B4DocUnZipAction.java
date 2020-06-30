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
 * $Id: B4DocUnZipAction.java,v 1.9 2006/03/13 14:19:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.DocUnZipForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
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
 *	Purpose: Action called b4 inflating the zipped file to display doc_unzip.jsp     
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    16-06-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocUnZipAction extends Action  {
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
    logger.info("Entering B4DocUnZipAction now ...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DbsLibrarySession dbsLibrarySession = null;
    DbsDocument dbsDocument = null;
    FolderDocInfo folderDocInfo = null;
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    try{
      DocUnZipForm docUnZipForm = (DocUnZipForm)form;
      logger.debug("docZipForm : " + docUnZipForm);
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long documentId = docUnZipForm.getChkFolderDocIds()[0];
      logger.debug("documentId : " + documentId);
      dbsDocument = (DbsDocument)dbsLibrarySession.getPublicObject(documentId);
      String currentFolderPath = folderDocInfo.getCurrentFolderPath();
      String documentName = dbsDocument.getName();
      String extractToFolderName;
      int indexOfDot = documentName.lastIndexOf(".");
      if(indexOfDot > 0){
        extractToFolderName = documentName.substring(0,indexOfDot);
      }else{
        extractToFolderName = documentName;
      }
      if(currentFolderPath.equals("/")){
        docUnZipForm.setHdnExtractToLocation(currentFolderPath +
                                             extractToFolderName);
      }else{
        docUnZipForm.setHdnExtractToLocation(currentFolderPath + "/" + 
                                             extractToFolderName);
      }
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      if(dex.getErrorCode() == 68004){
        exceptionBean.setMessageKey("msg.folder.path.not.found");
      }
      logger.error("An Exception occurred in B4DocUnZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4DocUnZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }
    logger.info("Exiting B4DocUnZipAction now ...");
    return mapping.findForward(forward);
  }
}
