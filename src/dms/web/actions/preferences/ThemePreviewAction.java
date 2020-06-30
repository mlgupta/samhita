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
 * $Id: ThemePreviewAction.java,v 1.9 2006/03/13 14:17:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.preferences;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.preferences.UserPreferenceProfileForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.theme.Theme;
import dms.web.beans.theme.Theme.StyleTagPlaceHolder;
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
 *	Purpose: To enable a preview for a theme selected in user preferences.
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:    23-03-2004
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   02-03-2006. 
 */
public class ThemePreviewAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = null;
    ExceptionBean exceptionBean;
    String forward = "success";
    Theme.StyleTagPlaceHolder styleTagPlaceHolder=null;
    DbsLibrarySession dbsLibrarySession=null;
    HttpSession httpSession=null;
    UserInfo userInfo = null;
    UserPreferences userPreferences=null;
    UserPreferenceProfileForm userPreferenceProfileForm=null;
    String themeName=null;
    try{ 
      logger = Logger.getLogger("DbsLogger");
      logger.info("Entering ThemePreviewAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      userPreferences = (UserPreferences) httpSession.getAttribute("UserPreferences");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      userPreferenceProfileForm= (UserPreferenceProfileForm) form;
      themeName=userPreferenceProfileForm.getCboSelectTheme();
      styleTagPlaceHolder = new StyleTagPlaceHolder(dbsLibrarySession,themeName);
      httpSession.setAttribute("styleTagPlaceHolder",styleTagPlaceHolder.getStylePlaceHolder());   
      httpSession.setAttribute("treeIconPath" ,userPreferences.getTreeIconPath());
    }catch(DbsException e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in ThemePreviewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors()); 
    }catch(Exception e){
      forward="failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in ThemePreviewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting ThemePreviewAction now...");
    return mapping.findForward(forward);
  }
}
