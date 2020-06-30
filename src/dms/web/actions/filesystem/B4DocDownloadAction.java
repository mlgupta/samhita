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
 * $Id: B4DocDownloadAction.java,v 20040220.12 2006/05/30 13:15:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms Package references*/
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserPreferences;
/*java API*/
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API*/
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Action called b4 downloading a file to check if it has to be opened
 *           in a new window or the same frame 
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    21-02-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class B4DocDownloadAction extends Action  {
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
    logger.info("Entering B4DocDownloadAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    FolderDocInfo folderDocInfo = null;
    UserPreferences userPref = null;
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("folderDocListForm : " + folderDocListForm);
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      userPref = (UserPreferences)httpSession.getAttribute("UserPreferences");
      logger.debug("folderDocInfo: "+folderDocInfo);
      logger.debug("userPreferences: "+userPref);
      /* when doc is to be opened in new window, donot set it's listing type to 
       * DISPLAY_PAGE */
      if(userPref.getChkOpenDocInNewWin() == UserPreferences.IN_NEW_WINDOW){
        forward= new String("forNewWindow");
      }else{
        folderDocInfo.setListingType(FolderDocInfo.DISPLAY_PAGE);
      }
      Long docId = folderDocListForm.getDocumentId();
      logger.debug("docId : " + docId);
      folderDocInfo.setDocId(docId);
      folderDocInfo.addFolderDocId(docId);
      folderDocInfo.setNoReloadTree(true);
      logger.debug("folderDocInfo : " + folderDocInfo);
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4DocDownloadAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting B4DocDownloadAction now...");
    return mapping.findForward(forward);
  }
}
