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
 * $Id: HTMLStyles.java,v 1.4 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;
/**
 *	Purpose: To Act as Enum for HTML Styles.
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   09-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public final class HTMLStyles  {

  //Style Property Name
  private final String style_name;

  //Private Contructor to use in this class only
  private HTMLStyles(String style_name){
    this.style_name = style_name;
  }

  /**
	 * Purpose  : To Get the Style Property Name, the toString() method of Object Class is overloaded 
	 * @returns : Returns Style Property Name
	 */
  public String toString(){
    return this.style_name;
  }

  /** Enum Constants defined as HTML Styles */
  public static final HTMLStyles BACKGROUND_COLOR = new HTMLStyles("BACKGROUND-COLOR");
  public static final HTMLStyles BACKGROUND_IMAGE = new HTMLStyles("BACKGROUND-IMAGE");
  public static final HTMLStyles COLOR = new HTMLStyles("COLOR");
  public static final HTMLStyles FONT_FAMILY = new HTMLStyles("FONT-FAMILY");
}
