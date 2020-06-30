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
 * $Id: FolderDocB4RenameAction.java,v 20040220.12 2006/03/13 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
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
 *	Purpose: Action called to display folder_doc_rename.jsp with prepopulated 
 *           name and description(if any)
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   27-01-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class FolderDocB4RenameAction extends Action  {
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
    logger.info("Entering FolderDocB4RenameAction now...");
    logger.debug("Building Rename List...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("folderDocListForm : " + folderDocListForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");

      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long[] selectedFolderDocIds = folderDocListForm.getChkFolderDocIds();
      logger.debug("selectedFolderDocIds.length : " + selectedFolderDocIds.length);
          
      FolderDocList folderDocList;
      DbsPublicObject dbsPublicObject;
      ArrayList folderDocRenameLists = new ArrayList();
      for(int counter = 0; counter < selectedFolderDocIds.length; counter++){
        folderDocList = new FolderDocList();
        dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[counter]);
        Long publicObjectId = dbsPublicObject.getId();
        logger.debug("publicObjectId : " + publicObjectId);
        folderDocList.setId(publicObjectId);             

        String publicObjectName = dbsPublicObject.getName();
        logger.debug("publicObjectName : " + publicObjectName);
        folderDocList.setName(publicObjectName);

        String publicObjectDesc = dbsPublicObject.getDescription();
        logger.debug("publicObjectDesc : " + publicObjectDesc);
        if(publicObjectDesc == null){
          folderDocList.setDescription("");
        }else{
          folderDocList.setDescription(publicObjectDesc);
        }
        folderDocRenameLists.add(folderDocList);
      }
      logger.debug("folderDocRenameLists.size() : " + folderDocRenameLists.size());
      logger.debug("Building Rename List Complete");
      request.setAttribute("folderDocRenameLists", folderDocRenameLists );
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocB4RenameAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocB4RenameAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocB4RenameAction now...");
    return mapping.findForward(forward);
  }
}
