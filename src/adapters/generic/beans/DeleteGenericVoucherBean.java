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
 * $Id: DeleteGenericVoucherBean.java,v 1.0 2006/06/21 06:32:21 suved Exp $
 *****************************************************************************
 */
package adapters.generic.beans;
/* adapters package references */
import adapters.beans.LoginBean;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsTransaction;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.DateUtil;
/* Java API */
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletContext;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean called to delete selected document images for Generic ERP.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 21-06-06
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class DeleteGenericVoucherBean  {
  private Long[] docIds;                      // array of document ids to rename
  private String aclName = null;             // aclName as workflow's itemKey
  private String workFlowFilePath = null;    // Workflows.xml file path
  private String [] names;                    // array of voucher ids   
  private Connection conn=null;              // JDBC Connection Object  
  private DbsLibrarySession dbsLibrarySession;// library session for cmsdk tasks
  private Logger logger;                      // logger for logging information
  private OracleConnectionCacheImpl wfConnCache=null; // Connection Pool from Oracle
  private WFContext ctx=null;                // Workflow Context
  private WFDB myDB=null;                    // Workflow Database

  /**
   * Constructor for DeleteGenericVoucherBean Class
   * @param workFlowFilePath
   * @param aclName
   * @param docIds
   * @param names
   * @param dbsLibrarySession
   * @param wfConnCache (Connection Pool From Oracle)
   */
  public DeleteGenericVoucherBean(OracleConnectionCacheImpl wfConnCache ,
         String userId,String password, Long [] docIds,
         String aclName,ServletContext context){
    this.wfConnCache = wfConnCache;
    this.docIds = docIds;
    this.aclName = aclName;
    this.workFlowFilePath = context.getRealPath("/")+"WEB-INF"+File.separator+
                            "params_xmls"+File.separator+"Workflows.xml";
    this.logger = Logger.getLogger("DbsLogger");
    LoginBean loginBean = new LoginBean(workFlowFilePath.replaceFirst("Workflows","GeneralActionParam"));
    this.dbsLibrarySession = loginBean.getUserSession();    
  }
 
  /**
   * Private method to delete voucher.
   * @return boolean indicating whether document deleted
   */
  private boolean deleteVoucher(){
    String iType = null;
    String iKey = aclName;
    int size = ( docIds != null )?docIds.length:0;
    DbsTransaction deleteTransaction = null;
    boolean deleted = false;
    int numRowsDeleted=0;
    try{
      /* get the itemType for the supplied aclName and proceed ahead only if the
         correct itemType has been found */
      iType = getIType();
      if( iType != null ){   
        /* fetch a jdbc connection from the connection pool */
        conn = wfConnCache.getConnection(); 
        if( conn != null ){
          /* set auto commit to false, 
           * Step 1 : delete voucher from Queue and from erp_data table */
          conn.setAutoCommit(false);
          if(size > 0){
            /* delete rows from Queue */
            String sql="delete from wf_item_attribute_values where name in (";
            StringBuffer whereNameInBuf = new StringBuffer();
            for (int index = 0 ; index < size ; index++){         
              whereNameInBuf.append("'"+docIds[index]+"',");
            }
            if( whereNameInBuf.length() > 0 ){
              whereNameInBuf.deleteCharAt(whereNameInBuf.length()-1);
            }
            String whereNameIn = whereNameInBuf.toString();
            sql += whereNameIn+")";
            Statement deleteStatement= conn.createStatement();
            numRowsDeleted=deleteStatement.executeUpdate(sql);
            /* delete rows from erp_data table */
            int rowsDeleted = deleteRowsFromERPTable(whereNameIn);
            logger.debug("Number of rows deleted from erp_data is : "+rowsDeleted);
          }
          
          /* Step 2 : delete voucher from cmsdk repository starts... */
         if(numRowsDeleted > 0){
          DbsPublicObject dbsPO = null;
          deleteTransaction = dbsLibrarySession.beginTransaction();
          DbsFileSystem dbsFileSys= new DbsFileSystem(dbsLibrarySession);
          for( int index = 0; index < size; index++ ){
            dbsPO = dbsLibrarySession.getPublicObject(docIds[index]);
            dbsFileSys.delete(dbsPO);
          }
          dbsLibrarySession.completeTransaction(deleteTransaction);
          deleteTransaction = null;
          /* delete voucher from cmsdk repository ends ... */
          /* commit the changes if there have been no hiccups in Steps 1 & 2 */
          conn.commit();
          deleted = true;
         }else{
           throw new Exception("Invalid document id specified");
         }
        }
      }else{
        logger.debug("No ItemType Entry found in workflow file");
      }
    }catch (DbsException dbsEx) {
      logger.debug(dbsEx.toString());
      try{
        if( deleteTransaction != null ){
          dbsLibrarySession.abortTransaction(deleteTransaction);
        }
      }catch (DbsException transEx) {
        logger.error(transEx.toString());
      }
      deleteTransaction = null;
      
    }catch (Exception e) {
      try{
        conn.rollback();
        logger.error(e.toString());
      }catch(SQLException sqe){
        logger.error(sqe.toString());
      }
    }finally{
      try{
        if(conn != null && !conn.isClosed()){             
          conn.close();
        }
        if( deleteTransaction != null ){
          try{
            dbsLibrarySession.abortTransaction(deleteTransaction);
          }catch (DbsException transEx) {
            logger.error(transEx.toString());          
          }
          deleteTransaction = null;
        }
      }catch (Exception e) {           
        logger.error(e.toString());       
      } 
    }
    return deleted;
  }

  /**
   * Private method to delete voucher data from erp_data table .
   * @return int indicating the number of rows deleted , -1 otherwise
   */
  private int deleteRowsFromERPTable(String docIdsAsString) throws Exception{
    String relativePath = this.workFlowFilePath.replaceFirst("Workflows","GeneralActionParam");
    ConnectionBean connBean = new ConnectionBean(relativePath);
    Connection con = null;
    Statement stmt = null;
    int rowsDeleted = -1;
    try{
      /* obtain connection for scheduler user */
      con = connBean.getConnection(true);
      con.setAutoCommit(false);
      stmt = con.createStatement();
      String query = "delete from erp_data where DOCUMENTID in ("+docIdsAsString+")";
      rowsDeleted = stmt.executeUpdate(query);
      con.setAutoCommit(true);
      logger.debug("Number of rows deleted from erp_data is : "+rowsDeleted);
    }catch (SQLException sqlEx) {
      logger.error("An Error occurred in DeleteGenericVoucherBean...");
      logger.error(sqlEx.toString());
      if(con != null){
        con.rollback();
      }
    }finally{
      if( stmt != null ){
        stmt.close();
        stmt = null;
      }
      if( con != null ){
        con.close();
        con = null;
      }
      connBean.closeConnection();
    }
    return rowsDeleted;
  }
  
  /**
   * Private method to fetch the itemTypeName for a given aclName
   * @return String itemTypeName
   */
  private String getIType(){
    ArrayList wfNames = null;
    String itemTypeName = null;
    XMLBean xmlBean = null;
    File file = new File(workFlowFilePath);
    if( file.exists() ){
      xmlBean = new XMLBean(workFlowFilePath);
      wfNames = xmlBean.getAllNames();
      String prefixName = null;
      for( int index = 0; index < wfNames.size(); index++ ){
        prefixName = xmlBean.getValue((String)wfNames.get(index),"PrefixName");
        if( aclName.startsWith(prefixName) ){
          itemTypeName = (String)wfNames.get(index);
          break;
        }
      }
    }
    return itemTypeName;
  }
  
  /**
   * Public method for deleting voucher 
   * @return boolean indicating whether status changed and doc renamed 
   *         successfully or not
   */
  public boolean getDeleteStatus(){
    return deleteVoucher();
  }
}
