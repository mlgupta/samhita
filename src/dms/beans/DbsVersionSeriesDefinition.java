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
 * $Id: DbsVersionSeriesDefinition.java,v 20040220.2 2006/01/25 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/* cmsdk API */
import oracle.ifs.beans.VersionSeriesDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of VersionSeriesDefinition
 *           class provided by CMSDK API.
 * 
 *  @author           Suved Mishra
 *  @version          1.0
 * 	Date of creation: 25-01-2006
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class DbsVersionSeriesDefinition extends DbsPublicObjectDefinition {
  // private member variable for accessing VersionSeriesDefinition
  private VersionSeriesDefinition versionSeriesDefinition = null;

  /**
   * Purpose:Constructor for DbsVersionSeriesDefinition Class
   * @throws dms.beans.DbsException
   * @param dbsSession
   */
  public DbsVersionSeriesDefinition( DbsLibrarySession dbsSession ) 
         throws DbsException{
    super(dbsSession);
    try{
      this.versionSeriesDefinition = new VersionSeriesDefinition(
                                         dbsSession.getLibrarySession());
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose:Method to get the cmsdk VersionSeriesDefinition object
   * @return VersionSeriesDefinition object
   */
  public VersionSeriesDefinition getVersionSeriesDefinition(){
    return this.versionSeriesDefinition;
  }

  /**
   * Purpose:Set the Family definition for the new PublicObject.
   * @throws dms.beans.DbsException
   * @param dbsFamDef
   */
  public void setFamilyDefinition( DbsFamilyDefinition dbsFamDef ) 
         throws DbsException {
    try{
      this.versionSeriesDefinition.setFamilyDefinition(
                                   dbsFamDef.getFamilyDefinition());
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }
  
}