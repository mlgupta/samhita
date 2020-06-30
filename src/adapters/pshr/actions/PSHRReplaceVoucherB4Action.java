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
 * $Id: PSHRReplaceVoucherB4Action.java,v 1.0 2006/01/11 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.pshr.actions;
/* adapters package references */
import adapters.beans.LoginBean;
import adapters.beans.ReplaceVoucherBean;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/* Servlet API */
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose            : Action called to obtain a dummy document for PSHR.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 2006/03/07
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class PSHRReplaceVoucherB4Action extends Action  {
/* this is the date format specified for adapters */
public static String DATE_FORMAT = "dd-MMM-yyyy";
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
  DateHelperForFileSystem dateHelper = new DateHelperForFileSystem();
  //Variable declaration
  ExceptionBean exceptionBean;                  // bean to handle exceptions 
  String forward = "showLink";                   // mapping for ActionForward
  DbsLibraryService dbsLibraryService;          // represents Library Service
  DbsCleartextCredential dbsCleartextCredential;// represents user Credentials
  DbsLibrarySession dbsLibrarySession = null;   // represents Library Session
  ServletContext context;                       // represents servlet context 
  String ifsService;                            // a parameter to start service
  String ifsSchemaPassword;                     // a parameter to start service
  String serviceConfiguration;                  // a parameter to start service
  String domain;                                // a parameter to start service
  String davpath=null;                          // represents webdav path
  String xmlString= null;                       // xmlString as response  
  String voucherId=null;                        // voucher id used for renaming
  LoginBean loginBean = null;                   // bean to obtain library session
  try{
    logger.info("Entering PSHRReplaceVoucherB4Action now...");
    /* obtain servlet context to fetch context path */
    context = getServlet().getServletContext();
    /* relative path for "GeneralActionParam.xml" */
    String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+
                         File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
    /* obtain necessary request attributes */
    String manageImageUser =null;
    manageImageUser=(String)request.getAttribute("userId");
    String manageImageUserPwd = null;
    manageImageUserPwd=(String)request.getAttribute("password");
    
    String documentId =null;
    String key = null;
    key = (String)request.getAttribute("key"); 
    voucherId=(String)request.getAttribute("voucherId");
    /* Obtain a dbsLibrarySession for the given user, using LoginBean */
    loginBean = new LoginBean(manageImageUser,manageImageUserPwd,relativePath);    
    dbsLibrarySession = loginBean.getUserSession();
    if(dbsLibrarySession.isConnected()){
      logger.debug("Session Id: "+dbsLibrarySession.getId());
      //check and set home folder id if it exist
      DbsFolder dbsFolder = 
            dbsLibrarySession.getUser().getPrimaryUserProfile().getHomeFolder();
      /* find out the managed folder id from the key supplied as request attribute */
      XMLBean xmlBean = new XMLBean(relativePath.replaceFirst("GeneralActionParam",
                                                              "Adapters"));
      String [] keyIds = xmlBean.getAllKeyTagIds("key");
      String [] keyVals = xmlBean.getAllKeyTagValues("key");
      String keyValue = null;      
      int keyIdsSize = ( keyIds == null )?0:keyIds.length;      
      for( int index = 0; index < keyIdsSize; index++ ){
        if( keyIds[index].equals(key) ){
          keyValue = keyVals[index];
          break;
        }
      }
      if(keyValue==null){
        throw new Exception("Key not present.");
      }
      Long homeFolderId = dbsFolder.getId();
      logger.debug("homeFolderId : " + homeFolderId);
      /* instantiate replaceVoucherBean to replace image or upload a dummy */
      String blankDocPath=context.getRealPath("/")+"themes"+File.separator+
                          "images"+File.separator+"backgrounds"+File.separator+
                          "blank.jpg";
      ReplaceVoucherBean replaceVchrBean = 
                         new ReplaceVoucherBean(dbsLibrarySession,keyValue,
                                                documentId,voucherId,blankDocPath);
      boolean isToBeReplaced = replaceVchrBean.replaceImage();
      documentId = replaceVchrBean.getDocumentId();
      /* if the image is not to be replaced, then upload a dummy doc
       * and get it's URL */
      if( !isToBeReplaced ){
        ArrayList DocLinkLists = null;
                      
        DocLinkLists = replaceVchrBean.getLink(false,
                      (String)context.getAttribute("contextPath"),
                      request.getRequestURI(),manageImageUser,manageImageUserPwd,
                      "pshrReplaceVoucherB4Action");                        
        request.setAttribute("DocLinkLists",DocLinkLists);
        /* set up the response xml for response.jsp */
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root Response = 'ManagedImage' ImageDocumentID = '");
        sb.append(documentId);
        sb.append("' ImageURL = '");
        String imageURL = ((FolderDocList)DocLinkLists.get(0)).getTxtLinkGenerated();
        sb.append(imageURL);
        sb.append("' />");
        xmlString = sb.toString();
        logger.debug("xmlString : "+xmlString);
        request.setAttribute("XmlResponse",xmlString);
      }else{
        /* set up the failure response xml for response.jsp */
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root Response = 'ManagedImageFailure' />");
        xmlString = sb.toString();
        logger.debug("xmlString : "+xmlString);
        request.setAttribute("XmlResponse",xmlString);
      }
    }else{
      logger.error("Invalid User Credentials");
      throw new Exception("Invalid User Credentials");
    }
  }catch(DbsException dex)  {
    /* in case of exception send xml response as 'ManageImageFailed' */
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root Response = 'ManagedImageFailure'");         
    sb.append(" />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
    if(dex.containsErrorCode(10170)){
      ActionErrors errors=new ActionErrors();
      ActionError loginError=new ActionError("msg.login.incorrect");
      errors.add(ActionErrors.GLOBAL_ERROR,loginError);
      saveErrors(request,errors);
      dex.printStackTrace();                
    }else{
      exceptionBean = new ExceptionBean(dex);
      logger.error(exceptionBean.getMessage());
      saveErrors(request,exceptionBean.getActionErrors());
      dex.printStackTrace();                
    }
  }catch(Exception ex){
    /* in case of exception send xml response as 'ManageImageFailed' */
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root Response = 'ManagedImageFailure'");         
    sb.append(" />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
    exceptionBean = new ExceptionBean(ex);
    logger.error(exceptionBean.getMessage());
    logger.debug(exceptionBean.getErrorTrace());
    saveErrors(request,exceptionBean.getActionErrors());
    ex.printStackTrace();
  }finally{
    /* disconnect the user library session */
    try{
      dbsLibrarySession.disconnect();
    }catch (DbsException dbsEx) {
      logger.error(dbsEx.toString());
    }
    loginBean.terminateSession();
  }

  logger.info("Exiting PSHRReplaceVoucherB4Action now...");
  return mapping.findForward(forward);
}
}