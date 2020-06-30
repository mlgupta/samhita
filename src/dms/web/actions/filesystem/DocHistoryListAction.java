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
 * $Id: DocHistoryListAction.java,v 20040220.14 2006/03/13 14:17:34 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.DocHistoryListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.filesystem.Version;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: Action called to view version list of a versioned document 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   10-03-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class DocHistoryListAction extends Action  {
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
    logger.info("Entering DocHistoryListAction now...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    UserInfo userInfo = null;
    ArrayList documentHistoryDetails;
    UserPreferences userPref = null;
    FolderDocInfo folderDocInfo = null;
    HttpSession httpSession = null;
    ActionErrors actionErrors = null;
    try{
      DocHistoryListForm docHistoryListForm = (DocHistoryListForm)form;
      logger.debug("docHistoryListForm : " + docHistoryListForm);
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      userPref = (UserPreferences)httpSession.getAttribute("UserPreferences");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");            
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");

      Long docId = docHistoryListForm.getChkFolderDocIds()[0];
      logger.debug("docId : " + docId);
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      int recordPerPage = userPref.getRecordsPerPage();
      logger.debug("recordPerPage : " + recordPerPage);
      int pageNumber = docHistoryListForm.getTxtHistoryPageNo();
      
      if(pageNumber == 0 ){
          pageNumber = 1;
          docHistoryListForm.setTxtHistoryPageNo(pageNumber);
      }
      logger.debug("pageNumber : " + pageNumber);
      
      String documentName = dbsLibrarySession.getPublicObject(docId).getName();
      
      logger.debug("documentName : " + documentName);
      docHistoryListForm.setDocumentName(documentName);
      
      Version version = new Version(dbsLibrarySession);
      documentHistoryDetails = version.getDocumentHistoryDetails(docId);

      int startIndex = recordPerPage * (pageNumber - 1) ;
      
      int endIndex = startIndex + recordPerPage;
      logger.debug("startIndex : " + startIndex);
      
      if(endIndex > documentHistoryDetails.size()){
        endIndex = documentHistoryDetails.size();
      }
      logger.debug("endIndex : " + endIndex);
      
      int pageCount =(int)StrictMath.ceil((double)documentHistoryDetails.size()/ 
                                         recordPerPage);
      if(pageCount > 0){
          docHistoryListForm.setTxtHistoryPageCount(pageCount);
      }else{
          docHistoryListForm.setTxtHistoryPageCount(1);
      }
      logger.debug("pageCount : " + pageCount);
      
      int itemCount = documentHistoryDetails.size();
      logger.debug("itemCount : " + itemCount);
      
      for(int index = endIndex; index < itemCount; index++){
        documentHistoryDetails.remove(endIndex);
      }

      for(int index = 0; index < startIndex; index++){
        documentHistoryDetails.remove(0);
      }
      
      logger.debug("docHistoryListForm : " + docHistoryListForm);
      
      request.setAttribute("documentHistoryDetails",documentHistoryDetails);

      actionErrors = (ActionErrors)httpSession.getAttribute("ActionErrors");
      if(actionErrors != null){
        logger.debug("Saving action error in request stream");
        saveErrors(request,actionErrors);
        httpSession.removeAttribute("ActionErrors");
      }else{                        
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.DocumentHistory");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        saveMessages(request,actionMessages);
      }
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
    logger.info("Exiting DocHistoryListAction now...");
    return mapping.findForward(forward);
  }
}