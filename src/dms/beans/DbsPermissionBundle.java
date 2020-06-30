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
 * $Id: DbsPermissionBundle.java,v 20040220.3 2006/02/28 09:44:10 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/* CMSDK API */
import oracle.ifs.beans.PermissionBundle;
import oracle.ifs.common.IfsException;
/**
 *	Purpose : To encapsulate the functionality of PermissionBundle class provided
 *            by CMSDK API.
 * 
 * @author               Rajan Kamal Gupta
 * @version              1.0
 * 	Date of creation:    22-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  28-02-2006  
 */
public class DbsPermissionBundle extends DbsSystemObject {
  // member variable to accept object of type PermissionBundle
  private PermissionBundle permissionBundle = null;  
  /* The level of the permission represented by this PermissionBundle */
  public static java.lang.String ACCESSLEVEL_ATTRIBUTE =
                                 PermissionBundle.ACCESSLEVEL_ATTRIBUTE; 
  /* The name of this class */
  public static java.lang.String CLASS_NAME=PermissionBundle.CLASS_NAME;
  /* A textual description of the PermissionBundle. */
  public static java.lang.String DESCRIPTION_ATTRIBUTE =
                                 PermissionBundle.DESCRIPTION_ATTRIBUTE;
  /* The extended permission levels created by a custom application which are 
   * associated with this PermissionBundle. */
  public static java.lang.String EXTENDEDPERMISSIONS_ATTRIBUTE =
                                 PermissionBundle.EXTENDEDPERMISSIONS_ATTRIBUTE;       
  /* The name of the PermissionBundle. */
  public static java.lang.String NAME_ATTRIBUTE = 
                                 PermissionBundle.NAME_ATTRIBUTE;          

  /**
   * Purpose : To create DbsPermissionBundle using PermissionBundle class
   * @param  : permissionBundle - An PermissionBundle Object  
   */
  public DbsPermissionBundle(PermissionBundle permissionBundle){
    super(permissionBundle);
    this.permissionBundle=permissionBundle;
  }

  /**
   * Purpose  : Returns the access level represented by this ACE.
   * @returns : the access level represented by this ACE
   * @throws  : DbsException - if operation fails
   */
  public DbsAccessLevel getAccessLevel() throws DbsException {
    try{
      return new DbsAccessLevel(permissionBundle.getAccessLevel());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : To set access level represented by this ACE.
   * @throws  : DbsException - if operation fails
   */
  public void setAccessLevel(DbsAccessLevel dbsAccessLevel) throws DbsException{
    try{
     permissionBundle.setAccessLevel(dbsAccessLevel.getAccessLevel());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Returns description of this object.
   * @returns : The object description
   * @throws  : DbsException - if operation fails
   */
  public String getDescription() throws DbsException {
    try{
      return this.permissionBundle.getDescription();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Update the description of this object. 
   * @throws dms.beans.DbsException
   * @param description
   */
  public void setDescription(String description) throws DbsException {
    try{
      this.permissionBundle.setDescription(description);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : get the cmsdk permission bundle
   * @return PermissionBundle
   */
  public PermissionBundle getPermissionBundle(){
    return this.permissionBundle;
  }
}
