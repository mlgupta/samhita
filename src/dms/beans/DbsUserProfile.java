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
 * $Id: DbsUserProfile.java,v 20040220.5 2006/02/28 10:52:26 suved Exp $
 *****************************************************************************
 */
package dms.beans;
/*CMSDK API*/ 
import oracle.ifs.beans.UserProfile;
/**
 *	Purpose: To encapsulate the functionality of UserProfile class provided by 
 *           CMSDK API.
 * 
 *  @author              Maneesh Mishra
 *  @version             1.0
 * 	Date of creation:    25-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsUserProfile extends DbsPublicObject{
  // to accept object of type UserProfile
  UserProfile userProfile=null;
  
  /**
   *Purpose : Constructor for DbsUserProfile Class 
   * @param userProfile
   */
  public DbsUserProfile(UserProfile userProfile){
    super(userProfile);
    this.userProfile=userProfile;
  }
}
