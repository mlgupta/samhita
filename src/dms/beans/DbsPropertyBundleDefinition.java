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
 * $Id: DbsPropertyBundleDefinition.java,v 20040220.5 2006/02/28 11:55:47 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/* CMSDK API*/
import oracle.ifs.beans.PropertyBundleDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of PropertyBundleDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    20-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsPropertyBundleDefinition extends DbsPublicObjectDefinition {

    private PropertyBundleDefinition propertyBundleDef = null;  // to accept object of type PropertyBundleDefinition

    /**
     * Purpose  : Used to get the object of class PropertyBundleDefinition
	   * @param   : dbsSession - Constructs a PropertyBundleDefinition explicitly capturing the session.
     * @throws  : DbsException - if operation fails
	   */
    public DbsPropertyBundleDefinition(DbsLibrarySession dbsSession) throws DbsException {
        super(dbsSession);
        try {            
            this.propertyBundleDef = new PropertyBundleDefinition(dbsSession.getLibrarySession());  
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }
    
    /**
     * Purpose  : Adds a Property Value to the new PropertyBundle. This results in the creation of a PropertyDefinition, 
     *            which gets added to the list of Properties to create.
	   * @param   : dbsAttributeValue - the DbsAttributeValue representing the Property value
     * @throws  : DbsException - if operation fails
	   */
    public void addPropertyValue(DbsAttributeValue dbsAttributeValue) throws DbsException {
        try{
            this.propertyBundleDef.addPropertyValue(dbsAttributeValue.getAttributeValue());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }
    }

    /**
     * Purpose  : Adds a Property Value to the new PropertyBundle. This results in the creation of a PropertyDefinition, 
     *            which gets added to the list of Properties to create.
	   * @param   : name - String
	   * @param   : dbsAttributeValue - the DbsAttributeValue representing the Property value
     * @throws  : DbsException - if operation fails
	   */
    public void addPropertyValue(java.lang.String name,DbsAttributeValue dbsAttributeValue) throws DbsException {
        try{
            this.propertyBundleDef.addPropertyValue(name, dbsAttributeValue.getAttributeValue());
        }catch(IfsException ifsError) {
            throw new DbsException(ifsError);
        }        
    }

  /**
	 * Purpose  : Used to get the object of class PropertyBundleDefinition
	 * @returns : PropertyBundleDefinition Object
	 */
    PropertyBundleDefinition getPropertyBundleDefinition() {
        return this.propertyBundleDef ;
    }
}
