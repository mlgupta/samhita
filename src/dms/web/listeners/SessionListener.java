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
 * $Id: SessionListener.java,v 20040220.17 2006/03/14 05:27:10 suved Exp $
 *****************************************************************************
 */
package dms.web.listeners; 
/* dms package references */  
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.Treeview;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
/* Java API */
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/* Logger API */
import org.apache.log4j.Logger;

/**
 *	Purpose: This class is used to  dispose off user specific resources whenever his/her 
 *           session is expired/invalidated.
 *           This is a session listener class whose sessionCreated() and sessionDestroyed() methods are 
 *           called corrospondingly whenever the user,s http session is created and destroyed.
 * 
 * @author              Mishra Maneesh
 * @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class SessionListener  implements  HttpSessionListener
{
    ExceptionBean exceptionBean = null;
    UserInfo userInfo = null;
    UserPreferences userPreferences = null;
    FolderDocInfo folderDocInfo = null;
    HttpSession httpSession = null;
    Treeview treeview =null;
    Treeview treeview4Select =null;
    String userId;
    Logger logger= Logger.getLogger("DbsLogger");
    
  
   /* Methods for the HttpSessionListener */
   public void sessionCreated(HttpSessionEvent hse)
   {
      HttpSession httpSession=hse.getSession();
      //logger.info("Http Session created for user"+((UserInfo)httpSession.getAttribute("UserInfo")).getUserID());;
   }

    /**
     * Purpose : To provide definition for sessionDestroyed(HttpSessionEvent) function in HttpSessionListener
     *           interface.This function is called whenever user session is destroyed.
     *           Here the CMSDK Library Session for the user is disconnected,resources/files related to his/her treeview are freed
     *           and all other user specific session data is also cleaned up.
     * @param  : hse - HttpSessionEvent 
     * 
     */ 
   public void sessionDestroyed(HttpSessionEvent hse){
         logger.info("Session invalidation called.");
         logger.info("Starting Http Session cleanup.");                  
         HttpSession httpSession = hse.getSession();
         ServletContext context = httpSession.getServletContext();
         long  start = httpSession.getCreationTime();
         long  end = httpSession.getLastAccessedTime();
         UserInfo userInfo=(UserInfo)httpSession.getAttribute("UserInfo");
        if(userInfo!=null){
             DbsLibrarySession dbsLibrarySession=userInfo.getDbsLibrarySession();
             userId=userInfo.getUserID();
             if(context.getAttribute(userId)!=null){
                 context.removeAttribute(userId);
             }
             logger.debug("userId : " + userId);
             if(dbsLibrarySession.isConnected()){
                try{
                    logger.debug("Library session  "+ dbsLibrarySession.getId());
                    dbsLibrarySession.disconnect();            
                    logger.info("Library session  "+ dbsLibrarySession.getId()+" for user '" + userId + "' disconnected");
                }catch(DbsException logoutException){
                    ExceptionBean exceptionBean = new ExceptionBean(logoutException);
                    logger.error(exceptionBean.getMessage());
                    logger.debug(exceptionBean.getErrorTrace());
                }
            }else{
                logger.info("Library session for user '" + userId + "' is already disconnected");            
            }

            userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
            logger.debug("userPreferences : " + userPreferences);
            folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
            logger.debug("folderDocInfo : " + folderDocInfo);

            treeview =  (Treeview)httpSession.getAttribute("Treeview");

            if (treeview!=null){
                try{
                   treeview.free();
                   logger.info("Treeview removed.");
                }catch(DbsException dbsException){
                    ExceptionBean exceptionBean = new ExceptionBean(dbsException);
                    logger.error(exceptionBean.getMessage());
                    logger.debug(exceptionBean.getErrorTrace());
                }catch(Exception exception){
                    ExceptionBean exceptionBean = new ExceptionBean(exception);
                    logger.error(exceptionBean.getMessage());
                    logger.debug(exceptionBean.getErrorTrace());
                }
            }

            treeview4Select=(Treeview)httpSession.getAttribute("Treeview4Select");

            if (treeview4Select!=null){
                try{
                   treeview4Select.free();
                   logger.info("Treeview removed.");
                }catch(DbsException dbsException){
                    ExceptionBean exceptionBean = new ExceptionBean(dbsException);
                    logger.error(exceptionBean.getMessage());
                    logger.debug(exceptionBean.getErrorTrace());
                }catch(Exception exception){
                    ExceptionBean exceptionBean = new ExceptionBean(exception);
                    logger.error(exceptionBean.getMessage());
                    logger.debug(exceptionBean.getErrorTrace());
                }
            }

            userId = userInfo.getUserID();
            logger.debug("userId : " + userId);
            //Disconnect LibrarySession and remove UserInfo from the session
        
            userInfo.setDbsLibrarySession(null);            
            userInfo.setLocale(null);
            userInfo.setLogger(null);
            userInfo.setUserID(null);
            
            //Remove FolderDocInfo from the session
            folderDocInfo.setCurrentFolderId(null);
            folderDocInfo.setCurrentFolderPath(null);
            folderDocInfo.setHomeFolderId(null);
            folderDocInfo.setJsFileName(null);

            //Remove UserPreferences. from the session
            httpSession.removeAttribute("UserInfo");
            httpSession.removeAttribute("UserPreferences");
            httpSession.removeAttribute("FolderDocInfo");
            httpSession.removeAttribute("Treeview");
            logger.info("Time for which user '"+userId+"' stayed connected: "+((end - start)/60)+" seconds");
            logger.info("Http session for user '"+userId+"' invalidated");
        }else{
            logger.info("No attributes found in Http Session.");
        }
   }
}


 
