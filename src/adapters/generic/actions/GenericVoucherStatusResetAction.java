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
 * $Id: GenericVoucherStatusResetAction.java,v 1.0 2006/06/21 06:42:00 suved Exp $
 *****************************************************************************
 */
package adapters.generic.actions;
/* adapters package references */
import adapters.generic.beans.GenericAdapterConstant;
import adapters.generic.beans.GenericVoucherStatusListBean;
import adapters.generic.wf.GenericResetVouchersWf;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.user.UserInfo;
/* Java API */
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Oracle Connection Pool*/
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To reset the GenericVoucherStatus from Inqueue to Inprocess
 *  @author             Suved Mishra
 *  @version            1.0
 * 	Date of creation:   2006/06/21
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GenericVoucherStatusResetAction extends Action  {
  DbsLibrarySession dbsLibrarySession = null;
  //Initialize logger
  Logger logger = Logger.getLogger("DbsLogger");
/**
 * This is the main action called from the Struts framework.
 * @param mapping The ActionMapping used to select this instance.
 * @param form The optional ActionForm bean for this request.
 * @param request The HTTP Request we are processing.
 * @param response The HTTP Response we are processing.
 */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    UserInfo userInfo = null;
    HttpSession httpSession = null;
    ActionErrors errors = new ActionErrors();
    logger.info("Entering GenericVoucherStatusResetAction now...");
    httpSession = request.getSession(false);
    userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
    OracleConnectionCacheImpl wfConnCache= (OracleConnectionCacheImpl)
                  httpSession.getServletContext().getAttribute("wfConnCache");
    dbsLibrarySession = userInfo.getDbsLibrarySession();
    ArrayList voucherArray = new ArrayList();
    String[] voucherToBeReset=null;
    String aclName=null;
    if (httpSession.getAttribute("voucherArrayList")!=null){
      voucherArray = (ArrayList)httpSession.getAttribute("voucherArrayList");
      aclName = ((GenericVoucherStatusListBean)voucherArray.get(0)).getAclName();
    }
    httpSession.removeAttribute("voucherArrayList");
    if (httpSession.getAttribute("radSelect")!=null){
      voucherToBeReset= (String[])httpSession.getAttribute("radSelect");
    }
    logger.debug("voucherArray.size()" + voucherArray.size());
    logger.debug("voucherToBeReset.length" + voucherToBeReset.length);
      
    GenericResetVouchersWf resetVouchers= new GenericResetVouchersWf(wfConnCache);
    try{
      if((aclName!=null) && (voucherToBeReset.length>0)){
        resetVouchers.reset(GenericAdapterConstant.ITYPE,aclName,voucherToBeReset);
        logger.info("Exiting GenericVoucherStatusResetAction now...");
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.VoucherResetSuccessful");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        saveMessages(request,actionMessages);
      }
    }catch (Exception exception){
      logger.error("Exception occured in GenericVoucherStatusResetAction" + exception.toString());
      ActionErrors actionErrors = new ActionErrors();
      ActionError actionError = new ActionError("errors.VoucherResetUnSuccessful");
      actionErrors.add(ActionErrors.GLOBAL_ERROR,actionError);
      saveMessages(request,actionErrors);
    }
    return mapping.findForward("success");
  }
}
