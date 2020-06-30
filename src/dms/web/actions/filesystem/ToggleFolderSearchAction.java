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
 * $Id: ToggleFolderSearchAction.java,v 20040220.13 2006/05/30 13:15:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.web.actionforms.filesystem.AdvanceSearchForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.ResetBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/*java API*/
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API*/
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To toggle between search and list type
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   24-03-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class ToggleFolderSearchAction extends Action  {
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
    logger.info("Entering ToggleFolderSearchAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    try{
      httpSession = request.getSession(false);
      AdvanceSearchForm advanceSearchForm = (AdvanceSearchForm)form;
      logger.debug("Toggling between treeview and searchview");
      logger.debug("AdvanceSearchForm:"+advanceSearchForm);
      advanceSearchForm.setTxtFolderOrDocName(new String());
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");            
      FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      logger.debug("folderDocInfo : " + folderDocInfo);
      /* if tree visible toggle to search */
      if(folderDocInfo.isTreeVisible()){
        folderDocInfo.setTreeVisible(false);
        advanceSearchForm.setTxtLookinFolderPath(folderDocInfo.getCurrentFolderPath());
        logger.debug("AdvanceSearchForm after setTxtLookinFolderPath"+advanceSearchForm);
        ResetBean resetBean=new ResetBean();
        advanceSearchForm=resetBean.resetAll(advanceSearchForm,userPreferences.getNavigationType());
        logger.debug("AdvanceSearchForm in Toggle now: "+advanceSearchForm);                
        httpSession.setAttribute("advanceSearchForm",advanceSearchForm);
        if( request.getAttribute("reset")!= null &&  
          ((String)request.getAttribute("reset")).trim().length() !=0 ){
          request.setAttribute("fromToggle","forSearch");
        }
      }
      /* toggle from search to tree */
      else{
        folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
        folderDocInfo.setTreeVisible(true);
        httpSession.removeAttribute("advanceSearchForm");
        request.setAttribute("fromToggle","forTree");
      }
      logger.debug("folderDocInfo : " + folderDocInfo);
      logger.debug("Toggling between treeview and searchview complete");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in ToggleFolderSearchAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting ToggleFolderSearchAction now...");
    return mapping.findForward(forward);
  }
}
