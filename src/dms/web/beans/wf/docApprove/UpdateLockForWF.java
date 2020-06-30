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
 * $Id: UpdateLockForWF.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsLockObjectDefinition;
import dms.beans.DbsPublicObject;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             To update document lock in workflow using lock defn.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class UpdateLockForWF  {
  DbsLibrarySession systemSession=null;
  Logger logger=null;
  public UpdateLockForWF(String relativePath) {
       logger = Logger.getLogger("DbsLogger");
       try{
           ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);
           String ifsServerService = parseUtil.getValue("IfsService","Configuration"); 
           DbsLibraryService dbsLibraryService = DbsLibraryService.findService(ifsServerService);
           String systemUser=  parseUtil.getValue("sysaduser","Configuration");
           String systemPasswd=  parseUtil.getValue("sysadpwd","Configuration");
           DbsCleartextCredential systemCredential=new DbsCleartextCredential(systemUser,systemPasswd);
           systemSession = dbsLibraryService.connect(systemCredential,null); 
       }catch(DbsException dbsExcep){
            logger.error("Exception occured in UpdateLockFoWF Constructor"+dbsExcep.toString());
        }catch(Exception excep){
            logger.error("Exception occured in UpdateLockFoWF Constructor"+excep.toString());
        }
  }
        
  public void updateLock(DbsLockObjectDefinition lockDefn,DbsPublicObject document){
        try{
          document.lock(lockDefn);
        }catch(DbsException dbsExcep){
            logger.error("Exception occured in UpdateLockFoWF updateLock()"+dbsExcep.getErrorMessage());
        }catch(Exception excep){
            logger.error("Exception occured in UpdateLockFoWF updateLock()"+excep.toString());
        }finally{
          try{
             if(systemSession!=null && systemSession.isConnected()){
                systemSession.disconnect();
             }
          }catch(DbsException dbsExcep){
            logger.error("Exception occured in UpdateLockFoWF "+dbsExcep.toString());
          }catch(Exception excep){
            logger.error("Exception occured in UpdateLockFoWF "+excep.toString());
        }
        }
    
  }
       
}
