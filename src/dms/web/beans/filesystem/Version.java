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
 * $Id: Version.java,v 20040220.33 2006/03/14 06:29:48 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
// dms package references
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsTransaction;
import dms.beans.DbsVersionDescription;
import dms.beans.DbsVersionSeries;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.GeneralUtil;
import dms.web.beans.wf.watch.InitiateWatch;
//Java API
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
// Struts API
import org.apache.log4j.Logger;
//import oracle.ifs.adk.filesystem.*;
//import oracle.ifs.beans.*;
//import oracle.ifs.common.*;
//import oracle.ifs.examples.api.utils.*;
/**
 *	Purpose: Bean to perform version operations for documents and folders. 
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   13-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class Version  {
    public static final String STATUS_CHECKEDOUT = "Checked Out";
    public static final String STATUS_CHECKEDIN = "Checked In";
    public static final String STATUS_CREATED = "Created";

    private DbsLibrarySession dbsLibrarySession = null;     //DbsLibrarySession Object
    private Logger logger = null;
    private Locale locale = null;
    private String[] replacementValues;
    private DbsTransaction dbsTransaction = null;
    
    public Version(DbsLibrarySession dbsLibrarySession) {
        this.dbsLibrarySession = dbsLibrarySession;
        //Initialize logger
        logger = Logger.getLogger("DbsLogger");
        //use default if locale not set
        locale = Locale.getDefault(); //
        replacementValues = new String[5];
    }


    /**
     * Purpose  : to make document versioned 
     * @param   : folderDocIds - list of folder and document
     * @returns : void
     * @throws  : DbsException - if operation fails
     */
    public void makeVersioned(Long[] folderDocIds,String relativePath,String userName) throws DbsException , IOException{
      DbsPublicObject dbsPublicObject = null;
      DbsFileSystem dbsFileSystem = null;
      String className;
      DbsPublicObject versionedPO = null;
      DbsFamily dbsFam = null;
      ConnectionBean connBean = null;
      String query = null;
      String [] results = null;
      int length = 0;
      String action = null;
      try{
        dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
        for(int index = 0; index < folderDocIds.length ; index++){
          dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[index]).getResolvedPublicObject();
          className = dbsPublicObject.getClassname();
          if(className.equals(DbsFolder.CLASS_NAME)){
              logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}" );
              makeVersionableRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,relativePath,userName);
          }
          if(className.equals(DbsDocument.CLASS_NAME)){
            // make doc versioned
            versionedPO=dbsFileSystem.makeVersioned(dbsPublicObject);
            if( relativePath!=null && userName!=null ){
              // code for wf submission goes here
              connBean = new ConnectionBean(relativePath);
              // fetch users who have applied watch on the unversioned document
              query = "SELECT USER_ID FROM watch_pos WHERE PO_ID="+dbsPublicObject.getId();
              results = connBean.executeQuery(query);
              length = ( results == null )?0:results.length;
              // delete users corr to the unversioned doc from table
              query = "DELETE FROM watch_pos WHERE PO_ID ="+dbsPublicObject.getId();
              logger.debug("result : "+connBean.executeUpdate(query));
            }
            try{
              if( versionedPO.isVersioned() ){
                dbsFam = versionedPO.getFamily();
                dbsFam.setDescription(
                      dbsFam.getResolvedPublicObject().getDescription());
                if( relativePath!=null && userName!=null ){
                  // if watch existed for the unversioned doc, add watch to 
                  // the family with the original set of users.
                  for( int resultsIndex =0; resultsIndex < length; resultsIndex++ ){
                    query = "INSERT INTO watch_pos VALUES("+dbsFam.getId()+",'"+results[resultsIndex]+"')";
                    logger.debug("result : "+connBean.executeUpdate(query));
                  }
                }
                DocEventLogBean eventBean = new DocEventLogBean();
                action = "Doc made versioned, Version 1 created";
                eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
              }else{
                logger.debug("Family could not be found");  
              }
              // if watch is set, submit to wf
              if( relativePath!=null && userName!=null ){
                if( length!=0 ){
                  String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been made versioned";
                  InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                  iniWatch.startWatchProcess();
                  actionForWatch = null;
                }
              }
            }catch( DbsException dbsEx ){
              dbsEx.printStackTrace();
            }catch( Exception ex ){
              ex.printStackTrace();
            }finally{
              connBean.closeStatement();
              connBean.closeConnection();
            }
            connBean = null;
            logger.info("DbsDocument Name : {" + versionedPO.getName() + "}" );    
          }
          if(className.equals(DbsFamily.CLASS_NAME)){
              //Do nothing if public object is a dbsFamily
              //because it is already versioned
          }
        }
      }catch(DbsException dbsException){
          throw dbsException;
      }
    }

    //to make documents in a folder versioned recursively
    private void makeVersionableRecursively(DbsFolder top,DbsFileSystem dbsFileSystem,String relativePath,String userName) throws DbsException {
      //get all the items present in the given folder
      DbsPublicObject[] dbsPublicObjects = top.getItems();
      DbsPublicObject dbsPublicObject;
      DbsPublicObject versionedPO = null;
      DbsFamily dbsFam = null;

      ConnectionBean connBean = null;
      String query = null;
      String [] results = null;
      String action = null;
      
      String className;
      int length = (dbsPublicObjects == null) ? 0 : dbsPublicObjects.length;
      int usersLength = 0;
      for (int i = 0; i < length; i++){
        // if the item is a folder, call this same method recursively
        dbsPublicObject = dbsPublicObjects[i];
        className = dbsPublicObject.getClassname();
        if(className.equals(DbsFolder.CLASS_NAME)){
          logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}" );
          makeVersionableRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,relativePath,userName);
        }
        if(className.equals(DbsDocument.CLASS_NAME)){
          // code for wf submission goes here
          versionedPO=dbsFileSystem.makeVersioned(dbsPublicObject);
          if( relativePath!=null && userName!=null ){
            // code for wf submission goes here
            connBean = new ConnectionBean(relativePath);
            // fetch users who have applied watch on the unversioned document
            query = "SELECT USER_ID FROM watch_pos WHERE PO_ID="+dbsPublicObject.getId();
            results = connBean.executeQuery(query);
            usersLength = ( results == null )?0:results.length;
            // delete users corr to the unversioned doc from table
            query = "DELETE FROM watch_pos WHERE PO_ID ="+dbsPublicObject.getId();
            logger.debug("result : "+connBean.executeUpdate(query));
          }
          try{
            if( versionedPO.isVersioned() ){
              dbsFam = versionedPO.getFamily();
              dbsFam.setDescription(
                    dbsFam.getResolvedPublicObject().getDescription());
              if( relativePath!=null && userName!=null ){
                // if watch existed for the unversioned doc, add watch to 
                // the family with the original set of users.
                for( int resultsIndex =0; resultsIndex < usersLength; resultsIndex++ ){
                  query = "INSERT INTO watch_pos VALUES("+dbsFam.getId()+",'"+results[resultsIndex]+"')";
                  logger.debug("result : "+connBean.executeUpdate(query));
                }
              }
              DocEventLogBean eventBean = new DocEventLogBean();
              action = "Doc made versioned,Version 1 created";
              eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
            }else{
              logger.debug("Family could not be found");  
            }
            if( relativePath!=null && userName!=null ){
              // if watch is set, submit to wf
              if( usersLength!=0 ){
                String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been made versioned";
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;
              }
            }
          }catch( DbsException dbsEx ){
            dbsEx.printStackTrace();
          }catch( Exception ex ){
            ex.printStackTrace();
          }finally{
            connBean.closeStatement();
            connBean.closeConnection();
          }
          connBean = null;
          logger.info("DbsDocument Name : {" + versionedPO.getName() + "}" );    
        }
        if(className.equals(DbsFamily.CLASS_NAME)){
            //Do nothing if public object is a dbsFamily
            //because it is already versioned
        }
      }
    }

    public int checkOut(Long[] folderDocIds, String comment,String relativePath,String userName) throws DbsException , IOException{
        DbsPublicObject dbsPublicObject = null;
        DbsFileSystem dbsFileSystem = null;
        String className;
        //0 = no document checkout because it is not versioned or its already checked out
        //1 = checkout successful
        int resultCode = 0;
        try{
          dbsTransaction = dbsLibrarySession.beginTransaction();        
          dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
          for(int index = 0; index < folderDocIds.length -1 ; index++){
            dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[index]);
            className = dbsPublicObject.getClassname();
            if(className.equals(DbsFolder.CLASS_NAME)){
              logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}");
              int resultCodeTemp = checkOutRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,comment,relativePath,userName);
              switch (resultCodeTemp){
                case 0 :
                    //do not modify resultcode
                    break;
                case 1 :
                    resultCode = 1;
                    break;
                }
              }
              if(className.equals(DbsDocument.CLASS_NAME)){
                  //Do nothing if public object is not a dbsFamily
                  //because it can not be checked out
              }
              if(className.equals(DbsFamily.CLASS_NAME)){
                //false means that if dbsPublicObject is not versioned then don't version it and check it out
                if(! dbsFileSystem.isCheckedOut(dbsPublicObject)){
                  keepCheckOut(dbsPublicObject.getId(),comment);
                  DbsFamily dbsFam = (DbsFamily)dbsPublicObject;
                  logger.debug("dbsFam Desc : "+dbsFam.getDescription());
                  dbsFam.setDescription(
                        dbsFam.getResolvedPublicObject().getDescription());
                  logger.debug("dbsFam Desc set : "+dbsFam.getDescription());                              
                  
                  logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}" );
                  if( relativePath!=null && userName!=null ){
                    // code for watch wf submission goes here
                    String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been checked out";
                    InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                    iniWatch.startWatchProcess();
                    actionForWatch = null;
                  }
                  resultCode = 1;
                }
              }
            }
          dbsLibrarySession.completeTransaction(dbsTransaction);
          dbsTransaction = null;
        }catch(DbsException dbsException){
          throw dbsException;
        }finally{
          if(dbsTransaction != null){
              dbsLibrarySession.abortTransaction(dbsTransaction);
          }
        }
        return  resultCode;
    }

    //to make documents in a folder versioned recursively
    private int checkOutRecursively(DbsFolder top,DbsFileSystem dbsFileSystem, String comment,String relativePath,String userName) throws DbsException {
        //get all the items present in the given folder
        DbsPublicObject[] dbsPublicObjects = top.getItems();
        DbsPublicObject dbsPublicObject;
        String className;
        int resultCode = 0;
        int length = (dbsPublicObjects == null) ? 0 : dbsPublicObjects.length;
        for (int i = 0; i < length; i++){
          // if the item is a folder, call this same method recursively
          dbsPublicObject = dbsPublicObjects[i];
          className = dbsPublicObject.getClassname();
          if(className.equals(DbsFolder.CLASS_NAME)){
            logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}");
            
            int resultCodeTemp = checkOutRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,comment,relativePath,userName);
            switch (resultCodeTemp){
              case 0 :
                  //do not modify resultcode
                  break;
              case 1 :
                  resultCode = 1;
                  break;
            }
          }
          if(className.equals(DbsDocument.CLASS_NAME)){
              //Do nothing if public object is a dbsFamily
              //because it can not be checked out
          }
          if(className.equals(DbsFamily.CLASS_NAME)){
            //false means that if dbsPublicObject is not versioned then don't version it and check it out
            if(! dbsFileSystem.isCheckedOut(dbsPublicObject)){
              keepCheckOut(dbsPublicObject.getId(),comment);
              logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}" );
              DbsFamily dbsFam = (DbsFamily)dbsPublicObject;              
              // code for watch wf submission goes here
              if( relativePath!=null && userName!=null ){
                String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been checked out";
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;                
              }
              resultCode = 1;
            }
          }
        }
        return  resultCode;
    }


