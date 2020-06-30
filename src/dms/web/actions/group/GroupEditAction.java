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
 * $Id: GroupEditAction.java,v 20040220.20 2006/03/13 14:06:27 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.group; 
// dms package references
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryGroupDefinition;
import dms.beans.DbsDirectoryObject;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.actionforms.group.GroupNewEditForm;
import dms.web.beans.exception.ExceptionBean;
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
 *	Purpose: To save group_edit.jsp with the specified group's data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 02-03-2006   
 */
public class GroupEditAction extends Action {
  DbsLibraryService dbsLibraryService = null;
  DbsCleartextCredential dbsCleartextCredential = null;
  DbsLibrarySession dbsLibrarySession = null;
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException {
    Locale locale = getLocale(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    String groupName=null;
    String accessControlListName=null;
    String groupDescription=null;
    DbsTransaction editTransaction=null;
    String[] usersOrGroups=null;
    DbsDirectoryGroup newGroup=null;
    DbsDirectoryObject newMember=null;
    DbsAccessControlList aclToApply=null;
    DbsDirectoryGroup groupToEdit=null;
    GroupNewEditForm groupNewEditForm;
    int radQuota;
    long txtQuota = 0;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try{ 
      logger.info("Entering GroupEditAction now...");
      groupName = (String)PropertyUtils.getSimpleProperty(form, "txtGroupName");  
      groupNewEditForm=(GroupNewEditForm)form;
      logger.debug("groupNewEditForm : " + groupNewEditForm);
      logger.debug("Saving data for edit group : " + groupName);            
      logger.debug("Initializing Variable ...");        
      accessControlListName=(String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList");  
      groupDescription=(String)PropertyUtils.getSimpleProperty(form, "txaDescription"); 
      usersOrGroups=(String[])PropertyUtils.getSimpleProperty(form, "lstGroupOrUserList");
      
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      editTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      logger.debug("groupNewEditForm " + groupNewEditForm);
      groupToEdit=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,groupName);
      aclToApply=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,accessControlListName);
      groupToEdit.setAttribute(DbsDirectoryGroup.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(groupDescription));        
      groupToEdit.setAcl(aclToApply);
      groupToEdit.removeDirectMembers();
      if(usersOrGroups!=null){
        for(int index=0;index < usersOrGroups.length ; index++){
          if(usersOrGroups[index].endsWith("[U]")){
            newMember=(DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,usersOrGroups[index].substring(0,(usersOrGroups[index].length()-4)));
            groupToEdit.addMember((DbsDirectoryUser)newMember);
            logger.debug("User "+newMember.getName()+" added");             
          }else if(usersOrGroups[index].endsWith("[G]")){
            newMember=(DbsDirectoryGroup)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryGroup.CLASS_NAME,usersOrGroups[index].substring(0,(usersOrGroups[index].length()-4)));            
            if(!(groupToEdit.getName()+" [G]").equals(usersOrGroups[index])){
              groupToEdit.addMember((DbsDirectoryGroup)newMember);            
              logger.debug("Group "+newMember.getName()+" added");
            }
          }
        }        
      }//end if
      dbsLibrarySession.completeTransaction(editTransaction);
      logger.debug("Data saved for edit group : " + groupName);
      logger.debug("Transaction completed");
      editTransaction=null;
    }catch(DbsException dbsException){
      try{  
        logger.error("An Exception occurred in GroupEditAction...");
        logger.error("Error adding member: "+newMember.getName());
        logger.error(dbsException.toString());
        ExceptionBean exceptionBean = new ExceptionBean(dbsException);
        if(dbsException.containsErrorCode(10013)){
          ActionError editError=new ActionError("errors.group.10013",newMember.getName());       
          errors.add(ActionErrors.GLOBAL_ERROR,editError);                                    
        }else if(dbsException.containsErrorCode(10021)){
          ActionError editError=new ActionError("errors.group.10021",newMember.getName(),groupToEdit.getName());       
          errors.add(ActionErrors.GLOBAL_ERROR,editError);                                    
        }else{
          ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());       
          errors.add(ActionErrors.GLOBAL_ERROR,editError);
        }
      }catch(DbsException nestedException){
        logger.error(nestedException.getErrorMessage());
      }  
    }catch(Exception exception){
      logger.error("An Exception occurred in GroupEditAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);       
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(editTransaction!=null){
          logger.debug("Aborting transaction ...");
          dbsLibrarySession.abortTransaction(editTransaction);
          logger.debug("Transaction aborted from group");
        }       
      }catch(DbsException nestedException){
        logger.error(nestedException.getErrorMessage());
      }  
    }//end finally
    if(!errors.isEmpty()) {
      logger.debug("Group could not be edited");
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    ActionMessages messages = new ActionMessages();
    ActionMessage msg = new ActionMessage("group.edit.ok",groupName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);
    logger.info("Exiting GroupEditAction now...");
    return mapping.findForward("success");
  }

//--------------These Functions  To be Removed Later -----------------------//

  public DbsDirectoryGroup createGroup(String groupname,String aclname)throws DbsException{
    DbsDirectoryGroup dbsGroup =null;
    try{
      DbsDirectoryGroupDefinition dbsGroupDefaultDef = new DbsDirectoryGroupDefinition(dbsLibrarySession);
      dbsGroupDefaultDef.setAttribute(DbsDirectoryGroup.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(groupname));
      DbsAccessControlList sharedAcl=(DbsAccessControlList)(SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclname));
      logger.debug("ACL "+aclname+" aquired");
      dbsGroupDefaultDef.setAttribute(DbsAccessControlList.ACL_ATTRIBUTE,DbsAttributeValue.newAttributeValue(sharedAcl));
      dbsGroup=(DbsDirectoryGroup)dbsLibrarySession.createPublicObject(dbsGroupDefaultDef);
      logger.debug("Group "+groupname+" Created");
    }catch (DbsException dbsException){
      throw dbsException;
    }
    return dbsGroup;
  }
} //EOF
