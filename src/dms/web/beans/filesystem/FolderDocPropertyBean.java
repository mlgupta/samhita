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
 * $Id: FolderDocPropertyBean.java,v 20040220.8 2006/03/14 07:19:41 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose: Bean to display property of folder(s) and/or document(s)
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 14-03-2006   
 */
public class FolderDocPropertyBean {
    private String folderDocType;         /* type of item */
    private String folderDocDescription;  /* description of item */
    private String docWorkFlowStatus;     /* doc workflow status */
    private String folderDocLocation;     /* location of item */
    private String folderDocSize;         /* size of item */
    private String createdDate;           /* item creation date */
    private String createdBy;             /* creator of item */
    private String modifiedDate;          /* item modification date */
    private String modifiedBy;            /* item modified by */
    private String aclName;               /* acl applied on item */
    private String versioned;             /* is item versioned */
    private String folderDocName;         /* name of item */
    private String folderDocPath;         /* path of item */
    private Long[] folderDocIds;          /* selected item ids */
    private boolean folderShared;         /* is folder shared */
    private int folderCount;              /* total subfolders in this folder */
    private int folderDocCount;           /* total items in this folder */
    private boolean shared;               /* is acl shared */
    private int documentCount;            /* total document count in this folder */
    Boolean aclError;                     /* acl error */
    private String documentId;            /* document id as String */

  /**
   * getter method for folderDocType
   * @return String folderDocType
   */
    public String getFolderDocType() {
      return folderDocType;
    }

  /**
   * setter method for folderDocType
   * @param newFolderDocType
   */
    public void setFolderDocType(String newFolderDocType) {
      folderDocType = newFolderDocType;
    }

  /**
   * getter method for folderDocDescription
   * @return String folderDocDescription
   */
    public String getFolderDocDescription() {
      return folderDocDescription;
    }

  /**
   * setter method for folderDocDescription
   * @param newFolderDocDescription
   */
    public void setFolderDocDescription(String newFolderDocDescription) {
      folderDocDescription = newFolderDocDescription;
    }

  /**
   * getter method for folderDocLocation
   * @return String folderDocLocation
   */
    public String getFolderDocLocation() {
      return folderDocLocation;
    }

  /**
   * setter method for folderDocLocation
   * @param newFolderDocLocation
   */
    public void setFolderDocLocation(String newFolderDocLocation) {
      folderDocLocation = newFolderDocLocation;
    }

  /**
   * getter method for folderDocSize
   * @return String folderDocSize
   */
    public String getFolderDocSize() {
      return folderDocSize;
    }

  /**
   * setter method for folderDocSize
   * @param newFolderDocSize
   */
    public void setFolderDocSize(String newFolderDocSize) {
      folderDocSize = newFolderDocSize;
    }

  /**
   * getter method for createdDate
   * @return String createdDate
   */
    public String getCreatedDate() {
      return createdDate;
    }

  /**
   * setter method for createdDate
   * @param newCreatedDate
   */
    public void setCreatedDate(String newCreatedDate) {
      createdDate = newCreatedDate;
    }

  /**
   * getter method for createdBy
   * @return String createdBy
   */
    public String getCreatedBy() {
      return createdBy;
    }

  /**
   * setter method for createdBy
   * @param newCreatedBy
   */
    public void setCreatedBy(String newCreatedBy) {
      createdBy = newCreatedBy;
    }

  /**
   * getter method for modifiedDate
   * @return String modifieddate
   */
    public String getModifiedDate() {
      return modifiedDate;
    }

  /**
   * setter method for modifiedDate
   * @param newModifiedDate
   */
    public void setModifiedDate(String newModifiedDate) {
      modifiedDate = newModifiedDate;
    }

  /**
   * getter method for modifiedBy
   * @return String modifiedBy
   */
    public String getModifiedBy() {
      return modifiedBy;
    }

  /**
   * setter method for modifiedBy
   * @param newModifiedBy
   */
    public void setModifiedBy(String newModifiedBy) {
      modifiedBy = newModifiedBy;
    }

