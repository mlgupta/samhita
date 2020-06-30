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
 * $Id: AclListSelectForWfAction.java,v 1.6 2006/03/13 14:17:52 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.security.AclListSelectWfForm;
import dms.web.beans.security.AclSelectListBean;
import dms.web.beans.utility.FetchAdapters;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.DateUtil;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
//Servlet API
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
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
 *	Purpose: To populate acl_list_select_wf.jsp
 *  @author         :    Suved Mishra
 *  @version        :    1.0
 * 	Date of creation:    15-12-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:    
 */
public class AclListSelectForWfAction extends Action  {
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
  String control=null;
  int numRecords=0;
  int pageNumber=1;
  int pageCount=0;
  boolean forUser=false;
  DbsAccessControlList[] aclListSelect=null;
  AclListSelectWfForm aclForm=new AclListSelectWfForm();  

  // Validate the request parameters specified by the user
  ActionErrors errors = new ActionErrors();
  try{
    logger.info("Entering AclListSelectForWfAction now...");
    logger.debug("Fetching ACL List Select ...");
    httpSession = request.getSession(false);
    userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
    UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
    numRecords= userPreferences.getRecordsPerPage();
    dbsLibrarySession = userInfo.getDbsLibrarySession();
    if(httpSession.getAttribute("pageNumber")!=null){
      pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
      httpSession.removeAttribute("pageNumber");
    }
    if(httpSession.getAttribute("control")!=null){
      control=(String)httpSession.getAttribute("control");
      logger.debug("control : " + control);
      request.setAttribute("control",control);
      httpSession.removeAttribute("control");
    }
    if(httpSession.getAttribute("forUser")!=null){
      if(((String)httpSession.getAttribute("forUser")).equals("true")){
        forUser=true;
      }            
      aclForm.setSystemAclParameter("&forUser=true");
      httpSession.removeAttribute("forUser");
    }
    String singleAclName=(String)httpSession.getAttribute("txtSearchByAclName");
    /* fetch prefixes of enabled adapters begin ...*/
    try{
      String adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                                "WEB-INF"+File.separator+"params_xmls"+
                                File.separator+"Adapters.xml";
      FetchAdapters fetchAdapters = new FetchAdapters(adaptersFilePath);
      String enabledPrefixes = null;
      enabledPrefixes = fetchAdapters.getEnabledPrefixes();
      if( enabledPrefixes.length()!=0 ){
        singleAclName += ","+enabledPrefixes;
      }
    }catch (Exception e) {
      logger.debug(e.toString());
    }
    /* fetch prefixes of enabled adapters end ...*/
    ArrayList aclselects=new ArrayList();
    if(singleAclName!=null && !singleAclName.equals("")) {
      logger.debug("singleAclName : " + singleAclName);
      pageCount=SearchUtil.listAclsPageCountForWf(dbsLibrarySession,singleAclName,numRecords,forUser);
      if(pageNumber>pageCount){
        pageNumber=pageCount;
      }
      aclListSelect= SearchUtil.listAclsForWf(dbsLibrarySession,singleAclName,pageNumber,numRecords,forUser);            
      aclForm.setTxtSearchByAclName((String)httpSession.getAttribute("txtSearchByAclName"));
      httpSession.removeAttribute("txtSearchByAclName");
      if(aclListSelect.length==0){
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("search.notFound",singleAclName);
        messages.add("message1", msg);
        saveMessages(request,messages);
        aclForm.setTxtSearchByAclName("");
      }
    }else{               
      pageCount=SearchUtil.listAclsPageCount(dbsLibrarySession,null,numRecords,forUser);
      if(pageNumber>pageCount){
        pageNumber=pageCount;
      }
      aclListSelect= SearchUtil.listAcls(dbsLibrarySession,null,pageNumber,numRecords,forUser);
    }//end else
    aclForm.setTxtPageCount(new String().valueOf(pageCount));
    logger.debug("pageCount : " + pageCount);            
    aclForm.setTxtPageNo(new String().valueOf(pageNumber));
    logger.debug("pageNumber : " + pageNumber);            
    AclSelectListBean[] aclSelectListBean=new AclSelectListBean[aclListSelect.length];
    for(int index=0;index < aclListSelect.length ; index ++){
      aclSelectListBean[index]=new AclSelectListBean();
      aclSelectListBean[index].setAclName(aclListSelect[index].getName());
      aclSelectListBean[index].setOwner(aclListSelect[index].getOwner().getDistinguishedName());
      aclSelectListBean[index].setCreateDate(DateUtil.getFormattedDate(aclListSelect[index].getCreateDate()));                        
      aclselects.add(aclSelectListBean[index]);
    }
    request.setAttribute("AclListSelectWfForm",aclForm);
    request.setAttribute("aclselects",aclselects);
    logger.debug("Number of records fetched : "+ aclListSelect.length);
    logger.debug("Fetching ACL List Completed.");        
  }catch(DbsException dbsException){
    logger.error("An Exception occurred in AclListSelectForWfAction... ");
    logger.error("Fetching ACL List Aborted");          
    logger.error(dbsException.toString());
    ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
    errors.add(ActionErrors.GLOBAL_ERROR,editError);
  }catch(Exception exception){
    logger.error("An Exception occurred in AclListSelectForWfAction... ");
    logger.error("Fetching ACL List Aborted");          
    logger.error(exception.toString());
    ActionError editError=new ActionError("errors.catchall",exception);
    errors.add(ActionErrors.GLOBAL_ERROR,editError);
  }
  if(!errors.isEmpty()){      
    saveErrors(request, errors);
    return (mapping.getInputForward());
  }
  logger.info("Exiting AclListSelectForWfAction now...");
  return mapping.findForward("success");
}
}