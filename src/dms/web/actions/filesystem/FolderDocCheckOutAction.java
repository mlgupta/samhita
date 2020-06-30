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
 * $Id: FolderDocCheckOutAction.java,v 20040220.26 2006/05/19 06:24:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocCheckOutForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Version;
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To check out a versioned document
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   22-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  02-03-2006  
 */
public class FolderDocCheckOutAction extends Action  {
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
  logger.info("Entering FolderDocCheckOutAction now...");
  logger.debug("Checking Out...");

  //variable declaration
  ExceptionBean exceptionBean;
  String forward = "success";
  Locale locale = getLocale(request);
  FolderDocInfo folderDocInfo = null;
  HttpSession httpSession = request.getSession(false);
  if(httpSession != null){
    try{
      //Initializing variables...        
      FolderDocCheckOutForm folderDocCheckOutForm = (FolderDocCheckOutForm)form;
      logger.debug("folderDocCheckOutForm : " + folderDocCheckOutForm);
      UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo) httpSession.getAttribute("FolderDocInfo");
      folderDocInfo.setNoReloadTree(true);
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long currentFolderId = folderDocInfo.getCurrentFolderId();
      logger.debug("currentFolderId : " + currentFolderId);
      // obtain the folderdocIds
      Long[] folderDocIds = folderDocCheckOutForm.getChkFolderDocIds();
      logger.debug("folderDocIds.length : " + folderDocIds.length);
      for( int index = 0; index < folderDocIds.length; index++ ){
        logger.debug("folderDocIds["+index+"] : "+folderDocIds[index]);
      }
      
      String comment = folderDocCheckOutForm.getTxaComment();
      logger.debug("comment : " + comment);
      logger.info("Comment : " + comment);
      
      String relativePath = httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
  
      String userName = dbsLibrarySession.getUser().getDistinguishedName();                
      // perform check out operation
      Version version  = new Version(dbsLibrarySession);
      ActionMessages actionMessages = new ActionMessages();
      ActionErrors actionErrors = new ActionErrors();
      ActionMessage actionMessage = null;
      ActionError actionError = null;
      int resultCode = version.checkOut(folderDocIds,comment,relativePath,userName);
      switch (resultCode){
        case 0:
          actionError = new ActionError("msg.CheckOutOperationUnsuccessful");
          actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
          httpSession.setAttribute("ActionErrors",actionErrors);
          break;
        case 1:
          actionMessage = new ActionMessage("msg.CheckOutOperationSuccessful");
          actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          httpSession.setAttribute("ActionMessages",actionMessages);
          break;
      }
      logger.debug("CheckOut  Complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      /* IFS-34611 is for inability of a user to upload a new version,due 
       * to access rights limitations viz:-inability to chkout a ver doc. */                
      if(dex.containsErrorCode(34611)){
        exceptionBean.setMessageKey("errors.34611.error.reserving.version.series");
      }
      logger.error("An Exception occurred in FolderDocCheckOutAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocCheckOutAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
  }
  logger.info("Entering FolderDocCheckOutAction now...");
  return mapping.findForward(forward);
}
}
