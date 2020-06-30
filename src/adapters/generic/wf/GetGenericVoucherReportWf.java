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
 * $Id: GetGenericVoucherReportWf.java,v 1.1 2006/06/23 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.generic.wf;
/* Java API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to get voucher report for Generic ERP
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 23-06-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class GetGenericVoucherReportWf  {

  private Connection conn = null;
  private OracleConnectionCacheImpl wfConnCache=null;
  private Logger logger = null;
  private WFContext ctx=null;
  private WFDB myDB=null;
  
  public GetGenericVoucherReportWf(OracleConnectionCacheImpl wfConnCache) {
    this.wfConnCache=wfConnCache;
    logger = Logger.getLogger("DbsLogger");       
  }
  
  public Hashtable getReport(String iType,String aclName,String dateTo,String dateFrom){
    Hashtable voucherReport=null;
    Long numInQueue=null;
    Long numInProcess=null;
    Long numProcessed=null;
    Long numTotal=null;
    try{
      conn= wfConnCache.getConnection();
      Statement stmt = conn.createStatement();
      
      //////////////////////////InQueue////////////////////////////////
      
      String sql = "select count(*) as NUM_INQUEUE from wf_item_attribute_values "
                   +" where item_type='"+iType+"' ";
      if(!aclName.equalsIgnoreCase("")){
             sql=sql+" and item_key='"+aclName +"' "; 
      }
      
      sql=sql+" and TEXT_VALUE like '%InQueue%' "
                  +" and TEXT_VALUE not like '%InProcess%' ";
                  
      if(!dateTo.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'InQueue')+8,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
      }
      
      if(!dateFrom.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'InQueue')+8,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
      } 
      
      logger.info("Executing SQL:"+sql);
      ResultSet inQueueSet= stmt.executeQuery(sql);      
      inQueueSet.next();
      numInQueue=new Long(inQueueSet.getString("NUM_INQUEUE"));
      
      //////////////////////////InQueue////////////////////////////////
      
      //////////////////////////InProcess////////////////////////////////
      sql = "select count(*) as NUM_INPROCESS from wf_item_attribute_values "
                   +" where item_type='"+iType+"' ";
      if(!aclName.equalsIgnoreCase("")){
        sql=sql+" and item_key='"+aclName +"' "; 
      }
      
      sql=sql+" and TEXT_VALUE like '%InProcess%' "
                  +" and TEXT_VALUE not like '%Processed%' ";
                  
      if(!dateTo.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'InProcess')+10,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
      }
         
      if(!dateFrom.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'InProcess')+10,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
      }   
      
      logger.info("Executing SQL:"+sql);
      ResultSet inProcessSet= stmt.executeQuery(sql);
      inProcessSet.next();
      numInProcess=new Long(inProcessSet.getString("NUM_INPROCESS"));
      
     //////////////////////////InProcess////////////////////////////////
     
     //////////////////////////Processed////////////////////////////////
      sql = "select count(*) as NUM_PROCESSED from wf_item_attribute_values "
                   +" where item_type='"+iType+"' ";
      if(!aclName.equalsIgnoreCase("")){
             sql=sql+" and item_key='"+aclName +"' "; 
      }
      
      sql=sql+" and TEXT_VALUE like '%Processed%' ";                 
                
      if(!dateTo.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'Processed')+10,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
      }
      
      if(!dateFrom.equalsIgnoreCase("")){
        sql=sql+" and to_date(substr(text_value,instr(text_value,'Processed')+10,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
      }   
      
      logger.info("Executing SQL:"+sql);
      ResultSet processedSet= stmt.executeQuery(sql);  
      processedSet.next();
      numProcessed=new Long(processedSet.getString("NUM_PROCESSED"));
       
     
     //////////////////////////Processed////////////////////////////////
     
      numTotal=new Long(numProcessed.longValue()+numInProcess.longValue()+numInQueue.longValue()); 
      voucherReport=new Hashtable();
      voucherReport.put("NUM_INQUEUE",numInQueue);
      voucherReport.put("NUM_PROCESSED",numProcessed);
      voucherReport.put("NUM_INPROCESS",numInProcess);
      voucherReport.put("NUM_TOTAL",numTotal);
    }catch (SQLException e) {
      logger.error(e.toString());
    }finally {
      try {
        if(conn != null && !conn.isClosed()){
          conn.close();
        }
      }catch (Exception e) {           
        logger.error(e.toString());
      }
    }
   return voucherReport; 
  }
}
