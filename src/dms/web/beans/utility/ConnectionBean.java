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
 * $Id: ConnectionBean.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;
/* dms package references */
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */ 
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
/* Oracle JDBC API */
import oracle.jdbc.pool.OracleDataSource;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to obtain a db connection using necessary parameters.
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    05-05-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class ConnectionBean{
  
  public Connection connection; // Database Connection Object
  public Statement  statement;
  private String relPath;        /* relative path for "GeneralActionParam.xml" */
  ParseXMLTagUtil parseUtil =null;
  Logger logger = null;
  
  public ConnectionBean(String relPath){
    //dbConnection();
    logger = Logger.getLogger("DbsLogger");
    this.relPath = relPath;
    //logger.debug("relPath: "+relPath);
    //System.out.println("relPath: "+relPath);
    parseUtil = new ParseXMLTagUtil(relPath);
  }
  
  public Connection getConnection() {
    try {
      OracleDataSource ods = new OracleDataSource();
      // Sets the driver type
      ods.setDriverType(parseUtil.getValue("DriverType","Configuration"));
      // Sets the database server name
      ods.setServerName(parseUtil.getValue("ServerName","Configuration"));
      // Sets the database name
      ods.setDatabaseName(parseUtil.getValue("DatabaseName","Configuration"));
      // Sets the port number
      ods.setPortNumber((new Integer(parseUtil.getValue("PortNumber","Configuration"))).intValue());
      // Sets the user name
      ods.setUser(parseUtil.getValue("dbsysaduser","Configuration"));
      // Sets the password
      ods.setPassword(parseUtil.getValue("dbsysadpwd","Configuration"));
      connection=ods.getConnection();
    }catch(SQLException sqlEx){ // Trap SQL errors
      connection=null;
      logger.error("SQLException: "+sqlEx.getMessage());
      sqlEx.printStackTrace();
    }catch(Exception ex){        /* catch other exceptions */
      connection =null;
      logger.error("Exception: "+ex.getMessage());
      ex.printStackTrace();
    }
    return connection;
  }
  
  public Connection getConnection( boolean isForWatch ) {
    try {
      OracleDataSource ods = new OracleDataSource();
      // Sets the driver type
      ods.setDriverType(parseUtil.getValue("DriverType","Configuration"));
      // Sets the database server name
      ods.setServerName(parseUtil.getValue("ServerName","Configuration"));
      // Sets the database name
      ods.setDatabaseName(parseUtil.getValue("DatabaseName","Configuration"));
      // Sets the port number
      ods.setPortNumber((new Integer(parseUtil.getValue("PortNumber","Configuration"))).intValue());
      //String [] splitVals = parseUtil.getValue("Domain","Configuration").split(":");
      String schedUser = parseUtil.getValue("scheduser","Configuration"); 
      // Sets the user name
      ods.setUser(schedUser);
      // Sets the password
      ods.setPassword(parseUtil.getValue("schedpwd","Configuration"));
      connection=ods.getConnection();
    }catch(SQLException sqlEx){ // Trap SQL errors
      connection=null;
      logger.error("SQLException: "+sqlEx.getMessage());
      sqlEx.printStackTrace();
    }catch(Exception ex){        /* catch other exceptions */
      connection =null;
      logger.error("Exception: "+ex.getMessage());
      ex.printStackTrace();
    }
    return connection;
  }
  
  public Statement getStatement(){
    Connection conn = getConnection();
    Statement stmt = null;
    if (conn!=null) {
      //Statement stmt = null;
      try{
        //Statement object for executing queries
        stmt = conn.createStatement();      
      }catch(SQLException sqlEx){ // Trap SQL errors
        logger.error("SQLException: "+sqlEx.getMessage());
        sqlEx.printStackTrace();
      }catch(Exception ex){        /* catch other exceptions */
        logger.error("Exception: "+ex.getMessage());
        ex.printStackTrace();
      }
    }    /* end if*/
    connection=conn;
    statement=stmt;
    return statement;
  }

  public Statement getStatement( boolean isForWatch ){
   Connection conn = getConnection(isForWatch);
   Statement stmt = null;
   if (conn!=null) {
     //Statement stmt = null;
     try{
       //Statement object for executing queries
       stmt = conn.createStatement();      
     }catch(SQLException sqlEx){ // Trap SQL errors
       logger.error("SQLException: "+sqlEx.getMessage());
       sqlEx.printStackTrace();
     }catch(Exception ex){        /* catch other exceptions */
       logger.error("Exception: "+ex.getMessage());
       ex.printStackTrace();
     }
   }    /* end if*/
   connection=conn;
   statement=stmt;
   return statement;
  }

  public int executeUpdate( String query ){
    Statement stmt = null;
    int result = 0;
    if( statement == null ){
      stmt = getStatement(true);  
    }else{
      stmt = statement;  
    }
    try{
      result = stmt.executeUpdate(query);
    }catch( SQLException sqlEx ){
      logger.debug(sqlEx.toString());
    }
    return result;
  }
  
  public String [] executeQuery( String query ){
    Statement stmt = null;
    if( statement == null ){
      stmt = getStatement(true);  
    }else{
      stmt = statement;  
    }
    ResultSet rs = null;
    ArrayList resultsArr = null;
    String [] results = null;
    try{
      rs = stmt.executeQuery(query);
      if( rs!=null ){
        resultsArr = new ArrayList();
        while( rs.next() ){
          resultsArr.add(rs.getString("USER_ID"));
        }
        int length = ( resultsArr == null )?0:resultsArr.size();
        results = new String[length];
        for( int index = 0; index < length; index++ ){
          results[index] = (String)resultsArr.get(index);
        }
      }
    }catch( SQLException sqlEx ){
      logger.debug(sqlEx.toString());
    }catch( Exception ex ){
      logger.debug(ex.toString());
    }
    return results;
  }
  
  /* Added by Rajan Sir for executing queries */
  public ResultSet executeQuery(String sqlString,boolean isForWatch)throws SQLException, Exception{
    //Connection conn = getConnection(isForWatch);
    Statement stmt = null;
    int result = 0;
    if( statement == null ){
      stmt = getStatement(true);  
    }else{
      stmt = statement;  
    }
    ResultSet resultSet=null;
    try{
      logger.info("Entering executeQuery");
      //this.statement = conn.createStatement();
      resultSet=stmt.executeQuery(sqlString);
    }catch(SQLException sQLException ){
      throw sQLException;
    }catch(Exception exception ){
      throw exception;
    }finally{
      logger.info("Exiting executeQuery");
    }
    return resultSet;
  }
  /* Added by Rajan Sir for executing queries ends */
  
  public boolean isUserWatchAdded( String [] results, String userName ){
    int length = ( results == null )?0:results.length;
    boolean isWatchAdded = false;
    for( int index = 0; index < length; index++ ){
      if( results[index].equalsIgnoreCase(userName) ){
        isWatchAdded = true;
        break;
      }
    }
    return isWatchAdded;
  }
  
  public void closeStatement(){
    if (statement!=null) {
      //Statement stmt = null;
      logger.debug("Statement not null");
      try{
        //Statement object for executing queries
        statement.close();
        logger.debug("Statement closed");    
      }catch(SQLException sqlEx){ // Trap SQL errors
        logger.error("SQLException: "+sqlEx.getMessage());
        sqlEx.printStackTrace();
      }catch(Exception ex){        /* catch other exceptions */        
        logger.error("Exception: "+ex.getMessage());
        ex.printStackTrace();
      }
    }    /* end if*/
  }
  
   public void closeConnection(){
    if (connection!=null) {
      //Statement stmt = null;
      logger.debug("connection not null");
      try{
        //Statement object for executing queries
        connection.close();
        logger.debug("connection closed");
      }catch(SQLException sqlEx){ // Trap SQL errors
        logger.error("SQLException: "+sqlEx.getMessage());
        sqlEx.printStackTrace();
      }catch(Exception ex){        /* catch other exceptions */
        logger.error("Exception: "+ex.getMessage());
        ex.printStackTrace();
      }
    }    /* end if*/
  }

}