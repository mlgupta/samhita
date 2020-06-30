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
 * $Id: FolderDocApplyAclAction.java,v 20040220.17 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.filesystem.FolderDocApplyAclForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To apply ACL to selected Public Objects
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   23-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  26-12-2005  
 */
public class FolderDocApplyAclAction extends Action  {
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
  logger.info("Entering FolderDocApplyAclAction now...");
  logger.info("Applying ACL ...");

  //variable declaration
  ExceptionBean exceptionBean;
  String forward = "success";
  Locale locale = getLocale(request);
  HttpSession httpSession = null;
  try{
    httpSession = request.getSession(false);
    logger.debug("Initializing Variables...");        
    FolderDocApplyAclForm folderDocApplyAclForm = (FolderDocApplyAclForm)form;
    logger.debug("folderDocApplyAclForm : " + folderDocApplyAclForm);
    UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
    DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
    FolderDocInfo folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
    folderDocInfo.setNoReloadTree(true);
    Long aclId = folderDocApplyAclForm.getRadAclId();
    logger.debug("aclId : " + aclId);
    Long[] folderDocIds = folderDocApplyAclForm.getHdnFolderDocIds();
    logger.debug("folderDocIds.length : " + String.valueOf(folderDocIds.length));
    boolean recursively = folderDocApplyAclForm.isChkApplyRecursively();
    logger.debug("recursively : " + String.valueOf(recursively));
    logger.debug("Initializing Variables Complete");        

    if(recursively == true){
        logger.info("The ACL is being applied recursively");
    }
    String relativePath =httpSession.getServletContext().getRealPath("/")+
                         "WEB-INF"+File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    
    FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
    folderDoc.applyAcl(folderDocIds,aclId,recursively,relativePath,
                       dbsLibrarySession.getUser().getDistinguishedName());
    relativePath = null;
    ActionMessages actionMessages = new ActionMessages();
    ActionMessage actionMessage = new ActionMessage("msg.AclAppliedSuccessfully");
    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
    httpSession.setAttribute("ActionMessages",actionMessages);
    logger.info("Applying ACL Complete");
  }catch(DbsException dex){
    exceptionBean = new ExceptionBean(dex);
    logger.error("An Exception occurred in FolderDocApplyAclAction... ");
    logger.error(exceptionBean.getErrorTrace());

    if(dex.getErrorCode() == 30035){
      exceptionBean.setMessageKey("errors.30035.insufficient.access.to.change.POAcl");
    }

    if(dex.containsErrorCode(30659)){
      exceptionBean.setMessageKey("errors.30659.PO.has.userlock.unable.to.change");
    }
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }catch(Exception ex){
    exceptionBean = new ExceptionBean(ex);
    logger.error("An Exception occurred in FolderDocApplyAclAction... ");
    logger.error(exceptionBean.getErrorTrace());
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }
  logger.info("Exiting FolderDocApplyAclAction now...");
  return mapping.findForward(forward);
}
}
