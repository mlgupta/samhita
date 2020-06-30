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
 * $Id: DbsLibraryObjectDefinition.java,v 20040220.8 2006/02/28 11:55:13 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.LibraryObjectDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of LibraryObjectDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author            Mishra Maneesh
 *  @version           1.0
 * 	Date of creation:  23-12-2003
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 28-02-2006   
 */
public class DbsLibraryObjectDefinition extends Object{
  // member variable to accept object of type PublicObjectDefinition
  private LibraryObjectDefinition libraryObjectDef = null;  

  /**
   * Purpose  : Used to get the object of class LibraryObjectDefinition
   * @param   : dbsSession - Constructs a LibraryObjectDefinition explicitly 
   *            capturing the session.
   */
  public DbsLibraryObjectDefinition(DbsLibrarySession dbsSession) throws 
         DbsException {
    try{
      this.libraryObjectDef = new LibraryObjectDefinition(
                                  dbsSession.getLibrarySession());  
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Set an attribute for the new instance.
   * @throws dms.beans.DbsException
   * @param dbsAttrValue
   * @param name
   */
  public void setAttribute(String name, DbsAttributeValue dbsAttrValue)
         throws DbsException{
    try{
      this.libraryObjectDef.setAttribute(name,dbsAttrValue.getAttributeValue());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Set name for the new instance.
   * @throws dms.beans.DbsException
   * @param name
   */
  public void setName(String name)throws DbsException{
    try{
      this.libraryObjectDef.setName(name);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }        
  }

  /**
   * Purpose  : Set an attribute for the new instance.
   * @throws dms.beans.DbsException
   * @param dbsAttributeValue
   * @param name
   */
  public void setAttributeByUpperCaseName(String name, 
         DbsAttributeValue dbsAttributeValue)throws DbsException{
    try{
      this.libraryObjectDef.setAttributeByUpperCaseName(name,
                            dbsAttributeValue.getAttributeValue());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }      
  }
 
  /**
   * Purpose  : Used to get the object of class LibraryObjectDefinition
   * @returns : LibraryObjectDefinition Object
   */
  public LibraryObjectDefinition getLibraryObjectDefinition() {
    return this.libraryObjectDef ;
  }
}
