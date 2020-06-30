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
 * $Id: AclB4EditAction.java,v 20040220.18 2006/03/13 14:06:06 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.security; 
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsDirectoryGroup;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.security.AclNewEditForm;
import dms.web.beans.security.AceListBean;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To populate acl_edit.jsp with the specified acls data.
 * 
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    23-01-2004   
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class AclB4EditAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Locale locale = getLocale(request);
    //Initializing logger
    Logger logger = Logger.getLogger("DbsLogger");        
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ArrayList memberList=new ArrayList();
    boolean deleteMessagePresent=false;
    String aclName=null;
    boolean validationError=false;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    try {
      logger.info("Entering AclB4EditAction now...");
      httpSession = request.getSession(false);
      if(httpSession.getAttribute("radSelect")!=null){
        aclName= (String)httpSession.getAttribute("radSelect");
      }
      if(httpSession.getAttribute("errors")!=null){
        errors = (ActionErrors)httpSession.getAttribute("errors");
        httpSession.removeAttribute("errors");
        validationError=true;
      }
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      if(httpSession.getAttribute("messages")!=null) {
        saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
        deleteMessagePresent=true;
        httpSession.removeAttribute("messages");
      }     
      DbsAccessControlList aclToEdit=(DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,aclName);
      if (aclToEdit!=null){
        logger.debug("Acl to edit "+aclToEdit.getName());
        AclNewEditForm aclNewEditForm=new AclNewEditForm();            
        aclNewEditForm.setTxtAclName(aclToEdit.getName());
        aclNewEditForm.setTxaAclDescription(aclToEdit.getAttribute(DbsAccessControlList.DESCRIPTION_ATTRIBUTE).toString());
        aclNewEditForm.setTxtOwnerName(aclToEdit.getOwner().getName()); 
        aclNewEditForm.setTxtAccessControlList(aclToEdit.getAcl().getName());
//      aclNewEditForm.setChkIsShared(aclToEdit.isShared());

        DbsAccessControlEntry[] acesInAcl=aclToEdit.getAccessControlEntrys();
        ArrayList aceList=new ArrayList();
        if(acesInAcl!=null){
          logger.debug("Total Aces : "+acesInAcl.length);
          for(int index=0;index < acesInAcl.length;index++){
            AceListBean aceListBean=new AceListBean();
            if(acesInAcl[index].getGrantee()!=null){
              if(acesInAcl[index].getGrantee() instanceof DbsDirectoryUser){
                aceListBean.setCategory("User");                    
              }else if(acesInAcl[index].getGrantee() instanceof DbsDirectoryGroup){
                aceListBean.setCategory("Group");                    
              }
              aceListBean.setName(acesInAcl[index].getGrantee().getName());
              if(acesInAcl[index].isGrant()){
                aceListBean.setState("Granted");
              }else{
                aceListBean.setState("Revoked");
              }
              aceListBean.setIndex(new String().valueOf(index));
            }
            aceList.add(aceListBean);
            logger.debug("Access Control Entry : " + aceListBean);
          }
        }else{
          if(!deleteMessagePresent ){
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("acl.empty",aclName);
            messages.add("message2", msg);
            saveMessages(request,messages);                  
          }
        }
        logger.debug("Access Control List : " + aclNewEditForm);
        request.setAttribute("aclNewEditForm",aclNewEditForm);
        httpSession.setAttribute("aceList",aceList);
      }else{
        ActionError editError=new ActionError("errors.acl.notfound",aclName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);            
        httpSession.setAttribute("errors",errors);
        return mapping.findForward("failure");
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in AclB4EditAction... ");
      logger.error(dbsException.toString());
      if(dbsException.containsErrorCode(10406)){
        ActionError editError=new ActionError("errors.acl.10406",aclName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }else if(dbsException.containsErrorCode(10200)){
        ActionError editError=new ActionError("errors.acl.10200",aclName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }else{
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
    }catch(Exception exception) {
      logger.error("An Exception occurred in AclB4EditAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if(!errors.isEmpty()) {
      saveErrors(request, errors);
      logger.debug(mapping.getInputForward()); 
      return (mapping.getInputForward());
    }
    logger.info("Exiting AclB4EditAction now...");
    return mapping.findForward("success");
  }
}
