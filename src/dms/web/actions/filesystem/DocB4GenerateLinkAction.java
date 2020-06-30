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
 * $Id: DocB4GenerateLinkAction.java,v 1.4 2006/06/08 12:54:41 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.actionforms.filesystem.LinkDetailsForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.user.UserInfo;
/*java API */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: Action called to fetch obtain_link_limits.jsp so that link limits in
 *          the form of dates and numerical limits can be placed for doc(s)(only)
 *          selected from folder-doc list. 
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 08-12-05
 * Last Modified Date : Suved Mishra
 * Last Modified By   : 01-03-2006
 */
public class DocB4GenerateLinkAction extends Action  {
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {

    String target= new String("success");
    DbsLibrarySession dbsLibrarySession= null;
    HttpSession httpSession= null;    
    ExceptionBean exceptionBean;
    ActionErrors errors=new ActionErrors();
    Locale locale = getLocale(request);
    ArrayList docLinkDetails= new ArrayList();
    FolderDocList folderDocList = null;
    DbsPublicObject dbsPO = null;
    LinkDetailsForm linkDetailsForm= null;
    
    Logger logger= Logger.getLogger("DbsLogger");
    logger.info("Entering DocB4GenerateLinkAction...");

    try{
      httpSession= request.getSession(false);
      FolderDocListForm folderDocListForm=(FolderDocListForm)form;
      logger.debug("folderDocListForm: "+folderDocListForm);

        /*obtain selected doc-ids */
      Long []selectedDocIds= folderDocListForm.getChkFolderDocIds();
      logger.debug("selectedDocIds length: "+selectedDocIds.length);
      
      UserInfo userInfo= (UserInfo)httpSession.getAttribute("UserInfo");
      logger.debug("UserInfo: "+userInfo);
      dbsLibrarySession= userInfo.getDbsLibrarySession();
      linkDetailsForm= new LinkDetailsForm();
      linkDetailsForm.setHdnTimeZone(TimeZone.getDefault().getID());
      for( int index = 0; index <selectedDocIds.length; index++ ){
        folderDocList = new FolderDocList();
        dbsPO = dbsLibrarySession.getPublicObject(selectedDocIds[index]);
        logger.debug("Name ["+index+"] : "+dbsPO.getName());
        folderDocList.setId(selectedDocIds[index]);
        folderDocList.setName(dbsPO.getName());
        folderDocList.setClassName(
                      (dbsPO.getResolvedPublicObject() instanceof DbsDocument)?
                      "Document":"Folder");
        Date currentDate = new Date();
        String formattedDate = DateHelperForFileSystem.format(currentDate,
                                                       "MM/dd/yyyy HH:mm:ss");
        folderDocList.setModifiedDate(formattedDate);
        folderDocList.setType("Enter the limit for "+index+" link view");
        docLinkDetails.add(folderDocList);
      }
      request.setAttribute("docLinkDetails",docLinkDetails);
      request.setAttribute("linkDetailsForm",linkDetailsForm);
    }catch(DbsException dex){
      exceptionBean= new ExceptionBean(dex);
      logger.error(exceptionBean.getMessage());
      logger.debug(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean= new ExceptionBean(ex);
      logger.error(exceptionBean.getMessage());
      logger.debug(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting DocB4GenerateLinkAction...");
    return mapping.findForward(target);
  }
}