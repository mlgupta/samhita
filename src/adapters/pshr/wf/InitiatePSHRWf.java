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
 * $Id: InitiatePSHRWf.java,v 1.0 2006/03/07 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.pshr.wf;
/* dms package references */ 
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.utility.DateUtil;
/* Java API */
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
/* Oracle API */
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFDB;
import oracle.apps.fnd.wf.engine.WFEngineAPI;
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to initializePSHR workflow and add documents to queue 
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 08-03-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class InitiatePSHRWf  {

  private Connection conn=null;   // jdbc connection object 
  private Logger logger = null;   // logger for verbose logging
  private OracleConnectionCacheImpl wfConnCache=null; // oracle connection pool
  private String PSFAclName = null;                   // aclName
  public ExceptionBean exBean;                        // exception bean                       

  /**
   * Constructor for InitiatePSHRWf Class
   * @param wfConnCache
   */
  public InitiatePSHRWf(OracleConnectionCacheImpl wfConnCache){
    this.wfConnCache=wfConnCache;
    logger=Logger.getLogger("DbsLogger");
  }

  /**
   * Function to initialize PSHR workflow and add the document ids to queue 
   * @param docIds
   * @param managers
   * @param aclName
   * @param userName
   * @param processTypeName
   * @param itemTypeName
   */
  public void startReqWF(String itemTypeName,String processTypeName,
              String userName,String aclName,String managers,String[] docIds) throws Exception{   
    String iType = null;   // represents process item type 
    String iKey = null;    // represents process item key
    String pr = null;      // represents process type name
    String uKey = null;    // represents unique key    
    try{ 
      /* Fetch JDBC connection object from Connection Pool */
      conn=wfConnCache.getConnection();
      /* set auto commit as false */
      conn.setAutoCommit(false);
      logger.debug("Connecting to the database...");
      /* obtain new workflow database object */
      WFDB myDB = new WFDB();
      /* set the connection with workflow database */
      myDB.setConnection(conn);
      /* obtain new workflow context */
      WFContext ctx = new WFContext(myDB, "UTF8");
      logger.debug("Instantiated WFDB and WFContext.");
      
      if(ctx.getDB().getConnection() == null){
        logger.debug("Connection Problem"); 
        throw new Exception("Database connection is not available");
      }
      /* set necessary parameters in order to create process */
      iType = itemTypeName;
      pr = processTypeName;
      uKey = userName.toUpperCase()+
             new String().valueOf(Calendar.getInstance().getTimeInMillis());
      
      logger.debug("Launching " + iType);
      iKey  = aclName; 
      // create an item
      if(WFEngineAPI.createProcess(ctx, iType, iKey, pr)){
        logger.debug("Created Item");
      }else{
        /* throw exception if unable to do so */
        logger.debug("createProcess failed");
        WFEngineAPI.showError(ctx);
        throw new Exception("An error occured while submitting to queue");
      }   
      /* set ItemAttr "USER" */
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "USER",userName)){
        logger.debug("user set");
      }else{
        /* throw exception if unable to do so */
        WFEngineAPI.showError(ctx);
        throw new Exception("An error occured while submitting to queue");
      }  
      /* set ItemAttr "MANAGERS" */
      if(WFEngineAPI.setItemAttrText(ctx, iType, iKey, "MANAGERS",managers)){
        logger.debug("managers set");
      }else{
        /* throw exception if unable to do so */
        WFEngineAPI.showError(ctx);
        throw new Exception("An error occured while submitting to queue");
      }  
      /* loop through the documentIds and add them to queue */
      for (int index = 0 ; index < docIds.length ; index++){ 
        if(WFEngineAPI.addItemAttrText(ctx, iType, iKey, docIds[index],
          "InQueue^"+new DateUtil().getFormattedDate(new Date())+" ")){                 
          logger.debug("Item attribute" +docIds[index]+" created");
        }else{
          /* throw exception if unable to do so */
          WFEngineAPI.showError(ctx);
          throw new Exception("An error occured while submitting to queue");
        }           
      }     
      /* start process after it has been created successfully */
      if(WFEngineAPI.startProcess(ctx, iType, iKey)){
        logger.debug("Process "+iType+"with key"+iKey+" Started successfully");
      }else{
        /* throw exception if unable to do so */
        logger.debug("launch failed");
        WFEngineAPI.showError(ctx);
      }    
      /* commit the changes */
      conn.commit();
    }catch(Exception e){     
      /* incase of error, rollback the changes */
      try{
        conn.rollback();     
      }catch (Exception ex){
        ex.printStackTrace();
        throw ex;
      }
      exBean = new ExceptionBean(e);          
      logger.error("Exception occured in InitiatePSHRWf "+e.toString());       
      e.printStackTrace();
      throw e;
    }finally{
      /* close the connection with database */
      try{
        if(conn!=null && !conn.isClosed()){            
          conn.close();
        }
      }catch(Exception e){
        exBean = new ExceptionBean(e);        
        e.printStackTrace();
        throw e;
      }
    }          
  }
}