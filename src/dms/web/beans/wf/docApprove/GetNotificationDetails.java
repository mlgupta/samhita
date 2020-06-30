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
 * $Id: GetNotificationDetails.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* dms package references */ 
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.web.beans.utility.WfAclUtil;
/* Java API */
import java.math.BigDecimal;
import java.sql.Connection;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.WFTwoDArray;
import oracle.apps.fnd.wf.engine.WFNotificationAPI;
/* Oracle JDBc API */
import oracle.jdbc.pool.OracleDataSource;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to obtain notification details .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class GetNotificationDetails   {

String relativePath=null;
Logger logger = Logger.getLogger("DbsLogger");
ParseXMLTagUtil parseUtil; 
WfAclUtil wfAclUtil;
BigDecimal notificationId=null;
public String subject=null;
public String body=null;
public String docUrl;

  public GetNotificationDetails(String relativePath, String notificationId) {  
      this.relativePath=relativePath;
      parseUtil = new ParseXMLTagUtil(relativePath);
      this.notificationId = new BigDecimal(notificationId);
  }
  
  public void fetchNotificationDetails(){
    
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
                 

      

      if (ctx.getDB().getConnection() == null)
      {
          logger.debug("Connection Problem");
          
      }
      logger.debug("Instantiated WFDB and WFContext.");
      
      logger.debug("Opening notification "+ notificationId);
      subject=WFNotificationAPI.getSubject(ctx, notificationId);
      logger.debug(WFNotificationAPI.getSubject(ctx, notificationId));
      logger.debug(" ------------------------------------------------------------------");
      body=WFNotificationAPI.getBody(ctx, notificationId, "text/plain");
      logger.debug(WFNotificationAPI.getBody(ctx, notificationId, "text/plain"));
      this.docUrl=WFNotificationAPI.getAttrText(ctx,notificationId,"DOCUMENT_URL");      
      logger.debug("docUrl: "+docUrl);
      logger.debug("--------------------------------END--------------------------------");
      
      }catch(Exception e){  
        e.printStackTrace();
      }finally{
  
        try{
             
          if(conn!=null && !conn.isClosed())
            conn.close();
              
          }catch(Exception e){
            e.printStackTrace();
          }
    
      }
  }

 
}
