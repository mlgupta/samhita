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
 * $Id: LoginAction.java,v 20040220.60 2006/07/21 08:29:13 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.loginout;
                                                           
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.web.actionforms.loginout.LoginForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.theme.Theme;
import dms.web.beans.theme.Theme.StyleTagPlaceHolder;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.wf.docApprove.UpdateUsersPB;
/* java API */
import java.io.IOException;
import java.io.*;
import java.util.Date;
import javax.servlet.ServletContext;
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
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To validate user credentials and allow login .
 *  @author             Mishra Maneesh 
 *  @version            1.0
 * 	Date of creation:   07-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class LoginAction extends Action  {
private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
/**
 * This is the main action called from the Struts framework.
 * @param mapping The ActionMapping used to select this instance.
 * @param form The optional ActionForm bean for this request.
 * @param request The HTTP Request we are processing.
 * @param response The HTTP Response we are processing.
 */
public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
  logger.info("Logging User ...");
  DateHelperForFileSystem dateHelper = new DateHelperForFileSystem();

  //Variable declaration
  ExceptionBean exceptionBean;        
  String forward = "success";
  DbsLibraryService dbsLibraryService;
  DbsCleartextCredential dbsCleartextCredential;
//        DbsCleartextCredential adminCredential;
  DbsLibrarySession dbsLibrarySession = null;
//        DbsLibrarySession dbsAdminLibrarySession = null;
  Theme.StyleTagPlaceHolder styleTagPlaceHolder=null;
  ServletContext context;
  HttpSession httpSession;
  LoginForm loginForm;
  String ifsService;
  String ifsSchemaPassword;
  String serviceConfiguration;
  String domain;
  String davpath=null;
  String themeName=null;
  ActionErrors actionErrors = new ActionErrors();
  ActionError actionError;
  Treeview treeview = null;
  try{
    loginForm = (LoginForm)form;
    context = getServlet().getServletContext();
    loginForm.setUserID(loginForm.getUserID().toUpperCase());
    logger.info("User ID : " + loginForm.getUserID()); 
    //logger.debug("contextPath: "+(String)context.getAttribute("contextPath"));
    String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+
                         File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
    //request.getSession().invalidate();            
    //logger.debug("request.getSession().isNew() : " + request.getSession().isNew());
    //logger.debug("request.getSession().getId() : " + request.getSession().getId());

    if(context.getAttribute(loginForm.getUserID())==null){
      httpSession = request.getSession(true);
      logger.debug("httpSession id: "+httpSession.getId());
      logger.debug("context Attribute set to null initially...");
    }else{
      HttpSession oldHttpSession=(HttpSession)context.getAttribute(loginForm.getUserID());
       if (oldHttpSession!=null){
         logger.debug("OldSessionExists.");
       }
       if(oldHttpSession.getId()==null){
          logger.debug("oldHttpSession id is null.");
       }else{
          logger.debug("oldHttpSession id is not null.");
       }
      if (oldHttpSession!=null || oldHttpSession.getId()!=null){
        logger.debug("oldHttpSession id: "+oldHttpSession.getId());
        oldHttpSession.invalidate();
      }
      httpSession = request.getSession(true);
      
      logger.debug("User logged in already...");
      //user is already logged in
    }
    
    dbsCleartextCredential = new DbsCleartextCredential(loginForm.getUserID(),loginForm.getUserPassword());
//            String systemUserId = context.getInitParameter("SystemUserId");
//            String systemUserPassword = context.getInitParameter("SystemUserPassword");
//            adminCredential = new DbsCleartextCredential(systemUserId,systemUserPassword);

    ifsService = parseUtil.getValue("IfsService","Configuration");  
    //ifsService = context.getInitParameter("IfsService");
    davpath = parseUtil.getValue("DavPath","Configuration");
    logger.debug("DavPath : " + davpath);
    if(DbsLibraryService.isServiceStarted(ifsService)){
      logger.info(ifsService + " is running");
      dbsLibraryService = DbsLibraryService.findService("IfsDefaultService");
    }else{
      logger.info("Starting Library Service...");            
      ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
      serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
      domain = parseUtil.getValue("Domain","Configuration");
      
      
      /*ifsSchemaPassword = context.getInitParameter("IfsSchemaPassword");
      serviceConfiguration = context.getInitParameter("ServiceConfiguration");
      domain = context.getInitParameter("Domain");*/
      //logger.info(""+);
      
      logger.debug("IfsService : " + ifsService);
      logger.debug("ServiceConfiguration : " + serviceConfiguration);
      logger.debug("Domain : " + domain);
      dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
      logger.info("Library Service Started ");
    }
    try{
      dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
//    dbsAdminLibrarySession = dbsLibraryService.connect(adminCredential,null);
    }catch(DbsException dex){
      logger.error("An Exception occurred in LoginAction... ");
      logger.error(dex.toString());
      dex.printStackTrace();                
      if(dex.containsErrorCode(10170)){
        throw dex;
      }else if(dex.getErrorCode() == 21008){
        logger.error("This exception is thrown when library service started successfully");
        logger.error("and then database went down");
        logger.error("In this case Library Service need to be restarted");
        ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
        serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
        domain = parseUtil.getValue("Domain","Configuration");
        /*ifsSchemaPassword = context.getInitParameter("IfsSchemaPassword");
        serviceConfiguration = context.getInitParameter("ServiceConfiguration");
        domain = context.getInitParameter("Domain");*/
        logger.error("Disposing Library Service...");
        dbsLibraryService.dispose(ifsSchemaPassword);
        logger.error("Disposing Library Service Complete");
        logger.info("ReStarting Library Service...");            
        dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
        logger.info("ReStarting Library Service Complete");
        dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
//      dbsAdminLibrarySession = dbsLibraryService.connect(adminCredential,null);
      }
    }
    
    if(dbsLibrarySession.isConnected()){
      logger.debug("Session Id: "+dbsLibrarySession.getId());
      UserInfo userInfo = new UserInfo();
      FolderDocInfo folderDocInfo = new FolderDocInfo();
      UserPreferences userPreferences = new UserPreferences();
      DbsDirectoryUser connectedUser=dbsLibrarySession.getUser();
      context.setAttribute(connectedUser.getName(),httpSession); 
      //  for selected tree access level,displaying number of rows per page,
      //  Navigation type,Theme selected and Open Doc Option selected.
      if(dbsLibrarySession.getUser().getPropertyBundle()!=null){
        DbsPropertyBundle propertyBundleToEdit= dbsLibrarySession.getUser().getPropertyBundle();         
        DbsProperty property=propertyBundleToEdit.getProperty("PermittedTreeAccessLevel");                    
        if (property!=null){
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setTreeLevel(attrValue.getInteger(dbsLibrarySession)); 
        }
        property=propertyBundleToEdit.getProperty("ItemsToBeDisplayedPerPage");                    
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setRecordsPerPage(attrValue.getInteger(dbsLibrarySession)); 
        }

        property=propertyBundleToEdit.getProperty("NavigationType");                    
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setNavigationType(attrValue.getInteger(dbsLibrarySession)); 
        }
        property=propertyBundleToEdit.getProperty("Theme");                    
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          themeName=attrValue.getString(dbsLibrarySession);
        }
        /* code added on account of open doc option */
        property=propertyBundleToEdit.getProperty("OpenDocOption");                  
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setChkOpenDocInNewWin(attrValue.getInteger(dbsLibrarySession));     
        }
      }

      themeName=(themeName==null)?"Default":themeName;
      styleTagPlaceHolder= new StyleTagPlaceHolder(dbsLibrarySession,themeName);
      userPreferences.setStylePlaceHolder(styleTagPlaceHolder.getStylePlaceHolder());
      userPreferences.setTreeIconPath(styleTagPlaceHolder.getTreeIconPath());
      userInfo.setUserID(connectedUser.getName());
