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
 * $Id: AclDeleteAction.java,v 20040220.14 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
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
 *	Purpose: To delete Access Control List.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    23-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class AclDeleteAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
 /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Locale locale = getLocale(request);
    //initializing logger
    Logger logger = Logger.getLogger("DbsLogger");        
    String aclName=null;
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    DbsTransaction deleteTransaction=null;
    DbsAccessControlList aclToDelete=null; 
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();  
    try{
      logger.info("Entering AclDeleteAction now...");
      logger.debug("Deleting Acl : " + aclName);            
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      if(httpSession.getAttribute("radSelect")!=null){  
        aclName= (String)httpSession.getAttribute("radSelect");
        httpSession.removeAttribute("radSelect");
      }
      aclToDelete=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclName);
      deleteTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      aclToDelete.free();

      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("acl.delete.ok",aclName);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
      dbsLibrarySession.completeTransaction(deleteTransaction);
      logger.debug("Deleted Acl : " + aclName);          
      logger.debug("Transaction completed");

      deleteTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclDeleteAction... ");
      logger.error("Deleting Access Control List Aborted");                  
      logger.error(dbsException.toString());
      if(dbsException.containsErrorCode(33420)){
        ActionError editError=new ActionError("errors.acl.delete.33420",aclName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);          
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in AclDeleteAction... ");
      logger.error("Deleting Access Control List Aborted");                  
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(deleteTransaction!=null){
          dbsLibrarySession.abortTransaction(deleteTransaction);
          logger.debug("Transaction aborted.");
        }
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in AclDeleteAction... ");
        logger.error(nestedException.getErrorMessage());
      }
    }//end finally
    if (!errors.isEmpty()) {
      httpSession.setAttribute("errors",errors);
      mapping.findForward("success");
    }
    logger.info("Exiting AclDeleteAction now...");
    return mapping.findForward("success");
  }
}
