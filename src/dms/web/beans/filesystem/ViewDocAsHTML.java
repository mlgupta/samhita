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
 * $Id: ViewDocAsHTML.java,v 1.0 2006/05/09 17:52:34 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFormat;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
/* Java API */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *	Purpose           : To view the content of a document in HTML/PlainText  
 *                      format using context INSO filters
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 05-09-2006
 * 	Last Modified by  :    
 * 	Last Modified Date:    
 */

public class ViewDocAsHTML  {

  private DbsDocument document;             // document whose contents are to be viewed in HTML / Plaintext
  private DbsLibrarySession dbsSession;     // represents user library session
  private DbsLibraryService dbsService;     // represents library service
  private DbsCleartextCredential dbsCtC;    // represents credentials object
  public DbsDirectoryUser dbsUser;          // represents directory user object
  private String serviceName;               // represents service name
  private String schemaPassword;            // represents schema password
  private String serviceConfiguration;      // represents service configuration
  private String domain;                    // represents IFS domain
  private String userName;                  // represents user id
  private String userPassword;              // represents user password

  public ViewDocAsHTML() {
  }

  /**
   * Constructs a new ViewDocAsHTML object with the specified userName
   * and userPassword.
   * @param userName.
   * @param userPassword.
   */
  public ViewDocAsHTML(String userName, String userPassword) {
    System.out.println("Entering Constructor for ViewDocAsHTML ...");
    if( (userName!= null || userName.trim().length()!=0) && 
        (userPassword!= null || userPassword.trim().length()!=0 ) ){
      this.userName = new String(userName);
      this.userPassword = new String(userPassword);
    }else{
      System.err.println("UserName or Password Not Correctly Specified.Terminating...");
      System.exit(1);
    }
    System.out.println("Exiting Constructor for ViewDocAsHTML ...");
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
      System.out.println("serviceName  "+this.serviceName);
      System.out.println("serviceConfiguration  "+this.serviceConfiguration);
      System.out.println("schemaPassword  "+this.schemaPassword);
      System.out.println("domain  "+this.domain);
      if(DbsLibraryService.isServiceStarted(serviceName)){                   
        this.dbsService = DbsLibraryService.findService(serviceName);
        System.out.println("Library Service Found");
      }else{              
        this.dbsService = DbsLibraryService.startService(serviceName,
                          schemaPassword, serviceConfiguration, domain);
        System.out.println("Library Service Started ");
      }
      
    }catch(DbsException dbsEx){
      dbsEx.printStackTrace();
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
      System.out.println("ClearText Credentials Set Successfully");
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
      System.out.println("User Session Obtained Successfully");
      this.dbsUser = dbsSession.getUser();
      this.dbsSession.setAdministrationMode(this.dbsUser.isAdminEnabled()||
                                            this.dbsUser.isSystemAdminEnabled());
    }catch( DbsException dbsEx ){
      throw dbsEx;
    }
  }  
  
  /**
   * Terminates the user library session .
   * @param none 
   */
  public void terminateSession(){
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
      this.dbsSession = null;
    }
  }

  /**
   * Get the current Directory User
   * @return dbsUser
   */
  public DbsDirectoryUser getDbsUser() {
    return dbsUser;
  }

  /**
   * Set the current Directory User
   * @param dbsUser
   */
  public void setDbsUser(DbsDirectoryUser dbsUser) {
    this.dbsUser = dbsUser;
  }

  /**
   * Get the library session
   * @return dbsSession
   */
  public DbsLibrarySession getDbsSession() {
    return dbsSession;
  }

  /**
   * Set the library session
   * @param dbsSession
   */
  public void setDbsSession(DbsLibrarySession dbsSession) {
    this.dbsSession = dbsSession;
  }

  /**
   * Get the document whose contents are to be viewed in HTML/PlainText
   * @return document
   */
  public DbsDocument getDocument() {
    return document;
  }

  /**
   * Set the document whose contents are to be viewed in HTML/PlainText
   * @param document
   */
  public void setDocument(DbsDocument document) {
    this.document = document;
  }

  /**
   * To view the content of the document in HTML/PlainText format 
   * @param docId
   */
  public void viewDoc( Long docId ){
    BufferedReader brdr = null;
    BufferedWriter bwtr = null; 
    boolean plainText = false;
    try {
      this.document = (DbsDocument)dbsSession.getPublicObject(docId);
      DbsFormat format = document.getFormat();
      String mimeType = format.getMimeType();
      System.out.println("MimeType : "+mimeType);
      if( mimeType.equals("text/plain") ){
        plainText = true;
      }
      System.out.println("plainText : "+plainText);
      // create an HTML version of the document using Oracle Text INSO filters
      document.filterContent(plainText);
      brdr = new BufferedReader(document.getFilteredContent());
      String userHomeFolder = System.getProperty("user.home");
      System.out.println("userHomeFolder :"+userHomeFolder);
      String fileName = ( plainText )?"ViewDoc.txt":"ViewDoc.html";
      System.out.println("fileName : "+fileName);
      bwtr = new BufferedWriter(new FileWriter(fileName));
      bwtr.flush();
      //File file = new File(userHomeFolder+File.separator+"ViewDoc.html");
      String line = brdr.readLine();
      while (line != null){
        System.out.println(line);
        bwtr.write(line);
        bwtr.write("\n");
        line = brdr.readLine();
      }
    }catch (DbsException dbsEx) {
      System.err.println("Error occurred in viewDoc()...");
      System.err.println(dbsEx.toString());
      dbsEx.printStackTrace();
    }catch (IOException ioEx) {
      System.err.println("Error occurred in viewDoc()...");
      System.err.println(ioEx.toString());
      ioEx.printStackTrace();
    }catch (Exception ex) {
      System.err.println("Error occurred in viewDoc()...");
      System.err.println(ex.toString());
      ex.printStackTrace();
    }finally{
      /* it is mandatory to close all open streams */
      try{
        if( brdr != null ){
          brdr.close();
          brdr = null;
        }
        if( bwtr != null ){
          bwtr.close();
          bwtr = null;
        }
      }catch( Exception ex ){
        ex.printStackTrace();
      }
    }
    
  }

  /*public static void main( String [] args ){
    ViewDocAsHTML viewDocAsHTML = new ViewDocAsHTML("system","system");
    try{
        viewDocAsHTML.setServiceParams("IfsDefaultService","cmsdk","SmallServiceConfiguration","ifs://dms.dbsentry.com:1521:samdb:CMSDK");
        viewDocAsHTML.setdbsCtc();
        viewDocAsHTML.startDbsService();
        viewDocAsHTML.setUserSession();
        viewDocAsHTML.viewDoc(new Long(10678));
    }catch( DbsException dbsEx ){
      System.err.println(dbsEx.toString());
      dbsEx.printStackTrace();
    }catch (Exception e){
      System.err.println(e.toString());
      e.printStackTrace();
    }finally{
      if( viewDocAsHTML.getDbsSession()!=null ){
        System.out.println("Terminating Session");
        viewDocAsHTML.terminateSession();
      }
    }
  }*/

}