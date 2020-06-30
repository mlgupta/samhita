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
 * $Id: UserListAction.java,v 20040220.16 2006/03/03 10:51:26 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user;
/*dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.web.actionforms.user.UserListForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserListBean;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.DateUtil;
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
 *	Purpose: To populate user_list.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 03-03-2006   
 */
public class UserListAction extends Action {
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
        logger.debug("Fetching User List ...");
        UserInfo userInfo = null;
        HttpSession httpSession = null;
        DbsDirectoryUser[] userList=null;          
        int numRecords=0;
        int pageNumber=1;
        int pageCount=0;
        Locale locale = getLocale(request);

        // Validate the request parameters specified by the user
        UserListForm userForm=new UserListForm();    
        ActionErrors errors = new ActionErrors();
        try{        
            logger.info("Entering UserListAction now...");
            httpSession = request.getSession(false);
            userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
            numRecords= userPreferences.getRecordsPerPage();
            if(httpSession.getAttribute("pageNumber")!=null){
                if(!httpSession.getAttribute("pageNumber").toString().trim().equals("")){
                    pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString().trim());
                }
                httpSession.removeAttribute("pageNumber");
            }
            userForm=new UserListForm();
            dbsLibrarySession = userInfo.getDbsLibrarySession();
            String singleUserName=(String)httpSession.getAttribute("txtSearchByUserName");        
            if(httpSession.getAttribute("messages")!=null){
                logger.debug("Saving action message in request stream");
                saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
                httpSession.removeAttribute("messages");
            } 
            if(httpSession.getAttribute("errors")!=null){
                logger.debug("Saving action errors in request stream");
                saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
                httpSession.removeAttribute("errors");
            }
            ArrayList users=new ArrayList();
            if(singleUserName!=null && !singleUserName.equals("")) {
                logger.debug("User To Find:"+ singleUserName);
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

                if(userList[index].getName().equalsIgnoreCase("system") || userList[index].getName().equalsIgnoreCase("logger")){
                    userListBean[index].setIsDisabled("true");
                }else {
                    userListBean[index].setIsDisabled("false");
                }

                userListBean[index].setName(userList[index].getName());
                userListBean[index].setOwner(userList[index].getOwner().getDistinguishedName());
                userListBean[index].setCreateDate(DateUtil.getFormattedDate(userList[index].getCreateDate()));
                userListBean[index].setAcl(userList[index].getAcl().getName());
                if(userList[index].isSystemAdminEnabled()){
                    userListBean[index].setStatus("System Admin");
                }else if(userList[index].isAdminEnabled()){
                    userListBean[index].setStatus("Admin");        
                }else{
                    userListBean[index].setStatus("Non-Admin");
                }
                if(dbsLibrarySession.getUser().isAdminEnabled()){
                    DbsPrimaryUserProfile userProfile=userList[index].getPrimaryUserProfile();
                    if(userProfile.getContentQuota()!=null && userProfile.getContentQuota().isEnabled()){
                        long quota=userProfile.getContentQuota().getAllocatedStorage();
                        quota=quota/(1024*1024);
                        userListBean[index].setQuota(new String().valueOf(quota));              
                    }else{
                        userListBean[index].setQuota("Unlimited");
                    }
                    ArrayList groups=new ArrayList();
                    for(int groupIndex=0 ; groupIndex < groupList.length ; groupIndex++){
                        if(groupList[groupIndex].isDirectMember(userList[index])){
                            groups.add(groupList[groupIndex].getName());
                        }
                    }
                    String [] memberGroups=new String[groups.size()];
                    for(int memberIndex=0;memberIndex<groups.size();memberIndex++){
                        memberGroups[memberIndex]=(String)groups.get(memberIndex);
                    }
                    userListBean[index].setGroup(memberGroups);
                }
                users.add(userListBean[index]);    
            }
            request.setAttribute("users",users);
            request.setAttribute("UserListForm",userForm);  
            logger.debug("Number of records fetched : "+userList.length);
            logger.debug("Fetching User List Complete");
        }catch(DbsException dbsException){
            logger.error("An Exception occurred in UserListAction... ");
            logger.error("Fetching User List Aborted");  
            ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }catch(Exception exception){
            logger.error("An Exception occurred in UserListAction... ");
            logger.error("Fetching User List Aborted");          
            ActionError editError=new ActionError("errors.catchall",exception);
            errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }
        if(!errors.isEmpty()) {      
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        logger.info("Exiting UserListAction now...");
        return mapping.findForward("success");
    }
}
