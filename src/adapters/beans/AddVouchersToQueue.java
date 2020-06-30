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
 * $Id: AddVouchersToQueue.java,v 1.8 2006/03/17 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* dms package reference */
import dms.web.beans.utility.DateUtil;
/* Java API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to add vouchers to Queue 
 * @author            : Maneesh Mishra
 * @version           : 1.0
 * Date of Creation   : 13-01-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class AddVouchersToQueue  {
  private Connection conn=null;   // jdbc connection object 
  private Logger logger = null;   // logger for verbose logging
  private OracleConnectionCacheImpl wfConnCache=null; // oracle connection pool
  private WFContext ctx=null;     // workflow context object
  private WFDB myDB=null;         // workflow database object
  
  /**
   * Constructor for AddVouchersToQueue Class
   * @param wfConnCache
   */
  public AddVouchersToQueue(OracleConnectionCacheImpl wfConnCache) {  
   this.wfConnCache=wfConnCache;
   logger = Logger.getLogger("DbsLogger");
  }
  
  /**
   * Function to add vouchers to Queue
   * @throws java.lang.Exception
   * @param docIds
   * @param aclName
   * @param userName
   * @param processTypeName
   * @param itemTypeName
   */
  public void addVouchers(String itemTypeName,String processTypeName,
              String userName,String aclName,String[] docIds) throws Exception{
    String iType = null;   // represents process item type 
    String iKey = null;    // represents process item key
    String pr = null;      // represents process type name
    try{
      /* Fetch JDBC connection object from Connection Pool */
      conn=wfConnCache.getConnection();
      /* set auto commit as false */
      conn.setAutoCommit(false);
      logger.debug("Connecting to the database...");
      Statement isPresentStmt = conn.createStatement();      
      /* obtain new workflow database object */
      myDB = new WFDB();
      /* set the connection with workflow database */
      myDB.setConnection(conn);
      /* obtain new workflow context */
      ctx = new WFContext(myDB, "UTF8");
      logger.debug("Instantiated WFDB and WFContext.");
      if (ctx.getDB().getConnection() == null){
        logger.debug("Connection Problem"); 
        throw new Exception("Database connection is not available");
      }
      /* set necessary parameters in order to add vouchers to queue */
      iType = itemTypeName;
      pr = processTypeName;           
      iKey  = aclName;
      logger.debug("Going to add vouchers to the queue " + iKey);
      /* loop through document ids to add them to queue */
      for (int index = 0 ; index < docIds.length ; index++){
        String sql="select count(*) as COUNT from wf_item_attribute_values where name='"+docIds[index]+"'";
        ResultSet isPresentSet = isPresentStmt.executeQuery(sql);
        isPresentSet.next();
        if(isPresentSet.getInt("COUNT") > 0){
          throw new Exception("Document(s) already present in the queue");
        }
        if(WFEngineAPI.addItemAttrText(ctx, iType, iKey, docIds[index],
          "InQueue^"+new DateUtil().getFormattedDate(new java.util.Date())+" ")){                 
          logger.debug("Item attribute" +docIds[index]+" created");
        }else{
          /* throw exception if unable to add to queue */
          WFEngineAPI.showError(ctx);
          throw new Exception("An error occured while submitting to queue");
        } 
      }
      /* commit the changes */
      conn.commit();
    }catch (SQLException sqle) {
      /* incase of error, rollback the changes */
      conn.rollback();
      logger.error(sqle.toString());      
      throw sqle ;
    }catch (Exception e) {
      /* incase of error, rollback the changes */
      conn.rollback();
      logger.error(e.toString());      
      throw e;
    }finally{
      try{
        /* close the connection with database */
        if(conn != null && !conn.isClosed()){
          conn.close();
        }
      }catch (Exception e) {           
        logger.error(e.toString());
      }      
    }
  }
}