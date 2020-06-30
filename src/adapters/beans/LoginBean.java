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
 * $Id: LoginBean.java,v 1.8 2006/02/02 12:31:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Struts API */
import org.apache.log4j.Logger;

/**
 * Purpose            : Bean to facilitate user login after verifying credentials
 *                      return library session and provide method to terminate it.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 16-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class LoginBean  {
  /* member variables declaration goes here */
  private String serviceName;         /* service name, a parameter for starting library service */
  private String schemaPassword;      /* schema password, a parameter for starting library service */ 
  private String serviceConfiguration;/* service configuration,a parameter for starting library service */
  private String domain;              /* domain, a parameter for starting library service */
  private String userName;            /* repersents user name */
  private String userPassword;        /* repersents user password */
  private DbsCleartextCredential dbsCtC; /* represents user credentials object */
  private Logger logger;              /* represents logger for verbose logging */ 
  private ParseXMLTagUtil parseUtil;  /* utility bean for parsing xml files */
  public DbsLibraryService dbsService;/* represents library service */
  public DbsLibrarySession dbsSession;/* represents library session */
  public DbsDirectoryUser dbsUser;    /* represents directory user */

  public LoginBean() {
  }
  /**
   * Constructs a new LoginBean object with the specified userName
   * and userPassword.
   * @param userName.
   * @param userPassword.
   * @param XML File Path.
   */
  public LoginBean(String userName, String userPassword,String relativePath) {
    if( (userName!= null || userName.trim().length()!=0) && 
        (userPassword!= null || userPassword.trim().length()!=0 ) &&
        (relativePath!= null || relativePath.trim().length()!=0 ) ){
      this.logger = Logger.getLogger("DbsLogger");
      this.userName = new String(userName);
      this.userPassword = new String(userPassword);
      parseUtil = new ParseXMLTagUtil(relativePath);
      this.domain = parseUtil.getValue("Domain","Configuration");
      this.serviceName = parseUtil.getValue("IfsService","Configuration");
      this.schemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
      this.serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
      this.dbsSession = null;
      logger.debug("Login bean initialized successfully... ");
    }else{
      logger.debug("UserName or Password or XML File Path Not Correctly Specified.");
    }
  }

  /**
   * Constructs a new LoginBean object with the specified XML File Path
   * @param XML File Path.
   */
  public LoginBean(String relativePath) {
    if( (relativePath!= null || relativePath.trim().length()!=0 ) ){
      this.logger = Logger.getLogger("DbsLogger");
      parseUtil = new ParseXMLTagUtil(relativePath);
      this.userName = parseUtil.getValue("sysaduser","Configuration");
      this.userPassword = parseUtil.getValue("sysadpwd","Configuration");
      this.domain = parseUtil.getValue("Domain","Configuration");
      this.serviceName = parseUtil.getValue("IfsService","Configuration");
      this.schemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
      this.serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
      this.dbsSession = null;
      logger.debug("Login bean initialized successfully... ");
    }else{
      logger.debug("XML File Path Not Correctly Specified.");
    }
  }

  /**
   * set the user cleartext credentials object.
   * @param none
   */
  private void setdbsCtc() throws DbsException{
    try{
      dbsCtC = new DbsCleartextCredential(this.userName,this.userPassword);
      logger.debug("ClearText Credentials Set Successfully");
    }catch( DbsException dbsEx ){
      throw dbsEx;
    }
  }

  /**
   * start the library service.
   * @param none
   */
  private void startDbsService() throws DbsException{
    try{
      if(DbsLibraryService.isServiceStarted(serviceName)){                   
        this.dbsService = DbsLibraryService.findService(serviceName);
        logger.debug("Library Service Found");
      }else{              
        this.dbsService = DbsLibraryService.startService(serviceName,
                          schemaPassword, serviceConfiguration, domain);
        logger.debug("Library Service Started ");
      }
      
    }catch(DbsException dbsEx){
      throw dbsEx;
    }
  }

  /**
   * set the user library session.this can be done only after library service has 
   * been found or started. 
   * @param none
   */
  private void setUserSession() throws DbsException{
    try{
      this.dbsSession = dbsService.connect(dbsCtC,null);
      logger.debug("User Session Obtained Successfully");
      this.dbsUser = dbsSession.getUser();
      /* set administration mode according to user status */
      this.dbsSession.setAdministrationMode(this.dbsUser.isAdminEnabled()||
                                            this.dbsUser.isSystemAdminEnabled());
    }catch( DbsException dbsEx ){
      throw dbsEx;
    }
  }  

  /**
   * get the user library session.
   * @param none
   * @return user librarySession
   */
  
  public DbsLibrarySession getUserSession(){
    try{
      if( this.dbsSession == null ){
        setdbsCtc();
        startDbsService();
        setUserSession();
      }
    }catch (DbsException dbsEx) {
      logger.error("Exception occurred in LoginBean...");
      logger.error(dbsEx.toString());
    }
    return this.dbsSession;
  }

  /**
   * Terminates the user library session .
   * @param none 
   */
  public void terminateSession(){
    try{
      /* disconnect the library session */
      if (dbsSession!=null && dbsSession.isConnected()) {
        this.dbsSession.disconnect();
        this.dbsSession = null;
      }
      logger.debug("User Session Terminated Successfully");
    }catch( DbsException dbsEx ){
      logger.error("Exception occurred in LoginBean...");
      logger.error(dbsEx.toString());
    }finally{
      try {
        if( this.dbsSession != null && this.dbsSession.isConnected()){
          this.dbsSession.disconnect();
          this.dbsSession = null;
        }
      }catch (DbsException dbsEx) {
        logger.error("Exception occurred in LoginBean...");
        logger.error(dbsEx.toString());
      }
    }
  }

}