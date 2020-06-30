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
 * $Id: WfAclUtil.java,v 1.10 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose: To fetch workflow ACL associated with a user,individual ACEs in the
 *           corresponding ACL,for approval purposes.
 * 
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    27/04/2005
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class WfAclUtil  {
  
  private String user4Credentials;    /* userName for establishing credentials */
  private String user4WfAcl;          /* userName whose Wf ACL is to be fetched */
  private String [] userWfAcls;       /* user's Wf ACL names */
  private String wfAclName;           /* name of the workflow ACL to be used*/ 
  private String nextApprover;        /* authority name for next approval */
  private String relPath;             /* relative path for parsing "GeneralActionParams.xml" */
  private int nextAceIndex;           /* variable to hold index of next ACE to be fetched */
  private boolean isLastEntry=false;  /* check if given entry is last entry in ACL */
  
  Logger logger = Logger.getLogger("DbsLogger");
  
  ParseXMLTagUtil parseUtil;       /* ParserUtility for parsing "GeneralActionParams.xml" */
  DbsLibraryService dbsLibraryService;
  DbsLibrarySession dbsLibrarySession=null;
  DbsCleartextCredential dbsCleartextCredential=null;
  DbsDirectoryUser directoryUser4WfAcl = null;        /* DU object of user4WfAcl */
  DbsPrimaryUserProfile user4WfAclProfile = null;     /* user4WfAcl's PrimaryUserProfile */
  String domain;                      /* 4 parameters necessary to start a dbsLibraryService*/
  String ifsService;
  String ifsSchemaPassword;
  String serviceConfiguration;
  DbsAccessControlList workFlowAcl;   /* workFlowAcl ACL object */
  DbsAccessControlEntry[] wfAceArray; /* ACE [] in workFlowAcl */ 

  public WfAclUtil(){
    
  }

  /**         WfAclUtil constructor
   * @param:  user4WfAcl,relativePath(for GeneralActionParams.xml)
   */

  public WfAclUtil(String user4WfAcl,String relativePath ){
        
    this.user4WfAcl = user4WfAcl;
    
    nextAceIndex = 0;
    
    try{
        relPath = relativePath;
        parseUtil = new ParseXMLTagUtil(relPath);
        this.user4Credentials = parseUtil.getValue("sysaduser","Configuration");
    }catch(Exception ex){
      logger.error("Exception: "+ex.getMessage());
      ex.printStackTrace();
    }
    
  }

  /**         WfAclUtil constructor
   * @param:  user4WfAcl,relativePath(for GeneralActionParams.xml),wfAcl selected
   */

  public WfAclUtil(String user4WfAcl,String relativePath,String userWfAcl ){
        
    this.user4WfAcl = user4WfAcl;
    this.wfAclName = userWfAcl;
    nextAceIndex = 0;
    
    try{
        relPath = relativePath;
        parseUtil = new ParseXMLTagUtil(relPath);
        this.user4Credentials = parseUtil.getValue("sysaduser","Configuration");
    }catch(Exception ex){
      logger.error("Exception: "+ex.getMessage());
      ex.printStackTrace();
    }
    
  }
  
  /**
   * Purpose: to establish credentials inorder to fetch WfAcl
   * @return: true,if credentials established,else false.
   */
  private boolean establishCredentials(){
        logger.info("Logging User ...");
        String userPassword = parseUtil.getValue("sysadpwd","Configuration");
        try{        
          dbsCleartextCredential = new DbsCleartextCredential(user4Credentials,userPassword);
          ifsService = parseUtil.getValue("IfsService","Configuration");  
            
        if(DbsLibraryService.isServiceStarted(ifsService)){
            logger.info(ifsService + " is running");
            dbsLibraryService = DbsLibraryService.findService("IfsDefaultService");
        }else{
            logger.info("Starting Library Service...");            

            ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
            serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
            domain = parseUtil.getValue("Domain","Configuration");
                
            logger.info("IfsService : " + ifsService);
            logger.info("ServiceConfiguration : " + serviceConfiguration);
            logger.info("Domain : " + domain);
            //logger.info("ifsSchemaPassword: "+ifsSchemaPassword);
            dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
            logger.info("Library Service Started ");
            }
            
            try{
                dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
            }catch(DbsException dex){
                logger.error(dex.getMessage());
                dex.getErrorMessage();
                dex.printStackTrace();                
                if(dex.containsErrorCode(10170)){
                   throw dex;
                }
                else if(dex.getErrorCode() == 21008){
                    logger.info("This exception is thrown when library service started successfully");
                    logger.info("and then database went down");
                    logger.info("In this case Library Service need to be restarted");

                    ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
                    serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
                    domain = parseUtil.getValue("Domain","Configuration");

                    logger.info("Disposing Library Service...");
                    dbsLibraryService.dispose(ifsSchemaPassword);
                    logger.info("Disposing Library Service Complete");
                    
                    logger.info("ReStarting Library Service...");            
                    dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
                    logger.info("ReStarting Library Service Complete");
                    dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
                }
            }
          logger.debug("User logged in successfully");    
        }catch(DbsException dex){
            logger.error("User login unsuccessful");
            logger.error("DbsException: "+dex.getMessage());
            dex.printStackTrace();
            return false;
        }catch(Exception ex)  {
            logger.error("User login unsuccessful");
            logger.error("Exception: "+ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        
        return true;
  }
  /**
   * Purpose: to obtain WorkFlow ACL for user4WfAcl
   * @param:  none
   * @return: wfAclName corresponding to user4WfAcl 
   */
  
  public String getWfAclName(){

    try{
      if(establishCredentials()){
        dbsLibrarySession.setAdministrationMode(true);
        if(dbsLibrarySession.isConnected()) {
          logger.debug("Session Id: "+dbsLibrarySession.getId());
        }else{
          logger.debug("Cannot connect to library session");
        }
      } /* end if 4 establishCredentials() */
    }catch( DbsException dex){
      logger.error("DbsException: "+dex.getMessage());
      dex.printStackTrace();  
    }catch( Exception ex ){
      logger.error("Exception: "+ex.getMessage());
      ex.printStackTrace();
    } 
    //logger.debug("obtaining wfAcesArray: "+getWfAces());    
     return wfAclName;
    }
    
    
    
  /**
   * Purpose: to obtain aces in wfAclName 
   * @return: boolean true,if aces obtained,else false; 
   */

  public boolean getWfAces(){
    String workFlowAclName = getWfAclName();
    /* check if wfAclName is null,if so, return false,else obtain an array of aces */
    if(workFlowAclName==null){
      logger.debug("No aces obtained as WfAclName is null");
      return false;
    }else{
        try{
          workFlowAcl = (DbsAccessControlList)SearchUtil.findObject(dbsLibrarySession,DbsAccessControlList.CLASS_NAME,workFlowAclName);
          wfAceArray = workFlowAcl.getAccessControlEntrys();
          /* if wfAceArray is empty,return false,else true */
          if(wfAceArray == null){
            logger.debug("WfAcl possesses no ACEs...");
            return false;
          }else{
            logger.debug("wfAceArray.length: "+wfAceArray.length);
          }
        }catch(DbsException dex){
          logger.error("DbsException: "+dex.getMessage());
          dex.printStackTrace();
        }
    }
    return true;
  }

  
  /**
   * Purpose: to obtain next approver in wfAclName 
   * @param : String forwardFrom
   * @return: nextApproverName,if one exists,else null; 
   */
  
  public String getNextApprover(String forwardFrom){
     
    /* if no ACE exists then return null */
    if(wfAceArray == null){
    
      logger.debug("No approver as no ACEs obtained");
      
      return null;
      
    }else{  /* if forwardFrom == null, return first ACE name */
    
        try{
        
            if(forwardFrom == null){
            
              nextApprover = wfAceArray[nextAceIndex].getGrantee().getName();
              
            }else{ /* if forwardFrom != null, return first ACE name after forwardFrom */
            
              for(int i=0;i<wfAceArray.length;i++){
              
                if(wfAceArray[i].getGrantee().getName().equals(forwardFrom)){               
                     
                    nextApprover = wfAceArray[i+1].getGrantee().getName();
                    
                    logger.debug("nextApprover: "+nextApprover);
                    
                    return nextApprover; /* return ACE name immediately after forwardFrom */          
                  
                }
                
              }
              
              return null; /* if forwardFrom doesnot exist,return null */
            }
            
        }catch(DbsException dex){
          logger.error("DbsException: "+dex.getMessage());
          dex.printStackTrace();
        }
        
    }
    
    return nextApprover;
    
  }
  

  /**
   * Purpose: to check if forwardFrom is last entry 
   * @param : String forwardFrom
   * @return: boolean true,if so else false; 
   */

  
  public boolean isLast(String forwardFrom){
  
    /* if no ACE exists then return null */
    if(wfAceArray == null){
    
      logger.debug("No approver as no ACEs obtained");  
      
      return false;
      
    }else{  /* chk if forward_from is last ACE */
    
        try{
              for(int i=0;i<wfAceArray.length;i++){
              
                if(wfAceArray[i].getGrantee().getName().equalsIgnoreCase(forwardFrom)){
                
                      if(i == wfAceArray.length-1){
                      
                        isLastEntry=true; /* if forwardFrom is last entry */
                        
                      }                  
                    
                }
                
              }
            
        }catch(DbsException dex){
          logger.error("DbsException: "+dex.getMessage());
          dex.printStackTrace();
        }
        
    }
    
    return isLastEntry;
    
  }
  
  /**
   * Purpose: to close the Wf oriented session
   * @param : none
   * @return: none 
   */
  
  public void closeThisWfSession(){
    
    logger.debug("Disconnecting librarySession...");
    
    try{    
      if( dbsLibrarySession!=null && dbsLibrarySession.isConnected()){
        logger.debug("dbsLibrarySession id: "+dbsLibrarySession.getId()+" will now be disconnected");
        dbsLibrarySession.disconnect();  /* disconnect this Wf library session */
      }
    }catch( DbsException dex){
      logger.error("DbsException: "+dex.getMessage());
      dex.printStackTrace();
    }
    
  }
  
}