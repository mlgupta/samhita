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
 * $Id: ThemeListAction.java,v 20040220.6 2006/03/17 12:55:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.theme;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.theme.ThemeListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.theme.Theme;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To display list of the themes available.
 *  @author              Sudheer Pujar 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */ 
public class ThemeListAction extends Action {
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    private Logger logger = null;
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      ExceptionBean exceptionBean=null;  
      DbsLibrarySession dbsLibrarySession=null;
      HttpSession httpSession=null;
      UserInfo userInfo = null;
      String forward = "success";
      Theme theme = null;
      int numRecords=0;
      int pageNumber=1;
      int pageCount=1;
      UserPreferences userPreferences =null;
      String searchString=null;
      ThemeListForm themeListForm =null;
      ArrayList themes=null;
      try{
          logger = Logger.getLogger("DbsLogger");
          logger.info("Entering ThemeListAction now...");
          httpSession = request.getSession(false);
          userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
          dbsLibrarySession = userInfo.getDbsLibrarySession();
          userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
          numRecords= userPreferences.getRecordsPerPage();
          themeListForm =new ThemeListForm();
          themes = new ArrayList();
          
          if(httpSession.getAttribute("pageNumber")!=null){
              pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
              httpSession.removeAttribute("pageNumber");
          }
          if(httpSession.getAttribute("txtSearchByThemeName")!=null){
            searchString=httpSession.getAttribute("txtSearchByThemeName").toString();
            httpSession.removeAttribute("txtSearchByThemeName");
          }

          
          theme = new Theme(dbsLibrarySession);
          themes=theme.listThemes(searchString,pageNumber,numRecords);
          pageCount=theme.getPageCount();
          if (pageNumber>pageCount){
            pageNumber=pageCount;
          }
          if (themes.size()==0 && searchString!=null){
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("search.notFound",searchString);
            messages.add("message1", msg);
            saveMessages(request,messages);
            searchString="";
          }
          themeListForm.setTxtPageNo(new String().valueOf(pageNumber));
          themeListForm.setTxtPageCount(new String().valueOf(pageCount));
          themeListForm.setTxtSearchByThemeName(searchString);
                
                
          request.setAttribute("themes",themes);
          request.setAttribute("themeListForm",themeListForm);

          if (httpSession.getAttribute("messages")!=null){
            saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));  
            httpSession.removeAttribute("messages");
          }
          if(httpSession.getAttribute("errors")!=null) {
            logger.debug("Saving action errors in request stream");    
            saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
            httpSession.removeAttribute("errors");
          }
      }catch(DbsException e){
        forward = "failure";
        exceptionBean = new ExceptionBean(e);
        logger.error("An Exception occurred in ThemeListAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors()); 
      }catch(Exception e){
        forward = "failure";
        exceptionBean = new ExceptionBean(e);
        logger.error("An Exception occurred in ThemeListAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
      }  
      logger.info("Exiting ThemeListAction now...");
      return mapping.findForward(forward);
    }
}
