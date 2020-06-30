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
 * $Id: SetURLEncryptPasswordAction.java,v 1.6 2006/03/13 14:06:42 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.user;
/*dms package references */
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsTransaction;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.user.UserInfo;
/*java API */
import java.io.File;
import java.io.FileWriter;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To set the URL encryption password for link generation.
 *  @author              Suved Mishra
 *  @version             1.0
 * 	Date of creation:    21-01-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */

public class SetURLEncryptPasswordAction extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
    String target=new String("success");
    HttpSession httpSession=null;
    UserInfo userInfo=null;
    Logger logger=Logger.getLogger("DbsLogger");
    Locale locale=getLocale(request);
    DbsLibrarySession dbsLibrarySession=null;
    DbsTransaction editTransaction=null;
    String editUserName=new String();
    ActionErrors errors=new ActionErrors();
    try{
      logger.info("Entering SetURLEncryptPasswordAction...");
      httpSession=request.getSession(false);
      userInfo=(UserInfo)httpSession.getAttribute("UserInfo");
      dbsLibrarySession=userInfo.getDbsLibrarySession();
      
      String password= request.getParameter("txtPassword");
      logger.debug("txtPassword: "+password);
      CryptographicUtil cryptoUtil= new CryptographicUtil();
      String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
      String pwdFilePath=cryptoUtil.getPwdFilePath(contextPath);
      
      File passwordFile= new File(pwdFilePath);
      FileWriter fr= new FileWriter(passwordFile);
      fr.write(password);
      fr.close();

      DbsDirectoryUser editUser= dbsLibrarySession.getUser();
      editUserName= editUser.getName();
      editTransaction=dbsLibrarySession.beginTransaction();
      logger.debug("Transaction begins ...");
      
      String result=cryptoUtil.setSystemKeystorePass(password,contextPath);
      
      if(result.equals("SUCCESS")){          
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("set.url.password.success");
        messages.add("message1",msg);
        httpSession.setAttribute("messages",messages);    
      }else if(result.equals("FAILURE")){
        dbsLibrarySession.abortTransaction(editTransaction);                 
        editTransaction=null;
        logger.debug("Transaction aborted");
        ActionError editError=new ActionError("set.url.password.failure");
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }else if(result.equals("KEY_GENERATION_ERROR")){
        dbsLibrarySession.abortTransaction(editTransaction);                 
        editTransaction=null;
        logger.debug("Transaction aborted");
        ActionError editError=new ActionError("keygeneration.error",editUserName);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);                
      }
      if( editTransaction != null ){
        dbsLibrarySession.completeTransaction(editTransaction);
        logger.debug("Transaction completed ...");
        editTransaction=null;
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in SetURLEncryptionPasswordAction... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in SetURLEncryptionPasswordAction... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }finally{
      try{
        if(editTransaction!=null){
          dbsLibrarySession.abortTransaction(editTransaction);                 
          logger.debug("Transaction aborted");                    
        }       
      }catch(DbsException nestedException){
        logger.error("An Exception occurred in SetURLEncryptionPasswordAction... ");
        logger.error(nestedException.toString());
      }  
    }//end finally
    if (!errors.isEmpty()) {      
      httpSession.setAttribute("errors",errors);
    } 
    logger.info("Exiting SetURLEncryptPasswordAction now...");
    
    return mapping.findForward(target);
  }
}
