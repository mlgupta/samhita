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
 * $Id: FolderDocRenameAction.java,v 20040220.19 2006/03/13 14:06:57 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocRenameForm;
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
 *	Purpose: To rename and alter description for selected items
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   02-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocRenameAction extends Action  {
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
    logger.info("Entering FolderDocRenameAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderDocRenameForm folderDocRenameForm = (FolderDocRenameForm)form;
      logger.debug("Renaming Folders And Document...");
      logger.debug("folderDocRenameForm : " + folderDocRenameForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      Long[] selectedFolderDocIds = folderDocRenameForm.getTxtId();
      logger.debug("selectedFolderDocIds.length : " + 
                    selectedFolderDocIds.length);
      String[] selectedFolderDocNames = folderDocRenameForm.getTxtNewName();
      logger.debug("selectedFolderDocNames.length : " + 
                    selectedFolderDocNames.length);
      String[] selectedFolderDocDescs = folderDocRenameForm.getTxtNewDesc();
      logger.debug("selectedFolderDocDescs.length : " + 
                    selectedFolderDocDescs.length);            
      folderDoc.renameFolderDoc(selectedFolderDocIds,selectedFolderDocNames,
                                selectedFolderDocDescs,treeview,relativePath,
                                dbsLibrarySession.getUser().getDistinguishedName());
          
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.ItemsRenamed");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
      relativePath = null;
      logger.debug("Renaming Folders And Document Complete");
    }catch(ExceptionBean exb){
      logger.error("An Exception occurred in FolderDocRenameAction... ");
      logger.error(exb.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exb.getActionErrors());
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocRenameAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if (dex.getErrorCode() == 30011){
        exceptionBean.setMessageKey("errors.30011.folderdoc.unabletorename");
      }
      logger.info("Rename Aborted");                    
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocRenameAction... ");
      logger.error(exceptionBean.getErrorTrace());
      logger.info("Rename Aborted");                    
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocRenameAction now...");
    return mapping.findForward(forward);
  }
}
