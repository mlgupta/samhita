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
 * $Id: FolderDocSelectNewFolderAction.java,v 1.11 2006/03/13 14:06:57 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocSelectForm;
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
 *	Purpose: To create and attach a new folder in the navigation tree popup
 *  @author             Sudheer Pujar
 *  @version            1.0
 * 	Date of creation:   02-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocSelectNewFolderAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering FolderDocSelectNewFolderAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    UserInfo userInfo=null;
    DbsLibrarySession dbsLibrarySession = null;
    boolean foldersOnly=true;
    String openerControl=null;
    String currentFolderPath=null; 
    FolderDocSelectForm folderDocSelectForm=null;
    DbsFileSystem dbsFileSystem=null;
    DbsFolder dbsFolder=null;   
    Long currentFolderId4Select=null;
    Treeview treeview4Select =null; 
    String folderName=null;
    String description = null;
    try{
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      folderDocSelectForm= (FolderDocSelectForm) form;
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("Performing Folder Doc Tree Append Action ...");
      logger.debug("folderDocInfo : " + folderDocInfo);

      foldersOnly=folderDocSelectForm.isHdnFoldersOnly();
      openerControl=folderDocSelectForm.getHdnOpenerControl();
      folderName=folderDocSelectForm.getHdnFolderName();
      description = folderDocSelectForm.getHdnFolderDesc();
      currentFolderPath=folderDocSelectForm.getHdnFolderDocument();
      dbsFileSystem= new DbsFileSystem(dbsLibrarySession); 
      currentFolderId4Select=(dbsFileSystem.findPublicObjectByPath(currentFolderPath)).getId();
      treeview4Select=(Treeview)httpSession.getAttribute("Treeview4Select");
      
      try{
        dbsFolder = (DbsFolder)dbsFileSystem.findPublicObjectById(currentFolderId4Select);
        dbsFolder = dbsFileSystem.createFolder(folderName,dbsFolder,true,null);
        dbsFolder.setDescription(description);
        treeview4Select.ifFolderAdded(dbsFolder.getId(),currentFolderId4Select);
        currentFolderId4Select=dbsFolder.getId();
        treeview4Select.forAddressBar(currentFolderId4Select);
        httpSession.setAttribute("currentFolderDocId4Select",currentFolderId4Select);
      }catch(DbsException e){
        exceptionBean = new ExceptionBean(e);
        logger.error("An Exception occurred in FolderDocSelectNewFolderAction... ");
        logger.error(exceptionBean.getErrorTrace());
        httpSession.setAttribute("actionerrors",exceptionBean.getActionErrors());    
      }
      
      httpSession.setAttribute("foldersOnly",new Boolean(foldersOnly));
      httpSession.setAttribute("openerControl",openerControl);  
      
    }catch(DbsException e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in FolderDocSelectNewFolderAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("actionerrors",exceptionBean.getActionErrors());    
    }catch(Exception e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in FolderDocSelectNewFolderAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("actionerrors",exceptionBean.getActionErrors());    
      }
    logger.info("Exiting FolderDocSelectNewFolderAction now...");
    return mapping.findForward(forward);
  }
}
