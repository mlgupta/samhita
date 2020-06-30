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
 * $Id: StyleTypes.java,v 1.6 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;

/**
 *	Purpose: To Act as Enum for Style Type - Class or Unique Ids.
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   09-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class StyleTypes  {

  // Style Type - Class or Unique Ids.
  private String type;
  
  //Private Contructor to use in this class only
  private StyleTypes(String type) {
    this.type=type;
  }

  /**
	 * Purpose  : To Get the Style Type, the toString() method of Object Class is overloaded 
	 * @returns : Returns an Style Type Char
	 */
  public String toString(){
    return this.type;
  } 

  /** Enum Constants Defined as  Style Type */
  public static final StyleTypes UNIQUE_ID= new StyleTypes("#");
  public static final StyleTypes CLASS= new StyleTypes(".");
}
