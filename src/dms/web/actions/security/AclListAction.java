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
 * $Id: AclListAction.java,v 20040220.21 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.security.AclListForm;
import dms.web.beans.security.AclListBean;
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To populate acl_list.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 03-03-2006   
 */
public class AclListAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
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
    //variable declaration
    Locale locale = getLocale(request);        
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    String singleAclName=null;
    int numRecords=0;
    int pageNumber=1;
    int pageCount=0;
    DbsAccessControlList[] aclList=null;

    // Validate the request parameters specified by the user
    AclListForm aclForm=new AclListForm();    
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering AclListAction now...");
      logger.debug("Fetching ACL List ...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
      numRecords= userPreferences.getRecordsPerPage();
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      if(httpSession.getAttribute("pageNumber")!=null){
        pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
        httpSession.removeAttribute("pageNumber");
      }
      singleAclName=(String)httpSession.getAttribute("txtSearchByAclName");
      logger.debug("singleAclName : "+singleAclName);
      if(httpSession.getAttribute("messages")!=null){
        logger.debug("Saving action message in request stream");
        saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
        httpSession.removeAttribute("messages");
      }
      if(httpSession.getAttribute("errors")!=null){
        logger.debug("Saving action errors in request stream");
        saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
        httpSession.removeAttribute("errors");
      }
      ArrayList acls=new ArrayList();
      if(singleAclName!=null && !singleAclName.equals("")) {
        logger.debug("Acl To Find : " + singleAclName);
        pageCount=SearchUtil.listAclsPageCount(dbsLibrarySession,singleAclName,numRecords,false);
        if(pageNumber>pageCount){
          pageNumber=pageCount;
        }
        aclList= SearchUtil.listAcls(dbsLibrarySession,singleAclName,pageNumber,numRecords,false);            
        aclForm.setTxtSearchByAclName((String)httpSession.getAttribute("txtSearchByAclName"));
        httpSession.removeAttribute("txtSearchByAclName");
        if(aclList.length==0){
          ActionMessages messages = new ActionMessages();
          ActionMessage msg = new ActionMessage("search.notFound",singleAclName);
          messages.add("message1", msg);
          saveMessages(request,messages);
          aclForm.setTxtSearchByAclName("");
        }
      }else{               
        pageCount=SearchUtil.listAclsPageCount(dbsLibrarySession,null,numRecords,false);
        if(pageNumber>pageCount){
          pageNumber=pageCount;
        }
        aclList= SearchUtil.listAcls(dbsLibrarySession,null,pageNumber,numRecords,false);
      }//end else
      aclForm.setTxtPageCount(new String().valueOf(pageCount));
      logger.debug("pageCount : " + pageCount);
      aclForm.setTxtPageNo(new String().valueOf(pageNumber));
      logger.debug("pageNumber : " + pageNumber);
      AclListBean[] aclListBean=new AclListBean[aclList.length];
      for(int index=0;index < aclList.length ; index ++) {
        aclListBean[index]=new AclListBean();
        if ((!(userInfo.isSystemAdmin()) && 
            (aclList[index].getName().startsWith("wf"))) || 
            ((aclList[index].getName().equals("Private")) || 
            (aclList[index].getName().equals("Protected"))|| 
            (aclList[index].getName().equals("Public")) || 
            (aclList[index].getName().equals("Published")))) {
          aclListBean[index].setIsDisabled("true");
        }else{
          aclListBean[index].setIsDisabled("false");
        }
        aclListBean[index].setAclName(aclList[index].getName());
        aclListBean[index].setOwner(aclList[index].getOwner().getName());
        DbsAccessControlList securingAcl=aclList[index].getAcl();
        if(securingAcl!=null){
          aclListBean[index].setSecuringAcl(securingAcl.getName());            
        }
        aclListBean[index].setIsShared(new Boolean(aclList[index].isShared()));      
        aclListBean[index].setModified(DateUtil.getFormattedDate(aclList[index].getLastModifyDate()));                        
        acls.add(aclListBean[index]);
      }
      request.setAttribute("acls",acls);
      request.setAttribute("AclListForm",aclForm);
      logger.debug("Number of records fetched : "+ aclList.length);
      logger.debug("Fetching ACL List Complete");
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclListAction... ");
      logger.error("Fetching ACL List Aborted");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in AclListAction... ");
      logger.error("Fetching ACL List Aborted");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()){
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting AclListAction now...");
    return mapping.findForward("success");
  }
}
