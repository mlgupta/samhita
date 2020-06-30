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
 * $Id: DocumentHistoryDetail.java,v 20040220.9 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose:             Bean used to store version details for a versioned doc.
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    10-04-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class DocumentHistoryDetail  {
    private long versionNumber;   /* represents version number */
    private String userName;      /* represents user name for this version*/
    private String versionDate;   /* represents date of creation of this version */
    private String actionType;    /* represents action type */
    private Long docId;           /* represents RPO Id */
    private String comment;       /* represents comment for this version */
    private String docName;       /* represents document name */

    public DocumentHistoryDetail() {
    }

  /**
   * getter method for version number
   * @return long verion number
   */
    public long getVersionNumber() {
        return versionNumber;
    }

  /**
   * setter method for version number
   * @param newVersionNumber
   */
    public void setVersionNumber(long newVersionNumber) {
        versionNumber = newVersionNumber;
    }

  /**
   * getter method for user name
   * @return String containing username
   */
    public String getUserName() {
        return userName;
    }

  /**
   * setter method for user name
   * @param newUserName
   */
    public void setUserName(String newUserName) {
        userName = newUserName;
    }

  /**
   * getter method for version date
   * @return String containing version date
   */
    public String getVersionDate() {
        return versionDate;
    }

  /**
   * setter method for version date
   * @param newVersionDate
   */
    public void setVersionDate(String newVersionDate) {
        versionDate = newVersionDate;
    }

  /**
   * getter method for action type
   * @return String containing action type
   */
    public String getActionType() {
        return actionType;
    }

  /**
   * setter method for action type
   * @param newActionType
   */
    public void setActionType(String newActionType) {
        actionType = newActionType;
    }

  /**
   * getter method for document Id
   * @return Long containing document Id
   */
    public Long getDocId() {
        return docId;
    }

  /**
   * setter method for document Id
   * @param newDocId
   */
    public void setDocId(Long newDocId) {
        docId = newDocId;
    }

  /**
   * getter method for comment
   * @return String containing comment
   */
    public String getComment() {
        return comment;
    }

  /**
   * setter method for comment
   * @param newComment
   */
    public void setComment(String newComment) {
        comment = newComment;
    }

  /**
   * getter method for docName
   * @return String containing docName
   */
    public String getDocName() {
        return docName;
    }

  /**
   * setter method for docName
   * @param newDocName
   */
    public void setDocName(String newDocName) {
        docName = newDocName;
    }
}
