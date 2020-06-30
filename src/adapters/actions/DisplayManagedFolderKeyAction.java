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
 * $Id: DisplayManagedFolderKeyAction.java,v 1.5 2006/02/02 12:25:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.actionforms.ShowContentForm;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
//Java API
import dms.web.beans.utility.SearchUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
//Servlet API
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.commons.beanutils.PropertyUtils;
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
 * Purpose            : Action called to display the key generated for a given 
 *                      Adapter's managed folder.This key would be used further
 *                      for accessing the folder from a selected page of a 
 *                      client's software. 
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class DisplayManagedFolderKeyAction extends Action  {
  /* date format to be used for generating key id for managed folder */
  public static String DATE_FORMAT = "MMM-DD-yyyy-HH-mm-ss";
  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    HttpSession httpSession = null;    /* http session for session management */
    UserInfo userInfo = null;          /* user specific information here */
    UserPreferences userPreferences = null;     /* user preferences here */
    DbsLibrarySession dbsLibrarySession = null; /* user library session */
    String adaptersFilePath = null;             /* path for Adapters.xml */ 
    String forward = "success";                 /* mapping for action */ 
    XMLBean xmlBean = null;                     /* bean to fetch xml tag values */
    try{
      logger.info("Entering DisplayManagedFolderKeyAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      /* fetch the adapters.xml file path */
      adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "Adapters.xml";
      /* fetch the prefix to find out the adapter name */
      String prefix = (String)PropertyUtils.getSimpleProperty(form,"hdnPrefix");
      logger.debug("prefix : "+prefix);
      /* fetch the folderPath selected for the new key to be generated */
      String folderPath = (String)PropertyUtils.getSimpleProperty(form,"txtFolderPath");
      logger.debug("folderPath : "+folderPath);
      /* set current date as unique key id */
      /*DateHelperForFileSystem dateHelper = new DateHelperForFileSystem();
      String date = dateHelper.format(new Date(),DATE_FORMAT);
      String keyToUser = date;*/
      String keyToUser = (String)PropertyUtils.getSimpleProperty(form,"newScreenKey");
      boolean keyExists = false;
      xmlBean = new XMLBean(adaptersFilePath);
      ArrayList wfNames = xmlBean.getAllNames();
      String wfName = null;
      /* fetch the exact adapter name , given the prefix */
      for( int index = 0; index < wfNames.size(); index++ ){
        if( xmlBean.getValue((String)wfNames.get(index),"PrefixName").equalsIgnoreCase(prefix) ){
          wfName = (String)wfNames.get(index);
          break;
        }
      }
      String [] keyIds = xmlBean.getKeyTagIds(wfName,"key");
      int keyIdsSize = ( keyIds == null )?0:keyIds.length;
      for( int index = 0; index < keyIdsSize; index++ ){
        if( keyIds[index].equals(keyToUser) ){
          keyExists = true;
          break;
        }
      }
      if( !keyExists ){
        DbsFileSystem dbsFs = new DbsFileSystem(dbsLibrarySession);
        /* fetch the folderId for the given folderPath */
        DbsFolder targetFolder = (DbsFolder)dbsFs.findPublicObjectByPath(
                                                                    folderPath);
        /* set the folder ACL to "Public" inorder to overcome access rights problems */
        DbsAccessControlList [] dbsAclsColl = SearchUtil.listAcls(
                                        dbsLibrarySession,"Public",-1,-1,false);
        targetFolder.setAcl(dbsAclsColl[0]);
        String targetFolderId = targetFolder.getId().toString(); 
        logger.debug("keyToUser : "+keyToUser);
        // code for entering value into xml file goes here...
        if( wfName != null ){
          /* Add a new <key> tag under <keys> for the given workflow tag.
           * The format for <key> tag is : <key keyId='keyToUser'>folderId</key>*/
          xmlBean.addNodeUnderTag(wfName,"keys","key",keyToUser,targetFolderId);
        }
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage message = new ActionMessage("msg.key.successfully.set");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,message);
        httpSession.setAttribute("messages",actionMessages);
        // ends here
        /*ShowContentForm showContentForm = new ShowContentForm();
        showContentForm.setManagedFolderKey(keyToUser);
        request.setAttribute("contentForm",showContentForm);
        request.setAttribute("managedFolderKey",keyToUser);*/
      }else{
        ActionErrors actionErrors = new ActionErrors();
        ActionError error = new ActionError("errors.key.already.exists",keyToUser);
        actionErrors.add(ActionErrors.GLOBAL_ERROR,error);
        httpSession.setAttribute("errors",actionErrors);
      }
    }catch( DbsException dbsEx ){
      logger.error("Exception occurred in DisplayManagedFolderKeyAction ...");
      logger.error(dbsEx.toString());
    }catch( Exception ex ){
      logger.error("Exception occurred in DisplayManagedFolderKeyAction ...");
      logger.error(ex.toString());
    }
    logger.info("Exiting DisplayManagedFolderKeyAction now...");
    return mapping.findForward(forward);
  }

}