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
 * $Id: AdapterPreferencesB4Action.java,v 1.5 2006/02/02 12:23:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.actionforms.AdapterPreferenceForm;
/* dms package references */
import dms.beans.DbsLibrarySession;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.FetchAdapters;
//Java API
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//Servlet API
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

/**
 * Purpose            : Action called to display adapter preferences.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 17-01-06
 * Last Modified Date : 19-01-06
 * Last Modified By   : Suved Mishra
 */

public class AdapterPreferencesB4Action extends Action  {

  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    HttpSession httpSession = null;    /* http session for session management */
    UserInfo userInfo = null;          /* user specific information here */
    UserPreferences userPreferences = null;     /* user preferences here */
    DbsLibrarySession dbsLibrarySession = null; /* user library session */
    String adaptersFilePath = null;             /* path for Adapters.xml */ 
    String forward = "success";                 /* mapping for action */ 
    try{
      logger.info("Entering AdapterPreferencesB4Action now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      /* fetch the adapters.xml file path */
      adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "Adapters.xml";
      /* create new adapterPreferenceForm object and prepopulate it with 
       * necessary data for display in adapter_preferences.jsp */
      AdapterPreferenceForm adapterPreferenceForm = new AdapterPreferenceForm();
      
      FetchAdapters fetchAdapters = new FetchAdapters(adaptersFilePath);
      // fetch enabled adapters
      ArrayList allEnabledAdapters = fetchAdapters.fetchEnabledAdaptersList();
      int size = allEnabledAdapters.size();
      String [] lstEnabledAdapters = new String[size];
      ArrayList cboAdapters = null;
      cboAdapters = fetchAdapters.fetchList();
      /* following 2 loc facilitate removal of approval and watch workflow as 
       * they are not required in adapter preferences */
      cboAdapters.remove(0);
      cboAdapters.remove(0);
      for( int index = 0; index < size; index++ ){
        lstEnabledAdapters[index] = (String)allEnabledAdapters.get(index);
      }
      adapterPreferenceForm.setLstEnabledAdapters(lstEnabledAdapters);
      // remove all enabled adapters from the list of available adpters 
      ArrayList availableAdapters = fetchAdapters.fetchAvailableAdaptersList();
      String toBeRemoved = null;
      for( int index = 0; index < size; index++ ){
        toBeRemoved = (String)allEnabledAdapters.get(index);
        availableAdapters.remove(toBeRemoved);
      }
      /* after removal of all enabled adapters, fetch the list of remaining
       * adapters as available */
      size = availableAdapters.size();
      String [] lstAvailableAdapters = new String[size];
      for( int index = 0; index < size; index++ ){
        lstAvailableAdapters[index] = (String)availableAdapters.get(index);
      }
      ActionMessages messages = (ActionMessages)httpSession.getAttribute("messages");
      saveMessages(request,messages);
      httpSession.removeAttribute("messages");    

      ActionErrors errors = (ActionErrors)httpSession.getAttribute("errors");
      saveErrors(request,errors);
      httpSession.removeAttribute("errors");
      
      adapterPreferenceForm.setLstAvailableAdapters(lstAvailableAdapters);
      adapterPreferenceForm.setEnableLookUp(false); 
      adapterPreferenceForm.setNewScreenKey("---Specify New Key---");
      request.setAttribute("adapterPreferenceForm",adapterPreferenceForm);
      request.setAttribute("cboAdapters",cboAdapters);
      request.setAttribute("enableLookUp",new Boolean(false));
    }catch( Exception ex ){
      logger.error("Exception occurred in AdapterPreferencesB4Action ...");
      logger.error(ex.toString());
    }
    logger.info("Exiting AdapterPreferencesB4Action now...");
    return mapping.findForward(forward);
  }
}