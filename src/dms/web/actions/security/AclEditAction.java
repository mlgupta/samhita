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
 * $Id: AclEditAction.java,v 20040220.20 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.AclNewEditForm;
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
 *	Purpose: To save acl_edit.jsp with the specified Acl data.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    11-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class AclEditAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
 /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing logger
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
    ArrayList aceList=null;
    boolean isEditException=false;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();        
    AclNewEditForm aclNewEditForm;
    try{ 
      logger.info("Entering AclEditAction now...");
      aclName=((String)PropertyUtils.getSimpleProperty(form, "txtAclName")).trim();
      logger.debug("Saving data for Edit Acl : " + aclName);            
      aclNewEditForm= (AclNewEditForm)form;
      description=((String)PropertyUtils.getSimpleProperty(form, "txaAclDescription")).trim();
      securingAclName=((String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList")).trim();
      ownerName=((String)PropertyUtils.getSimpleProperty(form, "txtOwnerName")).trim();
      httpSession = request.getSession(false);
      if(httpSession.getAttribute("aceList")!=null){
        request.setAttribute("aceList",httpSession.getAttribute("aceList"));
        httpSession.removeAttribute("aceList");
      }
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();

      newEditTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction started");
      aclNewEdit=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclName);         
      if(description!=null && (!description.equals(""))){
        aclNewEdit.setAttribute(DbsAccessControlList.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(description)); 
      }
      if(securingAclName!=null && (!securingAclName.equals(""))){
        DbsAccessControlList securingAcl=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,securingAclName);            
        aclNewEdit.setAcl(securingAcl); 
      }
      try{
        if(ownerName!=null && (!ownerName.equals(""))){
         aclNewEdit.setOwnerByName(ownerName); 
        }    
      }catch(DbsException editException){              
        if(editException.containsErrorCode(12200)){
          ActionError createError=new ActionError("errors.acl.12200",ownerName);       
          errors.add(ActionErrors.GLOBAL_ERROR,createError); 
          isEditException=true;
        }
        if(editException.getErrorCode() == 30041){
          ActionError createError=new ActionError("errors.30041.folderdoc.insufficient.access.updatePO");
          errors.add(ActionErrors.GLOBAL_ERROR,createError); 
          isEditException=true;  
        }                
        throw editException;
      }
      dbsLibrarySession.completeTransaction(newEditTransaction);
      logger.debug("aclNewEditForm : " + aclNewEditForm);
      logger.debug("Data saved for Acl : " + aclName);
      logger.debug("Transaction completed");
      newEditTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclEditAction... ");
      logger.error("Acl Edit Aborted.");
      logger.error(dbsException.toString());
      if(!isEditException){
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception){
      logger.error("An Exception occurred in AclEditAction... ");
      logger.error("Acl Edit Aborted.");
      logger.error(exception.toString());
      if(!isEditException){
        ActionError editError=new ActionError("errors.catchall",exception);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }finally{
      try{
        if(newEditTransaction!=null){
          dbsLibrarySession.abortTransaction(newEditTransaction);    
          logger.debug("Transaction aborted");                  
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in AclEditAction... ");
        logger.error(nestedException.getErrorMessage());
      }  
    }//end finally
    if (!errors.isEmpty()) {     
      saveErrors(request,errors);
      return mapping.getInputForward();
    }
    ActionMessages messages = new ActionMessages();      
    ActionMessage msg = new ActionMessage("acl.edit.ok",aclName);
    messages.add("message1", msg);
    httpSession.setAttribute("messages",messages);  
    logger.info("Exiting AclEditAction now...");
    return mapping.findForward("success");
  }
}
