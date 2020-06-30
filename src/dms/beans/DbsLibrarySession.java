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
 * $Id: DbsLibrarySession.java,v 20040220.14 2006/02/28 08:50:24 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*Java API*/
import java.io.Serializable;
/*CMSDK API*/
import oracle.ifs.beans.AccessControlEntry;
import oracle.ifs.beans.AccessControlEntryDefinition;
import oracle.ifs.beans.AccessControlList;
import oracle.ifs.beans.AccessControlListDefinition;
import oracle.ifs.beans.ContentQuota;
import oracle.ifs.beans.ContentQuotaDefinition;
import oracle.ifs.beans.DirectoryGroup;
import oracle.ifs.beans.DirectoryGroupDefinition;
import oracle.ifs.beans.Document;
import oracle.ifs.beans.Family;
import oracle.ifs.beans.Folder;
import oracle.ifs.beans.Format;
import oracle.ifs.beans.LibrarySession;
import oracle.ifs.beans.PermissionBundle;
import oracle.ifs.beans.PermissionBundleDefinition;
import oracle.ifs.beans.PropertyBundle;
import oracle.ifs.beans.PropertyBundleDefinition;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.beans.SystemObject;
import oracle.ifs.beans.ValueDefault;
import oracle.ifs.beans.ValueDefaultDefinition;
import oracle.ifs.beans.ValueDefaultPropertyBundle;
import oracle.ifs.beans.ValueDefaultPropertyBundleDefinition;
import oracle.ifs.beans.VersionDescription;
import oracle.ifs.common.Collection;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of LibrarySession class provided 
 *           by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    23-12-2003
 * 	Last Modified by :  Suved Mishra    
 * 	Last Modified Date: 25-01-2006   
 */
public class DbsLibrarySession {
  // member variable to accept object of type LibrarySession
  private LibrarySession librarySession; 

  /**
   * Purpose : To create DbsLibrarySesion using LibrarySesion class
   * @param  : librarySesion - An LibrarySesion Object  
   */
  public DbsLibrarySession(LibrarySession librarySession) {
      this.librarySession=librarySession;
  }

