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
 * $Id: DbsDocument.java,v 20040220.19 2006/02/28 11:52:18 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/* Java API */
import java.io.InputStream;
import java.io.Reader;
/*CMSDK API*/
import oracle.ifs.beans.Document;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of Document class provided by 
 *           CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 *	Last Modified Date:    
 */
public class DbsDocument extends DbsPublicObject {
  /* member variable to accept object of type Document */
  private Document document=null;  
  /* This class name for this class. */
  public static java.lang.String CLASS_NAME = Document.CLASS_NAME ;
  /* The ContentObject that contains this document's content. */
  public static final java.lang.String CONTENTOBJECT_ATTRIBUTE = 
                      Document.CONTENTOBJECT_ATTRIBUTE;
  /* The ContentQuota being charged for this document's content. */
  public static final java.lang.String CONTENTQUOTA_ATTRIBUTE = 
                      Document.CONTENTQUOTA_ATTRIBUTE;
  /* The Atribute specifying whether this document is encrypted or not. */
  public static final String ENCRYPTED = "ENCRYPTED";
  /* The Atribute specifying who has encrypted this document. */
  public static final String ENCRYPTED_BY = "ENCRYPTED_BY";
  /* The Atribute for keeping track of changes made to the document. */
  public static final String AUDIT_INFO = "AUDIT_INFO";
  /* The Atribute for keeping track of the workflow status of the document. */
  public static final String WORKFLOW_STATUS = "WORKFLOW_STATUS";

  /**
   * Purpose : Constructor to create DbsDocument using Document class
   * @param  : document - An Document Object  
   */
  public DbsDocument(Document document) {
    super(document);
    this.document=document;
  }
  
  /**
   * Purpose : Used to get the object of class Document
   * @return : Document Object
   */
  public Document getDocument() {
    return this.document;
  }

  /**
   * Purpose : Gets the size of this document's content. It returns 0 (zero) if 
   *           the content is zero length or if this document has a null 
   *           ContentObject
   * @return : the size of the underlying content.
   * @throws : DbsException - if operation fails
   */
  public long getContentSize() throws DbsException {
    try{
      return this.document.getContentSize();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Gets the format for this Document.
   * @return : the Format object.
   * @throws : DbsException - if operation fails
   */
  public DbsFormat getFormat() throws DbsException {
    try{
      if(document.getFormat() == null){
        return null;
      }else{
        return new DbsFormat(document.getFormat());
      }
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : to get inputstream of a document
   * @returns : the inputstream
   * @throws  : DbsException - if operation fails
   */
  public InputStream getContentStream() throws DbsException {
    InputStream inputStream = null;
    try{
      inputStream = document.getContentStream();
    }catch(IfsException iex){
      throw new DbsException(iex);
    }
    return inputStream;
  }

  /**
   * Purpose : Generates a HTML or plaintext version of the document, via the 
   *           Context INSO filters.
   * @throws : DbsException - if operation fails
   * @param  : boolean plaintext when TRUE, indicates that a plaintext version 
   *           of the document must be generated, otherwise an HTML version 
   *           will be generated
   */
  public void filterContent(boolean plaintext) throws DbsException{
    try{
      document.filterContent(plaintext);
    }catch(IfsException iex){
      throw new DbsException(iex);
    }
  }
   
 /**
  * Purpose : Gets a HTML or plaintext version of the document, via the 
  *            Context INSO filters.
  * @throws : DbsException - if operation fails
  */
  public Reader getFilteredContent() throws DbsException{
    Reader reader;
    try{
      reader = document.getFilteredContent();
    }catch(IfsException iex){
      throw new DbsException(iex);
    }
    return reader;
  }

  /**
   * Purpose : Sets the format for this Document.	  
   * @throws : DbsException - if operation fails
   */
  public void setFormat(DbsFormat dbsFormat) throws DbsException {
    try{
      document.setFormat(dbsFormat.getFormat());               
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Sets the content of the Document.	  
   * @throws : DbsException - if operation fails
   */
  public void setContent(DbsDocumentDefinition dbsDocumentDefinition) throws 
         DbsException {
    try{
      document.setContent(dbsDocumentDefinition.getDocumentDefinition());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }
}
