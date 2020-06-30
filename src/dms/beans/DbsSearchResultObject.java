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
 * $Id: DbsSearchResultObject.java,v 20040220.8 2006/02/28 11:56:39 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/* CMSDK API*/
import oracle.ifs.beans.Document;
import oracle.ifs.beans.Family;
import oracle.ifs.beans.Folder;
import oracle.ifs.beans.LibraryObject;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.beans.SearchResultObject;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of SearchResultObject class 
 *           provided by CMSDK API.
 * 
 * @author              Rajan Kamal Gupta
 * @version             1.0
 * 	Date of creation:   03-3-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsSearchResultObject {
    private SearchResultObject searchResultObject=null;// to accept object of type SearchResultObject

    /**
	   * Purpose : To create DbsSearchResultObject using SearchResultObject class
	   * @param  : SearchResultObject - An SearchResultObject Object  
	   */        
    public DbsSearchResultObject(SearchResultObject searchResultObject) {
       this.searchResultObject=searchResultObject;
    }

    /**
     * Purpose   : Returns the result object for the class. If an alias was used in the SearchClassSpecification, 
     *             it must be specified here.
     * @param    : dbsClassName - name of the class or alias of the class in the result
     * @returns  : LibraryObject if present; null otherwise.    
     */
    public DbsLibraryObject getLibraryObject(java.lang.String dbsClassName) throws DbsException{
        try{
            return new DbsLibraryObject(this.searchResultObject.getLibraryObject(dbsClassName));
        }catch(IfsException ifsError){
            throw new DbsException(ifsError);
        }   
    }

    /**
     * Purpose   : Returns the first object in the SearchResult.
     * @returns  : first object in SearchResult.
     */
    public DbsLibraryObject getLibraryObject() throws DbsException{
        try{
            LibraryObject lo=this.searchResultObject.getLibraryObject();
            if(lo instanceof Folder){
              return new DbsFolder((Folder)lo);              
            }else if(lo instanceof Document){
              return new DbsDocument((Document)lo);              
            }else if(lo instanceof Family){
              return new DbsFamily((Family)lo);              
            }else if(lo instanceof PublicObject){
              return new DbsPublicObject((PublicObject)lo);              
            }else{
              return new DbsLibraryObject(lo);
            }
        }catch(IfsException ifsError){
            throw new DbsException(ifsError);
        }   
    }

    /**
     * Purpose   : Returns the score associated with a ContextQualification. The ctxClauseName 
     *             should be the same as the name specified for the ContextQualification.
     * @param    : ctxClauseName - name of ContextQualification whose score is required.
     * @returns  : the interMedia score for this row of result.
     */
    public int getScore(java.lang.String ctxClauseName) throws DbsException{
        try{
            return this.searchResultObject.getScore(ctxClauseName);
        }catch(IfsException ifsError){
            throw new DbsException(ifsError);
        }   
    }
}
