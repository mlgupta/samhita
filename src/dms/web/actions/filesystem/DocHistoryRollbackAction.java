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
 * $Id: DocHistoryRollbackAction.java,v 20040220.11 2006/03/17 12:33:11 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.DocHistoryListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.Version;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
/* Servlet API */
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
 *	Purpose: Action called to rollback to a particular version of a versioned 
 *           document 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   03-03-2004
 * 	Last Modified by :   
 * 	Last Modified Date:   
 */
public class DocHistoryRollbackAction extends Action  {
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
        logger.info("Rolling Back History...");

        
        //variable declaration
        ExceptionBean exceptionBean;
        String forward = "success";
        HttpSession httpSession = null;        
        try{
            httpSession = request.getSession(false);
            DocHistoryListForm docHistoryListForm = (DocHistoryListForm)form;
            logger.debug("docHistoryListForm : " + docHistoryListForm);
            UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");

            DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
            Long docId = docHistoryListForm.getRadDocId();
            logger.debug("docId : " + docId);
            Long familyId = docHistoryListForm.getChkFolderDocIds()[0];
            logger.debug("familyId : " + familyId);

            DbsPublicObject publicObject = dbsLibrarySession.getPublicObject(docId);
            logger.info("Rolling Back to Version Number " + publicObject.getVersionNumber() + " of " + publicObject.getName());
                
            Version version = new Version(dbsLibrarySession);
            version.rollbackDocHistory(docId);
                    
            ActionMessages actionMessages = new ActionMessages();
            ActionMessage actionMessage = new ActionMessage("msg.HistoryRollbackedToSelectedVersion");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            saveMessages(request,actionMessages);
        }catch(DbsException dex){
            exceptionBean = new ExceptionBean(dex);
            logger.error(exceptionBean.getMessage());
            httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
            logger.info("Rollback Aborted");
        }catch(Exception ex){
            exceptionBean = new ExceptionBean(ex);
            logger.error(exceptionBean.getMessage());
            httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
            logger.info("Rollback Aborted");
        }
        logger.info("Rolling Back History Complete");
        return mapping.findForward(forward);
    }
}
