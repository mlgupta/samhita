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
 * $Id: InitiateRequisitionWF.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* Adapter Package references */
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsPublicObject;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.web.beans.utility.WfAclUtil;
/* Java API */
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.WFTwoDArray;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleDataSource;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to initiate approval workflow process.
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class InitiateRequisitionWF  {

String relativePath=null;
String workFlowAclName = null;
Logger logger = Logger.getLogger("DbsLogger");
ParseXMLTagUtil parseUtil; 
WfAclUtil wfAclUtil;
XMLBean xmlBean;
public ExceptionBean exBean;

public InitiateRequisitionWF(){
  
}

public InitiateRequisitionWF(String relativePath){

  this.relativePath=relativePath;
  parseUtil = new ParseXMLTagUtil(relativePath);
  
}

public InitiateRequisitionWF(String relativePath,String workFlowAclName){

  this.relativePath=relativePath;
  this.workFlowAclName = workFlowAclName;
  parseUtil = new ParseXMLTagUtil(relativePath);
  
}

        //public static void main(String args[]){
public void startReqWF(String requestor,String docUrl,DbsPublicObject document){
    String password;
    String database;
    String participant;
    String pause;
    String ans;
    
  
    // Variables for WFEngineAPI test
    BigDecimal mAmount;
    String iType, iKey, pr, uKey, owner;
    String myAttr;
    String myAttrType;
    String value;
    WFTwoDArray dataSource;
    String accKey;
    String approver;
  
    // Variables for WFNotificationAPI test
    BigDecimal myNid;
    BigDecimal amount;
    BigDecimal gid;
    BigDecimal count;
    String     userName;
    String     myDate;

    Connection conn=null;
        
    try{
      OracleDataSource ods = new OracleDataSource();
      // Sets the driver type
      ods.setDriverType(parseUtil.getValue("DriverType","Configuration"));
      // Sets the database server name
      ods.setServerName(parseUtil.getValue("ServerName","Configuration"));
      // Sets the database name
      ods.setDatabaseName(parseUtil.getValue("DatabaseName","Configuration"));
      // Sets the port number
      ods.setPortNumber(new Integer(parseUtil.getValue("PortNumber","Configuration")).intValue());
      // Sets the user name
      ods.setUser(parseUtil.getValue("User","Configuration"));
      // Sets the password
      ods.setPassword(parseUtil.getValue("Password","Configuration"));
      
      conn=ods.getConnection();
      logger.debug("Connecting to the database...");
      System.out.flush ();
              
      //WFDB myDB = new WFDB("owf_mgr", "dbs9901", "jdbc:oracle:thin:@", "cmsdk");
      WFDB myDB = new WFDB();
      myDB.setConnection(conn);
      WFContext ctx = new WFContext(myDB, "UTF8");
                 

      logger.debug("Instantiated WFDB and WFContext.");

      if (ctx.getDB().getConnection() == null){
        logger.debug("Connection Problem");
      }
      
      // Fetch data from Workflows.xml viz : processItemType and processName
      String workFlowFilePath = relativePath.replaceFirst("GeneralActionParam","Workflows");
      xmlBean = new XMLBean(workFlowFilePath);
      ArrayList wfNames = xmlBean.getAllNames();
      /* Use hashTable to fetch the processItemType, given the aclName */
      Hashtable hashTable = new Hashtable();
      for( int index = 0; index < wfNames.size(); index++ ){
        /* key is : processItemType, while value is : prefixName */
        hashTable.put((String)wfNames.get(index),xmlBean.getValue((String)wfNames.get(index),"PrefixName"));
      }
      iType = null;
      Enumeration keys = hashTable.keys();
      String thisKey = null;
      while( keys.hasMoreElements() ){
        thisKey = (String)keys.nextElement();
        if( this.workFlowAclName.startsWith((String)hashTable.get(thisKey)) ){
          iType = thisKey;
          break;              
        }
        thisKey = null;
      }
      pr = xmlBean.getValue(iType,"ProcessName");
      thisKey = null;
      //iType = parseUtil.getValue("iType","Configuration");
      //pr = parseUtil.getValue("pr","Configuration");
      uKey = null;
      owner = requestor.toUpperCase();
      logger.debug("Launching " + iType+ " with owner " + owner);
      iKey  = requestor.toUpperCase()+new String().valueOf(Calendar.getInstance().getTimeInMillis());
      String note="";
      //String requisitionDescription=parseUtil.getValue("requisitionDescription","Configuration");
      String requisitionDescription=xmlBean.getValue(iType,"requisitionDescription");
      String reqDocument=document.getName();
      String requisitionProcessOwner=owner;
      String remDocument=document.getName();
      String forwardFrom=null;
      String forwardTo =null;
      //String forwardTo=parseUtil.getValue("forwardTo","Configuration");
      logger.debug("requestor: "+requestor);
      wfNames = null;
      wfAclUtil=new WfAclUtil(requestor,relativePath,this.workFlowAclName);
      logger.debug("workFlowAclName : "+workFlowAclName);
      String wfAclName = wfAclUtil.getWfAclName();
      logger.debug("wfAclName : "+wfAclName);
      if( wfAclName!=null && !wfAclName.equals("")){
        if(wfAclUtil.getWfAces()){
          forwardTo=wfAclUtil.getNextApprover(null);
        }else{
          
        }
      }else{
        throw new Exception("Workflow ACL not enabled for "+requestor);
      }
      //forwardTo=new WfAclUtil(requestor,relativePath).getNextApprover(null);
      
      
      String documentUrl=docUrl;
      // create an item
      if (WFEngineAPI.createProcess(ctx, iType, iKey, pr))
        logger.debug("Created Item");
      else
      {
        logger.debug("createProcess failed");
        WFEngineAPI.showError(ctx);
      }

      // set attributes
      if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, "REQUISITION_DESCRIPTION",
              requisitionDescription))
        logger.debug("Requistion Desc"+ "requisitionDescription");
      else
      {
        WFEngineAPI.showError(ctx);
      }    
      
      if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, "NOTE",
              note))
        logger.debug("note set");
      else
      {
        WFEngineAPI.showError(ctx);
      }  
      
      if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, "REQ_DOCUMENT",
              reqDocument))
        logger.debug("REQ_DOCUMENT set");
      else
      {
        WFEngineAPI.showError(ctx);
      }  
      
       if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, "REQUISITION_PROCESS_OWNER",
              requisitionProcessOwner))
        logger.debug("REQUSITION_PROCESS_OWNER set");
      else
      {
        WFEngineAPI.showError(ctx);
      } 
      
      if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, "DOC_ID",
              new String().valueOf(document.getId())))
        logger.debug("DOC_ID set");
      else
      {
        WFEngineAPI.showError(ctx);
      } 

      String forward_to=null;
      if(forwardTo!=null){
          forward_to=forwardTo.toUpperCase();
      }
      logger.debug("FORWARD_TO set"+forwardTo);
      
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "FORWARD_TO",
          forward_to)){
        logger.debug("FORWARD_TO set"+forward_to);
      }else{
        WFEngineAPI.showError(ctx);
      } 
      
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "DOCUMENT_URL",
          docUrl)){
        logger.debug("DOC_URL set");
      }else{
        WFEngineAPI.showError(ctx);
      } 
      
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "REQUESTOR",
         requestor.toUpperCase())){
        logger.debug("REQUESTOR set");
      }else{
        WFEngineAPI.showError(ctx);
      }
      
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "RELATIVE_PATH",
          relativePath)){
        logger.debug("relativePath set");
      }else{
        WFEngineAPI.showError(ctx);
      }  
      
      if( WFEngineAPI.setItemAttrText(ctx,iType,iKey,"ACL_NAME",
          workFlowAclName) ){
        logger.debug("workflow acl name set");
      }else{
        WFEngineAPI.showError(ctx);
      }
      
      if (WFEngineAPI.startProcess(ctx, iType, iKey))
      logger.debug("Process Started successfully");
      else
      {
        logger.debug("launch failed");
        WFEngineAPI.showError(ctx);
      }
      
   // UpdateLockForWF lockDoc=new UpdateLockForWF(relativePath);
   // DbsLockObjectDefinition lockObjDef=new DbsLockObjectDefinition(lockDoc.systemSession);
   // lockObjDef.setLockState(DbsLockObjectDefinition.LOCKSTATE_HARDLOCK);
   // lockDoc.updateLock(lockObjDef,document);
   
  // get status and result for this item
      dataSource = WFEngineAPI.itemStatus(ctx, iType, iKey);
      logger.debug("Status and result for " + iType + "/" + iKey+ " = ");
      displayDataSource(ctx, dataSource);
    }catch(DbsException dbsExcep){
      exBean = new ExceptionBean(dbsExcep);      
      logger.error("Exception occured in InitiateRequisitionWF "+dbsExcep.getErrorMessage());
    }
    catch(Exception e){
      exBean = new ExceptionBean(e);          
      logger.error("Exception occured in InitiateRequisitionWF "+e.toString());       
      e.printStackTrace();
    }finally{
    
        try{
        
            wfAclUtil.closeThisWfSession();
            
            if(conn!=null && !conn.isClosed()){            
              conn.close();
            }
            
        }catch(Exception e){
          exBean = new ExceptionBean(e);        
          e.printStackTrace();
        }
      }          
   
    }
        
void displayDataSource(WFContext wCtx, WFTwoDArray dataSource){
 
   int c, column;
   
   int r, row;
   
   Object data;

   if (dataSource == null)
     return;

   row = dataSource.getRowCount();
   column = dataSource.getColumnCount();

   for (r = 0; r < row; r++){
     for (c = 0; c < column; c++){
       if (c > 0)
         logger.debug("\t");

       data = dataSource.getData(c, r);
       if (data != null)
         logger.debug(data);
     }
     logger.debug("\n");
   }
  }
}
