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
 * $Id: DbsFreeFormQualification.java,v 20040220.7 2006/02/28 07:09:05 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/*CMSDK API*/ 
import oracle.ifs.common.IfsException;
import oracle.ifs.search.FreeFormQualification;
import oracle.ifs.search.SearchQualification;
/**
 *	Purpose: To encapsulate the functionality of FreeFormQualification class 
 *           provided by CMSDK API.
 * 
 *  @author            Rajan Kamal Gupta
 *  @version           1.0
 * 	Date of creation:  24-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class DbsFreeFormQualification extends DbsSearchQualification {
  // member variable to accept object of type FreeFormQualification
  private FreeFormQualification freeFormQualification=null; 

  /**
   * Purpose: Constructor for DbsFreeFormQualification Class
   * @throws dms.beans.DbsException
   */
  public DbsFreeFormQualification() throws DbsException{
    try{
      this.freeFormQualification=new FreeFormQualification();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose : Constructor for DbsFreeFormQualification using 
   *           FreeFormQualification class
   * @param  : freeFormQualification - An FreeFormQualification Object  
   */    
  public DbsFreeFormQualification(FreeFormQualification freeFormQualification) {
    super();
    this.freeFormQualification = freeFormQualification;
  }

  /**
   * Purpose  : Used to get the object of class FreeFormQualification
   * @returns : FreeFormQualification Object
   */
  public FreeFormQualification getFreeFormQualification() {
    return this.freeFormQualification;
  }

  /**
   * Purpose  : Gets the SQL expression of this object.
   * @returns : SQL expression of this object.
   */
  public java.lang.String getSQLExpression() throws DbsException{
    try{
      return this.freeFormQualification.getSQLExpression();
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }

  /**
   * Purpose  : Sets the SQL expression for this object.
   * @returns : SQL - expression for this qualification
   */
  public void setSqlExpression(java.lang.String sqlExpression) throws DbsException{
    try{
      freeFormQualification.setSqlExpression(sqlExpression);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }    
  }

  /**
   * Purpose: get the SearchQualification object
   * @return SearchQualification object
   */
  public SearchQualification getSearchQualification() {
    return this.freeFormQualification;
  }
}
