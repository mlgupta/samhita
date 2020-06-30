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
 * $Id: DocEventLogBean.java,v 1.7 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/*dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsVersionDescription;
import dms.web.actions.filesystem.FolderDocRenameAction;
import dms.web.agents.DocumentLogAgent;
import dms.web.beans.filesystem.DateHelperForFileSystem;
/* Logger API */
import org.apache.log4j.Logger;
/** 
 * Purpose                : Bean used to log various operations/actions   
 *                          performed on a document as doc attribute "AUDIT_LOG"
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 09-02-2005
 * Last Modified by       :
 * Last Modification Date :
 */
public class DocEventLogBean  {

    private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
    Logger logger= Logger.getLogger("DbsLogger");
    DbsAttributeValue docEventLog= null;
    DateHelperForFileSystem dateUtil = new DateHelperForFileSystem();
    String lastModifyDate = new String(); 
    String event = new String();
    String[] eventArray = null;
    String[] prevEventArray = null;
    DbsDocument dbsDocument = null;
    String userName=null;
    
  public void logEvent(DbsLibrarySession dbsLibrarySession,Long docId,String action){
  
  boolean isNull = false; /* boolean used to check if AUDIT_LOG attribute is null */
    
    try{
      DocumentLogAgent.isGettingUpdated=true;
      logger.debug("ClassName : "+dbsLibrarySession.getPublicObject(docId).getClassname());
      dbsDocument = dbsLibrarySession.getPublicObject(docId).getResolvedPublicObject().getDocumentObject();
      docEventLog= dbsDocument.getAttribute(DbsDocument.AUDIT_INFO);
      if(docEventLog == null){
        logger.debug("docEventLog is null");
        isNull = true;
      }
      userName=dbsLibrarySession.getUser().getName();
      prevEventArray = docEventLog.getStringArray(dbsLibrarySession);
      
      if(prevEventArray==null){    
        logger.debug("prevEventArray is null");
        prevEventArray=new String[1];
        isNull=true;
      }
     action="Samhita"+"\t"+action;
     doLog( dbsDocument, userName, prevEventArray,action,isNull); 
     DocumentLogAgent.isGettingUpdated=false;
    }catch(DbsException dex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(dex.toString());
       DocumentLogAgent.isGettingUpdated=false;
    }catch(Exception ex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(ex.toString());
       DocumentLogAgent.isGettingUpdated=false;
    }
   
  }
    
    
  public void logEvent(DbsLibrarySession dbsLibrarySession,String userName,Long docId,String action){
  
    boolean isNull = false; /* boolean used to check if AUDIT_LOG attribute is null */
    
    try{
      dbsDocument = dbsLibrarySession.getPublicObject(docId).getResolvedPublicObject().getDocumentObject();
      docEventLog= dbsDocument.getAttribute(DbsDocument.AUDIT_INFO);
      if(docEventLog == null){
        logger.debug("docEventLog is null");
        isNull = true;
      }
      prevEventArray = docEventLog.getStringArray(dbsLibrarySession);
      
      if(prevEventArray==null){    
        logger.debug("prevEventArray is null");
        prevEventArray=new String[1];
        isNull=true;
      }
      doLog( dbsDocument, userName, prevEventArray,action,isNull); 
    }catch(DbsException dex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(dex.toString());
    }catch(Exception ex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(ex.toString());
      logger.error("Attribute not set initially");      
    }
    
   
  }
    
    /**Purpose: This method was coded so that the agent could use it directly
     *          to log events.As of yet, only events trapped by Samhita are logged.
     *@param  : dbsDcocument,userName,prevEventArray(log from "AUDIT_LOG"),action          
     */ 
    public void doLog(DbsDocument dbsDocument,String userName,String[] prevEventArray,String action,boolean isNull) {
    
    try{
    
      logger.debug("dbsDocument Id: "+dbsDocument.getId());
      
      /* */ 
      if(isNull){
        logger.debug("isNull: "+isNull);
        eventArray=new String[1];
        //action = "Document not uploaded through Samhita";
      }else{
        logger.debug("isNull: "+isNull);
        eventArray=new String[prevEventArray.length+1];
      }      
      lastModifyDate=dateUtil.format(dbsDocument.getLastModifyDate(),DATE_FORMAT);
      logger.debug("lastModifyDate: "+lastModifyDate);
      
      logger.debug("action: "+action);
      event = userName+"\t"+lastModifyDate+"\t"+action;
      logger.debug("event: "+event);
      
      
      int i=0;
      logger.debug("eventArray.length: "+eventArray.length);  
      logger.debug("prevEventArray.length: "+prevEventArray.length);
      
      if(!isNull){  
        for(i=0;i<prevEventArray.length;i++)
          logger.debug("prevEventArray["+i+"]: "+prevEventArray[i]);      
          
        for(i=0;i<prevEventArray.length;i++){
            eventArray[i] = prevEventArray[i];
        }
      }  
      eventArray[i] = event;
      
      
      dbsDocument.setAttribute(DbsDocument.AUDIT_INFO,docEventLog.newAttributeValue(eventArray));
      logger.debug("is attribute null now: "+dbsDocument.getAttribute(DbsDocument.AUDIT_INFO).isNullValue());
      
      for(int j=0;j<eventArray.length;j++)
        logger.debug("eventArray["+j+"]: "+eventArray[j]);
      
      
    }catch(DbsException dex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(dex.toString());
    }catch(Exception ex){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(ex.toString());
    }
    
  }
  
  public void logPrevFamilyEvents( DbsLibrarySession dbsLibrarySession,Long familyId ){
    DbsPublicObject dbsPO = null;
    DbsFamily dbsFam = null;
    DbsVersionDescription[] allVds = null;
    DbsDocument dbsDoc = null;
    DbsAttributeValue dbsAttr = null;
    DbsAttributeValue prevEventAttr = null;
    try{
      dbsPO = dbsLibrarySession.getPublicObject(familyId);
      if( dbsPO instanceof DbsFamily ){
        dbsFam = (DbsFamily)dbsPO;
        allVds = dbsFam.getPrimaryVersionSeries().getVersionDescriptions();
        int prevVersionNumber = allVds.length-2;
        //logger.debug("prevVersionNumber : "+prevVersionNumber);
        prevEventAttr = (DbsAttributeValue)((DbsDocument)(allVds[prevVersionNumber].getDbsPublicObject())).getAttribute(DbsDocument.AUDIT_INFO);
        dbsDoc = (DbsDocument)dbsFam.getPrimaryVersionSeries().getLastVersionDescription().getDbsPublicObject();
        dbsDoc.setAttribute(DbsDocument.AUDIT_INFO,prevEventAttr);
      }
    }catch ( DbsException dbsEx ) {
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(dbsEx.toString());
    }catch( Exception ex ){
      logger.error("Exception occurred in DocEventLogBean ...");
      logger.error(ex.toString());
    }
  }
}   
  
