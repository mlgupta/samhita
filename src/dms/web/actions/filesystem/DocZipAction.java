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
 * $Id: DocZipAction.java,v 20040220.18 2006/05/19 06:24:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.DocZipForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.ZipBean;
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
 *	Purpose: Action called to deflate a file
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   27-04-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class DocZipAction extends Action  {
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
    logger.info("Entering DocZipAction now...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DbsDocument dbsDocument = null;
    HttpSession httpSession = null;
    try{
      logger.info("Zipping File ...");
      DocZipForm docZipForm = (DocZipForm)form;
      logger.debug("docZipForm : " + docZipForm);
      httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      FolderDocInfo folderDocInfo = (FolderDocInfo)
                                httpSession.getAttribute("FolderDocInfo");
      folderDocInfo.setNoReloadTree(true);
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long documentIds[] = docZipForm.getChkFolderDocIds();
      String zipFileName = docZipForm.getHdnZipFileName();
      logger.debug("zipFileName : " + zipFileName);
      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      String userName = dbsLibrarySession.getUser().getDistinguishedName();
  
      ZipBean zipBean = new ZipBean(dbsLibrarySession,folderDocInfo);
      zipBean.zipFile(documentIds,zipFileName,relativePath,userName);
      
      userName = null;
      relativePath = null;
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.document.zipped.successfully");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
      logger.info("Zipping File complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in DocZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if(dex.getErrorCode() == 68004){
        exceptionBean.setMessageKey("msg.folder.path.not.found");
      }
      if(dex.getErrorCode() == 68010){
        exceptionBean.setMessageKey("msg.document.exist");
      }
      if(dex.containsErrorCode(30036)){
        exceptionBean.setMessageKey("errors.30036.insufficient.access.to.get.content");
      }
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DocZipAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting DocZipAction now...");
    return mapping.findForward(forward);
  }
}
