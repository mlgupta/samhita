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
 * $Id: UserDeleteAction.java,v 20040220.23 2006/03/17 12:59:51 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.beans.DbsUserManager;
import dms.web.actionforms.user.UserNewEditForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ConnectionBean;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.Hashtable;
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
 *	Purpose: To delete the user selected from user_list.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class UserDeleteAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  DbsTransaction deleteTransaction=null;
  //Initialize logger
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
    DbsDirectoryUser userToDelete=null;  
    String userName=null;
    boolean cmsdkUserDeleted = false;    /* boolean for deleting cmsdk user */
    boolean dbUserDeleted = false;       /* boolean for deleting db user */
    CryptographicUtil cryptUtil=new CryptographicUtil();
    ConnectionBean connBean = new ConnectionBean(request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml");
    Statement statement = null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();  
    try{
      logger.info("Entering UserDeleteAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserNewEditForm userNewEditFormOld=(UserNewEditForm)httpSession.getAttribute("userNewEditFormOld");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      userName= (String)httpSession.getAttribute("radSelect");
      logger.debug("Deleting user : " + userName);
      httpSession.removeAttribute("radSelect");
      
      DbsUserManager dbsManager = new DbsUserManager(dbsLibrarySession);      
      Hashtable deleteOptions = new Hashtable();
      // Override the default to free the user's home folder and contents
      deleteOptions.put(DbsUserManager.FREE_HOME_FOLDER,DbsAttributeValue.newAttributeValue(true));
      // Override the default to free the credential manager user
      deleteOptions.put(DbsUserManager.FREE_CREDENTIAL_MANAGER_USER,DbsAttributeValue.newAttributeValue(true));
          
      // Override the default to change the owner to system for 
      // PublicObjects owned by this user
      deleteOptions.put(DbsUserManager.CHANGE_OWNER, DbsAttributeValue.newAttributeValue(true));
      deleteOptions.put(DbsUserManager.NEW_OWNER_USER_NAME,DbsAttributeValue.newAttributeValue(dbsLibrarySession.getUser().getName()));
      // Delete a user
      logger.debug("Transaction started");
      deleteTransaction=dbsLibrarySession.beginTransaction();
      dbsManager.deleteUser(userName, deleteOptions);
      cryptUtil.deleteKeystore(userName,(String)httpSession.getServletContext().getAttribute("contextPath"));
      dbsLibrarySession.completeTransaction(deleteTransaction);
      cmsdkUserDeleted = true;
      logger.debug("Deleted user :"+userName);
      logger.debug("Transaction completed");
      deleteTransaction=null; 
      if(cmsdkUserDeleted){
      /* code for deleting db user */
        try{
          statement = connBean.getStatement();
          statement.execute("drop user "+userName.toUpperCase());
          statement = connBean.getStatement(true);
          statement.executeUpdate("DELETE FROM watch_pos WHERE USER_ID = '"+userName+"'");
          dbUserDeleted = true;
          logger.debug("DbUser "+userName.toUpperCase()+" has been deleted successfully!");
        }catch(Exception sqlEx){
          logger.error("An Exception occurred in UserDeleteAction... ");
          logger.error("SQLException: "+sqlEx.toString());
          sqlEx.printStackTrace();
          throw sqlEx;
        }
      }
    }catch(DbsException dbsException) {
      logger.error("An Exception occurred in UserDeleteAction... ");
      logger.error(dbsException.toString()); 
      if(dbsException.containsErrorCode(30659)){
        ActionError editError=new ActionError("errors.user.delete.30659",userName);       
        errors.add(ActionErrors.GLOBAL_ERROR,editError);  
        logger.debug(dbsException.getErrorMessage());
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in UserDeleteAction... ");
      logger.error(exception.toString()); 
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(deleteTransaction!=null){
          dbsLibrarySession.abortTransaction(deleteTransaction);
          logger.debug("Transaction aborted from user");
        }
        /* both users must be deleted */
        if(cmsdkUserDeleted && !dbUserDeleted){
          try{
            statement = connBean.getStatement();
            statement.execute("drop user "+userName.toUpperCase());
            statement = connBean.getStatement(true);
            statement.executeUpdate("DELETE FROM watch_pos WHERE USER_ID = '"+userName+"'");
            dbUserDeleted = true;
            logger.debug("DbUser "+userName.toUpperCase()+" has been deleted successfully!");
          }catch(Exception sqlEx){
            logger.error("An Exception occurred in UserDeleteAction... ");
            logger.error("SQLException in finally clause: "+sqlEx.toString());
            sqlEx.printStackTrace();
          }
        }
        connBean.closeStatement();    /* close statement and connection */
        connBean.closeConnection();
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in UserDeleteAction... ");
        logger.error(nestedException.toString());
      }  
    }//end finally
    if (!errors.isEmpty()) {
      httpSession.setAttribute("errors",errors);
      return mapping.findForward("success");
    }
    ActionMessages messages = new ActionMessages();
    ActionMessage msg = new ActionMessage("user.delete.ok",userName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);
    logger.info("Exiting UserDeleteAction now...");
    return mapping.findForward("success");  
  }
}
