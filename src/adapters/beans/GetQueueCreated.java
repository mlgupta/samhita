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
 * $Id: GetQueueCreated.java,v 1.2 2006/03/17 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* Java API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/* Oracle Connection Pool API */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to Create Queue if non existent 
 * @author            : Maneesh Mishra
 * @version           : 1.0
 * Date of Creation   : 07-03-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class GetQueueCreated  {
  private Connection conn = null;                     // jdbc connection object
  private OracleConnectionCacheImpl wfConnCache=null; // Oracle Conn Pool
  private Logger logger = null;                       // Logger for verbose logging 

  /**
   * Constructor for GetQueueCreated Class 
   * @param wfConnCache
   */
  public GetQueueCreated(OracleConnectionCacheImpl wfConnCache) {
    this.wfConnCache=wfConnCache;
    logger = Logger.getLogger("DbsLogger");
  }
    
  /**
   * Function to create Q if non existent and return boolean status
   * @return boolean to indicate if queue created
   * @param iKey
   * @param iType
   */
  public boolean isQueueCreated(String iType,String iKey){
    boolean isCreated = false ;
    try{
      /* Fetch JDBC connection object from Connection Pool */
      conn= wfConnCache.getConnection();
      /* Create statement object from connection object */
      Statement stmt = conn.createStatement();
      /* Construct Query string */
      String sql = "select count(*) from wf_items where item_type='"+iType+
                  "' and item_key='"+iKey+"'";
      /* Execute Query and obtain resultSet */
      ResultSet rst = stmt.executeQuery(sql);
      rst.next();
      int count = rst.getInt(1);
      /* count > 0 implies that Q has been created */
      if(count > 0){
        isCreated = true ;
      }
    }catch (SQLException e) {
      logger.error(e.toString());
      e.printStackTrace();
    }finally{
      /* close the db connection */
      try{
        if(conn != null && !conn.isClosed()){
          conn.close();
        }
      }catch (Exception e) {           
        logger.error(e.toString());
      }
    }
    return isCreated;
  }
}