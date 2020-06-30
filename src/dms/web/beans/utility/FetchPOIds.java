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
 * $Id: FetchPOIds.java,v 1.4 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;
/* dms package references */
import dms.beans.DbsAttributeQualification;
import dms.beans.DbsAttributeSearchSpecification;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsFolder;
import dms.beans.DbsFolderRestrictQualification;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsSearch;
import dms.beans.DbsSearchClassSpecification;
import dms.beans.DbsSearchClause;
import dms.beans.DbsSearchQualification;
import dms.beans.DbsSearchSortSpecification;
/* Java API */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* Logger API */
import org.apache.log4j.Logger;

/**
 * Purpose:             Bean called to fetch document ids or folder ids from 
 *                      given folders and documents.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 14-01-2006
 * Last Modified Date : 10-02-2006
 * Last Modified By   : Suved Mishra
 */

public class FetchPOIds  {
  private Long [] ids;                  // contains array of Public Object ids
  private DbsLibrarySession dbsSession; // represents library session
  private DbsFolder parentFolder;       // represents search folder
  private Logger logger;                // logger for verbose logging
  
  public FetchPOIds() {
  }
  
  /**
   * Constructor for FetchPOIds Class
   * @param dbsSession
   */
  public FetchPOIds( DbsLibrarySession dbsSession ){
    this.dbsSession = dbsSession;
    this.logger = Logger.getLogger("DbsLogger");
  }

  /**
   * Purpose : To get Document ids from a given collection of folders and 
   *           documents.
   * @return : Return an array of Document ids.
   * @param  : ids
   * @param  : multilevelSearch
   */    
  public Long [] getDocIds(Long [] ids,boolean multilevelSearch){
    this.ids = ids;
    ArrayList pos = new ArrayList();
    DbsPublicObject dbsPO = null;
    DbsFolder dbsFolder = null;
    try{
      for( int index = 0; index < ids.length; index++ ){
        dbsPO = dbsSession.getPublicObject(ids[index]); 
        if( dbsPO instanceof DbsDocument ){
          pos.add(ids[index]);
        }else if( dbsPO instanceof DbsFamily  ){
          pos.add(dbsPO.getResolvedPublicObject().getId()); 
        }else if( dbsPO instanceof DbsFolder ){
          dbsFolder = (DbsFolder)dbsPO;
          logger.debug("Folder Name : "+dbsFolder.getName());
          Long [] docsIds = getDocIdsFromFolder(dbsFolder,multilevelSearch);
          int size = ( docsIds == null )?0:docsIds.length;
          for( int i =0; i < size; i++ ){
            pos.add(docsIds[i]);
          }
        }
      }
    }catch (DbsException dbsEx) {
      logger.error(dbsEx.toString());
      dbsEx.printStackTrace();
    }
    Long [] docIds = new Long[pos.size()];
    for( int index = 0; index < pos.size(); index++ ){
      docIds[index] = (Long)pos.get(index);
    }
    return docIds;
  }
  

  /**
   * Purpose : To check if a folder exists within a given folder and return it 
   *           so , else return null .
   * @return : DbsFolder.
   * @param  : folderName to check 
   * @param  : parentFolder
   * @param  : multiLevelSearch
   */    
  public DbsFolder getFolderByName( DbsFolder parentFolder, String folderName, 
                                    boolean multiLevelSearch ){
    this.parentFolder = parentFolder;
    DbsFolder targetFolder = null;
    Long [] folderIds = null;
    try{
      folderIds = getSubFolderIds(parentFolder,multiLevelSearch);
      int folderIdsSize = (folderIds == null )?0:folderIds.length; 
      for( int index = 0; index < folderIdsSize; index++ ){
        if(dbsSession.getPublicObject(folderIds[index]).getName().equalsIgnoreCase(folderName)){
          targetFolder = (DbsFolder)dbsSession.getPublicObject(folderIds[index]);
          break;
        }
      }
    }catch (DbsException dbsEx) {
      logger.error(dbsEx.toString());
    }
    return targetFolder;
  }
  
