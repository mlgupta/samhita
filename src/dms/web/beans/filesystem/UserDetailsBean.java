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
 * $Id: UserDetailsBean.java,v 1.0 2006/06/06 10:30:58 IST suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.web.beans.theme.Theme;
import dms.web.beans.theme.Theme.StyleTagPlaceHolder;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose            : Bean to facilitate set up user details viz user 
 *                      preferences , user Info and folderDocInfo and provide 
 *                      methods to return them .
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 06-06-06
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class UserDetailsBean  {

  private UserPreferences userPreferences;
  private UserInfo userInfo;
  private FolderDocInfo folderDocInfo; 
  private DbsLibrarySession dbsLibrarySession;
  private Logger logger;
  
  public UserDetailsBean() {
  }
  
  public UserDetailsBean(DbsLibrarySession dbsLibrarySession){
    logger = Logger.getLogger("DbsLogger");
    logger.info("Entering UserDetailsBean Constructor now...");
    this.dbsLibrarySession = dbsLibrarySession;
    logger.info("Exiting UserDetailsBean Constructor now...");
  }

  public FolderDocInfo getFolderDocInfo() {
    return folderDocInfo;
  }

  public void setFolderDocInfo(FolderDocInfo folderDocInfo) {
    this.folderDocInfo = folderDocInfo;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public UserPreferences getUserPreferences() {
    return userPreferences;
  }

  public void setUserPreferences(UserPreferences userPreferences) {
    this.userPreferences = userPreferences;
  }
  
  public void setDetails(Long folderId){
    String davpath=null;                          // represents webdav path
    String themeName=null;                        // theme name for the user
    Theme.StyleTagPlaceHolder styleTagPlaceHolder=null; // represents user theme
    try {
      if(dbsLibrarySession.isConnected()){
        logger.debug("DbsSession Id: "+dbsLibrarySession.getId());
        /* initialize userInfo, userPreferences,folderDocInfo */
        UserInfo userInfo = new UserInfo();
        FolderDocInfo folderDocInfo = new FolderDocInfo();
        UserPreferences userPreferences = new UserPreferences();
        DbsDirectoryUser connectedUser=dbsLibrarySession.getUser();
        //  for selected tree access level,displaying number of rows per page,
        //  Navigation type,Theme selected and Open Doc Option selected.
        if (dbsLibrarySession.getUser().getPropertyBundle()!=null){
          DbsPropertyBundle propertyBundleToEdit= 
                            dbsLibrarySession.getUser().getPropertyBundle();         
          DbsProperty property=propertyBundleToEdit.getProperty(
                                                    "PermittedTreeAccessLevel");                    
          if (property!=null){
            DbsAttributeValue attrValue=property.getValue();
            userPreferences.setTreeLevel(attrValue.getInteger(dbsLibrarySession)); 
          }
  
          property=propertyBundleToEdit.getProperty("ItemsToBeDisplayedPerPage");                    
          if (property!=null) {
            DbsAttributeValue attrValue=property.getValue();
            userPreferences.setRecordsPerPage(attrValue.getInteger(
                                                        dbsLibrarySession)); 
          }
  
          property=propertyBundleToEdit.getProperty("NavigationType");                    
          if (property!=null) {
            DbsAttributeValue attrValue=property.getValue();
            userPreferences.setNavigationType(attrValue.getInteger(
                                                        dbsLibrarySession)); 
          }
  
          property=propertyBundleToEdit.getProperty("Theme");                    
          if (property!=null) {
            DbsAttributeValue attrValue=property.getValue();
            themeName=attrValue.getString(dbsLibrarySession);
          }
          /* code added on account of open doc option */
          property=propertyBundleToEdit.getProperty("OpenDocOption");                  
          if (property!=null) {
            DbsAttributeValue attrValue=property.getValue();
            userPreferences.setChkOpenDocInNewWin(attrValue.getInteger(
                                                            dbsLibrarySession));     
          }
        }
        /* fetch theme name to initialize appropriate css theme files for the user */
        themeName=(themeName==null)?"Default":themeName;
        styleTagPlaceHolder= new StyleTagPlaceHolder(dbsLibrarySession,themeName);
        /* set style place holder */
        userPreferences.setStylePlaceHolder(styleTagPlaceHolder.getStylePlaceHolder());
        /* set the path for the icons to be used while displaying user folder tree */
        userPreferences.setTreeIconPath(styleTagPlaceHolder.getTreeIconPath());
        /* set userId as directory user name */
        userInfo.setUserID(connectedUser.getName());
        userInfo.setDbsLibrarySession(dbsLibrarySession);
        //set logger to be used in the application
        userInfo.setLogger(logger);
        //this locale should be obtained from DirectoryUser class
        //for the time being we will use default locale used by the system.
        DbsPrimaryUserProfile connectedUserProf=connectedUser.getPrimaryUserProfile();
        /* set locale for user information */
        userInfo.setLocale(connectedUserProf.getLocale());
        /* set language for user information */
        userInfo.setLanguage(connectedUserProf.getLanguage());
        /* set timezone for user information */
        userInfo.setTimeZone(connectedUserProf.getTimeZone());
        /* set character set for user information */
        userInfo.setCharSet(connectedUserProf.getCharacterSet());
        /* set web dav path for user information */
        userInfo.setDavPath(davpath);
        logger.debug("davpath: "  + davpath);
        logger.debug("Name : "+connectedUser.getDistinguishedName());
        /* set user status in user information */
        if (dbsLibrarySession.getUser().isSystemAdminEnabled()){
          userInfo.setSystemAdmin(true);
        }else{
          if(dbsLibrarySession.getUser().isAdminEnabled()){
            userInfo.setAdmin(true);
          }else{
          }
        }
        setUserInfo(userInfo);
        setUserPreferences(userPreferences);
        //check and set home folder id if it exist
        DbsFolder dbsFolder = connectedUserProf.getHomeFolder();
        if(dbsFolder == null){
          folderDocInfo.setHomeFolderId(null);
        }else{
          Long homeFolderId = dbsFolder.getId();
          logger.debug("homeFolderId : " + homeFolderId);
          folderDocInfo.setHomeFolderId(homeFolderId);
          logger.debug("folderDocInfo : " + folderDocInfo);

          folderDocInfo.setCurrentFolderId((folderId == null)?
                                            homeFolderId:folderId);
          String currentFolderPath = ( folderId == null )?
                dbsFolder.getAnyFolderPath():
                ((DbsFolder)dbsLibrarySession.getPublicObject(folderId)).getResolvedPublicObject().getAnyFolderPath();
          folderDocInfo.setCurrentFolderPath(currentFolderPath);
          folderDocInfo.setListingType(FolderDocInfo.SIMPLE_LISTING);
          folderDocInfo.setPageNumber(1);
        }
        setFolderDocInfo(folderDocInfo);
        logger.debug("userInfo : " + userInfo);
        logger.debug("userPreferences : " + userPreferences);
        logger.debug("folderDocInfo : " + folderDocInfo);
      }    
    }catch (DbsException dbsEx) {
      logger.error("An error occurred in UserDetailsBean ...");
      logger.error(dbsEx.toString());
    }catch (Exception ex) {
      logger.error("An error occurred in UserDetailsBean ...");
      logger.error(ex.toString());
    }
  }
}