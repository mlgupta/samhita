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
 * $Id: ThemeDeleteAction.java,v 20040220.5 2006/03/17 12:55:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.theme;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To delete the theme selected.
 *  @author              Sudheer Pujar 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 
public class ThemeDeleteAction extends Action {
    private Logger logger = null;
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      ExceptionBean exceptionBean=null;  
      DbsLibrarySession dbsLibrarySession=null;
      HttpSession httpSession=null;
      UserInfo userInfo = null;
      String forward = "success";
      Theme theme = null;
      String themeName=null;
      StyleSheet styleSheet=null;
      String physicalPath=null;
      try{

          logger = Logger.getLogger("DbsLogger");
          logger.info("Entering Theme Delete Action");
          httpSession = request.getSession(false);
          userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
          dbsLibrarySession = userInfo.getDbsLibrarySession();
          themeName= (String)httpSession.getAttribute("radSelect");
          httpSession.removeAttribute("radSelect");
          theme = new Theme(dbsLibrarySession);
          physicalPath=httpSession.getServletContext().getRealPath("/") + "themes/";
          if(theme.deleteTheme(themeName)){  
            styleSheet= new StyleSheet(themeName+".css",physicalPath,new ArrayList()) ;
            styleSheet.delete();
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("theme.delete.ok",themeName);
            messages.add("msg", msg);
            httpSession.setAttribute("messages",messages);
          }
       }catch(DbsException e){
        forward = "failure";
        exceptionBean = new ExceptionBean(e);
        logger.error("An Exception occurred in ThemeDeleteAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors()); 
      }catch(Exception e){
        forward = "failure";
        exceptionBean = new ExceptionBean(e);
        logger.error("An Exception occurred in ThemeDeleteAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
      }  
      logger.info("Exiting Theme Delete Action");
      return mapping.findForward(forward);
    }
}
