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
 * $Id: FolderDocMenuClickAction.java,v 20040220.28 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
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
 *	Purpose: Action called when the document icon on webtop is clicked
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   23-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocMenuClickAction extends Action  {
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
    logger.info("Entering FolderDocMenuClickAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    try{
      logger.debug("Performing menu click operation ...");
      httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("folderDocInfo : " + folderDocInfo);
      FolderDocListForm folderdoclistform=(FolderDocListForm)form;
      logger.debug("FolderDocListForm: "+folderdoclistform);
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();

      //The following code is to check is users home folder is set.
      //If not an error is thrown
      DbsDirectoryUser dbsDirectoryUser = dbsLibrarySession.getUser();            
      DbsPrimaryUserProfile dbsPrimaryUserProfile = dbsDirectoryUser.getPrimaryUserProfile();
      DbsFolder dbsFolder = dbsPrimaryUserProfile.getHomeFolder();
      if(dbsFolder == null){
        ExceptionBean exb = new ExceptionBean();
        exb.setMessage("Home Folder Not Set");
        exb.setMessageKey("errors.homefolder.notset");
        throw exb;
      }else{
        if (treeview == null){
          logger.info("Building Folder Tree...");
          treeview = new Treeview(dbsLibrarySession,"FolderDocumentList",userPreferences.getTreeLevel(),getServlet().getServletContext().getRealPath("/")  , userPreferences.getTreeIconPath() + "/",httpSession.getId(),true);
          folderDocInfo.setJsFileName(treeview.getJsFileName());
          Long currentFolderId = dbsFolder.getId();
          treeview.forAddressBar(currentFolderId);
          logger.debug("currentFolderId : " + currentFolderId);
          logger.info("Building Folder Tree Complete");
              
          //initialize folderDocInfo for nevigation
          folderDocInfo.initializeNevigation();

          Long homeFolderId = dbsFolder.getId();
          logger.debug("homeFolderId : " + homeFolderId);
          folderDocInfo.setHomeFolderId(homeFolderId);
          folderDocInfo.setCurrentFolderId(currentFolderId);
          folderDocInfo.setDbsLibrarySession(dbsLibrarySession);
          folderDocInfo.setPageNumber(1);
          folderDocInfo.addFolderDocId(folderDocInfo.getCurrentFolderId());
          httpSession.setAttribute("Treeview", treeview);
          folderDocInfo.setHierarchySetNo(1);
          
          logger.debug("folderDocInfo : " + folderDocInfo);
        }
        folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
        folderDocInfo.setTreeVisible(true);
        httpSession.removeAttribute("advanceSearchForm");
      }
      httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");      
      logger.debug("FolderDocListForm at the end: "+folderdoclistform);
      logger.debug("Performing menu click operation complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocMenuClickAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request, exceptionBean.getActionErrors());
      forward = "failure";
    }catch(ExceptionBean exb){
      logger.error("An Exception occurred in FolderDocMenuClickAction... ");
      logger.error(exb.getErrorTrace());
      saveErrors(request,exb.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocMenuClickAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }
    logger.info("Exiting FolderDocMenuClickAction now...");
    return mapping.findForward(forward);
  }
}
