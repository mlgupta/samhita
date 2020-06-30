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
 * $Id: ZipBean.java,v 20040220.20 2006/03/13 14:18:28 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.beans.wf.watch.InitiateWatch;
/* Java API */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose: Bean to inflate and deflate documents and folders. 
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class ZipBean {
    static final int BUFFER = 2048;
    DbsLibrarySession dbsLibrarySession;
    FolderDocInfo folderDocInfo;
    Logger logger;
   
    public ZipBean(DbsLibrarySession dbsLibrarySession,FolderDocInfo folderDocInfo){
        this.dbsLibrarySession = dbsLibrarySession;
        this.folderDocInfo = folderDocInfo;
        logger = Logger.getLogger("DbsLogger");
    }
   
    public void zipFile(Long documentIds[] , String zipFileName,String relativePath,String userName) throws DbsException,Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER);
        ByteArrayInputStream bais;
        int documentCount = documentIds.length;
        DbsPublicObject dbsPublicObjects[] = new DbsPublicObject[documentCount];
        String fileName;
        String folderPath;
        String currentFolderPath = folderDocInfo.getCurrentFolderPath();
        DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);        
        DbsFolder dbsFolder = null;
        DbsFolder dbsRootFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath("/");
        
        //check if the path specified is absolute or relative
        if(zipFileName.startsWith("/")){      //the path is absolute
            zipFileName = zipFileName;
        }else{      //the path is relative
            zipFileName = currentFolderPath + "/" + zipFileName;
        }
        
        folderPath = zipFileName.substring (0,zipFileName.lastIndexOf("/"));
        if(folderPath.equals("")){
            folderPath = "/";
        }
        fileName = zipFileName.substring(zipFileName.lastIndexOf("/") + 1);;        

        //check existance of a folder before executing create folder code
        try{
            dbsFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath(folderPath);
        }catch(DbsException dex){
           dbsFolder = null; 
        }
        if(dbsFolder == null){
            dbsFileSystem.createFolder(folderPath.replaceFirst("/",""),dbsRootFolder,true,null);
        }
        
        try{

            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(baos));
            for(int index = 0; index < documentCount; index++){
                dbsPublicObjects[index] = (dbsLibrarySession.getPublicObject(documentIds[index])).getResolvedPublicObject();
            }
            creatZip(dbsPublicObjects,zos,relativePath,userName);
            zos.close();
            bais = new ByteArrayInputStream(baos.toByteArray());
            dbsFileSystem.createDocument(fileName,bais,folderPath,null);
        } catch(Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            throw e;
        } catch(DbsException dex) {
            logger.error(dex.toString());
            dex.printStackTrace();
            throw dex;
        }
   }

   public void creatZip(DbsPublicObject[] dbsPublicObjects, ZipOutputStream zos,String relativePath,String userName) throws FileNotFoundException, IOException, DbsException{
        BufferedInputStream origin;
        DbsPublicObject dbsPublicObjectTemp;
        DbsDocument dbsDocumentTemp;
        InputStream is;
        String zipEntryName;
        ZipEntry entry;
        byte data[] = new byte[BUFFER];
        int listLength = dbsPublicObjects.length;
        String currentFolderPath = folderDocInfo.getCurrentFolderPath();
        if(!currentFolderPath.equals("/")){
            currentFolderPath = currentFolderPath + "/";
        }
        
        String filePath=new String();
        for(int index = 0; index < listLength; index++){
            if(dbsPublicObjects[index] instanceof DbsFolder){
                creatZip(((DbsFolder)(dbsPublicObjects[index])).getItems(), zos,relativePath,userName);
            }else{
                dbsPublicObjectTemp = dbsPublicObjects[index].getResolvedPublicObject();
                dbsDocumentTemp = (DbsDocument)dbsPublicObjectTemp.getResolvedPublicObject();
                is = dbsDocumentTemp.getContentStream();
                origin = new BufferedInputStream(is, BUFFER);
                if(!dbsPublicObjects[index].isVersioned()){
                  filePath = dbsPublicObjects[index].getAnyFolderPath();
                  logger.debug("filePath: "+filePath);                 
                  /* logic for logging event in "AUDIT_LOG" attribute */
                  DocEventLogBean logBean = new DocEventLogBean();
                  logBean.logEvent(dbsLibrarySession,dbsPublicObjects[index].getId(),"Zip File Generated");
                }else if(dbsPublicObjects[index].isVersioned()){
                  filePath = dbsPublicObjects[index].getFamily().getAnyFolderPath();
                  logger.debug("filePath: "+filePath);
                  /* logic for logging event in "AUDIT_LOG" attribute */
                  DocEventLogBean logBean = new DocEventLogBean();
                  logBean.logEvent(dbsLibrarySession,dbsPublicObjects[index].getFamily().getId(),"Zip File Generated");
                }
                // code for wf submission goes here.
                if( relativePath!=null && userName!=null){
                  String actionForWatch = new String();
                  Long poId = dbsPublicObjects[index].getId();
                  actionForWatch="You are receiving this mail because you had applied watch on "+dbsPublicObjects[index].getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Zip File Generated.";
                  InitiateWatch iniWatch = new InitiateWatch(relativePath,userName,
                                          actionForWatch,poId);
                  iniWatch.startWatchProcess();
                  actionForWatch = null;
                }
                
                zipEntryName = filePath.replaceFirst(currentFolderPath,"");
                entry = new ZipEntry(zipEntryName);
                logger.debug("Adding: "+ zipEntryName);
                zos.putNextEntry(entry);
                int count;
                while((count = origin.read(data, 0, BUFFER)) != -1) {
                   zos.write(data, 0, count);
                }
                zos.flush();
                origin.close();
            }
        }
   }

    public DbsDocument unzipFile(InputStream is, String extractToLocation) throws DbsException,Exception {
        try {
            String absFileName;
            String absExtractToPath;
            String fileName;
            String folderPath;

            ByteArrayOutputStream baos;
            BufferedOutputStream bos;
            ByteArrayInputStream bais;
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);        
            DbsFolder dbsFolder = null;
            DbsFolder dbsRootFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath("/");
            DbsDocument docToReturn = null;
            
            //check if the path specified is absolute or relative
            if(extractToLocation.startsWith("/")){      //the path is absolute
                if(extractToLocation.endsWith("/")){
                    absExtractToPath = extractToLocation;
                }else{
                    absExtractToPath = extractToLocation + "/" ;
                }
            }else{      //the path is relative
                if(extractToLocation.endsWith("/")){
                    absExtractToPath = folderDocInfo.getCurrentFolderPath() + "/" + extractToLocation;
                }else{
                    absExtractToPath = folderDocInfo.getCurrentFolderPath() + "/" + extractToLocation + "/" ;
                }
            }
            
            while((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                logger.debug("Extracting: " +entryName);
                int count;
                byte data[] = new byte[BUFFER];
                // write the files to the disk
                if(! entryName.endsWith("/")){
                    baos = new ByteArrayOutputStream(BUFFER);                
                    bos = new BufferedOutputStream(baos, BUFFER);

                    absFileName = absExtractToPath + entryName;
                    folderPath = absFileName.substring(0,absFileName.lastIndexOf("/"));
                    fileName = absFileName.substring(absFileName.lastIndexOf("/") + 1);
                    
                    //check existance of a folder before executing create folder code
                    try{
                        dbsFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath(folderPath);
                    }catch(DbsException dex){
                       dbsFolder = null; 
                    }
                    if(dbsFolder == null){
                        dbsFileSystem.createFolder(folderPath.replaceFirst("/",""),dbsRootFolder,true,null);
                    }

                    
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                       bos.write(data, 0, count);
                    }
                    
                    bos.flush();
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    docToReturn = dbsFileSystem.createDocument(fileName,bais,folderPath,null);
                    bos.close();
                }
            }
            zis.close();
            return docToReturn;    
        } catch(Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            throw e;
        } catch(DbsException dex) {
            logger.error(dex.toString());
            dex.printStackTrace();
            throw dex;
        }
        
    }

