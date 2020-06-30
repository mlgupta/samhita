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
 * $Id: DbsContentQuotaDefinition.java,v 20040220.4 2006/02/28 11:51:13 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.ContentQuotaDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of ContentQuotaDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsContentQuotaDefinition extends DbsApplicationObjectDefinition {
    private ContentQuotaDefinition contentQuotaDef = null;  // to accept object of type ContentQuotaDefinition
       
    /**
     * Purpose  : Used to get the object of class ContentQuotaDefinition
     * @param   : dbsSession - Constructs a ContentQuotaDefinition explicitly capturing the session.
     */
    public DbsContentQuotaDefinition(DbsLibrarySession dbsSession) throws DbsException {
        super(dbsSession);
        try {            
            this.contentQuotaDef = new ContentQuotaDefinition(dbsSession.getLibrarySession());  
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
	   * Purpose  : Used to get the object of class ContentQuotaDefinition
	   * @returns : ContentQuotaDefinition Object
	   */
    public ContentQuotaDefinition getContentQuotaDefinition() {
        return this.contentQuotaDef ;
    }
}
