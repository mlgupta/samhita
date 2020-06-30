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
 * $Id: DbsDirectoryUserManager.java,v 20040220.5 2006/02/28 11:51:35 suved Exp $
 *****************************************************************************
 */
package dms.beans;  
/*CMSDK API*/ 
import oracle.ifs.beans.DirectoryUser;
import oracle.ifs.beans.DirectoryUserDefinition;
import oracle.ifs.beans.PrimaryUserProfile;
import oracle.ifs.beans.LibrarySession;
import oracle.ifs.common.IfsException;
/**
 *	Purpose: To encapsulate the functionality of DirectoryUserManager class 
 *           provided by CMSDK API.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class DbsDirectoryUserManager {
  // member variable for DirectoryUser object
  private DirectoryUser m_DirectoryUser;
  // member variable for DirectoryUserDefinition object
  private DirectoryUserDefinition m_DirectoryUserDefinition;

  public DbsDirectoryUserManager() throws IfsException {}

  /**
   * Constructor for DbsDirectoryUserManager
   * @throws oracle.ifs.common.IfsException
   * @param session
   */
  public DbsDirectoryUserManager(LibrarySession session)
      throws IfsException {
      m_DirectoryUserDefinition = new DirectoryUserDefinition(session);
  }

  /**
   * Purpose : gets all user members
   * @throws oracle.ifs.common.IfsException
   * @return DirectoryUser[] 
   */
  public DirectoryUser[] getAllUserMembers() throws IfsException {
      return m_DirectoryUser.getAllUserMembers();
  }

  /**
   * Purpose : gets user member at specified index
   * @throws oracle.ifs.common.IfsException
   * @return DirectoryUser
   * @param index
   */
  public DirectoryUser getAllUserMembers(int index) throws IfsException {
      return m_DirectoryUser.getAllUserMembers(index);
  }
  
  /**
   * Purpose : gets the name of credential manager as string
   * @throws oracle.ifs.common.IfsException
   * @return String CredentialManager name
   */
  public java.lang.String getCredentialManager() throws IfsException {
      return m_DirectoryUser.getCredentialManager();
  }

  /**
   * Purpose : gets the distinguished name
   * @throws oracle.ifs.common.IfsException
   * @return String distinguished name
   */
  public java.lang.String getDistinguishedName() throws IfsException {
      return m_DirectoryUser.getDistinguishedName();
  }

  /**
   * Purpose : gets the primary user profile of the given user
   * @throws oracle.ifs.common.IfsException
   * @return PrimaryUserProfile
   */
  public PrimaryUserProfile getPrimaryUserProfile() throws IfsException {
      return m_DirectoryUser.getPrimaryUserProfile();
  }

  /**
   * Purpose : to determine if the given user is an admin or not
   * @throws oracle.ifs.common.IfsException
   * @return boolean true if admin else false
   */
  public boolean isAdminEnabled() throws IfsException {
      return m_DirectoryUser.isAdminEnabled();
  }

  /**
   * Purpose : to determine if the given user is system admin or not 
   * @throws oracle.ifs.common.IfsException
   * @return boolean true if system admin else false
   */
  public boolean isSystemAdminEnabled() throws IfsException {
      return m_DirectoryUser.isSystemAdminEnabled();
  }

  /**
   * Purpose : set the credential manager
   * @throws oracle.ifs.common.IfsException
   * @param credentialManager
   */
  public void setCredentialManager(java.lang.String credentialManager)
          throws IfsException {
      m_DirectoryUser.setCredentialManager(credentialManager);
  }

  /**
   * Purpose : set distinguished name
   * @throws oracle.ifs.common.IfsException
   * @param name
   */
  public void setDistinguishedName(java.lang.String name)
          throws IfsException {
      m_DirectoryUser.setDistinguishedName(name);
  }

  /**
   * Purpose : set user for system admin previledges
   * @throws oracle.ifs.common.IfsException
   * @param value
   */
  public void setSystemAdminEnabled(boolean value) throws IfsException {
      m_DirectoryUser.setSystemAdminEnabled(value);
  }

  /**
   * Purpose : get the directory user
   * @return DirectoryUser object
   */
  public DirectoryUser getDirectoryUser() {
      return m_DirectoryUser;
  }

  /**
   * Purpose : set the directory user
   * @param v_DirectoryUser
   */
  public void setDirectoryUser(DirectoryUser v_DirectoryUser) {
      m_DirectoryUser = v_DirectoryUser;
  }

  /**
   * Purpose : set the directory user definition
   * @return 
   */
  public DirectoryUserDefinition getDirectoryUserDefinition() {
      return m_DirectoryUserDefinition;
  }

}
