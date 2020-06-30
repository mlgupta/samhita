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
 * $Id: LoginBean.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.loginout;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsExtendedUserProfile;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/* Struts API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to perform user login and return library session .
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class LoginBean  {
  /* member variables declaration goes here */
  public DbsLibraryService dbsService;
  public DbsLibrarySession dbsSession;
  private DbsCleartextCredential dbsCtC;
  public DbsDirectoryUser dbsUser;
  private String serviceName;
  private String schemaPassword;
  private String serviceConfiguration;
  private String domain;
  private String userName;
  private String userPassword;
  private String [] subscribersArr;
  private Logger logger;
  public LoginBean() {
  }

  /**
   * Constructs a new LoginBean object with the specified userName
   * and userPassword.
   * @param userName.
   * @param userPassword.
   */
  public LoginBean(String userName, String userPassword) {
    this.logger = Logger.getLogger("DbsLogger");
    if( (userName!= null || userName.trim().length()!=0) && 
        (userPassword!= null || userPassword.trim().length()!=0 ) ){
      this.userName = new String(userName);
      this.userPassword = new String(userPassword);
    }else{
      logger.error("UserName or Password Not Correctly Specified.Terminating...");
    }
  }

  /**
   * set the service parameters necessary for starting library service.
   * @param String serviceName
   * @param String schemaPassword
   * @param String serviceConfiguration
   * @param String domain
   */
  public void setServiceParams(String serviceName, String schemaPassword, 
          String serviceConfiguration, String domain) throws Exception{
    try{
      this.serviceName = serviceName;
      this.serviceConfiguration = serviceConfiguration;
      this.schemaPassword = schemaPassword;
      this.domain = domain;
    }catch(Exception exc){
      throw exc;
    }
  }

  /**
   * start the library service.
   * @param none
   */
  public void startDbsService() throws DbsException{
    try{
      if(DbsLibraryService.isServiceStarted(serviceName)){                   
        this.dbsService = DbsLibraryService.findService(serviceName);
        logger.info("Library Service Found");
      }else{              
        this.dbsService = DbsLibraryService.startService(serviceName,
                          schemaPassword, serviceConfiguration, domain);
        logger.info("Library Service Started ");
      }
      
    }catch(DbsException dbsEx){
      throw dbsEx;
    }
  }
  
  /**
   * set the user cleartext credentials object.
   * @param none
   */
  public void setdbsCtc() throws DbsException{
    try{
      dbsCtC = new DbsCleartextCredential(this.userName,this.userPassword);
      logger.info("ClearText Credentials Set Successfully");
    }catch( DbsException dbsEx ){
      throw dbsEx;
    }
  }

  /**
   * set the user library session.this can be done only after library service has 
   * been found or started. 
   * @param none
   */
  public void setUserSession() throws DbsException{
    try{
      this.dbsSession = dbsService.connect(dbsCtC,null);
      logger.info("User Session Obtained Successfully");
      this.dbsUser = dbsSession.getUser();
      this.dbsSession.setAdministrationMode(this.dbsUser.isAdminEnabled()||this.dbsUser.isSystemAdminEnabled());
    }catch( DbsException dbsEx ){
      throw dbsEx;
    }
  }  
  
  public String getSubscribersEMailId(){
    DbsDirectoryUser [] dbsUsers = null;
    DbsExtendedUserProfile emailProfile = null;
    StringBuffer emailIds = new StringBuffer(); 
    String[] users = null;
    int length = (getSubscribersArr() == null)?0:getSubscribersArr().length;
    logger.debug("length : "+length);
    //System.out.println("getSubscribersArr()[0]"+getSubscribersArr()[0]);
    try {
      users = getSubscribersArr();
      for( int index =0; index < length; index++ ){
        dbsUsers = SearchUtil.listUsersForWatch(this.dbsSession,users[index]);
        logger.debug("dbsUsers : "+dbsUsers[0].getDistinguishedName());
        if( dbsUsers !=null && dbsUsers.length!=0 ){
          emailProfile = SearchUtil.getEmailUserProfile(this.dbsSession,dbsUsers[0]);
          DbsAttributeValue emailAttr = emailProfile.getAttribute("EMAILADDRESS");
          emailIds = emailIds.append(emailAttr.getString(this.dbsSession));
          if( index < length-1 )
            emailIds = emailIds.append(",");
          }
        dbsUsers = null;
        emailProfile = null;
      }
    }catch (DbsException dbsEx) {
      dbsEx.printStackTrace();
    }
    String subscribersEmailIds = emailIds.toString();
    /*try{
      BufferedWriter out = new BufferedWriter(new FileWriter("toSendTo.txt"));
      out.write(subscribersEmailIds);
      out.close();
    }catch(IOException ioEx ){
      System.out.println(ioEx.toString());
    }*/
    return subscribersEmailIds;
  }
  
  /**
   * Terminates the user library session after the XML file has been created
   * successfully on the user's local FileSystem.
   * @param none 
   */
  public void terminateService(){
    try{
      this.dbsSession.disconnect();
    }catch( DbsException dbsEx ){
      dbsEx.printStackTrace();
    }finally{
      try {
        if( this.dbsSession != null ){
          this.dbsSession.disconnect();        
        }
      }catch (DbsException dbsEx) {
        dbsEx.printStackTrace();
      }
    }
  }

  public String getEmailId( DbsDirectoryUser watchDU ){
    DbsExtendedUserProfile watchUserProf = null;
    DbsAttributeValue mailAttr = null;
    String mailId = null;
    try{
      watchUserProf = SearchUtil.getEmailUserProfile(this.dbsSession,watchDU);
      if( watchUserProf!=null ){
        try{
          mailAttr = watchUserProf.getAttribute("EMAILADDRESS");
          if((mailAttr != null)){
            mailId = mailAttr.getString(this.dbsSession);
          }
        }catch (Exception e) {
          logger.error(e.toString());
        }
      }
    }catch( DbsException dbsEx ){
      logger.error(dbsEx.toString());
    }catch( Exception ex ){
      logger.error(ex.toString());
    }
    return mailId;
  }
  public String getSenderEmailId(){
    DbsExtendedUserProfile dbsUserProf = null; 
    try{
      dbsUserProf = SearchUtil.getEmailUserProfile(this.dbsSession,this.dbsUser);
      //String senderEmailId = dbsUserProf.getAttribute("EMAILADDRESS").getString(this.dbsSession); 
      /*try{
        BufferedWriter out = new BufferedWriter(new FileWriter("senderEmailid.txt"));
        out.write(senderEmailId);
        out.close();
      }catch(IOException ioEx ){
        System.out.println(ioEx.toString());
      }*/
      //return senderEmailId;
      return dbsUserProf.getAttribute("EMAILADDRESS").getString(this.dbsSession);
    }catch (DbsException dbsEx) {
      /*try{
        BufferedWriter out = new BufferedWriter(new FileWriter("senderEmailidErr.txt"));
        out.write(dbsEx.toString());
        out.close();
      }catch(IOException ioEx ){
        System.out.println(ioEx.toString());
      }*/
      logger.error(dbsEx.toString());
    }
    return null;
  }
  
  public DbsDirectoryUser getDbsUser() {
    return dbsUser;
  }

  public void setDbsUser(DbsDirectoryUser dbsUser) {
    this.dbsUser = dbsUser;
  }

  public String[] getSubscribersArr() {
    return subscribersArr;
  }

  public void setSubscribersArr(String[] subscribersArr) {
    this.subscribersArr = subscribersArr;
  }

  public String getPOName( Long poId ){
    try {
      return this.dbsSession.getPublicObject(poId).getName();
    }
    catch (DbsException dbsEx) {
      dbsEx.printStackTrace();
    }
    return null;
  }

/*  public static void main( String [] args ){
    LoginBean loginBean = new LoginBean("system","system");
    Logger logger = Logger.getLogger("DbsLogger");
    String [] subscribersArr = new String[1];
    subscribersArr[0]="SoftEngr";
    String emailIds = null;
    String poName = null;
    Long poId = new Long("356887");
    try{
        loginBean.setServiceParams("IfsDefaultService","cmsdk","SmallServiceConfiguration","ifs://user1.dbsentry.com:1521:cmsdk:CMSDK");
        loginBean.setdbsCtc();
        loginBean.startDbsService();
        loginBean.setUserSession();
        loginBean.setSubscribersArr(subscribersArr);
        emailIds = loginBean.getSubscribersEMailId();
        System.out.println("emailIds : "+emailIds);
        poName = loginBean.getPOName(poId);
        String senderEmailId = loginBean.getSenderEmailId();
        System.out.println("senderEmailId : "+senderEmailId);
    }catch( DbsException dbsEx ){
      logger.debug(dbsEx.toString());
    }catch (Exception e){
      logger.error(e.toString());
    }finally{
      if( loginBean.dbsSession!=null ){
        loginBean.terminateService();
      }
    }
  }*/

}