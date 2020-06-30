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
 * $Id: ListManagedFoldersAction.java,v 1.5 2006/02/02 12:26:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.actionforms.AdapterPreferenceForm;
import adapters.beans.ListManagedFoldersBean;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Purpose            : Action called to display all the keys generated as well 
 *                      as managed folders path for a given adapter.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class ListManagedFoldersAction extends Action  {

  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    HttpSession httpSession = null;    /* http session for session management */
    UserInfo userInfo = null;          /* user specific information here */
    UserPreferences userPreferences = null;     /* user preferences here */
    DbsLibrarySession dbsLibrarySession = null; /* user library session */
    String adaptersFilePath = null;             /* path for Adapters.xml */ 
    String forward = "success";                 /* mapping for action */ 
    ListManagedFoldersBean listBean = null;     /* bean to store data for managed folders */
    ArrayList listBeans = new ArrayList();      /* arraylist to store all list bean*/
    AdapterPreferenceForm adapterForm = null;   /* form to fetch data */
    try{
      logger.info("Entering ListManagedFoldersAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      /* fetch the adapters.xml file path */
      adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "Adapters.xml";
      adapterForm = (AdapterPreferenceForm)form;
      /* fetch the prefix to find out the adapter name */
      String prefix = adapterForm.getHdnPrefix();
      logger.debug("prefix : "+prefix);
      
      // code for fetching values from xml file goes here...
      XMLBean xmlBean = new XMLBean(adaptersFilePath);
      ArrayList wfNames = xmlBean.getAllNames();
      String wfName = null;
      /* fetch the exact adapter name , given the prefix */
      for( int index = 0; index < wfNames.size(); index++ ){
        if( xmlBean.getValue((String)wfNames.get(index),"PrefixName").equalsIgnoreCase(prefix) ){
          wfName = (String)wfNames.get(index);
          break;
        }
      }
      if( wfName != null ){
        String [] keyIds = null;
        /* fetch the keyid attribute value for each <key> tag under the 
         * corresponding adapter name */
        keyIds = xmlBean.getKeyTagIds(wfName,"key");
        String [] keyValues = null;
        /* fetch the corresponding text value for each <key> tag under the 
         * corresponding adapter name. Each text value is nothing but managed
         * folder's id for a given keyid.*/
        keyValues = xmlBean.getKeyTagValues(wfName,"key");
        int sizeOfKeyVals = ( keyValues == null )?0:keyValues.length;
        Long [] folderIds = new Long[sizeOfKeyVals];
        DbsPublicObject dbsPO = null; 
        /* prepare a collection of such keyid and keyValue pairs, to be
         * displayed in jsp .*/
        for( int index = 0; index < folderIds.length; index++ ){
          folderIds[index] = new Long(keyValues[index]);
          /* check for the existence of every managed folder first.
           * if folder exists , go ahead and fetch details else skip */
          try{
            dbsPO = dbsLibrarySession.getPublicObject(folderIds[index]);
          }catch( DbsException dbsEx ){
            logger.error("Exception occurred in ListManagedFoldersAction ...");
            logger.error(dbsEx.toString());
          }
          if( dbsPO != null ){
            logger.debug("DbsPO is not null...");
            listBean = new ListManagedFoldersBean();
            listBean.setAccessId(keyIds[index]);
            /* limit the display length for keyId a.k.a accessId here */
            if( keyIds[index].length() > 20 ){
              listBean.setTrimAccessId(keyIds[index].substring(0,16)+"...");
            }else{
              listBean.setTrimAccessId(keyIds[index]);
            }         
            listBean.setManagedFolderPath(dbsPO.getAnyFolderPath());
            /* limit the display length for managed folder's path */
            if( listBean.getManagedFolderPath().length() > 40 ){
              listBean.setTrimManagedFolderPath(
                        listBean.getManagedFolderPath().substring(0,36)+"...");
            }else{
              listBean.setTrimManagedFolderPath(listBean.getManagedFolderPath());
            }
            listBeans.add(listBean);
          }
        }
      }
      request.setAttribute("listBeans",listBeans);
      request.setAttribute("prefix",prefix);
      // ends here
    }catch( DbsException dbsEx ){
      logger.error("Exception occurred in ListManagedFoldersAction ...");
      logger.error(dbsEx.toString());
    }catch( Exception ex ){
      logger.error("Exception occurred in ListManagedFoldersAction ...");
      logger.error(ex.toString());
    }
    logger.info("Exiting ListManagedFoldersAction now...");
    return mapping.findForward(forward);
  }

}