//                userInfo.setAdminLibrarySession(dbsAdminLibrarySession);
      userInfo.setDbsLibrarySession(dbsLibrarySession);
      //set logger to be used in the application
      userInfo.setLogger(logger);
      //this locale should be obtained from DirectoryUser class
      //for the time being we will use default locale used by the system.
      DbsPrimaryUserProfile connectedUserProf=connectedUser.getPrimaryUserProfile();
      userInfo.setLocale(connectedUserProf.getLocale());
      userInfo.setLanguage(connectedUserProf.getLanguage());
      userInfo.setTimeZone(connectedUserProf.getTimeZone());
      userInfo.setCharSet(connectedUserProf.getCharacterSet());
      userInfo.setDavPath(davpath);
      logger.debug("davpath: "  + davpath);
      logger.debug("Name : "+connectedUser.getDistinguishedName());
      treeview = new Treeview(dbsLibrarySession,"FolderDocumentList",
                              userPreferences.getTreeLevel(),
                              getServlet().getServletContext().getRealPath("/"),
                              userPreferences.getTreeIconPath() + "/",
                              httpSession.getId(),true);      
      folderDocInfo.setJsFileName(treeview.getJsFileName());
      Long currentFolderId = connectedUserProf.getHomeFolder().getId();
      treeview.forAddressBar(currentFolderId);
      logger.debug("currentFolderId : " + currentFolderId);
      logger.info("Building Folder Tree Complete");
          
      //initialize folderDocInfo for nevigation
      folderDocInfo.initializeNevigation();

      Long homeFolderId = currentFolderId;
      logger.debug("homeFolderId : " + homeFolderId);
      folderDocInfo.setHomeFolderId(homeFolderId);
      folderDocInfo.setCurrentFolderId(currentFolderId);
      folderDocInfo.setDbsLibrarySession(dbsLibrarySession);
      folderDocInfo.setPageNumber(1);
      folderDocInfo.addFolderDocId(folderDocInfo.getCurrentFolderId());
      folderDocInfo.setHierarchySetNo(1);
      httpSession.setAttribute("Treeview",treeview);
      httpSession.setAttribute("UserInfo",userInfo );
      httpSession.setAttribute("UserPreferences",userPreferences );
      httpSession.setAttribute("FolderDocInfo",folderDocInfo);
      httpSession.setAttribute("password",loginForm.getUserPassword());
      if (dbsLibrarySession.getUser().isSystemAdminEnabled()){
        userInfo.setSystemAdmin(true);
        dbsLibrarySession.setAdministrationMode(true);
//        forward = "success_admin";
      }else{
        if(dbsLibrarySession.getUser().isAdminEnabled()){
          dbsLibrarySession.setAdministrationMode(true);
          userInfo.setAdmin(true);
//          forward = "success_admin";
        }else{
//          forward = "success_nonadmin";
        }
      }
      forward = "success";
  
      logger.debug("userInfo : " + userInfo);
      logger.debug("userPreferences : " + userPreferences);
      Date loginDate = new Date();
      String userLoginDate = dateHelper.format(loginDate,DATE_FORMAT);
      logger.info(userInfo.getUserID() + " has logged in at "+userLoginDate);                
    }else{
      actionError = new ActionError("login.invalid_user");
      actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
      saveErrors(request,actionErrors);
      logger.error("Invalid User");
      forward = "failure";
    }
  }catch(DbsException dex)  {
    logger.error("An Exception occurred in LoginAction... ");
    logger.error(dex.toString());
    if(dex.containsErrorCode(10170)){
      ActionErrors errors=new ActionErrors();
      ActionError loginError=new ActionError("msg.login.incorrect");
      errors.add(ActionErrors.GLOBAL_ERROR,loginError);
      saveErrors(request,errors);
      forward = "failure";
      dex.printStackTrace();                
    }else{
      exceptionBean = new ExceptionBean(dex);
      //logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
      forward = "failure";
      dex.printStackTrace();                
    }
  }catch(Exception ex)  {
    exceptionBean = new ExceptionBean(ex);
    logger.error("An Exception occurred in LoginAction... ");
    logger.error(exceptionBean.getErrorTrace());
    saveErrors(request,exceptionBean.getActionErrors());
    forward = "failure";
    ex.printStackTrace();
  }
  logger.info("Logging User Complete");
  return mapping.findForward(forward);
}
}