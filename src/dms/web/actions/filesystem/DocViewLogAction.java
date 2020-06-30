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
 * $Id: DocViewLogAction.java,v 1.5 2006/03/01 12:22:17 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.web.actionforms.filesystem.DocLogListForm;
import dms.web.beans.filesystem.DocEventAttributeBean;
import dms.web.beans.filesystem.DocLogConstants;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
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
/** Purpose               : Action used to logging event in dbsDocument 
 *                          attribute "AUDIT_LOG"
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 15-02-2005
 * Last Modified by       : Suved Mishra
 * Last Modification Date : 01-03-2006
 */
public class DocViewLogAction extends Action  {
  
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    Logger logger = Logger.getLogger("DbsLogger");
    int numRecords=0;
    int pageNumber=1;
    int pageCount=0;
    Locale locale = getLocale(request);
    String fromDate=null;
    String toDate=null;
    String searchUser=null;
    String searchLogType=null;
    ArrayList eventArrayList = new ArrayList();
    try{
      logger.info("Entering DocViewLogAction now...");    
      DocLogListForm docLogListForm = new DocLogListForm();
      
      fromDate=request.getParameter("txtDocLogFromDate");
      if(fromDate==null){
        fromDate="";
        logger.debug("fromDate: "+fromDate);   
      }  
      logger.debug("fromDate: "+fromDate);
      
      toDate=request.getParameter("txtDocLogToDate");
      if(toDate==null){
        toDate="";
        logger.debug("toDate: "+toDate);
      }
      logger.debug("toDate: "+toDate);
      
      searchUser=request.getParameter("txtSearchByUserName");
      if(searchUser==null){
        searchUser="";
        logger.debug("searchUser: "+searchUser);
      }
      logger.debug("searchUser: "+searchUser);
      
      searchLogType=request.getParameter("cboSearchByLogType");
      if(searchLogType==null){
        searchLogType="";
        logger.debug("searchLogType: "+searchLogType);
      }
      logger.debug("searchLogType: "+searchLogType);
      
      Long docId=new Long(Long.parseLong(request.getParameter("chkFolderDocIds")));
      
      docLogListForm.setChkFolderDocIds(docId);
      
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
      numRecords = userPreferences.getRecordsPerPage();
      
      if(searchLogType.equals("")){
       searchLogType = DocLogConstants.Log_From_Samhita;
       docLogListForm.setCboSearchByLogType(searchLogType);
       logger.debug("searchLogType: "+searchLogType);
      }else{
        docLogListForm.setCboSearchByLogType(searchLogType);
      }
      
      DocEventAttributeBean docEventAttributeBean = new DocEventAttributeBean();
      
      eventArrayList = docEventAttributeBean.getEventLog(userInfo,docId,searchUser,searchLogType,fromDate,toDate);    
      
      String docName = docEventAttributeBean.getDocName();
      
      String docPath = docEventAttributeBean.getDocPath();
      
      request.setAttribute("eventArrayList",eventArrayList);
      logger.debug("eventArrayList.size: "+eventArrayList.size());
      logger.debug("numRecords: "+numRecords);
      int sizeOfArray = eventArrayList.size();
      logger.debug("txtPageCount: "+new Integer((int)StrictMath.ceil(((double)sizeOfArray/numRecords))).toString());
      docLogListForm.setTxtPageNo(new Integer(pageNumber).toString());
      docLogListForm.setTxtPageCount(new Integer((int)StrictMath.ceil(((double)sizeOfArray/numRecords))).toString());
      docLogListForm.setTxtDocLogFromDate(fromDate);
      docLogListForm.setTxtDocLogToDate(toDate);
      
      docLogListForm.setTxtSearchByUserName(searchUser);
      docLogListForm.setOperation(new String());
      docLogListForm.setTimezone(TimeZone.getDefault().getID());
      
      request.setAttribute("DocLogListForm",docLogListForm);
      request.setAttribute("docName",docName);
      request.setAttribute("docPath",docPath);
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.document.with.name",docName);
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      saveMessages(request,actionMessages);
      logger.debug("docLogListForm: "+(DocLogListForm)request.getAttribute("DocLogListForm"));
      logger.debug("Exiting DocViewLogAction...");
      
    }catch(Exception ex){
      logger.error("An Exception occurred in DocViewLogAction... ");
      logger.error(ex.toString());
    }
    logger.info("Exiting DocViewLogAction now...");    
    return mapping.findForward("success");
  }

}
