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
 * $Id: FolderNewAction.java,v 20040220.15 2006/03/13 14:06:57 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderNewForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To create new folder
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   28-01-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderNewAction extends Action  {
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
    logger.info("Entering FolderNewAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderNewForm folderNewForm = (FolderNewForm)form;
      logger.debug("Creating New Folder");
      logger.debug("folderNewForm : " + folderNewForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");            
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");

      String currentFolderPath = folderDocInfo.getCurrentFolderPath();
      Long currentFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("currentFolderId : " + currentFolderId);
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      String folderName = folderNewForm.getHdnFolderName();
      logger.debug("folderName : " + folderName);
      String folderDesc = folderNewForm.getHdnFolderDesc();
      logger.debug("folderDesc : " + folderDesc);
      
      if(folderName.trim().length() != 0){
        folderDoc.newFolder(folderName,folderDesc,currentFolderId,treeview);
        logger.info("Folder's FullPath : " + currentFolderPath + "/" + folderName );
      }else{
        ExceptionBean exb = new ExceptionBean();
        exb.setMessage("Folder Name Can't be blank");
        exb.setMessageKey("errors.foldername.required");
        throw exb;
      }
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.NewFolderCreated");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
      logger.debug("Creating New Folder Complete");
    }catch(ExceptionBean exb){
      logger.error("An Exception occurred in FolderNewAction... ");
      logger.error(exb.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exb.getActionErrors());
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderNewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      logger.info("Creation Aborted");
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderNewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      logger.info("Creation Aborted");                    
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderNewAction now...");
    return mapping.findForward(forward);
  }
}
