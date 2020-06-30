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
 * $Id: RenameVoucherBean.java,v 1.7 2006/02/02 12:32:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsTransaction;
/* Logger API */
import dms.web.beans.utility.DateUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.WFTwoDArray;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
import org.apache.log4j.Logger;

/**
 * Purpose            : Bean called to rename selected document images to their
 *                      corresponding voucherIds.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 18-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class RenameVoucherBean  {
  private DbsLibrarySession dbsLibrarySession;// library session for cmsdk tasks
  private String [] names;                    // array of voucher ids   
  private Long[] docIds;                      // array of document ids to rename
  private Logger logger;                      // logger for logging information
  private OracleConnectionCacheImpl wfConnCache=null; // Connection Pool from Oracle
  private WFContext ctx=null;                // Workflow Context
  private WFDB myDB=null;                    // Workflow Database
  private Connection conn=null;              // JDBC Connection Object  
  private String aclName = null;             // aclName as workflow's itemKey
  private String workFlowFilePath = null;    // Workflows.xml file path
  public RenameVoucherBean() {
  }

  /**
   * Constructor for RenameVoucherBean Class
   * @param workFlowFilePath
   * @param aclName
   * @param docIds
   * @param names
   * @param dbsLibrarySession
   * @param wfConnCache (Connection Pool From Oracle)
   */
  public RenameVoucherBean(OracleConnectionCacheImpl wfConnCache ,
         DbsLibrarySession dbsLibrarySession, String [] names,Long [] docIds,
         String aclName,String workFlowFilePath){
    this.wfConnCache = wfConnCache;
    this.dbsLibrarySession = dbsLibrarySession;
    this.names = names;
    this.docIds = docIds;
    this.aclName = aclName;
    this.workFlowFilePath = workFlowFilePath;
    this.logger = Logger.getLogger("DbsLogger");
  }
  
  /**
   * Private method to change voucher status to "Processed" and rename the 
   * document image with the voucher id.
   * @throws java.lang.Exception
   * @throws dms.beans.DbsException
   * @return boolean indicating whether status changed and doc renamed 
   *         successfully or not
   */
  private boolean changeStatusToProcessed(){
    String iType = null;
    String iKey = aclName;
    int size = ( names != null )?names.length:0;
    DbsTransaction renameTransaction = null;
    boolean processedAndRenamed = false;
    try{
      /* get the itemType for the supplied aclName and proceed ahead only if the
         correct itemType has been found */
      iType = getIType();
      if( iType != null ){
        /* fetch workflow db object */
        myDB = new WFDB();
        /* fetch a jdbc connection from the connection pool */
        conn = wfConnCache.getConnection();
        /* set the connection to workflow db */
        myDB.setConnection(conn);
        /* obtain new workflow context */
        ctx = new WFContext(myDB, "UTF8");
        logger.debug("Instantiated WFDB and WFContext.");
        if (ctx.getDB().getConnection() == null){
          logger.error("Connection Problem"); 
          throw new Exception("Database connection is not available");
        }
        if( conn != null ){
          
          /* Step 1 : rename to voucher id in cmsdk repository starts... */
          DbsPublicObject dbsPO = null;
          renameTransaction = dbsLibrarySession.beginTransaction();
          String extension = null;
          for( int index = 0; index < size; index++ ){
            dbsPO = dbsLibrarySession.getPublicObject(
                                      docIds[index]).getResolvedPublicObject();
            extension =  dbsPO.getName().substring(
                                          dbsPO.getName().lastIndexOf("."),
                                          dbsPO.getName().length());
            if( names[index] != null && names[index].length() !=0 ){
              dbsPO.setName(names[index]+extension);
              /* set description to voucher id also, an enhancement */
              dbsPO.setDescription(names[index]);
            }
          }
          /* rename to voucher id in cmsdk repository ends ... */
          /* set auto commit to false, 
           * Step 2 : change the document state to "Processed" in Queue */
          conn.setAutoCommit(false);
           
          Statement stmt = conn.createStatement();       
          for (int index = 0 ; index < size ; index++){ 
            String sql = "select NAME,TEXT_VALUE from wf_item_attribute_values "
                          +"where item_type='"+iType+"' " 
                          +"and item_key='"+iKey+"' "
                          +"and name='"+docIds[index].toString()+"' "; 
            ResultSet rst = stmt.executeQuery(sql);
            String status=null;
            while(rst.next()){            
              status=rst.getString("TEXT_VALUE");
              break;
            }
            
            status+="Processed^"+new DateUtil().getFormattedDate(new Date())+" ";
             
            if (WFEngineAPI.setItemAttrText(ctx, iType, iKey, 
                                            docIds[index].toString(),status)){                 
              logger.debug("Item attribute" +docIds[index].toString()+" created");
            }else{
              WFEngineAPI.showError(ctx);
              throw new Exception("Invalid Item Attribute");
            } 
          }
          dbsLibrarySession.completeTransaction(renameTransaction);
          renameTransaction = null;
          /* commit the changes if there have been no hiccups in Steps 1 & 2 */
          conn.commit();
          processedAndRenamed = true;
        
        }
      }else{
        logger.debug("No ItemType Entry found in workflow file");
      }
    }catch (DbsException dbsEx) {
      logger.error("Exception occurred in RenameVoucherBean...");
      logger.error(dbsEx.toString());
      try{
        if( renameTransaction != null ){
          dbsLibrarySession.abortTransaction(renameTransaction);
        }
      }catch (DbsException transEx) {
        logger.error("Exception occurred in RenameVoucherBean...");
        logger.error(transEx.toString());
      }
      renameTransaction = null;
     
    }catch (Exception e) {
      try{
         conn.rollback();
         logger.error(e.toString());
      }catch(SQLException sqe){
        logger.error("Exception occurred in RenameVoucherBean...");
        logger.error(sqe.toString());
      }
     
    }finally{
      try{
        if(conn != null && !conn.isClosed()){             
          conn.close();
        }
        if( renameTransaction != null ){
          try{
            dbsLibrarySession.abortTransaction(renameTransaction);
          }catch (DbsException transEx) {
            logger.error("Exception occurred in RenameVoucherBean...");
            logger.error(transEx.toString());            
          }
          renameTransaction = null;
        }
      }catch (Exception e) {           
        logger.error("Exception occurred in RenameVoucherBean...");
        logger.error(e.toString());     
      } 
    }
    return processedAndRenamed;
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
   * Public method for changing voucher status and renaming the document by 
   * voucher id.
   * @throws java.lang.Exception
   * @throws dms.beans.DbsException
   * @return boolean indicating whether status changed and doc renamed 
   *         successfully or not
   */
  public boolean getRenameStatus(){
    return changeStatusToProcessed();
  }
  
  
}