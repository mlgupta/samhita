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
 * $Id: DocDownloadAction.java,v 20040220.24 2006/03/13 14:17:34 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DocEventLogBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 *	Purpose: Action called to download selected document
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   16-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006  
 */
public class DocDownloadAction extends Action  {
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
    logger.info("Entering DocDownloadAction now...");
    logger.info("Downloading File ...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DbsDocument dbsDocument = null;
    DbsPublicObject dbsPublicObject = null;
    DbsLibrarySession dbsLibrarySession = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;
    FolderDocInfo folderDocInfo = null;
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    try{
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("folderDocListForm : " + folderDocListForm);
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      /* obtain library session */
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long documentId = folderDocListForm.getDocumentId();
      logger.debug("documentId : " + documentId);
      /* obtain the publicobject from the cmsdk repository .*/
      dbsPublicObject = dbsLibrarySession.getPublicObject(documentId).getResolvedPublicObject();
      if(dbsPublicObject instanceof DbsDocument){
        dbsDocument = (DbsDocument)dbsPublicObject;
        logger.debug("Document Name : " + dbsDocument.getName());
        /* get the content stream */
        inputStream = dbsDocument.getContentStream();
        DbsFormat format = dbsDocument.getFormat();

        String mimeType;
        if(folderDocListForm.isBlnDownload()){
          mimeType = "application/octet-stream";
        }else{
          mimeType = format.getMimeType();
        }
        logger.debug("document mimetype : " + mimeType);
        /* set up response object */
        /* step 1 : set it's content type to the document's mimetype  */
        response.setContentType(mimeType);
        /* step 2 : set it's header */
        response.setHeader("Content-Disposition","filename=\""+ dbsDocument.getName() + "\"");           
        int contentSize = (int)dbsDocument.getContentSize();
        logger.debug("Document Size : " + contentSize);
        /* step 3 : set it's content length */
        response.setContentLength(contentSize);
        /* step 4 : obtain a byte [] of content to write to response o/p stream */
        byte[] content = new byte[contentSize];
        inputStream.read(content,0,contentSize);
        /* step 5 : close the content stream */
        inputStream.close();
        inputStream = null;
        logger.debug("File input stream closed");
        /* step 6 : obtain response o/p stream and write byte [] from step 4 
         * into it */
        outputStream = response.getOutputStream();
        outputStream.write(content);
        /* step 7 : close the response o/p stream */
        outputStream.close();
        outputStream = null;
        logger.debug("File output stream closed");
        response.flushBuffer();
        /* log event in document's AUDIT_LOG attribute */
        DocEventLogBean logBean = new DocEventLogBean();
        logBean.logEvent(dbsLibrarySession,dbsDocument.getId(),"Doc Downloaded");
      }
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in DocDownloadAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DocDownloadAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }finally{
      /* it is mandatory to close streams in any case of execution */
      if( outputStream != null ){
        try{
          outputStream.close();
        }catch (Exception e) {
          logger.error("An Exception occurred in DocDownloadAction... ");
          logger.error(e.toString());
        }
        outputStream = null;
      }
      if( inputStream != null ){
        try{
          inputStream.close();
        }catch (Exception e) {
          logger.error("An Exception occurred in DocDownloadAction... ");
          logger.error(e.toString());
        }
        inputStream = null;
      }
    }
    logger.info("Downloading File Complete");
    logger.info("Exiting DocDownloadAction now...");
    return null; 
  }
}
