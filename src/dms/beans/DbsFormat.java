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
 * $Id: DbsFormat.java,v 20040220.9 2006/02/28 11:54:49 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/
import oracle.ifs.beans.Format;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of Format class provided by CMSDK 
 *           API.
 * 
 *  @author            Jeetendra Prasad   
 *  @version           1.0
 * 	Date of creation:  05-01-2004
 * 	Last Modified by :  Suved Mishra   
 *	Last Modified Date: 28-02-2006   
 */

public class DbsFormat extends DbsSystemObject {
  /* member variable to accept object of type Format */
  private Format format = null;  
  /* The class name for this class. */
  public static java.lang.String CLASS_NAME = Format.CLASS_NAME ;
  /* The name for this class instance. */
  public static java.lang.String NAME_ATTRIBUTE = Format.NAME_ATTRIBUTE; 
  /* The mime type for this class instance. */
  public static java.lang.String MIMETYPE_ATTRIBUTE = Format.MIMETYPE_ATTRIBUTE; 

  /**
   * Purpose : Constructor to create DbsFormat using Format class
   * @param  : format - An Format Object  
   */
  public DbsFormat(Format format) {
    super(format);
    this.format = format;
  }

  /**
   * Purpose : to get MimeType for the given document
   * @return : MimeType as String
   */
  public String getMimeType() throws DbsException {
    String mimeType = null;
    try{
      mimeType = format.getMimeType();
    }catch(IfsException iex){
      throw new DbsException(iex);
    }
    return mimeType;
  }

  /**
   * Purpose : set MimeType for the given document 
   * @throws dms.beans.DbsException
   * @param mimeType
   */
  public void setMimeType(java.lang.String mimeType) throws DbsException{
    try{
      format.setMimeType(mimeType); 
    }catch(IfsException iex){
      throw new DbsException(iex);
    }
  }
  
  /**
   * Purpose : get the format for the given document 
   * @return Format object
   */
  public Format getFormat(){
    return format;
  }

}
