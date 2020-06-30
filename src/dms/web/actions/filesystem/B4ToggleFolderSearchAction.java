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
 * $Id: B4ToggleFolderSearchAction.java,v 20040220.8 2006/05/30 13:15:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
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
/*java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose : To reconstruct folder tree when new search is required
 * @author              Suved Mishra
 * @version             1.0
 * 	Date of creation:   17-12-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 01-03-2006   
*/
public class B4ToggleFolderSearchAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    UserPreferences userPref = null;
    UserInfo userInfo = null;
    FolderDocInfo folderDocInfo = null;
    DbsLibrarySession dbsLibrarySession = null;
    DbsDirectoryUser dbsDirectoryUser = null;
    DbsPrimaryUserProfile dbsPrimaryUserProfile = null;
    Treeview treeview = null;
    try{
      logger.info("Entering B4ToggleFolderSearchAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      userPref = (UserPreferences)httpSession.getAttribute("UserPreferences");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("folderDocInfo : " + folderDocInfo);
      FolderDocListForm folderdoclistform=(FolderDocListForm)form;
      logger.info("FolderDocListForm: "+folderdoclistform);
      treeview = (Treeview)httpSession.getAttribute("Treeview");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      //The following code is to check is users home folder is set.
      //If not an error is thrown
      dbsDirectoryUser = dbsLibrarySession.getUser();            
      dbsPrimaryUserProfile = dbsDirectoryUser.getPrimaryUserProfile();
      DbsFolder dbsFolder = dbsPrimaryUserProfile.getHomeFolder();
      if(dbsFolder == null){
        ExceptionBean exb = new ExceptionBean();
        exb.setMessage("Home Folder Not Set");
        exb.setMessageKey("errors.homefolder.notset");
        throw exb;
      }else{
        /* set up treeview if toggling from search to listing */
        if (treeview == null){
          logger.info("Building Folder Tree...");
          treeview = new Treeview(dbsLibrarySession,"FolderDocumentList",userPref.getTreeLevel(),getServlet().getServletContext().getRealPath("/")  , userPref.getTreeIconPath() + "/",httpSession.getId(),true);
          folderDocInfo.setJsFileName(treeview.getJsFileName());
          Long currentFolderId = dbsFolder.getId();
          treeview.forAddressBar(currentFolderId);
          logger.debug("currentFolderId : " + currentFolderId);
          logger.info("Building Folder Tree Complete");
          //initialize folderDocInfo for navigation
          folderDocInfo.initializeNevigation();
          Long homeFolderId = dbsFolder.getId();
          logger.debug("homeFolderId : " + homeFolderId);
          /* set navigation specific parameters */
          folderDocInfo.setHomeFolderId(homeFolderId);
          folderDocInfo.setCurrentFolderId(currentFolderId);
          folderDocInfo.setDbsLibrarySession(dbsLibrarySession);
          folderDocInfo.setPageNumber(1);
          folderDocInfo.addFolderDocId(folderDocInfo.getCurrentFolderId());
          httpSession.setAttribute("Treeview", treeview);
          logger.debug("folderDocInfo : " + folderDocInfo);
        }
        /* set listing type to simple listing */
        folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
        folderDocInfo.setTreeVisible(true);
        httpSession.removeAttribute("advanceSearchForm");
      }
      httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");     
      request.setAttribute("reset","reset");
      logger.debug("FolderDocListForm modified to: "+folderdoclistform);
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in B4DocZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request, exceptionBean.getActionErrors());
      forward = "failure";
    }catch(ExceptionBean exb){
      logger.error("An Exception occurred in B4DocZipAction... ");
      logger.error(exb.toString());
      saveErrors(request,exb.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4DocZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }
    logger.info("Exiting B4ToggleFolderSearchAction now...");
    return mapping.findForward(forward);
  }
}
