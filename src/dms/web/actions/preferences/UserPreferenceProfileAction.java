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
 * $Id: UserPreferenceProfileAction.java,v 20040220.30 2006/03/13 14:17:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.preferences;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsPropertyBundleDefinition;
import dms.web.actionforms.preferences.UserPreferenceProfileForm;
import dms.web.beans.theme.Theme.StyleTagPlaceHolder;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
/**
 *	Purpose: To save user_preference_profile.jsp with the specified data for the
 *           logged in User.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    16-02-2004
 * 	Last Modified by :    Suved Mishra 
 * 	Last Modified Date:   27-12-2004. 
 */
public class UserPreferenceProfileAction extends Action {
  DbsLibrarySession dbsLibrarySession = null;
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //initializing logger
    Logger logger = Logger.getLogger("DbsLogger");
    //initializing variables
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ArrayList memberList=new ArrayList();
    String[] credentialManager=null; 
    int treeAccesslevel=0;
    int itemsToBeDisplayedPerPage=0;
    int navigationType=2;
    int chkOpenDocInNewWin=2; /*variable for doc open option */
    String themeName = null;
    DbsDirectoryUser userToEdit=null;
    // Validate the request parameters specified by the user
    ActionErrors errors = new ActionErrors();
    UserPreferenceProfileForm userPreferenceProfileForm;
    try{
      httpSession = request.getSession(false);
      logger.info("Entering UserPreferenceProfileAction now...");
      userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
      UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
      dbsLibrarySession = userInfo.getDbsLibrarySession();
      DbsDirectoryUser editUser= dbsLibrarySession.getUser();
      logger.debug("Saving User Preference Profile for : " + editUser);
      userPreferenceProfileForm=(UserPreferenceProfileForm)form;
      treeAccesslevel= Integer.parseInt(PropertyUtils.getSimpleProperty(form, "txtPermittedTreeAccessLevel").toString());
      itemsToBeDisplayedPerPage=Integer.parseInt(PropertyUtils.getSimpleProperty(form, "txtItemsToBeDisplayedPerPage").toString());
      themeName=PropertyUtils.getSimpleProperty(form, "cboSelectTheme").toString();
      if (Integer.parseInt(PropertyUtils.getSimpleProperty(form, "cboNavigationType").toString())==UserPreferences.FLAT_NAVIGATION) {
        navigationType=userPreferences.FLAT_NAVIGATION;
      }else if ( Integer.parseInt(PropertyUtils.getSimpleProperty(form, "cboNavigationType").toString())==UserPreferences.TREE_NAVIGATION ) {
        navigationType=userPreferences.TREE_NAVIGATION;            
      }
      /* code added on account of doc open option */ 
      logger.debug("userPreferenceProfileForm.getBoolChkOpenDocInNewWin(): "+userPreferenceProfileForm.getBoolChkOpenDocInNewWin());
      if(userPreferenceProfileForm.getBoolChkOpenDocInNewWin())
        chkOpenDocInNewWin=UserPreferences.IN_NEW_WINDOW;
      else if(!userPreferenceProfileForm.getBoolChkOpenDocInNewWin())
        chkOpenDocInNewWin=UserPreferences.IN_SAME_FRAME;
      logger.debug("chkOpenDocInNewWin in UserPreferenceProfileAction: "+chkOpenDocInNewWin);            
      
      if(editUser.getPropertyBundle()!=null){
        editUser.getPropertyBundle().putPropertyValue("PermittedTreeAccessLevel",DbsAttributeValue.newAttributeValue(treeAccesslevel));
        editUser.getPropertyBundle().putPropertyValue("ItemsToBeDisplayedPerPage",DbsAttributeValue.newAttributeValue(itemsToBeDisplayedPerPage));
        editUser.getPropertyBundle().putPropertyValue("Theme",DbsAttributeValue.newAttributeValue(themeName));
        editUser.getPropertyBundle().putPropertyValue("NavigationType",DbsAttributeValue.newAttributeValue(navigationType));
        /*added code */ 
        editUser.getPropertyBundle().putPropertyValue("OpenDocOption",DbsAttributeValue.newAttributeValue(chkOpenDocInNewWin));
        logger.debug("editUser.getPropertyBundle().putPropertyValue: "+editUser.getPropertyBundle().getPropertyValue("OpenDocOption").getInteger(dbsLibrarySession));
      }else{
        DbsPropertyBundleDefinition dbsPropertyBundleDef = new DbsPropertyBundleDefinition(dbsLibrarySession);
        dbsPropertyBundleDef.setAttribute(DbsPropertyBundle.CLASS_NAME,DbsAttributeValue.newAttributeValue("User Profile Preference"));
        dbsPropertyBundleDef.addPropertyValue("PermittedTreeAccessLevel",DbsAttributeValue.newAttributeValue(treeAccesslevel));
        dbsPropertyBundleDef.addPropertyValue("ItemsToBeDisplayedPerPage",DbsAttributeValue.newAttributeValue(itemsToBeDisplayedPerPage));
        dbsPropertyBundleDef.addPropertyValue("Theme",DbsAttributeValue.newAttributeValue(themeName));
        dbsPropertyBundleDef.addPropertyValue("NavigationType",DbsAttributeValue.newAttributeValue(navigationType));
        /*added code */ 
        dbsPropertyBundleDef.addPropertyValue("OpenDocOption",DbsAttributeValue.newAttributeValue(chkOpenDocInNewWin));                
        DbsPropertyBundle dbsPropertyBundle= dbsLibrarySession.createPublicObject(dbsPropertyBundleDef);
        editUser.setPropertyBundle(dbsPropertyBundle);
      }
      userPreferences.setRecordsPerPage(itemsToBeDisplayedPerPage);
      userPreferences.setNavigationType(navigationType);
      /* added code */
      userPreferences.setChkOpenDocInNewWin(chkOpenDocInNewWin);
      StyleTagPlaceHolder styleTagPlaceHolder= new StyleTagPlaceHolder(dbsLibrarySession,themeName);
      userPreferences.setStylePlaceHolder(styleTagPlaceHolder.getStylePlaceHolder());
      
      logger.debug("userPreferenceProfileForm : " + userPreferenceProfileForm);
      logger.debug("User Preference Profile saved for : " + editUser);            
    }catch(DbsException dbsException) {
      logger.error("An Exception occurred in UserPreferenceProfileAction... ");
      logger.error("User Preference Profile Action Aborted.");                          
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());       
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception) {
      logger.error("An Exception occurred in UserPreferenceProfileAction... ");
      logger.error("User Preference Profile Action Aborted.");                          
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);       
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    logger.info("Exiting UserPreferenceProfileAction now...");
    return mapping.findForward("success");
  }
}
