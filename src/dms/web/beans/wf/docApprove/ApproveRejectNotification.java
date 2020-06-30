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
 * $Id: ApproveRejectNotification.java,v 1.2 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* dms package references */ 
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.web.beans.utility.WfAclUtil;
/* Java API */
import java.math.BigDecimal;
import java.sql.Connection;
/* Workflow API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.WFTwoDArray;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.apps.fnd.wf.engine.WFNotificationAPI;
/* Oracle JDBC API */
import oracle.jdbc.pool.OracleDataSource;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to approve or reject approval notifications .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class ApproveRejectNotification  {

String relativePath=null;
Logger logger = Logger.getLogger("DbsLogger");
ParseXMLTagUtil parseUtil; 
WfAclUtil wfAclUtil;
BigDecimal notificationId=null;
WFContext ctx=null;
WFDB myDB=null;
Connection conn=null;
String userName=null;
public String docId=null;


  public ApproveRejectNotification(String relativePath, String notificationId,String userName) {  
      this.relativePath=relativePath;
      parseUtil = new ParseXMLTagUtil(relativePath);
      this.notificationId = new BigDecimal(notificationId);
      this.userName=userName;
  }
  
 
  
  private void getDocumentId(String itemType,String itemKey){
    WFTwoDArray result=null;
     result=WFEngineAPI.getItemAttributes(ctx,itemType,itemKey);
     String docId=null;
     for(int rowIndex=0;rowIndex<result.getRowCount();rowIndex++){
       if(((String)result.getData(0,rowIndex)).equals("DOC_ID")){
         docId=(String)result.getData(2,rowIndex);
         break;
       }
     }
     this.docId= docId;
  }
  
  
  
  
  public void approveRequisition(String note,String itemType,String itemKey){
     try{
         getWFContext();
          WFTwoDArray result=null;          
         //WFNotificationAPI.setAttrText(ctx, notificationId, "NOTE", note);
         WFEngineAPI.setItemAttrText(ctx,itemType,itemKey,"NOTE",note);
         WFNotificationAPI.setAttrText(ctx, notificationId, "RESULT", "APPROVED");
          if (!WFNotificationAPI.respond(ctx, notificationId, "Requisition has been approved", userName))
            WFEngineAPI.showError(ctx);
          else{
            
            logger.debug("Approved successful by notification api.");
            getDocumentId( itemType, itemKey);
            logger.debug("docId obtained in approveRequisition");
            
           
          }
     }catch(Exception ex){
       ex.printStackTrace();
     }finally{
       if(conn!=null){
         try{
             conn.close();
         }catch(Exception ex){
           ex.printStackTrace();
         }
       }
     }
  }
  
   public void closeRequisition(){
     try{
         getWFContext();
          if (!WFNotificationAPI.close(ctx, notificationId, userName))
            WFEngineAPI.showError(ctx);
          else
            logger.debug("Closed successful by notification api.");
          
     }catch(Exception ex){
       ex.printStackTrace();
     }finally{
       if(conn!=null){
         try{
             conn.close();
         }catch(Exception ex){
           ex.printStackTrace();
         }
       }
     }
  }
  
  public void rejectRequisition(String note,String itemType,String itemKey){
     try{
         getWFContext();
         
        // WFNotificationAPI.setAttrText(ctx, notificationId, "NOTE", note);
        WFEngineAPI.setItemAttrText(ctx,itemType,itemKey,"NOTE",note);
         WFNotificationAPI.setAttrText(ctx, notificationId, "RESULT", "REJECTED");
         if (!WFNotificationAPI.respond(ctx, notificationId, "Requisition has been rejected", userName))
            WFEngineAPI.showError(ctx);
          else{
            logger.debug("Rejected successful by notification api.");
            getDocumentId( itemType, itemKey);
            logger.debug("docId obtained in rejectRequisition");            
          }
     }catch(Exception ex){
       ex.printStackTrace();
     }finally{
       if(conn!=null){
         try{
            conn.close();
         }catch(Exception ex){
           ex.printStackTrace();
         }
       }
     }
  }
  
  public WFContext getWFContext(){
    
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
    //String requestor="BLEWIS";
    //String docUrl="http//192.168.0.1:7778";
    //String docName="hello.doc";
    
   
        
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
      myDB = new WFDB();
      myDB.setConnection(conn);
      ctx = new WFContext(myDB, "UTF8");
      if (ctx.getDB().getConnection() == null){
          logger.debug("Connection Problem");
          return null;
      }
      logger.debug("Instantiated WFDB and WFContext.");
      
       
      }catch(Exception e){
   
  e.printStackTrace();
  }
  return ctx;
  }
}
