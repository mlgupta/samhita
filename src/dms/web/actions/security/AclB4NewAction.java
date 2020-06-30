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
 * $Id: AclB4NewAction.java,v 20040220.14 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* adapter package references */
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.security.AclNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.FetchAdapters;
/* Java API */
import java.io.File;
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
/**
 *	Purpose: To populate acl_new.jsp with the default data.
 *  @author              Rajan Kamal Gupta 
 *  @version             1.0
 * 	Date of creation:    23-01-2004
 * 	Last Modified by :   Suved O.Mishra  
 * 	Last Modified Date:  12-01-2006
 *  The recent modification consists of including various ACL types as dropdown 
 *  options, viz: adapters ACL, approvers ACL, non adapter ACLS.
 */
public class AclB4NewAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  //initializing Logger
  Logger logger = Logger.getLogger("DbsLogger");    
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ArrayList memberList=new ArrayList();
    boolean deleteMessagePresent=false;
    XMLBean xmlBean = null;
    String adaptersFilePath = null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering AclB4NewAction now...");
      httpSession = request.getSession(false);           
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();   
      adaptersFilePath = httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+
                         File.separator+"Adapters.xml";
                         
      AclNewEditForm aclNewEditForm=new AclNewEditForm();            
      aclNewEditForm.setTxtOwnerName(dbsLibrarySession.getUser().getName()); 
      aclNewEditForm.setTxtAccessControlList(dbsLibrarySession.getUser().getAcl().getName());
      /* fetch adapters' name and their respective prefix*/
      FetchAdapters fetchAdapters = new FetchAdapters(adaptersFilePath);
      ArrayList adapters = fetchAdapters.fetchList();
      logger.debug("adapters' size : "+adapters.size());
      request.setAttribute("adapters",adapters);
      aclNewEditForm.setHdnPrefix(fetchAdapters.getAllPrefixes());
      aclNewEditForm.setHdnAdapterName("nonadapter");

      request.setAttribute("aclNewEditForm",aclNewEditForm);            
      logger.debug("aclNewEditForm : " + aclNewEditForm); 
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclB4NewAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in AclB4NewAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {      
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting AclB4NewAction now...");
    return mapping.findForward("success");
  }
}
