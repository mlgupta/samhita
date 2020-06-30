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
 * $Id: UserListSelectAction.java,v 20040220.11 2006/03/13 14:17:25 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user; 
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.user.UserListSelectForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserListBean;
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
 *	Purpose: To populate user_list_select.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 03-03-2006   
 */
public class UserListSelectAction extends Action {
    DbsLibraryService dbsLibraryService = null;
    DbsCleartextCredential dbsCleartextCredential = null;
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

        Locale locale = getLocale(request);  	
        UserInfo userInfo = null;
        HttpSession httpSession = null;
        DbsDirectoryUser[] userList=null;
        String control=null;
        int numRecords=0;
        int pageNumber=1;
        int pageCount=0;

        // Validate the request parameters specified by the user
        UserListSelectForm userForm=new UserListSelectForm(); 
        ActionErrors errors = new ActionErrors();
        try{
            logger.info("Entering UserListSelectAction now...");
            logger.debug("Fetching User List ...");
            httpSession = request.getSession(false);
            logger.debug(httpSession);
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
            ArrayList users=new ArrayList();
            String singleUserName=(String)httpSession.getAttribute("txtSearchByUserName");
            if(singleUserName!=null && !singleUserName.equals("")) {
                logger.debug("User To Find"+ singleUserName);
                pageCount=SearchUtil.listUsersPageCount(dbsLibrarySession,singleUserName,numRecords);
                if(pageNumber>pageCount){
                    pageNumber=pageCount;
                }
                userList= SearchUtil.listUsers(dbsLibrarySession,singleUserName,pageNumber,numRecords);            
                userForm.setTxtSearchByUserName((String)httpSession.getAttribute("txtSearchByUserName"));
                httpSession.removeAttribute("txtSearchByUserName");
                if(userList.length==0){
                    ActionMessages messages = new ActionMessages();
                    ActionMessage msg = new ActionMessage("search.notFound",singleUserName);
                    messages.add("message1", msg);
                    saveMessages(request,messages);
                    userForm.setTxtSearchByUserName("");
                }
            }else{               
                pageCount=SearchUtil.listUsersPageCount(dbsLibrarySession,null,numRecords);
                if(pageNumber>pageCount){
                    pageNumber=pageCount;
                }
                userList= SearchUtil.listUsers(dbsLibrarySession,null,pageNumber,numRecords);    
            }//end else
            userForm.setTxtPageCount(new String().valueOf(pageCount));
            logger.debug("pageCount : " + pageCount);                        
            userForm.setTxtPageNo(new String().valueOf(pageNumber));    
            logger.debug("pageNumber : " + pageNumber);                        
            DbsDirectoryGroup[] groupList=SearchUtil.listGroups(dbsLibrarySession);
            UserListBean[] userListBean=new UserListBean[userList.length];
            for(int index=0;index < userList.length ; index ++){
                userListBean[index]=new UserListBean();
                userListBean[index].setName(userList[index].getName());
                if(userList[index].isSystemAdminEnabled()){
                    userListBean[index].setStatus("System Admin");
                }else if(userList[index].isAdminEnabled()){
                    userListBean[index].setStatus("Admin");        
                }else{
                    userListBean[index].setStatus("Non-Admin");
                }
                users.add(userListBean[index]);    
            } 
            request.setAttribute("users",users);
            request.setAttribute("UserListSelectForm",userForm);
            logger.debug("Number of records fetched : "+userList.length);
            logger.debug("Fetching User List Complete");
        }catch(DbsException dbsException){
            logger.error("An Exception occurred in UserListSelectAction... ");
            logger.error("Fetching User List Aborted"); 
            logger.error(dbsException.toString());
            ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }catch(Exception exception){
            logger.error("An Exception occurred in UserListSelectAction... ");
            logger.error("Fetching User List Aborted"); 
            logger.error(exception.toString());
            ActionError editError=new ActionError("errors.catchall",exception);
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }
        if(!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        logger.info("Exiting UserListSelectAction now...");
        return mapping.findForward("success");
    }
}
