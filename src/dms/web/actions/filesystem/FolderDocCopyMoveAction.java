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
 * $Id: FolderDocCopyMoveAction.java,v 20040220.25 2006/05/19 06:24:03 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocCopyMoveForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.FolderOperation;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.Locale;
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
 *	Purpose: To perform copy to and move to operations for items stored in buffer
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   02-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocCopyMoveAction extends Action  {
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
    logger.info("Entering FolderDocCopyMoveAction now...");
    logger.debug("Performing copy or move operation ...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    FolderDocInfo folderDocInfo = null;
    try{
      httpSession = request.getSession(false);
      FolderDocCopyMoveForm folderDocCopyMoveForm = (FolderDocCopyMoveForm)form;
      logger.debug("folderDocCopyMoveForm : " + folderDocCopyMoveForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      String targetFolderPath = folderDocCopyMoveForm.getHdnTargetFolderPath();
      logger.debug("targetFolderPath : " + targetFolderPath);

      DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
      DbsFolder dbsFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath(
                                                     targetFolderPath);
      Long targetFolderId = dbsFolder.getId();
      logger.debug("targetFolderId : " + targetFolderId);

      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      String userName = dbsLibrarySession.getUser().getDistinguishedName();
      Long[] folderDocIds = folderDocCopyMoveForm.getChkFolderDocIds();
      logger.debug("folderDocIds.length : " + folderDocIds.length);
      Boolean overwrite = folderDocCopyMoveForm.isHdnOverWrite();
      logger.debug("overwrite : " + overwrite);
      if(overwrite.booleanValue()){
        logger.debug("The copy is being performed with overwrite option set to true");
      }
      byte actionType = folderDocCopyMoveForm.getHdnActionType();
          
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      if( actionType == FolderOperation.COPY){
        logger.info("actionType : FolderOperation.COPY");
        logger.info("Copying Folders and documents...");
        folderDoc.copy(targetFolderId,folderDocIds,overwrite,treeview,
                       relativePath,userName);
        logger.info("Copy Completed");

        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.CopyToPerformed");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
            
      }else{
        if( actionType == FolderOperation.MOVE){
          logger.info("actionType : FolderOperation.MOVE");
          logger.info("Moving Folders and documents...");
          folderDoc.move(targetFolderId,folderDocIds,overwrite,treeview,
                         relativePath,userName);
          logger.info("Move Complete");

          ActionMessages actionMessages = new ActionMessages();
          ActionMessage actionMessage = new ActionMessage("msg.MoveToPerformed");
          actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          httpSession.setAttribute("ActionMessages",actionMessages);
        }
      }
      folderDocInfo.setCurrentFolderId(targetFolderId);
      Long currentFolderId = targetFolderId;
      folderDocInfo.addFolderDocId(currentFolderId);

      folderDocInfo.setPageNumber(1);
      folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
      folderDocInfo.setNoReloadTree(false);
      userName = null;
      relativePath = null;
      logger.debug("Performing copy or move operation complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      if(dex.getErrorCode() == 30049){
        exceptionBean.setMessageKey("errors.30049.insufficient.access.to.remove.items");
      }
      if(dex.containsErrorCode(30041)){
        exceptionBean.setMessageKey("errors.30041.folderdoc.insufficient.access.updatePO");
      }
      logger.error("An Exception occurred in FolderDocCopyMoveAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocCopyMoveAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocCopyMoveAction now...");
    return mapping.findForward(forward);
  }
}
