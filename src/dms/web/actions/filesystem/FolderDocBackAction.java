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
 * $Id: FolderDocBackAction.java,v 20040220.23 2006/05/19 06:24:03 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
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
/**
 *	Purpose: Action called to navigate backwards in navigation history
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   27-01-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class FolderDocBackAction extends Action  {
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
    logger.info("Entering FolderDocBackAction now...");
    logger.info("Fetching Previous Folder Path...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("folderDocInfo : " + folderDocInfo);            
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      if(folderDocInfo.getListingType()  != FolderDocInfo.DISPLAY_PAGE){
        Long folderDocId = folderDocInfo.getPrevFolderDocId();
        if(folderDocId == null){
          ExceptionBean exb = new ExceptionBean();
          exb.setMessage("Home Folder Deleted");
          exb.setMessageKey("errors.homefolder.delete");
          throw exb;
        }
        DbsPublicObject dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocId);
        folderDocInfo.setCurrentFolderId(folderDocId);
        folderDocInfo.setPageNumber(1);
        folderDocInfo.setHierarchySetNo(1);
        logger.debug("folderDocInfo : " + folderDocInfo);
      }
      folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);            
      httpSession.setAttribute("IS_SIMPLE_NAVIGATION","TRUE");
      logger.info("Fetching Previous Folder Path Complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocBackAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(ExceptionBean exb){
      logger.error("An Exception occurred in FolderDocBackAction... ");
      logger.error(exb.getErrorTrace());
      saveErrors(request,exb.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocBackAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocBackAction now...");
    return mapping.findForward(forward);
  }
}
