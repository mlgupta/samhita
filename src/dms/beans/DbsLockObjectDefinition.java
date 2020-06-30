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
 * $Id: DbsLockObjectDefinition.java,v 20040220.4 2005/03/29 04:41:35 manish Exp $
 *****************************************************************************
 */
package dms.beans; 
/*CMSDK API*/ 
import oracle.ifs.beans.LockObject;
import oracle.ifs.beans.LockObjectDefinition;
import oracle.ifs.common.IfsException;
/**
 *	Purpose : To encapsulate the functionality of LockObjectDefinition class 
 *            provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 28-02-2006   
 */
public class DbsLockObjectDefinition {
  /* member variable to accept object of type LockObjectDefinition */
  private LockObjectDefinition lockObjectDefinition = null;  
  /* Represents a HardLock state. */
  public static int  LOCKSTATE_HARDLOCK = LockObject.LOCKSTATE_HARDLOCK; 
  /* Represents a NoLock state. */
  public static int  LOCKSTATE_UNLOCK= LockObject.LOCKSTATE_UNLOCK;
  /* Represents a SoftLock state. */
  public static int  LOCKSTATE_SOFTLOCK= LockObject.LOCKSTATE_SOFTLOCK;

  /**
   * Constructor for DbsLockObjectDefinition Class
   * @throws dms.beans.DbsException
   * @param dbsSession
   */
  public DbsLockObjectDefinition(DbsLibrarySession dbsSession)throws DbsException{
    try{
      this.lockObjectDefinition=new LockObjectDefinition(
                                    dbsSession.getLibrarySession());
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose : To create DbsLockObjectDefinition using LockObjectDefinition class
   * @param  : lockObjectDefinition - An LockObjectDefinition Object  
   */    
  public DbsLockObjectDefinition(LockObjectDefinition lockObjectDefinition) {
    this.lockObjectDefinition = lockObjectDefinition;
  }

  /**
   * Purpose : Set the LockState for the LockObject
   * @throws dms.beans.DbsException
   * @param lockstate
   */
  public void setLockState(int lockstate)throws DbsException{
    try{  
      this.lockObjectDefinition.setLockState(lockstate);
    }catch(IfsException ifsError) {
      throw new DbsException(ifsError);
    }
  }
  
  /**
   * Purpose : get the value for the lockstate of this lock
   * @throws dms.beans.DbsException
   * @return integer lockstate
   */
  public int getLockState()throws DbsException{
    int lockstate;
    try{
      lockstate=this.lockObjectDefinition.getLockState();
    }catch(IfsException ifsError){
      throw new DbsException(ifsError);
    }
    return lockstate;
  }
 
 /**
  * Purpose  : Used to get the object of class LockObjectDefinition
  * @returns : LockObjectDefinition Object
  */
  public LockObjectDefinition getLockObjectDefinition() {
    return this.lockObjectDefinition ;
  }
}
