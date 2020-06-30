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
 * $Id: FaxB4Action.java,v 20040220.9 2006/03/29 09:36:32 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.fax;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.actionforms.fax.FaxForm;
import dms.web.actionforms.filesystem.DocFaxForm;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.TimeZone;
/* Servlet API */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To populate faxing.jsp
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    02-07-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  01-03-2006  
 */
public class FaxB4Action extends Action {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering FaxB4Action now...");
    ActionErrors errors=new ActionErrors();
    String[] faxFileNames=null;
    try{   
      DocFaxForm docFaxForm = (DocFaxForm)form;
      logger.debug("docFaxForm in FaxB4Action : " + docFaxForm);
      HttpSession httpSession = request.getSession(false);
      UserInfo userInfo = (UserInfo)httpSession.getAttribute("UserInfo");
      
      DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
      Long[] documentIds = docFaxForm.getChkFolderDocIds();
      if( documentIds!=null && documentIds.length > 0 ){
        logger.debug("documentIds obtained successfully ");
        faxFileNames =new String[documentIds.length];
        for(int index=0; index < faxFileNames.length ; index++){
          faxFileNames[index]= dbsLibrarySession.getPublicObject(
                                          documentIds[index]).getAnyFolderPath();
          logger.info(faxFileNames[index]);
        }
      }
    }catch(DbsException dbsException){
      logger.error("An Exception occurred in FaxB4Action... ");
      logger.error(dbsException.toString());
      ActionError editError=new ActionError("errors.catchall",
                                             dbsException.getErrorMessage());
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }catch(Exception exception){
      logger.error("An Exception occurred in FaxB4Action... ");
      logger.error(exception.toString());
      ActionError editError=new ActionError("errors.catchall",exception);
      errors.add(ActionErrors.GLOBAL_ERROR,editError);
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return (mapping.getInputForward());
    }
    FaxForm faxForm=new FaxForm();
    if(faxFileNames!=null){
      faxForm.setLstAttachment(faxFileNames);
    }
    faxForm.setTimezone(TimeZone.getDefault().getID());
    request.setAttribute("FaxForm",faxForm);
    logger.info("Exiting FaxB4Action now...");
    return mapping.findForward("success");
  }
}
