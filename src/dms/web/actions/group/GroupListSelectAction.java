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
 * $Id: GroupListSelectAction.java,v 20040220.14 2006/03/13 14:06:27 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.group; 
// dms package references
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.group.GroupListSelectForm;
import dms.web.beans.group.GroupListBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
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
 *	Purpose: To populate group_list_select.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class GroupListSelectAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
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
    logger.info("Entering GroupListSelectAction now...");
    Locale locale = getLocale(request);       
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    DbsDirectoryGroup[] groupList=null; 
    String control=null;
    int numRecords=0;
    int pageNumber=1;
    int pageCount=0;

    // Validate the request parameters specified by the user
    GroupListSelectForm groupForm=new GroupListSelectForm();
    ActionErrors errors = new ActionErrors();
    try{
      logger.debug("Fetching Group List ...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
      numRecords= userPreferences.getRecordsPerPage();
      if(httpSession.getAttribute("pageNumber")!=null){
        pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
        httpSession.removeAttribute("pageNumber");
      }
      if(httpSession.getAttribute("control")!=null){
        control=(String)httpSession.getAttribute("control");
        request.setAttribute("control",control);
        httpSession.removeAttribute("control");
      }
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      String singleGroupName=(String)httpSession.getAttribute("txtSearchByGroupName");
      ArrayList groups=new ArrayList();
      if(singleGroupName!=null && !singleGroupName.equals("")) {
        logger.debug("Group To Find"+ singleGroupName);
        pageCount=SearchUtil.listGroupsPageCount(dbsLibrarySession,singleGroupName,numRecords);
        if(pageNumber>pageCount){
          pageNumber=pageCount;
        }
        groupList= SearchUtil.listGroups(dbsLibrarySession,singleGroupName,pageNumber,numRecords);            
        groupForm.setTxtSearchByGroupName((String)httpSession.getAttribute("txtSearchByGroupName"));
        httpSession.removeAttribute("txtSearchByGroupName");
        if(groupList.length==0){
          ActionMessages messages = new ActionMessages();
          ActionMessage msg = new ActionMessage("search.notFound",singleGroupName);
          messages.add("message1", msg);
          saveMessages(request,messages);
          groupForm.setTxtSearchByGroupName("");
        }
      }else{
        pageCount=SearchUtil.listGroupsPageCount(dbsLibrarySession,null,numRecords);
        if(pageNumber>pageCount){
          pageNumber=pageCount;
        }
        groupList= SearchUtil.listGroups(dbsLibrarySession,null,pageNumber,numRecords);
      }//end else
      groupForm.setTxtPageCount(new String().valueOf(pageCount));
      logger.debug("pageCount : " + pageCount);            
      groupForm.setTxtPageNo(new String().valueOf(pageNumber));
      logger.debug("pageNumber : " + pageNumber);
      GroupListBean[] groupListBean=new GroupListBean[groupList.length];           
      for(int index=0;index < groupList.length ; index ++){
        groupListBean[index]=new GroupListBean();
        groupListBean[index].setName(groupList[index].getName());
        groups.add(groupListBean[index]);            
      }
      request.setAttribute("groups",groups);
      request.setAttribute("GroupListSelectForm",groupForm);
      logger.debug("Number of records fetched : "+groupList.length);
      logger.debug("Fetching Group List Complete");
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in GroupListSelectAction... ");
      logger.error(dbsException.toString());
      logger.debug("Fetching Group List Aborted");  
      logger.debug(dbsException.getErrorMessage());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in GroupListSelectAction... ");
      logger.error(exception.toString());
      logger.debug("Fetching Group List Aborted");  
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    logger.info("Exiting GroupListSelectAction now...");
    return mapping.findForward("success");
  }
}
