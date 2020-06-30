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
 * $Id: DocHistoryDeleteAction.java,v 20040220.18 2006/03/13 14:15:38 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsTransaction;
import dms.web.actionforms.filesystem.DocHistoryListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.Version;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: Action called to delete selected version from a versioned document 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   02-03-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class DocHistoryDeleteAction extends Action  {
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
    logger.info("Entering DocHistoryDeleteAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    DbsTransaction deleteTransaction = null;
    DbsLibrarySession dbsLibrarySession = null;
    try{
      httpSession = request.getSession(false);
      DocHistoryListForm docHistoryListForm = (DocHistoryListForm)form;
      logger.debug("docHistoryListForm : " + docHistoryListForm);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");

      dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long docId = docHistoryListForm.getRadDocId();
      logger.debug("docId : " + docId);
      Long familyId = docHistoryListForm.getChkFolderDocIds()[0];
      logger.debug("familyId : " + familyId);

      DbsPublicObject publicObject = dbsLibrarySession.getPublicObject(docId);
      logger.info("Deleting Version Number " + publicObject.getVersionNumber() +
                  " of " + publicObject.getName());
      //Call delete within a transaction
      deleteTransaction = dbsLibrarySession.beginTransaction();
      Version version = new Version(dbsLibrarySession);
      version.deleteDocHistory(docId);
      dbsLibrarySession.completeTransaction(deleteTransaction);
      logger.debug("Transaction completed successfully...");
      deleteTransaction = null;
      /* set up action message upon successful deletion */
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.SelectedVersionDeleted");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);
            
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in DocHistoryDeleteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      if(dex.containsErrorCode(30033)){
        exceptionBean.setMessageKey("errors.30033.insufficient.access.to.delete.or.free.a.PO");
      }
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
      logger.error("Delete Version Aborted");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DocHistoryDeleteAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
      logger.error("Delete Version Aborted");
    }finally{
      if( deleteTransaction != null && !deleteTransaction.isCompleteable() ){
        try{
          dbsLibrarySession.abortTransaction(deleteTransaction);
          logger.error("Aborting Transaction ...");
        }catch(DbsException dbsEx) {
          logger.error("An Exception occurred in DocHistoryDeleteAction... ");
          logger.error(dbsEx.toString());          
        }
        deleteTransaction = null;
      }
    }
    logger.info("Exiting DocHistoryDeleteAction now...");
    return mapping.findForward(forward);
  }
}
