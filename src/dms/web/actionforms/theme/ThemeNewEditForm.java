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
 * $Id: ThemeNewEditForm.java,v 1.13 2006/03/17 07:05:17 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.theme;
//Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of themeNewEditForm in  
 *           theme_new.jsp and theme_edit.jsp
 *  @author             Rajan Gupta
 *  @version            1.0
 * 	Date of creation:   25-02-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class ThemeNewEditForm extends ValidatorForm{
  /* member variables declaration goes here */
  private String hdnBackgroundColor;      /* background color for this theme */
  private String hdnBackgroundImage;      /* background image for this theme */
  private String hdnColorScheme;          /* color scheme selected for this theme */
  private String cboBodyText;             /* body text list */
  private String cboElementText;          /* element text list */
  private String cboHeadings;             /* headings list */
  private String cboMenuText;             /* menu text list */
  private String hdnStyle;                /* style to be applied */
  private String txtThemeName;            /* name of this theme */
  private String hdnFontColorHeadings;    /* font color for headings */  
  private String hdnFontColorBodyText;    /* font color for body texts */
  private String hdnFontColorMenuText;    /* font color for menu texts */
  private String hdnFontColorElementText; /* font color for element texts */
  private String radBackground;           /* select background */
  private String hdnFontColorElementBg;   /* font dolor element for background */
  /* declaration of constants to be used */
  public static final String BACKGROUND_COLOR="BACKGROUND-COLOR";
  public static final String BACKGROUND_IMAGE="BACKGROUND-IMAGE";
  public static final String COLOR_SCHEME="COLOR-SCHEME";
  public static final String BODY_TEXT="BODY-TEXT";
  public static final String ELEMENT_TEXT="ELEMENT-TEXT";
  public static final String HEADINGS="HEADINGS";
  public static final String MENU_TEXT="MENU-TEXT";
  public static final String STYLE="STYLE";
  public static final String HEADINGS_COLOR="HEADINGS-COLOR";
  public static final String BODY_TEXT_COLOR="BODY-TEXT-COLOR";
  public static final String MENU_TEXT_COLOR="MENU-TEXT-COLOR";
  public static final String ELEMENT_TEXT_COLOR="ELEMENT-TEXT-COLOR";
  public static final String BACKGROUND="BACKGROUND";
  public static final String ELEMENT_BACKGROUND_COLOR="ELEMENT-BACKGROUND-COLOR";

  /**
   * getter method for hdnBackgroundColor
   * @return String hdnBackgroundColor
   */
  public String getHdnBackgroundColor() {
    return hdnBackgroundColor;
  }

  /**
   * setter method for hdnBackgroundColor
   * @param newHdnBackgroundColor
   */
  public void setHdnBackgroundColor(String newHdnBackgroundColor) {
    hdnBackgroundColor = newHdnBackgroundColor;
  }

  /**
   * getter method for hdnBackgroundImage
   * @return String hdnBackgroundImage
   */
  public String getHdnBackgroundImage() {
    return hdnBackgroundImage;
  }

  /**
   * setter method for hdnBackgroundImage
   * @param newHdnBackgroundImage
   */
  public void setHdnBackgroundImage(String newHdnBackgroundImage) {
    hdnBackgroundImage = newHdnBackgroundImage;
  }

  /**
   * getter method for hdnColorScheme
   * @return String hdnColorScheme
   */
  public String getHdnColorScheme() {
    return hdnColorScheme;
  }

  /**
   * setter method for hdnColorScheme
   * @param newHdnColorScheme
   */
  public void setHdnColorScheme(String newHdnColorScheme) {
    hdnColorScheme = newHdnColorScheme;
  }

  /**
   * getter method for cboBodyText
   * @return String cboBodyText
   */
  public String getCboBodyText() {
    return cboBodyText;
  }

  /**
   * setter method for cboBodyText
   * @param newCboBodyText
   */
  public void setCboBodyText(String newCboBodyText) {
    cboBodyText = newCboBodyText;
  }

  /**
   * getter method for cboElementText
   * @return String cboElementText
   */
  public String getCboElementText() {
    return cboElementText;
  }

  /**
   * setter method for cboElementText
   * @param newCboElementText
   */
  public void setCboElementText(String newCboElementText) {
    cboElementText = newCboElementText;
  }

  /**
   * getter method for cboHeadings
   * @return String cboHeadings
   */
  public String getCboHeadings() {
    return cboHeadings;
  }

  /**
   * setter method for cboHeadings
   * @param newCboHeadings
   */
  public void setCboHeadings(String newCboHeadings) {
    cboHeadings = newCboHeadings;
  }

  /**
   * getter method for cboMenuText
   * @return String cboMenuText
   */
  public String getCboMenuText() {
    return cboMenuText;
  }

  /**
   * setter method for cboMenuText
   * @param newCboMenuText
   */
  public void setCboMenuText(String newCboMenuText) {
    cboMenuText = newCboMenuText;
  }

  /**
   * getter method for hdnStyle
   * @return String hdnStyle
   */
  public String getHdnStyle() {
    return hdnStyle;
  }

  /**
   * setter method for hdnStyle
   * @param newHdnStyle
   */
  public void setHdnStyle(String newHdnStyle) {
    hdnStyle = newHdnStyle;
  }

  /**
   * getter method for txtThemeName
   * @return String txtThemeName
   */
  public String getTxtThemeName() {
    return txtThemeName;
  }

  /**
   * setter method for txtThemeName
   * @param newTxtThemeName
   */
  public void setTxtThemeName(String newTxtThemeName) {
    txtThemeName = newTxtThemeName;
  }

  /**
   * getter method for hdnFontColorBodyText
   * @return String hdnFontColorBodyText
   */
  public String getHdnFontColorBodyText() {
    return hdnFontColorBodyText;
  }

  /**
   * setter method for hdnFontColorBodyText
   * @param newHdnFontColorBodyText
   */
  public void setHdnFontColorBodyText(String newHdnFontColorBodyText) {
    hdnFontColorBodyText = newHdnFontColorBodyText;
  }

  /**
   * getter method for hdnFontColorElementText
   * @return String hdnFontColorElementText
   */
  public String getHdnFontColorElementText() {
    return hdnFontColorElementText;
  }

  /**
   * setter method for hdnFontColorElementText
   * @param newHdnFontColorElementText
   */
  public void setHdnFontColorElementText(String newHdnFontColorElementText) {
    hdnFontColorElementText = newHdnFontColorElementText;
  }

  /**
   * getter method for newHdnFontColorHeadings
   * @return String newHdnFontColorHeadings
   */
  public String getHdnFontColorHeadings() {
    return hdnFontColorHeadings;
  }

  /**
   * setter method for newHdnFontColorHeadings
   * @param newHdnFontColorHeadings
   */
  public void setHdnFontColorHeadings(String newHdnFontColorHeadings) {
    hdnFontColorHeadings = newHdnFontColorHeadings;
  }

  /**
   * getter method for hdnFontColorMenuText
   * @return String hdnFontColorMenuText
   */
  public String getHdnFontColorMenuText() {
    return hdnFontColorMenuText;
  }

  /**
   * setter method for hdnFontColorMenuText
   * @param newHdnFontColorMenuText
   */
  public void setHdnFontColorMenuText(String newHdnFontColorMenuText) {
    hdnFontColorMenuText = newHdnFontColorMenuText;
  }

  /**
   * getter method for radBackground
   * @return String radBackground
   */
  public String getRadBackground() {
    return radBackground;
  }

  /**
   * setter method for radBackground
   * @param newRadBackground
   */
  public void setRadBackground(String newRadBackground) {
    radBackground = newRadBackground;
  }

  /**
   * getter method for hdnFontColorElementBg
   * @return String hdnFontColorElementBg
   */
  public String getHdnFontColorElementBg() {
    return hdnFontColorElementBg;
  }

  /**
   * setter method for hdnFontColorElementBg
   * @param newHdnFontColorElementBg
   */
  public void setHdnFontColorElementBg(String newHdnFontColorElementBg) {
    hdnFontColorElementBg = newHdnFontColorElementBg;
  }
}
