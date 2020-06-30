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
 * $Id: NotificationDetailsAction.java,v 20040220.14 2005/03/29 04:41:36 suved Exp $
 *****************************************************************************
 */

package dms.web.actions.workflow;
/* dms package references */
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.workflow.NotificationDetailsForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.wf.docApprove.ApproveRejectNotification;
import dms.web.beans.wf.docApprove.ChangeDocWfStatusBean;
/* Java API */
import java.io.File;
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
 *	Purpose: To populate notification_details.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    03-05-2005
 * 	Last Modified by :   Suved Mishra     
 * 	Last Modified Date:  03-03-2006  
 */
public class NotificationDetailsAction extends Action {   

    Logger logger =Logger.getLogger("DbsLogger");
    Long documentId = null;     
    ExceptionBean exBean =null;    
    HttpSession httpSession= null;   
    
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      
      String target = new String("success");
      String notificationId=null; 
      String relativePath=null;
      UserInfo userInfo=null;
      String note=null;
      String itemType=null;
      String docUrl=null; 
      String itemKey=null;
      DbsLibrarySession dbsLibrarySession = null;
      ActionMessages actionMessages=null;
      ActionMessage actionMessage=null;      
      
      try{
          httpSession= request.getSession(false);
          logger.info("Entering NotificationDetailsAction now...");
          userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
          dbsLibrarySession = userInfo.getDbsLibrarySession();
          NotificationDetailsForm  notificationDetailsForm=new  NotificationDetailsForm();        
          String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
          relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
          notificationId=request.getParameter("notificationId");
          note=request.getParameter("note");
          itemType=request.getParameter("itemType");
          itemKey=request.getParameter("itemKey");
          logger.debug("Note : "+note);
          ApproveRejectNotification approveRejectNotification = new ApproveRejectNotification(relativePath,notificationId,userInfo.getUserID());
          if(request.getParameter("userAction").equals("APPROVE")){
            
            approveRejectNotification.approveRequisition(note,itemType,itemKey);           
            
            documentId = new Long(approveRejectNotification.docId);
            logger.debug("Document id in NotificationDetailsAction: "+documentId);
            
            /* logic for logging Wf approval event in doc attribute "AUDIT_LOG" */
            ChangeDocWfStatusBean docWfStatusBean = new ChangeDocWfStatusBean(relativePath,documentId,"Approved");
            docWfStatusBean.forwardTo=userInfo.getUserID();
            docWfStatusBean.logEvent("Doc Approved");
            docWfStatusBean.closeThisWfSession();
            
            actionMessages = new ActionMessages();
            actionMessage = new ActionMessage("msg.requisition.has.been.approved");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            httpSession.setAttribute("ActionMessages",actionMessages);                      
            
          }else if(request.getParameter("userAction").equals("REJECT")){
          
            approveRejectNotification.rejectRequisition(note,itemType,itemKey);
            
            documentId = new Long(approveRejectNotification.docId);
            
            logger.debug("Document id in NotificationDetailsAction: "+documentId);

            /* logic for logging Wf rejection event in doc attribute "AUDIT_LOG" */
            ChangeDocWfStatusBean docWfStatusBean = new ChangeDocWfStatusBean(relativePath,documentId,"Rejected");
            docWfStatusBean.forwardTo=userInfo.getUserID();
            docWfStatusBean.logEvent("Approval Requisition Rejected");
            docWfStatusBean.closeThisWfSession();
            
            actionMessages = new ActionMessages();
            actionMessage = new ActionMessage("msg.requisition.has.been.rejected");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            httpSession.setAttribute("ActionMessages",actionMessages);           
            
          }else if(request.getParameter("userAction").equals("CLOSE")){
            approveRejectNotification.closeRequisition();
            
            actionMessages = new ActionMessages();
            actionMessage = new ActionMessage("msg.requisition.has.been.closed");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            httpSession.setAttribute("ActionMessages",actionMessages);
            
          }
      }catch( Exception ex ){
        logger.debug("An Exception occurred in NotificationDetailsAction... ");
        logger.error(ex.toString());
        //target = new String("failure");
        exBean = new ExceptionBean(ex);
        httpSession.setAttribute("ActionErrors",exBean.getActionErrors());
        ex.printStackTrace();
      }
    logger.info("Exiting NotificationDetailsAction now...");
    return mapping.findForward("success");
  }
}