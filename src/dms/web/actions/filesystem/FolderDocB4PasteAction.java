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
 * $Id: FolderDocB4PasteAction.java,v 1.7 2006/03/13 14:19:18 suved Exp $
 *****************************************************************************
 */

package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
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
 *	Purpose: Action called to paste selected document(s) and/or folder(s)
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   20-07-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class FolderDocB4PasteAction extends Action  {
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
    logger.info("Entering FolderDocB4PasteAction now...");
    logger.info("Pasting Copied OR Cut Folder or Document");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    boolean askToOverWrite = false;
    try{
      httpSession = request.getSession(false);
      FolderDocPasteForm folderDocPasteForm = (FolderDocPasteForm)form;
      logger.debug("folderDocPasteForm : " + folderDocPasteForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long targetFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("targetFolderId : " + targetFolderId);
      Long[] folderDocIds = folderDocInfo.getClipBoard();
      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      String userName = dbsLibrarySession.getUser().getDistinguishedName();
      if(folderDocIds != null){
        int folderDocLength = folderDocIds.length;
        DbsPublicObject targetFolder = dbsLibrarySession.getPublicObject(targetFolderId);
        for(int index = 0 ; index < folderDocLength; index ++){
          DbsPublicObject dbsPublicObjectToSearch = dbsLibrarySession.getPublicObject(folderDocIds[index]);
          DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
          DbsPublicObject[] dbsPublicObjectFound = dbsFileSystem.searchByName(dbsPublicObjectToSearch.getName(),(DbsFolder)targetFolder, 1);
          if(dbsPublicObjectFound != null){
              askToOverWrite = true;
              break;
          }
        }
        if( askToOverWrite){
          httpSession.setAttribute("OverWrite",new Integer(1));
        }else{
          logger.debug("folderDocIds.length : " + folderDocIds.length);            
          byte actionType = folderDocInfo.getClipBoardContent();
          FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
          if( actionType == FolderDocInfo.CLIPBOARD_CONTENT_COPY){
            logger.debug("actionType : FolderDocInfo.CLIPBOARD_CONTENT_COPY");
            folderDoc.copy(targetFolderId,folderDocIds,
                           new Boolean(false),treeview,relativePath,
                           userName);
          }else{
            if( actionType == FolderDocInfo.CLIPBOARD_CONTENT_CUT){
              logger.debug("actionType : FolderDocInfo.CLIPBOARD_CONTENT_CUT");
              folderDoc.move(targetFolderId,folderDocIds,
                            new Boolean(false),treeview,relativePath,
                            userName);
              folderDocInfo.setClipBoardContent(
                            FolderDocInfo.CLIPBOARD_CONTENT_COPY);
            }
          }
          folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
          ActionMessages actionMessages = new ActionMessages();
          ActionMessage actionMessage = new ActionMessage("msg.PastePerformed");
          actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          httpSession.setAttribute("ActionMessages",actionMessages);
          relativePath = null;
          userName = null;
        }
        logger.info("Pasting Copied OR Cut Folder or Document Complete");
      }else{
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.cutorcopy.documentorfolder.topaste");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocB4PasteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocB4PasteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocB4PasteAction now...");
    return mapping.findForward(forward);
  }
}
