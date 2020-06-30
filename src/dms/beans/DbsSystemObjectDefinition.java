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
 * $Id: DbsSystemObjectDefinition.java,v 20040220.5 2006/02/28 11:56:57 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.SystemObjectDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of SystemObjectDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsSystemObjectDefinition extends DbsLibraryObjectDefinition{

    private SystemObjectDefinition systemObjectDef = null;// to accept object of type SystemObjectDefinition

    /**
	   * Purpose : To create DbsSystemObjectDefinition using SystemObjectDefinition class
	   * @param  : dbssession - the session
	   */
    public DbsSystemObjectDefinition(DbsLibrarySession dbsSession) throws DbsException {
        super(dbsSession);
        try{
            this.systemObjectDef = new SystemObjectDefinition(dbsSession.getLibrarySession());  
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

   /**
	  * Purpose  : Used to get the object of class SystemObjectDefinition
	  * @returns : SystemObjectDefinition Object
	  */
    public SystemObjectDefinition getSystemObjectDefinition() {
        return this.systemObjectDef ;
    }
}
