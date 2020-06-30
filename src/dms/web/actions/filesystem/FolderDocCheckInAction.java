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
 * $Id: FolderDocCheckInAction.java,v 20040220.25 2006/05/19 06:24:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocCheckInForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Version;
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To check in a versioned document ie: create new version
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   29-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006  
 */
public class FolderDocCheckInAction extends Action  {
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
    logger.info("Entering FolderDocCheckInAction now...");
    logger.debug("Checking In...");

    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    HttpSession httpSession = request.getSession(false);
    if(httpSession != null){
      try{
        //Initializing variables...        
        FolderDocCheckInForm folderDocCheckInForm = (FolderDocCheckInForm)form;
        logger.debug("folderDocCheckInForm : " + folderDocCheckInForm);
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
        FolderDocInfo folderDocInfo = (FolderDocInfo) 
                                      httpSession.getAttribute("FolderDocInfo");
        UserPreferences userPreferences = (UserPreferences)
                                    httpSession.getAttribute("UserPreferences");
        
        DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
        Long currentFolderId = folderDocInfo.getCurrentFolderId();
        logger.debug("currentFolderId : " + currentFolderId);       
        
        folderDocInfo.setNoReloadTree(true);
        
        Long[] folderDocIds = folderDocCheckInForm.getChkFolderDocIds();
        logger.debug("folderDocIds.length : " + folderDocIds.length);
        for( int index = 0; index < folderDocIds.length; index++ ){
          logger.debug("folderDocIds["+index+"] : "+folderDocIds[index]);
        }
        String comment = folderDocCheckInForm.getTxaComment();
        logger.debug("comment : " + comment);
        logger.info("Comment : " + comment);
        boolean keepCheckedOut = folderDocCheckInForm.isChkKeepCheckedOut();
        logger.debug("keepCheckedOut : " + keepCheckedOut);
        ActionErrors actionErrors = new ActionErrors();
        ActionMessages actionMessages = new ActionMessages();
        
        String relativePath =httpSession.getServletContext().getRealPath("/")+
                             "WEB-INF"+File.separator+"params_xmls"+File.separator+
                             "GeneralActionParam.xml";
    
        String userName = dbsLibrarySession.getUser().getDistinguishedName();                
        
        Version version  = new Version(dbsLibrarySession);
        if(keepCheckedOut){
            logger.info("The document will remain checked out");
        }
        switch(version.checkIn(folderDocIds,comment,keepCheckedOut,relativePath,userName)){
          case 0:
              ActionError actionError = new ActionError("msg.CheckInOperationUnsuccessful");
              actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
              httpSession.setAttribute("ActionErrors",actionErrors);
              break;
          case 1:
              ActionMessage actionMessage = new ActionMessage("msg.CheckInOperationSuccessful");
              actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
              httpSession.setAttribute("ActionMessages",actionMessages);
              break;
        }
        logger.debug("Checking In Complete");
      }catch(DbsException dex){
        exceptionBean = new ExceptionBean(dex);
        logger.error("An Exception occurred in FolderDocCheckInAction... ");
        logger.error(exceptionBean.getErrorTrace());
        /* IFS-30063 is for inability of a user to upload a new version,
         * due to insufficient access to change PublicObject's 
         * PolicyBundle. */
        if(dex.containsErrorCode(30063)){
          exceptionBean.setMessageKey("errors.30063.insufficient.access.tochange.PO's.PB");
        }
        
        if(dex.getErrorCode() == 68017){
          exceptionBean.setMessageKey("errors.68017.checkedout.byotheruser");
        }
        httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
      }catch(Exception ex){
        exceptionBean = new ExceptionBean(ex);
        logger.error("An Exception occurred in FolderDocCheckInAction... ");
        logger.error(exceptionBean.getErrorTrace());
        httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
      }
    }
    logger.info("Exiting FolderDocCheckInAction now...");
    return mapping.findForward(forward);
  }
}
