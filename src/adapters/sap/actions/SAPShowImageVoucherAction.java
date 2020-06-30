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
 * $Id: SAPShowImageVoucherAction.java,v 1.0 2006/03/14 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.sap.actions;
/* adapters package references */
import adapters.beans.LoginBean;
import adapters.beans.XMLBean;
import adapters.sap.beans.FindSAPVoucherBean;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
/* Java API */
import java.io.File;
import java.io.IOException;
/* Servlet API */
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose            : Action called to show image for SAP.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 2006/03/14
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class SAPShowImageVoucherAction extends Action  {
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
  //Variable declaration
  String forward = "showImage";                 // mapping for ActionForward
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
  int responseCode = 0;                         // 0 suggests success, while 
                                                // nonzero is for failure
  try{
    logger.info("Entering SAPShowImageVoucherAction now...");
    /* obtain servlet context to fetch context path */
    context = getServlet().getServletContext();
    /* relative path for "GeneralActionParam.xml" */
    String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+
                         File.separator+"params_xmls"+File.separator+
                         "GeneralActionParam.xml";
    /* obtain necessary request attributes */
    String manageImageUser =null;
    manageImageUser=(String)request.getAttribute("userId");
    String manageImageUserPwd = null;
    manageImageUserPwd=(String)request.getAttribute("password");
    
    String key = null;
    key = (String)request.getAttribute("screenId"); 
    voucherId=(String)request.getAttribute("voucherId");
    String aclName = (String)request.getAttribute("aclName"); 
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
      String keyValue = xmlBean.getKeyForKeyId("key",key,aclName);
      if(keyValue==null){
        responseCode = 1;
        throw new Exception("ScreenId not found.");
      }
      /* instantiate replaceVoucherBean to replace image or upload a dummy */
      String blankDocPath=context.getRealPath("/")+"themes"+File.separator+
                          "images"+File.separator+"backgrounds"+File.separator+
                          "blank.jpg";
      FindSAPVoucherBean findVchr = new FindSAPVoucherBean(voucherId,key,relativePath);
      String voucherURL = findVchr.getVoucherURL();
      if( voucherURL != null ){
        /* set up the response xml for response.jsp */
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root RC = '0' Response = 'Image' ImageURL = '");
        sb.append(voucherURL);
        sb.append("' />");
        xmlString = sb.toString();
        logger.debug("xmlString : "+xmlString);
      }else{
        responseCode = 3;
        logger.error("Unable to find voucher URL corresponding to the specified voucher id and screen id.");
        throw new Exception("voucher URL not found");
      }
      request.setAttribute("XmlResponse",xmlString);
    }else{
      responseCode = 2;
      logger.error("Invalid User Credentials");
      throw new Exception("Invalid User Credentials");
    }
  }catch(DbsException dex)  {
    /* in case of exception send xml response as 'ShowImageFailed' */
    responseCode = 4;
    String rc = dex.getErrorMessage();
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root RC = '");
    sb.append(responseCode);
    sb.append("' Response = '");
    sb.append(rc);
    sb.append("' />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
  }catch(Exception ex){
    /* in case of exception send xml response as 'ShowImageImageFailed' */
    if(responseCode==0){
          responseCode = 5;
    }
    String rc = ex.getMessage();
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
    sb.append("\n");
    sb.append("<root RC = '");
    sb.append(responseCode);
    sb.append("' Response = '");
    sb.append(rc);
    sb.append("' />");
    xmlString = sb.toString();         
    request.setAttribute("XmlResponse",xmlString);
  }finally{
    /* disconnect the user library session */
    try{
      dbsLibrarySession.disconnect();
    }catch (DbsException dbsEx) {
      logger.error(dbsEx.toString());
    }
    loginBean.terminateSession();
  }

  logger.info("Exiting SAPShowImageVoucherAction now...");
  return mapping.findForward(forward);
}

}