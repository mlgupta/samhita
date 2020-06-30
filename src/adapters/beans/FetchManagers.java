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
 * $Id: FetchManagers.java,v 1.4 2006/02/02 12:30:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.utility.SearchUtil;
/* Struts API */
import org.apache.log4j.Logger;

/**
 * Purpose: Bean called to fetch managers from given acl.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 14-01-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class FetchManagers  {
  /* member variables declaration goes here */
  private Logger logger;              /* represents logger for verbose logging */ 
  private LoginBean loginBean;        /* represents login bean to fetch library session */
  public DbsLibrarySession dbsSession;/* represents library session */
  
  public FetchManagers() {
  }
  
  /**
   * Constructor for FetchManagers Class
   * @param relativePath
   */
  public FetchManagers(String relativePath){
    logger = Logger.getLogger("DbsLogger");
    loginBean = new LoginBean(relativePath);
    this.dbsSession = loginBean.getUserSession();
    logger.info("Fetch Managers initailized successfully");
  }

  /**
   * Purpose:Terminates the user library session 
   * @param: none 
   */
  private void terminateSession(){
    try{
      if( this.dbsSession != null && this.dbsSession.isConnected()){
        this.dbsSession.disconnect();
        logger.debug("LibrarySession disconnected successfully ...");
        this.dbsSession = null;
      }
    }catch( DbsException dbsEx ){
      logger.error("Exception occurred in FetchManagers...");
      logger.error(dbsEx.toString());
    }finally{
      try {
        /* disconnect the library session */
        if( this.dbsSession != null && this.dbsSession.isConnected()){
          this.dbsSession.disconnect();        
        }
      }catch (DbsException dbsEx) {
        logger.error("Exception occurred in FetchManagers...");
        logger.error(dbsEx.toString());
      }
    }
  }

  /**
   * Purpose : Fetches ACEs( viz manager names ) as csv if found , else null 
   *           from a given ACL name 
   * @param  : aclName
   * @return : managers in a string as csv.
   */
  public String getManagers( String aclName ){
    DbsAccessControlList [] acls = null;
    DbsAccessControlEntry [] aces = null;
    String managers = null;
    try{
      try{
        /* fetch the ACL object corresponding to aclName */
        acls = SearchUtil.listAclsForWf(this.dbsSession,aclName,-1,-1,false);
      }catch (DbsException dbsEx) {
        logger.error(dbsEx.toString());
      }
      if( acls != null ){
        /* fetch the access control entries for the given ACL object */
        aces = acls[0].getAccessControlEntrys();
        StringBuffer sb = new StringBuffer();
        int length = ( aces != null )?aces.length:0;
        for( int index = 0; index < length; index++ ){
          sb.append(aces[index].getGrantee().getName());
          sb.append(",");
        }
        if( sb.length() != 0 ){
          sb = sb.deleteCharAt(sb.length()-1);
          managers = sb.toString();
        }
      }else{
        logger.debug("Invalid AclName specified... ");
      }
    }catch (DbsException dbsEx) {
      logger.error("Exception occurred in FetchManagers...");
      logger.error(dbsEx.toString());      
    }finally{
      /* terminate library session */
      if( loginBean != null ){
        loginBean.terminateSession();
      }
      if( this.dbsSession != null && this.dbsSession.isConnected() ){
        terminateSession();
        this.dbsSession = null;
      }
    }
    return managers;
  }
  
}