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
 * $Id: DbsAttributeValue.java,v 20040220.16 2006/02/28 11:50:09 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/
import oracle.ifs.beans.AccessControlList;
import oracle.ifs.beans.DirectoryGroup;
import oracle.ifs.beans.DirectoryUser;
import oracle.ifs.beans.Document;
import oracle.ifs.beans.Folder;
import oracle.ifs.beans.PropertyBundle;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.common.AttributeValue;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of AccessAttributeValue class 
 *           provided by CMSDK API.
 * 
 *  @author            Mishra Maneesh
 *  @version           1.0
 * 	Date of creation:  23-12-2003
 * 	Last Modified by :  15-12-2005  
 *	Last Modified Date: Suved Mishra  
 */
public class DbsAttributeValue {
  private  AttributeValue attributeValue=null;  // to accept object of type AttributeValue
  public static String CONST="CONST";

  /**
   * Purpose : To create DbsAttributeValue using AttributeValue class
   * @param  : attributeValue - An AttributeValue Object  
   */
  public DbsAttributeValue(AttributeValue attributeValue) {
    this.attributeValue=attributeValue;
  }

  /**
   * Purpose  : Constructs a new string AttributeValue.
   * @param   : value - the string value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(java.lang.String strValue) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(strValue));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new string AttributeValue.
   * @param   : value - the string value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(java.lang.String[] strValue) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(strValue));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new long AttributeValue.
   * @param   : value - the long value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(long value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new Long AttributeValue.
   * @param   : value - the Long value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(Long value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new int AttributeValue.
   * @param   : value - the int value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(int value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new Integer AttributeValue.
   * @param   : value - the Integer value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(Integer value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new boolean AttributeValue.
   * @param   : value - the boolean value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(boolean value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new Access Control List AttributeValue.
   * @param   : value - the Access Control List value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(DbsAccessControlList dbsValue) throws DbsException {
    DbsAttributeValue dbsAttrValue=null;
    try{ 
      AttributeValue attrValue=AttributeValue.newAttributeValue(dbsValue.getAccessControlList());
      dbsAttrValue=new DbsAttributeValue(attrValue);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new Access Control List [] AttributeValue.
   * @param   : value - the Access Control List []
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(DbsAccessControlList[] dbsValues) throws DbsException {
    DbsAttributeValue dbsAttrValue=null;
    //DbsPublicObject [] dbsPOs = null;
    PublicObject [] pOs = null;
    try{ 
      if( dbsValues!=null ){
        //dbsPOs = new DbsPublicObject[dbsValues.length];
        pOs = new PublicObject[dbsValues.length];
        for( int index=0;index<dbsValues.length;index++ ){
          //dbsPOs[index] = dbsValues[index];
          pOs[index] = dbsValues[index].getPublicObject();
        }
        AttributeValue attrValue=AttributeValue.newAttributeValue(pOs);
        dbsAttrValue=new DbsAttributeValue(attrValue);
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose : Constructs a new Format AttributeValue.
   * @throws : dms.beans.DbsException
   * @return : the new AttributeValue
   * @param  : dbsValue
   */
   public static DbsAttributeValue newAttributeValue(DbsFormat dbsValue) throws DbsException {
      DbsAttributeValue dbsAttrValue=null;
      try{ 
           AttributeValue attrValue=AttributeValue.newAttributeValue(dbsValue.getFormat());
           dbsAttrValue=new DbsAttributeValue(attrValue);
      }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
      }
      return dbsAttrValue;
    }

  /**
   * Purpose  : Constructs a new Folder AttributeValue.
   * @param   : value - the Folder value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(DbsFolder dbsValue) throws DbsException{
    DbsAttributeValue dbsAttrValue=null;
    try{ 
      AttributeValue attrValue=AttributeValue.newAttributeValue(dbsValue.getFolder());
      dbsAttrValue=new DbsAttributeValue(attrValue);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new AttributeValue.
   * @param   : value - the AttributeValue value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(DbsAttributeValue dbsValue) throws DbsException {
    DbsAttributeValue dbsAttrValue=null;
    try{ 
      AttributeValue attrValue=AttributeValue.newAttributeValue(dbsValue.getAttributeValue());
      dbsAttrValue=new DbsAttributeValue(attrValue);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new PropertyBundle AttributeValue.
   * @param   : value - the PropertyBundle value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(DbsPropertyBundle dbsPropertyBundle) throws DbsException {
    DbsAttributeValue dbsAttrValue=null;
    try{ 
      AttributeValue attrValue=AttributeValue.newAttributeValue(dbsPropertyBundle.getDbsPropertyBundle());
      dbsAttrValue=new DbsAttributeValue(attrValue);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new Date AttributeValue.
   * @param   : value - the Date value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(java.util.Date value) throws DbsException {
    try{
      return new DbsAttributeValue(AttributeValue.newAttributeValue(value));
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Constructs a new Object AttributeValue.
   * @param   : value - the Object value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(Object obj) throws DbsException{
    DbsAttributeValue dbsAttrValue=null;
    try{ 
      AttributeValue attrValue=AttributeValue.newAttributeValue(obj);
      dbsAttrValue=new DbsAttributeValue(attrValue);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new Integer[] AttributeValue.
   * @param   : value - the Integer[] value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(Integer[] value) throws DbsException {
    DbsAttributeValue dbsAttrValue = null;
    try{
      AttributeValue attrValue = AttributeValue.newAttributeValue(value);
      dbsAttrValue = new DbsAttributeValue(attrValue);
    }catch( IfsException ifsEx ){
      throw new DbsException(ifsEx);
    }
    return dbsAttrValue;
  }

  /**
   * Purpose  : Constructs a new Long[] AttributeValue.
   * @param   : value - the Long[] value
   * @returns : the new AttributeValue
   * @throws  : DbsException - if the operation fails
   */
  public static DbsAttributeValue newAttributeValue(Long[] value) throws DbsException {
    DbsAttributeValue dbsAttrValue = null;
    try{
      AttributeValue attrValue = AttributeValue.newAttributeValue(value);
      dbsAttrValue = new DbsAttributeValue(attrValue);
    }catch( IfsException ifsEx ){
      throw new DbsException(ifsEx);
    }
    return dbsAttrValue;
  }
    
  /**
   * Purpose    : Gets the default object representation of this AttributeValue as a string.
   * @overrides : toString in class java.lang.Object
   * @returns   : a string
   */
  public String toString() {
    return this.attributeValue.toString();
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a boolean.
   * @param   : dbsSession - the Librarysession
   * @returns : the boolean value
   * @throws  : DbsException - if the operation fails
   */
  public boolean getBoolean(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
      return this.attributeValue.getBoolean(dbsSession.getLibrarySession()); 
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a int.
   * @param   : dbsSession - the Librarysession
   * @returns : the int value
   * @throws  : DbsException - if the operation fails
   */
  public int getInteger(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
     return this.attributeValue.getInteger(dbsSession.getLibrarySession()); 
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as an Object.
   * @param   : dbsSession - the Librarysession
   * @returns : the Object value
   * @throws  : DbsException - if the operation fails
   */
  public Object getObject(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
      return this.attributeValue.getObject(dbsSession.getLibrarySession()); 
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a String.
   * @param   : dbsSession - the Librarysession
   * @returns : the String value
   * @throws  : DbsException - if the operation fails
   */
  public String getString(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
      return this.attributeValue.getString(dbsSession.getLibrarySession()); 
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a String[].
   * @param   : dbsSession - the Librarysession
   * @returns : the String[] 
   * @throws  : DbsException - if the operation fails
   */
  public String[] getStringArray(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
      return this.attributeValue.getStringArray(dbsSession.getLibrarySession()); 
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a PublicObject.
   * @param   : dbsSession - the Librarysession
   * @returns : the PublicObject value
   * @throws  : DbsException - if the operation fails
   */
  public DbsPublicObject getPublicObject(DbsLibrarySession dbsSession) throws DbsException {
    try{ 
      if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof Document){
        return new DbsDocument((Document)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof Folder){
        return new DbsFolder((Folder)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof DirectoryUser){
        return new DbsDirectoryUser((DirectoryUser)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof DirectoryGroup){
        return new DbsDirectoryGroup((DirectoryGroup)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof AccessControlList){
        return new DbsAccessControlList((AccessControlList)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else if((attributeValue.getPublicObject(dbsSession.getLibrarySession())) instanceof PropertyBundle){
        return new DbsPropertyBundle((PropertyBundle)(attributeValue.getPublicObject(dbsSession.getLibrarySession())));
      }else{
        return new DbsPublicObject((PublicObject)attributeValue.getPublicObject(dbsSession.getLibrarySession()));
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as a PublicObject.
   * @param   : dbsSession - the Librarysession
   * @returns : the PublicObject value
   * @throws  : DbsException - if the operation fails
   */
  public DbsPublicObject[] getPublicObjectArray(DbsLibrarySession dbsSession) throws DbsException {
    DbsPublicObject [] dbsPOs = null;
    PublicObject [] pOs = null;
    try{ 
      pOs = (PublicObject [])attributeValue.getPublicObjectArray(dbsSession.getLibrarySession());
      if( pOs != null ){
        dbsPOs = new DbsPublicObject[pOs.length];
        for( int index = 0; index < pOs.length; index++ ){
          if( pOs [index] instanceof Document){
            dbsPOs [index] = new DbsDocument((Document)(pOs[index]));
          }else if(pOs [index] instanceof Folder){
            dbsPOs [index] = new DbsFolder((Folder)(pOs[index]));
          }else if(pOs[index] instanceof DirectoryUser){
            dbsPOs [index] = new DbsDirectoryUser((DirectoryUser)(pOs[index]));
          }else if(pOs[index] instanceof DirectoryGroup){
            dbsPOs [index] = new DbsDirectoryGroup((DirectoryGroup)(pOs[index]));
          }else if(pOs[index] instanceof AccessControlList){
            dbsPOs [index] = new DbsAccessControlList((AccessControlList)(pOs[index]));
          }else if(pOs[index] instanceof PropertyBundle){
            dbsPOs [index] = new DbsPropertyBundle((PropertyBundle)(pOs[index]));
          }else{
            dbsPOs [index] = new DbsPublicObject((PublicObject)pOs[index]);
          }
        }
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsPOs;
  }

  /**
   * Purpose  : Gets whether this AttributeValue is null.
   * @returns : whether the AttributeValue is null
   * @throws  : DbsException - if the operation fails
   */
  public boolean isNullValue()throws DbsException{
    try{
      return this.attributeValue.isNullValue();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Used to get the object of class AttributeValue
   * @return AttributeValue Object
   */
  public AttributeValue getAttributeValue() {
    return this.attributeValue;
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as an int []
   * @param   : dbsSession - the Librarysession
   * @returns : the int [] value
   * @throws  : DbsException - if the operation fails
   */
  public int [] getIntegerArray(DbsLibrarySession dbsSession) throws DbsException {
    try{
      return this.attributeValue.getIntegerArray(dbsSession.getLibrarySession());
    }catch (IfsException ifsEx) {
      throw new DbsException(ifsEx);
    }
  }

  /**
   * Purpose  : Gets this AttributeValue's representation as long []
   * @param   : dbsSession - the Librarysession
   * @returns : the long [] value
   * @throws  : DbsException - if the operation fails
   */
  private long[] getLongArray(DbsLibrarySession dbsSession) throws DbsException {
    try {
      return this.attributeValue.getLongArray(dbsSession.getLibrarySession());
    }catch (IfsException ifsEx) {
      throw new DbsException(ifsEx);
    }
  }

}
