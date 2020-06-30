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
 * $Id: ThemeB4EditAction.java,v 20040220.7 2006/03/17 12:55:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.theme;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.theme.ThemeListForm;
import dms.web.actionforms.theme.ThemeNewEditForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.theme.SuffixFilter;
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
/**
 *	Purpose: To display theme_edit.jsp.
 *  @author              Sudheer Pujar 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 
public class ThemeB4EditAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    private Logger logger = null;
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ThemeNewEditForm themeNewEditForm=null;
        ExceptionBean exceptionBean=null;  
        DbsLibrarySession dbsLibrarySession=null;
        HttpSession httpSession=null;
        UserInfo userInfo = null;
        String forward = "success";
        String themeName=null;
        ThemeListForm themeListForm=null;
        Theme theme=null;
        ArrayList fonts=null;
        ArrayList styles=null;
        ArrayList colorSchemes=null;
        String physicalPath=null;
        ActionErrors errors = new ActionErrors();
        try{
            logger = Logger.getLogger("DbsLogger");
            logger.info("Entering ThemeB4EditAction now...");
            httpSession = request.getSession(false);
            userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            dbsLibrarySession = userInfo.getDbsLibrarySession();
            themeListForm = (ThemeListForm) form;
            themeName= (String)httpSession.getAttribute("radSelect");
            theme=new Theme(dbsLibrarySession);
            physicalPath=request.getSession().getServletContext().getRealPath("/");
            fonts=Theme.getFonts("",physicalPath);
            styles=Theme.getStyles("",physicalPath);
            colorSchemes=Theme.getColorSchemes("",physicalPath);
            themeNewEditForm = theme.getThemeProperties(themeName);
            if (themeNewEditForm!=null){
              request.setAttribute("themeNewEditForm",themeNewEditForm);
              request.setAttribute("styles",styles);
              request.setAttribute("fonts",fonts);
              request.setAttribute("colorSchemes",colorSchemes);
              httpSession.setAttribute("backgrounds", SuffixFilter.listFiles(new String[]{"jpg","gif","png","bmp"},physicalPath+"themes/images/backgrounds"));
            }else{
                ActionError editError=new ActionError("errors.theme.notfound",themeName);
                errors.add(ActionErrors.GLOBAL_ERROR,editError);            
                httpSession.setAttribute("errors",errors);
                forward = "failure";
            }
        }catch(DbsException e){
          forward = "failure";
          exceptionBean = new ExceptionBean(e);
          logger.error("An Exception occurred in ThemeB4EditAction... ");
          logger.error(exceptionBean.getErrorTrace());
          saveErrors(request,exceptionBean.getActionErrors()); 
        }catch(Exception e){
          forward = "failure";
          exceptionBean = new ExceptionBean(e);
          logger.error("An Exception occurred in ThemeB4EditAction... ");
          logger.error(exceptionBean.getErrorTrace());
          saveErrors(request,exceptionBean.getActionErrors());
        }  
        if(!errors.isEmpty()) {
          saveErrors(request, errors);
        }        
        logger.info("Exiting ThemeB4EditAction now...");
        return mapping.findForward(forward);
    }
}
