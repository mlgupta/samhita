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
 * $Id: DbsValueDefaultDefinition.java,v 20040220.3 2006/02/28 11:57:28 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/* CMSDK API*/
import oracle.ifs.beans.ValueDefaultDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of ValueDefaultDefinition  class 
 *           provided by CMSDK API.
 * 
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:    
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsValueDefaultDefinition extends DbsSchemaObjectDefinition  {

    private ValueDefaultDefinition valueDefaultDefinition; // to accept object of type ValueDefaultDefinition 

    /**
     * Purpose : To create DbsValueDefaultDefinition using ValueDefaultDefinition class
     * @param  : valueDefaultDefinition - A ValueDefaultDefinition Object  
     * @throws : DbsException - if operation fails
     */
    public DbsValueDefaultDefinition(DbsLibrarySession dbsLibrarySession) throws DbsException {
        super(dbsLibrarySession);
        try{
            this.valueDefaultDefinition = new ValueDefaultDefinition(dbsLibrarySession.getLibrarySession());      
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
     * Purpose : To set the Attribute upper case value
     * @param  : name - String
     * @param  : dbsAttributeValue - DbsAttributeValue
     * @throws : DbsException - if operation fails
     */
    public void setAttributeByUpperCaseName(String name, DbsAttributeValue dbsAttributeValue) throws DbsException{
        try{
            this.valueDefaultDefinition.setAttributeByUpperCaseName(name,dbsAttributeValue.getAttributeValue());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }      
    }

    /**
     * Purpose  : Used to get the object of class ValueDefaultDefinition
     * @returns : ValueDefaultDefinition Object
     */
    public ValueDefaultDefinition getValueDefaultDefinition(){
        return this.valueDefaultDefinition;
    }
}
