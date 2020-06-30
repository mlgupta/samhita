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
 * $Id: DocViewAsHtmlAction.java,v 20040220.6 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
/* Servlet API */
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
 * Purpose                : To view the content of a document in HTML/PlainText 
 *                          format using context INSO filters
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 10-05-2006
 * Last Modified by       : 
 * Last Modification Date : 
 */
public class DocViewAsHtmlAction extends Action  {
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
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success"; 
    DbsDocument document = null;        // represents cmsdk document  
    DbsPublicObject dbsRPO = null;      // represents cmsdk Resolved PO 
    BufferedReader brdr = null;         // buffered reader for reading content
    BufferedWriter bwtr = null;         // buffered writer for response 
    boolean plainText = false;          // boolean response be plaintext or not
    boolean isNonIndexedMedia = false;  // file type is non-indexed media or not 
    
    try{
      logger.info("Entering DocViewAsHtmlAction now ...");
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      
      DbsLibrarySession dbsSession = userInfo.getDbsLibrarySession();
      Long documentId = new Long(request.getParameter("documentId"));
      logger.debug("documentId : " + documentId);
      /* obtain the RPO from the id supplied */
      dbsRPO = dbsSession.getPublicObject(documentId).getResolvedPublicObject();
      if(dbsRPO instanceof DbsDocument){
        /* if RPO instanceof Document then fetch content */
        document = (DbsDocument)dbsRPO;
        int contentSize = (int)document.getContentSize();
        DbsFormat format = document.getFormat();
        String mimeType = format.getMimeType();
        if( mimeType.equals("text/plain") ){
          plainText = true;
        }
        try{
        /* create an HTML/PlainText version of the document using 
         * context INSO filters */ 
          document.filterContent(plainText);
        }catch (DbsException dbsEx) {
          /* IFS-22512: Operation filter not supported for non-indexed media */
          if( dbsEx.containsErrorCode(22512) ){
            isNonIndexedMedia = true;
          }
        }
        if( !isNonIndexedMedia ){
          /* obtain the filtered content in a buffered reader */
          brdr = new BufferedReader(document.getFilteredContent());
          String contentType = ( plainText )?"text/plain":
                               (mimeType.equals("text/xml"))?"text/xml":"text/html";
          /* set the response content type , if obtaining a PrintWriter  
           * ie: using getWriter(), this method should be called first */
          response.setContentType(contentType);
          /* set a response header with the given name and value */
          response.setHeader("Content-Disposition","filename=\""+ document.getName() + "\"");           
          /* set the length of the content body in the response. In HTTP 
           * servlets, this method sets the HTTP Content-Length header */
          response.setContentLength(contentSize);
          /* obtain response writer and write the filtered content into it */
          bwtr = new BufferedWriter(response.getWriter());
          bwtr.flush();
          String line = brdr.readLine();
          if( line!=null ){
            while (line != null){
              bwtr.write(line);
              bwtr.write("\n");
              line = brdr.readLine();
            }
          }else{
            /* for images,java files and other indexed media not supported by 
             * INSO filters */
            line = "Operation not supported for the requested file type.";
            bwtr.write(line);
            bwtr.write("\n");
          }
          /* forces any content in the buffer to be written to the client. 
           * A call to this method automatically commits the response, 
           * meaning the status code and headers will be written */
          response.flushBuffer();
        }else{
          // for non-indexed media, INSO filters donot support filterContent()
          response.setContentType("text/html");
          response.setHeader("Content-Disposition","filename=\""+ document.getName() + "\"");           
          response.setContentLength(contentSize);
          bwtr = new BufferedWriter(response.getWriter());
          bwtr.flush();
          String line = "Operation not supported for the requested file type.";
          bwtr.write(line);
          bwtr.write("\n");
          response.flushBuffer();
        }
      }else{
        /* RPO not an instanceof DbsDocument, log error */
        logger.error("Selected item's RPO is not an instanceof Document...");
      }
    }catch(DbsException dex){
      logger.error("An Error occurred in DocViewAsHtmlAction...");
      exceptionBean = new ExceptionBean(dex);
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      ex.printStackTrace();
      logger.error("An Error occurred in DocViewAsHtmlAction...");
      exceptionBean = new ExceptionBean(ex);
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
    }finally{
      /* it is mandatory to close the open streams */
      try{
        if( brdr != null ){
          brdr.close();
          brdr = null;
        }
        if( bwtr != null ){
          bwtr.close();
          bwtr = null;
        }
      }catch( Exception ex ){   
        ex.printStackTrace();
        logger.error("An Error occurred in DocViewAsHtmlAction...");
        exceptionBean = new ExceptionBean(ex);
        logger.error(exceptionBean.getMessage());
      }
    }
    logger.info("Exiting DocViewAsHtmlAction now ...");
    return null;
  }
}
