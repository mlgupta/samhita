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
 * $Id: PermissionBundleDeleteAction.java,v 20040220.13 2006/03/13 14:16:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
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
 *	Purpose: To delete the specified Permission Bundle data from permission_bundle.jsp.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    06-02-2004 
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class PermissionBundleDeleteAction extends Action {
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
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    Logger logger = null;
    logger = Logger.getLogger("DbsLogger");
    DbsTransaction deleteTransaction=null;
    DbsPermissionBundle permissionBundleToDelete=null;   
    String permissionBundleName=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();  

    try{
      logger.info("Entering PermissionBundleDeleteAction now...");
      httpSession = request.getSession(false);      
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      permissionBundleName= (String)httpSession.getAttribute("radSelect");
      logger.debug("Deleting Permission Bundle : " + permissionBundleName);
      httpSession.removeAttribute("radSelect");
      permissionBundleToDelete=(DbsPermissionBundle)SearchUtil.findObject(dbsLibrarySession,DbsPermissionBundle.CLASS_NAME,permissionBundleName);
      deleteTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      permissionBundleToDelete.free(); 	   
      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("permissionBundle.delete.ok",permissionBundleName);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
      dbsLibrarySession.completeTransaction(deleteTransaction);
      logger.debug("Deleted Permission Bundle : " + permissionBundleName);    
      logger.debug("Transaction completed");
      deleteTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionBundleDeleteAction... ");
      logger.error("Deleting Permission Bundle Aborted");          
      logger.error(dbsException.toString());
      if(dbsException.containsErrorCode(33421)){
        ActionError editError=new ActionError("errors.permissionBundle.delete.33421",permissionBundleName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);          
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionBundleDeleteAction... ");
      logger.error("Deleting Permission Bundle Aborted");          
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(deleteTransaction!=null){
          dbsLibrarySession.abortTransaction(deleteTransaction);
          logger.debug("Transaction aborted");
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in PermissionBundleDeleteAction... ");
        logger.error(nestedException.getErrorMessage());
      }  
      deleteTransaction=null;
    }//end finally
    if (!errors.isEmpty()) {      
      httpSession.setAttribute("errors",errors);
      mapping.findForward("success");
    }
    logger.info("Exiting PermissionBundleDeleteAction now...");
    return mapping.findForward("success");
  }
}
