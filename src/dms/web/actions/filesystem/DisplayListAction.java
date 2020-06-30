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
 * $Id: DisplayListAction.java,v 1.0 2006/04/03 14:06:50 suved Exp $
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
import java.util.ArrayList;
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
 *	Purpose           : To display a list of folder(s) and document(s) for a 
 *                      given path or search criteria in list_frame.jsp
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 03-04-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class DisplayListAction extends Action  {
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
    logger.info("Entering DisplayListAction now...");
    ExceptionBean exceptionBean;
    String forward = "success";
    ArrayList folderDocLists = new ArrayList();
    UserPreferences userPreferences = null;
    FolderDocInfo folderDocInfo = null;
    try{
      //Initializing variables
      HttpSession httpSession = request.getSession(false);
      request.setAttribute("folderDocLists",folderDocLists);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long currentFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("CurrentFolderId : " + currentFolderId);
      DbsFolder dbsFolder = null;
      /* obtain the current folder from currentFolderId , incase of an exception
       * set the current folder to the user's home folder */
      try{
        dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                 currentFolderId);
      }catch(DbsException dex){
        try{
          dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                          folderDocInfo.getHomeFolderId());
          folderDocInfo.setCurrentFolderId(dbsFolder.getId());
        }catch(DbsException dex1){
          ExceptionBean exb = new ExceptionBean();
          exb.setMessage("Home Folder Not Set");
          exb.setMessageKey("errors.homefolder.notset");
          throw exb;
        }
      }
      String currentFolderPath = dbsFolder.getAnyFolderPath();
      logger.debug("currentFolderPath : " + currentFolderPath);
      logger.info("Listing items present in the folder : " + currentFolderPath);
      folderDocInfo.setCurrentFolderPath(currentFolderPath);
      /* for FLAT_NAVIGATION, obtain the list of parents of the current folder */
      if(userPreferences.getNavigationType() == UserPreferences.FLAT_NAVIGATION){
        ArrayList listOfParents = folderDocInfo.getListOfParents();
        ArrayList listOfParentsId = folderDocInfo.getListOfParentsId();
        listOfParents.clear();
        listOfParentsId.clear();

        String currentFolderNameTemp = dbsFolder.getName();
        String currentFolderIdTemp = dbsFolder.getId().toString();
        listOfParents.add(0,currentFolderNameTemp);
        listOfParentsId.add(0,currentFolderIdTemp);
        
        while(!dbsFolder.getAnyFolderPath().equals("/")){
          dbsFolder = dbsFolder.getFolderReferences(0);
          currentFolderNameTemp = dbsFolder.getName();
          currentFolderIdTemp = dbsFolder.getId().toString();
          listOfParents.add(0,currentFolderNameTemp);
          listOfParentsId.add(0,currentFolderIdTemp);
        }
      }
      
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        forward = new String("successForTreeNav");
      }else if( userPreferences.getNavigationType() == UserPreferences.FLAT_NAVIGATION ) {
        forward = new String("successForFlatNav");
      }
    }catch(ExceptionBean eb){
      logger.error("An Exception occurred in FolderDocListAction... ");
      logger.error(eb.getErrorTrace());
      saveErrors(request,eb.getActionErrors());
      forward = "failure";
    }catch(DbsException dbsEx){
      exceptionBean = new ExceptionBean(dbsEx);
      logger.error("An Exception occurred in DisplayListAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DisplayListAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }
    logger.info("Exiting DisplayListAction now...");
    return mapping.findForward(forward);

  }
}