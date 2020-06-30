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
 * $Id: ListManagedFoldersBean.java,v 1.4 2006/02/02 12:30:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;

/**
 * Purpose            : Bean to store details for each managed folder.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class ListManagedFoldersBean  {
  private String accessId;              /* access id for a given managed folder */
  private String managedFolderPath;     /* folderPath for a given managed folder */ 
  private String trimAccessId;          /* trim value of accessId for display */ 
  private String trimManagedFolderPath; /* trim value of folderPath for display */

  /**
   * Constructor for ListManagedFoldersBean Class
   */
  public ListManagedFoldersBean() {
  }

  /**
   * getter method for accessId
   * @return String accessId
   */
  public String getAccessId() {
    return accessId;
  }

  /**
   * setter method for accessId
   * @param accessId
   */
  public void setAccessId(String accessId) {
    this.accessId = accessId;
  }

  /**
   * getter method for managedFolderPath 
   * @return String managedFolderPath
   */
  public String getManagedFolderPath() {
    return managedFolderPath;
  }

  /**
   * setter method for managedFolderPath
   * @param managedFolderPath
   */
  public void setManagedFolderPath(String managedFolderPath) {
    this.managedFolderPath = managedFolderPath;
  }

  /**
   * getter method for trimAccessId
   * @return String trimAccessId
   */
  public String getTrimAccessId() {
    return trimAccessId;
  }

  /**
   * setter method for trimAccessId
   * @param trimAccessId
   */
  public void setTrimAccessId(String trimAccessId) {
    this.trimAccessId = trimAccessId;
  }

  /**
   * getter method for trimManagedFolderPath
   * @return String trimManagedFolderPath
   */
  public String getTrimManagedFolderPath() {
    return trimManagedFolderPath;
  }

  /**
   * setter method for trimManagedFolderPath
   * @param trimManagedFolderPath
   */
  public void setTrimManagedFolderPath(String trimManagedFolderPath) {
    this.trimManagedFolderPath = trimManagedFolderPath;
  }
}