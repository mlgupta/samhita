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
 * $Id: FolderDocListAction.java,v 20040220.59 2006/05/30 13:15:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsSearch;
import dms.web.actionforms.filesystem.AdvanceSearchForm;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.AdvanceSearchBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
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
 *	Purpose: To display a list of folder(s) and document(s) for a given path or
 *           search criteria
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocListAction extends Action  {
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
    logger.info("Entering FolderDocListAction now...");
    int itemCount = 0;
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "successForTree";
    UserPreferences userPreferences = null;
    FolderDocInfo folderDocInfo = null;
    FolderDoc folderDoc;
    ArrayList folderDocLists = new ArrayList();
    ActionMessages actionMessages = null;
    String davPath=null;
    HttpSession httpSession = null;
    try{
      //Initializing variables
      httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("Building Folder Doc List...");
      logger.debug("folderDocInfo : " + folderDocInfo);
      logger.debug("userPreferences : " + userPreferences);
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      FolderDocListForm folderdoclistform=(FolderDocListForm)form;
      davPath = userInfo.getDavPath();
      logger.debug("davPath : " + davPath);
      logger.debug("FolderDocListForm: "+folderdoclistform);
      
      //If its simple listing then only update currentFolderPath and add 
      //cuttentFolderId in the navigation history
      if(!folderDocInfo.isTreeVisible()){
        Object[] formats = dbsLibrarySession.getFormatCollection().getItems();
        request.setAttribute("formats",formats);
       // logger.info(formats);
      }

      if( request.getParameter("reloadTree")!= null && 
          request.getParameter("reloadTree").trim().length()!=0 &&
          request.getParameter("reloadTree").equalsIgnoreCase("false") ){
        folderDocInfo.setNoReloadTree(true);
      }
      
      if((folderDocInfo.getListingType() == FolderDocInfo.SIMPLE_LISTING)){
        Long currentFolderId = folderDocInfo.getCurrentFolderId();
        logger.debug("CurrentFolderId : " + currentFolderId);
        DbsFolder dbsFolder = null;
        try{
          dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                   currentFolderId);
        }catch(DbsException dex){
          try{
            dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                            folderDocInfo.getHomeFolderId());
            folderDocInfo.setCurrentFolderId(dbsFolder.getId());
          }catch(DbsException dex1){
            ExceptionBean exb = new ExceptionBean();
            exb.setMessage("Home Folder Not Set");
            exb.setMessageKey("errors.homefolder.notset");
            throw exb;
          }
        }
        String currentFolderPath = dbsFolder.getAnyFolderPath();
        logger.debug("currentFolderPath : " + currentFolderPath);
        logger.info("Listing items present in the folder : " + currentFolderPath);
        folderDocInfo.setCurrentFolderPath(currentFolderPath);
//                folderDocInfo.setDavPath(userInfo.getDavPath());
        folderDoc = new FolderDoc(dbsLibrarySession);
        folderDocLists = folderDoc.getFolderDocList(folderDocInfo.getCurrentFolderId(),folderDocInfo,userPreferences, davPath);
        /* uncomment this if displayListAction is not being used */
        request.setAttribute("folderDocLists", folderDocLists);
        /* uncomment this if displayListAction is being used */
        //httpSession.setAttribute("folderDocLists", folderDocLists);
        if(httpSession.getAttribute("OverWrite") != null){
          request.setAttribute("OverWrite",(Integer)httpSession.getAttribute("OverWrite"));
          httpSession.removeAttribute("OverWrite");
        }
        if(userPreferences.getNavigationType() == UserPreferences.FLAT_NAVIGATION){
          forward = new String("successForFlat");
          /*ArrayList listOfParents = folderDocInfo.getListOfParents();
          ArrayList listOfParentsId = folderDocInfo.getListOfParentsId();
          listOfParents.clear();
          listOfParentsId.clear();

          String currentFolderNameTemp = dbsFolder.getName();
          String currentFolderIdTemp = dbsFolder.getId().toString();
          listOfParents.add(0,currentFolderNameTemp);
          listOfParentsId.add(0,currentFolderIdTemp);
          
          while(!dbsFolder.getAnyFolderPath().equals("/")){
            dbsFolder = dbsFolder.getFolderReferences(0);
            currentFolderNameTemp = dbsFolder.getName();
            currentFolderIdTemp = dbsFolder.getId().toString();
            listOfParents.add(0,currentFolderNameTemp);
            listOfParentsId.add(0,currentFolderIdTemp);
          }*/
        }
        /* added for appropriate message display in message bar for both nav types */
        if(userPreferences.getNavigationType() == UserPreferences.FLAT_NAVIGATION){
          dbsFolder=(DbsFolder)dbsLibrarySession.getPublicObject(folderDocInfo.getCurrentFolderId());
          actionMessages = new ActionMessages();
          if(dbsFolder.getItemCount()<=1){
            ActionMessage actionMessage = new ActionMessage("msg.folderdoc.folder_content_item",dbsFolder.getName(), String.valueOf(dbsFolder.getItemCount()));
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          }else{
            ActionMessage actionMessage = new ActionMessage("msg.folderdoc.folder_content_items",dbsFolder.getName(), String.valueOf(dbsFolder.getItemCount()));
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);        
          }
          //httpSession.setAttribute("ActionMessages" , actionMessages);
        }else{
          actionMessages = new ActionMessages();
          if(dbsFolder.getItemCount()<=1){
            ActionMessage actionMessage = new ActionMessage("msg.folderdoc.folder_content_item",dbsFolder.getName(), String.valueOf(dbsFolder.getItemCount()));
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
          }else{
            ActionMessage actionMessage = new ActionMessage("msg.folderdoc.folder_content_items",dbsFolder.getName(), String.valueOf(dbsFolder.getItemCount()));
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);        
          }
        }
      }else if(folderDocInfo.getListingType() == FolderDocInfo.SEARCH_LISTING){
        folderDocInfo.setNoReloadTree(true);
        AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm)httpSession.getAttribute("advanceSearchForm");
        advanceSearchForm.setCurrentFolderId(folderDocInfo.getCurrentFolderId());
        AdvanceSearchBean advanceSearchBean = new AdvanceSearchBean(dbsLibrarySession);
        DbsSearch dbsSearch = advanceSearchBean.getSearchObject(advanceSearchForm);
        itemCount = dbsSearch.getItemCount();
        folderDoc = new FolderDoc(dbsLibrarySession);
        folderDocLists = folderDoc.buildFolderDocList(dbsSearch,FolderDocInfo.SEARCH_LISTING,folderDocInfo,userPreferences,davPath);
        /* uncomment this if displayListAction is not being used */
        request.setAttribute("folderDocLists", folderDocLists);
        /* uncomment this if displayListAction is being used */
        //httpSession.setAttribute("folderDocLists", folderDocLists);
        actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.folderdoc.number_of_item_found", String.valueOf(itemCount));
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages" , actionMessages);
      }else{
          //page listing
      }
      if(userPreferences.getNavigationType() == UserPreferences.FLAT_NAVIGATION){
        forward = new String("successForFlat");
      }else if(userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION){
        forward = new String("successForTree");
      }

      ActionErrors actionErrors = (ActionErrors)httpSession.getAttribute("ActionErrors");
      if(actionErrors != null){
        logger.debug("Saving action error in request stream");
        if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
          saveErrors(request,actionErrors);
          httpSession.removeAttribute("ActionErrors");
        }
      }else{  
        //if it is simple navigation then display itemcount in the folder
        if(httpSession.getAttribute("IS_SIMPLE_NAVIGATION") != null){
          httpSession.removeAttribute("IS_SIMPLE_NAVIGATION");
          if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
            saveMessages(request,actionMessages);
          }else{
            httpSession.setAttribute("ActionMessages" , actionMessages);
          }
        }else{
          ActionMessages actionMessagesSession = (ActionMessages)httpSession.getAttribute("ActionMessages");
          if(actionMessagesSession != null){
            if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
              logger.debug("Saving action message in request stream");
              saveMessages(request,actionMessagesSession);
              httpSession.removeAttribute("ActionMessages");
            }else{
              httpSession.setAttribute("ActionMessages" , actionMessagesSession);
            }
          }else{
            //this is to handle the refresh case
            if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
              saveMessages(request,actionMessages);
            }else{
              httpSession.setAttribute("ActionMessages" , actionMessages);
            }
          }
        }
      }
      logger.debug("Building Folder Doc List Complete");
    }catch( ParseException pex){
      exceptionBean = new ExceptionBean(pex);
      logger.error("An Exception occurred in FolderDocListAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        saveErrors(request,exceptionBean.getActionErrors());
      }else{
        httpSession.setAttribute("ActionErrors" , exceptionBean.getActionErrors());
      }
      forward = "failure";
    }catch(ExceptionBean eb){
      logger.error("An Exception occurred in FolderDocListAction... ");
      logger.error(eb.getErrorTrace());
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        saveErrors(request,eb.getActionErrors());
      }else{
        httpSession.setAttribute("ActionErrors" , eb.getActionErrors());
      }
      forward = "failure";
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocListAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if(dex.getErrorCode() == 10201){
        exceptionBean.setMessageKey("errors.10201.folderdoc.folder.notexist");
      }
      if(dex.getErrorCode() == 21030){
        exceptionBean.setMessageKey("errors.21030.db.oper.timedout");
      }
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        saveErrors(request,exceptionBean.getActionErrors());
      }else{
        httpSession.setAttribute("ActionErrors" , exceptionBean.getActionErrors());
      }
      forward = "failure";
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocListAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if( userPreferences.getNavigationType() == UserPreferences.TREE_NAVIGATION ){
        saveErrors(request,exceptionBean.getActionErrors());
      }else{
        httpSession.setAttribute("ActionErrors" , exceptionBean.getActionErrors());
      }
      forward = "failure";
    }
    logger.info("Exiting FolderDocListAction now...");
    return mapping.findForward(forward);
  }
} 
