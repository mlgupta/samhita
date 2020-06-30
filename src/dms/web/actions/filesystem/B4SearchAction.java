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
 * $Id: B4SearchAction.java,v 1.0 2006/04/03 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.AdvanceSearchForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.ResetBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
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
/**
 *	Purpose           : B4 Action called to display search page
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 03-04-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class B4SearchAction extends Action  {
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
    logger.info("Entering B4SearchAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    FolderDocInfo folderDocInfo = null;
    UserPreferences userPreferences = null;
    try{
      //Initializing variables
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      
      //Obtain the formats available in cmsdk for mime type search options
      Object[] formats = dbsLibrarySession.getFormatCollection().getItems();
      request.setAttribute("formats",formats);
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        folderDocInfo.setNoReloadTree(true);
      }

      ResetBean resetBean=new ResetBean();
      AdvanceSearchForm advanceSearchForm = new AdvanceSearchForm(); 
      advanceSearchForm.setTxtLookinFolderPath(folderDocInfo.getCurrentFolderPath());
      advanceSearchForm=resetBean.resetAll(advanceSearchForm,userPreferences.getNavigationType());
      httpSession.setAttribute("advanceSearchForm",advanceSearchForm);
      
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in B4SearchAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in B4SearchAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
    }
    logger.info("Exiting B4SearchAction now...");
    return mapping.findForward(forward);
  }

}