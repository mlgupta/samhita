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
 * $Id: FolderFlatListAction.java,v 1.0 2006/04/18 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
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
/**
 *	Purpose           : To display a hierarchy of folder(s) for a given path in 
 *                      flat_folder_list.jsp
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 18-04-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class FolderFlatListAction extends Action  {
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
      String target = "success";
      FolderDocInfo folderDocInfo = null;
      HttpSession httpSession = null; 
      DbsLibrarySession dbsLibrarySession = null;
      UserInfo userInfo = null;
      int setNo =0;
      ExceptionBean exceptionBean = null;
      try{
        logger.debug("Entering FolderFlatListAction... ");
        httpSession = (HttpSession)request.getSession(false);
        userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
        folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        Long currentFolderId = folderDocInfo.getCurrentFolderId();
        DbsFolder dbsFolder = null;
        logger.debug("folderDocInfo : "+folderDocInfo);
        
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
        folderDocInfo.setCurrentFolderPath(dbsFolder.getAnyFolderPath());
        /* obtain arraylist of parents of the current folder */
        ArrayList listOfParents = folderDocInfo.getListOfParents();
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
        }
        for( int index = 0; index < listOfParents.size(); index++ ){
          logger.debug("listOfParents["+index+"] :"+listOfParents.get(index));
        }
        /* logic for setting nav specific data begins ... */
        int foldersCount = listOfParents.size();
        int folders = foldersCount;
        setNo = folderDocInfo.getHierarchySetNo();
        //if( drawersCount >= folderDocInfo.getHierarchySetNo()*7 ){

        int startIndex = ( setNo == 0 )?0:(setNo-1)*10;
        int endIndex = startIndex + 9;
        if( endIndex >= folders-1 )
          endIndex = folders-1;
        int difference = endIndex - startIndex;
        
        logger.debug("foldersCount: "+foldersCount);               
        logger.debug("startIndex: "+startIndex);
        logger.debug("endIndex: "+endIndex);    
      
        for( int index = 0; index < startIndex; index++ ){
          logger.debug("Name: "+listOfParents.get(0));
          listOfParents.remove(0);
          listOfParentsId.remove(0);
        }
        endIndex = difference;
        folders -= startIndex;
        logger.debug("folders: "+folders);               

        if( endIndex < folders ){
          for( int index = endIndex+1; index < folders; index++ ){
            logger.debug("Name next : "+listOfParents.get(endIndex+1));
            listOfParents.remove(endIndex+1);
            listOfParentsId.remove(endIndex+1);
          }
        }
        //}
        /* isFirstSet will take care of previous drawers navigation */
        boolean isFirstSet = (folderDocInfo.getHierarchySetNo() == 1)?true:false;
        /* isLastSet will take care of next drawers navigation */
        boolean isLastSet = ( foldersCount <= (10*(folderDocInfo.getHierarchySetNo())) ) ?
                              true:false;
        logger.debug("isLastSet: "+isLastSet);               
        request.setAttribute("isFirstSet",new Boolean(isFirstSet));
        request.setAttribute("isLastSet",new Boolean(isLastSet));
        /* logic for setting nav-specific data ens ... */
      }catch(ExceptionBean eb){
        logger.error("An Exception occurred in FolderFlatListAction... ");
        logger.error(eb.getErrorTrace());
      }catch(DbsException dex){
        exceptionBean = new ExceptionBean(dex);
        logger.error("An Exception occurred in FolderFlatListAction... ");
        logger.error(exceptionBean.getErrorTrace());
      }catch(Exception ex){
        exceptionBean = new ExceptionBean(ex);
        logger.error("An Exception occurred in FolderFlatListAction... ");
        logger.error(exceptionBean.getErrorTrace());
        ex.printStackTrace();
      }
    logger.debug("Exiting FolderFlatListAction... ");    
    return mapping.findForward(target);
  }
}