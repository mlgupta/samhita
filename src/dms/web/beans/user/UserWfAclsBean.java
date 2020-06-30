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
 * $Id: UserWfAclsBean.java,v 1.2 2006/03/13 15:29:15 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.user;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsPublicObject;
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.web.beans.utility.SearchUtil;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to perform wf acls operations .
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    15-12-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class UserWfAclsBean  {

  private String user4Credentials;    /* userName for establishing credentials */
  private String user4WfAcl;          /* userName whose Wf ACL is to be fetched */
  private String [] userWfAcls;       /* user's Wf ACL names */
  private String relPath;             /* relative path for parsing "GeneralActionParams.xml" */
  
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
  
   

  /**         UserWfAclsBean constructor
   * @param:  user4WfAcl,relativePath(for GeneralActionParams.xml)
   */

  public UserWfAclsBean(String user4WfAcl,String relativePath) {
    this.user4WfAcl = user4WfAcl;
    try{
        relPath = relativePath;
        parseUtil = new ParseXMLTagUtil(relPath);
        this.user4Credentials = parseUtil.getValue("sysaduser","Configuration");
    }catch(Exception ex){
      logger.debug("Exception: "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  /**
   * Purpose: to establish credentials inorder to fetch WfAcl
   * @return: true,if credentials established,else false.
   */
  private boolean establishCredentials(){
        logger.debug("Logging User ...");
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
            logger.debug("User login unsuccessful");
            logger.debug("DbsException: "+dex.toString());
            dex.printStackTrace();
            return false;
        }catch(Exception ex)  {
            logger.debug("User login unsuccessful");
            logger.debug("Exception: "+ex.toString());
            ex.printStackTrace();
            return false;
        }
        
        return true;
  }
  
  
  /**
   * Purpose: to obtain WorkFlow ACLs for user4WfAcl
   * @param:  none
   * @return: An Array of ACLs or null if no ACL is set 
   */
  
  public DbsPublicObject[] getUserWfAcls(){
  
    DbsPrimaryUserProfile userProfile4WfAcl;
    DbsDirectoryUser[] allDirectoryUsers; 
    DbsPropertyBundle defaultAcls;
    DbsProperty workFlowAclProp;
    DbsAttributeValue dbsWorkFlowAttrValue;
    String workFlowAclDefault="WORKFLOW_NOT_ENABLED";
    DbsPublicObject [] dbsACLs = null;
    try{
    
      if(establishCredentials()){
        dbsLibrarySession.setAdministrationMode(true);
        if(dbsLibrarySession.isConnected()) {
          logger.debug("Session Id: "+dbsLibrarySession.getId());
          /* obtain user4WfAcl's Directory user object,Primary User Profile*/
          directoryUser4WfAcl = (DbsDirectoryUser)SearchUtil.findObject(dbsLibrarySession,DbsDirectoryUser.CLASS_NAME,user4WfAcl);
          user4WfAclProfile = directoryUser4WfAcl.getPrimaryUserProfile();
          /* obtain wfAclName for user4WfAcl */
          if((directoryUser4WfAcl!=null) && (user4WfAclProfile!=null)){
            logger.debug("directoryUser4WfAcl is not null");
            defaultAcls = user4WfAclProfile.getDefaultAcls();
            if(defaultAcls!=null){
              workFlowAclProp = defaultAcls.getProperty("WORKFLOWACL");
              /* if workFlowAclProp!=null */ 
              if((workFlowAclProp!=null) ){
                dbsWorkFlowAttrValue=workFlowAclProp.getValue();
                try{
                  logger.debug("WF ACL is not null");
                  dbsACLs = dbsWorkFlowAttrValue.getPublicObjectArray(dbsLibrarySession);
                }catch(DbsException dbsEx){           
                  logger.error("Exception occurred while obtaining workFlowAcl.getName()...");
                  logger.error(dbsEx.toString());
                  dbsEx.printStackTrace();                        
                }catch(Exception aclNotFoundExcep){   /* when WfACL applied is accidently deleted ...*/
                  logger.error("Exception occurred while obtaining workFlowAcl.getName()...");
                  logger.error(aclNotFoundExcep.toString());
                  aclNotFoundExcep.printStackTrace();
                }
              }else{
                logger.debug("workFlowAclProp is set to null or default.");
              }
            }else{
              logger.error("No Default Acls set... So WfACL cannot be obtained.");
            }
          }
        }else{
            logger.debug("Cannot connect to library session");
        }
      } /* end if 4 establishCredentials() */
    }catch( DbsException dex){
      logger.error("Exception occurred while obtaining workFlowAcl.getName()...");
      logger.error(dex.toString());
      dex.printStackTrace();  
    }catch( Exception ex ){
      logger.error("Exception occurred while obtaining workFlowAcl.getName()...");
      logger.error(ex.toString());
      ex.printStackTrace();
    } 
     return dbsACLs;
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
      logger.error("Exception occurred while obtaining workFlowAcl.closeThisWfSession()...");
      logger.error(dex.toString());
      dex.printStackTrace();
    }
  }
}