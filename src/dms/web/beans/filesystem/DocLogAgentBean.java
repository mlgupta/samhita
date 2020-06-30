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
 * $Id: DocLogAgentBean.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/*dms package references */
import dms.web.agents.DocumentLogAgent;
import dms.web.beans.filesystem.DateHelperForFileSystem;
/* cmsdk API */
import oracle.ifs.beans.Document;
import oracle.ifs.beans.LibrarySession;
import oracle.ifs.common.AttributeValue;
import oracle.ifs.common.IfsException;
/** 
 * Purpose                : Bean used to log various operations/actions   
 *                          performed on a document as doc attribute "AUDIT_LOG"
 * @author                : Suved Mishra
 * @version               : 1.0
 * Date Of Creation       : 09-02-2005
 * Last Modified by       :
 * Last Modification Date :
 */
public class DocLogAgentBean  {
  private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
  //Logger logger= Logger.getLogger("DbsLogger");
  AttributeValue docEventLog= null;
  DateHelperForFileSystem dateUtil = new DateHelperForFileSystem();
  String lastModifyDate = new String(); 
  String event = new String();
  String[] eventArray = null;
  String[] prevEventArray = null;
  Document dbsDocument = null;
  String userName=null;
    
  public void logEvent(LibrarySession dbsLibrarySession,Long docId,String action){
    boolean isNull = false; /* boolean used to check if AUDIT_LOG attribute is null */
    
    try{
      DocumentLogAgent.isGettingUpdated=true;
      dbsDocument = (Document)(dbsLibrarySession.getPublicObject(docId).getResolvedPublicObject());
      docEventLog= dbsDocument.getAttribute("AUDIT_INFO");
      if(docEventLog == null){
       // logger.debug("docEventLog is null");
        isNull = true;
      }
      userName=dbsLibrarySession.getUser().getName();
      prevEventArray = docEventLog.getStringArray(dbsLibrarySession);
      
      if(prevEventArray==null){    
       // logger.debug("prevEventArray is null");
        prevEventArray=new String[1];
        isNull=true;
      }
     action="Samhita"+"\t"+action;
     doLog( dbsDocument, userName, prevEventArray,action,isNull); 
     DocumentLogAgent.isGettingUpdated=false;
    }catch(IfsException dex){
      //logger.debug("DbsException thrown...");
      dex.printStackTrace();
      DocumentLogAgent.isGettingUpdated=false;
    }catch(Exception ex){
      //logger.debug("exception: "+ex.toString());
      ex.printStackTrace();  
      DocumentLogAgent.isGettingUpdated=false;
    }
  }
    
  public void logEvent(LibrarySession dbsLibrarySession,String userName,Long docId,String action){
    boolean isNull = false; /* boolean used to check if AUDIT_LOG attribute is null */
    try{
      dbsDocument = (Document)(dbsLibrarySession.getPublicObject(docId).getResolvedPublicObject());
      docEventLog= dbsDocument.getAttribute("AUDIT_INFO");
      if(docEventLog == null){
        isNull = true;
      }
      prevEventArray = docEventLog.getStringArray(dbsLibrarySession);
      if(prevEventArray==null){    
        prevEventArray=new String[1];
        isNull=true;
      }
      doLog( dbsDocument, userName, prevEventArray,action,isNull); 
    }catch(IfsException dex){
      dex.printStackTrace();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
    
  /**
   * Purpose: This method was coded so that the agent could use it directly
   *          to log events.As of yet, only events trapped by Samhita are logged.
   *@param  : dbsDcocument,userName,prevEventArray(log from "AUDIT_LOG"),action          
   */ 
  public void doLog(Document dbsDocument,String userName,String[] prevEventArray,String action,boolean isNull) {
    try{
      /* */ 
      if(isNull){
        eventArray=new String[1];
        //action = "Document not uploaded through Samhita";
      }else{
        eventArray=new String[prevEventArray.length+1];
      }      
      lastModifyDate=dateUtil.format(dbsDocument.getLastModifyDate(),DATE_FORMAT);
      event = userName+"\t"+lastModifyDate+"\t"+action;
      int i=0;
      if(!isNull){         
        for(i=0;i<prevEventArray.length;i++){
          eventArray[i] = prevEventArray[i];
        }
      }  
      eventArray[i] = event;
      dbsDocument.setAttribute("AUDIT_INFO",docEventLog.newAttributeValue(eventArray));
    }catch(IfsException dex){
      dex.printStackTrace(); 
    }catch(Exception ex){
      ex.printStackTrace();      
    }
  }
}   
  
