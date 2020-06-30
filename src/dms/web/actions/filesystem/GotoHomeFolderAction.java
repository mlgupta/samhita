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
 * $Id: GotoHomeFolderAction.java,v 1.0 2006/03/28 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
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
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose           : To enable user to navigate to his home folder directly 
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 28-03-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class GotoHomeFolderAction extends Action  {
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
    UserPreferences userPreferences = null;
    FolderDocInfo folderDocInfo = null;
    ActionMessages actionMessages = null;
    try{
      logger.info("Entering GotoHomeFolderAction now...");
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      /* set the current folder to home folder */
      folderDocInfo.setCurrentFolderId(folderDocInfo.getHomeFolderId());
      /* set the listing type to SIMPLE_LISTING */
      folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
      DbsLibrarySession dbsSession = userInfo.getDbsLibrarySession();
      /* obtain the home folder path for the current user */
      String homeFolderPath = ((DbsFolder)dbsSession.getPublicObject(folderDocInfo.getHomeFolderId())).getAnyFolderPath();
      /* set the current folder path to home folder path */
      folderDocInfo.setCurrentFolderPath(homeFolderPath);
      /* add the home folder in navigation history */
      folderDocInfo.addFolderDocId(folderDocInfo.getHomeFolderId());
      /* set the current page number and hierarchy set number for display to 1 */
      folderDocInfo.setPageNumber(1);
      folderDocInfo.setHierarchySetNo(1);
      logger.debug("Navigation set to home folder successfully...");
    }catch( DbsException dbsEx ){
      forward = "failure";
      exceptionBean = new ExceptionBean(dbsEx);
      logger.error("An Exception occurred in GotoHomeFolderAction ...");
      logger.error(exceptionBean.getErrorTrace());
    }catch( Exception ex ){
      forward = "failure";
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in GotoHomeFolderAction ...");
      logger.error(exceptionBean.getErrorTrace());
    }
    logger.info("Exiting GotoHomeFolderAction now...");
    return mapping.findForward(forward);
  }
}