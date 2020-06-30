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
 * $Id: MyNotificationB4Action.java,v 1.9 2005/03/29 04:41:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.workflow;
/* dms package references */
import dms.web.actionforms.workflow.MyNotificationListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.wf.docApprove.GetNotifications;
import dms.web.beans.wf.docApprove.NotificationListBean;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To populate mynotifications.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    03-05-2005
 * 	Last Modified by :   Suved Mishra     
 * 	Last Modified Date:  03-03-2006  
 */
public class MyNotificationB4Action extends Action  {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    private static String DATE_FORMAT="MM/dd/yyyy"; //"MM/dd/yyyy HH:mm:ss z"; //also present in JobListBean
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

      Logger logger =Logger.getLogger("DbsLogger");
      String target = new String("success");
      UserInfo userInfo=null;
      HttpSession httpSession=null;
      String loggedInUser=null;
      String relPath=null;
      int numRecords=0;
      int pageNumber=1;
      int pageCount=0;
      GetNotifications notifications=null;
      ArrayList notificationList=null;
      ExceptionBean exBean=null;
      
      try{
        logger.info("Entering MyNotificationB4Action now...");
        httpSession=request.getSession(false);
        userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
        String status=null;
        MyNotificationListForm myForm = new MyNotificationListForm();
        if(request.getParameter("cboStatus")==null || request.getParameter("cboStatus").equals("")){
          status= "OPEN";
          myForm.setCboStatus((new String[]{status}));
        }else{
          status=request.getParameter("cboStatus");
          myForm.setCboStatus((new String[]{status}));
        }
        
        String fromBeginDate=request.getParameter("txtSearchBeginDateFrom");
        String toBeginDate=request.getParameter("txtSearchBeginDateTo");
        String fromEndDate=request.getParameter("txtSearchEndDateFrom");
        String toEndDate=request.getParameter("txtSearchEndDateTo");
        loggedInUser=userInfo.getUserID();
        relPath=request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
        notifications=new GetNotifications(relPath);
        UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
            numRecords= userPreferences.getRecordsPerPage();
         if(request.getParameter("txtPageNo")!=null){
                pageNumber=Integer.parseInt(request.getParameter("txtPageNo").toString());                
         }
        notificationList=notifications.getNotificationData(loggedInUser,status,fromBeginDate,toBeginDate,fromEndDate,toEndDate,pageNumber, numRecords);
        pageCount=notifications.pageCount;        
        if(pageNumber>pageCount){
             pageNumber=pageCount;
        }
        ArrayList notificationsList=new ArrayList();
        for(int index=0;index < notificationList.size();index++){
              NotificationListBean notificationBean=new NotificationListBean();
              Hashtable notification=(Hashtable)notificationList.get(index);
              String notificationKey=(String)notification.get("STATUS")+":"+(String)notification.get("MESSAGE_NAME")+":"+((BigDecimal)notification.get("NOTIFICATION_ID")).toString()+":"+(String)notification.get("CONTEXT");
              notificationBean.setNotificationId(notificationKey);
              notificationBean.setSubject((String)notification.get("SUBJECT"));
              notificationBean.setStatus((String)notification.get("STATUS"));
              notificationBean.setBeginDate(DateHelper.format((Date)notification.get("BEGIN_DATE"),DATE_FORMAT));
              if(notification.containsKey("END_DATE")){
                notificationBean.setEndDate(DateHelper.format((Date)notification.get("END_DATE"),DATE_FORMAT));  
              }else{
                notificationBean.setEndDate("");
              }
              notificationsList.add(notificationBean);
        }
         if(request.getParameter("txtSearchBeginDateFrom")!=null){
            myForm.setTxtSearchBeginDateFrom(request.getParameter("txtSearchBeginDateFrom"));
         }
         if(request.getParameter("txtSearchBeginDateTo")!=null){
            myForm.setTxtSearchBeginDateTo(request.getParameter("txtSearchBeginDateTo"));
         }
         if(request.getParameter("txtSearchEndDateFrom")!=null){
            myForm.setTxtSearchEndDateFrom(request.getParameter("txtSearchEndDateFrom"));
         }
         if(request.getParameter("txtSearchEndDateTo")!=null){
            myForm.setTxtSearchEndDateTo(request.getParameter("txtSearchEndDateTo"));
         }
        
        myForm.setTxtPageCount(new String().valueOf(pageCount));
        logger.debug("pageCount : " + pageCount);                        
        myForm.setTxtPageNo(new String().valueOf(pageNumber));
        logger.debug("pageNumber : " + pageNumber);             
        request.setAttribute("status",status);
        request.setAttribute("notificationsList",notificationsList);
        request.setAttribute("MyNotificationListForm",myForm);
        logger.debug("myForm: "+myForm);
        
        ActionMessages actionMessages = (ActionMessages)httpSession.getAttribute("ActionMessages");
        if(actionMessages!=null){
          logger.debug("Saving action messages in request stream");
          saveMessages(request,actionMessages);
          httpSession.removeAttribute("ActionMessages");
          }
        ActionErrors actionErrors=(ActionErrors)httpSession.getAttribute("ActionErrors");
        if(actionErrors!=null){
          logger.debug("Saving action errors in request stream");
          saveErrors(request,actionErrors);
          httpSession.removeAttribute("ActionErrors");
        }
        
      }catch( Exception ex ){
        logger.debug("An Exception occurred in MyNotificationB4Action... ");
        logger.error(ex.toString());
        exBean = new ExceptionBean(ex);
        saveErrors(request,exBean.getActionErrors());
        //target = new String("failure");
        ex.printStackTrace();
      }
      logger.info("Exiting MyNotificationB4Action now...");
      return mapping.findForward(target);
    }

}