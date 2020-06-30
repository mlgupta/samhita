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
 * $Id: TotalSizeFoldersDocs.java,v 20040220.5 2006/03/14 06:35:54 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose: Bean to store size of and item count of individual folders   
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class TotalSizeFoldersDocs  {
    private long size;          /* size of this folder */
    private int folderCount;    /* total number of sub folders in this folder */
    private int documentCount;  /* total number of documents in this folder */
    private int folderDocCount; /* total number of sub folders and documents in 
                                 * this folder */

    public TotalSizeFoldersDocs() {
    }

  /**
   * getter method for size
   * @return long size
   */
    public long getSize() {
        return size;
    }

  /**
   * setter method for size
   * @param newSize
   */
    public void setSize(long newSize) {
        size = newSize;
    }

  /**
   * getter method for folder count
   * @return int folder count
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
   * getter method for documentCount 
   * @return int document count
   */
    public int getDocumentCount() {
        return documentCount;
    }

  /**
   * setter method for document count
   * @param newDocumentCount
   */
    public void setDocumentCount(int newDocumentCount) {
        documentCount = newDocumentCount;
    }

  /**
   * getter method for folder document count
   * @return int folder document count 
   */
    public int getFolderDocCount() {
        return folderDocCount;
    }

  /**
   * setter method for folder document count
   * @param newFolderDocCount
   */
    public void setFolderDocCount(int newFolderDocCount) {
        folderDocCount = newFolderDocCount;
    }
}
