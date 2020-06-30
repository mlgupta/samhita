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
 * $Id: ChangeEncryptionPasswordAction.java,v 20040220.8 2006/03/13 14:19:04 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
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
 *	Purpose: To change encryption password for a given user.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    15-04-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class ChangeEncryptionPasswordAction extends Action{
  DbsLibraryService dbsLibraryService = null;
  DbsCleartextCredential dbsCleartextCredential = null;
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
    HttpSession httpSession = null;
    Logger logger = null;
    logger = Logger.getLogger("DbsLogger");
    UserInfo userInfo = null;
    ActionErrors errors = new ActionErrors();
    String editUserName=null;
    try{
      logger.info("Entering ChangeEncryptionPasswordAction now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      //String password=((String)PropertyUtils.getSimpleProperty(form, "txtPassword")).trim();
      String oldPassword=request.getParameter("txtOldPassword");
      String password=request.getParameter("txtPassword");
      CryptographicUtil cryptoUtil=new CryptographicUtil();
      String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
      DbsDirectoryUser editUser=dbsLibrarySession.getUser();
      editUserName=editUser.getName();
      String result=cryptoUtil.changeKeystorePass(editUserName,oldPassword,password,contextPath);
      if(result.equals("SUCCESS")){
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("change.password.success",editUserName);
        messages.add("message1", msg);
        httpSession.setAttribute("messages",messages);
      }else if(result.equals("FAILURE")){
        ActionError editError=new ActionError("change.password.failure",editUserName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }else if(result.equals("PASSWORD_WRONG")){
        ActionError editError=new ActionError("password.wrong",editUserName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);                
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in ChangeEncryptionPasswordAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in ChangeEncryptionPasswordAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {      
      httpSession.setAttribute("errors",errors);
    }
    logger.info("Exiting ChangeEncryptionPasswordAction now...");
    return mapping.findForward("success");
  }
}
