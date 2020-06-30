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
 * $Id: DbsVersionDescriptionDefinition.java,v 20040220.2 2006/02/28 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/*CMSDK API*/ 
import oracle.ifs.beans.VersionDescriptionDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of VersionDescriptionDefinition
 *           class provided by CMSDK API.
 * 
 *  @author           Suved Mishra
 *  @version          1.0
 * 	Date of creation: 25-01-2006
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class DbsVersionDescriptionDefinition extends DbsPublicObjectDefinition {
  // private member variable for accessing VersionDescriptionDefinition
  private VersionDescriptionDefinition vdd = null;
  
  /**
   * Purpose:Constructor for DbsVersionDescriptionDefinition Class
   * @throws dms.beans.DbsException
   * @param dbsSession
   */
  public DbsVersionDescriptionDefinition(DbsLibrarySession dbsSession) 
         throws DbsException {
    super(dbsSession);
    try{
      this.vdd = new VersionDescriptionDefinition(dbsSession.getLibrarySession());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose:Method to get the cmsdk VersionDescriptionDefinition object
   * @return VersionDescriptionDefinition object
   */
  public VersionDescriptionDefinition getVersionDescriptionDefinition(){
    return this.vdd;
  }
  
  /**
   * Purpose:Set the Version Description definition to reference an exiting 
   *         PublicObject. 
   * @throws dms.beans.DbsException
   * @param dbsPublicObject
   */
  public void setPublicObject(DbsPublicObject dbsPublicObject) 
         throws DbsException{
    try{
      this.vdd.setPublicObject(dbsPublicObject.getPublicObject());  
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose:This option specifies whether the new VersionDescription should 
   *         have the same owner as the PublicObject it is refering to.
   * @throws dms.beans.DbsException
   * @param boolOption
   */
  public void setOwnerBasedOnPublicObjectOption(boolean boolOption) 
         throws DbsException{
    try{
      this.vdd.setOwnerBasedOnPublicObjectOption(boolOption);
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose:Set the Version Series definition for the new PublicObject.
   * @throws dms.beans.DbsException
   * @param dbsVsd
   */
  public void setVersionSeriesDefinition(DbsVersionSeriesDefinition dbsVsd ) 
         throws DbsException{
    try{
      this.vdd.setVersionSeriesDefinition(dbsVsd.getVersionSeriesDefinition());
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }

}