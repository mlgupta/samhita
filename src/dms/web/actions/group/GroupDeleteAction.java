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
 * $Id: GroupDeleteAction.java,v 20040220.14 2006/03/17 12:42:46 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.group; 
/* dms package references */
import dms.beans.DbsDirectoryGroup;
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
 *	Purpose: To delete the group selected from group_list.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    09-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GroupDeleteAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
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
    DbsTransaction deleteTransaction=null;
    DbsDirectoryGroup groupToDelete=null;   
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();  
    try{            
      httpSession = request.getSession(false);      
      logger.info("Entering GroupDeleteAction now...");
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession(); 
      String groupName= (String)httpSession.getAttribute("radSelect");
      logger.debug("Deleting group : " + groupName);            
      httpSession.removeAttribute("radSelect");
      groupToDelete=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groupName);
      deleteTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      groupToDelete.free(); 	   
      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("group.delete.ok",groupName);
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
      dbsLibrarySession.completeTransaction(deleteTransaction);
      logger.debug("Group deleted : " + groupName);
      logger.debug("Transaction completed");
      deleteTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in GroupDeleteAction... ");
      logger.error(dbsException.toString());
      if(dbsException.containsErrorCode(10013)){
        ActionError editError=new ActionError("errors.group.delete.1003");
        errors.add(ActionErrors.GLOBAL_ERROR,editError);  
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in GroupDeleteAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(deleteTransaction!=null){
          dbsLibrarySession.abortTransaction(deleteTransaction);
          logger.debug("Transaction aborted from group");
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in GroupDeleteAction... ");
        logger.error(nestedException.toString());
      }  
    }//end finally
    if(!errors.isEmpty()){
      httpSession.setAttribute("errors",errors);
      return mapping.findForward("success");
    }
    logger.info("Exiting GroupDeleteAction now...");
    return mapping.findForward("success");
  }
}
