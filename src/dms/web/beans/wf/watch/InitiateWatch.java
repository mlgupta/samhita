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
 * $Id: InitiateWatch.java,v 1.5 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.watch;
/* Adapter Package references */
import adapters.beans.XMLBean;
/* dms package references */ 
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
 *	Purpose:             To initiate watch workflow for selected documents.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class InitiateWatch  {
String relativePath=null;
String userName = null;
String action = null;
Long poId = null;
Logger logger = Logger.getLogger("DbsLogger");
ParseXMLTagUtil parseUtil; 
XMLBean xmlBean;
public ExceptionBean exBean;

  public InitiateWatch(){
  }

  public InitiateWatch( String relativePath,String userName,String action,Long poId ){
    this.relativePath = new String(relativePath);
    this.parseUtil = new ParseXMLTagUtil(relativePath); 
    this.userName = new String(userName);
    this.action = new String(action);
    this.poId = poId;
    logger.debug("Initiate Watch instantiated successfully...");
  }
  
  public void startWatchProcess(){
    // Variables for WFEngineAPI test
    String iType, iKey, pr, uKey, owner;
    WFTwoDArray dataSource;
    Connection conn=null;
    try{
      // oracle datasource    
      OracleDataSource ods = new OracleDataSource();
      // Sets the driver type
      ods.setDriverType(parseUtil.getValue("DriverType","Configuration"));
      // Sets the database server name
      ods.setServerName(parseUtil.getValue("ServerName","Configuration"));
      // Sets the database name
      ods.setDatabaseName(parseUtil.getValue("DatabaseName","Configuration"));
      // Sets the port number
      ods.setPortNumber(new Integer(parseUtil.getValue(
                                    "PortNumber","Configuration")).intValue());
      // Sets the user name
      ods.setUser(parseUtil.getValue("User","Configuration"));
      // Sets the password
      ods.setPassword(parseUtil.getValue("Password","Configuration"));
      // obtain connection
      conn=ods.getConnection();
      logger.debug("Connecting to the database...");
      System.out.flush();
      // set new WFDB connection        
      WFDB myDB = new WFDB();
      myDB.setConnection(conn);
      // set new WF Context
      WFContext ctx = new WFContext(myDB, "UTF8");
      logger.debug("Instantiated WFDB and WFContext.");

      if(ctx.getDB().getConnection() == null){
        logger.debug("Connection Problem");
      }
      // set parameters for create item process
      // Fetch data from Workflows.xml viz : processItemType and processName
      String workFlowFilePath = relativePath.replaceFirst("GeneralActionParam","Workflows");
      xmlBean = new XMLBean(workFlowFilePath);
      iType = "SAM_WTH";
      pr = xmlBean.getValue(iType,"ProcessName");
      //iType = parseUtil.getValue("watchiType","Configuration");
      //pr = parseUtil.getValue("watchpr","Configuration");
      uKey = null;
      owner = userName.toUpperCase();
      logger.debug("Launching " + iType+ " with owner " + owner);
      iKey  = userName.toUpperCase()+
              new String().valueOf(Calendar.getInstance().getTimeInMillis());
      // create an item process for watch
      if(WFEngineAPI.createProcess(ctx, iType, iKey, pr)){
        logger.debug("Created Item");
      }else{
        logger.debug("createProcess failed");
        WFEngineAPI.showError(ctx);
      }
      // set ACT_MESSAGE as itemAttr
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "ACT_MESSAGE",action)){
        logger.debug("action message set");
      }else{
        WFEngineAPI.showError(ctx);
      }  
      // set PO_ID as itemAttr
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "PO_ID",poId.toString())){
        logger.debug("PO_ID set");
      }else{
        WFEngineAPI.showError(ctx);
      }  
      // set USER_NAME as itemAttr
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "USER_NAME",userName)){
        logger.debug("USER_NAME set");
      }else{
        WFEngineAPI.showError(ctx);
      } 
      // set REL_PATH as itemAttr
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "REL_PATH",relativePath)){
        logger.debug("relativePath set");
      }else{
        WFEngineAPI.showError(ctx);
      }  
      /* start the Watch process */    
      if (WFEngineAPI.startProcess(ctx, iType, iKey))
      logger.debug("Process Started successfully");
      else{
        logger.debug("launch failed");
        WFEngineAPI.showError(ctx);
      }
      // get status and result for this item
      dataSource = WFEngineAPI.itemStatus(ctx, iType, iKey);
      logger.debug("Status and result for " + iType + "/" + iKey+ " = ");
      displayDataSource(ctx, dataSource);
    }catch(Exception e){
      exBean = new ExceptionBean(e);          
      logger.error("Exception occured in InitiateWatch "+e.toString());       
      //e.printStackTrace();
    }finally{
      try{
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
    
    if(dataSource == null)
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