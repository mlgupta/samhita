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
 * $Id: ShowContentAction.java,v 1.14 2006/03/13 14:06:57 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFormat;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.ShowContentForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ParseXMLTagUtil;
/*java API */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import javax.servlet.ServletContext;
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
 * Purpose: Action called to show doc(s) content from link(s) generated in 
 *          doc_generate_url.jsp
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 13-01-05
 * Last Modified Date : 12-12-05
 * Last Modified By   : Suved Mishra
 */
/** The link(s) would be decrypted and after appropriate validation, the docs 
 *  would be displayed */
public class ShowContentAction extends Action{
private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss"; 
/**
 * This is the main action called from the Struts framework.
 * @param mapping The ActionMapping used to select this instance.
 * @param form The optional ActionForm bean for this request.
 * @param request The HTTP Request we are processing.
 * @param response The HTTP Response we are processing.
 */
public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
{ 
  String target= new String("success");
  boolean validSession=false;
  Logger logger= Logger.getLogger("DbsLogger");
  UserInfo userInfo=null;
  ServletContext context;
  String contextPath=new String();
  DbsLibraryService dbsLibraryService=null;
  DbsLibrarySession dbsLibrarySession= null;
  DbsCleartextCredential dbsCleartextCredential;
  DbsPublicObject dbsPublicObject=null;
  DbsDocument dbsDocument= null;
  String ifsSchemaPassword=new String();
  HttpSession httpSession=null;    
  ExceptionBean exceptionBean;
  logger.debug("Entering ShowContentAction now...");
  CryptographicUtil cryptoUtil=new CryptographicUtil(); 
  String encodedString=new String();
  String decryptedString=new String();
  String userId=null;
  String userPassword=null;
  Long documentId=null;
  Integer linkId = null;
  int [] linkCount = null;
  String [] linkExpDate = null;
  ActionErrors errors=new ActionErrors();
  boolean showDoc = false;

  try{
    logger.info("Entering ShowContentAction...");
    ShowContentForm showContentForm=(ShowContentForm)form;
    logger.debug("ShowContentForm: "+showContentForm);
    context=getServlet().getServletContext();
    contextPath=(String)context.getAttribute("contextPath");
    logger.debug("contextPath: "+contextPath);

    /*before decrypting check for the existence of .password & .keyStore*/

    /*if they exist, go ahead with decryption */
    if(cryptoUtil.getSystemKeyStoreFile(contextPath)){
      String relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+
                           File.separator+"GeneralActionParam.xml";
      ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            

      /* obtain necessary parameters*/        
      String ifsService= parseUtil.getValue("IfsService","Configuration");
      logger.debug("ifsService: "+ifsService);
      
      String serviceConfiguration= parseUtil.getValue("ServiceConfiguration",
                                                      "Configuration");
      logger.debug("serviceConfiguration: "+serviceConfiguration);

      /*if .password exists obtain cryptoPassword for decryption */
      if(cryptoUtil.getSystemKeyStoreFile(contextPath)){
        String pwdFilePath=cryptoUtil.getPwdFilePath(contextPath);
        String cryptoPassword=cryptoUtil.getCryptoPassword(pwdFilePath);
  
        /*while obtaining encodedString, URLDecoder will automatically perform 
         * it's function and the encodedString is now Base64Decoded to obtain a 
         * byte[]buf */
        encodedString=request.getParameter("auth");
  
        byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer(encodedString);
        String decodedStr=URLDecoder.decode(encodedString,"UTF-8");
        logger.debug("URLDECODE: "+decodedStr);
      
        logger.debug("byteArray2 (buf)in showContentAction: "+new String(buf));
        logger.debug("byteArray2(buf).length: "+buf.length);
  
        /* buf is assigned to byte[]ToDecrypt*/
        byte[] byteArrayToDecrypt=buf;
        logger.debug("byteArrayToDecrypt.length: "+byteArrayToDecrypt.length);
  
        /*obtain byte[]Input Stream from byte[]ToDecrypt and byte[]Decrypted 
         * using crypto.decryptString()*/
        ByteArrayInputStream isStream= new ByteArrayInputStream(byteArrayToDecrypt);
        logger.debug("isStream.class: "+isStream.getClass());
        ByteArrayInputStream deciStream=(ByteArrayInputStream)
                                        cryptoUtil.decryptString(isStream,
                                        cryptoPassword,contextPath);
  
        int destavailable= deciStream.available();
        logger.debug("destavailable: "+destavailable);
        byte[] byteArrayDecrypted=new byte[destavailable];
        int decbytecount= deciStream.read(byteArrayDecrypted,0,destavailable);
        logger.debug("decbytecount: "+decbytecount);
        
        /*if streamreading has been successful, obtain decryptedString */
        if(decbytecount == destavailable){
          decryptedString=new String(byteArrayDecrypted);
          /* split decryptedString to obtain userId,userPassword,documentId,
           * linkId(optional) */
          String [] splitVals = decryptedString.split("&");
          int length = (splitVals == null)?0:splitVals.length;
          /* for backward compatibility , where linkId wasn't specified,viz: Wf */
          if( length == 3 ){
            String [] userIdArr = splitVals[0].split("=");
            userId = userIdArr[1];                        /* parameter 1 */
            String [] passwordArr = splitVals[1].split("=");
            userPassword = passwordArr[1];                /* parameter 2 */
            String [] docIdArr = splitVals[2].split("=");
            documentId = new Long(docIdArr[1]);           /* parameter 3 */
          }else{
          /* for modified link generation,there will be 4 parameters to obtain */
            String [] userIdArr = splitVals[0].split("=");
            userId = userIdArr[1];                        /* parameter 1 */
            String [] passwordArr = splitVals[1].split("=");
            userPassword = passwordArr[1];                /* parameter 2 */
            String [] docIdArr = splitVals[2].split("=");
            documentId = new Long(docIdArr[1]);           /* parameter 3 */
            String [] linkIdArr = splitVals[3].split("=");
            linkId = new Integer(linkIdArr[1]);           /* parameter 4 */
            logger.debug("linkId :"+linkId.intValue());
          }
          /*start libraryService,librarySession */
          dbsCleartextCredential= new DbsCleartextCredential(userId,userPassword);
    
          if(DbsLibraryService.isServiceStarted(ifsService)){
            logger.info(ifsService + " is running");
            dbsLibraryService = DbsLibraryService.findService("IfsDefaultService");
          }
          try{
            dbsLibrarySession=dbsLibraryService.connect(dbsCleartextCredential,null);
            logger.debug("LibrarySession obtained: "+dbsLibrarySession.isConnected());
          }catch(DbsException dex){
            logger.error(dex.getErrorMessage());
            logger.debug("errortrace: "+dex.toString());
            if(dex.containsErrorCode(10170)){
              throw dex;
            }else if(dex.containsErrorCode(21008)){
              logger.info("This exception is thrown when library service started");
              logger.info(" successfully and then database went down");
              logger.info("In this case Library Service need to be restarted");
    
              ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword",
                                                     "Configuration");
              serviceConfiguration = parseUtil.getValue("ServiceConfiguration",
                                                        "Configuration");
              String domain = parseUtil.getValue("Domain","Configuration");
              
              /*ifsSchemaPassword = context.getInitParameter("IfsSchemaPassword");
              serviceConfiguration = context.getInitParameter("ServiceConfiguration");
              domain = context.getInitParameter("Domain"); */
    
              logger.info("Disposing Library Service...");
              dbsLibraryService.dispose(ifsSchemaPassword);
              logger.info("Disposing Library Service Complete");
    
              logger.info("ReStarting Library Service...");            
              dbsLibraryService = DbsLibraryService.startService(ifsService,
                                  ifsSchemaPassword,serviceConfiguration,domain);
              logger.info("ReStarting Library Service Complete");
              dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);              
            }
          }
          /*obtain doc as dbsPublicObject using docId from URL */
          dbsPublicObject=dbsLibrarySession.getPublicObject(documentId).getResolvedPublicObject();
          /* for backward compatibility */
          if( linkId == null ){
            showDoc = true;
            /*if dbsPublicObject is an instance of DbsDocument read it's content */
            if(dbsPublicObject instanceof DbsDocument){
              dbsDocument=(DbsDocument)dbsPublicObject;
              logger.debug("Doc name: "+dbsDocument.getName());
            }
          }else{
            /* new logic for link limits by time and trials begins here */
            if( dbsPublicObject instanceof DbsDocument ){
              dbsDocument=(DbsDocument)dbsPublicObject;
              /* obtain document attrs. LINK_COUNT and LINK_EXP_DATE */
              linkCount = dbsDocument.getAttribute("LINK_COUNT").getIntegerArray(
                                                                 dbsLibrarySession);
              linkExpDate = dbsDocument.getAttribute("LINK_EXP_DATE").getStringArray(
                                                                  dbsLibrarySession);
              /* fetch the linkCountValue and linkExpiryDateValue corr. to index
               * linkId obtained from the decrypted String */
              int linkCountVal = linkCount[linkId.intValue()];
              logger.debug("linkCountVal: "+linkCountVal);
              String linkExpDateVal = linkExpDate[linkId.intValue()];
              logger.debug("linkExpDateVal : "+linkExpDateVal);
              /* if there is a link expiry date */
              if( !linkExpDateVal.equals("-1") ){
                Date expiryDate = DateHelperForFileSystem.parse(linkExpDateVal,
                                                                DATE_FORMAT);
                Date dateObj = new Date();
                /* compare current date with the expiry date */
                if( dateObj.compareTo(expiryDate) <= 0 ){
                  logger.debug("link not expired...");
                  /* check for link count value */
                  /* if -1, then there is no limit on viewing doc thru link */
                  /* for non-negative value, update count and view link */
                  if( linkCountVal == -1 ){
                    showDoc = true;
                  }else if( linkCountVal > 0 ){
                    linkCount[linkId.intValue()] = --linkCountVal;
                    Integer [] link_Count = new Integer[linkCount.length];
                    for( int index = 0; index < linkCount.length; index++ ){
                      link_Count[index] = new Integer(linkCount[index]);
                    }
                    try{
                      /* Please note that when we have set the link for a version
                       * and then a new version is uploaded, our document becomes
                       * a previous version and hence acquires a soft lock .
                       * Inorder to view it's content, we have to unlock it first
                       * because on a soft lock , one can delete an object but 
                       * cannot modify it and we are about to do the latter
                       * part by modifying it's attribute and hence need to unlock
                       * it first.*/
                      if( dbsDocument.getResolvedPublicObject().isLocked() ){
                        dbsDocument.getResolvedPublicObject().unlock();
                      }
                      /* set "LINK_COUNT" attr for the document */
                      dbsDocument.setAttribute("LINK_COUNT",
                                               DbsAttributeValue.newAttributeValue(
                                               link_Count));
                    }catch (DbsException dbsEx) {
                      logger.debug(dbsEx.toString()); 
                    }
                    showDoc = true;
                  }else{            /* for zero link count donot show document */
                    showDoc = false;
                  }
                }else{              /* if link has expired, donot show document */
                  showDoc = false;
                }
                
              }else{ /* if no expiry date set, check only for link count */
                if( linkCountVal == -1 ){
                  showDoc = true;
                }else if( linkCountVal > 0 ){
                  linkCount[linkId.intValue()] = --linkCountVal;
                  Integer [] link_Count = new Integer[linkCount.length];
                  for( int index = 0; index < linkCount.length; index++ ){
                    link_Count[index] = new Integer(linkCount[index]);
                  }
                  try{
                    /* Please note that when we have set the link for a version
                     * and then a new version is uploaded, our document becomes
                     * a previous version and hence acquires a soft lock .
                     * Inorder to view it's content, we have to unlock it first
                     * because on a soft lock , one can delete an object but 
                     * cannot modify it and we are about to do the latter
                     * part by modifying it's attribute and hence need to unlock
                     * it first.*/
                    if( dbsDocument.getResolvedPublicObject().isLocked() ){
                      dbsDocument.getResolvedPublicObject().unlock();
                    }
                    dbsDocument.setAttribute("LINK_COUNT",
                                              DbsAttributeValue.newAttributeValue(
                                              link_Count));
                  }catch (DbsException dbsEx) {
                    logger.debug(dbsEx.toString()); 
                  }
                  showDoc = true;
                }else{
                  showDoc = false;
                }
              }
            }
          }
          /* if showDoc then display doc through link */
          if(showDoc){
            InputStream inputStream= dbsDocument.getContentStream();
            DbsFormat dbsFormat= dbsDocument.getFormat();
            String mimeType=dbsFormat.getMimeType();
            logger.debug("Doc mimeType: "+mimeType);
      
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition","filename=\""+ 
                                dbsDocument.getName() + "\"");
            response.setContentLength((int)dbsDocument.getContentSize());
            logger.debug("Doc ContentSize: "+dbsDocument.getContentSize());
      
            byte[] content= new byte[(int)dbsDocument.getContentSize()];
            inputStream.read(content,0,(int)dbsDocument.getContentSize());
            inputStream.close();
            inputStream = null;
            
            OutputStream outputStream= response.getOutputStream();
            outputStream.write(content);
            outputStream.close();
            outputStream = null;
            
            response.flushBuffer();
          }else{
            /* unable to display doc as no criteria satisfied.
             * display appropriate error message */
            ActionError actionError=new ActionError("errors.link.limits.exceeded");
            errors.add(ActionErrors.GLOBAL_ERROR,actionError);
            saveErrors(request,errors);
            try{
              if(userInfo==null)
                target=new String("failureuserInfoNull");
              if(userInfo!=null)
                target=new String("failureInSession");
            }catch(Exception e){
              target=new String("failureuserInfoNull");
            }
          }
          dbsLibrarySession.disconnect();
        }else{
          logger.error("error in reading stream...");
        }
      }else{/*if .password & .keyStore donot exist setup an ActionError */
        ActionError actionError=new ActionError("errors.in.displaying.content");
        errors.add(ActionErrors.GLOBAL_ERROR,actionError);
        saveErrors(request,errors);
        try{
          if(userInfo==null)
            target=new String("failureuserInfoNull");
          if(userInfo!=null)
            target=new String("failureInSession");
        }catch(Exception e){
          target=new String("failureuserInfoNull");
        }
      }
    }else{
      /* if .SystemKeyStore dir doesn't exist , display an ActionError */
      ActionError actionError=new ActionError("errors.in.displaying.content");
      errors.add(ActionErrors.GLOBAL_ERROR,actionError);
      saveErrors(request,errors);
      try{
        if(userInfo==null)
          target=new String("failureuserInfoNull");
        if(userInfo!=null)
          target=new String("failureInSession");
      }catch(Exception e){
        target=new String("failureuserInfoNull");
      }
    }
  }catch(DbsException dex){
    exceptionBean = new ExceptionBean(dex);
    dex.printStackTrace();
    logger.debug(dex.toString());
    if(dex.containsErrorCode(10201)){
      exceptionBean.setMessageKey("errors.unable.to.access.file");
    }
    logger.error(exceptionBean.getMessage());
    saveErrors(request,exceptionBean.getActionErrors());
    try{
      if(userInfo==null)
        target=new String("failureuserInfoNull");
      if(userInfo!=null)
        target=new String("failureInSession");
    }catch(Exception e){
      target=new String("failureuserInfoNull");
    }
  }catch(Exception ex){
    ActionError actionError = new ActionError("errors.unable.to.display.file");
    errors.add(ActionErrors.GLOBAL_ERROR,actionError);            
    saveErrors(request,errors);
    try{
      if(userInfo==null)
        target=new String("failureuserInfoNull");
      if(userInfo!=null)
        target=new String("failureInSession");
    }catch(Exception e){
      target=new String("failureuserInfoNull");
    }
  }finally{
    if( dbsLibrarySession != null ){
      try{
        dbsLibrarySession.disconnect();
      }catch (DbsException dbsEx) {
        logger.debug(dbsEx.toString());
      }
    }
  }
  logger.info("Exiting ShowContentAction...");
  if(target.equals("success"))
    return null;
  else
    return mapping.findForward(target);
}   
}
