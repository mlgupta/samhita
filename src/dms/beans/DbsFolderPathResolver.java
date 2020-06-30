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
 * $Id: DbsFolderPathResolver.java,v 20040220.4 2006/02/28 11:54:49 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.Document;
import oracle.ifs.beans.Folder;
import oracle.ifs.beans.FolderPathResolver;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of FolderPathResolver class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:    23-12-2003
 * 	Last Modified by :     
 *	Last Modified Date:    
 */
public class DbsFolderPathResolver {
    private FolderPathResolver folderPathResolver=null; // to accept object of type FolderPathResolver

    /**
     * Purpose   : To create DbsFolderPathResolver using FolderPathResolver class
     * @param    : dbsSession - the session
     * @throws   : DbsException - if operation fails.
     */
    public DbsFolderPathResolver(DbsLibrarySession dbsSession) throws DbsException {         
        try{
            this.folderPathResolver=new FolderPathResolver(dbsSession.getLibrarySession());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
     * Purpose   : To create DbsFolderPathResolver using FolderPathResolver class
     * @param    : dbsSession - the session
     * @throws   : DbsException - if operation fails.
     */
    public DbsPublicObject findPublicObjectByPath(java.lang.String path)throws DbsException{
        DbsPublicObject dbsPublicObject=null;
        try{       
            PublicObject publicObject=folderPathResolver.findPublicObjectByPath(path);
         if(publicObject instanceof Folder){
            DbsFolder dbsFolder=new DbsFolder((Folder)publicObject);
            return dbsFolder;
         }else if(publicObject instanceof Document){
            DbsDocument dbsDocument=new DbsDocument((Document)publicObject);
            return dbsDocument;
         }
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
        return dbsPublicObject;
    }

    /**
     * Purpose   : Change current folder to the Root Folder
     * @throws   : DbsException - if operation fails.
     */
    public void setRootfolder() throws DbsException {
        try{
            folderPathResolver.setRootFolder();
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
	   * Purpose : Used to get the object of class FolderPathResolver
	   * @return : FolderPathResolver Object
	   */
    FolderPathResolver getFolderPathResolver() {
        return this.folderPathResolver;
    }
}
