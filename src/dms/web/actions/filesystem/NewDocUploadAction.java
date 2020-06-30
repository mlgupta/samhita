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
 * $Id: NewDocUploadAction.java,v 1.11 2006/05/19 06:24:03 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.NewDocUploadForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
/*java API*/
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API*/
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
 * Purpose  : Action code for uploading multiple files with appropriate checks.
 * @author              Suved Mishra
 * @version             1.0
 * 	Date of creation:   14-12-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 16-04-2005   
 */
public class NewDocUploadAction extends Action  {
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
    logger.info("Entering NewDocUploadAction now...");
    logger.info("Uploading File");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    String fileNames =new String();
    HttpSession httpSession = null;
    Integer errorIndex=new Integer(-1);
    FolderDocInfo folderDocInfo = null;
    try{
      httpSession = request.getSession(false);
      NewDocUploadForm newDocUploadForm = (NewDocUploadForm)form;
      logger.debug("NewDocUploadForm : " + newDocUploadForm);
      
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");            
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      /*set the path where the doc is to be uploaded*/
      newDocUploadForm.setTxtPath(folderDocInfo.getCurrentFolderPath());
      String currentFolderPath = newDocUploadForm.getTxtPath();
      logger.info("Upload Location : " + currentFolderPath);
      logger.debug("currentFolderPath : " + currentFolderPath);
      /* donot reload tree */
      folderDocInfo.setNoReloadTree(true);
      /*obtain the destination folder's id*/
      Long currentFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("currentFolderId : " + currentFolderId);
      
      /*obtain an array of FormFile and the resp description string */ 
      FormFile[] frmFile= newDocUploadForm.getFleFiles();
      String[] fileDesc=newDocUploadForm.getTxaFileDescs();
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      /* check if total upload limit set in controller tag of struts-config is 
       * beign violated*/ 
      Boolean isLimitExceeded=(Boolean)request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
      if(isLimitExceeded!=null){
        logger.info("Total Upload Limit Violated!!!");
        errorIndex=new Integer(0);
      }
      /*if no violation, upload the valid files and their resp descriptions*/
      else{
      for(int fileCount=0;fileCount<frmFile.length;fileCount++){
        logger.debug("Filename: "+frmFile[fileCount].getFileName()+";Filesize: "+
                      frmFile[fileCount].getFileSize()+";Filetype: "+
                      frmFile[fileCount].getContentType());
        logger.debug("File Description : " + fileDesc[fileCount]);
        /*check for valid file-names and proceed with uploading*/
        if((frmFile[fileCount].getFileName()!=null) && 
           (frmFile[fileCount].getFileName().trim().length()>0)){              
          
          folderDoc.uploadDocument(frmFile[fileCount],currentFolderPath,
                                   fileDesc[fileCount]);
          if(fileNames.length()==0){
            fileNames += frmFile[fileCount].getFileName();
            fileNames.trim();
          }
          else{
            fileNames +=","+frmFile[fileCount].getFileName();
          }
          logger.info("File: "+frmFile[fileCount].getFileName()+
                      " has been uploaded successfully.");
        }
      }
      }
      /*success, set up an action message*/
      if(errorIndex.intValue() == -1){
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.FileUploadedSuccessfully",fileNames);
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
      logger.error("An Exception occurred in NewDocUploadAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward= new String("failure");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in NewDocUploadAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting NewDocUploadAction now...");
    return mapping.findForward(forward);
  }
}
