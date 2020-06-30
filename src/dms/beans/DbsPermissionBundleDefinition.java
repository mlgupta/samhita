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
 * $Id: DbsPermissionBundleDefinition.java,v 20040220.4 2006/02/28 11:55:47 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.PermissionBundleDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of PermissionBundleDefinition 
 *           class provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    23-12-2003
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   28-02-2006 
 */
public class DbsPermissionBundleDefinition extends DbsSystemObjectDefinition{
  // member variable to accept object of type PermissionBundleDefinition    
  private PermissionBundleDefinition permissionBundleDef = null;

  /**
   * Purpose : Constructor for DbsPermissionBundleDefinition using 
   *           PermissionBundleDefinition class
   * @param  : dbsSession - the session
   */
  public DbsPermissionBundleDefinition(DbsLibrarySession dbsSession) 
         throws DbsException {
    super(dbsSession);
    try{
      this.permissionBundleDef = new PermissionBundleDefinition(
                                     dbsSession.getLibrarySession());  
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Returns the AccessLevel to be associated with this PermBundle.
   * @throws dms.beans.DbsException
   * @return DbsAccessLevel
   */
  public DbsAccessLevel getAccessLevel()throws DbsException{
    try{
      return new DbsAccessLevel(this.permissionBundleDef.getAccessLevel());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Sets the AccessLevel to be associated with this PermissionBundle.
   * @throws dms.beans.DbsException
   * @param accessLevel
   */
  public void setAccessLevel(DbsAccessLevel accessLevel)throws DbsException{
    try{
      this.permissionBundleDef.setAccessLevel(accessLevel.getAccessLevel());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Used to get the object of class PermissionBundleDefinition
   * @returns : PermissionBundleDefinition Object
   */
  public PermissionBundleDefinition getPermissionBundleDefinition() {
    return this.permissionBundleDef ;
  }
}
