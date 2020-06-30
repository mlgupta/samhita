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
 * $Id: GenericRenameVoucherAction.java,v 1.0 2006/06/20 06:42:00 suved Exp $
 *****************************************************************************
 */
package adapters.generic.actions;
/* adapter package references */
import adapters.beans.LoginBean;
import adapters.beans.RenameVoucherBean;
import adapters.beans.XMLBean;
import adapters.generic.beans.FindGenericVoucherBean;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.utility.ParseXMLTagUtil;
//Java API
import java.io.File;
import java.io.IOException;
//Servlet API
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Oracle Connection Pool API
import oracle.jdbc.pool.OracleConnectionCacheImpl;
//Struts API
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose            : Action called to rename the document image to it's 
 *                      voucherId for Generic ERP.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 20-06-06
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class GenericRenameVoucherAction extends Action  {
  /* this is the date format specified for adapters */
  public static String DATE_FORMAT = "dd-MMM-yyyy";
  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger");
    DateHelperForFileSystem dateHelper = new DateHelperForFileSystem();
  
    //Variable declaration
    ExceptionBean exceptionBean;                  // bean to handle exceptions 
    String forward = "success";                   // mapping for ActionForward
    DbsLibraryService dbsLibraryService;          // represents Library Service
    DbsCleartextCredential dbsCleartextCredential;// represents user Credentials
    DbsLibrarySession dbsLibrarySession = null;   // represents Library Session
    ServletContext context;                       // represents servlet context 
    HttpSession httpSession;                      // represents http session 
    String ifsService;                            // a parameter to start service
    String ifsSchemaPassword;                     // a parameter to start service
    String serviceConfiguration;                  // a parameter to start service
    String domain;                                // a parameter to start service
    ActionErrors actionErrors = null;             // hold a collection of error objs
    ActionError actionError;                      // individual error objects
    String xmlString= null;                       // xmlString as response
    LoginBean loginBean=null;                     // bean to obtain library session
    int responseCode = 0;                         // 0 suggests success, while 
                                                  // nonzero is for failure
    try{
      logger.info("Entering GenericRenameVoucherAction now...");
      actionErrors = new ActionErrors();
      /* obtain servlet context to fetch context path */
      context = getServlet().getServletContext();
      /* relative path for "GeneralActionParam.xml" */
      String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+
                           File.separator+"params_xmls"+File.separator+
                           "GeneralActionParam.xml";
      ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);
      /* obtain necessary request attributes */
      String manageImageUser = (String)request.getAttribute("userId");
      String manageImageUserPwd =  (String)request.getAttribute("password");
      String documentId =(String)request.getAttribute("docId");
      String voucherId = (String)request.getAttribute("voucherId");
      String aclName =   (String)request.getAttribute("aclName");
      String key = (String)request.getAttribute("screenId"); 
      /* kill an already existing httpSession for the credentials provided */
      if(context.getAttribute(manageImageUser.toUpperCase())==null){
        httpSession = request.getSession(true);
        logger.debug("httpSession id: "+httpSession.getId());
        logger.debug("context Attribute set to null initially...");
      }else{
        HttpSession oldHttpSession=(HttpSession)context.getAttribute(
                                                manageImageUser.toUpperCase());
         if (oldHttpSession!=null){
           logger.debug("OldSessionExists.");
         }
         if(oldHttpSession.getId()==null){
            logger.debug("oldHttpSession id is null.");
         }else{
            logger.debug("oldHttpSession id is not null.");
         }
        if (oldHttpSession!=null || oldHttpSession.getId()!=null){
          logger.debug("oldHttpSession id: "+oldHttpSession.getId());
          oldHttpSession.invalidate();
        }
        httpSession = request.getSession(true);
        
        logger.debug("User logged in already...");
        //user is already logged in
      }
      /* Obtain a dbsLibrarySession for the given user, using LoginBean */
      loginBean = new LoginBean(manageImageUser,manageImageUserPwd,
                                          relativePath);    
      dbsLibrarySession = loginBean.getUserSession();
      if(dbsLibrarySession.isConnected()){
        /* Obtain the names and document ids */
        String [] names = new String[1];
        names[0] = voucherId;
        Long [] docIds = new Long[1];
        docIds[0] = new Long(documentId);
        /* fetch the Oracle Connection Pool from context */ 
        OracleConnectionCacheImpl wfConnCache = (OracleConnectionCacheImpl)
                httpSession.getServletContext().getAttribute("wfConnCache");
        String workFlowFilePath = relativePath.replaceAll("GeneralActionParam",
                                                          "Workflows");
        /* find out the managed folder id from the key supplied as request attribute */
        XMLBean xmlBean = new XMLBean(relativePath.replaceFirst("GeneralActionParam",
                                                                "Adapters"));
        String keyValue = xmlBean.getKeyForKeyId("key",key,aclName);
        if(keyValue==null){
          responseCode = 1;
          throw new Exception("ScreenId not found.");
        }
        /* Instantiate the RenameVoucherBean to rename the document to voucherId */
        RenameVoucherBean renameVchr = new RenameVoucherBean(wfConnCache,
                                           dbsLibrarySession,names,docIds,
                                           aclName,workFlowFilePath);
       
        if( renameVchr.getRenameStatus() ){
          FindGenericVoucherBean findVchr = new FindGenericVoucherBean(voucherId,key,relativePath);
          findVchr.updateVoucherData(documentId);
          logger.debug("Document Renamed successfully...");
        }else{
          logger.debug("Couldnot Rename Document!!!");
          throw new Exception("Invalid document id");
        }
        
      }
      /* Set up xmlString to be fetched to ps_response.jsp */
      //////////
      StringBuffer sb = new StringBuffer();
      sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
      sb.append("\n");
      sb.append("<root RC = '0' Response = 'IMAGEPROCESSEDSUCCESS'");       
      sb.append(" />");
      xmlString = sb.toString();
      logger.debug("xmlString : "+xmlString);         
      //////////
      if( dbsLibrarySession != null && dbsLibrarySession.isConnected() ){
        dbsLibrarySession.disconnect();
      }
    }catch(DbsException dbsEx){
      logger.debug("Exiting GenericRenameVoucherAction : " + dbsEx.toString());
      /* Set up xmlString to be fetched to ps_response.jsp */
      StringBuffer sb = new StringBuffer();
      String rc = dbsEx.getMessage();
      sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
      sb.append("\n");
      sb.append("<root RC = '");
      sb.append(responseCode);
      sb.append("' Response = '");
      sb.append(rc);
      sb.append("' />");
      xmlString = sb.toString();
    }catch(Exception ex){
      logger.debug("Exiting GenericRenameVoucherAction : " + ex.toString());
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
    }finally{     
      /* terminate the librarySession upon successful renaming */
      try{
        dbsLibrarySession.disconnect();
      }catch(DbsException dbsEx){
        logger.error("Exiting GenericRenameVoucherAction : " + dbsEx.toString());
      }
      loginBean.terminateSession();
    }
    request.setAttribute("XmlResponse",xmlString);
    logger.info("Exiting GenericRenameVoucherAction now...");
    
    return mapping.findForward(forward);
    
  }
}
