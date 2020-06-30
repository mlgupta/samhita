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
 * $Id: AclNewAction.java,v 20040220.19 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAccessControlListDefinition;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.AclNewEditForm;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
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
 *	Purpose: To save acl_new.jsp with the specified Acl data.
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    12-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class AclNewAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing Logger
    Logger logger = Logger.getLogger("DbsLogger");
    Locale locale = getLocale(request);
    //variable declaration
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    DbsTransaction newEditTransaction=null;       
    String aclName=null;
    String description=null;
    String securingAclName=null;
    String ownerName=null; 
    DbsAccessControlList aclNewEdit=null;
    boolean isCreateException=false;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    AclNewEditForm aclNewEditForm;
    try{ 
      logger.info("Entering AclNewAction now...");
      aclName=((String)PropertyUtils.getSimpleProperty(form, "txtAclName")).trim();
      logger.debug("Saving data for New Acl : " + aclName);
      aclNewEditForm=(AclNewEditForm) form; 
      description=((String)PropertyUtils.getSimpleProperty(form, "txaAclDescription")).trim();
      securingAclName=((String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList")).trim();
      ownerName=((String)PropertyUtils.getSimpleProperty(form, "txtOwnerName")).trim();
      httpSession = request.getSession(false);
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();

      newEditTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      try{
        DbsAccessControlListDefinition aclDef = new DbsAccessControlListDefinition(dbsLibrarySession);
        aclDef.setAttribute(DbsAccessControlList.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(aclName));
        aclNewEdit = dbsLibrarySession.createPublicObject(aclDef);

        if(securingAclName!=null && (!securingAclName.equals(""))){
          DbsAccessControlList securingAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,securingAclName);
          aclNewEdit.setAcl(securingAcl);
        }
        if(ownerName!=null && (!ownerName.equals(""))){
          aclNewEdit.setOwnerByName(ownerName);
        }
      }catch(DbsException createException){
        logger.error(createException.getErrorMessage());
        if(createException.containsErrorCode(30010)){
          ActionError createError=new ActionError("errors.acl.30010",aclName);
          errors.add(ActionErrors.GLOBAL_ERROR,createError);
          isCreateException=true;
        }else if(createException.containsErrorCode(12200)){
          ActionError createError=new ActionError("errors.acl.12200",ownerName);
          errors.add(ActionErrors.GLOBAL_ERROR,createError);
          isCreateException=true;
        }
        throw createException;
      }
      logger.debug("description " + description);
      if(description!=null && (!description.equals(""))){
        aclNewEdit.setAttribute(DbsAccessControlList.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(description));
      }
      dbsLibrarySession.completeTransaction(newEditTransaction);
      logger.debug("aclNewEditForm " + aclNewEditForm);
      logger.debug("Transaction completed");
      logger.debug("Data saved for New Acl : " + aclName);                        
      newEditTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclNewAction... ");
      logger.error("Acl New Action Aborted.");          
      logger.error(dbsException.toString());
      if(isCreateException==false){
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in AclNewAction... ");
      logger.error("Acl New Action Aborted.");          
      logger.error(exception.toString());
      if(isCreateException==false){
        ActionError editError=new ActionError("errors.catchall",exception);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }finally{
      try{
        if(newEditTransaction!=null){
          dbsLibrarySession.abortTransaction(newEditTransaction);
          logger.debug("Transaction aborted.");
        }
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in AclNewAction... ");
        logger.error(nestedException.toString());
      }
    }//end finally
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    ActionMessages messages = new ActionMessages();
    ActionMessage msg = new ActionMessage("acl.edit.ok",aclName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);
    logger.info("Exiting AclNewAction now...");
    return mapping.findForward("success");
  }
}
