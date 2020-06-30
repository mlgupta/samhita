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
 * $Id: ThemeB4NewAction.java,v 1.11 2006/03/17 12:55:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.theme;
/* dms package references */
import dms.web.actionforms.theme.ThemeNewEditForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.theme.SuffixFilter;
import dms.web.beans.theme.Theme;
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
/**
 *	Purpose: To display theme_new.jsp.
 *  @author              Sudheer Pujar 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 
public class ThemeB4NewAction extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = null;
    ExceptionBean exceptionBean=null; 
    ThemeNewEditForm  themeNewEditForm = null; 
    ArrayList fonts=null;
    ArrayList styles=null;
    ArrayList colorSchemes=null;
    String forward = "success";
    String physicalPath=null;
    HttpSession httpSession=null;
    try{
      logger = Logger.getLogger("DbsLogger");
      logger.info("Entering ThemeB4NewAction now...");
      themeNewEditForm = new ThemeNewEditForm();
      //Initilization of the ThemeNewEditForm
      themeNewEditForm.setCboBodyText("Verdana");
      themeNewEditForm.setCboElementText("Verdana");
      themeNewEditForm.setCboHeadings("Verdana");
      themeNewEditForm.setCboMenuText("Verdana");
      themeNewEditForm.setHdnStyle("Standard");
      themeNewEditForm.setHdnBackgroundColor("#ffffff");
      themeNewEditForm.setHdnColorScheme("Ocean");
      themeNewEditForm.setHdnFontColorHeadings("#000099");
      themeNewEditForm.setHdnFontColorBodyText("#000000");
      themeNewEditForm.setHdnFontColorMenuText("#000000");
      themeNewEditForm.setHdnFontColorElementText("#000099");
      themeNewEditForm.setHdnFontColorElementBg("#ffffff");
      themeNewEditForm.setRadBackground("color");

      httpSession= request.getSession(false);
      physicalPath=request.getSession().getServletContext().getRealPath("/");
      fonts=Theme.getFonts("",physicalPath);
      styles=Theme.getStyles("",physicalPath);
      colorSchemes=Theme.getColorSchemes("",physicalPath);
      request.setAttribute("themeNewEditForm",themeNewEditForm);
      httpSession.setAttribute("styles",styles);
      httpSession.setAttribute("fonts",fonts);
      httpSession.setAttribute("colorSchemes",colorSchemes);
      httpSession.setAttribute("backgrounds", SuffixFilter.listFiles(new String[]{"jpg","gif","png","bmp"},physicalPath+"themes/images/backgrounds"));
      
    }catch(Exception e){
      forward = "failure";
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in ThemeB4NewAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }  
    logger.info("Exiting ThemeB4NewAction now...");
    return mapping.findForward(forward);
  }
}
