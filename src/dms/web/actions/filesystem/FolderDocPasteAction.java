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
 * $Id: FolderDocPasteAction.java,v 20040220.18 2006/03/13 14:15:38 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocPasteForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
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
 *	Purpose: To perform paste operation for items stored in buffer
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   05-03-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocPasteAction extends Action  {
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
    logger.info("Entering FolderDocPasteAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderDocPasteForm folderDocPasteForm = (FolderDocPasteForm)form;
      logger.debug("Pasting Copied OR Cut Folder or Document");
      logger.debug("folderDocPasteForm : " + folderDocPasteForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long targetFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("targetFolderId : " + targetFolderId);
      Long[] folderDocIds = folderDocInfo.getClipBoard();
     
      if(folderDocIds != null){
        int folderDocLength = folderDocIds.length;
        DbsPublicObject targetFolder = dbsLibrarySession.getPublicObject(targetFolderId);
        logger.debug("folderDocIds.length : " + folderDocIds.length);            
        
        boolean overwrite = false;
        Integer pasteOption =  folderDocPasteForm.getRadPasteOption();
        if( pasteOption.intValue() == 1){
          overwrite = true;
        }
        if(overwrite){
          logger.debug("The folder or document is being pasted with overwrite option set to true");
        }
        byte actionType = folderDocInfo.getClipBoardContent();
        FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
        if( actionType == FolderDocInfo.CLIPBOARD_CONTENT_COPY){
          logger.debug("actionType : FolderDocInfo.CLIPBOARD_CONTENT_COPY");
          folderDoc.copy(targetFolderId,folderDocIds,Boolean.valueOf(overwrite),treeview,null,null);
        }else{
          if( actionType == FolderDocInfo.CLIPBOARD_CONTENT_CUT){
            logger.debug("actionType : FolderDocInfo.CLIPBOARD_CONTENT_CUT");
            folderDoc.move(targetFolderId,folderDocIds,Boolean.valueOf(overwrite),treeview,null,null);
            folderDocInfo.setClipBoardContent(FolderDocInfo.CLIPBOARD_CONTENT_COPY);
          }
        }
        folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.PastePerformed");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }else{
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.cutorcopy.documentorfolder.topaste");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }
      logger.debug("Pasting Copied OR Cut Folder or Document Complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocPasteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocPasteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocPasteAction now...");
    return mapping.findForward(forward);
  }
}
