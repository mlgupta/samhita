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
 * $Id: DbsException.java,v 20040220.9 2006/02/28 11:54:10 suved Exp $
 *****************************************************************************
 */
package dms.beans; 
import oracle.ifs.common.IfsException;
/**
 *	Purpose:  To encapsulate the functionality of IfsException class provided by
 *            CMSDK API.
 * 
 *  @author            Maneesh Mishra
 *  @version           1.0
 * 	Date of creation:  23-12-2003
 * 	Last Modified by :     
 *	Last Modified Date:    
 */

public class DbsException extends Throwable {

  private IfsException ifsException=null;
  private String messageKey;
  private String[] replacementValues;

  /**
   * Purpose : To create DbsException using IfsException class
   * @param  : iex - An IfsException Object  
   */    
  public DbsException(IfsException iex) {
    this.ifsException = iex;
  }

  /**
   * Purpose  : Returns the Error Code.
   * @returns : ErrorCode - int 
   */
  public int getErrorCode() {
    return ifsException.getErrorCode();
  }

  /**
   * Purpose  : Returns the Error Message.
   * @returns : ErrorCode - String 
   */
  public String getMessage() {
    return ifsException.getMessage();
  }

  /**
   * Purpose : Returns the Error Message.
   * returns : ErrorMessage - String
   */
  public String  getErrorMessage(){
    String errorMessage="An Error Has Occured : "+ ifsException.getMessage();
    ifsException.printStackTrace();
    return errorMessage;
  }

  /**
   * Purpose : Returns the Contains Error Code.
   * @param  : errorCode - int
   */
  public boolean containsErrorCode(int errorCode){
    return this.ifsException.containsErrorCode(errorCode);
  }

  /**
   * Purpose : Returns the Message Key.
   */
  public String getMessageKey() {
    return messageKey;
  }

  /**
   * Purpose : Sets the message key
   * @param  : newMessageKey
   */
  public void setMessageKey(String newMessageKey) {
    messageKey = newMessageKey;
  }

  /**
   * Purpose : Sets the message keys
   * @param  : newMessageKey
   * @param  : values -  An Array
   */
  public void setMessageKey(String newMessageKey,String[] values) {
    messageKey = newMessageKey;
    replacementValues = values;
  }

  /**
   * Purpose   : Returns an array of the replacement values.
   * @returns  : replacementValues - An String array
   */
  public String[] getReplacementValues() {
    return replacementValues;
  }

  /**
   * Purpose : Sets the ReplacementValues
   * @param  : newReplacementValues
   */
  public void setReplacementValues(String[] newReplacementValues) {
    replacementValues = newReplacementValues;
  }
}
