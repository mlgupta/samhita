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
 * $Id: FolderDocB4ApplyAclAction.java,v 20040220.18 2006/03/17 08:44:47 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocApplyAclForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.security.AclSelectListBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.DateUtil;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
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
 *	Purpose: Action called to display list of available ACLs to apply to a PO
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006  
 */
public class FolderDocB4ApplyAclAction extends Action {
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
    logger.info("Entering FolderDocB4ApplyAclAction now...");
    logger.debug("Fetching ACL List ...");

    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);
    
    ArrayList aclselects=new ArrayList();
    DbsAccessControlList[] aclListSelect=null;
    int pageNumber;
    try{
      FolderDocApplyAclForm folderDocApplyAclForm = (FolderDocApplyAclForm)form;
      logger.debug("folderDocApplyAclForm : " + folderDocApplyAclForm);
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences) httpSession.getAttribute("UserPreferences");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      String singleAclName=null;

      logger.debug("singleAclName : " + singleAclName);
      int recordPerPage = userPreferences.getRecordsPerPage();
      logger.debug("recordPerPage : " + recordPerPage);
      pageNumber = folderDocApplyAclForm.getTxtApplyAclPageNo();
      logger.debug("Initializing Variable Complete");

      if (folderDocApplyAclForm.getTxtSearchByAclName()==null || 
          folderDocApplyAclForm.getTxtSearchByAclName().equals(""))  {
        singleAclName = null;
      }else{
        singleAclName = folderDocApplyAclForm.getTxtSearchByAclName();
        pageNumber = 0;
      }

      if(pageNumber == 0 ){
        pageNumber = 1;
        folderDocApplyAclForm.setTxtApplyAclPageNo(pageNumber);
      }
      
      aclListSelect= SearchUtil.listAclSelect(dbsLibrarySession,singleAclName);
      logger.debug("aclListSelect.length : " + aclListSelect.length);
      AclSelectListBean[] aclSelectListBean=new AclSelectListBean[aclListSelect.length];
      int startIndex = recordPerPage * (pageNumber - 1) ;
      int endIndex = startIndex + recordPerPage;
      if(endIndex > aclListSelect.length){
        endIndex = aclListSelect.length;
      }
      int pageCount = (int)StrictMath.ceil((double)aclListSelect.length / recordPerPage);
      logger.debug("pageCount : " + pageCount);

      if(pageNumber == 0 ){
        pageNumber = 1;
        folderDocApplyAclForm.setTxtApplyAclPageNo(pageNumber);
      }

      if(pageNumber > pageCount){
        pageNumber = pageCount;
        folderDocApplyAclForm.setTxtApplyAclPageNo(pageNumber);
      }

      logger.debug("pageNumber : " + pageNumber);

      if(pageCount > 0){
        folderDocApplyAclForm.setTxtApplyAclPageCount(pageCount);
      }else{
        folderDocApplyAclForm.setTxtApplyAclPageCount(1);
      }
      logger.debug("startIndex : " + startIndex);
      logger.debug("endIndex : " + endIndex);
      String strTemp="{Id,\"Name\"} {";
      for(int index = startIndex; index < endIndex; index++){
        aclSelectListBean[index]=new AclSelectListBean();
        aclSelectListBean[index].setAclId(aclListSelect[index].getId());
        aclSelectListBean[index].setAclName(aclListSelect[index].getName());
        strTemp += " {" + aclSelectListBean[index].getAclId() + ",\"" + aclSelectListBean[index].getAclName() + "\"}";                
        aclSelectListBean[index].setOwner(aclListSelect[index].getOwner().getName());
        aclSelectListBean[index].setCreateDate(DateUtil.getFormattedDate(aclListSelect[index].getLastModifyDate()));
        aclselects.add(aclSelectListBean[index]);
      }
      strTemp += " }";
      logger.debug("ACL List : " + strTemp);
      request.setAttribute("aclselects",aclselects);
      
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.SelectAclToApply");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      saveMessages(request,actionMessages);
      logger.debug("Fetching ACL List Complete");
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in FolderDocB4ApplyAclAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocB4ApplyAclAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocB4ApplyAclAction now...");
    return mapping.findForward(forward);
  }
}
