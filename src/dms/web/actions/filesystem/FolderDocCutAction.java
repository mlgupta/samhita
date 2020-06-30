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
 * $Id: FolderDocCutAction.java,v 20040220.9 2006/05/19 06:24:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDocInfo;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To store selected items in buffer for cut / paste operation
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   05-03-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 02-03-2006  
 */
public class FolderDocCutAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering FolderDocCutAction now...");
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    HttpSession httpSession = null;
    FolderDocInfo folderDocInfo = null;
    try{
      httpSession = request.getSession(false);
      FolderDocListForm folderDocListForm = (FolderDocListForm)form;
      logger.debug("Storing \"Folder Or Doc to Cut\" in the buffer");
      logger.debug("folderDocListForm : " + folderDocListForm);
      folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
      folderDocInfo.setNoReloadTree(true);
      folderDocInfo.setClipBoard(folderDocListForm.getChkFolderDocIds());
      folderDocInfo.setClipBoardContent(FolderDocInfo.CLIPBOARD_CONTENT_CUT);
      logger.debug("folderDocInfo : " + folderDocInfo);
      
      ActionMessages actionMessages = new ActionMessages();
      ActionMessage actionMessage = new ActionMessage("msg.CutPerformed");
      actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
      httpSession.setAttribute("ActionMessages",actionMessages);

      logger.debug("Storing \"Folder Or Doc to Cut\" in the buffer complete");
    }catch(Exception ex){
      exceptionBean = new ExceptionBean(ex);
      logger.error("An Exception occurred in FolderDocCutAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting FolderDocCutAction now...");
    return mapping.findForward(forward);
  }
}
