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
 * $Id: PermissionB4NewEditAction.java,v 20040220.13 2006/03/13 14:16:02 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security;
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAccessControlListDefinition;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPermissionBundle;
import dms.beans.DbsTransaction;
import dms.web.actionforms.security.PermissionForm;
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
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To populate permission.jsp with the default data.
 *  @author              Maneesh Mishra
 *  @version             1.0
 * 	Date of creation:    16-02-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class PermissionB4NewEditAction extends Action {
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
    //initializing variables
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    
    String granteeType=null;
    String granteeName=null;
    String aclName=null;
    String securingAcl=null;
    String description=null;
    DbsAccessControlList aclNewEdit=null;        
    DbsAccessControlList aclToApply=null;
    String ownerName=null;
    String isNewAcl=null;
    DbsTransaction newEditTransaction=null;
    boolean isEditException=false;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    String permissionBundleName = null;
    try{
      logger.info("Entering PermissionB4NewEditAction now...");
      granteeType = (String)PropertyUtils.getSimpleProperty(form, "cboUserGroup");  
      granteeName = (String)PropertyUtils.getSimpleProperty(form, "txtAddUserGroupAcl"); 
      aclName=((String)PropertyUtils.getSimpleProperty(form, "txtAclName")).trim();
      description=((String)PropertyUtils.getSimpleProperty(form, "txaAclDescription")).trim();
      httpSession = request.getSession(false);            
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      newEditTransaction=dbsLibrarySession.beginTransaction();
      isNewAcl=request.getParameter("isNewAcl");
      if(isNewAcl.equals("false")){
        aclNewEdit=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclName);
      }else{   
        try{
          DbsAccessControlListDefinition aclDef = new DbsAccessControlListDefinition(dbsLibrarySession);
          aclDef.setAttribute(DbsAccessControlList.NAME_ATTRIBUTE,DbsAttributeValue.newAttributeValue(aclName));
          aclDef.setAttribute(DbsAccessControlList.DESCRIPTION_ATTRIBUTE,DbsAttributeValue.newAttributeValue(description));                    
          aclNewEdit = dbsLibrarySession.createPublicObject(aclDef);
          ownerName=((String)PropertyUtils.getSimpleProperty(form, "txtOwnerName")).trim();
          securingAcl=((String)PropertyUtils.getSimpleProperty(form, "txtAccessControlList")).trim(); 
          aclToApply=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,securingAcl);
          if(ownerName!=null && (!ownerName.equals(""))){
            aclNewEdit.setOwnerByName(ownerName);
          }
          if(aclToApply!=null){
            aclNewEdit.setAcl(aclToApply);
          }                 
        }catch(DbsException createException){
          logger.info(createException.getErrorMessage());
          if(createException.containsErrorCode(12200)){
            ActionError createError=new ActionError("errors.acl.12200",ownerName);       
            errors.add(ActionErrors.GLOBAL_ERROR,createError);
            isEditException=true;
          }else if(createException.containsErrorCode(30010)){
            ActionError createError=new ActionError("errors.acl.30010",aclName);       
            errors.add(ActionErrors.GLOBAL_ERROR,createError);
            isEditException=true;
          }
          throw createException;
        }
      }
      DbsPermissionBundle[] allPermissionBundles=SearchUtil.listPermissionBundles(dbsLibrarySession);
      PermissionForm permissionForm=new PermissionForm();
      permissionForm.setLstSelectedPermissionBundle(new String[]{});
      permissionForm.setRadPermission("1");
      ArrayList availablePermissionBundles=new ArrayList();
      for(int index=0;index < allPermissionBundles.length ; index++){
        permissionBundleName=allPermissionBundles[index].getName();
        if(permissionBundleName!=null){
          availablePermissionBundles.add(permissionBundleName);                    
        }
      }
      if(request.getParameter("isNewAcl").equals("true")) {
        permissionForm.setIsNewAcl("true");
      }else{
        permissionForm.setIsNewAcl("false");
      }
      permissionForm.setIsNew("true");
      if(!((String)request.getParameter("isNew")).equals("true")){
        permissionForm.setIsNew("false");
        permissionForm.setIndex(request.getParameter("index"));
        int indexToEdit=Integer.parseInt(request.getParameter("index"));
        granteeName=request.getParameter("granteeName");
        granteeType=request.getParameter("granteeType");
        DbsAccessControlEntry entryInAcl=aclNewEdit.getAccessControlEntrys(indexToEdit);
        boolean isGranted=entryInAcl.isGrant();
        DbsPermissionBundle[] permissionBundlesInAce=entryInAcl.getPermissionBundles();
        ArrayList selectedPermissionBundles=new ArrayList();
        for(int index=0;index < permissionBundlesInAce.length ; index++){
          permissionBundleName=permissionBundlesInAce[index].getName();
          if(permissionBundleName!=null){
            selectedPermissionBundles.add(permissionBundleName); 
            availablePermissionBundles.remove(permissionBundleName);
          }
        }
        String[] permissionBundleSelected=new String[selectedPermissionBundles.size()];
        for(int indexSelected=0;indexSelected < selectedPermissionBundles.size() ; indexSelected++){
          permissionBundleSelected[indexSelected]=(String)selectedPermissionBundles.get(indexSelected);
        }
        permissionForm.setLstSelectedPermissionBundle(permissionBundleSelected);
        if(isGranted){
          permissionForm.setRadPermission("1");
        }else{
          permissionForm.setRadPermission("0");
        }
      }//end if
      
      String[] permissionBundleAvailable=new String[availablePermissionBundles.size()];
      for(int indexAvailable=0;indexAvailable < availablePermissionBundles.size() ; indexAvailable++){
        permissionBundleAvailable[indexAvailable]=(String)availablePermissionBundles.get(indexAvailable);           
      }
      permissionForm.setGranteeType(granteeType);
      permissionForm.setGranteeName(granteeName);
      permissionForm.setAclName(aclName);            
      permissionForm.setLstAvailablePermissionBundle(permissionBundleAvailable);
                  
      request.setAttribute("permissionForm",permissionForm);
      dbsLibrarySession.completeTransaction(newEditTransaction);
      logger.debug("permissionForm" + permissionForm);
      newEditTransaction=null;
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in PermissionB4NewEditAction... ");
      if(isEditException==false){
        logger.error("Aborting PermissionB4NewEditAction");      
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
      logger.error(dbsException.toString());
    }catch(Exception exception){
      logger.error("An Exception occurred in PermissionB4NewEditAction... ");
      if(isEditException==false){
        logger.error("Aborting PermissionB4NewEditAction");      
        ActionError editError=new ActionError("errors.catchall",exception);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
      logger.error(exception.toString());
    }finally{
      try{
        if(newEditTransaction!=null){
          dbsLibrarySession.abortTransaction(newEditTransaction);                 
          logger.debug("Transaction aborted ");      
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in PermissionB4NewEditAction... ");
        logger.error(nestedException.toString());
      }  
    }//end finally 
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      if(isNewAcl.equals("true")){
        return (mapping.findForward("errorNew"));
      }
      else if(isNewAcl.equals("false")){
        return (mapping.findForward("errorEdit"));
      }
    }
    logger.info("Exiting PermissionB4NewEditAction now...");
    return mapping.findForward("success");
  }
}
