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
 * $Id: AceListBean.java,v 20040220.5 2006/03/13 15:14:03 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.security;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to store ACE specific info .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    07-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class AceListBean {
    private String category;    /* represents category of ace */
    private String name;        /* represents name */
    private String state;       /* represents state of ace */
    private String permission;  /* represents permission on ace */
    private String index;       /* represents index */

    public AceListBean(){
    }
 
  /**
   * getter method for category
   * @return String category
   */
    public String getCategory() {
        return category;
    }

  /**
   * setter method for category
   * @param newCategory
   */
    public void setCategory(String newCategory) {
        category = newCategory;
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
   * getter method for state
   * @return String state
   */
    public String getState() {
        return state;
    }

  /**
   * setter method for state
   * @param newState
   */
    public void setState(String newState) {
        state = newState;
    }

  /**
   * getter method for permission
   * @return String permission
   */
    public String getPermission() {
        return permission;
    }

  /**
   * setter method for permission
   * @param newPermission
   */
    public void setPermission(String newPermission) {
        permission = newPermission;
    }

  /**
   * getter method for index
   * @return String index
   */
    public String getIndex() {
        return index;
    }

  /**
   * setter method for index
   * @param newIndex
   */
    public void setIndex(String newIndex) {
        index = newIndex;
    }

    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            strTemp += "\n\tcategory : " + category;
            strTemp += "\n\tname : " + name;
            strTemp += "\n\tstate : " + state;
        }
        return strTemp;
    }
}
