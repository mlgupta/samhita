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
 * $Id: DisplayNothingAction.java,v 1.4 2006/02/02 12:26:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Purpose            : Action called to disconnect a given librarySession
 *                      after a document has been managed for a given adapter
 *                      viz: either upload a new image uploaded or dummy 
 *                      document generated and it's link submitted.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class DisplayNothingAction extends Action  {
  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    HttpSession httpSession = null;    /* http session for session management */
    UserInfo userInfo = null;          /* user specific information here */
    DbsLibrarySession dbsLibrarySession = null; /* user library session */
    try{
      logger.info("Entering DisplayNothingAction now...");
      httpSession = request.getSession(false);
      userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      if( userInfo != null ){
        /* obtain the library session for disconnection */
        dbsLibrarySession = userInfo.getDbsLibrarySession();
      }
    }catch(Exception e ){
      logger.error("Exception occurred in DisplayNothingAction ...");
      logger.error(e.toString());
    }finally{
      /* this finally block will disconnect the library session */
      try{
        if( dbsLibrarySession != null && dbsLibrarySession.isConnected() ){
          dbsLibrarySession.disconnect();
          logger.debug("Library Session disconnected successfully...");
          dbsLibrarySession = null;
        }
        if( httpSession != null ){
          httpSession.invalidate();
          logger.debug("Http Session invalidated successfully...");
          httpSession = null;
        }
      }catch(DbsException dbsEx){
        logger.error("Exception occurred in DisplayNothingAction ...");
        logger.error(dbsEx.toString());
        dbsLibrarySession = null;
      }
    }
    logger.debug("Exiting DisplayNothingAction now...");
    return mapping.findForward(null);
  }
}