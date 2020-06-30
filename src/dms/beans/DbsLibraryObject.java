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
 * $Id: DbsLibraryObject.java,v 20040220.6 2006/02/28 11:55:13 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.LibraryObject;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of LibraryObject class provided by
 *           CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsLibraryObject {
  // member variable to accept object of type LibraryObject
  private LibraryObject libraryObject = null;  

  protected DbsLibraryObject(){}
  
  /**
   * Purpose : To create DbsLibraryObject using LibraryObject class
   * @param  : libraryObject - An LibraryObject Object  
   */    
  public DbsLibraryObject(LibraryObject libraryObject) {
    this.libraryObject = libraryObject;
  }

  /**
   * Purpose: Returns the Name of this object, or null of this object does not 
   *          have a NAME attribute.
   * @throws dms.beans.DbsException
   * @return name of the library object as String
   */
  public String getName()throws DbsException{
    try{
      return this.libraryObject.getName();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose: Returns this object's ID (as a Long).
   * @throws dms.beans.DbsException
   * @return Long id of the selected object
   */
  public Long getId() throws DbsException{
    try{
      return this.libraryObject.getId();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose:Returns an AttributeValue object that holds the value of the 
   *         requested attribute.
   * @throws dms.beans.DbsException
   * @return attribute value as DbsAttributeValue
   * @param attrName
   */
  public DbsAttributeValue getAttribute(String attrName)throws DbsException{
    DbsAttributeValue attrValue=null;
    try{           
      if(this.libraryObject.getAttribute(attrName)!=null){
        attrValue= new DbsAttributeValue(this.libraryObject.getAttribute(attrName));
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return attrValue;
  } 
  
  /**
   * Purpose:Set this object's attribute to the specified value.
   * @throws dms.beans.DbsException
   * @param attrValue
   * @param attrName
   */
  public void setAttribute(String attrName,DbsAttributeValue attrValue)throws 
         DbsException{
    try{
      this.libraryObject.setAttribute(attrName,attrValue.getAttributeValue());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose:Set the Name of this object.
   * @throws dms.beans.DbsException
   * @param name
   */
  public void setName(java.lang.String name) throws DbsException{
    try{
      this.libraryObject.setName(name);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose:Permanently deletes this object.
   * @throws dms.beans.DbsException
   */
  public void free()throws DbsException{
    try{
      this.libraryObject.free();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

 /**
  * Purpose  : Used to get the object of class LibraryObject
  * @returns : LibraryObject Object
  */
  public LibraryObject getLibraryObject() {
    return this.libraryObject ;
  }
}
