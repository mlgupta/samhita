
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
 * $Id: NotificationDetailsB4Action.java,v 1.11 2005/03/29 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.workflow;
/* dms package references */
import dms.web.actionforms.workflow.NotificationDetailsForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.wf.docApprove.GetNotificationDetails;
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
/**
 *	Purpose: To populate notification_details.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    03-05-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class NotificationDetailsB4Action extends Action  {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    
      Logger logger =Logger.getLogger("DbsLogger");
      String target = new String("success");
      String notificationKey=null;
      String notificationId=null;
      String itemType=null;
      String itemKey=null;
      String messageName=null;
      String status=null;
      HttpSession httpSession= null; 
      String relativePath=null;
      UserInfo userInfo=null;
      Boolean isApprover = new Boolean(false);      /* variable for setting "Approve","Reject".*/      
      try{
        logger.info("Entering NotificationDetailsB4Action now...");
        httpSession= request.getSession(false);
        userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
        NotificationDetailsForm  notificationDetailsForm=new  NotificationDetailsForm();        
        String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
        relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
        notificationKey=request.getParameter("radSelect");
        String notificationComponents[]=notificationKey.split(":");
        status=notificationComponents[0];
        messageName=notificationComponents[1];
        notificationId=notificationComponents[2];
        itemType=notificationComponents[3];
        itemKey=notificationComponents[4];
        logger.debug(messageName);
        GetNotificationDetails notificationDetails=new GetNotificationDetails(relativePath,notificationId);
        notificationDetails.fetchNotificationDetails();
        notificationDetailsForm.setTxtNotificationName(messageName);
        notificationDetailsForm.setTxtNotificationSent(userInfo.getUserID());
        notificationDetailsForm.setTxtNotificationSubject(notificationDetails.subject);
        notificationDetailsForm.setNotificationId(notificationId);
        notificationDetailsForm.setItemKey(itemKey);
        notificationDetailsForm.setItemType(itemType);
        notificationDetailsForm.setStatus(status);
        logger.debug("Notification Body: "+notificationDetails.body);
        notificationDetailsForm.setTxaNotificationComments(notificationDetails.body);
        if(messageName.equals("APPROVE_REQUISITION_REMINDER") || 
           messageName.equals("APPROVE_REQUISITION")){
          isApprover = new Boolean(true);
        }
        String docUrl = notificationDetails.docUrl;
        if((docUrl!=null) && (!docUrl.equals(""))){
          logger.debug("docUrl is not null in notificationDetailsB4Action");
          request.setAttribute("docUrl",docUrl);
        }else{
          logger.debug("docUrl is null/empty in notificationDetailsB4Action");          
        }
        request.setAttribute("status",status);  
        request.setAttribute("isApprover",isApprover);  
        request.setAttribute("NotificationDetailsForm",notificationDetailsForm);
      }catch( Exception ex ){
        logger.error("An Exception occurred in  NotificationDetailsB4Action...");
        logger.error(ex.toString());
        target = new String("failure");
        ex.printStackTrace();
      }
      logger.info("Exiting NotificationDetailsB4Action now...");
      return mapping.findForward(target); 
    
    }

}
