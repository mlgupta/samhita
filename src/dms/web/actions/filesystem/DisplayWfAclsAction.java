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
 * $Id: DisplayWfAclsAction.java,v 1.3 2006/03/28 07:09:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import adapters.beans.XMLBean;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.actionforms.security.AclListSelectForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.security.AclSelectListBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserWfAclsBean;
import dms.web.beans.utility.DateUtil;
/*java API */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: Action called to display workflow acls set for a given user  
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 15-12-2005
 * Last Modified Date : Suved Mishra
 * Last Modified By   : 01-03-2006
 */
public class DisplayWfAclsAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException   {
  
  String target= new String("success");
  DbsLibrarySession dbsLibrarySession= null;
  DbsPublicObject [] dbsPOs=null;
  HttpSession httpSession= null;    
  ExceptionBean exceptionBean;
  ActionErrors errors=new ActionErrors();
  String userID;
  Logger logger= Logger.getLogger("DbsLogger");
  logger.info("Entering DisplayWfAclsAction now...");
  String relativePath=null;
  String workflowFilePath = null;
  Locale locale = getLocale(request);
  UserWfAclsBean userWfAclsBean = null; 
  AclListSelectForm aclListSelectForm = null;
  try{
    httpSession= request.getSession(false);
    FolderDocListForm folderDocListForm=(FolderDocListForm)form;
    logger.debug("folderDocListForm: "+folderDocListForm);
    aclListSelectForm = new AclListSelectForm();
    aclListSelectForm.setOperation("");
    aclListSelectForm.setTxtPageCount("");
    aclListSelectForm.setTxtPageNo("");
    /*obtain selected doc-ids */
    Long []selectedDocIds= folderDocListForm.getChkFolderDocIds();
    logger.debug("selectedDocIds length: "+selectedDocIds.length);
    
    /*obtain userId,userPassword */
    UserInfo userInfo= (UserInfo)httpSession.getAttribute("UserInfo");
    logger.debug("UserInfo: "+userInfo);
    dbsLibrarySession= userInfo.getDbsLibrarySession();
    userID= dbsLibrarySession.getUser().getName();
    
    /*obtain contextPath,SystemKeyStorePath,passwordFilePath */
    String contextPath=(String)httpSession.getServletContext().getAttribute(
                                                               "contextPath");
    relativePath = contextPath+"WEB-INF"+File.separator+"params_xmls"+
                   File.separator+"GeneralActionParam.xml";
    /* find out approver prefix to be used in acl_list_submit_workflow.jsp */
    XMLBean xmlBean = null;
    workflowFilePath = contextPath+"WEB-INF"+File.separator+"params_xmls"+
                       File.separator+"Workflows.xml";
    String approverWfPrefix = new String("");
    File file = new File(workflowFilePath);
    if( file.exists() ){
      xmlBean = new XMLBean(workflowFilePath);
      approverWfPrefix = xmlBean.getValue("WF_SAM","PrefixName");
    }
    request.setAttribute("approverWfPrefix",approverWfPrefix);
    
    userWfAclsBean = new UserWfAclsBean(userID,relativePath);
    dbsPOs = userWfAclsBean.getUserWfAcls();
    
    if( dbsPOs == null ){
      logger.error("WorkFlow ACLs not set");
      target = new String("failure");
      ActionError actionError=new ActionError("errors.problem.in.workflow.submission");
      errors.add(ActionErrors.GLOBAL_ERROR,actionError);
      saveErrors(request,errors);
    }else{
      httpSession.setAttribute("selectedDocIds",selectedDocIds);
      ArrayList aclSelects = new ArrayList();
      AclSelectListBean[] aclSelectListBean=new AclSelectListBean[dbsPOs.length];
      for( int index = 0; index < dbsPOs.length; index++ ){
        aclSelectListBean[index] = new AclSelectListBean();
        aclSelectListBean[index].setAclId(dbsPOs[index].getId());
        aclSelectListBean[index].setAclName(dbsPOs[index].getName());
        aclSelectListBean[index].setCreateDate(DateUtil.getFormattedDate(
                                               dbsPOs[index].getCreateDate()));
        aclSelectListBean[index].setOwner(
                                dbsPOs[index].getOwner().getDistinguishedName());
        aclSelects.add(aclSelectListBean[index]);
      }
      request.setAttribute("aclselects",aclSelects);
      request.setAttribute("aclListSelectForm",aclListSelectForm);
    }
  }catch(DbsException dex){
    exceptionBean= new ExceptionBean(dex);
    logger.error("An Exception occurred in DisplayWfAclsAction... ");
    logger.error(exceptionBean.getErrorTrace());
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }catch(Exception ex){
    exceptionBean= new ExceptionBean(ex);
    logger.error("An Exception occurred in DisplayWfAclsAction... ");
    logger.error(exceptionBean.getErrorTrace());
    httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
  }finally{
    if( userWfAclsBean!= null ){
      userWfAclsBean.closeThisWfSession();
    }
  }
  logger.info("Exiting DisplayWfAclsAction now...");
  return mapping.findForward(target);
}
}