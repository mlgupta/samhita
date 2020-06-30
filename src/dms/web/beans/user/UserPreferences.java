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
 * $Id: UserPreferences.java,v 20040220.20 2006/03/17 13:10:06 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.user;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/** 
 *	Purpose: To hold UserPreferences
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  27-12-2004.  
 */
public class UserPreferences  {

  private int treeLevel;
  private String treeIconPath;
  private int recordsPerPage;
  private String stylePlaceHolder;
  private int navigationType;
  private int chkOpenDocInNewWin; /*variable for open doc option */
  
  public static int FLAT_NAVIGATION = 1;
  public static int TREE_NAVIGATION = 2;
  public static int BRAIN_TREE_NAVIGATION = 3;

  /* constants for open doc option */
  public static int IN_SAME_FRAME = 1;
  public static int IN_NEW_WINDOW = 2;
  
  /**
   * Constructor for UserPreferences Class
   */
  public UserPreferences() {
    treeLevel = 3;
    treeIconPath = "themes/standard/";
    recordsPerPage = 15;
    navigationType = TREE_NAVIGATION;
    chkOpenDocInNewWin=IN_SAME_FRAME; /* initialize to same frame option */
  }

  /**
   * getter method for treeLevel
   * @return int treeLevel
   */
  public int getTreeLevel() {
    return treeLevel;
  }

  /**
   * setter method for treeLevel
   * @param newTreeLevel
   */
  public void setTreeLevel(int newTreeLevel) {
    treeLevel = newTreeLevel;
  }

  /**
   * getter method for treeIconPath
   * @return String treeIconPath
   */
  public String getTreeIconPath() {
    return treeIconPath;
  }

  /**
   * setter method for treeIconPath
   * @param newTreeIconPath
   */
  public void setTreeIconPath(String newTreeIconPath) {
    treeIconPath = newTreeIconPath;
  }

  /**
   * getter method for recordsPerPage
   * @return int recordsPerPage
   */
  public int getRecordsPerPage() {
    return recordsPerPage;
  }

  /**
   * setter method for recordsPerPage
   * @param newRecordsPerPage
   */
  public void setRecordsPerPage(int newRecordsPerPage) {
    recordsPerPage = newRecordsPerPage;
  }

  public String toString(){
    String strTemp = "";
    Logger logger = Logger.getLogger("DbsLogger");
    if(logger.getLevel() == Level.DEBUG){
      strTemp += "\n\ttreeLevel : " + treeLevel;
      strTemp += "\n\ttreeIconPath : " + treeIconPath;
      strTemp += "\n\trecordsPerPage : " + recordsPerPage;
      strTemp += "\n\tchkOpenDocInNewWin : "+chkOpenDocInNewWin;
    }
    return strTemp;
  }

  /**
   * getter method for stylePlaceHolder
   * @return String stylePlaceHolder
   */
  public String getStylePlaceHolder() {
    return stylePlaceHolder;
  }

  /**
   * setter method for newStylePlaceHolder
   * @param newStylePlaceHolder
   */
  public void setStylePlaceHolder(String newStylePlaceHolder) {
    stylePlaceHolder = newStylePlaceHolder;
  }

  /**
   * getter method for navigationType
   * @return navigationType
   */
  public int getNavigationType() {
    return navigationType;
  }

  /**
   * setter method for navigationType
   * @param newNavigationType
   */
  public void setNavigationType(int newNavigationType) {
    navigationType = newNavigationType;
  }

  /**
   * Purpose  : Returns Open Document Option.
   * @return  : int value.
   */
  public int getChkOpenDocInNewWin(){
    return chkOpenDocInNewWin;      
  }

  /**
   * Purpose  : Sets Open Document Option
   * @param   : checkbox status.
   */
  public void setChkOpenDocInNewWin(int chkOpenDocInNewWin){
    this.chkOpenDocInNewWin=chkOpenDocInNewWin;      
  }
}
