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
 * $Id: AceRemoveAction.java,v 20040220.17 2006/03/13 14:17:52 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.AclNewEditForm;
import dms.web.beans.user.UserInfo;
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
import org.apache.commons.beanutils.PropertyUtils;
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
 *	Purpose: The remove Access Control Entry from Access Control List.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    07-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class AceRemoveAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
 /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing logger
    Logger logger = Logger.getLogger("DbsLogger");
    //variable declaration
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    DbsTransaction deleteTransaction=null;
    DbsAccessControlList aclToEdit=null;
    String aclName=null;
    String[] indexToRemove=null;
    ArrayList aceList=null;
    boolean exceptionOccured=false;
    DbsTransaction editTransaction=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{
      logger.info("Entering ACERemoveAction now...");
      aclName = ((String)PropertyUtils.getSimpleProperty(form, "txtAclName")).trim();
      //indexToRemove=((String[])PropertyUtils.getSimpleProperty(form, "chkSelect"));
      indexToRemove=((AclNewEditForm)form).getChkSelect();
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();

      deleteTransaction=dbsLibrarySession.beginTransaction();
      aclToEdit=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,
                                      DbsAccessControlList.CLASS_NAME,aclName);
      logger.debug("Transaction started");
      DbsAccessControlEntry[] allEntries = aclToEdit.getAccessControlEntrys();
      for(int index=0;index<indexToRemove.length;index++){    
        int removeIndex=(Integer.parseInt(indexToRemove[index]));
        DbsAccessControlEntry entryToRemove=allEntries[removeIndex];
        logger.debug(entryToRemove.getGrantee().getName() + " removed from " +
                     aclToEdit.getName());
        aclToEdit.removeAccessControlEntry(entryToRemove);
      }
      dbsLibrarySession.completeTransaction(deleteTransaction);
      logger.debug("Transaction completed");
      deleteTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AceRemoveAction... ");
      logger.error("Removing Access Control Entry Aborted");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in AceRemoveAction... ");
      logger.error("Removing Access Control Entry Aborted");
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
        logger.error("An Exception occurred in AceRemoveAction... ");
        logger.error(nestedException.getErrorMessage());
      }
      deleteTransaction = null;
    }//end finally
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    if(!exceptionOccured) {
      ActionMessages messages = new ActionMessages();
      ActionMessage msg = new ActionMessage("ace.delete.ok");
      messages.add("message1", msg);
      httpSession.setAttribute("messages",messages);
    }
    httpSession.setAttribute("radSelect",aclName);
    logger.info("Exiting ACERemoveAction now...");
    return mapping.findForward("success");
  }
}
