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
 * $Id: DocDecryptAction.java,v 20040220.14 2006/05/19 06:24:16 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.DocDecryptForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DocEventLogBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
import dms.web.beans.wf.watch.InitiateWatch;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
 *	Purpose: To decrypt an encryted dbsDocument and log event in dbsDocument 
 *           attribute namely, "AUDIT_LOG"
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  01-03-2006  
 */
public class DocDecryptAction extends Action  {
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
    logger.info("Entering DocDecryptAction now...");
    logger.info("Decrypting File ...");
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    DbsDocument dbsDocument = null;
    DbsPublicObject dbsPublicObject = null;
    DbsLibrarySession dbsLibrarySession = null;
    FolderDocInfo folderDocInfo = null;
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    InputStream inputStream = null;
    try{
      DocDecryptForm docDecryptForm = (DocDecryptForm)form;
      logger.debug("docDecryptForm : " + docDecryptForm);
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      folderDocInfo.setNoReloadTree(true);
      
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long documentId = docDecryptForm.getChkFolderDocIds()[0];
      logger.debug("documentId : " + documentId);
      String encryptionPassword = docDecryptForm.getTxtEncryptionPassword();
      dbsPublicObject = 
        dbsLibrarySession.getPublicObject(documentId).getResolvedPublicObject();
      String relativePath =httpSession.getServletContext().getRealPath("/")+
                           "WEB-INF"+File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      String userName = dbsLibrarySession.getUser().getDistinguishedName();                
      
      if(dbsPublicObject instanceof DbsDocument){
        dbsDocument = (DbsDocument)dbsPublicObject;
        DbsDirectoryUser dbsDirectoryUser = dbsLibrarySession.getUser(); 
        DbsPropertyBundle dbsPropertyBundle = dbsDirectoryUser.getPropertyBundle();
        boolean encryptionEnabled = false;
        DbsProperty dbsProperty = null; 
        if(dbsPropertyBundle != null){
          dbsProperty = dbsPropertyBundle.getProperty(DbsDirectoryUser.ENCRYPTION_ENABLED);
          if(dbsProperty != null){
            logger.debug("encryptionEnabled : true");
            DbsAttributeValue dbsAttributeValue = dbsDocument.getAttribute("ENCRYPTED");
            if(!dbsAttributeValue.isNullValue() && dbsAttributeValue.getBoolean(dbsLibrarySession) == true ){
              CryptographicUtil cryptographicUtil = new CryptographicUtil();
              inputStream = cryptographicUtil.decryptDoc(dbsDocument,dbsDirectoryUser.getName(),encryptionPassword,(String)httpSession.getServletContext().getAttribute("contextPath"));
              if(inputStream != null){
                DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
                dbsFileSystem.putDocument(dbsDocument.getName(),inputStream,dbsDocument.getFolderReferences(0).getAnyFolderPath(),null);
                dbsDocument.setAttribute(DbsDocument.ENCRYPTED,DbsAttributeValue.newAttributeValue(false));
                if( relativePath!=null && userName!=null ){
                  // code for watch wf submission goes here
                  String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsDocument.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsDocument.getName()+" has been decrypted";
                  InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsDocument.getId());
                  iniWatch.startWatchProcess();
                  actionForWatch = null;
                  relativePath = null;
                  userName = null;
                }
                /* logic for logging event in "AUDIT_LOG" attribute */
                DocEventLogBean logBean = new DocEventLogBean();
                logBean.logEvent(dbsLibrarySession,dbsPublicObject.getId(),"Encrypted Document Decrypted");
              }else{
                //password is wrong
                ActionErrors actionErrors = new ActionErrors();
                ActionError actionError = new ActionError("msg.decryption.password.wrong");
                actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
                httpSession.setAttribute("ActionErrors",actionErrors);
                return mapping.findForward(forward);
              }
            }else{
              //then display message that it is not encrypted
              ActionErrors actionErrors = new ActionErrors();
              ActionError actionError = new ActionError("msg.document.notencrypted");
              actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
              httpSession.setAttribute("ActionErrors",actionErrors);
              return mapping.findForward(forward);
            }
          }else{
            //please set encryption password to enable this feature
            ActionErrors actionErrors = new ActionErrors();
            ActionError actionError = new ActionError("msg.document.notencrypted");
            actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
            httpSession.setAttribute("ActionErrors",actionErrors);
            return mapping.findForward(forward);
          }
        }else{
          //please set encryption password to enable this feature
          ActionErrors actionErrors = new ActionErrors();
          ActionError actionError = new ActionError("msg.document.notencrypted");
          actionErrors.add(ActionMessages.GLOBAL_MESSAGE,actionError);
          httpSession.setAttribute("ActionErrors",actionErrors);
          return mapping.findForward(forward);
        }
        //display message document encrypted successfully
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.document.decrypted.successfully");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }
    }catch(DbsException dex){
      exceptionBean = new ExceptionBean(dex);
      logger.error("An Exception occurred in DocDecryptAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in DocDecryptAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }finally{
      /* it is mandatory to close streams in any case of execution */
      try{
        if( inputStream != null ){
          inputStream.close();
          logger.debug("InputStream closed successfully...");
        }
      }catch(Exception ex){
        logger.error("An Exception occurred in DocDecryptAction... ");
        logger.error(ex.toString());
      }
      inputStream = null;
    }
    logger.info("Decrypting File complete");
    logger.info("Exiting DocDecryptAction now...");
    return mapping.findForward(forward);
  }
}