  /**
   * Purpose : To search for documents recursively in a given folder
   * @return : Return an array of Document ids contained in that folder.
   */    
  private Long[] getDocIdsFromFolder(DbsFolder dbsFolder,boolean multilevelSearch){
    //Variable Declaration
    ArrayList folderDocLists = new ArrayList();
    DbsSearch dbsSearch = null;
    List searchQualificationList = new ArrayList();
    try{
      DbsAttributeSearchSpecification dbsAttributeSearchSpecification = new DbsAttributeSearchSpecification();
      DbsSearchSortSpecification dbsSearchSortSpecification = new DbsSearchSortSpecification();
      DbsFolderRestrictQualification dbsFolderRestrictQualification = new DbsFolderRestrictQualification();
      DbsSearchClassSpecification dbsSearchClassSpecification = new DbsSearchClassSpecification();
      
      //dbsFolder = (DbsFolder)(new DbsFileSystem(dbsLibrarySession).findPublicObjectById((currentFolderId)));
      dbsFolderRestrictQualification.setStartFolder(dbsFolder);
      dbsFolderRestrictQualification.setSearchClassname(DbsPublicObject.CLASS_NAME);
      dbsFolderRestrictQualification.setMultiLevel(multilevelSearch);
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
      folderDocIdAttrbQual.setValue(DbsAttributeValue.newAttributeValue(dbsFolder.getId()));
      searchQualificationList.add(folderDocIdAttrbQual);

      //And together all the dbsSearch qualifications
      DbsSearchQualification dbsSearchQualification = null;
      Iterator iterator = searchQualificationList.iterator();
      while (iterator.hasNext()){
        DbsSearchQualification nextSearchQualification = (DbsSearchQualification) iterator.next();
        if (dbsSearchQualification == null) {
          dbsSearchQualification = nextSearchQualification;
        }else{
          dbsSearchQualification = new DbsSearchClause(dbsSearchQualification, nextSearchQualification, DbsSearchClause.AND);
        }
      }

      dbsAttributeSearchSpecification.setSearchQualification(dbsSearchQualification);
      dbsAttributeSearchSpecification.setSearchClassSpecification(dbsSearchClassSpecification);
      dbsAttributeSearchSpecification.setSearchSortSpecification(dbsSearchSortSpecification);
      
      dbsSearch = new DbsSearch(dbsSession,dbsAttributeSearchSpecification);
      //logger.debug("SQL Query: ");
      //logger.debug(dbsSearch.getSQL());
      dbsSearch.open();
      int itemCount = dbsSearch.getItemCount();
      logger.debug("itemCount : "+itemCount);
      DbsPublicObject dbsPO = null;
      DbsFamily dbsFam = null;
      for( int index = 0; index < itemCount; index++ ){
        dbsPO = ((DbsPublicObject)dbsSearch.next().getLibraryObject());
        if( dbsPO instanceof DbsDocument ){
          folderDocLists.add(dbsPO.getId());
        }else if( dbsPO instanceof DbsFamily ){
          dbsFam = (DbsFamily)dbsPO;
          folderDocLists.add(dbsFam.getResolvedPublicObject().getId());
        }else{
          // donot add folders
          
        }
      }
    }catch(DbsException dbsException){
      logger.error(dbsException.toString());
      dbsException.printStackTrace();
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
    if( folderDocLists.size() > 0 ){
      Long [] pOIds = new Long[folderDocLists.size()];
      for( int index = 0; index < pOIds.length; index++ ){
        pOIds[index] = (Long)folderDocLists.get(index);
      }
      return pOIds;
    }
    return null;
  }
 

  /**
   * Purpose : To search for sub folders recursively in a given folder
   * @return : Return an array of Folder ids contained in that folder.
   */    
  private Long[] getSubFolderIds(DbsFolder dbsFolder , boolean multiLevelSearch){
    //Variable Declaration
    ArrayList folderDocLists = new ArrayList();
    
    List searchQualificationList = new ArrayList();
    DbsSearch dbsSearch = null;
    try{
      DbsAttributeSearchSpecification dbsAttributeSearchSpecification = new DbsAttributeSearchSpecification();
      DbsSearchSortSpecification dbsSearchSortSpecification = new DbsSearchSortSpecification();
      DbsFolderRestrictQualification dbsFolderRestrictQualification = new DbsFolderRestrictQualification();
      DbsSearchClassSpecification dbsSearchClassSpecification = new DbsSearchClassSpecification();
      
      //dbsFolder = (DbsFolder)(new DbsFileSystem(dbsLibrarySession).findPublicObjectById((currentFolderId)));
      dbsFolderRestrictQualification.setStartFolder(dbsFolder);
      dbsFolderRestrictQualification.setSearchClassname(DbsFolder.CLASS_NAME);
      dbsFolderRestrictQualification.setMultiLevel(multiLevelSearch);
      searchQualificationList.add(dbsFolderRestrictQualification);
      
      // array of class to be searched
      String [] searchClasses = new String[] {DbsFolder.CLASS_NAME};
      dbsSearchClassSpecification.addSearchClasses(searchClasses);
      dbsSearchClassSpecification.addResultClass(DbsFolder.CLASS_NAME);
      
      // Array of classes involved in the order by clause
      String [] sortClasses = new String[] {DbsFolder.CLASS_NAME};
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
      folderDocIdAttrbQual.setValue(DbsAttributeValue.newAttributeValue(dbsFolder.getId()));
      searchQualificationList.add(folderDocIdAttrbQual);

      //And together all the dbsSearch qualifications
      DbsSearchQualification dbsSearchQualification = null;
      Iterator iterator = searchQualificationList.iterator();
      while (iterator.hasNext()){
        DbsSearchQualification nextSearchQualification = (DbsSearchQualification) iterator.next();
        if (dbsSearchQualification == null) {
          dbsSearchQualification = nextSearchQualification;
        }else{
          dbsSearchQualification = new DbsSearchClause(dbsSearchQualification, nextSearchQualification, DbsSearchClause.AND);
        }
      }

      dbsAttributeSearchSpecification.setSearchQualification(dbsSearchQualification);
      dbsAttributeSearchSpecification.setSearchClassSpecification(dbsSearchClassSpecification);
      dbsAttributeSearchSpecification.setSearchSortSpecification(dbsSearchSortSpecification);
      
      dbsSearch = new DbsSearch(dbsSession,dbsAttributeSearchSpecification);
      int itemCount = dbsSearch.getItemCount();
      logger.debug("itemCount : "+itemCount);
      dbsSearch.open();
      DbsPublicObject dbsPO = null;
      for( int index = 0; index < itemCount; index++ ){
        dbsPO = ((DbsPublicObject)dbsSearch.next().getLibraryObject());
        folderDocLists.add(dbsPO.getId());
      }
    }catch(DbsException dbsException){
      dbsException.printStackTrace();
      logger.error(dbsException.toString());
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
    if( folderDocLists.size() > 0 ){
      Long [] pOIds = new Long[folderDocLists.size()];
      for( int index = 0; index < pOIds.length; index++ ){
        pOIds[index] = (Long)folderDocLists.get(index);
      }
      return pOIds;
    }
    return null;
  }
  
}