  /**
   * Purpose  : Aborts a transaction.
   * @param   : dbsTransaction - the transaction to abort
   * @throws  : DbsException - if operation fails
   */
  public final void abortTransaction(DbsTransaction dbsTransaction) 
         throws DbsException {
    try{
      librarySession.abortTransaction(dbsTransaction.getTransaction());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Starts a writeable transaction.
   * @returns : a token that can be passed to either completeTransaction or 
   *            abortTransaction
   * @throws  : DbsException - if operation fails
   */  
  public final DbsTransaction beginTransaction() throws DbsException {
    DbsTransaction dbsTransaction=null;
    try{
      dbsTransaction=new DbsTransaction( librarySession.beginTransaction());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsTransaction;
  }

  /**
   * Purpose  : Starts a writeable transaction.
   * @param   : Completes a transaction.
   * @throws  : DbsException - if operation fails
   */  
  public final void completeTransaction(DbsTransaction dbsTransaction) 
         throws DbsException{
    try{
      librarySession.completeTransaction(dbsTransaction.getTransaction());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }
   
  /**
   * Purpose : Create a new Public object.  
   * @throws dms.beans.DbsException
   * @return DbsDirectoryGroup
   * @param dgDef
   */
  public DbsDirectoryGroup createPublicObject(DbsDirectoryGroupDefinition dgDef)
         throws DbsException {
    DbsDirectoryGroup dbsDirGroup = null;
    DirectoryGroupDefinition dirGroupDef = null;
    DirectoryGroup dirGroup = null;
    try{
      dirGroupDef=dgDef.getDirectoryGroupDefinition(); 
      dirGroup = (DirectoryGroup)this.librarySession.createPublicObject(
                                      dirGroupDef);
      dbsDirGroup = new DbsDirectoryGroup(dirGroup);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsDirGroup ;
  }

  /**
   * Purpose : Create a new Public object.
   * @throws dms.beans.DbsException
   * @return DbsAccessControlList
   * @param dbsAclDef
   */
  public DbsAccessControlList createPublicObject(DbsAccessControlListDefinition 
         dbsAclDef) throws DbsException {
    DbsAccessControlList dbsAcl = null;
    AccessControlListDefinition aclDef = null;
    AccessControlList acl = null;
    try{
      aclDef=dbsAclDef.getAccessControlListDefinition(); 
      acl=(AccessControlList)this.librarySession.createPublicObject(aclDef);
      dbsAcl = new DbsAccessControlList(acl);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsAcl ;
  }

  /**
   * Purpose : Create a new Public object.
   * @throws dms.beans.DbsException
   * @return DbsPropertyBundle
   * @param dbsPropertyBundleDef
   */
  public DbsPropertyBundle createPublicObject(DbsPropertyBundleDefinition 
         dbsPropertyBundleDef) throws DbsException {
    DbsPropertyBundle dbsPropertyBundle = null;
    PropertyBundleDefinition propertyBundleDef = null;
    PropertyBundle propertyBundle = null;
    try{
      propertyBundleDef=dbsPropertyBundleDef.getPropertyBundleDefinition();
      propertyBundle=(PropertyBundle)this.librarySession.createPublicObject(
                                                         propertyBundleDef);
      dbsPropertyBundle = new DbsPropertyBundle(propertyBundle);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsPropertyBundle;
  }

  /**
   * Purpose : Create a new Public object.
   * @throws dms.beans.DbsException
   * @return DbsValueDefaultPropertyBundle
   * @param dbsValDefltPBDef
   */
  public DbsValueDefaultPropertyBundle createPublicObject(
         DbsValueDefaultPropertyBundleDefinition dbsValDefltPBDef)
         throws DbsException {
         
    DbsValueDefaultPropertyBundle dbsValDefltPB = null;
    ValueDefaultPropertyBundleDefinition valDefltPBDef = null;
    ValueDefaultPropertyBundle valDefPB = null;
    try{
      valDefltPBDef = dbsValDefltPBDef.getValueDefaultPropertyBundleDefinition();

      valDefPB = 
        (ValueDefaultPropertyBundle)this.librarySession.createPublicObject(
                                                        valDefltPBDef);
      dbsValDefltPB = new DbsValueDefaultPropertyBundle(valDefPB);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsValDefltPB;
  }

  /**
   * Purpose : Create a new Public object.
   * @throws dms.beans.DbsException
   * @return DbsPublicObject
   * @param dbsPublicObjectDefinition
   */
  public DbsPublicObject createPublicObject(DbsPublicObjectDefinition 
         dbsPublicObjectDefinition) throws DbsException{

    DbsPublicObject dbsPublicObject = null;
    DbsDocument dbsDocument = null;
    DbsVersionDescription dbsVersionDescription = null;
    DbsDocumentDefinition dbsDocDef = null;
    DbsFolderDefinition dbsFolderDefinition = null;
    DbsVersionDescriptionDefinition dbsVersionDescriptionDefinition = null;
    PublicObject publicObject = null;
    Document document = null;
    Folder folder = null;
    VersionDescription versionDescription = null;

    try{
      if(dbsPublicObjectDefinition instanceof DbsDocumentDefinition){
        dbsDocDef = (DbsDocumentDefinition)dbsPublicObjectDefinition;
        document = (Document)this.librarySession.createPublicObject(
                             dbsDocDef.getDocumentDefinition());
        dbsDocument = new DbsDocument(document);
        return dbsDocument;
      }else if(dbsPublicObjectDefinition instanceof DbsFolderDefinition){
        dbsFolderDefinition = (DbsFolderDefinition)dbsPublicObjectDefinition;
        folder = (Folder)this.librarySession.createPublicObject(
                              dbsFolderDefinition.getFolderDefinition());
        DbsFolder dbsFolder=new DbsFolder(folder);
        return dbsFolder;
      }else if(dbsPublicObjectDefinition instanceof DbsVersionDescriptionDefinition){
        dbsVersionDescriptionDefinition = (DbsVersionDescriptionDefinition)
                                           dbsPublicObjectDefinition;
        versionDescription = (VersionDescription)
                             (this.librarySession.createPublicObject(
                             dbsVersionDescriptionDefinition.getVersionDescriptionDefinition()));
        dbsVersionDescription = new DbsVersionDescription(versionDescription);
        return dbsVersionDescription;
      }else{                    
        PublicObject pubObject= this.librarySession.createPublicObject(
                          dbsPublicObjectDefinition.getPublicObjectDefinition());              
        dbsPublicObject = new DbsPublicObject(pubObject);
        return dbsPublicObject;
      }
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Create a new system object.
   * @throws dms.beans.DbsException
   * @return DbsAccessControlEntry
   * @param dbsAceDef
   */
  public DbsAccessControlEntry createSystemObject(DbsAccessControlEntryDefinition
         dbsAceDef) throws DbsException {
    DbsAccessControlEntry dbsAce = null;
    AccessControlEntryDefinition aceDef = null;
    AccessControlEntry ace = null;
    try{
      aceDef=dbsAceDef.getAccessControlEntryDefinition(); 
      ace=(AccessControlEntry)this.librarySession.createSystemObject(aceDef);
      dbsAce = new DbsAccessControlEntry(ace);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsAce ;
  }

  /**
   * Purpose : Create a new system object.
   * @throws dms.beans.DbsException
   * @return DbsPermissionBundle
   * @param dbsPermissionBundleDef
   */
  public DbsPermissionBundle createSystemObject(DbsPermissionBundleDefinition 
         dbsPermissionBundleDef) throws DbsException {
    DbsPermissionBundle dbsPermissionBundle = null;
    PermissionBundleDefinition permissionBundleDef  = null;
    PermissionBundle permissionBundle = null;
    try{
      permissionBundleDef=dbsPermissionBundleDef.getPermissionBundleDefinition(); 
      permissionBundle=(PermissionBundle)this.librarySession.createSystemObject(
                                              permissionBundleDef);
      dbsPermissionBundle = new DbsPermissionBundle(permissionBundle);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsPermissionBundle ;
  }

  /**
   * Purpose  : Create a new content quota object. 
   * @throws dms.beans.DbsException
   * @return DbsContentQuota
   * @param dbsContentQuotaDef
   */
  public DbsContentQuota createPublicObject(DbsContentQuotaDefinition 
         dbsContentQuotaDef)throws DbsException {
    DbsContentQuota dbsQuota = null;
    ContentQuotaDefinition quotaDef = null;
    ContentQuota quota = null;
    try{
      quotaDef=dbsContentQuotaDef.getContentQuotaDefinition(); 
      quota=(ContentQuota)this.librarySession.createPublicObject(quotaDef);
      dbsQuota = new DbsContentQuota(quota);            
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsQuota ;
  }
 
  /**
   * Purpose  : Create a new schema object.
   * @param   : soDef - the SchemaObjectDefinition for the new system object
   * @throws  : DbsException - if operation fails
   * @throws dms.beans.DbsException
   * @return DbsValueDefault
   * @param dbsValueDefaultDefinition
   */  
  public DbsValueDefault createSchemaObject(DbsValueDefaultDefinition 
         dbsValueDefaultDefinition) throws DbsException {
    DbsValueDefault dbsValueDefault = null;
    ValueDefaultDefinition  valueDefaultDefinition = null;
    ValueDefault valueDefault = null;
    try{
      valueDefaultDefinition = 
                          dbsValueDefaultDefinition.getValueDefaultDefinition();
      valueDefault=(ValueDefault)librarySession.createSchemaObject(
                                                valueDefaultDefinition);
      dbsValueDefault=new DbsValueDefault(valueDefault);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsValueDefault ;
  }

  /**
   * Purpose  : Disconnects the session, effectively disposing the instance.
   * @returns : status of the disconnect operation (always true)
   * @throws  : DbsException - if operation fails
   */  
  public boolean disconnect() throws DbsException{
    boolean isDisconnected;
    try{
      isDisconnected=librarySession.disconnect();     
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return isDisconnected;
  }

  /**
   * Purpose  : Gets a reference to the ClassAccessControlList collection.
   * @returns : the CachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */  
  public final Collection getClassAccessControlListCollection() 
         throws DbsException {
    Collection collection = null;
    try{
      collection = librarySession.getClassAccessControlListCollection();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return collection ;
  }

  /**
   * Purpose: Returns a class object given a class name. This uses the 
   *          ClassObjectCollection and is simply a convenience method.
   * @throws dms.beans.DbsException
   * @return dbsClassObject
   * @param  className
   */
  public final DbsClassObject getClassObjectByName(String className) 
         throws DbsException{
    DbsClassObject dbsClassObject=null;
    try{
      dbsClassObject= new DbsClassObject(librarySession.getClassObjectByName(
                                                        className));
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsClassObject;    
  }
 
  /**
   * Purpose  : Gets the session's default ContentQuota, set via 
   *            setDefaultContentQuota().
   * @returns : the quota object being used by the session, or null if the 
   *            user's quota is being used.
   * @throws  : DbsException - if operation fails
   */
  public DbsContentQuota getDefaultContentQuota() throws DbsException {
    DbsContentQuota dbsQuota=null;
    try{
      dbsQuota=new DbsContentQuota(librarySession.getDefaultContentQuota());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsQuota;
  }

  /**
   * Purpose  : Lookup a DirectoryObject by its id.
   * @param   : id - identifier for the object
   * @throws  : DbsException - if operation fails
   */
  public DbsDirectoryObject getDirectoryObject(Long id) throws DbsException {
    DbsDirectoryObject dbsDirObj=null;
    try{
      dbsDirObj=new DbsDirectoryObject(librarySession.getDirectoryObject(id));
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsDirObj;
  }

  /**
   * Purpose  : Gets a reference to the DirectoryUser collection.
   * @returns : the UncachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */
  public final DbsCollection getDirectoryUserCollection() throws DbsException {
    DbsCollection collection = null;
    try{
      collection = new DbsCollection(librarySession.getDirectoryUserCollection());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return collection ;
  }

  /**
   * Purpose  : Gets the id of this session.
   * @throws  : DbsException - if operation fails
   */
  public java.lang.Long getId() throws DbsException{
    Long id=null; 
    try{
      id=librarySession.getId();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return id;
  }

  /**
   * Purpose  : Gets a reference to the PermissionBundle collection.
   * @returns : the CachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */
  public final Collection getPermissionBundleCollection() throws DbsException{
    Collection collection = null;
    try{
      collection = librarySession.getPermissionBundleCollection();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return collection ;
  }

  /**
   * Purpose  : Gets a reference to the Format collection.
   * @returns : the CachedResolverCollection
   * @throws  : DbsException - if operation fails
   */
  public final Collection getFormatCollection() throws DbsException{
    Collection collection = null;
    try{
      collection = librarySession.getFormatCollection();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return collection ;
  }

  /**
   * Purpose  : Gets a reference to the Format collection.
   * @returns : the CachedResolverCollection
   * @throws  : DbsException - if operation fails
   */
  public DbsCollection getFormatExtensionCollection()throws DbsException{
    Collection coll=null;
    DbsCollection dbsCollection=null;
    try{
      coll= this.librarySession.getFormatExtensionCollection();
      dbsCollection  = new DbsCollection(coll);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    } 
    return dbsCollection;
  }

  /**
   * Purpose  : Gets a reference to the Policy collection.
   * @returns : the CachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */
  public final Collection getPolicyCollection() throws DbsException {
    Collection collection = null;
    try{
      collection = librarySession.getPolicyCollection();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return collection ;
  }

  /**
   * Purpose  : Lookup a PublicObject by its id.
   * @param   : id - identifier for the object
   * @returns : the PublicObject
   * @throws  : DbsException - if operation fails
   */
  public DbsPublicObject getPublicObject(java.lang.Long id) throws DbsException{
    DbsPublicObject dbsPublicObject=null;
    PublicObject publicObject;
    try{
      publicObject = librarySession.getPublicObject(id);
      if(publicObject instanceof Folder){
        return new DbsFolder((Folder)publicObject);
      }else if(publicObject instanceof Document){
        return new DbsDocument((Document)publicObject);
      }else if(publicObject instanceof Family){
        return new DbsFamily((Family)publicObject);
      }else if(publicObject instanceof AccessControlList){
        return new DbsAccessControlList((AccessControlList)publicObject);
      }else{
        return new DbsPublicObject(publicObject);
      }
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Return the top-most Folder in the iFS folder hierarchy.
   * @returns : the top-most root folder
   * @throws  : DbsException - if operation fails
   */
  public DbsFolder getRootFolder() throws DbsException {
    DbsFolder dbsFolder=null;
    try{
      dbsFolder=new DbsFolder(librarySession.getRootFolder());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsFolder;
  }

  /**
   * Purpose  : Lookup a SchemaObject by its id.
   * @param   : id - identifier for the object
   * @returns : the SchemaObject
   * @throws  : DbsException - if operation fails
   */
  public DbsSchemaObject getSchemaObject(java.lang.Long id) throws DbsException{
    DbsSchemaObject dbsSo=null;
    try{
      dbsSo= new DbsSchemaObject(librarySession.getSchemaObject(id));
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsSo;
  }

  /**
   * Purpose  : Gets the specified service configuration property.
   * @param   : name - the property name
   * @returns : the property, or null if there is no such property
   * @throws  : DbsException - if operation fails
   */
  public DbsAttributeValue getServiceConfigurationProperty(java.lang.String name) 
         throws DbsException{
    DbsAttributeValue dbsAttributeValue=null;
    try{
      dbsAttributeValue=new DbsAttributeValue(
                        librarySession.getServiceConfigurationProperty(name));
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsAttributeValue;        
  }

  /**
   * Purpose  : Gets the value of the specified service configuration property, 
   *            as a string.
   * @param   : name - the property name
   * @param   : defaultValue - the default value
   * @returns : the property value
   * @throws  : DbsException - if operation fails
   */
  public java.lang.String getServiceConfigurationProperty(java.lang.String name,
                          java.lang.String defaultValue) throws DbsException{
    String configProp=null;
    try{
      configProp=librarySession.getServiceConfigurationProperty(name,defaultValue);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return configProp;
  } 

  /**
   * Purpose  : Gets the value of the specified service configuration property, 
   *            as a boolean.
   * @param   : name - the property name
   * @param   : defaultValue - the default value
   * @returns : the property value
   * @throws  : DbsException - if operation fails
   */
  public boolean getServiceConfigurationProperty(java.lang.String name,
                 boolean defaultValue) throws DbsException{
    boolean configProp;
    try{
      configProp=librarySession.getServiceConfigurationProperty(name,defaultValue);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return configProp;
  } 

  /**
   * Purpose  : Gets the value of the specified service configuration property, 
   *            as a integer.
   * @param   : name - the property name
   * @param   : defaultValue - the default value
   * @returns : the property value
   * @throws  : DbsException - if operation fails
   */
  public int getServiceConfigurationProperty(java.lang.String name,
             int defaultValue) throws DbsException{
    int configProp=0;
    try{
      configProp=librarySession.getServiceConfigurationProperty(name,defaultValue);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return configProp;
  } 

  /**
   * Purpose  : Gets the id of this session's service.
   * @throws  : DbsException - if operation fails
   */    
  public java.lang.Long getServiceId() throws DbsException{
    Long id = null;
    try{
      id=librarySession.getServiceId();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return id;
  }

  /**
   * Purpose  : Gets the name of the iFS service to which this session is 
   *            connected.
   * @returns : the service name
   * @throws  : DbsException - if operation fails
   */
  public java.lang.String getServiceName() throws DbsException{
    String serviceName=null;
    try{
      serviceName=librarySession.getServiceName();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return serviceName;
  }

  /**
   * Purpose  : Gets a reference to the SharedAccessControlList collection
   * @returns : the UncachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */
  public final DbsCollection getSharedAccessControlListCollection()
         throws DbsException{
    DbsCollection dbsAcl=null;
    try{
      dbsAcl=new DbsCollection(
                 librarySession.getSharedAccessControlListCollection());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsAcl ;
  }

  /**
   * Purpose  : Gets a reference to the SystemAccessControlList collection.
   * @returns : the CachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */
  public final DbsCollection getSystemAccessControlListCollection() 
         throws DbsException{
    DbsCollection dbsAcl=null;
    try{
      dbsAcl=new DbsCollection(
                 librarySession.getSystemAccessControlListCollection());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsAcl;
  }

  /**
   * Purpose  : Gets the DirectoryUser who is the permanent admin user.
   * @returns : the system user
   * @throws  : DbsException - if operation fails
   */
  public DbsDirectoryUser getSystemDirectoryUser() throws DbsException{
    DbsDirectoryUser dbsDirectoryUser=null;
    try{
      dbsDirectoryUser=new DbsDirectoryUser(
                           librarySession.getSystemDirectoryUser());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsDirectoryUser;
  }

  /**
   * Purpose  : Lookup a SystemObject by its id.
   * @returns : id - identifier for the object
   * @throws  : DbsException - if operation fails
   */
  public DbsSystemObject getSystemObject(java.lang.Long id) throws DbsException{
    DbsSystemObject dbsSystemObject=null;
    SystemObject systemObject=null;
    try{
      systemObject = librarySession.getSystemObject(id);
      if(systemObject instanceof Format){
        dbsSystemObject = new DbsFormat((Format)systemObject);
      }else{
        dbsSystemObject = new DbsSystemObject(systemObject);
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return dbsSystemObject;
  }

  /**
   * Purpose  : Return the user associated with the current session. If this  
   *          : session is impersonating a user that is not the same user who  
   *          : authenticated,this method returns the impersonating user.
   * @returns : the current user
   * @throws  : DbsException - if operation fails
   */
  public DbsDirectoryUser getUser() throws DbsException{
    DbsDirectoryUser directoryUser=null;
    try{
      directoryUser=new DbsDirectoryUser(librarySession.getUser());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
    return directoryUser;
  }

  /**
   * Purpose  : Grant the Administrator Mode state for the specified Directory
   *          : User.
   * @returns : user - the user to receive the admin grant
   * @throws  : DbsException - if operation fails
   */
  public void grantAdministration(DbsDirectoryUser dbsUser)throws DbsException{
    try{
      librarySession.grantAdministration(dbsUser.getDirectoryUser());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Assume the identity of another user. New objects created will be
   *          : owned by this impersonated user. 
   *          : This operation requires that the authenticated user has the 
   *          : ability to enable administration mode,but will also work if the 
   *          : user is currently in not in administration mode. In this case, 
   *          : the impersonation will also not be in administration mode. 
   *          : Also note that since admin mode is required for this operation, 
   *          : admin mode can be enabled while impersonating the new user 
   *          : (who may not normally have admin privileges).
   * @param   : dbsuser - new User to impersonate, or null to cancel a previous 
   *          : impersonation.
   * @throws  : DbsException - if operation fails
   */
  public void impersonateUser(DbsDirectoryUser dbsUser) throws DbsException{
    try{
      if(dbsUser == null){
        librarySession.impersonateUser(null);
      }else{
        librarySession.impersonateUser(dbsUser.getDirectoryUser());
      }
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Return connection state of this LibrarySession
   * @param   : the connection state; true if connected.
   * @throws  : DbsException - if operation fails
   */
  public boolean isConnected(){
    boolean isConnected;
    isConnected=librarySession.isConnected();
    return isConnected ;
  }

  /**
   * Purpose  : Return administrationMode state of this LibrarySession
   * @param   : the administrationMode ; true if administrationMode enabled.
   * @throws  : DbsException - if operation fails
   */
  public boolean isAdministrationMode(){
    boolean isAdministrationMode;
    isAdministrationMode = librarySession.isAdministrationMode();
    return isAdministrationMode;
  }

  /**
   * Purpose  : Set the administrator mode state for this session
   * @param   : mode - new state of administrator mode; 
   *            true to enable administration mode
   * @throws  : DbsException - if operation fails
   */
  public void setAdministrationMode(boolean mode) throws DbsException{
    try{
      librarySession.setAdministrationMode(mode);
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Sets the session's default ContentQuota. This ContentQuota 
   *          : object will be used for every document that this session 
   *          : creates. If the specified value is null, then the user's 
   *          : ContentQuota object will be used by default.
   * @param   : quota - the quota object to use by default
   * @throws  : DbsException - if operation fails
   */  
  public void setDefaultContentQuota(DbsContentQuota dbsQuota) 
         throws DbsException {
    try{
      librarySession.setDefaultContentQuota(dbsQuota.getContentQuota());  
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose: Invokes a custom server-side method.
   * @throws dms.beans.DbsException
   * @return the method result
   * @param payload
   * @param methodName
   */
  public Serializable invokeServerMethod(String methodName,Serializable payload)
         throws DbsException {
    try{
      return librarySession.invokeServerMethod(methodName,payload);  
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Gets a reference to the ClassObject collection.
   * @returns : the CachedSelectorCollection
   * @throws  : DbsException - if operation fails
   */  
  public final DbsCollection getClassObjectCollection() throws DbsException{
    DbsCollection dbsCollection=null;
    try{
      dbsCollection=new DbsCollection(librarySession.getClassObjectCollection());
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return dbsCollection;    
  }

 /**
  * Purpose  : Used to get the object of class LibrarySession
  * @returns : LibrarySession Object
  */
  public LibrarySession getLibrarySession(){
    return this.librarySession ;
  }
}
