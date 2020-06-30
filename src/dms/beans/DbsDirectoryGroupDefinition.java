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
 * $Id: DbsDirectoryGroupDefinition.java,v 20040220.4 2006/02/28 11:51:13 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.DirectoryGroupDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of DirectoryGroupDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsDirectoryGroupDefinition extends DbsDirectoryObjectDefinition{
    private DirectoryGroupDefinition directoryGroupDef = null;  // to accept object of type DirectoryGroupDefinition

    /**
	   * Purpose  : Used to get the object of class DirectoryGroupDefinition
	   * @param   : dbsSession - Constructs a DirectoryGroupDefinition explicitly capturing the session.
     * @throws  : DbsException - if operation fails.
	   */
    public DbsDirectoryGroupDefinition(DbsLibrarySession dbsSession) throws DbsException {
        super(dbsSession);
        try {
            this.directoryGroupDef = new DirectoryGroupDefinition(dbsSession.getLibrarySession());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
	   * Purpose  : Set an attribute for the new instance. Each invocation of this method results in another attribute setting being added to an internal list.
	   * @param   : name - The attribute name
	   * @param   : dbsAttrValue - The attribute value.
     * @throws  : DbsException - if operation fails.
	   */
    public void setAttribute(String name, DbsAttributeValue dbsAttrValue)throws DbsException{
        try{
            this.directoryGroupDef.setAttribute(name,dbsAttrValue.getAttributeValue());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
	   * Purpose  : Used to get the object of class DirectoryGroupDefinition
	   * @returns : DirectoryGroupDefinition Object
	   */
    DirectoryGroupDefinition getDirectoryGroupDefinition() {
        return this.directoryGroupDef ;
    }
}
