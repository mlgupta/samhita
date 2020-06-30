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
 * $Id: FolderDocFolderClickAction.java,v 20040220.19 2006/05/19 06:24:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
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
 *	Purpose: To perform folder click operation revealing folder contents
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   23-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocFolderClickAction extends Action  {
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
    logger.info("Entering FolderDocFolderClickAction now...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    FolderDocInfo folderDocInfo = null;
    try{
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("Folder Click operation performed");
      logger.debug("folderDocListForm : " + folderDocListForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("folderDocInfo : " + folderDocInfo);
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long currentFolderId = folderDocListForm.getCurrentFolderId();
      logger.debug("currentFolderId : " + currentFolderId);
          
      folderDocInfo.setCurrentFolderId(currentFolderId);
      folderDocInfo.setPageNumber(1);
      folderDocInfo.setHierarchySetNo(1);
      folderDocInfo.addFolderDocId(currentFolderId);

      DbsFolder currentFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                             currentFolderId);
      String currentFolderPath = currentFolder.getAnyFolderPath();
      logger.debug("currentFolderPath : " + currentFolderPath);

      if(! currentFolderPath.equals("/")){
        treeview.forAddressBar(currentFolderId);
      }
      folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
      httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");
      logger.debug("Folder Click operation performed complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocFolderClickAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocFolderClickAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocFolderClickAction now...");
    return mapping.findForward(forward);
  }
}
