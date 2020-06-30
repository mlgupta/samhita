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
 * $Id: DbsFamilyDefinition.java,v 20040220.2 2006/01/25 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/* cmsdk API */
import oracle.ifs.beans.FamilyDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of DbsFamilyDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author           Suved Mishra
 *  @version          1.0
 * 	Date of creation: 25-01-2006
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class DbsFamilyDefinition  extends DbsPublicObjectDefinition {
  // private member variable for accessing FamilyDefinition
  private FamilyDefinition familyDefinition = null;
  
  /**
   * Constructor for DbsFamilyDefinition Class
   * @throws dms.beans.DbsException
   * @param dbsSession
   */
  public DbsFamilyDefinition(DbsLibrarySession dbsSession) throws DbsException{
    super(dbsSession);
    try{
      this.familyDefinition = new FamilyDefinition(
                                  dbsSession.getLibrarySession());
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Used to get the object of class FamilyDefinition
   * @return FamilyDefinition Object
   */
  public FamilyDefinition getFamilyDefinition() {
    return this.familyDefinition;
  }

}