  /**
   * getter method for versioned
   * @return String versioned
   */
    public String getVersioned() {
      return versioned;
    }

  /**
   * setter method for versioned
   * @param newVersioned
   */
    public void setVersioned(String newVersioned) {
      versioned = newVersioned;
    }

  /**
   * getter method for folderDocName
   * @return String folderDocName
   */
    public String getFolderDocName() {
      return folderDocName;
    }

  /**
   * setter method for folderDocName
   * @param newFolderDocName
   */
    public void setFolderDocName(String newFolderDocName) {
      folderDocName = newFolderDocName;
    }

  /**
   * getter method for folderDocPath
   * @return String folderDocPath 
   */
    public String getFolderDocPath() {
      return folderDocPath;
    }

  /**
   * setter method for folderDocPath
   * @param newFolderDocPath
   */
    public void setFolderDocPath(String newFolderDocPath) {
      folderDocPath = newFolderDocPath;
    }

  /**
   * getter method for aclName
   * @return String aclName
   */
    public String getAclName() {
      return aclName;
    }

  /**
   * setter method for aclName
   * @param newAclName
   */
    public void setAclName(String newAclName) {
      aclName = newAclName;
    }

  /**
   * getter method for folderShared
   * @return boolean folderShared
   */
    public boolean isFolderShared() {
      return folderShared;
    }

  /**
   * setter method for folderShared
   * @param newFolderShared
   */
    public void setFolderShared(boolean newFolderShared) {
      folderShared = newFolderShared;
    }

  /**
   * getter method for folderCount
   * @return int folderCount
   */
    public int getFolderCount() {
      return folderCount;
    }

  /**
   * setter method for folderCount
   * @param newFolderCount
   */
    public void setFolderCount(int newFolderCount) {
      folderCount = newFolderCount;
    }

  /**
   * getter method for folderDocCount
   * @return int folderDocCount
   */
    public int getFolderDocCount() {
      return folderDocCount;
    }

  /**
   * setter method for folderDocCount
   * @param newFolderDocCount
   */
    public void setFolderDocCount(int newFolderDocCount) {
      folderDocCount = newFolderDocCount;
    }

  /**
   * getter method for folderDocIds
   * @return Long[] folderDocIds
   */
    public Long[] getFolderDocIds() {
      return folderDocIds;
    }

  /**
   * setter method for folderDocIds
   * @param newFolderDocIds
   */
    public void setFolderDocIds(Long[] newFolderDocIds) {
      folderDocIds = newFolderDocIds;
    }

  /**
   * getter method for shared
   * @return boolean shared
   */
    public boolean isShared() {
      return shared;
    }

  /**
   * setter method for shared
   * @param newShared
   */
    public void setShared(boolean newShared) {
      shared = newShared;
    }

  /**
   * getter method for documentCount
   * @return int documentCount
   */
    public int getDocumentCount() {
      return documentCount;
    }

  /**
   * setter method for documentCount
   * @param newDocumentCount
   */
    public void setDocumentCount(int newDocumentCount) {
      documentCount = newDocumentCount;
    }

  /**
   * getter method for aclError
   * @return Boolean aclError
   */
    public Boolean isAclError() {
      return aclError;
    }

  /**
   * setter method for aclError
   * @param newAclError
   */
    public void setAclError(Boolean newAclError) {
      aclError = newAclError;
    }

  /**
   * getter method for docWorkFlowStatus
   * @return String docWorkFlowStatus
   */
    public String getDocWorkFlowStatus() {
      return docWorkFlowStatus;
    }
  
  /**
   * setter method for docWorkFlowStatus
   * @param docWorkFlowStatus
   */
    public void setDocWorkFlowStatus(String docWorkFlowStatus) {
      this.docWorkFlowStatus = docWorkFlowStatus;
    }
  
  /**
   * getter method for documentId
   * @return String documentId
   */
    public String getDocumentId() {
      return documentId;
    }
    
  /**
   * setter method for documentId
   * @param documentId
   */
    public void setDocumentId(String documentId) {
      this.documentId = documentId;
    }
}
