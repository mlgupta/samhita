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
 * $Id: FolderDocMakeVersionableAction.java,v 20040220.16 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.Version;
import dms.web.beans.filesystem.FolderDocInfo;
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
 *	Purpose: To make selected Public Objects Versioned.
 *  @author              Maneesh Mishra  
 *  @version             1.0
 * 	Date of creation:    20-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  23/12/2005 
 */
public class FolderDocMakeVersionableAction extends Action  {
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
    logger.info("Entering FolderDocMakeVersionableAction now...");

    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = null;
    try{
      logger.debug("Making document versionable");
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("folderDocListForm : " + folderDocListForm);
      UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)
                                    httpSession.getAttribute("FolderDocInfo");
      folderDocInfo.setNoReloadTree(true);
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      
      Long[] folderDocIds = folderDocListForm.getChkFolderDocIds();
      logger.debug("folderDocIds.length : " + folderDocIds.length);

      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
  
      String userName = dbsLibrarySession.getUser().getDistinguishedName();                
      Version version  = new Version(dbsLibrarySession);
      version.makeVersioned(folderDocIds,relativePath,userName);
      
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.DocumentMadeVersionable");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
      logger.debug("Making document versionable complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      if(dex.containsErrorCode(30049)){
        exceptionBean.setMessageKey("errors.30049.insufficient.access.to.remove.items");
      }
      logger.error("An Exception occurred in FolderDocMakeVersionableAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocMakeVersionableAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocMakeVersionableAction now...");
    return mapping.findForward(forward);
  }
}
