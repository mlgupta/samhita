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
 * $Id: FolderDocPropertyAction.java,v 20040220.16 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocPropertyForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DocEventLogBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.SearchUtil;
import dms.web.beans.wf.watch.InitiateWatch;
/* Java API */
import java.io.File;
import java.io.IOException;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To view property of selected Public Objects
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   23-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  26-12-2005  
 */
public class FolderDocPropertyAction extends Action  {
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
  logger.info("Entering FolderDocPropertyAction now...");
  //variable declaration
  ExceptionBean exceptionBean;
  String forward = "success";
  HttpSession httpSession = null;
  boolean folderRenamed = false;
  DbsPublicObject dbsPO = null;
  try{
    httpSession = request.getSession(false);
    FolderDocPropertyForm folderDocPropertyForm = (FolderDocPropertyForm)form;
    logger.debug("Modifying Folder or Doc property like its name and its ACL");
    logger.debug("folderDocPropertyForm : " + folderDocPropertyForm);
    UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
    FolderDocInfo folderDocInfo = (FolderDocInfo)
                                  httpSession.getAttribute("FolderDocInfo");            
    UserPreferences userPreferences = (UserPreferences)
                                  httpSession.getAttribute("UserPreferences");
    Treeview treeview = (Treeview)httpSession.getAttribute("Treeview");
    
    DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
    Long currentFolderId = folderDocInfo.getCurrentFolderId();
    logger.debug("currentFolderId : " + currentFolderId);
    
    Long[] selectedFolderDocIds = folderDocPropertyForm.getHdnFolderDocIds();
    logger.debug("selectedFolderDocIds.length : " + selectedFolderDocIds.length);
    String selectedFolderDocName = folderDocPropertyForm.getTxtFolderDocName();
    logger.debug("selectedFolderDocName : " + selectedFolderDocName);
    String selectedFolderDocOldName = dbsLibrarySession.getPublicObject(
                                      selectedFolderDocIds[0]).getName();
    logger.debug("selectedFolderDocOldName : " + selectedFolderDocOldName);

    String relativePath =httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    
    dbsPO = dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
    if(!selectedFolderDocOldName.equals(selectedFolderDocName)){
      logger.info("Renaming Folders And Document...");
      FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
      folderDoc.renameFolderDoc(selectedFolderDocIds[0],selectedFolderDocName,
                                treeview,relativePath,
                                dbsLibrarySession.getUser().getDistinguishedName());
      logger.info("Renaming Complete");
      /* if a folder has been renamed , reload the tree , else donot reload */
      if( dbsPO.getResolvedPublicObject() instanceof DbsDocument ){
        folderDocInfo.setNoReloadTree(true);
      }else if( dbsPO.getResolvedPublicObject() instanceof DbsFolder ){
        folderRenamed = true;
        folderDocInfo.setNoReloadTree(false);
      }
      
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.ItemsRenamed");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
    }
    logger.debug("folderRenamed : "+folderRenamed);
    String userName = dbsLibrarySession.getUser().getDistinguishedName();
    Boolean aclError = folderDocPropertyForm.isAclError();
    if(!aclError.booleanValue()){
      String aclName = folderDocPropertyForm.getTxtAclName();
      logger.info("aclName : " + aclName);
      DbsAccessControlList acl = dbsPO.getAcl();
      String aclOldName;
      if(acl != null){
        aclOldName = dbsPO.getAcl().getName();
      }else{
        aclOldName = "";
      }
      logger.debug("aclOldName : " + aclOldName);            
      if(!aclOldName.equals(aclName)){
        logger.info("Applying Acl...");                
        SearchUtil searchUtil = new SearchUtil();
        DbsAccessControlList dbsAccessControlList =
                             (DbsAccessControlList)searchUtil.findObject(
                             dbsLibrarySession,DbsAccessControlList.CLASS_NAME,
                             aclName);
        dbsPO.setAcl(dbsAccessControlList);
        if(dbsPO instanceof DbsDocument){
          folderDocInfo.setNoReloadTree(true);
          // code for wf submission goes here
          if( relativePath!=null && userName!=null ){
            String actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPO.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : ACL changed from "+aclOldName+" to "+dbsAccessControlList.getName();
            InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,
                                        actionForWatch,dbsPO.getId());
            iniWatch.startWatchProcess();
            actionForWatch = null;
          }
        
          String action = "ACL Changed: "+aclName;
          DocEventLogBean logBean = new DocEventLogBean();
          logBean.logEvent(dbsLibrarySession,dbsPO.getId(),action);
        }else if( dbsPO instanceof DbsFolder && !folderRenamed ){
          /* this is to handle the case when the acl has and name has not been   
           * changed for a folder */
          folderDocInfo.setNoReloadTree(true);
        }
        logger.info("Applying Acl Complete");
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.AclAppliedSuccessfully");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }            
    }
    logger.debug("Modifying Folder or Doc property like its name and its ACL complete");
    relativePath = null;
  }catch(DbsException dex){
    exceptionBean = new ExceptionBean(dex);
    logger.error("An Exception occurred in FolderDocPropertyAction... ");
    logger.error(exceptionBean.getErrorTrace());
    if(dex.getErrorCode() == 30035){
      exceptionBean.setMessageKey("errors.30035.insufficient.access.to.change.POAcl");
    }
    if(dex.getErrorCode() == 30041){
      exceptionBean.setMessageKey("errors.30041.folderdoc.insufficient.access");
    }
    if(dex.containsErrorCode(30659)){
      exceptionBean.setMessageKey("errors.30659.PO.has.userlock.unable.to.change");
    }
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }catch(Exception ex){
    exceptionBean = new ExceptionBean(ex);
    logger.error("An Exception occurred in FolderDocPropertyAction... ");
    logger.error(exceptionBean.getErrorTrace());
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }
  logger.info("Exiting FolderDocPropertyAction now...");
  return mapping.findForward(forward);
}
}
