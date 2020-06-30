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
 * $Id: HTMLTags.java,v 1.5 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;
/**
 *	Purpose: To Act as Enum for HTML Tags.
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   09-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
 
public final class HTMLTags  {

  //Tag Name 
  private final String tag_name;

 //Private Contructor to use in this class only
  private HTMLTags(String tag_name){
    this.tag_name=tag_name;
  }

   /**
	 * Purpose  : To Get the Tag Name, the toString() method of Object Class is overloaded 
	 * @returns : Returns an HTML Tag Name
	 */
  public String toString(){
    return this.tag_name;
  }

  /** Enum Constants Defined as  HTML Tags */
  public static final HTMLTags BODY = new  HTMLTags("BODY");
  public static final HTMLTags TD = new  HTMLTags("TD");
  public static final HTMLTags TH = new  HTMLTags("TH");
  public static final HTMLTags A = new  HTMLTags("A");
  public static final HTMLTags A_LINK = new  HTMLTags("A:LINK");
  public static final HTMLTags A_VISITED = new  HTMLTags("A:VISITED");
  public static final HTMLTags A_HOVER = new  HTMLTags("A:HOVER");
  public static final HTMLTags A_ACTIVE = new  HTMLTags("A:ACTIVE");
  
}
