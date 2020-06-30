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
 * $Id: DbsApplicationObject.java,v 20040220.5 2006/02/28 11:50:09 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.ApplicationObject;
/**
 *	Purpose: To encapsulate the functionality of ApplicationObject class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsApplicationObject extends DbsPublicObject{
    ApplicationObject applicationObject=null; // to accept object of type ApplicationObject

    /**
     * Purpose : To create DbsApplicationObject using ApplicationObject class
     * @param  : applicationObject - An ApplicationObject Object  
	   */
    public DbsApplicationObject(ApplicationObject applicationObject) {
        super(applicationObject);
        this.applicationObject=applicationObject;
    }

    /**
	   * Purpose  : Used to get the object of class ApplicationObject
	   * @returns : ApplicationObject Object
	   */
    public ApplicationObject getApplicationObject() {
        return this.applicationObject ;
    }
}
