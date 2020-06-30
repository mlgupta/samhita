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
 * $Id: SetURLEncryptPasswordB4Action.java,v 1.6 2006/03/13 14:06:42 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user;
/*dms package references */
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.user.SetEncryptionPasswordForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
/*java API */
import java.io.IOException;
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
 *	Purpose: To display set_url_encryption_password.jsp.
 *  @author              Suved Mishra
 *  @version             1.0
 * 	Date of creation:    21-01-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class SetURLEncryptPasswordB4Action extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
    String target=new String("success");
    Locale locale=getLocale(request);
    HttpSession httpSession=null;
    Logger logger=Logger.getLogger("DbsLogger");
    DbsLibrarySession dbsLibrarySession;   
    ExceptionBean exceptionBean;
    ActionErrors errors= new ActionErrors();
    try{
      logger.info("Entering SetURLEncryptPasswordB4Action now...");
      httpSession=request.getSession(false);
      UserInfo userInfo=(UserInfo)httpSession.getAttribute("UserInfo"); 
      dbsLibrarySession=userInfo.getDbsLibrarySession();
      if(!userInfo.isSystemAdmin()){ 
        ActionError actionError=new ActionError("set.url.password.SysAdmin.only");
        errors.add(ActionErrors.GLOBAL_ERROR,actionError);  
        logger.error("ActionError set for SysAdmin access..");
      }else{
        SetEncryptionPasswordForm setEncryptionPasswordForm=new SetEncryptionPasswordForm();
        setEncryptionPasswordForm.setTxtPassword("");
        setEncryptionPasswordForm.setTxtConfirmPassword("");
        request.setAttribute("setEncryptionPasswordForm",setEncryptionPasswordForm);
        logger.debug("setEncryptionPasswordForm: "+
                      request.getAttribute("setEncryptionPasswordForm"));
      }
    }catch(Exception ex){
      logger.error("An Exception occurred in SetURLEncrypPasswordB4Action... ");
      logger.error(ex.toString());
      ActionError actionError=new ActionError("errors.catchall",ex);
      errors.add(ActionErrors.GLOBAL_ERROR,actionError);
      ex.printStackTrace();
    }
    logger.info("Exiting SetURLEncryptPasswordB4Action now...");
    if(!errors.isEmpty()){
      target=new String("failure");
      saveErrors(request,errors);
    }
    
    return mapping.findForward(target);
  }
}
