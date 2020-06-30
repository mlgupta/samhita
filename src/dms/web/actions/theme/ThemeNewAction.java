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
 * $Id: ThemeNewAction.java,v 20040220.6 2006/03/17 12:55:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.theme;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.theme.ThemeNewEditForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.theme.StyleSheet;
import dms.web.beans.theme.Theme;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
/* Servlet API */
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
 *	Purpose: To create new theme .
 *  @author              Sudheer Pujar 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 

public class ThemeNewAction extends Action 
{
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  private Logger logger = null;
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    ExceptionBean exceptionBean=null;  
    DbsLibrarySession dbsLibrarySession=null;
    HttpSession httpSession=null;
    UserInfo userInfo = null;
    String forward = "success";
    Theme theme = null;
    String themeName=null;
    ActionErrors errors = new ActionErrors();
    ActionMessages messages = new ActionMessages();
    StyleSheet styleSheet=null;
    String physicalPath=null;
    ArrayList elementList=null;
    try{
        logger = Logger.getLogger("DbsLogger");
        logger.info("Entering ThemeNewAction now...");
        httpSession = request.getSession(false);
        userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        theme = new Theme(dbsLibrarySession);
        themeName=((ThemeNewEditForm)form).getTxtThemeName();
        physicalPath=httpSession.getServletContext().getRealPath("/");
        if (!theme.createTheme((ThemeNewEditForm)form)){
            ActionError error = new ActionError("errors.theme.unique",themeName);
            errors.add(ActionErrors.GLOBAL_ERROR, error);            
        }else{
            elementList=Theme.getStyleElementList((ThemeNewEditForm)form,"",physicalPath);
            styleSheet= new StyleSheet(themeName+".css",physicalPath+"themes/",elementList) ;
            styleSheet.create();
            ActionMessage msg = new ActionMessage("theme.new.ok",themeName);
            messages.add("msg", msg);      
            httpSession.removeAttribute("styles");
            httpSession.removeAttribute("fonts");
            httpSession.removeAttribute("colorSchemes");
            httpSession.removeAttribute("backgrounds");
      
        }          
        if (!errors.isEmpty()){
          saveErrors(request,errors);
          return (mapping.getInputForward());
        }
        if (!messages.isEmpty()){
          httpSession.setAttribute("messages",messages);
        }
     }catch(DbsException e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in ThemeNewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors()); 
    }catch(Exception e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in ThemeNewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }  
    logger.info("Exiting ThemeNewAction now...");
    return mapping.findForward(forward);
  }
}
