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
 * $Id: AdapterPreferencesAction.java,v 1.3 2006/02/02 12:21:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.actionforms.AdapterPreferenceForm;
import adapters.beans.XMLBean;
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
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Purpose            : Action called to set adapter preferences.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 17-01-06
 * Last Modified Date : 19-01-06
 * Last Modified By   : Suved Mishra
 */

public class AdapterPreferencesAction extends Action  {
  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    HttpSession httpSession = null;    /* http session for session management */
    UserInfo userInfo = null;          /* user specific information here */
    UserPreferences userPreferences = null;     /* user preferences here */
    DbsLibrarySession dbsLibrarySession = null; /* user library session */
    String adaptersFilePath = null;             /* path for Adapters.xml */ 
    String forward = "success";                 /* mapping for action */ 
    AdapterPreferenceForm adapterPreferenceForm = null;  /* form to fetch data */
    try{
      logger.info("Entering AdapterPreferencesAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      /* fetch the adapters.xml file path */
      adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "Adapters.xml";
      adapterPreferenceForm = (AdapterPreferenceForm)form;
      /* fetch the list of available adapters */
      String [] lstAvailable = (String [])PropertyUtils.getSimpleProperty(
                                          form,"lstAvailableAdapters");
      int size = (lstAvailable == null)?0:lstAvailable.length;
      for( int index = 0; index < size; index++ ){
        logger.debug("lstAvailable ["+index+"] : "+lstAvailable[index]);
      }

      /* fetch the list of enabled adapters */
      String [] lstEnabled = (String [])PropertyUtils.getSimpleProperty(
                                        form,"lstEnabledAdapters");
      size = (lstEnabled == null)?0:lstEnabled.length;
      for( int index = 0; index < size; index++ ){
        logger.debug("lstEnabled ["+index+"] : "+lstEnabled[index]);
      }
      
      XMLBean xmlBean = new XMLBean(adaptersFilePath);
      ArrayList wfNames = xmlBean.getAllNames();
      /* enable all those selected in enable list */
      if( lstEnabled != null && lstEnabled.length !=0 ){
        for( int i = 0; i < wfNames.size(); i++ ){
          for( int j = 0; j < lstEnabled.length; j++ ){
            if( xmlBean.getValue((String)wfNames.get(i),"Name").equalsIgnoreCase(lstEnabled[j]) ){
              xmlBean.setValue((String)wfNames.get(i),"Enabled","True");
            }
          }
        }
      }
      /* disable all those remaining in available list */
      if( lstAvailable != null && lstAvailable.length !=0 ){
        for( int i = 0; i < wfNames.size(); i++ ){
          for( int j = 0; j < lstAvailable.length; j++ ){
            if( xmlBean.getValue((String)wfNames.get(i),"Name").equalsIgnoreCase(lstAvailable[j]) ){
              xmlBean.setValue((String)wfNames.get(i),"Enabled","False");
            }
          }
        }
      }
    }catch( Exception ex ){
      logger.error("Exception occurred in AdapterPreferencesAction ...");
      logger.error(ex.toString());
    }
    logger.info("Exiting AdapterPreferencesAction now...");
    return mapping.findForward(forward);
  }

}