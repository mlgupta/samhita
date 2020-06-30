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
 * $Id: ObtainLink.java,v 1.0 2006/03/07 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* adapter package references */
import adapters.beans.LoginBean;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.ServletContext;
/* Logger API */
import org.apache.log4j.Logger;

/**
 * Purpose            : Bean called to generate link for supplied doc id.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 27-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class ObtainLink  {
  
  private ServletContext context;     // servlet context
  private String userId;              // userId
  private String userPassword;        // userPassword
  private String documentId;          // document id
  private String relativePath = null; // relativePath for GeneralActionParam.xml
  private String contextPath = null;  // contextPath           
  private Logger logger;              // logger
  private DbsLibrarySession dbsLibrarySession = null; // library session
  
  public ObtainLink() {
  }
  /**
   * Constructor for ObtainLink Class
   * @param documentId
   * @param userPassword
   * @param userId
   * @param context
   */
  public ObtainLink(ServletContext context, String userId, String userPassword,
                    String documentId){
    this.context = context;
    this.userId = userId;
    this.userPassword = userPassword;
    this.documentId = documentId;
    if( context != null ){
      this.contextPath = (String)context.getAttribute("contextPath");
      this.relativePath = context.getRealPath("/")+"WEB-INF"+File.separator+
                          "params_xmls"+File.separator+"GeneralActionParam.xml";
      LoginBean loginBean = new LoginBean(userId,userPassword,relativePath);
      dbsLibrarySession = loginBean.getUserSession();
    }
    this.logger = Logger.getLogger("DbsLogger");
    if( contextPath!= null && relativePath != null ){
      logger.info(" ObtainLink initialized successfully... ");
    }else{
      logger.info(" Error initializing ObtainLink... ");
    }
  }


  /**
   * Function called to obtain link for the document id submitted
   * @throws java.lang.Exception
   * @throws java.io.IOException
   * @throws java.io.UnsupportedEncodingException
   * @throws dms.beans.DbsException
   * @return String containing document link if encryption successful, else null
   * @param String requestURI
   * @param String toReplace with "showContentAction.do"
   */
  public String getLink(String requestURI,String toReplace) 
        throws DbsException,UnsupportedEncodingException,IOException,Exception {
    String cryptoPassword=new String();
    String encryptedString=new String();
    String linkString= null;
    
    /* If context path and relative path are set ,go ahead with encryption */
    if( this.contextPath!= null && this.relativePath != null ){
      try{
        CryptographicUtil crypto = new CryptographicUtil();
        String keyPath=crypto.getSystemKeyStorePath(contextPath);
        String pwdFilePath=crypto.getPwdFilePath(contextPath);
        
        ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
  
        /*before link generation, check for the existence of .password & .keyStore */
        /*failure, throw an Exception */
        if(!crypto.getSystemKeyStoreFile(contextPath)){
          Exception fnfEx = 
            new Exception("Problem in link generation.Kindly contact your System Administrator.");
          throw fnfEx;
        }else{
          /*else go ahead to encrypt the userpassword */
          /*obtain cryptoPassword from .password, a byte[]Stream from userId,
           * userpassword,docId and encrypt using crypto.encryptString()*/
          cryptoPassword=crypto.getCryptoPassword(pwdFilePath);
          String toBeEncrypted="userId="+userId+"&password="+userPassword+
                               "&documentId="+dbsLibrarySession.getPublicObject(
                               new Long(getDocumentId())).getResolvedPublicObject().getId();
  
          byte[] byteArrayToEncrypt= toBeEncrypted.getBytes();
  
          ByteArrayInputStream inStream= new ByteArrayInputStream(
                                                            byteArrayToEncrypt);
          logger.debug("inStream: "+inStream.getClass());
          ByteArrayInputStream enciStream=(ByteArrayInputStream)
                crypto.encryptString(inStream,userId,cryptoPassword,contextPath);
          int available=enciStream.available();
          logger.debug("available: "+available);
          byte[] byteArrayEncrypted=new byte[available];
          int byteCount=enciStream.read(byteArrayEncrypted,0,available);
  
          enciStream.close();
  
          /*if streamreading has been successful, encode the byte[]Encrypted to  
            encryptedString using Base64 Encoder and then URLEncode the latter */
          if(byteCount == available){
            encryptedString = new sun.misc.BASE64Encoder().encode(
                                                            byteArrayEncrypted);
            logger.debug("Base64Enc "+ encryptedString);
            String encodedStr=URLEncoder.encode(encryptedString,"UTF-8");
            /* construct linkString using value of <redirector> tag from 
             * "GeneralActionParam.xml" */
            linkString= parseUtil.getValue("redirector","Configuration")+
                               requestURI.replaceAll(toReplace,
                               "showContentAction.do")+"?auth="+encodedStr;
            logger.debug("linkString: "+linkString);
  
          }else{
            /*if stream reading has been unsuccessful,throw an exception */
            //target=new String("failure");
            Exception srEx = new Exception("Problem in reading stream.");
            throw srEx;
            //saveErrors(request,errors);
          }
        }
      }catch (DbsException dbsEx) {
        logger.debug(dbsEx.toString());
        throw dbsEx;
      }catch (UnsupportedEncodingException uEnEx) {
        logger.debug(uEnEx.toString());
        throw uEnEx;
      }catch (IOException ioEx) {
        logger.debug(ioEx.toString());
        throw ioEx;
      }catch (Exception ex) {
        logger.debug(ex.toString());
        throw ex;
      }finally{
        /* if dbsLibrarySession exists, disconnect it */
        if( dbsLibrarySession != null && dbsLibrarySession.isConnected() ){
          dbsLibrarySession.disconnect();
          dbsLibrarySession = null;
        }
      }
    }else{
      /* if context path and relative path are not set then do not go for 
       * encryption and log appropriate message */
      logger.debug("Failure in obtaining link for the document...");
      logger.debug("Appropriate files not found.");
    }
    return linkString;
  }

  /**
   * getter method for userId
   * @return String userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * setter method for userId
   * @param userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * getter method for userPassword
   * @return String userPassword
   */
  public String getUserPassword() {
    return userPassword;
  }

  /**
   * setter method for userPassword
   * @param userPassword
   */
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  /**
   * getter method for documentId
   * @return String documentId
   */
  public String getDocumentId() {
    return documentId;
  }

  /**
   * setter method for documentId
   * @param documentId
   */
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

}