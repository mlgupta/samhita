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
 * $Id: GetGenericVoucherFromQueueWf.java,v 1.1 2006/06/23 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.generic.wf;
/* dms package references */
import dms.web.beans.utility.DateUtil;
/* Java API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to get voucher from Queue for Generic ERP
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 23-06-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class GetGenericVoucherFromQueueWf  {
  private int numVouchersInQ = 0; // number of vouchers in Queue
  private Connection conn=null;   // jdbc connection object 
  private Logger logger = null;   // logger for verbose logging
  private OracleConnectionCacheImpl wfConnCache=null; // oracle connection pool
  private WFContext ctx=null;     // workflow context object
  private WFDB myDB=null;         // workflow database object
  /**
   * Constructor for GetGenericVoucherFromQueueWf Class
   * @param wfConnCache
   */
  public GetGenericVoucherFromQueueWf(OracleConnectionCacheImpl wfConnCache) {
    this.wfConnCache=wfConnCache;
    logger = Logger.getLogger("DbsLogger");       
  }
  
  /**
   * Function to get voucher from Queue
   * @throws java.lang.Exception
   * @return 
   * @param iKey
   * @param iType
   */
  public String  getVoucher(String iType,String iKey)throws Exception{
    String docId = null;    // document id as String
    String status = null;   // status of the document
    try{
      /* Fetch JDBC connection object from Connection Pool */
      conn=wfConnCache.getConnection();
      /* set auto commit as false */
      conn.setAutoCommit(false);
      /* create statement object */
      Statement stmt = conn.createStatement();
      /* construct sql query string */
      String sql = "select NAME,TEXT_VALUE from wf_item_attribute_values "
                    +"where item_type='"+iType+"' " 
                    +"and item_key='"+iKey+"' "
                    +"and TEXT_VALUE like '%InQueue%' "
                    +"and TEXT_VALUE not like '%InProcess%' ";
      /* syncronized access for the result set */
      synchronized(this){
        ResultSet rst = stmt.executeQuery(sql);
        while(rst.next()){
          docId=rst.getString("NAME");
          status=rst.getString("TEXT_VALUE");
          numVouchersInQ++;
          break;
        }
        /* if no vouchers in queue , throw exception that the queue is empty */
        if(numVouchersInQ == 0){
          throw new Exception("QueueEmpty");
        }
        /* construct status string */
        status=status+"InProcess^"+new DateUtil().getFormattedDate(new Date())+" ";
        /* obtain new workflow database object */
        myDB = new WFDB();
        /* set the connection with workflow database */
        myDB.setConnection(conn);
        /* obtain new workflow context */
        ctx = new WFContext(myDB, "UTF8");
        logger.debug("Instantiated WFDB and WFContext.");
        if(ctx.getDB().getConnection() == null){
          logger.debug("Connection Problem"); 
          throw new Exception("Database connection is not available");
        }
        /* set status for the document id in queue */ 
        if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, docId,status)){
          logger.debug("status set");
        }else{
          WFEngineAPI.showError(ctx);
          throw new Exception("Unable to set the status");
        }
      /* commit the changes */
        conn.commit();
      }
    }catch (SQLException e){
      logger.error(e.toString());
      /* incase of error, rollback the changes */
      conn.rollback();
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
    return docId;
  }
}
