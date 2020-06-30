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
 * $Id: DbsSystemObject.java,v 20040220.6 2006/03/17 12:16:56 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.SystemObject;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of SystemObject class provided by 
 *           CMSDK API.
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsSystemObject extends DbsLibraryObject{
    SystemObject systemObject=null; // to accept object of type SystemObject

    /**
  	 * Purpose : To create DbsSystemObject using SystemObject class
	   * @param  : systemObject - An SystemObject Object  
	   */
    public DbsSystemObject(SystemObject systemObject) {
        super(systemObject);
        this.systemObject=systemObject;
    }

   /**
	  * Purpose  : Used to get the object of class SystemObject
	  * @returns : SystemObject Object
	  */
    public SystemObject getSystemObject() {
        return this.systemObject ;
    }
    
  /**
   * Purpose  : Used to remove Property from the property bundle
   * @throws dms.beans.DbsException
   * @param name
   */
    public void removeProperty(java.lang.String name) throws DbsException{
        try{
            this.systemObject.removeProperty(name);
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }
}