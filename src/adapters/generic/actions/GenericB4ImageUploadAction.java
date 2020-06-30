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
 * $Id: GenericB4ImageUploadAction.java,v 1.0 2006/06/19 12:52:38 suved Exp $
 *****************************************************************************
 */
package adapters.generic.actions;
/* adapters package references */
import adapters.beans.LoginBean;
import adapters.beans.XMLBean;
import adapters.generic.actionforms.GenericReplaceVoucherForm;
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
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.theme.Theme;
import dms.web.beans.theme.Theme.StyleTagPlaceHolder;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/* Servlet API */
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
 /**
 * Purpose            : Action called to present erp_replace_voucher.jsp to
 *                      replace a document image.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-06-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class GenericB4ImageUploadAction extends Action  {
/* this is the date format specified for adapters */
public static String DATE_FORMAT = "dd-MMM-yyyy";
/**
 * This is the main action called from the Struts framework.
 * @param mapping The ActionMapping used to select this instance.
 * @param form The optional ActionForm bean for this request.
 * @param request The HTTP Request we are processing.
 * @param response The HTTP Response we are processing.
 */

public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
  DateHelperForFileSystem dateHelper = new DateHelperForFileSystem();
  //Variable declaration
  ExceptionBean exceptionBean;                  // bean to handle exceptions 
  String forward = "success";                   // mapping for ActionForward
  DbsLibraryService dbsLibraryService;          // represents Library Service
  DbsCleartextCredential dbsCleartextCredential;// represents user Credentials
  DbsLibrarySession dbsLibrarySession = null;   // represents Library Session
  Theme.StyleTagPlaceHolder styleTagPlaceHolder=null; // represents user theme
  ServletContext context;                       // represents servlet context 
  HttpSession httpSession;                      // represents http session 
  String ifsService;                            // a parameter to start service
  String ifsSchemaPassword;                     // a parameter to start service
  String serviceConfiguration;                  // a parameter to start service
  String domain;                                // a parameter to start service
  String davpath=null;                          // represents webdav path
  String themeName=null;                        // theme name for the user
  ActionErrors actionErrors = null;             // hold a collection of error objs
  ActionError actionError;                      // individual error objects
  String xmlString= null;                       // xmlString as response  
  String voucherId=null;                        // voucher id used for renaming
  try{
    logger.info("Entering GenericB4ImageUploadAction now...");
    actionErrors = new ActionErrors();
    /* obtain servlet context to fetch context path */
    context = getServlet().getServletContext();
    /* relative path for "GeneralActionParam.xml" */
    String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+
                         File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
    /* obtain necessary request attributes */
    String manageImageUser =null;
    manageImageUser=request.getParameter("userId");
    String manageImageUserPwd = null;
    manageImageUserPwd=request.getParameter("password");
    
    String documentId =null;
    String key = null;
    documentId=request.getParameter("docId");
    
    /* kill an already existing httpSession for the credentials provided */
    if(context.getAttribute(manageImageUser.toUpperCase())==null){
      httpSession = request.getSession(true);
      logger.debug("httpSession id: "+httpSession.getId());
      logger.debug("context Attribute set to null initially...");
    }else{
      HttpSession oldHttpSession=(HttpSession)context.getAttribute(
                                              manageImageUser.toUpperCase());
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
    /* Obtain a dbsLibrarySession for the given user, using LoginBean */
    LoginBean loginBean = new LoginBean(manageImageUser,manageImageUserPwd,
                                        relativePath);    
    dbsLibrarySession = loginBean.getUserSession();
    if(dbsLibrarySession.isConnected()){
      logger.debug("Session Id: "+dbsLibrarySession.getId());
      /* initialize userInfo, userPreferences,folderDocInfo */
      UserInfo userInfo = new UserInfo();
      FolderDocInfo folderDocInfo = new FolderDocInfo();
      UserPreferences userPreferences = new UserPreferences();
      DbsDirectoryUser connectedUser=dbsLibrarySession.getUser();
      context.setAttribute(connectedUser.getName(),httpSession); 
      //  for selected tree access level,displaying number of rows per page,
      //  Navigation type,Theme selected and Open Doc Option selected.
      if (dbsLibrarySession.getUser().getPropertyBundle()!=null){
        DbsPropertyBundle propertyBundleToEdit= 
                          dbsLibrarySession.getUser().getPropertyBundle();         
        DbsProperty property=propertyBundleToEdit.getProperty(
                                                  "PermittedTreeAccessLevel");                    
        if (property!=null){
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setTreeLevel(attrValue.getInteger(dbsLibrarySession)); 
        }

        property=propertyBundleToEdit.getProperty("ItemsToBeDisplayedPerPage");                    
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setRecordsPerPage(attrValue.getInteger(
                                                      dbsLibrarySession)); 
        }

        property=propertyBundleToEdit.getProperty("NavigationType");                    
        if (property!=null) {
          DbsAttributeValue attrValue=property.getValue();
          userPreferences.setNavigationType(attrValue.getInteger(
                                                      dbsLibrarySession)); 
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
          userPreferences.setChkOpenDocInNewWin(attrValue.getInteger(
                                                          dbsLibrarySession));     
        }
      }
      /* fetch theme name to initialize appropriate css theme files for the user */
      themeName=(themeName==null)?"Default":themeName;
      styleTagPlaceHolder= new StyleTagPlaceHolder(dbsLibrarySession,themeName);
      /* set style place holder */
      userPreferences.setStylePlaceHolder(styleTagPlaceHolder.getStylePlaceHolder());
      /* set the path for the icons to be used while displaying user folder tree */
      userPreferences.setTreeIconPath(styleTagPlaceHolder.getTreeIconPath());
      /* set userId as directory user name */
      userInfo.setUserID(connectedUser.getName());
      userInfo.setDbsLibrarySession(dbsLibrarySession);
      //set logger to be used in the application
      userInfo.setLogger(logger);
      //this locale should be obtained from DirectoryUser class
      //for the time being we will use default locale used by the system.
      DbsPrimaryUserProfile connectedUserProf=connectedUser.getPrimaryUserProfile();
      /* set locale for user information */
      userInfo.setLocale(connectedUserProf.getLocale());
      /* set language for user information */
      userInfo.setLanguage(connectedUserProf.getLanguage());
      /* set timezone for user information */
      userInfo.setTimeZone(connectedUserProf.getTimeZone());
      /* set character set for user information */
      userInfo.setCharSet(connectedUserProf.getCharacterSet());
      /* set web dav path for user information */
      userInfo.setDavPath(davpath);
      logger.debug("davpath: "  + davpath);
      logger.debug("Name : "+connectedUser.getDistinguishedName());
      /* store userInfo, userPreferences,folderDocInfo,treeview,userPassword
       * in httpSession */
      httpSession.setAttribute("UserInfo",userInfo );
      httpSession.setAttribute("UserPreferences",userPreferences );
      httpSession.setAttribute("FolderDocInfo",folderDocInfo);
      httpSession.setAttribute("Treeview",null);   
      //httpSession.setAttribute("userPassword",manageImageUserPwd);
      /* set user status in user information */
      if (dbsLibrarySession.getUser().isSystemAdminEnabled()){
        userInfo.setSystemAdmin(true);
      }else{
        if(dbsLibrarySession.getUser().isAdminEnabled()){
          userInfo.setAdmin(true);
        }else{
        }
      }
      forward = "success";
      //check and set home folder id if it exist
      DbsFolder dbsFolder = connectedUserProf.getHomeFolder();
      if(dbsFolder == null){
        folderDocInfo.setHomeFolderId(null);
      }else{
        Long homeFolderId = dbsFolder.getId();
        logger.debug("homeFolderId : " + homeFolderId);
        folderDocInfo.setHomeFolderId(homeFolderId);
        logger.debug("folderDocInfo : " + folderDocInfo);
        DbsFolder parentFolder = dbsLibrarySession.getPublicObject(
                                  new Long(documentId)).getFolderReferences(0);
        folderDocInfo.setCurrentFolderId(parentFolder.getId());
        folderDocInfo.setCurrentFolderPath(parentFolder.getAnyFolderPath());
        /* if image is to be replaced, then submit to upload jsp */
        GenericReplaceVoucherForm genericReplaceVoucherForm = new GenericReplaceVoucherForm();
        genericReplaceVoucherForm.setVoucherId(voucherId);
        genericReplaceVoucherForm.setTxtDocumentId(documentId);
        request.setAttribute("genericReplaceVoucherForm",genericReplaceVoucherForm);
        logger.info("Serving JSP for Replacing Document Complete");
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.SelectFileToUpload");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        saveMessages(request,actionMessages);
      }
      logger.debug("userInfo : " + userInfo);
      logger.debug("userPreferences : " + userPreferences);
    }else{
      actionError = new ActionError("login.invalid_user");
      actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
      saveErrors(request,actionErrors);
      logger.error("Invalid User Credentials");
      throw new Exception("Invalid User Credentials");
    }
  }catch(DbsException dex)  {
    /* in case of exception send xml response as 'ManageImageFailed' */
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root RC = '3' Response = 'MANAGEDIMAGEFAILURE'");         
    sb.append(" />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
    forward = new String("failure");
    if(dex.containsErrorCode(10170)){
      ActionErrors errors=new ActionErrors();
      ActionError loginError=new ActionError("msg.login.incorrect");
      errors.add(ActionErrors.GLOBAL_ERROR,loginError);
      saveErrors(request,errors);
      dex.printStackTrace();                
    }else{
      exceptionBean = new ExceptionBean(dex);
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
      dex.printStackTrace();                
    }
  }catch(Exception ex)  {
    /* in case of exception send xml response as 'ManageImageFailed' */
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root RC = '4' Response = 'MANAGEDIMAGEFAILURE'");         
    sb.append(" />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
    forward = new String("failure");
    exceptionBean = new ExceptionBean(ex);
    logger.error(exceptionBean.getMessage());
    logger.debug(exceptionBean.getErrorTrace());
    saveErrors(request,exceptionBean.getActionErrors());
    ex.printStackTrace();
  }

  logger.info("Exiting GenericB4ImageUploadAction now...");
  return mapping.findForward(forward);
}
}
