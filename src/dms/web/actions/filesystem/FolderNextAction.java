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
 * $Id: FolderNextAction.java,v 1.0 2006/04/18 14:06:50 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
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
/**
 *	Purpose           : To display a hierarchy of folder(s) for a given path in 
 *                      flat_folder_list.jsp
 *  @author           : Suved Mishra
 *  @version          : 1.0
 * 	Date of creation  : 18-04-2006
 * 	Last Modified by  :   
 * 	Last Modified Date:   
 */
public class FolderNextAction extends Action  {
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
      String target = "success";
      FolderDocInfo folderDocInfo = null;
      HttpSession httpSession = null; 
      int setNo =0;
      ExceptionBean exceptionBean = null;
      try{
        logger.debug("Entering FolderNextAction... ");
        httpSession = (HttpSession)request.getSession(false);
        folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
        /* set the hierarchy setNo according to operation "oper" to be performed */
        setNo = folderDocInfo.getHierarchySetNo();
        logger.debug("setNo: "+setNo);
        if(request.getParameter("oper").equalsIgnoreCase("prev")){
          setNo -= 1;         /* oper = "prev" , decrement setNo by 1 */
        }else{
          setNo +=1;          /* oper = "next" , increment setNo by 1 */
        }
        logger.debug("setNo after manipulation: "+setNo);
        folderDocInfo.setHierarchySetNo(setNo);
        logger.debug("folderDocInfo: "+folderDocInfo);
      }catch(Exception ex){
        exceptionBean = new ExceptionBean(ex);
        logger.error("An Exception occurred in FolderNextAction... ");
        logger.error(exceptionBean.getErrorTrace());
        ex.printStackTrace();
      }
    logger.debug("Exiting FolderNextAction... ");    
    return mapping.findForward(target);
  }

}