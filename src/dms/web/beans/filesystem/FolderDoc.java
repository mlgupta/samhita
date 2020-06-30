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
 * $Id: FolderDoc.java,v 20040220.77 2006/06/08 12:54:31 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;

/**
 *	Purpose: To perform filesystem operations
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  14-02-2005  
 */
// dms package references
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeQualification;
import dms.beans.DbsAttributeSearchSpecification;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsFolderRestrictQualification;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsSearch;
import dms.beans.DbsSearchClassSpecification;
import dms.beans.DbsSearchClause;
import dms.beans.DbsSearchQualification;
import dms.beans.DbsSearchSortSpecification;
import dms.beans.DbsTransaction;
import dms.beans.DbsVersionDescription;
import dms.beans.DbsVersionSeries;

import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.GeneralUtil;
import dms.web.beans.utility.SearchUtil;
import dms.web.beans.wf.watch.InitiateWatch;
//Java API
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
//Struts API
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

public class FolderDoc  {

    private DbsLibrarySession dbsLibrarySession = null;     //DbsLibrarySession Object
    private Logger logger = null;
    private Locale locale = null;
    private String[] replacementValues;
    private DbsTransaction dbsTransaction = null;
    /**
     * Purpose : Contructs a FolderDoc Object for Given Librarysession
     * @param dbsLibrarySession - A Librarysession object to generate Tree
     */
    public FolderDoc(DbsLibrarySession dbsLibrarySession){
        this.dbsLibrarySession = dbsLibrarySession;
        //Initialize logger
        logger = Logger.getLogger("DbsLogger");
        //use default if locale not set
        locale = Locale.getDefault(); //
        replacementValues = new String[5];
    }
  
    /**
     * Purpose : To create collection of FolderDocListForm object
     * @return : Return a collection of FolderDocListForm object
     */    
    public ArrayList getFolderDocList(Long currentFolderId,FolderDocInfo folderDocInfo,UserPreferences userPreferences, String davPath) throws DbsException{
      //Variable Declaration
      ArrayList folderDocLists;
      DbsFolder dbsFolder;
      DbsSearch dbsSearch = null;
      List searchQualificationList = new ArrayList();
      try{
        DbsAttributeSearchSpecification dbsAttributeSearchSpecification = new DbsAttributeSearchSpecification();
        DbsSearchSortSpecification dbsSearchSortSpecification = new DbsSearchSortSpecification();
        DbsFolderRestrictQualification dbsFolderRestrictQualification = new DbsFolderRestrictQualification();
        DbsSearchClassSpecification dbsSearchClassSpecification = new DbsSearchClassSpecification();
        
        dbsFolder = (DbsFolder)(new DbsFileSystem(dbsLibrarySession).findPublicObjectById((currentFolderId)));
        dbsFolderRestrictQualification.setStartFolder(dbsFolder);
        dbsFolderRestrictQualification.setSearchClassname(DbsPublicObject.CLASS_NAME);
        dbsFolderRestrictQualification.setMultiLevel(false);
        searchQualificationList.add(dbsFolderRestrictQualification);
        
        // array of class to be searched
        String [] searchClasses = new String[] {DbsPublicObject.CLASS_NAME};
        dbsSearchClassSpecification.addSearchClasses(searchClasses);
        dbsSearchClassSpecification.addResultClass(DbsPublicObject.CLASS_NAME);
        
        // Array of classes involved in the order by clause
        String [] sortClasses = new String[] {DbsPublicObject.CLASS_NAME};
         // Array of Attribute Names to match class names.
        String [] attNames = new String[] {"NAME"};
        // Order of Sort for each sort element
        boolean [] orders = new boolean[] {true};
        // Case insensitive Sort for each sort element
        String [] caseSorts = new String[] {"nls_upper"};
        dbsSearchSortSpecification.add(sortClasses,attNames,orders,caseSorts);
        
        DbsAttributeQualification folderDocIdAttrbQual = new DbsAttributeQualification();
        String searchColumn = "ID";
        folderDocIdAttrbQual.setAttribute(searchColumn);
        folderDocIdAttrbQual.setOperatorType(DbsAttributeQualification.NOT_EQUAL);
        folderDocIdAttrbQual.setValue(DbsAttributeValue.newAttributeValue(folderDocInfo.getCurrentFolderId()));
        searchQualificationList.add(folderDocIdAttrbQual);

        //And together all the dbsSearch qualifications
        DbsSearchQualification dbsSearchQualification = null;
        Iterator iterator = searchQualificationList.iterator();
        while (iterator.hasNext()){
          DbsSearchQualification nextSearchQualification = 
                                        (DbsSearchQualification) iterator.next();
          if (dbsSearchQualification == null) {
            dbsSearchQualification = nextSearchQualification;
          }else{
            dbsSearchQualification = new DbsSearchClause(dbsSearchQualification,
                                  nextSearchQualification, DbsSearchClause.AND);
          }
        }

        dbsAttributeSearchSpecification.setSearchQualification(
                                                        dbsSearchQualification);
        dbsAttributeSearchSpecification.setSearchClassSpecification(
                                                    dbsSearchClassSpecification);
        dbsAttributeSearchSpecification.setSearchSortSpecification(
                                                    dbsSearchSortSpecification);
        
        dbsSearch = new DbsSearch(dbsLibrarySession,dbsAttributeSearchSpecification);
        folderDocLists = buildFolderDocList(dbsSearch,FolderDocInfo.SIMPLE_LISTING,
                                        folderDocInfo,userPreferences, davPath);
      }catch(DbsException dbsException){
        throw dbsException;
      }finally{
        /* it is mandatory to close the search object after it has been used */
        if( dbsSearch!= null ){
          try{
            dbsSearch.close();
            dbsSearch.dispose();
            dbsSearch = null;
          }catch(DbsException dbsEx) {
            dbsSearch = null;
          }
        }
      }
      logger.debug("folderDocLists.size() : " + folderDocLists.size());
      return folderDocLists;
    }