/*
package dms.web.actions.filesystem;
import java.io.*;
import java.util.zip.*;

public class Zip {
   static final int BUFFER = 2048;
   public static void main (String argv[]) {
      try {
         File fileZip = new File("/home/jeet/a.zip");
         fileZip.createNewFile();
         FileOutputStream dest = new FileOutputStream(fileZip);
         ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
         //out.setMethod(ZipOutputStream.DEFLATED);
         File f = new File("/home/jeet/temp");
         creatZip(f,out);
         out.close();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }

   public static void creatZip(File folder, ZipOutputStream out) throws FileNotFoundException, IOException{
       BufferedInputStream origin = null;
        File files[] = folder.listFiles();
        int listLength = files.length;
         byte data[] = new byte[BUFFER];

        for(int index = 0; index < listLength; index++){
            if(files[index].isDirectory()){
                creatZip(files[index], out);
            }else{
                FileInputStream fi = new FileInputStream(files[index]);
                origin = new BufferedInputStream(fi, BUFFER);
                String zipEntryName = files[index].getAbsolutePath().replaceFirst("/home/jeet/temp/","");
                ZipEntry entry = new ZipEntry(zipEntryName);
                System.out.println("Adding: "+ zipEntryName);
                out.putNextEntry(entry);
                int count;
                while((count = origin.read(data, 0, BUFFER)) != -1) {
                   out.write(data, 0, count);
                }
                origin.close();
            }
        }
   }
*/

}
