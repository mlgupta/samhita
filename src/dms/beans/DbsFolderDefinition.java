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
 * $Id: DbsFolderDefinition.java,v 20040220.4 2006/02/28 11:54:49 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/
import oracle.ifs.beans.FolderDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of AccessLevel class provided by 
 *           CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 *	Last Modified Date:    
 */
public class DbsFolderDefinition extends DbsPublicObjectDefinition {
    private FolderDefinition folderDefinition=null; // to accept object of type AccessLevel
    
    /**
	   * Purpose : To create DbsFolderDefinition using FolderDefinition class
	   * @param  : dbsSession - current LibrarySession
	   */
    public DbsFolderDefinition(DbsLibrarySession dbsSession)throws DbsException {
        super(dbsSession);
        try{
            this.folderDefinition=new FolderDefinition(dbsSession.getLibrarySession());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
	   * Purpose : Used to get the object of class FolderDefinition
	   * @return : FolderDefinition Object
	   */
    FolderDefinition getFolderDefinition(){
        return this.folderDefinition;
    }
}