    public ArrayList buildFolderDocList(DbsSearch dbsSearch,byte listingType,FolderDocInfo folderDocInfo,UserPreferences userPreferences, String davPath) throws DbsException{
      ArrayList folderDocLists = new ArrayList();
      FolderDocList folderDocList;
      int folderCount = 0;
      DbsDocument dbsDocument;
      ArrayList publicObjectFoundLists = new ArrayList();
      DbsPublicObject dbsPublicObject;
      int itemCount;
      String description = null;
      try{
//            logger.debug(dbsSearch.getSQL());
        dbsSearch.open();
        itemCount = dbsSearch.getItemCount();
        for(int index = 0; index < itemCount ; index++){
          //Resolved public object returns either folder or dbsDocument
          dbsPublicObject = ((DbsPublicObject)dbsSearch.next().getLibraryObject());

          if (dbsPublicObject.getResolvedPublicObject() instanceof DbsFolder){
            publicObjectFoundLists.add(folderCount,dbsPublicObject);
            folderCount++;
          }else{
            publicObjectFoundLists.add(dbsPublicObject);
          }
        }
  
        //initialize pageCount to 1
        folderDocInfo.setPageCount(new Integer(1));
  
        itemCount = dbsSearch.getItemCount();
        int startIndex, endIndex, pageCount;
        logger.debug("itemCount : " + itemCount);
        if (itemCount > 0 ){
          //set start index and end index of the records to be displayed
          startIndex = (folderDocInfo.getPageNumber() -1) * 
                        userPreferences.getRecordsPerPage();
          logger.debug("startIndex : " + startIndex);
          endIndex = startIndex + userPreferences.getRecordsPerPage();
          logger.debug("endIndex : " + endIndex);
          if(endIndex >= itemCount){
            endIndex = itemCount;
          }
          logger.debug("endIndex : " + endIndex);
          //find page count 
          pageCount = (int)StrictMath.ceil((double)itemCount / 
                                            userPreferences.getRecordsPerPage());
          if(pageCount > 0){
            folderDocInfo.setPageCount(new Integer(pageCount));
          }

          for(int intCounter = startIndex; intCounter < endIndex; intCounter++){
            dbsPublicObject = (DbsPublicObject)publicObjectFoundLists.get(intCounter);
            folderDocList = new FolderDocList();
            Long id = dbsPublicObject.getId();
//                    logger.debug("id : " + id);
            folderDocList.setId(id);
            description = dbsPublicObject.getDescription(); 
            if( (description != null) && (description.trim().length() !=0) && 
                (description.trim().length() >= 29) ){
              folderDocList.setTrimDescription(description.substring(0,25)+"...");  
            }else{
              folderDocList.setTrimDescription(description);
            }
            folderDocList.setDescription(description);
            if(listingType == FolderDocInfo.SIMPLE_LISTING){
              String name = dbsPublicObject.getName();
//                        logger.debug("name : " + name);
              folderDocList.setName(name);
              if( name.length() >= 29 ){
                folderDocList.setPathOrTrimmedName(name.substring(0,25)+"...");
              }else{
                folderDocList.setPathOrTrimmedName(name);
              }
            }else{
              String fullPath = dbsPublicObject.getAnyFolderPath();
//                        logger.debug("fullPath : " + fullPath);
              if( fullPath.length() >= 29 ){
                folderDocList.setPathOrTrimmedName(fullPath.substring(0,25)+"...");
              }else{
                folderDocList.setPathOrTrimmedName(fullPath);
              }
              folderDocList.setName(fullPath);
            }
            String modifiedDate = GeneralUtil.getDateForDisplay(dbsPublicObject.getLastModifyDate(),locale);
//                    logger.debug("modifiedDate : " + modifiedDate);
            folderDocList.setModifiedDate(modifiedDate);
            String className = dbsPublicObject.getClassname();
//                    logger.debug("className : " + className);
            folderDocList.setType(className);
            folderDocList.setClassName(className);
            if(dbsPublicObject instanceof DbsFamily){
              DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
              boolean checkedOut = dbsFileSystem.isCheckedOut(dbsPublicObject);
//                        logger.debug("checkedOut : " + checkedOut);
              folderDocList.setCheckedOut(checkedOut);
            }

            //Resolved public object returns either folder or dbsDocument
            String docPath = null; 
            docPath = dbsPublicObject.getAnyFolderPath();
            dbsPublicObject = dbsPublicObject.getResolvedPublicObject();

            if (dbsPublicObject instanceof DbsFolder){
              folderCount++;
              DbsFolder dbsFolder=(DbsFolder)dbsPublicObject;
              int items= dbsFolder.getItemCount();
              folderDocList.setItem(items);
//                        logger.debug("folderCount : " + folderCount);
            }else if(dbsPublicObject instanceof DbsDocument){
              dbsDocument = (DbsDocument)dbsPublicObject;
              DbsFormat dbsFormat = dbsDocument.getFormat();

              if(dbsFormat != null){
                String name = dbsPublicObject.getName();
                String mimeType = dbsFormat.getMimeType();
                folderDocList.setType(mimeType);
                if((mimeType.equalsIgnoreCase("application/msword")) || 
                   (mimeType.equalsIgnoreCase("application/vnd.ms-excel")) || 
                   (mimeType.equalsIgnoreCase("application/vnd.ms-powerpoint")) ||
                   (mimeType.equalsIgnoreCase("text/plain")) || 
                   (mimeType.equalsIgnoreCase("application/rtf"))){
                
                  folderDocList.setDavPath(davPath + docPath);
                }else{
                  folderDocList.setDavPath(null);
                }
                logger.debug("mimeType : " + folderDocList.getType());
                logger.debug("davpath : " + folderDocList.getDavPath());
              }
              boolean encrypted = false;
              DbsAttributeValue dbsAttributeValue = dbsDocument.getAttribute(
                                                        DbsDocument.ENCRYPTED);
              if(dbsAttributeValue.isNullValue()){
                encrypted = false;
              }else{
                encrypted = (dbsDocument.getAttribute(DbsDocument.ENCRYPTED)).getBoolean(dbsLibrarySession);
              }
              folderDocList.setEncripted(encrypted);
              folderDocList.setSize(GeneralUtil.getDocSizeForDisplay(((DbsDocument)dbsPublicObject).getContentSize(),locale));
            }else{
                
            }
            folderDocLists.add(folderDocList);
          }
        }
      }catch(DbsException dex){
        logger.debug("Exception stack trace: ");
        dex.printStackTrace();        
        logger.error(dex.getMessage());
        throw dex;
      }finally{
        /* it is mandatory to close the search object after it has been used */
        if( dbsSearch!= null ){
          try{
            dbsSearch.close();
            dbsSearch.dispose();
            dbsSearch = null;
          }catch(DbsException dbsEx) {
            dbsSearch = null;
          }
        }
      }
      return folderDocLists;
    }
    /**
     * Purpose : To create collection of FolderDocListForm object
     * @return Return a collection of FolderDocListForm object
     */    
    //it is being used basically for copy and move operation
    public ArrayList getFolderDocList(Long[] ids) throws DbsException{
        ArrayList folderDocLists = new ArrayList();
        try{    
            int intCounter;        
            DbsPublicObject dbsPublicObject = null;
            FolderDocList folderDocList = null;
            
            for(intCounter = 0; intCounter < ids.length; intCounter++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(ids[intCounter]);
                folderDocList = new FolderDocList();
                folderDocList.setId(dbsPublicObject.getId());                
                
                folderDocList.setName(dbsPublicObject.getName());
                folderDocList.setDescription(dbsPublicObject.getDescription());
                folderDocList.setModifiedDate(GeneralUtil.getDateForDisplay(dbsPublicObject.getLastModifyDate(),locale));
                if (dbsPublicObject.getClassname().equals("DOCUMENT")){                
                    folderDocList.setType(((DbsDocument)dbsPublicObject).getFormat().getMimeType());
                    folderDocList.setSize(GeneralUtil.getDocSizeForDisplay(((DbsDocument)dbsPublicObject).getContentSize(),locale));
                }
                folderDocLists.add(folderDocList);                
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }
        return folderDocLists;
    }
  
    public ArrayList getDocumentList(Long [] selectedDocIds,byte listingType,FolderDocInfo folderDocInfo,UserPreferences userPreferences, String davPath) throws DbsException{
      ArrayList folderDocLists = new ArrayList();
      FolderDocList folderDocList;
      int folderCount = 0;
      DbsDocument dbsDocument;
      ArrayList publicObjectFoundLists = new ArrayList();
      DbsPublicObject dbsPublicObject;
      int itemCount;
      String description = null;
      try{
        itemCount = ( selectedDocIds == null )?0:selectedDocIds.length;
        for(int index = 0; index < itemCount ; index++){
          //Resolved public object returns either folder or dbsDocument
          dbsPublicObject = (dbsLibrarySession.getPublicObject(selectedDocIds[index]));
          publicObjectFoundLists.add(dbsPublicObject);
        }
  
        //initialize pageCount to 1
        folderDocInfo.setPageCount(new Integer(1));
  
        int startIndex, endIndex, pageCount;
        logger.debug("itemCount : " + itemCount);
        if (itemCount > 0 ){
          //set start index and end index of the records to be displayed
          startIndex = 0;
          logger.debug("startIndex : " + startIndex);
          endIndex = itemCount;
          logger.debug("endIndex : " + endIndex);
          //find page count 
          pageCount = (int)StrictMath.ceil((double)itemCount / 
                                            userPreferences.getRecordsPerPage());
          if(pageCount > 0){
            folderDocInfo.setPageCount(new Integer(pageCount));
          }

          for(int intCounter = startIndex; intCounter < endIndex; intCounter++){
            dbsPublicObject = (DbsPublicObject)publicObjectFoundLists.get(intCounter);
            folderDocList = new FolderDocList();
            Long id = dbsPublicObject.getId();
//                    logger.debug("id : " + id);
            folderDocList.setId(id);
            description = dbsPublicObject.getDescription(); 
            if( (description != null) && (description.trim().length() !=0) && 
                (description.trim().length() >= 29) ){
              folderDocList.setTrimDescription(description.substring(0,25)+"...");  
            }else{
              folderDocList.setTrimDescription(description);
            }
            folderDocList.setDescription(description);
            if(listingType == FolderDocInfo.SIMPLE_LISTING){
              String name = dbsPublicObject.getName();
//                        logger.debug("name : " + name);
              folderDocList.setName(name);
              if( name.length() >= 29 ){
                folderDocList.setPathOrTrimmedName(name.substring(0,25)+"...");
              }else{
                folderDocList.setPathOrTrimmedName(name);
              }
            }else{
              String fullPath = dbsPublicObject.getAnyFolderPath();
//                        logger.debug("fullPath : " + fullPath);
              if( fullPath.length() >= 29 ){
                folderDocList.setPathOrTrimmedName(fullPath.substring(0,25)+"...");
              }else{
                folderDocList.setPathOrTrimmedName(fullPath);
              }
              folderDocList.setName(fullPath);
            }
            String modifiedDate = GeneralUtil.getDateForDisplay(dbsPublicObject.getLastModifyDate(),locale);
//                    logger.debug("modifiedDate : " + modifiedDate);
            folderDocList.setModifiedDate(modifiedDate);
            String className = dbsPublicObject.getClassname();
//                    logger.debug("className : " + className);
            folderDocList.setType(className);
            folderDocList.setClassName(className);
            if(dbsPublicObject instanceof DbsFamily){
              DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
              boolean checkedOut = dbsFileSystem.isCheckedOut(dbsPublicObject);
//                        logger.debug("checkedOut : " + checkedOut);
              folderDocList.setCheckedOut(checkedOut);
            }

            //Resolved public object returns either folder or dbsDocument
            String docPath = null; 
            docPath = dbsPublicObject.getAnyFolderPath();
            dbsPublicObject = dbsPublicObject.getResolvedPublicObject();

            dbsDocument = (DbsDocument)dbsPublicObject;
            DbsFormat dbsFormat = dbsDocument.getFormat();

            if(dbsFormat != null){
              String name = dbsPublicObject.getName();
              String mimeType = dbsFormat.getMimeType();
              folderDocList.setType(mimeType);
              if((mimeType.equalsIgnoreCase("application/msword")) || 
                 (mimeType.equalsIgnoreCase("application/vnd.ms-excel")) || 
                 (mimeType.equalsIgnoreCase("application/vnd.ms-powerpoint")) ||
                 (mimeType.equalsIgnoreCase("text/plain")) || 
                 (mimeType.equalsIgnoreCase("application/rtf"))){
              
                folderDocList.setDavPath(davPath + docPath);
              }else{
                folderDocList.setDavPath(null);
              }
              logger.debug("mimeType : " + folderDocList.getType());
              logger.debug("davpath : " + folderDocList.getDavPath());
            }
            boolean encrypted = false;
            DbsAttributeValue dbsAttributeValue = dbsDocument.getAttribute(
                                                      DbsDocument.ENCRYPTED);
            if(dbsAttributeValue.isNullValue()){
              encrypted = false;
            }else{
              encrypted = (dbsDocument.getAttribute(DbsDocument.ENCRYPTED)).getBoolean(dbsLibrarySession);
            }
            folderDocList.setEncripted(encrypted);
            folderDocList.setSize(GeneralUtil.getDocSizeForDisplay(((DbsDocument)dbsPublicObject).getContentSize(),locale));
            folderDocLists.add(folderDocList);
          }
        }
      }catch(DbsException dex){
        logger.debug("Exception stack trace: ");
        dex.printStackTrace();        
        logger.error(dex.getMessage());
        throw dex;
      }
      return folderDocLists;
    }
    /**
     * Purpose : To delete folder and dbsDocument
     * @param  : array of Long id
     * @return : void
     */    

    public void deleteFolderDoc(Long id[], Treeview treeview, boolean recursively,String relativePath,String userName) throws DbsException,Exception{
      DbsFileSystem dbsFileSystem = null;
      DbsPublicObject dbsPublicObject = null;
      Long parentId = null;
      String folderDocName = "";
      String className;
      try{
        dbsTransaction = dbsLibrarySession.beginTransaction();
        dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
        for(int counter = 0 ; counter < id.length ; counter ++){
          dbsPublicObject = dbsFileSystem.findPublicObjectById(id[counter]);
          className = dbsPublicObject.getClassname();
          folderDocName = dbsPublicObject.getName();
          logger.debug("DbsFolder Or DbsDocument Name : " + dbsPublicObject.getAnyFolderPath() + "/" + folderDocName);
          if (className.equals(DbsFolder.CLASS_NAME)){
            parentId = dbsPublicObject.getFolderReferences()[0].getId();
            dbsFileSystem.delete((DbsFolder)dbsPublicObject,recursively);
            treeview.ifFolderDeleted(id[counter],parentId);
          }else{
          if(className.equals(DbsDocument.CLASS_NAME)){
            // code for wf submission goes here.
            if( relativePath!=null && userName!=null){
              String actionForWatch = new String();
              Long poId = dbsPublicObject.getId();
              actionForWatch="You are receiving this mail because you had applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsPublicObject.getName()+" has been deleted";
              InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,
                                      actionForWatch,poId);
              iniWatch.startWatchProcess();
              actionForWatch = null;
            }
            dbsFileSystem.delete(dbsPublicObject);
          }
          if(className.equals(DbsFamily.CLASS_NAME)){
            // code for wf submission goes here.
            if( relativePath!=null && userName!=null){
              String actionForWatch = new String();
              Long poId = dbsPublicObject.getId();
              actionForWatch="You are receiving this mail because you had applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsPublicObject.getName()+" has been deleted";
              InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,
                                      actionForWatch,poId);
              iniWatch.startWatchProcess();
              actionForWatch = null;
            }
            dbsFileSystem.delete(dbsPublicObject);
          }
        }
        }
        dbsLibrarySession.completeTransaction(dbsTransaction);
        dbsTransaction = null;
      }catch(DbsException dbsException){
        if(dbsException.getErrorCode() == 30033){
          replacementValues[0] = folderDocName;
          dbsException.setMessageKey("errors.30033.folderdoc.delete.denied",replacementValues);
        }
        throw dbsException;
      }finally{
        if(dbsTransaction != null){
          dbsLibrarySession.abortTransaction(dbsTransaction);
        }
      }
    }
  
    /**
     * Purpose  : to rename folder or dbsDocument and log event in dbsDocument attribute
     *            namely, "AUDIT_LOG"
     * @param   : id - id of the DbsPublicObject to rename
     * @param   : name - new name of the DbsPublicObject to rename
     * @param   : treeview - to refresh the treeview  
     * @returns : void
     * @throws  : DbsException - if operation fails
     */
    public void renameFolderDoc(Long id[],String[] name ,String[] descs, Treeview treeview,String relativePath,String userName) throws DbsException, Exception, ExceptionBean {
        DbsFileSystem dbsFileSystem = null;
        DbsPublicObject dbsPublicObject = null;
        DbsFolder dbsFolder = null;
        Long parentId = null;
        String oldName = null;
        String newName = null;
        String newDesc = null;
        /*new boolean variables added to check whether doc was renamed or doc desc was altered */
        boolean nameChanged = false;
        boolean descChanged = false;
        try{
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            for(int counter = 0 ; counter < id.length ; counter ++){
                dbsPublicObject = dbsFileSystem.findPublicObjectById(id[counter]);
                logger.debug("dbsPublicObjectId: "+dbsPublicObject.getId());
                logger.debug("id["+counter+"] "+id[counter]);
                oldName = dbsPublicObject.getName();
                newName = name[counter];
                newDesc = descs[counter];
                
                if(!oldName.equals(newName))
                  nameChanged = true;
                
                try{
                  if(!dbsPublicObject.getDescription().equals(newDesc)){
                    descChanged = true;
                  }
                }catch(DbsException dex){
                  logger.debug("DbsException");
                  logger.debug("descChanged: "+descChanged);
                }catch(Exception ex){
                  logger.debug("Exception occurred: "+ex.getMessage());
                  if(newDesc.trim().length()!= 0)
                    descChanged = true;
                }
                if(newName.trim().length() != 0){
                  dbsPublicObject.setName(newName);
                  dbsPublicObject.setDescription(newDesc);
                  if( dbsPublicObject instanceof DbsFamily && nameChanged){
                    renameVersions(newName,dbsPublicObject.getId());                    
                  }
                  if (dbsPublicObject instanceof DbsFolder){
                      logger.debug("DbsFolder :: Old Name : " + oldName + " New Name : " + newName );
                      dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(id[counter]);
                      treeview.ifFolderRenamed(id[counter]);
                  }else{
                      logger.debug("DbsDocument :: Old Name : " + oldName + " New Name : " + newName );
                      /*logic for logging suitable event in doc attribute "AUDIT_LOG" */
                      if((!nameChanged) && (!descChanged)){
                        logger.debug("None of the properties were changed");
                      }else{
                        
                        String action = new String();
                        String actionForWatch = new String();
                        if(nameChanged && !descChanged){
                          action = "DocRenamed to: "+newName;
                          actionForWatch="You are receiving this mail because you have applied watch on "+oldName+" and a new operation has been performed on it.\n\t\tOperation Performed : "+oldName+" Renamed to : "+newName;                            
                        }
                        if(!nameChanged && descChanged){
                          action="Description changed to: "+newDesc;
                          actionForWatch="You are receiving this mail you have applied watch on "+oldName+" and a new operation has been performed on it.\n\t\tOperation Performed :  Description changed to: "+newDesc;
                        }
                        if(nameChanged && descChanged){
                          action = "DocRenamed to: "+newName+", Description changed to: "+newDesc;
                          actionForWatch="You are receiving this mail you have applied watch on "+oldName+" and a new operation has been performed on it.\n\t\tOperation Performed : "+oldName+" Renamed to: "+newName+", Description changed to: "+newDesc;
                        }
                        logger.debug("printing action now...");
                        logger.debug("action: "+action);

                        // code for wf submission goes here
                        if( relativePath!=null && userName!=null){
                          InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsPublicObject.getId());
                          iniWatch.startWatchProcess();
                        }

                        DocEventLogBean logBean = new DocEventLogBean();
                        logBean.logEvent(dbsLibrarySession,id[counter],action);
                        action = null;
                        actionForWatch = null;
                      }
                    }
                }else{
                    ExceptionBean exb = new ExceptionBean();
                    exb.setMessage("Folder Name Can't be blank");
                    exb.setMessageKey("errors.foldername.required");
                    throw exb;
                }
            }
        }catch(DbsException dbsException){
            if(dbsException.getErrorCode() == 30041){
                replacementValues[0] = oldName;
                dbsException.setMessageKey("errors.30041.folderdoc.rename.denied",replacementValues);
            }
            throw dbsException;
        }
    }

    /**
     * Purpose  : to rename folder or dbsDocument and log event in dbsDocument attribute
     *            namely, "AUDIT_LOG"
     * @param   : id - id of the DbsPublicObject to rename
     * @param   : name - new name of the DbsPublicObject to rename
     * @param   : treeview - to refresh the treeview  
     * @returns : void
     * @throws  : DbsException - if operation fails
     */

    public void renameFolderDoc(Long id,String name ,Treeview treeview,String relativePath,String userName) throws DbsException, Exception{
        DbsFileSystem dbsFileSystem = null;
        DbsPublicObject dbsPublicObject = null;
        DbsFolder dbsFolder = null;
        Long parentId = null;
        String oldName = null;
        String newName = null;
        try{
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            dbsPublicObject = dbsFileSystem.findPublicObjectById(id);
            oldName = dbsPublicObject.getName();
            newName = name;
            dbsPublicObject.setName(newName);
            if (dbsPublicObject instanceof DbsFolder){
              logger.debug("DbsFolder :: Old Name : " + oldName + " New Name : " + newName );
              dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(id);
              treeview.ifFolderRenamed(id);
            }else{
              if( relativePath!=null && userName!=null ){
                // code for wf submission goes here
                String actionForWatch = new String();
                actionForWatch="You are receiving this mail because you have applied watch on "+oldName+" and a new operation has been performed on it.\n\t\tOperation Performed : "+oldName+" Renamed to : "+newName;
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsPublicObject.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;
              }  
                /*logic for logging rename event in doc attribute "AUDIT_LOG" */
                logger.debug("DbsDocument :: Old Name : " + oldName + " New Name : " + newName );
                DocEventLogBean logBean = new DocEventLogBean();
                logBean.logEvent(dbsLibrarySession,id,"DocRenamed to: "+newName);
            }
        }catch(DbsException dbsException){
//            DbsException dex = new DbsException(ex);
            if(dbsException.getErrorCode() == 30041){
                replacementValues[0] = oldName;
                dbsException.setMessageKey("errors.30041.folderdoc.rename.denied",replacementValues);
            }
            throw dbsException;
        }
    }

    /**
     * Purpose  : to create new folder 
     * @param   : folderName - name of the folder to create
     * @param   : currentFolderId - id if the folder under which new folder is to be created 
     * @param   : treeview - to refresh the treeview  
     * @returns : void
     * @throws  : DbsException - if operation fails
     */
    public void newFolder(String folderName,String folderDesc, Long currentFolderId,Treeview treeview ) throws DbsException, Exception{
        DbsFileSystem dbsFileSystem = null;
        DbsFolder dbsFolder = null;
        try{
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            dbsFolder = (DbsFolder)dbsFileSystem.findPublicObjectById(currentFolderId);
            dbsFolder = dbsFileSystem.createFolder(folderName,dbsFolder,true,null);
            dbsFolder.setDescription(folderDesc);
            treeview.ifFolderAdded(dbsFolder.getId(),currentFolderId);
        }catch(DbsException dbsException){
//            DbsException dex = new DbsException(ex);
            if(dbsException.getErrorCode() == 30002){
                replacementValues[0] = folderName;
                dbsException.setMessageKey("errors.30002.folderdoc.newFolder.denied",replacementValues);
            }else{
                if(dbsException.containsErrorCode(68005)){
                    replacementValues[0] = folderName;
                    dbsException.setMessageKey("errors.68005.folderdoc.folderexist",replacementValues);
                }
            }
            throw dbsException;
        }
    }

    /**
     * Purpose  : to upload dbsDocument and log event in dbsDocument attribute
     *            namely, "AUDIT_LOG"
     * @param   : formFile - FormFile object which contains the uploaded files
     * @param   : currentFolderId - id if the folder under which new dbsDocument will be uploaded
     * @returns : void
     * @throws  : DbsException - if operation fails
     */
    public void uploadDocument(FormFile formFile ,String currentFolderPath, String docDesc) throws DbsException , IOException{
        DbsFileSystem dbsFileSystem = null;
        DbsDocument dbsDocument = null;
        try{
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            if(!formFile.getFileName().equals("")){
                dbsDocument = dbsFileSystem.putDocument(formFile.getFileName(),formFile.getInputStream(),currentFolderPath,null);
                logger.debug("File Input Stream closed");
                //formFile.getInputStream().close();
                dbsDocument.setDescription(docDesc);
                logger.debug("DbsDocument : " + dbsDocument.getName() );
                logger.debug("DbsDocument format is: "+ dbsDocument.getFormat().getName());
                /* logic for logging upload event in doc attribute "AUDIT_LOG" */
                DocEventLogBean logBean = new DocEventLogBean();
                logBean.logEvent(dbsLibrarySession,dbsDocument.getId(),"Doc Uploaded ");
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }catch(IOException io){
            throw io;
        }
    }


    /**
     * Purpose  : to copy folder or dbsDocument and log event in dbsDocument attribute
     *            namely, "AUDIT_LOG"
     * @param   : targetFolderId - target folder id
     * @param   : folderDocIds - list of folder and dbsDocument
     * @param   : overwrite -  whether to overwrite the folder and dbsDocument 
     *            for detail refer cmsdk IfsFilesystem.copy() function
     * @param   : treeview - to refresh the treeview  
     * @returns : void
     * @throws  : DbsException - if operation fails
     */

    public void copy(Long targetFolderId, Long[] folderDocIds, Boolean overwrite,Treeview treeview,String relativePath,String userName ) throws DbsException , IOException,Exception{
        DbsFileSystem dbsFileSystem = null;
        DbsDocument dbsDocument = null;
        DbsPublicObject dbsPublicObject = null;
        DbsPublicObject newPublicObject = null;
        DbsFolder targetFolder = null;
        boolean docExists = true; /* boolean variable added for checking existence of dbsDocument*/
        try{
            logger.debug("Entering FolderDoc.copy() with overwrite set to: "+overwrite.booleanValue());
            dbsTransaction = dbsLibrarySession.beginTransaction();
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            targetFolder = (DbsFolder)dbsLibrarySession.getPublicObject(targetFolderId);
            
            for (int counter = 0; counter < folderDocIds.length; counter++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[counter]);
              try{ /*logic for checking existence of dbsDocument by name in target folder */
                if(dbsPublicObject.getResolvedPublicObject() instanceof DbsDocument){
                    DbsPublicObject[] chkDoc4Existence =dbsFileSystem.searchByName(dbsPublicObject.getName(),targetFolder,0);
                  if(chkDoc4Existence==null){
                    docExists = false;
                  }else{
                    /* if docExists = true, check if the docname is the name of a folder within target folder ? */
                    for(int i=0;i<chkDoc4Existence.length;i++){
                      logger.debug("chkDoc4Existence: "+chkDoc4Existence[i].getClassname());
                      if(chkDoc4Existence[i] instanceof DbsDocument)
                        break;
                    }
                  }
                  
                }
              }catch(DbsException dex){
                logger.error("DbsException: "+dex.getMessage());
              }catch(Exception ex){
                logger.error("Exception: "+ex.getMessage());                
              }
                /* perform doc copy operation */
                newPublicObject=dbsFileSystem.copy(dbsPublicObject,targetFolder,null,overwrite.booleanValue());
                logger.debug("newPublicObject: "+newPublicObject.toString());
                logger.debug("newPublicObject Id: "+newPublicObject.getId());
                logger.debug("dbsPublicObject Id: "+dbsPublicObject.getId());

                /* log event in "AUDIT_LOG" attribute */
                if(newPublicObject.getResolvedPublicObject() instanceof DbsDocument){
                    logger.debug("DbsDocument : " + newPublicObject.getName());  
                    
                    DocEventLogBean logBean = new DocEventLogBean();
                    String actionForWatch = new String();
                    if(overwrite.booleanValue() && docExists){                      
                      logBean.logEvent(dbsLibrarySession,dbsPublicObject.getId(),"Contents Overwritten");
                      actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Contents Overwritten";
                    }else{
                      int strLen = newPublicObject.getAnyFolderPath().length();
                      int objLen = newPublicObject.getName().length();
                      logBean.logEvent(dbsLibrarySession,dbsPublicObject.getId(),"Copy Generated At: "+newPublicObject.getAnyFolderPath().substring(0,strLen-objLen));
                      logBean.logEvent(dbsLibrarySession,newPublicObject.getId(),"Generated From: "+dbsPublicObject.getAnyFolderPath());
                      actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Copy Generated At: "+newPublicObject.getAnyFolderPath().substring(0,strLen-objLen);
                    }
                    // code for wf submission goes here
                    if(relativePath!=null && userName!=null) {
                      InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsPublicObject.getId());
                      iniWatch.startWatchProcess();
                      actionForWatch = null;
                    }
                }
                
                if(newPublicObject.getResolvedPublicObject() instanceof DbsFolder){    
                    logger.debug("DbsFolder : " + newPublicObject.getName() );
                    treeview.ifFolderAdded(newPublicObject.getId(),targetFolderId);
                }
            }
            dbsLibrarySession.completeTransaction(dbsTransaction);
            dbsTransaction = null;
        }catch(DbsException dbsException){
            logger.debug("DbsException: "+dbsException.getMessage());
            throw dbsException;
        }finally{
            if(dbsTransaction != null){
                logger.debug("Transaction aborted... in finally clause");
                dbsLibrarySession.abortTransaction(dbsTransaction);
            }
        }
    }

    /**
     * Purpose  : to copy folder or dbsDocument
     * @param   : dbsPublicObject - PublicObject to copy
     * @param   : targetFolder - target folder
     * @param   : dbsAccessControlList - access control list to be aplied on the public object
     * @param   : overwrite -  whether to overwrite the public object
     * @returns : the public object created
     * @throws  : DbsException - if operation fails
     */