//0 = no document checkin because it is not versioned or its not checked out
//1 = checkin successful
    
    public int checkIn(Long[] folderDocIds, String comment,boolean keepCheckedOut,String relativePath,String userName) throws DbsException , IOException{
      DbsPublicObject dbsPublicObject = null;
      DbsFileSystem dbsFileSystem = null;
      String className;
      String action = null; 
      int checkInSuccess = 0;
      DbsPublicObject dbsPO = null;

      try{
        dbsTransaction = dbsLibrarySession.beginTransaction();
        dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
        for(int index = 0; index < folderDocIds.length ; index++){
          dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[index]);
          if(dbsPublicObject instanceof DbsFolder){
            logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}" );
            
            int checkingSuccessTemp = checkInRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,comment,keepCheckedOut,relativePath,userName);
            switch (checkingSuccessTemp){
                case 0:
                    break;
                case 1:
                    checkInSuccess = 1;
                    break;
            }
          }
          if(dbsPublicObject instanceof DbsDocument){
              //Do nothing if public object is not a dbsFamily
              //because it can not be checked out
          }
          if(dbsPublicObject instanceof DbsFamily){
            if(dbsFileSystem.isCheckedOut(dbsPublicObject)){
              dbsFileSystem.checkIn(dbsPublicObject,comment);
              DbsFamily dbsFam = (DbsFamily)dbsPublicObject;
              logger.debug("dbsFam Desc : "+dbsFam.getDescription());
              dbsFam.setDescription(
                    dbsFam.getResolvedPublicObject().getDescription());
              logger.debug("dbsFam Desc set : "+dbsFam.getDescription());
              // code for watch wf submission goes here
              if( relativePath!=null && userName!=null ){
                String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been checked in,ie: New Version uploaded";
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;
              }
              DocEventLogBean eventBean = new DocEventLogBean();
              eventBean.logPrevFamilyEvents(dbsLibrarySession,dbsFam.getId());
              action = "New version uploaded,Version "+
                        dbsFam.getPrimaryVersionSeries().getLastVersionDescription().getVersionNumber()+
                        " created";
              eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
              action = "Doc checked in";
              eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
              if(keepCheckedOut){
                keepCheckOut(dbsPublicObject.getId(),comment);
              }
              logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}" );
              checkInSuccess = 1;
            }
          }
        }
        dbsLibrarySession.completeTransaction(dbsTransaction);
        dbsTransaction = null;
      }catch(DbsException dbsException){
        throw dbsException;
      }finally{
        if(dbsTransaction != null){
          dbsLibrarySession.abortTransaction(dbsTransaction);
        }
      }

      return checkInSuccess;
    }

    //to make documents in a folder versioned recursively
    private int checkInRecursively(DbsFolder top,DbsFileSystem dbsFileSystem,String comment, boolean keepCheckedOut,String relativePath,String userName) throws DbsException {
      //get all the items present in the given folder
      DbsPublicObject[] dbsPublicObjects = top.getItems();
      DbsPublicObject dbsPublicObject;
      String className;
      String action = null; 
      int checkInSuccess = 0;
      int length = (dbsPublicObjects == null) ? 0 : dbsPublicObjects.length;
      for (int i = 0; i < length; i++){
        // if the item is a folder, call this same method recursively
        dbsPublicObject = dbsPublicObjects[i];
        className = dbsPublicObject.getClassname();
        if(className.equals(DbsFolder.CLASS_NAME)){
            logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}");
            int checkInSuccessTemp = checkInRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,comment,keepCheckedOut,relativePath,userName);
            switch (checkInSuccessTemp){
              case 0:
                break;
              case 1:
                checkInSuccess = 1;
                break;
            }
        }
        if(className.equals(DbsDocument.CLASS_NAME)){
            //Do nothing if public object is a dbsFamily
            //because it can not be checked out
        }
        if(className.equals(DbsFamily.CLASS_NAME)){
          if(dbsFileSystem.isCheckedOut(dbsPublicObject)){
            dbsFileSystem.checkIn(dbsPublicObject,comment);
            DbsFamily dbsFam = (DbsFamily)dbsPublicObject;
            logger.debug("dbsFam Desc : "+dbsFam.getDescription());
            dbsFam.setDescription(
                  dbsFam.getResolvedPublicObject().getDescription());
            logger.debug("dbsFam Desc set : "+dbsFam.getDescription());                              
            // code for watch wf submission goes here
            if( relativePath!=null && userName!=null ){
              String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" has been checked in,ie: New Version uploaded";
              InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
              iniWatch.startWatchProcess();
              actionForWatch = null;
            }
            DocEventLogBean eventBean = new DocEventLogBean();
            eventBean.logPrevFamilyEvents(dbsLibrarySession,dbsFam.getId());
            action = "New version uploaded,Version "+
                      dbsFam.getPrimaryVersionSeries().getLastVersionDescription().getVersionNumber()+
                      " created";
            eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
            action = "Doc checked in";
            eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
            if(keepCheckedOut){
              keepCheckOut(dbsPublicObject.getId(),comment);
            }
            logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}");
            checkInSuccess = 1;
          }
        }
      }
      return checkInSuccess;
    }

    public int cancelCheckout(Long[] folderDocIds,String relativePath,String userName) throws DbsException ,IOException{
      DbsPublicObject dbsPublicObject = null;
      DbsFileSystem dbsFileSystem = null;
      String className;
      String action = null;
      int cancelCheckOutSuccess = 0;
      try{
        dbsTransaction = dbsLibrarySession.beginTransaction();
        dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
        for(int index = 0; index < folderDocIds.length ; index++){
          dbsPublicObject = dbsLibrarySession.getPublicObject(folderDocIds[index]);
          className = dbsPublicObject.getClassname();
          if(className.equals(DbsFolder.CLASS_NAME)){
            logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}" );
            int cancelCheckOutSuccessTemp = cancelCheckoutRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,relativePath,userName);
            switch (cancelCheckOutSuccessTemp){
              case 0:
                break;
              case 1:
                cancelCheckOutSuccess = 1;
                break;
            }
          }
          if(className.equals(DbsDocument.CLASS_NAME)){
              //Do nothing if public object is a dbsFamily
              //because it can not be checked out
          }
          if(className.equals(DbsFamily.CLASS_NAME)){
            if(dbsFileSystem.isCheckedOut(dbsPublicObject)){
              dbsFileSystem.cancelCheckout(dbsPublicObject);
              logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}" );
              DbsFamily dbsFam = (DbsFamily)dbsPublicObject;              
              // code for watch wf submission goes here
              if( relativePath!=null && userName!=null ){
                String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" check out has been cancelled";
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;
              }
              DocEventLogBean eventBean = new DocEventLogBean();
              action = "Doc check out cancelled";
              eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
              cancelCheckOutSuccess = 1;
            }
          }
        }
        dbsLibrarySession.completeTransaction(dbsTransaction);
        dbsTransaction = null;
      }catch(DbsException dbsException){
        throw dbsException;
      }finally{
        if(dbsTransaction != null){
          dbsLibrarySession.abortTransaction(dbsTransaction);
        }
      }
      return cancelCheckOutSuccess;
    }

    //to make documents in a folder versioned recursively
    private int cancelCheckoutRecursively(DbsFolder top,DbsFileSystem dbsFileSystem,String relativePath,String userName) throws DbsException {
      //get all the items present in the given folder
      DbsPublicObject[] dbsPublicObjects = top.getItems();
      DbsPublicObject dbsPublicObject;
      String className;
      String action = null;
      int cancelCheckOutSuccess = 0;
      int length = (dbsPublicObjects == null) ? 0 : dbsPublicObjects.length;
      for (int i = 0; i < length; i++){
        // if the item is a folder, call this same method recursively
        dbsPublicObject = dbsPublicObjects[i];
        className = dbsPublicObject.getClassname();
        if(className.equals(DbsFolder.CLASS_NAME)){
          logger.info("DbsFolder Name : {" + dbsPublicObject.getName() + "}" );
          int cancelCheckOutSuccessTemp = cancelCheckoutRecursively((DbsFolder)dbsPublicObject,dbsFileSystem,relativePath,userName);
          switch(cancelCheckOutSuccessTemp){
            case 0:
              break;
            case 1:
              cancelCheckOutSuccess = 1;
              break;
          }
        }
        if(className.equals(DbsDocument.CLASS_NAME)){
            //Do nothing if public object is a dbsFamily
            //because it can not be checked out
        }
        if(className.equals(DbsFamily.CLASS_NAME)){
          if(dbsFileSystem.isCheckedOut(dbsPublicObject)){
            dbsFileSystem.cancelCheckout(dbsPublicObject);
            logger.info("DbsDocument Name : {" + dbsPublicObject.getName() + "}" );
            DbsFamily dbsFam = (DbsFamily)dbsPublicObject;              
            // code for watch wf submission goes here
            if( relativePath!=null && userName!=null ){
              String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsFam.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : "+dbsFam.getName()+" check out has been cancelled";
              InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,actionForWatch,dbsFam.getId());
              iniWatch.startWatchProcess();
              actionForWatch = null;            
            }
            DocEventLogBean eventBean = new DocEventLogBean();
            action = "Doc check out cancelled";
            eventBean.logEvent(dbsLibrarySession,dbsFam.getId(),action);
            cancelCheckOutSuccess = 1;
          }
        }
      }
      return cancelCheckOutSuccess;
    }


    public ArrayList getDocumentHistoryDetails(Long docId) throws DbsException{
        //docid = 158299
        DocumentHistoryDetail dhd = null;
        ArrayList documentHistoryDetails = new ArrayList();
        DbsVersionSeries dbsVersionSeries;
        DbsVersionDescription[] dbsVersionDescriptions; 
        DbsPublicObject dbsPublicObject;
        DbsFamily dbsFamily;
        DbsFileSystem dbsFileSystem;
        try{
            dbsPublicObject = dbsLibrarySession.getPublicObject(docId);
            dbsFileSystem = new DbsFileSystem(dbsLibrarySession);
            if(dbsPublicObject.getClassname().equals(DbsFamily.CLASS_NAME)){
                dbsFamily = (DbsFamily)dbsPublicObject;
                dbsVersionSeries = dbsFamily.getPrimaryVersionSeries();
                dbsVersionDescriptions = dbsVersionSeries.getVersionDescriptions();

                if(dbsFileSystem.isCheckedOut(dbsFamily)){
                    dhd = new DocumentHistoryDetail();
                    dhd.setVersionNumber(dbsVersionSeries.getLastVersionDescription().getVersionNumber() + 1);
                    dhd.setDocId(null);
                    dhd.setDocName(dbsFamily.getName());
                    dhd.setVersionDate(GeneralUtil.getDateForDisplay(dbsVersionSeries.getReservationDate(),locale));
                    dhd.setUserName(dbsVersionSeries.getReservor().getName());
                    dhd.setComment(dbsVersionSeries.getReservationComment());                    
                    dhd.setActionType(STATUS_CHECKEDOUT);
                    documentHistoryDetails.add(dhd);
                }
                for(int index = dbsVersionDescriptions.length - 1 ; index >= 0 ; index--){
                    dhd = new DocumentHistoryDetail();
                    dhd.setVersionNumber(dbsVersionDescriptions[index].getVersionNumber());
                    dbsPublicObject = dbsVersionDescriptions[index].getDbsPublicObject();
                    dhd.setDocId(dbsPublicObject.getId());
                    dhd.setDocName(dbsFamily.getName());
                    dhd.setVersionDate(GeneralUtil.getDateForDisplay(dbsPublicObject.getCreateDate(),locale));
                    dhd.setUserName(dbsPublicObject.getCreator().getName());
                    dhd.setComment(dbsVersionDescriptions[index].getRevisionComment());
                    dhd.setActionType(STATUS_CHECKEDIN);
                    documentHistoryDetails.add(dhd);
                }
                dhd = (DocumentHistoryDetail)documentHistoryDetails.get(documentHistoryDetails.size() -1);
                dhd.setActionType(STATUS_CREATED);
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }
        return documentHistoryDetails;
    }

    /**
     * Purpose : To delete a document from the document history
     * @param  : Long id of the document to be deleted
     * @return : void
     */    
    public void deleteDocHistory(Long id) throws DbsException, Exception{
        DbsPublicObject dbsPublicObject = null;
        String action = null;
        long versionNumber = 0;
        try{
            dbsPublicObject = dbsLibrarySession.getPublicObject(id);
            DbsFamily dbsFamily = dbsPublicObject.getFamily();
            DbsVersionSeries vs = dbsFamily.getPrimaryVersionSeries();
            DbsVersionDescription[] vd = vs.getVersionDescriptions();
            for(int index = 0; index < vd.length; index ++){
                if(vd[index].getDbsPublicObject().getId().longValue() == dbsPublicObject.getId().longValue()){
                    //if vd is not latest version description then free it
                    if(!vd[index].isLatestVersionDescription()){
                        logger.info("Version {" + vd[index].getVersionNumber() + "} of {" + dbsPublicObject.getName() + "} deleted successfully");
                        versionNumber = vd[index].getVersionNumber();
                        action = "Version "+versionNumber+" has been deleted";
                        vd[index].free();
                        DocEventLogBean eventBean = new DocEventLogBean();
                        eventBean.logEvent(dbsLibrarySession,dbsFamily.getId(),action);
                    }
                    break;
                }
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }
    }

    /**
    * Purpose : To rollback the history to a point where the document exist
    * @param  : Long id of the document to be deleted
    * @return : void
    */    
    public void rollbackDocHistory(Long id) throws DbsException, Exception{
        DbsPublicObject dbsPublicObject = null;
        try{
            dbsPublicObject = dbsLibrarySession.getPublicObject(id);
            DbsFamily dbsFamily = dbsPublicObject.getFamily();
            DbsVersionSeries vs = dbsFamily.getPrimaryVersionSeries();
            DbsVersionDescription[] vd = vs.getVersionDescriptions();
            for(int index = 0; index < vd.length; index ++){
                if(vd[index].getVersionNumber() > dbsPublicObject.getVersionNumber()){
                    logger.info("Version {" + vd[index].getVersionNumber() + "} of {" + dbsPublicObject.getName() + "} deleted successfully");                    
                    vd[index].free();
                }
            }
        }catch(DbsException dbsException){
//            DbsException dex = new DbsException(ex);
            throw dbsException;
        }
    }

    public void keepCheckOut(Long familyId, String comment) throws DbsException{
        DbsFamily dbsFamily = (DbsFamily)dbsLibrarySession.getPublicObject(familyId);
        DbsVersionSeries vs = dbsFamily.getPrimaryVersionSeries();
        if (!vs.isReserved() && !dbsFamily.isLocked()) {
           vs.reserveNext(null, comment);
        }
        String action = "Doc checked out ";
        DocEventLogBean eventBean = new DocEventLogBean();
        eventBean.logEvent(this.dbsLibrarySession,familyId,action);
    }

    public DocumentHistoryDetail getVersionedDocProperty(Long familyId, Long docId) throws DbsException{
        DocumentHistoryDetail dhd = null;
        DbsVersionSeries dbsVersionSeries;
        DbsVersionDescription[] dbsVersionDescriptions; 
        DbsPublicObject dbsPublicObject;
        DbsFamily dbsFamily;
        try{
            dbsFamily = (DbsFamily)dbsLibrarySession.getPublicObject(familyId);
            dbsVersionSeries = dbsFamily.getPrimaryVersionSeries();
            dbsVersionDescriptions = dbsVersionSeries.getVersionDescriptions();
            dhd = new DocumentHistoryDetail();
            
            if(docId.longValue() == 0){
                dhd.setVersionNumber(dbsVersionSeries.getLastVersionDescription().getVersionNumber() + 1);
                dhd.setDocId(null);
                dhd.setDocName(dbsFamily.getName());
                dhd.setVersionDate(GeneralUtil.getDateForDisplay(dbsVersionSeries.getReservationDate(),locale));
                dhd.setUserName(dbsVersionSeries.getReservor().getName());
                dhd.setComment(dbsVersionSeries.getReservationComment());
                dhd.setActionType(STATUS_CHECKEDOUT);
            }else{
                for(int index = dbsVersionDescriptions.length - 1 ; index >= 0 ; index--){
                    dbsPublicObject = dbsVersionDescriptions[index].getDbsPublicObject();
                    if(dbsPublicObject.getId().longValue() == docId.longValue()){
                        dhd.setVersionNumber(dbsVersionDescriptions[index].getVersionNumber());
                        dhd.setDocId(dbsPublicObject.getId());
                        dhd.setDocName(dbsFamily.getName());
                        dhd.setVersionDate(GeneralUtil.getDateForDisplay(dbsPublicObject.getCreateDate(),locale));
                        dhd.setUserName(dbsPublicObject.getCreator().getName());
                        dhd.setComment(dbsVersionDescriptions[index].getRevisionComment());
                        if(index == 0){
                            dhd.setActionType(STATUS_CREATED);
                        }else{
                            dhd.setActionType(STATUS_CHECKEDIN);
                        }
                        break;
                    }
                }
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }
        return dhd;
    }
}
