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
 * $Id: FolderDocAddWatchAction.java,v 1.3 2006/01/03 06:32:21 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsFileSystem;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.loginout.LoginBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
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
 *	Purpose: To add watch to selected Public Objects.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    15/12/2005
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   26/12/2005 
 */
public class FolderDocAddWatchAction extends Action  {

public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException{
  
  String target = new String("success");
  Logger logger = Logger.getLogger("DbsLogger");
  HttpSession httpSession = null;
  DbsLibrarySession dbsLibrarySession = null;
  DbsFileSystem dbsFs = null;
  ConnectionBean connBean = null;
  LoginBean loginBean = null;
  Statement statement = null;
  UserInfo userInfo = null;
  FolderDocInfo folderDocInfo = null;
  FolderDocListForm folderDocListForm = null;
  ParseXMLTagUtil parseUtil = null;
  String mailId = null;
  boolean addWatch = true;
  boolean watchExists = false;
  String query = null;
  String docName = null;
  
  try{
    httpSession = request.getSession(false);
    logger.info("Entering FolderDocAddWatch now...");
    userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
    folderDocListForm = (FolderDocListForm)form;
    dbsLibrarySession = userInfo.getDbsLibrarySession();
    folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
    /* donot reload the tree in this case */
    folderDocInfo.setNoReloadTree(true);
    /* obtain selected POs ids */
    Long [] selectedPOsIds = folderDocListForm.getChkFolderDocIds();
    int idsSize = ( selectedPOsIds == null )?0:selectedPOsIds.length;
    String relativePath =httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+
                         File.separator+"GeneralActionParam.xml";
    String userName = dbsLibrarySession.getUser().getDistinguishedName();
    /* if idsSize > 0 */
    if( idsSize>0 ){
      dbsFs = new DbsFileSystem(dbsLibrarySession);
      connBean = new ConnectionBean(relativePath);
      for( int index = 0;index < idsSize; index++ ){
        /* check if the user has already applied watch on the document */
        String [] results = null;
        query = "SELECT USER_ID FROM watch_pos WHERE PO_ID="+selectedPOsIds[index]+" AND USER_ID='"+userName+"'";
        results = connBean.executeQuery(query);
        if( connBean.isUserWatchAdded(results,userName) ){
          watchExists = true;
          docName = dbsLibrarySession.getPublicObject(selectedPOsIds[index]).getName();
          results = null;
          break;
        }
        results = null;
      }
      query = null;
      connBean.closeStatement();
      connBean.closeConnection();
      connBean = null;
      /* go ahead to fetch db conn and update table */
      if( !watchExists ){
        parseUtil = new ParseXMLTagUtil(relativePath);
        String sysaduser = parseUtil.getValue("sysaduser","Configuration");
        String sysadpwd = parseUtil.getValue("sysadpwd","Configuration");
        String domain = parseUtil.getValue("Domain","Configuration");
        String ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword",
                                                      "Configuration");
        String ifsService = parseUtil.getValue("IfsService","Configuration");
        String serviceConfiguration = parseUtil.getValue("ServiceConfiguration",
                                                         "Configuration");
        
        loginBean = new LoginBean(sysaduser,sysadpwd);
        try{
          loginBean.setServiceParams(ifsService,ifsSchemaPassword,
                                      serviceConfiguration,domain);
          loginBean.setdbsCtc();
          loginBean.startDbsService();
          loginBean.setUserSession();
          mailId = loginBean.getEmailId(dbsLibrarySession.getUser());
        }catch (Exception e) {
          logger.error("An Exception occurred in FolderDocAddWatch... ");
          logger.error(e.toString());
        }finally{
          loginBean.terminateService();
        }
        if(mailId!=null){
        /* obtain connection bean */
        connBean = new ConnectionBean(relativePath);
        try{
          /* obtain statement object */
          statement = connBean.getStatement(true);
          /* statement object successfully obtained */
          if( statement != null ){
            /* construct dynamic query string and execute */
            for( int index = 0; index < idsSize; index++ ){
              query = "INSERT INTO watch_pos VALUES("+selectedPOsIds[index]+",'"+userName+"')";
              statement.executeUpdate(query);
              query = null;
            }
            logger.debug("Watch added successfully to selected POs");
            ActionMessages actionMessages = new ActionMessages();
            ActionMessage actionMessage = 
                              new ActionMessage("msg.watch.added.successfully");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            httpSession.setAttribute("ActionMessages",actionMessages);
          }else{
            /* unable to obtain statement object */
            ActionErrors actionErrors = new ActionErrors();
            ActionError actionError = new ActionError("errors.add.watch");
            actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
            httpSession.setAttribute("ActionErrors",actionErrors);
          }
          parseUtil = null;
          sysaduser = null;
          sysadpwd = null;
          domain = null;
          serviceConfiguration = null;
          ifsSchemaPassword = null;
          ifsService = null;
        }catch (SQLException sqlEx) {
          logger.error("An Exception occurred in FolderDocAddWatch... ");
          logger.error(sqlEx.toString());
          ActionErrors actionErrors = new ActionErrors();
          ActionError actionError = new ActionError("errors.add.watch");
          actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
          httpSession.setAttribute("ActionErrors",actionErrors);
          sqlEx.printStackTrace();
        }finally{
          /* close statement and connection objects */
          connBean.closeStatement();
          connBean.closeConnection();          
        }
      }else{ 
        // cannot add watch when user has no email-id in his profile
        ActionErrors actionErrors = new ActionErrors();
        ActionError actionError = new ActionError("errors.add.watch.no.emailid");
        actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
        httpSession.setAttribute("ActionErrors",actionErrors);
      }
    }else if( watchExists ){
      // cannot add watch when user has already applied watch for a document
      ActionErrors actionErrors = new ActionErrors();
      ActionError actionError = new ActionError("errors.watch.already.exists",docName);
      actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
      httpSession.setAttribute("ActionErrors",actionErrors);
    }else{
      
    }
    }else{
      logger.error("No items selected");
    }
  }catch( DbsException dbsEx ){
    logger.error("An Exception occurred in FolderDocAddWatch... ");
    logger.error(dbsEx.toString());
    dbsEx.printStackTrace();
  }
  logger.info("Exiting FolderDocAddWatch now...");
  return mapping.findForward(target);
}
}