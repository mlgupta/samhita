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
 * $Id: ReplaceVoucherBean.java,v 1.6 2006/02/02 12:32:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsClassObject;
import dms.beans.DbsCollection;
import dms.beans.DbsDocument;
import dms.beans.DbsDocumentDefinition;
import dms.beans.DbsException;
import dms.beans.DbsFamily;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsSelector;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.utility.FetchPOIds;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import dms.web.beans.utility.SearchUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
/* Logger API */
import org.apache.log4j.Logger;

/**
 * Purpose            : Bean called to manage document images.
 *                      If no documentId is sent as request parameter, set up
 *                      a dummy document in the managed folder and return it's
 *                      id as well as link.
 *                      If a documentId is provided as request parameter,then 
 *                      return boolean true to the invoking action for further
 *                      processing viz: opening up a file upload jsp.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 16-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class ReplaceVoucherBean  {
  /* member variables declaration goes here */
  private String documentId;                  /* represents document id */
  private String managedFolderId = null;      /* represents managed folder id */  
  private String voucherId=null;              /* represents voucher id */
  private String blankDocPath=null;           /* represents physical path for blank doc */
  private DbsLibrarySession dbsLibrarySession;/* library session object */
  private DbsFolder dbsFolder;                /* folder object */ 
  private DbsFolder parentFolder;             /* parent folder object */ 
  private FileInputStream fis =null;          /* input stream for default content */
  private Logger logger;                      /* logger for verbose logging */
  public static String DATE_FORMAT = "yyyyMMdd"; /* date format specified for adapters */
  public DateHelperForFileSystem dateHelper;  /* bean for formatting date */ 
  
  public ReplaceVoucherBean() {
  }
  
  /**
   * Constructor for ReplaceVoucherBean Class
   * @param dbsLibrarySession
   * @param documentId
   * @param dbsFolder
   * @param blankDocPath
   * @param voucherId
   * @param managedFolderId
   */
  public ReplaceVoucherBean( DbsLibrarySession dbsLibrarySession,
                             String managedFolderId,String documentId,
                             String voucherId,String blankDocPath ){
    this.dbsLibrarySession = dbsLibrarySession;
    this.dbsFolder = dbsFolder;
    this.documentId = documentId;
    this.logger = Logger.getLogger("DbsLogger");
    this.dateHelper = new DateHelperForFileSystem();
    this.parentFolder = null;
    this.managedFolderId = managedFolderId;
    this.voucherId=voucherId;
    this.blankDocPath=blankDocPath;
  }

  /**
   * Function called to replace a document's image
   * @return boolean true if image has been replaced, false if dummy doc created 
   */
  public boolean replaceImage(){
    boolean isToBeReplaced = true;
    DbsFolder targetFolder = null;
    try{
      /* if a valid document id has been provided, update it's content */
      if( documentId != null && documentId.trim().length()!=0 ){
        DbsPublicObject dbsPO = dbsLibrarySession.getPublicObject(
                                                  new Long(documentId));
        parentFolder = dbsPO.getFolderReferences(0);
        DbsDocument dbsDoc = null;
        if( dbsPO instanceof DbsDocument ){
          dbsDoc = (DbsDocument)dbsPO;
        }else if( dbsPO instanceof DbsFamily ){
          DbsFamily dbsFam = (DbsFamily)dbsPO;
          dbsDoc = (DbsDocument)dbsFam.getResolvedPublicObject();
        }else{
          // do nothing
        }
        logger.debug("Document Name : "+dbsDoc.getName());
        logger.debug("Folder Name : "+parentFolder.getName()+" Path : "+
                      parentFolder.getAnyFolderPath());
      }else{
        /* if no document id has been provided, then create a dummy image and 
         * upload it in the default managed folder */
        /* step 1 : create document definition  */
        DbsDocumentDefinition dbsDocDef = new DbsDocumentDefinition(
                                                            dbsLibrarySession);
        /* step 2 : search for "DbsDocument" object to set the definition's 
         *          ClassObject */
        DbsClassObject co = dbsLibrarySession.getClassObjectByName(
                                                        DbsDocument.CLASS_NAME);
        dbsDocDef.setClassObject(co);
        /* step 3 : set content as blank document  */
        //logger.debug("Blank Doc Path:"+blankDocPath);
        fis = new FileInputStream(new File(blankDocPath)); 
        dbsDocDef.setContentStream(fis);
        /* step 4 : set the format object for the definition , default extension
         *          for the document would be ".jpg" */
         
        DbsFormat format = null;
        DbsCollection coll = dbsLibrarySession.getFormatExtensionCollection();
        String ext = "jpg";
        try{
          format = (DbsFormat)coll.getItems(ext);
        }catch (DbsException e) {
          if (e.containsErrorCode(12200)) {
            DbsSelector selector = new DbsSelector(dbsLibrarySession);
            selector.setSearchClassname(DbsFormat.CLASS_NAME);
           
            selector.setSearchSelection(DbsFormat.MIMETYPE_ATTRIBUTE + 
                                          " = 'application/octet-stream'");
           
            format = (DbsFormat) selector.getItems(0);
            e.printStackTrace();
          }                
         }
        dbsDocDef.setFormat(format);
        if( voucherId == null ){
          String name = "NewImage"+dateHelper.format(new Date(),
                                                  "yyyy-mm-dd-HH-mm-ss");
          voucherId = name;
        }
        /* step 5 : set an unique name for the definition */
        dbsDocDef.setAttribute(DbsDocument.NAME_ATTRIBUTE,
                  DbsAttributeValue.newAttributeValue(voucherId+"."+ext));
        /* step 6 : set initial description for the definition as 
         *          "default description" */          
        dbsDocDef.setAttribute(DbsDocument.DESCRIPTION_ATTRIBUTE,
                  DbsAttributeValue.newAttributeValue("default description"));
        /* step 7: create the document object from the definition object */          
        DbsDocument dbsDoc = (DbsDocument)
                              dbsLibrarySession.createPublicObject(dbsDocDef);
        logger.debug("dbsDoc Name : "+dbsDoc.getName());
        DbsAccessControlList [] dbsAclColl = 
                    SearchUtil.listAcls(dbsLibrarySession,"Public",-1,-1,false);
        dbsDoc.setAcl(dbsAclColl[0]);
        this.documentId = dbsDoc.getId().toString();
        String folderName = dateHelper.format(new Date(),DATE_FORMAT);
        /* step 8: now that the document object has been created successfully,
         *         upload it in the destination folder. */
        DbsFileSystem dbsFs = new DbsFileSystem(dbsLibrarySession);
        try{
          /* find the managed folder */
          targetFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                      new Long(managedFolderId)); 
        }catch( DbsException dbsEx ){
          logger.error("Exception occurred in ReplaceVoucherBean...");
          logger.error(dbsEx.toString());
        }catch( Exception e ){
          logger.error("Exception occurred in ReplaceVoucherBean...");
          logger.error(e.toString());
        }
        /* if found, search for a folder by name of format "yyyy-mm-dd",
         * add the document object to this destination folder */
        if( targetFolder != null ){
          logger.debug("targetFolder Name : "+targetFolder.getName()+" Path: "+
                        targetFolder.getAnyFolderPath());
          FetchPOIds fetchIds = new FetchPOIds(dbsLibrarySession);
          parentFolder = fetchIds.getFolderByName(targetFolder,folderName,false);
          if( parentFolder == null ){
            parentFolder = dbsFs.createFolder(folderName,targetFolder,true,null);
          }
          parentFolder.addItem(dbsDoc);
        }else{
          /* else, throw exception that managed folder not found */
          logger.error("Exception occurred in ReplaceVoucherBean... ");
          logger.error("Managed Folder Not Found");
          throw new Exception("Managed Folder Not Found");
          /*dbsFolder = dbsLibrarySession.getUser().getPrimaryUserProfile().getHomeFolder();
          logger.debug("dbsFolder Name : "+dbsFolder.getName());
          logger.debug("FolderName : "+folderName);        
          parentFolder = dbsFs.createFolder(folderName,dbsFolder,true,null);
          parentFolder.addItem(dbsDoc);*/
        }
        isToBeReplaced = false;
      }
    }catch (DbsException dbsEx) {
      logger.error("Exception occurred in ReplaceVoucherBean...");
      dbsEx.printStackTrace();
      logger.error(dbsEx.toString());
    }catch(Exception e){
      logger.error("Exception occurred in ReplaceVoucherBean...");
      e.printStackTrace();
      logger.error(e.toString());
    }finally{
      /* close the content input stream */
      if(fis !=null){
        try{
          fis.close();
          fis = null;
        }catch(Exception e){
          logger.error("Exception occurred in ReplaceVoucherBean...");
          logger.error(e.toString());
        }
      }
    }
    return isToBeReplaced;
  }

  /**
   * Function to get parent folder object.
   * Should be called only after replaceImage() has been executed
   * @return DbsFolder
   */
  public DbsFolder getParentFolder(){
    return this.parentFolder;
  }

  /**
   * Function to get document id.
   * Should be called only after replaceImage() has been executed.
   * @return String documentId
   */
  public String getDocumentId(){
    return this.documentId;
  }

  /**
   * Function called to obtain link for the given dummy document created
   * @throws java.lang.Exception
   * @throws java.io.IOException
   * @throws java.io.UnsupportedEncodingException
   * @throws dms.beans.DbsException
   * @return ArrayList containing document link, document name and document id. 
   * @param userPassword
   * @param userID
   * @param requestURI
   * @param contextPath
   * @param isToBeReplaced
   * @param actionName
   */
  public ArrayList getLink( boolean isToBeReplaced ,String contextPath,
        String requestURI,String userID,String userPassword,String actionName) 
        throws DbsException,UnsupportedEncodingException,IOException,Exception {
    String link = null;
    String relativePath = null;
    String cryptoPassword=new String();
    String encryptedString=new String();
    ArrayList DocLinkLists= null;
    FolderDocList folderDocList = null;
    
    try {
      if( !isToBeReplaced ){
        CryptographicUtil crypto = new CryptographicUtil();
        String keyPath=crypto.getSystemKeyStorePath(contextPath);
        //logger.debug("keyPath: "+keyPath);
        String pwdFilePath=crypto.getPwdFilePath(contextPath);
        //logger.debug("pwdFilePath: "+pwdFilePath);
        
        //logger.debug("keyPath and password file paths obtained.");
        
        relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+
                      File.separator+"GeneralActionParam.xml";
    
        ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            

        /*before link generation, check for the existence of .password & .keyStore */
        /*failure, throw an Exception */
        if(!crypto.getSystemKeyStoreFile(contextPath)){
          Exception fnfEx = 
            new Exception("Problem in link generation.Kindly contact your System Administrator.");
          throw fnfEx;
        }else{
          /*else go ahead to encrypt the userpassword */
          /*obtain cryptoPassword from .password, a byte[]Stream from userId,
           * userpassword,docId and encrypt using crypto.encryptString()*/
          cryptoPassword=crypto.getCryptoPassword(pwdFilePath);
          String toBeEncrypted="userId="+userID+"&password="+userPassword+
                               "&documentId="+dbsLibrarySession.getPublicObject(
                               new Long(documentId)).getResolvedPublicObject().getId();

          byte[] byteArrayToEncrypt= toBeEncrypted.getBytes();

          ByteArrayInputStream inStream= new ByteArrayInputStream(
                                                            byteArrayToEncrypt);
          logger.debug("inStream: "+inStream.getClass());
          ByteArrayInputStream enciStream=(ByteArrayInputStream)
                crypto.encryptString(inStream,userID,cryptoPassword,contextPath);
          int available=enciStream.available();
          logger.debug("available: "+available);
          byte[] byteArrayEncrypted=new byte[available];
          int byteCount=enciStream.read(byteArrayEncrypted,0,available);
  
          enciStream.close();
  
          /*if streamreading has been successful, encode the byte[]Encrypted to  
            encryptedString using Base64 Encoder and then URLEncode the latter */
          if(byteCount == available){
            encryptedString = new sun.misc.BASE64Encoder().encode(
                                                            byteArrayEncrypted);
            logger.debug("Base64Enc "+ encryptedString);
            String encodedStr=URLEncoder.encode(encryptedString,"UTF-8");
            /* construct linkString using value of <redirector> tag from 
             * "GeneralActionParam.xml" */
            String linkString= parseUtil.getValue("redirector","Configuration")+
                               requestURI.replaceAll(actionName,
                                                     "showContentAction");
            logger.debug("linkString: "+linkString);
            DocLinkLists = new ArrayList();
            folderDocList= new FolderDocList();          
      
            DbsPublicObject dbsPublicObject= 
                        dbsLibrarySession.getPublicObject(new Long(documentId));
            DbsDocument dbsDoc = 
                        (DbsDocument)dbsPublicObject.getResolvedPublicObject();
            String extension = dbsDoc.getName().substring(
                                dbsDoc.getName().lastIndexOf("."),
                                dbsDoc.getName().length());
            dbsDoc.setName(voucherId+extension);  
            logger.debug("dbsDoc.getId: "+dbsDoc.getId());
            logger.debug("dbsDoc.getName: "+dbsDoc.getName());

            folderDocList.setId(dbsDoc.getId());
            folderDocList.setName(dbsDoc.getName());
  
            String linkGenerated =linkString+"?auth="+encodedStr;
            logger.debug("linkGenerated: "+linkGenerated);
            folderDocList.setTxtLinkGenerated(linkGenerated);
            DocLinkLists.add(folderDocList);

          }else{
            /*if stream reading has been unsuccessful,throw an exception */
            //target=new String("failure");
            Exception srEx = new Exception("Problem in reading stream.");
            throw srEx;
            //saveErrors(request,errors);
          }
        }
        
      }
    }catch (DbsException dbsEx) {
      logger.error("Exception occurred in ReplaceVoucherBean...");
      logger.error(dbsEx.toString());
      throw dbsEx;
    }catch (UnsupportedEncodingException uEnEx) {
      logger.error("Exception occurred in ReplaceVoucherBean...");
      logger.error(uEnEx.toString());
      throw uEnEx;
    }catch (IOException ioEx) {
      logger.error("Exception occurred in ReplaceVoucherBean...");
      logger.error(ioEx.toString());
      throw ioEx;
    }catch (Exception ex) {
      logger.error("Exception occurred in ReplaceVoucherBean...");
      logger.error(ex.toString());
      throw ex;
    }
    return DocLinkLists;
  }
}