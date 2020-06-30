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
 * $Id: ListPSHRVouchers.java,v 1.0 2006/03/07 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.pshr.beans;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
/* Java API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/* Oracle Connection Pool API */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to list vouchers according to specified status for 
 *                      PSHR. 
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 07-03-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class ListPSHRVouchers  {
  private Connection conn = null;                     // jdbc connection object
  private OracleConnectionCacheImpl wfConnCache=null; // Oracle Conn Pool
  private Logger logger = null;                       // Logger for verbose logging 
  public String pageCountString="1";                  // page count variable
  
  /**
   * Constructor for ListPSHRVouchers Class 
   * @param wfConnCache
   */
  public ListPSHRVouchers(OracleConnectionCacheImpl wfConnCache) {
    this.wfConnCache=wfConnCache;
    logger = Logger.getLogger("DbsLogger");
  }
  
  /**
   * Function to obtain list of vouchers according to status specified
   * @return Arraylist containing list of vouchers according to status specified
   * @param numRecords
   * @param pageNumber
   * @param dbsLibrarySession
   * @param dateTo
   * @param dateFrom
   * @param status
   * @param aclName
   * @param iType
   */
  public ArrayList getList(String iType,String aclName,String status,
         String dateFrom,String dateTo, DbsLibrarySession dbsLibrarySession,
         int pageNumber,int numRecords){
    ArrayList voucherLists = new ArrayList();   // returns list of vouchers
    ResultSet voucherSet=null;                  // result set as query result 
    int pageCount=1;                            // default page count value  

    try{
      /* fetch jdbc connection object from oracle connection pool */
      conn= wfConnCache.getConnection(); 
      /* create statement object from connection object */
      Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      /* construct sql query string according to status specified */
      String sql = "select item_type,item_key,name,text_value,"
                  +" substr(text_value,instr(text_value,'InQueue')+8,11) As DATE_INQUEUE";
      logger.info(status);
      /* Case 1: InQueue */ 
      if(status.equalsIgnoreCase("InQueue")){
        /* criteria 1 : specify item_type criteria and it's corresponding value */
        sql = sql+" from wf_item_attribute_values "
                +" where item_type='"+iType+"' ";
        if(!aclName.equalsIgnoreCase("")){
          /* criteria 2 : specify item_key criteria and it's corresponding value */  
          sql=sql+" and item_key='"+aclName +"' "; 
        }
        /* criteria 3 : for fetching "InQueue", apply filter : like '%InQueue%' 
         * and not like '%InProcess%' */
        sql=sql+" and TEXT_VALUE like '%InQueue%' "
              +" and TEXT_VALUE not like '%InProcess%' ";
        /* criteria 4 : for a specified dateTo value enter criteria */        
        if(!dateTo.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'InQueue')+8,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
        }
        /* criteria 5 : for a specified dateFrom value enter criteria */        
        if(!dateFrom.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'InQueue')+8,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
        }
        /* and thus query string has been constructed */
        logger.info("Executing SQL:"+sql);
        /* execute query and fetch results from result set */
        voucherSet= stmt.executeQuery(sql);
        
        if(voucherSet!=null){
          /* set page count for the results returned */
          pageCount= getPageCount(voucherSet,numRecords);           
          if (pageNumber>pageCount ) {
            pageNumber=pageCount;
          }
          int startIndex=(pageNumber*numRecords) - numRecords;
          int endIndex= (startIndex + numRecords)-1;
          logger.debug("startIndex : " + startIndex); 
          logger.debug("endIndex : " + endIndex);
          
          if(startIndex>0){
            voucherSet.absolute(startIndex);
          }
          /* traverse the resultset, construct voucher bean and add to arraylist */
          while(voucherSet.next()){
            PSHRVoucherStatusListBean voucher = new PSHRVoucherStatusListBean();
            voucher.setAclName(aclName);
            voucher.setDocID(voucherSet.getString("NAME").trim());
            try{
              voucher.setDocName(dbsLibrarySession.getPublicObject(new Long(voucherSet.getString("NAME"))).getName());
            }catch (DbsException dbsException){
              logger.error(dbsException.toString());
              dbsException.printStackTrace();
            }
            voucher.setDateInqueue(voucherSet.getString("DATE_INQUEUE"));
            voucher.setPsVoucherStatus("InQueue");
            
            voucherLists.add(voucher);
            startIndex++;
            if (startIndex>endIndex){
              break;
            }
          }
          pageCountString=new Integer(pageCount).toString();             
        }
      }else if(status.equalsIgnoreCase("InProcess")){
        /* Case 2: InProcess */
        /* criteria 1 : include criteria for DATE_INPROCESS */
        sql = sql +",  substr(text_value,instr(text_value,'InProcess')+10,11) As DATE_INPROCESS ";
        /* criteria 2 : specify item_type criteria and it's corresponding value */
        sql = sql+" from wf_item_attribute_values "
                +" where item_type='"+iType+"' ";        
        if(!aclName.equalsIgnoreCase("")){
          /* criteria 3 : specify item_key criteria and it's corresponding value */  
          sql=sql+" and item_key='"+aclName +"' "; 
        }
        /* criteria 4 : for fetching "InQueue", apply filter : like '%InProcess%' 
         * and not like '%Processed%' */
        sql=sql+" and TEXT_VALUE like '%InProcess%' "
              +" and TEXT_VALUE not like '%Processed%' ";
        /* criteria 5 : for a specified dateTo value enter criteria */        
        if(!dateTo.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'InProcess')+10,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
        }
        /* criteria 6 : for a specified dateFrom value enter criteria */        
        if(!dateFrom.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'InProcess')+10,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
        } 
        /* and thus query string has been constructed */
        logger.info("Executing SQL:"+sql);
        /* execute query and fetch results from result set */
        voucherSet= stmt.executeQuery(sql);           
        if(voucherSet!=null){
          /* set page count for the results returned */
          pageCount= getPageCount(voucherSet,numRecords);           
          if (pageNumber>pageCount ) {
            pageNumber=pageCount;
          }
          int startIndex=(pageNumber*numRecords) - numRecords;
          int endIndex= (startIndex + numRecords)-1;
          logger.debug("startIndex : " + startIndex); 
          logger.debug("endIndex : " + endIndex);
          
          if(startIndex>0){
            voucherSet.absolute(startIndex);
          }
          /* traverse the resultset, construct voucher bean and add to arraylist */
          while(voucherSet.next()){
            logger.debug("voucherSet.getRow() : " + voucherSet.getRow());
            PSHRVoucherStatusListBean voucher = new PSHRVoucherStatusListBean();
            voucher.setAclName(aclName);
            voucher.setDocID(voucherSet.getString("NAME").trim());
            try{
              voucher.setDocName(dbsLibrarySession.getPublicObject(new Long(voucherSet.getString("NAME"))).getName());
            }catch (DbsException dbsException){
              logger.error(dbsException.toString());
              dbsException.printStackTrace();
            }
            voucher.setDateInqueue(voucherSet.getString("DATE_INQUEUE"));
            voucher.setDateInprocess(voucherSet.getString("DATE_INPROCESS"));
            voucher.setPsVoucherStatus("InProcess");
            voucherLists.add(voucher);
            startIndex++;
            if (startIndex>endIndex){
              break;
            }
          }
          pageCountString=new Integer(pageCount).toString();             
        }
      }else if(status.equalsIgnoreCase("Processed")){
        /* Case 3: Processed */
        /* criteria 1 : include criteria for DATE_INPROCESS */
        sql = sql +",  substr(text_value,instr(text_value,'InProcess')+10,11) As DATE_INPROCESS ";
        /* criteria 2 : include criteria for DATE_PROCESSED */
        sql = sql +",  substr(text_value,instr(text_value,'Processed')+10,11) As DATE_PROCESSED ";
        /* criteria 3 : specify item_type criteria and it's corresponding value */
        sql = sql+" from wf_item_attribute_values "
                +" where item_type='"+iType+"' ";
        if(!aclName.equalsIgnoreCase("")){
          /* criteria 4 : specify item_key criteria and it's corresponding value */  
          sql=sql+" and item_key='"+aclName +"' "; 
        }
        /* criteria 5 : for fetching "InQueue", apply filter : like '%Processed%' */
        sql=sql+" and TEXT_VALUE like '%Processed%' ";
        /* criteria 6 : for a specified dateTo value enter criteria */        
        if(!dateTo.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'Processed')+10,11),'dd-Mon-yyyy') <= to_date("+dateTo+",'dd-Mon-yyyy') "; 
        }
        /* criteria 7 : for a specified dateFrom value enter criteria */        
        if(!dateFrom.equalsIgnoreCase("")){
          sql=sql+" and to_date(substr(text_value,instr(text_value,'Processed')+10,11),'dd-Mon-yyyy') >= to_date("+dateFrom+",'dd-Mon-yyyy') "; 
        } 
        /* and thus query string has been constructed */
        logger.info("Executing SQL:"+sql);
        /* execute query and fetch results from result set */
        voucherSet= stmt.executeQuery(sql);           
        
        if (voucherSet!=null){
          /* set page count for the results returned */
          pageCount= getPageCount(voucherSet,numRecords);           
          if(pageNumber>pageCount ) {
            pageNumber=pageCount;
          }
          int startIndex=(pageNumber*numRecords) - numRecords;
          int endIndex= (startIndex + numRecords)-1;
          logger.debug("startIndex : " + startIndex); 
          logger.debug("endIndex : " + endIndex);
          
          if(startIndex>0){
            voucherSet.absolute(startIndex);
          }
          /* traverse the resultset, construct voucher bean and add to arraylist */
          while(voucherSet.next()){
            logger.debug("voucherSet.getRow() : " + voucherSet.getRow());
            
            PSHRVoucherStatusListBean voucher = new PSHRVoucherStatusListBean();
            voucher.setAclName(aclName);
            voucher.setDocID(voucherSet.getString("NAME"));
            try{
              voucher.setDocName(dbsLibrarySession.getPublicObject(new Long(voucherSet.getString("NAME"))).getName());
            }catch (DbsException dbsException){
              logger.error(dbsException.toString());
              dbsException.printStackTrace();
            }
            voucher.setDateInqueue(voucherSet.getString("DATE_INQUEUE"));
            voucher.setDateInprocess(voucherSet.getString("DATE_INPROCESS"));
            voucher.setDateProcessed(voucherSet.getString("DATE_PROCESSED"));
            voucher.setPsVoucherStatus("Processed");
            voucherLists.add(voucher);
            startIndex++;
            if (startIndex>endIndex){
              break;
            }
          }
          pageCountString=new Integer(pageCount).toString();             
        }
      }else if(status.equalsIgnoreCase("")){
        
      }
    }catch (SQLException e) {
      e.printStackTrace();
      logger.error(e.toString());
    }finally{
      /* close connection */
      try{
        if(conn != null && !conn.isClosed()){
          conn.close();
        }
      }catch (Exception e) {           
        logger.error(e.toString());
      }
    }
    return voucherLists;
  }
 
  /**
   * Function to obtain page count for the current set of data
   * @throws java.sql.SQLException
   * @return integer containing page count.
   * @param numberOfRecords
   * @param rs
   */
  public int getPageCount(ResultSet rs, int numberOfRecords)throws SQLException{
    int recordCount;   // stores recordCount of a given resultSet 
    int pageCount=1;   // page count for a given resultSet set to 1 by default
    try{
      /* traverse to the last row of result set */
      rs.last();
      /* fetch the record count */
      recordCount=rs.getRow();
      logger.debug("recordCount : " + recordCount);
      if (recordCount!=0){
        /* calculate page count */
        pageCount=((recordCount%numberOfRecords)==0)?
                (recordCount/numberOfRecords):((recordCount/numberOfRecords)+1);
      }
      /* traverse to the beginning of result set */ 
      rs.beforeFirst(); 
    }catch(SQLException se){
      logger.error("***Exception in getPageCount() method"+se.getMessage());
      throw se;   
    }
    return pageCount;
  } 
}