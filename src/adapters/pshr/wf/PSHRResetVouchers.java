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
 * $Id: PSHRResetVouchers.java,v 1.0 2006/03/07 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.pshr.wf;
/* dms package references */
import dms.web.beans.utility.DateUtil;
/* Java API */
import java.sql.Connection;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to reset vouchers status to 'InQueue' for PSHR 
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 08-03-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class PSHRResetVouchers  {
  private Connection conn=null;   // jdbc connection object 
  private Logger logger = null;   // logger for verbose logging
  private OracleConnectionCacheImpl wfConnCache=null; // oracle connection pool
  private WFContext ctx=null;     // workflow context object
  private WFDB myDB=null;         // workflow database object
  /**
   * Constructor for PSHRResetVouchers Class
   * @param wfConnCache
   */
  public PSHRResetVouchers(OracleConnectionCacheImpl wfConnCache) {
    this.wfConnCache=wfConnCache;
    logger = Logger.getLogger("DbsLogger");    
  }
  
  /**
   * Function to reset vouchers status to 'InQueue'
   * @throws java.lang.Exception
   * @param docIds
   * @param iKey
   * @param iType
   */
  public void reset(String iType,String iKey,String[] docIds)throws Exception{
    try{
      /* obtain new workflow database object */
      myDB = new WFDB();
      /* Fetch JDBC connection object from Connection Pool */
      conn= wfConnCache.getConnection();
      /* set the connection with workflow database */
      myDB.setConnection(conn); 
      /* obtain new workflow context */
      ctx = new WFContext(myDB, "UTF8");
      logger.debug("Instantiated WFDB and WFContext.");
      
      if (ctx.getDB().getConnection() == null){
        logger.debug("Connection Problem"); 
        throw new Exception("Database connection is not available");
      }
      /* loop through document ids to reset their status to InQueue */
      for (int index = 0 ; index < docIds.length ; index++){ 
        if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, docIds[index],
            "InQueue^"+new DateUtil().getFormattedDate(new java.util.Date())+" ")){                 
          logger.debug("Item attribute" +docIds[index]+" reset");
        }else{
          WFEngineAPI.showError(ctx);
        } 
      } 
      /* commit the changes */
      conn.commit();
    }catch (Exception e){
      /* incase of error, rollback the changes */
      conn.rollback();
      logger.error(e.toString());
    }finally{
      /* close the connection with database */
      try{
        if(conn != null && !conn.isClosed()){             
          conn.close();
        }
      }catch (Exception e) {           
        logger.error(e.toString());
      } 
    }
  }
}