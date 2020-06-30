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
 * $Id: ChangeDocWfStatusBean.java,v 1.2 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.agents.DocumentLogAgent;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose: To change document workflow status to "approved" or "rejected"
 *            submission to workflow.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    18/05/2005
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class ChangeDocWfStatusBean  {

private String relPath;             /* relative path to access GeneralActionsParams.xml */
private Long documentId;            /* document id to be accessed */
private String status;              /* document's Wf status indicator */
public ExceptionBean exBean = null;
public String forwardTo;
ParseXMLTagUtil parseUtil = null;
DbsLibraryService dbsLibraryService;
DbsLibrarySession dbsLibrarySession=null;
DbsPublicObject dbsPublicObject = null;
DbsCleartextCredential dbsCleartextCredential=null;
String domain;                      /* 4 parameters necessary to start a dbsLibraryService*/
String ifsService;
String ifsSchemaPassword;
String serviceConfiguration;
Logger logger = null;
String event = new String();
String[] eventArray = null;
String[] prevEventArray = null;
DbsAttributeValue docEventLog= null;
String lastModifyDate = new String(); 
String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
DateHelper dateUtil = new DateHelper();

  /**         ChangeDocWfStatusBean constructor
   * @param:  relativePath(for GeneralActionParams.xml),documentId,documentStatus
   */

  public ChangeDocWfStatusBean(String relPath,Long documentId,String status) {
    logger = Logger.getLogger("DbsLogger");
    this.relPath = relPath;
    this.documentId = documentId;
    this.status = status;
    try{
      parseUtil = new ParseXMLTagUtil(relPath);
      if(parseUtil !=null){
        logger.debug("parseUtility instantiated correctly.");
      }
    }catch(Exception ex){
      logger.error("Exception occurred in ChangeDocWfStatusBean constructor");
      ex.printStackTrace();
    }
  }
  
  /**
   * Purpose: to establish credentials inorder to change document wf status
   * @return: true,if credentials established,else false.
   */
  private boolean establishCredentials(){
        boolean returnValue = false;
        logger.info("Logging User ...");
        String user4Credentials = parseUtil.getValue("sysaduser","Configuration");
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
          returnValue = true;
          logger.debug("User logged in successfully");    
        }catch(DbsException dex){
            logger.error("User login unsuccessful");
            logger.error("DbsException: "+dex.getMessage());
            dex.printStackTrace();
            returnValue = false;
        }catch(Exception ex)  {
            logger.error("User login unsuccessful");
            logger.error("Exception: "+ex.getMessage());
            ex.printStackTrace();
            returnValue = false;
        }
        logger.debug("returnValue: "+returnValue);
        return returnValue;
  }



    /**
     * Purpose: to change document workflow status to "approved" or "rejected".
     * @param:  none
     * @return: boolean true if status changed successfully else false.
     */
    public boolean changeWorkflowStatus() {
      
      boolean returnValue = false;
      
      try{
      
        if(establishCredentials()){
        
          dbsLibrarySession.setAdministrationMode(true);
          
          dbsPublicObject = dbsLibrarySession.getPublicObject(documentId).getResolvedPublicObject();
          DbsAttributeValue docWfStatusAttrValue = null;          
          
          if(dbsPublicObject instanceof DbsDocument){
            logger.debug("dbsPublicObject is an instance of DbsDocument");
            docWfStatusAttrValue = dbsPublicObject.getAttribute(DbsDocument.WORKFLOW_STATUS);
            if( docWfStatusAttrValue!= null){
              dbsPublicObject.getResolvedPublicObject().setAttribute(DbsDocument.WORKFLOW_STATUS,docWfStatusAttrValue.newAttributeValue(status));
              returnValue = true;    
            }else{
              logger.debug("docWfStatusAttrValue is null");
            }
          }else{
            logger.debug("dbsPublicObject is not an instance of DbsDocument");
          }
        }
      }catch( DbsException dex ){
        logger.error("DbsException: "+dex.getMessage());
        exBean = new ExceptionBean(dex);
       
        dex.printStackTrace();

      }catch( Exception ex ){
        logger.error("DbsException: "+ex.getMessage());
        exBean = new ExceptionBean(ex);
       
        ex.printStackTrace();
        
      }
      logger.debug("returnValue: "+returnValue);
      return returnValue;
    }
    
    
  public void logEvent( String action ){

  // DbsDocument dbsDocument=null;
   
    try{
      if(establishCredentials()){
        dbsLibrarySession.setAdministrationMode(true);          
        dbsPublicObject = dbsLibrarySession.getPublicObject(documentId).getResolvedPublicObject();      
        DocumentLogAgent.isGettingUpdated=true;
      //  dbsDocument = dbsLibrarySession.getPublicObject(this.documentId).getResolvedPublicObject().getDocumentObject();
        docEventLog= dbsPublicObject.getAttribute(DbsDocument.AUDIT_INFO);
        prevEventArray = docEventLog.getStringArray(dbsLibrarySession);
        action="Samhita"+"\t"+action;
        doLog(prevEventArray,action); 
        DocumentLogAgent.isGettingUpdated=false;
      }
    }catch(DbsException dex){
      logger.error("DbsException thrown...");
      logger.error("Error Message: "+dex.getMessage());
      DocumentLogAgent.isGettingUpdated=false;
    }catch(Exception ex){
      logger.error("exception: "+ex.toString());
      logger.error("Attribute not set initially");   
      DocumentLogAgent.isGettingUpdated=false;
    }  
   
  }



    /**Purpose: This method was coded so that the agent could use it directly
     *          to log events.As of yet, only events trapped by Samhita are logged.
     *@param  : dbsDcocument,userName,prevEventArray(log from "AUDIT_LOG"),action          
     */ 
    public void doLog(String[] prevEventArray,String action) {
    
    try{
    
      logger.debug("dbsDocument Id: "+dbsPublicObject.getId());
      
      eventArray=new String[prevEventArray.length+1];
            
      lastModifyDate=dateUtil.format(dbsPublicObject.getLastModifyDate(),DATE_FORMAT);
      logger.debug("lastModifyDate: "+lastModifyDate);
      
      logger.debug("action: "+action);
      event = forwardTo+"\t"+lastModifyDate+"\t"+action;
      logger.debug("event: "+event);
      
      
      int i=0;
      logger.debug("eventArray.length: "+eventArray.length);  
      logger.debug("prevEventArray.length: "+prevEventArray.length);
      
  
        for(i=0;i<prevEventArray.length;i++)
          logger.debug("prevEventArray["+i+"]: "+prevEventArray[i]);      
          
        for(i=0;i<prevEventArray.length;i++){
            eventArray[i] = prevEventArray[i];
        }

      eventArray[i] = event;
      
      
      dbsPublicObject.setAttribute(DbsDocument.AUDIT_INFO,docEventLog.newAttributeValue(eventArray));
      logger.debug("is attribute null now: "+dbsPublicObject.getAttribute(DbsDocument.AUDIT_INFO).isNullValue());
      
      for(int j=0;j<eventArray.length;j++)
        logger.debug("eventArray["+j+"]: "+eventArray[j]);
      
      
    }catch(Exception ex){
      logger.error("Exception occurred");
      logger.error("Exception: "+ex.toString());        
    }catch(DbsException dex){
      logger.error("DbsException occurred");
      logger.error("Exception: "+dex.getErrorMessage());        
    }
    
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
