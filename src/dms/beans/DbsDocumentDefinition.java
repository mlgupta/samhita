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
 * $Id: DbsDocumentDefinition.java,v 20040220.2 2006/01/25 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/
import oracle.ifs.beans.ContentObjectDefinition;
import oracle.ifs.beans.DocumentDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of DbsDocumentDefinition class 
 *           provided by CMSDK API.
 * 
 *  @author           Suved Mishra
 *  @version          1.0
 * 	Date of creation: 25-01-2006
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class DbsDocumentDefinition extends DbsPublicObjectDefinition {
  // private member variable for accessing DocumentDefinition
  private DocumentDefinition documentDefinition=null;  
  
  /**
   * Purpose : Constructor for DbsDocumentDefinition Class
   * @throws : dms.beans.DbsException
   * @param  : dbsLibrarySession - A DbsLibrarySession Object
   */
  public DbsDocumentDefinition(DbsLibrarySession dbsLibrarySession) 
         throws DbsException {
    super(dbsLibrarySession);
    try{
      this.documentDefinition= new DocumentDefinition(
                                   dbsLibrarySession.getLibrarySession());
    }catch(IfsException e){
      throw new DbsException(e);
    }
  }

  /**
   * Purpose : Used to get the object of class DocumentDefinition
   * @return : DocumentDefinition Object
   */
  public DocumentDefinition getDocumentDefinition(){
    return this.documentDefinition;
  }
       
  /**
   * Purpose:Sets the content of the document that will be loaded/created.
   * @throws dms.beans.DbsException
   * @param content
   */
  public void setContent(java.lang.String content)throws DbsException{
    try{
      this.documentDefinition.setContent(content);
    }catch(IfsException ifsException) {
      throw new DbsException(ifsException);
    }    
  }

  /**
   * Purpose : Sets the Content for this DocumentDefinition.	  
   * @throws dms.beans.DbsException
   * @param contentStream
   */
  public void setContentStream(java.io.InputStream contentStream) 
        throws DbsException {
    try{
      this.documentDefinition.setContentStream(contentStream);
    }catch(IfsException ifsException) {
      throw new DbsException(ifsException);
    }
  }
  
  /**
   * Purpose : Sets the ContentObjectDefinition of the document that will be 
   *           used to create/update the content object associated with this doc.
   * @throws dms.beans.DbsException
   * @param contentStream
   */
  public void setContentObjectDefinition(ContentObjectDefinition 
              contentObjectDefinition) throws DbsException {
    try{
      documentDefinition.setContentObjectDefinition(contentObjectDefinition);
    }catch(IfsException ifsException) {
      throw new DbsException(ifsException);
    }
  }
  
  /**
   * Purpose:Set the ClassObject for the new instance.
   * @throws dms.beans.DbsException
   * @param dbsClassObject
   */
  public void setClassObject(DbsClassObject dbsClassObject)throws DbsException{
    try{
      this.documentDefinition.setClassObject(dbsClassObject.getClassObject());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }      
  }
  
  /**
   * Purpose:Sets the format describing the type of content.
   * @throws dms.beans.DbsException
   * @param dbsFormat
   */
  public void setFormat(DbsFormat dbsFormat) throws DbsException{
    try{
      this.documentDefinition.setFormat(dbsFormat.getFormat());
    }catch( IfsException ifsError ){
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose:Set an attribute for the new instance.
   * @throws dms.beans.DbsException
   * @param dbsAttrValue
   * @param name
   */
  public void setAttribute(String name, DbsAttributeValue dbsAttrValue)
         throws DbsException{
    try{
      this.documentDefinition.setAttribute(name,dbsAttrValue.getAttributeValue());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

}