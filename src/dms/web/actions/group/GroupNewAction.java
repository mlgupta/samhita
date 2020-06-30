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
 * $Id: GroupNewAction.java,v 20040220.14 2006/03/13 14:06:27 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.group; 
// dms package references
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryGroupDefinition;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.actionforms.group.GroupNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.commons.beanutils.PropertyUtils;
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
 *	Purpose: To save group_new.jsp with the specified group's data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class GroupNewAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  DbsTransaction newTransaction=null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException {
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering GroupNewAction now...");
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    String groupName=null;
    String accessControlListName=null;
    String groupDescription=null;
    String[] usersOrGroups=null;
    DbsDirectoryGroup newGroup=null;
    GroupNewEditForm groupNewEditForm;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{
      groupName = (String)PropertyUtils.getSimpleProperty(form, "txtGroupName");
      groupNewEditForm=(GroupNewEditForm)form;
      logger.debug("Saving data for new group : " + groupName);
      accessControlListName=(String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList");
      usersOrGroups=(String[])PropertyUtils.getSimpleProperty(form,"lstGroupOrUserList");
      groupDescription=(String)PropertyUtils.getSimpleProperty(form, "txaDescription"); 
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      newTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("groupNewEditForm " + groupNewEditForm);
      logger.debug("Transaction started");
      newGroup= createGroup(groupName,accessControlListName);
      newGroup.setAttribute(DbsDirectoryGroup.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(groupDescription));
      if(usersOrGroups!=null){
        for(int index=0;index < usersOrGroups.length ; index++){
          if(usersOrGroups[index].endsWith("[U]")){                 
            DbsDirectoryUser newMember=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,usersOrGroups[index].substring(0,(usersOrGroups[index].length()-4)));
            try{
              newGroup.addMember(newMember);
              logger.debug("User "+newMember.getName()+" added");
            }catch(DbsException dbsException){
              logger.error("An Exception occurred in GroupNewAction... ");
              logger.error("User "+newMember.getName()+" already present");
              logger.error(dbsException.toString());
            }    
          }else if(usersOrGroups[index].endsWith("[G]")){
            DbsDirectoryGroup newMember=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,usersOrGroups[index].substring(0,(usersOrGroups[index].length()-4)));
            try{
              if(!(newGroup.getName()+" [G]").equals(newMember.getName())){
                newGroup.addMember(newMember);
                logger.debug("Group "+newMember.getName()+" added");
              }
            }catch(DbsException dbsException){
              logger.error("Group "+newMember.getName()+" already present");
            }
          }
        }
      }
      dbsLibrarySession.completeTransaction(newTransaction);
      newTransaction=null;
      logger.debug("Transaction completed");
      logger.debug("Data saved for group : " + groupName);            
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in GroupNewAction... ");
      logger.error(dbsException.toString());
      if(dbsException.containsErrorCode(30002)){
        ActionError createError=new ActionError("errors.group.unique");       
        errors.add(ActionErrors.GLOBAL_ERROR,createError);       
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);  
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in GroupNewAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(newTransaction!=null){
          logger.debug("Going to abort transaction");
          dbsLibrarySession.abortTransaction(newTransaction);
          logger.debug("Transaction aborted from group");
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in GroupNewAction... ");
        logger.error(nestedException.toString());
      }  
    }//end finally
    if (!errors.isEmpty()) {
      logger.debug("Group could not be created");
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    ActionMessages messages = new ActionMessages();
    ActionMessage msg = new ActionMessage("group.new.ok",groupName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);
    logger.info("Exiting GroupNewAction now...");
    return mapping.findForward("success");
  }

//--------------These Function List To be Removed Later -----------------------//

  public DbsDirectoryGroup createGroup(String groupname,String aclname) throws DbsException {
    DbsDirectoryGroup dbsGroup =null;
    try{
      DbsDirectoryGroupDefinition dbsGroupDefaultDef = new DbsDirectoryGroupDefinition(dbsLibrarySession);
      dbsGroupDefaultDef.setAttribute(DbsDirectoryGroup.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(groupname));
      DbsAccessControlList sharedAcl=(DbsAccessControlList)(SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclname));
      dbsGroupDefaultDef.setAttribute(DbsAccessControlList.ACL_ATTRIBUTE,DbsAttributeValue.newAttributeValue(sharedAcl));
      dbsGroup=(DbsDirectoryGroup)dbsLibrarySession.createPublicObject(dbsGroupDefaultDef);
    }catch (DbsException dbsException) {
      throw dbsException;
    }
    return dbsGroup;
  }
} //EOF
