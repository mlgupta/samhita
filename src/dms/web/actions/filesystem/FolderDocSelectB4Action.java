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
 * $Id: FolderDocSelectB4Action.java,v 1.16 2006/05/19 06:24:03 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocSelectForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
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
/**
 *	Purpose: To Poulate Treeview for Selection
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   02-04-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class FolderDocSelectB4Action extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering FolderDocSelectB4Action now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    UserInfo userInfo=null;
    Treeview treeview4Select =null; 
    DbsLibrarySession dbsLibrarySession = null;
    UserPreferences userPreferences=null; 
    String physicalPath=null;
    boolean foldersOnly=true;
    Long currentFolderDocId4Select =null;
    Long currentDocumentId4Select=null;
    boolean treeAppend=false;
    FolderDocInfo folderDocInfo=null;
    String openerControl=null;
    String currentFolderPath=null; 
    DbsPublicObject dbsPublicObject=null; 
    FolderDocSelectForm folderDocSelectForm=null;
    boolean isDocument=false;
    boolean recreateTree=false;
    String heading="";
    try{
      httpSession = request.getSession(false);
      logger.info("Performing Folder Doc Tree Append Action ...");
      physicalPath=request.getSession().getServletContext().getRealPath("/");
      userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();

      if(request.getParameter("recreate")!=null){
        recreateTree=(new Boolean(request.getParameter("recreate"))).booleanValue();
      }
      
      
      if(request.getParameter("currentFolderDocId4Select")!=null){
        currentFolderDocId4Select =new Long(request.getParameter("currentFolderDocId4Select"));
      }else if (httpSession.getAttribute("currentFolderDocId4Select")!=null){
        currentFolderDocId4Select =(Long)httpSession.getAttribute("currentFolderDocId4Select");
      }else{
        currentFolderDocId4Select =folderDocInfo.getHomeFolderId();
      }

      if(request.getParameter("heading")!=null){
        heading=request.getParameter("heading");
      }else if(httpSession.getAttribute("heading")!=null) {
        heading=(String)httpSession.getAttribute("heading");
      }
      
      if(request.getParameter("foldersOnly")!=null){
        foldersOnly=(new Boolean(request.getParameter("foldersOnly"))).booleanValue();
      }else if(httpSession.getAttribute("foldersOnly")!=null) {
        foldersOnly=(new Boolean(httpSession.getAttribute("foldersOnly").toString())).booleanValue();
      }
            
      if(request.getParameter("openerControl")!=null){
        openerControl=request.getParameter("openerControl");
      }else if(httpSession.getAttribute("foldersOnly")!=null) {
        openerControl=httpSession.getAttribute("openerControl").toString();
      }
     
      if(request.getParameter("treeAppend")!=null){
        treeAppend=true;
      }
      if (httpSession.getAttribute("Treeview4Select")!=null){
        treeview4Select=(Treeview)httpSession.getAttribute("Treeview4Select");
          if (treeview4Select!=null){
            if (treeview4Select.isFoldersOnly()!=foldersOnly){
              treeview4Select.free();
              treeview4Select = new Treeview(dbsLibrarySession,"FolderDocSelect",userPreferences.getTreeLevel(),physicalPath,userPreferences.getTreeIconPath()+ "/",httpSession.getId(),foldersOnly);
            }
        }
      }
      
      if (currentFolderDocId4Select !=null){
      
        if (treeview4Select==null || recreateTree){
          if (treeview4Select!=null){
            treeview4Select.free();
          }
          treeview4Select = new Treeview(dbsLibrarySession,"FolderDocSelect",userPreferences.getTreeLevel(),physicalPath,userPreferences.getTreeIconPath()+ "/",httpSession.getId(),foldersOnly);
        }
        /* if accidently, the currentFolderDocId4Select is deleted from the 
         * navigation tree or list , then the popup tree structure display 
         * should remain unaffected and the current folder be set to user's 
         * home folder */
        try {
          dbsPublicObject = dbsLibrarySession.getPublicObject(currentFolderDocId4Select).getResolvedPublicObject();
        }catch (DbsException dbsEx) {
          currentFolderDocId4Select = folderDocInfo.getHomeFolderId();
          dbsPublicObject = dbsLibrarySession.getPublicObject(currentFolderDocId4Select).getResolvedPublicObject();
        }
        currentFolderPath=dbsPublicObject.getAnyFolderPath();
        isDocument=(dbsPublicObject instanceof DbsDocument);
        
        if(treeAppend){
          treeview4Select.appendTree(currentFolderDocId4Select );
        }

        treeview4Select.forAddressBar(currentFolderDocId4Select );

        folderDocSelectForm= new  FolderDocSelectForm();
        folderDocSelectForm.setFolderDocument(currentFolderPath);
        folderDocSelectForm.setHdnFolderDocument(currentFolderPath); 
        folderDocSelectForm.setHdnOpenerControl(openerControl);
        folderDocSelectForm.setHdnFoldersOnly(foldersOnly);
        
        httpSession.setAttribute("Treeview4Select",treeview4Select);
        httpSession.setAttribute("currentFolderDocId4Select",currentFolderDocId4Select );    
        request.setAttribute("isDocument",new Boolean(isDocument));    
        request.setAttribute("folderDocSelectForm",folderDocSelectForm);
        httpSession.setAttribute("heading",heading);
      }      

      if (httpSession.getAttribute("actionerrors")!=null){
        ActionErrors actionErrors = (ActionErrors) httpSession.getAttribute("actionerrors");
        saveErrors(request,actionErrors); 
        httpSession.removeAttribute("actionerrors");
      }
    }catch(DbsException e){
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in FolderDocSelectB4Action... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors()); 
      return mapping.findForward(forward);
    }catch(Exception e){
      exceptionBean = new ExceptionBean(e);
      logger.error("An Exception occurred in FolderDocSelectB4Action... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
      return mapping.findForward(forward);
    }
    logger.info("Exiting FolderDocSelectB4Action now...");
    return mapping.findForward(forward);
  }
}
