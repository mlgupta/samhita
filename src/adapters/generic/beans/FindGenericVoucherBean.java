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
 * $Id: FindGenericVoucherBean.java,v 1.0 2006/06/21 06:32:21 ias Exp $
 *****************************************************************************
 */
package adapters.generic.beans;
/* dms package references */
import dms.web.beans.utility.ConnectionBean;
/* JDBC API */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to find documentId and voucherURL for the given  
                        VOUCHERID and SCREENID combination.
 * @author            : Maneesh Mishra
 * @version           : 1.0
 * Date of Creation   : 2006/06/21
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class FindGenericVoucherBean {
  private String documentId=null;     /* represents document id */
  private String screenId=null;       /* represents screen id */
  private String relativePath=null;   /* represents path for "GeneralActionParam.xml" */
  private Connection voucherCon=null; /* represents voucher connection object */
  private String voucherId=null;      /* represents voucher id */
  private String documentURL=null;    /* represents document URL */
  private Logger logger = null;       /* represents logger for verbose logging */

  /**
   * Constructor for FindGenericVoucherBean Class
   * @param relativePath
   * @param screenId
   * @param voucherId
   */
  public FindGenericVoucherBean(String voucherId,String screenId,String relativePath) {
    logger = Logger.getLogger("DbsLogger");
    this.voucherId = voucherId;
    this.screenId = screenId;
    this.relativePath = relativePath;
    logger.info("FindGenericVoucherBean initialized successfully...");
  }
  
  /**
   * Constructor for FindGenericVoucherBean Class
   * @param relativePath
   */
  public FindGenericVoucherBean(String relativePath) {
    logger = Logger.getLogger("DbsLogger");
    this.relativePath = relativePath;
    logger.info("FindGenericVoucherBean initialized successfully...");
  }

  /**
   * Purpose : Private function to obtain database connection object 
   */
  private void initiateDBConnection()throws Exception{
    ConnectionBean connBean = new ConnectionBean(relativePath);
    voucherCon=connBean.getConnection(true);
    try {
      if(voucherCon==null || voucherCon.isClosed()){
         throw new Exception("Unable to get connection to database");
      }else{
        logger.info("Database Connection for FindGenericVoucherBean.java initialised.");
      }
    }
    catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
    }
    
  }
  
  /**
   * Purpose : Function to fetch document id corresponding to the voucherId and
   *           screenId supplied  
   * @throws java.lang.Exception
   * @return String document id
   */
  public String getDocumentId()throws Exception{
    // obtain db connection
    initiateDBConnection();
    try {
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "select documentid from erp_data where "
                       +"voucherid='"+voucherId+"' and "
                       +"screenid='"+screenId+"' ";
      logger.debug(sqlQuery);                    
      ResultSet voucherSet = voucherStatement.executeQuery(sqlQuery);
      if(voucherSet.next()){
        documentId=voucherSet.getString("DOCUMENTID");
      }
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
    return documentId;
  }

  /**
   * Purpose : Function to fetch document id corresponding to the voucherId and
   *           screenId supplied  
   * @param screenId
   * @param voucherId
   * @throws java.lang.Exception
   * @return String document id
   */
  public String getDocumentId(String voucherId,String screenId)throws Exception{
    this.voucherId = voucherId;
    this.screenId = screenId;
    // obtain db connection
    initiateDBConnection();
    try {
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "select documentid from erp_data where "
                       +"voucherid='"+voucherId+"' and "
                       +"screenid='"+screenId+"' ";
      logger.debug(sqlQuery);                    
      ResultSet voucherSet = voucherStatement.executeQuery(sqlQuery);
      if(voucherSet.next()){
        documentId=voucherSet.getString("DOCUMENTID");
      }
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
    return documentId;
  }
  
  /**
   * Purpose : Function to fetch document URL corresponding to the voucherId 
   *           and screenId supplied  
   * @throws java.lang.Exception
   * @return String voucherURL
   */
  public String getVoucherURL()throws Exception{
    // obtain db connection
    initiateDBConnection();
    try{
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "select url from erp_data where "
                       +"voucherid='"+voucherId+"' and "
                       +"screenid='"+screenId+"' ";
      logger.debug(sqlQuery); 
      ResultSet voucherSet = voucherStatement.executeQuery(sqlQuery);
      if(voucherSet.next()){
        documentURL=voucherSet.getString("URL");
      }
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
    return documentURL;
  }

  /**
   * Purpose : Function to fetch document URL corresponding to the voucherId 
   *           and screenId supplied  
   * @param screenId
   * @param voucherId
   * @throws java.lang.Exception
   * @return String voucherURL
   */
  public String getVoucherURL(String voucherId,String screenId)throws Exception{
    this.voucherId = voucherId;
    this.screenId = screenId;
    // obtain db connection
    initiateDBConnection();
    try{
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "select url from erp_data where "
                       +"voucherid='"+voucherId+"' and "
                       +"screenid='"+screenId+"' ";
      logger.debug(sqlQuery); 
      ResultSet voucherSet = voucherStatement.executeQuery(sqlQuery);
      if(voucherSet.next()){
        documentURL=voucherSet.getString("URL");
      }
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
    return documentURL;
  }
  
  /**
   * Purpose : Function to update voucher data in erp_data table  
   * @throws java.lang.Exception
   * @param documentId
   */
  public void updateVoucherData(String documentId)
    throws Exception{
    // obtain db connection
    initiateDBConnection();
    try{
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "update erp_data set voucherid='"
                  +voucherId+"',screenid='"+screenId+"' where documentid ='"+
                  Long.parseLong(documentId)+"'";
      logger.debug(sqlQuery);             
      voucherStatement.executeUpdate(sqlQuery);
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
  }

  /**
   * Purpose : Function to add voucher data into erp_data table  
   * @throws java.lang.Exception
   * @param documentURL
   * @param documentId
   */
  public void enterVoucherData(String documentId,String documentURL)
    throws Exception{
    // obtain db connection
    initiateDBConnection();
    try{
      Statement voucherStatement = voucherCon.createStatement();
      String sqlQuery = "insert into erp_data values "
                  +" ("+Long.parseLong(documentId)+",'"+voucherId+"','"+documentURL+"','"+screenId+"')";
      logger.debug(sqlQuery);             
      voucherStatement.execute(sqlQuery);
    }catch (SQLException e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw new Exception("Error connecting to database");
    }catch (Exception e) {
      logger.error("Exception occurred in FindGenericVoucherBean ...");
      logger.error(e.toString());
      throw e;
    }finally {
      if(voucherCon!=null){
        voucherCon.close();
        logger.info("Connection closed successfully ...");
      }
    }
  }

}
