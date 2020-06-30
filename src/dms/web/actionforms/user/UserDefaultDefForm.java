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
 * $Id: UserDefaultDefForm.java,v 20040220.3 2006/03/17 07:46:24 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.user; 
/* Struts API */
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of 
 *           userDefaultDefForm in user_default_def.jsp
 *  @author             Maneesh Mishra
 *  @version            1.0
 * 	Date of creation:   21-02-2004
 * 	Last Modified by :  Suved Mishra   
 * 	Last Modified Date: 17-03-2006   
 */
public class UserDefaultDefForm extends ValidatorForm {
    /* member variables declaration goes here */
    private String cboCharacterSet;       /* list of character sets supported */ 
    private String cboLanguage;           /* list of languages supported */
    private String cboLocale;             /* list of locales supported */
    private String cboTimeZone;           /* list of timezones supported */  
    private String txtHomeFolder;         /* home folder for the user */
    private String radEmail;              /* mail folder for the user */
    private String radQuota;              /* allocated user quota */
    private String radPassword;           /* user login password */
    private String txtEmailAddress;       /* email id of the user */
    private String txtStorage;            /* quota used up for storage as yet */

  /**
   * getter method for cboCharacterSet
   * @return String cboCharacterSet
   */
    public String getCboCharacterSet() {
        return cboCharacterSet;
    }

  /**
   * setter method for cboCharacterSet
   * @param newCboCharacterSet
   */
    public void setCboCharacterSet(String newCboCharacterSet) {
        cboCharacterSet = newCboCharacterSet;
    }

  /**
   * getter method for cboLanguage
   * @return String cboLanguage
   */
    public String getCboLanguage() {
        return cboLanguage;
    }

  /**
   * setter method for cboLanguage
   * @param newCboLanguage
   */
    public void setCboLanguage(String newCboLanguage) {
        cboLanguage = newCboLanguage;
    }

  /**
   * getter method for cboLocale
   * @return String cboLocale
   */
    public String getCboLocale() {
        return cboLocale;
    }

  /**
   * setter method for cboLocale
   * @param newCboLocale
   */
    public void setCboLocale(String newCboLocale) {
        cboLocale = newCboLocale;
    }

  /**
   * getter method for cboTimeZone
   * @return String cboTimeZone
   */
    public String getCboTimeZone() {
        return cboTimeZone;
    }

  /**
   * setter method for cboTimeZone
   * @param newCboTimeZone
   */
    public void setCboTimeZone(String newCboTimeZone) {
        cboTimeZone = newCboTimeZone;
    }

  /**
   * getter method for txtHomeFolder
   * @return String txtHomeFolder
   */
    public String getTxtHomeFolder() {
        return txtHomeFolder;
    }

  /**
   * setter method for txtHomeFolder
   * @param newTxtHomeFolder
   */
    public void setTxtHomeFolder(String newTxtHomeFolder) {
        txtHomeFolder = newTxtHomeFolder;
    }

  /**
   * getter method for radEmail
   * @return String radEmail
   */
    public String getRadEmail() {
        return radEmail;
    }

  /**
   * setter method for radEmail
   * @param newRadEmail
   */
    public void setRadEmail(String newRadEmail) {
        radEmail = newRadEmail;
    }

  /**
   * getter method for radQuota
   * @return String radQuota
   */
    public String getRadQuota() {
        return radQuota;
    }

  /**
   * setter method for radQuota
   * @param newRadQuota
   */
    public void setRadQuota(String newRadQuota) {
        radQuota = newRadQuota;
    }

  /**
   * getter method for radPassword
   * @return String radPassword
   */
    public String getRadPassword() {
        return radPassword;
    }

  /**
   * setter method for radPassword
   * @param newRadPassword
   */
    public void setRadPassword(String newRadPassword) {
        radPassword = newRadPassword;
    }

  /**
   * getter method for txtEmailAddress
   * @return String txtEmailAddress
   */
    public String getTxtEmailAddress() {
        return txtEmailAddress;
    }

  /**
   * setter method for txtEmailAddress
   * @param newTxtEmailAddress
   */
    public void setTxtEmailAddress(String newTxtEmailAddress) {
        txtEmailAddress = newTxtEmailAddress;
    }

  /**
   * getter method for txtStorage
   * @return String txtStorage
   */
    public String getTxtStorage() {
        return txtStorage;
    }

  /**
   * setter method for txtStorage
   * @param newTxtStorage
   */
    public void setTxtStorage(String newTxtStorage) {
        txtStorage = newTxtStorage;
    }
}
