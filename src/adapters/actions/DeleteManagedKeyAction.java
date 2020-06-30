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
 * $Id: DeleteManagedKeyAction.java,v 1.3 2006/02/02 12:24:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.actionforms.AdapterPreferenceForm;
import adapters.actionforms.ListManagedFoldersForm;
import adapters.beans.XMLBean;
//Java API
import java.io.File;
import java.io.IOException;
//Servlet API
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Purpose            : Action called to delete the keys selected for a given 
 *                      adapter from Adapters.xml.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 19-01-06
 * Last Modified Date : 23-01-06
 * Last Modified By   : Suved Mishra
 */

public class DeleteManagedKeyAction extends Action  {
  public ActionForward execute( ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws IOException,ServletException{
    Logger logger = Logger.getLogger("DbsLogger"); /* logger for verbose logging */
    ListManagedFoldersForm listManagedFoldersForm = null; /* form to fetch data */
    HttpSession httpSession = null;   /* http session for session management */
    XMLBean xmlBean = null;           /* xml bean for fetching tag attrs & values */
    try{
      logger.info("Entering DeleteManagedKeyAction now...");
      httpSession = request.getSession(false);
      listManagedFoldersForm = (ListManagedFoldersForm)form;
      /* fetch the prefix to find out the adapter name */
      String prefix = listManagedFoldersForm.getHdnPrefix();
      logger.debug("Prefix : "+prefix);
      /* fetch the keys that were selected for removal from Adapters.xml.*/
      String [] chkManagedFolderKeys = listManagedFoldersForm.getChkManagedFolderKeys();
      int keysSize = ( chkManagedFolderKeys == null )?0:chkManagedFolderKeys.length;
      String tagName = "key";
      /* fetch the adapters.xml file path */
      String adapterFilePath = httpSession.getServletContext().getRealPath("/")+
                               "WEB-INF"+File.separator+"params_xmls"+
                               File.separator+"Adapters.xml";
      xmlBean = new XMLBean(adapterFilePath);
      /* loop through to remove the <key> tags corresponding to the value of
       * chkManagedFolderKeys[].*/
      for( int index = 0; index < keysSize; index++ ){
        logger.debug("chkManagedFolderKeys["+index+"] : "+chkManagedFolderKeys[index]);
        xmlBean.removeKeyNode(tagName,chkManagedFolderKeys[index]);
      }
      AdapterPreferenceForm adapterForm = new AdapterPreferenceForm();
      adapterForm.setHdnPrefix(prefix);
      request.setAttribute("form",adapterForm);
    }catch( Exception e ){
      logger.error("Exception occurred in DeleteManagedKeyAction ...");
      logger.error(e.toString());      
    }
    logger.info("Exiting DeleteManagedKeyAction now...");
    return mapping.findForward("success");
  }

}