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
 * $Id: DbsSchemaObject.java,v 20040220.4 2006/02/28 11:56:39 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.SchemaObject;
/**
 *	Purpose: To encapsulate the functionality of SchemaObject class provided by 
 *           CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsSchemaObject extends DbsLibraryObject{
    SchemaObject schemaObject=null; // to accept object of type SchemaObject

    /**
	 * Purpose : To create DbsSchemaObject using SchemaObject class
	 * @param  : schemaObject - An SchemaObject Object  
	 */    
    public DbsSchemaObject(SchemaObject schemaObject) {
        super(schemaObject);
        this.schemaObject=schemaObject;
    }
   /**
	 * Purpose  : Used to get the object of class SchemaObject
	 * @returns : SchemaObject Object
	 */
    public SchemaObject getSchemaObject() {
        return this.schemaObject ;
    }
}
