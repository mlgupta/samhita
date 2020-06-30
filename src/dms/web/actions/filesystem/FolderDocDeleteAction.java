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
 * $Id: FolderDocDeleteAction.java,v 20040220.13 2006/03/13 14:06:50 suved Exp $
 *****************************************************************************
 */

package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.File;
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
 *	Purpose: To delete selected Public Objects
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   23-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocDeleteAction extends Action  {
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
  logger.info("Entering FolderDocDeleteAction now...");
  logger.debug("Deleting Folders And Documents...");
  //variable declaration
  ExceptionBean exceptionBean;
  String forward = "success";
  HttpSession httpSession = null;
  try{
    httpSession = request.getSession(false);            
    FolderDocListForm folderDocListForm = (FolderDocListForm)form;
    logger.debug("folderDocListForm : " + folderDocListForm);
    UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
    Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");

    DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
    Long[] selectedFolderDocIds = folderDocListForm.getChkFolderDocIds();
    logger.debug("selectedFolderDocIds.length : " + selectedFolderDocIds.length);

    String relativePath =httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";

    String userName = dbsLibrarySession.getUser().getDistinguishedName();                
    FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
    boolean recursively = true;
    logger.debug("recursively : " + recursively);
    if(recursively){
      logger.info("Folder will be deleted recursively");
    }
    folderDoc.deleteFolderDoc(selectedFolderDocIds,treeview,recursively,
                              relativePath,userName);
    
    ActionMessages actionMessages = new ActionMessages();
    ActionMessage actionMessage = new ActionMessage("msg.DeletePerformed");
    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
    httpSession.setAttribute("ActionMessages",actionMessages);
    logger.debug("Deleting Folders And Documents complete");
    relativePath = null;
    userName = null;
  }catch(DbsException dex){
    exceptionBean = new ExceptionBean(dex);
    if(dex.containsErrorCode(30659)){
      exceptionBean.setMessageKey("errors.30659.folderdoc.cannotdelete.checkedout");
    }
    logger.error("An Exception occurred in FolderDocDeleteAction... ");
    logger.error(exceptionBean.getErrorTrace());
    logger.info("Delete Aborted");
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }catch(Exception ex){
    exceptionBean = new ExceptionBean(ex);
    logger.error("An Exception occurred in FolderDocDeleteAction... ");
    logger.error(exceptionBean.getErrorTrace());
    logger.info("Delete Aborted");
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }
  logger.info("Exiting FolderDocDeleteAction now...");
  return mapping.findForward(forward);
}
}
