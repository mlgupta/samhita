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
 * $Id: SAPReplaceVoucherAction.java,v 1.3 2006/03/13 suved Exp $
 *****************************************************************************
 */
package adapters.sap.actions;
/*adapters package references*/
import adapters.sap.actionforms.SAPReplaceVoucherForm;
/*dms package references*/
import dms.beans.DbsCollection;
import dms.beans.DbsDocument;
import dms.beans.DbsDocumentDefinition;
import dms.beans.DbsException;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsSelector;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/*Java Api*/
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts Api*/
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
 /**
 * Purpose            : Action called to change a document's content for SAP.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 2006/03/13
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class SAPReplaceVoucherAction extends Action  {
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
    String fileNames =new String();
    HttpSession httpSession = null;
    Integer errorIndex=new Integer(-1);
    Long documentId = null;
    DbsDocument dbsDocID = null;
    DbsLibrarySession dbsLibrarySession = null;
    try{
      logger.info("Entering SAPReplaceVoucherAction now...");
      httpSession = request.getSession(false);
      //replaceVoucherId= Long.getLong(request.getParameter("voucherID")); 
      
      SAPReplaceVoucherForm replaceVoucherForm = (SAPReplaceVoucherForm)form;
      documentId= new Long(replaceVoucherForm.getTxtDocumentId());
      logger.debug("SAPReplaceVoucherForm : " + replaceVoucherForm);
                  
      UserInfo userInfo = (UserInfo)
                              httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)
                              httpSession.getAttribute("FolderDocInfo");            
      UserPreferences userPreferences = (UserPreferences)
                              httpSession.getAttribute("UserPreferences");
      
      //Get the session from userInfo
      dbsLibrarySession = userInfo.getDbsLibrarySession();
  
      //Locate DocumentObject for the received Document ID
      dbsDocID = (DbsDocument)dbsLibrarySession.getPublicObject(documentId);
      String docName = dbsDocID.getName();
      
      //obtain FormFile and the description string
      FormFile frmFile= replaceVoucherForm.getFrmFile();
      String fileDesc=replaceVoucherForm.getTxaFileDesc().trim();
      
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      //check if total upload limit set in controller tag of struts-config
      //is beign violated
      Boolean isLimitExceeded=(Boolean)request.getAttribute(
                  MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
      if(isLimitExceeded!=null){
        logger.debug("Total Upload Limit Violated!!!");
        errorIndex=new Integer(0);
      }
      //if no violation, upload the valid files and their resp descriptions
      else{
        logger.debug("Filename: "+ frmFile.getFileName()+";Filesize: "+
              frmFile.getFileSize()+";Filetype: "+frmFile.getContentType());
        logger.debug("File Description : " + fileDesc);
        //checks for valid file-names and proceed with uploading
        if((frmFile.getFileName()!=null) && 
           (frmFile.getFileName().trim().length()>0)){
           String fileExt=frmFile.getFileName().substring(
                              frmFile.getFileName().lastIndexOf("."),frmFile.getFileName().length());
          DbsDocumentDefinition dbsDocumentDefinition = 
                              new DbsDocumentDefinition(dbsLibrarySession);
          dbsDocumentDefinition.setName(
                              replaceVoucherForm.getVoucherId()+fileExt);
          dbsDocumentDefinition.setContentStream(frmFile.getInputStream());
          logger.debug("File Extension: "+fileExt);
          if(fileExt!=null && fileExt.length()!=0){
            DbsFormat format = null;
            DbsCollection coll = dbsLibrarySession.getFormatExtensionCollection();
            try{
              format = (DbsFormat)coll.getItems(fileExt.substring(1));
            }catch (DbsException e) {
              if (e.containsErrorCode(12200)) {
                DbsSelector selector = new DbsSelector(dbsLibrarySession);
                selector.setSearchClassname(DbsFormat.CLASS_NAME);
               
                selector.setSearchSelection(DbsFormat.MIMETYPE_ATTRIBUTE + 
                                              " = 'application/octet-stream'");
               
                format = (DbsFormat) selector.getItems(0);
                e.printStackTrace();
              }                
             }
            dbsDocumentDefinition.setFormat(format);
          }
          dbsDocID.setContent(dbsDocumentDefinition);   
          dbsDocID.setName(frmFile.getFileName());
          if (fileDesc!=null){
            dbsDocID.setDescription(fileDesc);
          }
          logger.debug("File: "+frmFile.getFileName()+
                        " has been uploaded successfully.");
          frmFile.getInputStream().close();
          frmFile.destroy();
        }
      }
      /*success, set up an action message*/
      if(errorIndex.intValue() == -1){
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = 
            new ActionMessage("msg.FileUploadedSuccessfully",fileNames);
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        request.setAttribute("ActionMessages",actionMessages);
        /*httpSession.setAttribute("ActionMessages",actionMessages);*/
      }
      /*else set up an error message*/
      else{
       ActionErrors actionErrors= new ActionErrors();
       ActionError actionError= new ActionError("errors.limitViolation");
       actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
       if(!actionErrors.isEmpty()){
         saveErrors(request,actionErrors);
       }
      }
      request.setAttribute("errorIndex",errorIndex);
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      /* IFS-34611,30659 contain error msgs for version series also.*/
      
      /* 34611 is for inability of a user to upload a new version,due to
       * access rights limitations viz:-inability to chkout a ver doc. */
      if(dex.containsErrorCode(34611)){
        exceptionBean.setMessageKey("errors.34611.error.reserving.version.series");
      }
      /* 30659 is for inability of a user to upload a new version,due to
       * lock acquired on the versioned doc by another user, viz:- doc is
       * chkd out by one user,while another wishes to chk in/out. */            
      if(dex.containsErrorCode(30659)){
        exceptionBean.setMessageKey("errors.30659.PO.has.userlock.unable.to.change");
      }
      /* 30063 is for inability of a user to upload a new version,due to
       * insufficient access to change PublicObject's PolicyBundle. */
      if(dex.containsErrorCode(30063)){
        exceptionBean.setMessageKey("errors.30063.insufficient.access.tochange.PO's.PB");
      }
      logger.error("Exception in SAPReplaceVoucherAction" +
                    exceptionBean.getMessage());
      logger.error(exceptionBean.getErrorTrace());     
      saveErrors(request,exceptionBean.getActionErrors());
      forward= new String("failure");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error(exceptionBean.getMessage());
      logger.debug(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }finally{
      logger.debug("Disconnecting DbsLibrarySession now...");
      try{
        if( dbsLibrarySession != null && dbsLibrarySession.isConnected() ){
          dbsLibrarySession.disconnect();
          dbsLibrarySession = null;
          logger.debug("Disconnected DbsLibrarySession successfully...");
        }
      }catch (DbsException dbsEx) {
        logger.error("An error occurred in SAPReplaceVoucherAction"+
                      dbsEx.toString());
      }
    }
    logger.info("Exiting SAPReplaceVoucherAction now...");
    return mapping.findForward(forward);
  }
}
