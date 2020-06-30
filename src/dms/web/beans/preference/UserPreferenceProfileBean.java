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
 * $Id: UserPreferenceProfileBean.java,v 20040220.5 2006/03/14 06:14:34 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.preference;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 *	Purpose: To populate User Preference Profile  in user_preference_profile.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   15-02-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */ 
public class UserPreferenceProfileBean {
    private String userName=null;
    private String emailAddress=null;
    private String description=null;
    private String mailFolder=null;
    private String accessControlList=null;
    private String defaultDocumentAcl=null;
    private String defaultFolderAcl=null;
    private String homeFolder=null;
    private String language=null;
    private String storage=null;
    private String characterSet=null;
    private String locale=null;
    private String timeZone=null;
    
    /**
     * Purpose   : Returns the name of the User.
     * @return   : String
     */
    public String getUserName() {
        return userName;
    }

   /**
    * Purpose   : Sets the name of user.
    * @param    : newUserName Value of name 
    */
    public void setUserName(String newUserName) {
        userName = newUserName;
    }

    /**
     * Purpose   : Returns the EmailAddress.
     * @return   : String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

   /**
    * Purpose   : Sets the name of emailAddress.
    * @param    : newEmailAddress Value of name 
    */
    public void setEmailAddress(String newEmailAddress) {
        emailAddress = newEmailAddress;
    }

    /**
     * Purpose   : Returns the description.
     * @return   : String
     */
    public String getDescription() {
        return description;
    }

   /**
    * Purpose   : Sets the name of description.
    * @param    : newDescription Value of description
    */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**
     * Purpose   : Returns the mailFolder.
     * @return   : String
     */
    public String getMailFolder() {
        return mailFolder;
    }

   /**
    * Purpose   : Sets the name of mailFolder.
    * @param    : newMailFolder Value of mailFolder
    */
    public void setMailFolder(String newMailFolder) {
        mailFolder = newMailFolder;
    }

    /**
     * Purpose   : Returns the description.
     * @return   : String
     */
    public String getAccessControlList() {
        return accessControlList;
    }

   /**
    * Purpose   : Sets the name of accessControlList.
    * @param    : newAccessControlList Value of accessControlList
    */
    public void setAccessControlList(String newAccessControlList) {
        accessControlList = newAccessControlList;
    }

    /**
     * Purpose   : Returns the defaultDocumentAcl.
     * @return   : String
     */
    public String getDefaultDocumentAcl() {
        return defaultDocumentAcl;
    }

   /**
    * Purpose   : Sets the name of defaultDocumentAcl.
    * @param    : newDefaultDocumentAcl Value of defaultDocumentAcl
    */
    public void setDefaultDocumentAcl(String newDefaultDocumentAcl) {
        defaultDocumentAcl = newDefaultDocumentAcl;
    }

    /**
     * Purpose   : Returns the defaultFolderAcl.
     * @return   : String
     */
    public String getDefaultFolderAcl() {
        return defaultFolderAcl;
    }

   /**
    * Purpose   : Sets the name of defaultFolderAcl.
    * @param    : newDefaultFolderAcl Value of defaultFolderAcl
    */
    public void setDefaultFolderAcl(String newDefaultFolderAcl) {
        defaultFolderAcl = newDefaultFolderAcl;
    }

    /**
     * Purpose   : Returns the homeFolder.
     * @return   : String
     */
    public String getHomeFolder() {
        return homeFolder;
    }

   /**
    * Purpose   : Sets the name of homeFolder.
    * @param    : newHomeFolder Value of homeFolder 
    */
    public void setHomeFolder(String newHomeFolder) {
        homeFolder = newHomeFolder;
    }

    /**
     * Purpose   : Returns the language.
     * @return   : String
     */
    public String getLanguage() {
        return language;
    }

   /**
    * Purpose   : Sets the name of language.
    * @param    : newLanguage Value of language 
    */
    public void setLanguage(String newLanguage) {
        language = newLanguage;
    }

    /**
     * Purpose   : Returns the storage.
     * @return   : double
     */
    public String getStorage() {
        return storage;
    }

   /**
    * Purpose   : Sets the name of storage.
    * @param    : newStorage Value of storage 
    */
    public void setStorage(String newStorage) {
        storage = newStorage;
    }

    /**
     * Purpose   : Returns the characterSet.
     * @return   : String
     */
    public String getCharacterSet() {
        return characterSet;
    }

   /**
    * Purpose   : Sets the name of characterSet.
    * @param    : newCharacterSet Value of characterSet 
    */
    public void setCharacterSet(String newCharacterSet) {
        characterSet = newCharacterSet;
    }

    /**
     * Purpose   : Returns the locale.
     * @return   : String
     */
    public String getLocale() {
        return locale;
    }

   /**
    * Purpose   : Sets the name of locale.
    * @param    : newLocale Value of locale
    */
    public void setLocale(String newLocale) {
        locale = newLocale;
    }
    
    /**
     * Purpose   : Returns the timeZone.
     * @return   : String
     */
    public String getTimeZone() {
        return timeZone;
    }

   /**
    * Purpose   : Sets the name of timeZone.
    * @param    : newTimeZone Value of timeZone
    */
    public void setTimeZone(String newTimeZone) {
        timeZone = newTimeZone;
    }

    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            strTemp += "\n\tuserName : " + userName;
            strTemp += "\n\temailAddress : " + emailAddress;
            strTemp += "\n\tdescription : " + description;
            strTemp += "\n\tmailFolder : " + mailFolder;
            strTemp += "\n\taccessControlList : " + accessControlList;
            strTemp += "\n\tdefaultDocumentAcl : " + defaultDocumentAcl;
            strTemp += "\n\tdefaultFolderAcl : " + defaultFolderAcl;
            strTemp += "\n\thomeFolder : " + homeFolder;
            strTemp += "\n\tlanguage : " + language;
            strTemp += "\n\tstorage : " + storage;
            strTemp += "\n\tcharacterSet : " + characterSet;
            strTemp += "\n\tlocale : " + locale;
            strTemp += "\n\ttimeZone : " + timeZone;
        }
        return strTemp;
    }
}
