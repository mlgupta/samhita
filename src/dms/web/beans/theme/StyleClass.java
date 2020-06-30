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
 * $Id: StyleClass.java,v 1.8 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;
/* Java API */
import java.util.Enumeration;
import java.util.Hashtable;
/**
 *	Purpose: To Create HTML Tag Styles , Classes and  Unique Id
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   09-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class StyleClass  {

  private String styleClassData;
  private static final String STYLE_HEADER="{";
  private static final String STYLE_FOOTER="}"; 
  private static final String LINE_FEED="\n";
  private static final String STYLE_DELIMITER=";";
  private static final String STYLE_ASSIGNMENT=":";
  
  /**
   * Purpose  : To create HTML Tag Style 
   * @param   : tag - object of  HTMLTags;
   * @param   : styles - a Hash Table consisting of style properties and Value  
   */
  public StyleClass(HTMLTags tag, Hashtable styles){

    Enumeration  stylesKeys = styles.keys();
    String classData = new String(); 
    String header=tag + STYLE_HEADER + LINE_FEED;

    while(stylesKeys.hasMoreElements()){
      HTMLStyles styleKey= (HTMLStyles)stylesKeys.nextElement(); 
      String styleValue=styleKey + STYLE_ASSIGNMENT + styles.get(styleKey);
      classData=classData + styleValue + STYLE_DELIMITER + LINE_FEED;
    }
    
    this.styleClassData=header+classData +STYLE_FOOTER+LINE_FEED;    

  }

    /**
   * Purpose  : To create style (Class or Unique_Id),  associated with HTML Tag 
   * @param   : styleName - Name of the Class or Unique_Id
   * @param   : tag - Associated HTML Tag, an object of  HTMLTags
   * @param   : styles - a Hash Table consisting of style properties and Value  
   * @param   : styleType - a Hash Table consisting of style properties and Value  
   */
  public StyleClass(String styleName, HTMLTags associatedTag, Hashtable styles, StyleTypes styleType){

    Enumeration  stylesKeys = styles.keys();
    String classData = new String(); 
    String header= new String(); 
    
    if (associatedTag.equals(HTMLTags.A_ACTIVE)||associatedTag.equals(HTMLTags.A_HOVER)||associatedTag.equals(HTMLTags.A_VISITED)||associatedTag.equals(HTMLTags.A_LINK)){
      header=associatedTag.toString().replaceFirst(associatedTag.A.toString(),associatedTag.A.toString()+styleType+styleName) + STYLE_HEADER + LINE_FEED;
    }else{
      header=associatedTag.toString() + styleType + styleName + STYLE_HEADER + LINE_FEED;
    }
    

    while(stylesKeys.hasMoreElements()){
      HTMLStyles styleKey= (HTMLStyles)stylesKeys.nextElement(); 
      String styleValue=styleKey + STYLE_ASSIGNMENT + styles.get(styleKey);
      classData=classData + styleValue + STYLE_DELIMITER + LINE_FEED;
    }
    
    this.styleClassData=header+classData +STYLE_FOOTER+LINE_FEED;    
  }

      /**
   * Purpose  : To create style (Class or Unique_Id)  
   * @param   : styleName - Name of the Class or Unique_ID
   * @param   : styles - a Hash Table consisting of style properties and Value  
   * @param   : styleType - a Hash Table consisting of style properties and Value  
   */
  public StyleClass(String styleName, Hashtable styles, StyleTypes styleType){

    Enumeration  stylesKeys = styles.keys();
    String classData = new String(); 
    String header= new String(); 
    
    header=styleType + styleName + STYLE_HEADER + LINE_FEED;

    while(stylesKeys.hasMoreElements()){
      HTMLStyles styleKey= (HTMLStyles)stylesKeys.nextElement(); 
      String styleValue=styleKey + STYLE_ASSIGNMENT + styles.get(styleKey);
      classData=classData + styleValue + STYLE_DELIMITER + LINE_FEED;
    }
    
    this.styleClassData=header+classData +STYLE_FOOTER+LINE_FEED;    
  }

   
  /**
	 * Purpose  : To Get the Data for HTML Tag Styles , Classes or  Unique Id , the toString() method of Object Class is overloaded 
	 * @returns : Returns Data for HTML Tag Styles , Classes or  Unique Id
	 */
  public String toString(){
    return this.styleClassData;
  }
 
}