/*
  public DbsPublicObject copyDocument(DbsPublicObject dbsPublicObject,DbsFolder targetFolder,DbsAccessControlList dbsAccessControlList,boolean overwrite) throws DbsException , IOException,Exception{
    
  }
*/  
    /**
     * Purpose  : to move folder or dbsDocument and log event in dbsDocument attribute
     *            namely, "AUDIT_LOG"
     * @param   : targetFolderId - target folder id
     * @param   : folderDocIds - list of folder and dbsDocument
     * @param   : overwrite -  whether to overwrite the folder and dbsDocument 
     *            for detail refer cmsdk IfsFilesystem.move() function
     * @param   : treeview - to refresh the treeview  
     * @returns : void
     * @throws  : DbsException - if operation fails
     */

    public void move(Long targetFolderId, Long[] folderDocIds, Boolean overwrite,Treeview treeview ,String relativePath,String userName) throws DbsException , IOException,Exception{
        DbsFileSystem dbsFileSystem = null;
        DbsPublicObject dbsPublicObject = null;
        DbsPublicObject newPublicObject = null;
        DbsFolder targetFolder = null;
        DbsFolder sourceFolder = null;
        Long sourceFolderId = null;
        try{
            dbsTransaction = dbsLibrarySession.beginTransaction();
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            targetFolder = (DbsFolder)dbsLibrarySession.getPublicObject(targetFolderId);
            sourceFolder = (DbsFolder)dbsLibrarySession.getPublicObject(folderDocIds[0]).getFolderReferences(0);
            sourceFolderId = sourceFolder.getId();
            for (int counter = 0; counter < folderDocIds.length; counter++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[counter]);
                newPublicObject=dbsFileSystem.move(sourceFolder, targetFolder, dbsPublicObject,null,null,overwrite.booleanValue());
                if (newPublicObject.getResolvedPublicObject() instanceof DbsFolder){
                    logger.debug("DbsFolder : " + newPublicObject.getName() );
                    treeview.ifFolderDeleted(folderDocIds[counter],sourceFolderId);
                    treeview.ifFolderAdded(newPublicObject.getId(),targetFolderId);
                }else{
                    if (newPublicObject.getResolvedPublicObject() instanceof DbsDocument){
                        logger.debug("DbsDocument : " + newPublicObject.getName() );
                        logger.debug("dbsPublicObject Path: "+dbsPublicObject.getResolvedPublicObject().getClass());
                        String actionForWatch = new String();
                        
                        /* log event in "AUDIT_LOG" attribute */
                        DocEventLogBean logBean = new DocEventLogBean();
                        if(targetFolderId == sourceFolder.getId()){
                          logBean.logEvent(dbsLibrarySession,newPublicObject.getId(),"Contents Overwritten");
                          actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Contents Overwritten";                          
                        }else{
                          int strLen = dbsPublicObject.getAnyFolderPath().length();
                          int objLen = newPublicObject.getName().length();       
                          logBean.logEvent(dbsLibrarySession,newPublicObject.getId(),"Doc Moved to:"+dbsPublicObject.getAnyFolderPath().substring(0,strLen-objLen));
                          actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Moved to: "+dbsPublicObject.getAnyFolderPath().substring(0,strLen-objLen);
                        }  
                        // code for wf submission goes here
                        if(relativePath!=null && userName!=null) {
                          InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsPublicObject.getId());
                          iniWatch.startWatchProcess();
                          actionForWatch = null;
                        }
                        
                    }
                }
            }
            dbsLibrarySession.completeTransaction(dbsTransaction);
            dbsTransaction = null;
        }catch(DbsException dbsException){
            throw dbsException;
        }finally{
            if(dbsTransaction!=null){
                dbsLibrarySession.abortTransaction(dbsTransaction);
            }
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale newLocale) {
        locale = newLocale;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger newLogger) {
        logger = newLogger;
    }

    /**
     * Purpose  : to apply dbsAccessControlList on folder and dbsDocument and 
     *            log event in dbsDocument attribute namely, "AUDIT_LOG"
     * @param   : aclId - dbsAccessControlList Id to be applied
     * @param   : recursively - whether to apply recursively
     * @returns : void
     * @throws  : DbsException - if operation fails
     */

    public void applyAcl(Long[] folderDocIds,Long aclId,boolean recursively,String relativePath,String userName) throws DbsException , IOException{
        DbsAccessControlList  newAccessControlList = null;
        String newAclName = null;
        String oldAclName = null;
        DbsPublicObject dbsPublicObject = null;
        DbsPublicObject[] items = null;
        DbsFolder dbsFolder;
        SearchUtil searchUtil = null;
        
        try{
            newAccessControlList = (DbsAccessControlList)dbsLibrarySession.getPublicObject(aclId);        
            newAclName = newAccessControlList.getName();
            logger.debug("New ACL Name : " + newAclName);
            
            for(int index = 0; index < folderDocIds.length ; index++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[index]);
                logger.debug("DbsFolder Or Doc Name : " + dbsPublicObject.getName());
                oldAclName = dbsPublicObject.getAcl().getName();
                dbsPublicObject.setAcl(newAccessControlList);
                
                if(!oldAclName.equals(newAclName)){
                  if(dbsPublicObject instanceof DbsDocument){
                  // code for wf submission goes here
                  if( relativePath!=null && userName!=null ){
                    String actionForWatch ="You are receiving this mail because you have applied watch on "+dbsPublicObject.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : ACL changed from "+oldAclName+" to "+newAclName;
                    InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsPublicObject.getId());
                    iniWatch.startWatchProcess();
                    actionForWatch = null;
                  }
                  }
                  /* log event in "AUDIT_LOG" attribute */
                  DocEventLogBean logBean = new DocEventLogBean();
                  logBean.logEvent(dbsLibrarySession,folderDocIds[index],"ACL Changed :"+dbsPublicObject.getAcl().getName());                    
                }
                if(dbsPublicObject instanceof DbsFolder){
                    if (recursively == true){
                        changeFolderAclRecursively((DbsFolder)dbsPublicObject,newAccessControlList,relativePath,userName);
                    }
                }
            }
        }catch(DbsException dbsException){
            if(dbsException.getErrorCode() == 30041){
                replacementValues[0] = dbsPublicObject.getName();
                dbsException.setMessageKey("errors.30041.folderdoc.insufficient.access",replacementValues);
            }        
            throw dbsException;
        }
    }

    //to change dbsAccessControlList of a folder recursively
    private void changeFolderAclRecursively(DbsFolder top, DbsAccessControlList dbsAccessControlList,String relativePath,String userName) throws DbsException {
        // change the ACL of the specified folder,
        // and set all of the items in the folder to that same ACL
        top.setAcl(dbsAccessControlList);
        DbsPublicObject[] items = top.getItems();
        int length = (items == null) ? 0 : items.length;
        for (int i = 0; i < length; i++){
            logger.debug("Public Object Name : " + items[i].getName());
            // if the item is a folder, call this same method recursively
            if (items[i] instanceof DbsFolder)	{
                DbsFolder f = (DbsFolder)items[i];
                changeFolderAclRecursively(f, dbsAccessControlList,relativePath,userName);
            }else{
            
                // simply change the item's ACL
                String oldAclName = items[i].getAcl().getName();
                items[i].setAcl(dbsAccessControlList);
                if(!oldAclName.equals(dbsAccessControlList.getName())){
                  if(items[i] instanceof DbsDocument){
                  // code for wf submission goes here
                  if( relativePath!=null && userName!=null ){
                    String actionForWatch ="You are receiving this mail because you have applied watch on "+items[i].getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : ACL changed from "+oldAclName+" to "+items[i].getAcl().getName();
                    InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,items[i].getId());
                    iniWatch.startWatchProcess();
                    actionForWatch = null;
                  }
                  /* log event in "AUDIT_LOG" attribute */
                    DocEventLogBean logBean = new DocEventLogBean();
                    logBean.logEvent(dbsLibrarySession,items[i].getId(),"ACL Changed :"+items[i].getAcl().getName());
                  }
               }
            }
        }
    }

    //Find size foldes and documents for a given public object id
    public TotalSizeFoldersDocs findTotalSizeFoldersDocs(Long publicObjectId) throws DbsException {
        // change the ACL of the specified folder,
        // and set all of the items in the folder to that same ACL
        TotalSizeFoldersDocs total = new TotalSizeFoldersDocs();
        DbsPublicObject dbsPublicObject;
        DbsDocument dbsDocument;
        try{        
            dbsPublicObject = dbsLibrarySession.getPublicObject(publicObjectId);
            
            if(dbsPublicObject.getResolvedPublicObject() instanceof DbsDocument){
                dbsDocument = (DbsDocument)dbsPublicObject.getResolvedPublicObject();
                total.setSize(dbsDocument.getContentSize());
                total.setDocumentCount(1);
                total.setFolderDocCount(1);
            }else{
                if(dbsPublicObject instanceof DbsFolder){            
                    total.setFolderCount(1);
                    total.setFolderDocCount(1);
            
                    DbsFolder topFolder = (DbsFolder)dbsPublicObject;
                    DbsPublicObject[] itemsInTheFolder = topFolder.getItems();
                    if(itemsInTheFolder != null ){
                        for (int index = 0; index < itemsInTheFolder.length; index++){
                            TotalSizeFoldersDocs tempTotal = findTotalSizeFoldersDocs(itemsInTheFolder[index].getId());
                            total.setSize(total.getSize() + tempTotal.getSize());
                            total.setDocumentCount(total.getDocumentCount() + tempTotal.getDocumentCount());
                            total.setFolderCount(total.getFolderCount() + tempTotal.getFolderCount());                        
                            total.setFolderDocCount(total.getFolderDocCount() + tempTotal.getFolderDocCount());
                        }
                    }
                }
            }                
        }catch(DbsException dbsException){
            throw dbsException;
        }
        return total;
    }
    
    
  /**
   * Purpose  : To update the versioned document format accessed through webDav as versioned 
   *            document modified by it sets the Format as null
   * @throws java.lang.Exception
   * @throws dms.beans.DbsException
   * @param familyId
   */
    public void updateFormat(Long familyId) throws DbsException, Exception{
      DbsFileSystem dbsFileSystem = null;
      DbsPublicObject dbsPublicObject = null;
      DbsDocument dbsDocument=null;
      DbsVersionSeries dbsVersionSeries;
      DbsVersionDescription[] dbsVersionDescriptions; 
      DbsFamily dbsFamily;
      
      try{
        dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
        dbsPublicObject = dbsFileSystem.findPublicObjectById(familyId);            
        dbsFamily = dbsPublicObject.getFamily();
        if(dbsFamily != null){
          dbsVersionSeries = dbsFamily.getPrimaryVersionSeries();
          dbsVersionDescriptions = dbsVersionSeries.getVersionDescriptions();
          dbsPublicObject = dbsPublicObject.getResolvedPublicObject();
          dbsDocument = (DbsDocument)dbsPublicObject;
          DbsFormat dbsFormat = dbsDocument.getFormat();
          if(dbsFormat==null){
            DbsPublicObject dbsPreviousPublicObject = dbsVersionDescriptions[dbsVersionDescriptions.length - 2].getDbsPublicObject();
            DbsDocument dbsPreviousDocument = (DbsDocument)dbsPreviousPublicObject;
            DbsFormat dbsPreviousFormat = dbsPreviousDocument.getFormat();
            dbsDocument.setFormat(dbsPreviousFormat);
          }
        }
      }catch(Exception dbsException){
        dbsException.printStackTrace();
        throw dbsException;
      }
    }
    
    private boolean renameVersions(String newName,Long familyId){
      DbsPublicObject dbsPO = null;
      DbsFamily dbsFam = null;
      DbsVersionDescription [] allVds = null;
      boolean renamed = false;
      DbsTransaction renameTransaction = null;
      try{
        renameTransaction = dbsLibrarySession.beginTransaction();
        dbsPO = dbsLibrarySession.getPublicObject(familyId);
        dbsFam = (DbsFamily)dbsPO;
        allVds = dbsFam.getPrimaryVersionSeries().getVersionDescriptions();
        int totalVds = ( allVds == null )?0:allVds.length;
        for( int index = 0; index < totalVds; index++ ){
          dbsPO = allVds[index].getDbsPublicObject();
          dbsPO.unlock();
          dbsPO.setName(newName);
        }
        dbsLibrarySession.completeTransaction(renameTransaction);
      }catch(DbsException ex){
        renamed = false;
        try {
          dbsLibrarySession.abortTransaction(renameTransaction);
        }catch (DbsException dbsEx) {
          logger.debug(dbsEx.toString());
        }
        logger.error(ex.toString());
      }
      return renamed;
    }
}