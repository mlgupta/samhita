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
 * $Id: ManageWatchAction.java,v 1.2 2006/03/02 09:33:31 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.preferences;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.preferences.UserPreferenceProfileForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ConnectionBean;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.Locale;
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
 *	Purpose: To manage watch on documents.
 *  @author             Suved Mishra 
 *  @version            1.0
 * 	Date of creation:   04-01-2006
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class ManageWatchAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   * The code also takes into account user preferences for opening a document.  
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");        
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ConnectionBean connBean = null;
    Statement statement = null;
    UserPreferenceProfileForm userPreferenceProfileForm=null;
    String operation = request.getParameter("operation");
    logger.info("Entering ManageWatchAction now...");
    logger.debug(operation);
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      userPreferenceProfileForm=(UserPreferenceProfileForm)form;
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      String query = null; 
      String deleteDocID=null;
      deleteDocID= userPreferenceProfileForm.getHdnSelectedDocID();
      //System.out.println(deleteDocID);
      String relativePath =httpSession.getServletContext().getRealPath("/")+
                       "WEB-INF"+File.separator+"params_xmls"+File.separator+
                       "GeneralActionParam.xml";
      connBean = new ConnectionBean(relativePath);
      
      /* obtain statement object */
      String userName= "'" + dbsLibrarySession.getUser().getName() + "'";
      /* obtain statement object */
      statement = connBean.getStatement(true);
      /* statement object successfully obtained */
      if( statement != null ){
      /* construct dynamic query string and execute */
        query="DELETE FROM watch_pos WHERE USER_ID =";
        query = query + userName + " AND ";
        query = query + "PO_ID IN(";
        query = query + deleteDocID +")";
        
        logger.debug(query);
        if((operation.equals("delete")) && (deleteDocID!=null)){
          connBean.executeUpdate(query);
          logger.debug("Watch deleted successfully from user watch list");
          ActionMessages actionMessages = new ActionMessages();
          ActionMessage actionMessage = new ActionMessage("msg.watch.added.successfully");
          actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          httpSession.setAttribute("ActionMessages",actionMessages);
        }
      }
    }catch (DbsException dbsException){
      logger.error("An Exception occurred in ManageWatchAction... ");
      logger.error(dbsException.toString());
    }finally{
      /* close statement and connection objects */
      connBean.closeStatement();
      connBean.closeConnection();          
    }
    logger.info("Exiting ManageWatchAction now...");
    return mapping.findForward("success");   
  }
}