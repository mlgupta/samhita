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
 * $Id: PermissionBundleListAction.java,v 20040220.11 2006/03/13 14:07:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
import dms.web.actionforms.security.PermissionBundleListForm;
import dms.web.beans.security.PermissionBundleListBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
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
 *	Purpose: To populate permission_bundle_list.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class PermissionBundleListAction extends Action {
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
    logger.info("Entering PermissionBundleListAction now...");
    Locale locale = getLocale(request);        
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    int numRecords=0;
    int pageNumber=1;
    int pageCount=0;
    // Validate the request parameters specified by the user
    PermissionBundleListForm permissionBundleForm=new PermissionBundleListForm();    
    ActionErrors errors = new ActionErrors();    
    try{
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
      numRecords= userPreferences.getRecordsPerPage();
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      if(httpSession.getAttribute("pageNumber")!=null){
        pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
        httpSession.removeAttribute("pageNumber");
      }
      String singlePermissionBundleName=(String)httpSession.getAttribute("txtSearchByPermissionBundleName");
      if(httpSession.getAttribute("messages")!=null){
        saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
        httpSession.removeAttribute("messages");
      }
      if(httpSession.getAttribute("errors")!=null){
        saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
        httpSession.removeAttribute("errors");
      }
      ArrayList permissionbundles=new ArrayList();
      DbsPermissionBundle[] permissionBundle= null;
      if(singlePermissionBundleName!=null && !singlePermissionBundleName.equals("")) {
        logger.debug("Permission Bundle To Find"+ singlePermissionBundleName);
        pageCount=SearchUtil.listPermissionBundlesPageCount(dbsLibrarySession,singlePermissionBundleName,numRecords);
        if(pageNumber>pageCount){
           pageNumber=pageCount;
        }
        permissionBundle= SearchUtil.listPermissionBundles(dbsLibrarySession,singlePermissionBundleName,pageNumber,numRecords);            
        permissionBundleForm.setTxtSearchByPermissionBundleName((String)httpSession.getAttribute("txtSearchByPermissionBundleName"));
        httpSession.removeAttribute("txtSearchByPermissionBundleName");  
        if(permissionBundle.length==0){
          ActionMessages messages = new ActionMessages();
          ActionMessage msg = new ActionMessage("search.notFound",singlePermissionBundleName);
          messages.add("message1", msg);
          saveMessages(request,messages);
          permissionBundleForm.setTxtSearchByPermissionBundleName("");
        }                
      }else{               
        pageCount=SearchUtil.listPermissionBundlesPageCount(dbsLibrarySession,null,numRecords);
        if(pageNumber>pageCount){
          pageNumber=pageCount;
        }
        permissionBundle= SearchUtil.listPermissionBundles(dbsLibrarySession,null,pageNumber,numRecords);   
      }//end else
      permissionBundleForm.setTxtPageCount(new String().valueOf(pageCount));
      logger.debug("pageCount : " + pageCount);
      permissionBundleForm.setTxtPageNo(new String().valueOf(pageNumber));
      logger.debug("pageNumber : " + pageNumber);
      PermissionBundleListBean[] permissionBundleListBean=new PermissionBundleListBean[permissionBundle.length];
      for(int index=0;index < permissionBundle.length; index ++){
        if(permissionBundle[index].getName()!=null){
          permissionBundleListBean[index]=new PermissionBundleListBean();
          permissionBundleListBean[index].setPermissionName(permissionBundle[index].getName());
          permissionBundleListBean[index].setPermissionDescription(permissionBundle[index].getDescription());                   
          permissionbundles.add(permissionBundleListBean[index]);
        }
      }
      request.setAttribute("permissionbundles",permissionbundles);
      request.setAttribute("PermissionBundleListForm",permissionBundleForm);
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionBundleListAction... ");
      logger.error("Fetching Permission Bundle List Aborted");  
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionBundleListAction... ");
      logger.error("Fetching Permission Bundle List Aborted");  
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {      
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting PermissionBundleListAction now...");
    return mapping.findForward("success");
  }
}
