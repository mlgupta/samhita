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
 * $Id: FolderDocParentAction.java,v 20040220.19 2006/05/19 06:24:03 suved Exp $
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To go to parent folder
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   27-01-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocParentAction extends Action  {
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
    logger.info("Entering FolderDocParentAction now...");
    //variable declaration
    ExceptionBean exceptionBean = null;
    String forward = "success";
    FolderDocInfo folderDocInfo = null; 
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("Finding Parent");
      logger.debug("folderDocInfo : " + folderDocInfo);        
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long currentFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("currentFolderId : " + currentFolderId);
          
      DbsFolder dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                         currentFolderId);
      if(!dbsFolder.getAnyFolderPath().equals("/")){ 
        dbsFolder = dbsFolder.getFolderReferences(0);
        currentFolderId = dbsFolder.getId();
        logger.debug("currentFolderId : " + currentFolderId);
        folderDocInfo.setCurrentFolderId(currentFolderId);
        folderDocInfo.setPageNumber(1);
        folderDocInfo.setHierarchySetNo(1);
        
        folderDocInfo.addFolderDocId(currentFolderId);
        logger.debug("folderDocInfo : " + folderDocInfo);
        folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
        httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");
      }else{
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.CanNotMoveAboveRoot");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }
      logger.debug("Finding Parent Complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocParentAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocParentAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocParentAction now...");
    return mapping.findForward(forward);
  }
}
