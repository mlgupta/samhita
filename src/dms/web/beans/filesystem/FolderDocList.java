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
 * $Id: FolderDocList.java,v 20040220.16 2006/03/14 06:58:35 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose: To hold information regarding folder and document
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 14-03-2006   
 */
public class FolderDocList  {
    private String modifiedDate;  /* modified date of folder or doc */
    private String name;          /* folder or doc name */ 
    private String type;          /* type of item */
    private String size;          /* size of folder or doc */
    private Long id;              /* folder or doc id */
    private int item=0;           /* number of items in folder */
    private String treeFileName;  /* tree file name corr to the session */
    private String path;          /* path of folder or doc */
    private boolean checkedOut;   /* to determine if folder or doc is checked out */
    private String className;     /* class of the item */
    boolean encripted;            /* to determine if doc is encrypted */
    String description;           /* description corr to this folder or doc */
    private String txtLinkGenerated;  /* link generated for this doc */
    private String davPath;           /* webdav path for this doc */
    private String pathOrTrimmedName; /* trimmed path or name for display */
    private String trimDescription;   /* trimmed description for display */
    
  /**
   * getter method for modifiedDate
   * @return String modifiedDate
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
   * getter method for name
   * @return String name
   */
    public String getName() {
      return name;
    }

  /**
   * setter method for name
   * @param newName
   */
    public void setName(String newName) {
      name = newName;
    }

  /**
   * getter method for type
   * @return String type
   */
    public String getType() {
      return type;
    }

  /**
   * setter method for type
   * @param newType
   */
    public void setType(String newType) {
      type = newType;
    }

  /**
   * getter method for size
   * @return String size
   */
    public String getSize() {
      return size;
    }

  /**
   * setter method for size
   * @param newSize
   */
    public void setSize(String newSize) {
      size = newSize;
    }

  /**
   * getter method for Id
   * @return Long Id
   */
    public Long getId() {
      return id;
    }

  /**
   * setter method for Id
   * @param newId
   */
    public void setId(Long newId) {
      id = newId;
    }

  /**
   * getter method for item
   * @return int item
   */
    public int getItem() {
      return item;
    }

  /**
   * setter method for item
   * @param items
   */
    public void setItem(int items) {
      item = items;
    }
    
  /**
   * getter method for treeFileName
   * @return String treeFileName
   */
    public String getTreeFileName() {
      return treeFileName;
    }

  /**
   * setter method for treeFileName
   * @param newTreeFileName
   */
    public void setTreeFileName(String newTreeFileName) {
      treeFileName = newTreeFileName;
    }

  /**
   * getter method for path
   * @return String path
   */
    public String getPath() {
      return path;
    }

  /**
   * setter method for path
   * @param newPath
   */
    public void setPath(String newPath) {
      path = newPath;
    }

  /**
   * getter method for checkedOut
   * @return boolean checkedOut
   */
    public boolean isCheckedOut() {
      return checkedOut;
    }

  /**
   * setter method for checkedOut
   * @param checkedOut
   */
    public void setCheckedOut(boolean checkedOut) {
      this.checkedOut = checkedOut;
    }

  /**
   * getter method for className
   * @return String className
   */
    public String getClassName() {
      return className;
    }

  /**
   * setter method for className
   * @param newClassName
   */
    public void setClassName(String newClassName) {
      className = newClassName;
    }

  /**
   * getter method for encrypted
   * @return boolean encrypted
   */
    public boolean isEncripted() {
      return encripted;
    }

  /**
   * setter method for encrypted
   * @param newEncripted
   */
    public void setEncripted(boolean newEncripted) {
      encripted = newEncripted;
    }

  /**
   * getter method for description
   * @return String description
   */
    public String getDescription() {
      return description;
    }

  /**
   * setter method for description
   * @param newDescription
   */
    public void setDescription(String newDescription) {
      description = newDescription;
    }
    
  /**
   * getter method for txtLinkGenerated
   * @return String txtLinkGenerated
   */
    public String getTxtLinkGenerated(){
      return txtLinkGenerated;
    }
    
  /**
   * setter method for txtLinkGenerated
   * @param newTxtLinkGenerated
   */
    public void setTxtLinkGenerated(String newTxtLinkGenerated){
      txtLinkGenerated = newTxtLinkGenerated;
    }

  /**
   * getter method for davPath
   * @return String davPath
   */
    public String getDavPath(){
      return davPath;
    }

  /**
   * setter method for davPath
   * @param davPath
   */
    public void setDavPath(String davPath){
      this.davPath = davPath;
    }

  /**
   * getter method for pathOrTrimmedName
   * @return String pathOrTrimmedName
   */
    public String getPathOrTrimmedName() {
      return pathOrTrimmedName;
    }
  
  /**
   * setter method for pathOrTrimmedName
   * @param pathOrTrimmedName
   */
    public void setPathOrTrimmedName(String pathOrTrimmedName) {
      this.pathOrTrimmedName = pathOrTrimmedName;
    }

  /**
   * getter method for trimDescription
   * @return String trimDescription
   */
    public String getTrimDescription() {
      return trimDescription;
    }
  
  /**
   * setter method for trimDescription
   * @param trimDescription
   */
    public void setTrimDescription(String trimDescription) {
      this.trimDescription = trimDescription;
    }
}