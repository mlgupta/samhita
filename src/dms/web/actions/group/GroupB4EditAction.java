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
 * $Id: GroupB4EditAction.java,v 20040220.16 2006/03/13 14:06:27 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.group; 
/* dms package references */
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryObject;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.group.GroupNewEditForm;
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
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To populate group_edit.jsp with the specified group's data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class GroupB4EditAction extends Action {
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
    logger.info("Entering GroupB4EditAction now...");
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    // Validate the request parameters specified by the user
    ArrayList memberList=new ArrayList();
    ActionErrors errors = new ActionErrors();
    try{
      httpSession = request.getSession(false);  
      String groupName= (String)httpSession.getAttribute("radSelect");
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      logger.info("Fetching data for group : " + groupName);
      DbsDirectoryGroup groupToEdit=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groupName);
      if (groupToEdit!=null) {
        if(!(groupToEdit.getDirectMembers()==null)){
          DbsDirectoryObject[] dirObj=groupToEdit.getDirectMembers();    
          for(int index=0;index < groupToEdit.getDirectMembers().length; index++) {
            if(dirObj[index] instanceof DbsDirectoryUser){          
              memberList.add(dirObj[index].getName()+" [U]");         
            }
            if (dirObj[index] instanceof DbsDirectoryGroup){
              if(!dirObj[index].getName().equals(groupToEdit.getName())){
                memberList.add(dirObj[index].getName()+" [G]");
              }
            }
          }
        }
        String[] groupMembers=new String[memberList.size()];
        for(int index=0;index < memberList.size() ; index++){
          groupMembers[index]=(String)memberList.get(index);        
        }
        GroupNewEditForm groupNewEditForm=new GroupNewEditForm();
        groupNewEditForm.setTxtGroupName(groupToEdit.getName());
        groupNewEditForm.setTxaDescription(groupToEdit.getAttribute(DbsDirectoryGroup.DESCRIPTION_ATTRIBUTE).toString());
        groupNewEditForm.setTxtAccessControlList(groupToEdit.getAcl().getName());
        groupNewEditForm.setLstGroupOrUserList(groupMembers);
        
        logger.debug("groupNewEditForm : " + groupNewEditForm);
        logger.info("Fetching data for group : " + groupName);
        request.setAttribute("groupNewEditForm",groupNewEditForm);
        logger.info("Fetching data for group "+groupName+" completed. ");
      }else{
        ActionError editError=new ActionError("errors.group.notfound",groupName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);            
        httpSession.setAttribute("errors",errors);
        return mapping.findForward("failure");
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in GroupB4EditAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
      logger.info("Fetching data for group Aborted");  
    }catch(Exception exception){
      logger.error("An Exception occurred in GroupB4EditAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
      logger.info("Fetching data for group Aborted");          
    }
    logger.info("Exiting GroupB4EditAction now...");
    return mapping.findForward("success");
  }
}
