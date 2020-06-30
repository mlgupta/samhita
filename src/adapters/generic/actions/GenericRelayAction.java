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
 * $Id: GenericRelayAction.java,v 1.0 2006/06/19 06:42:00 suved Exp $
 *****************************************************************************
 */ 
package adapters.generic.actions;
//Java API
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: To look for an "operation" parameter and use it to find a forward 
 *           in the mapping and then submit to it.
 * @author             Suved Mishra
 * @version            1.0
 * Date of creation:   2006/06/19
 * Last Modified by :     
 * Last Modified Date:    
 */
public class GenericRelayAction extends Action  {
 /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
    Logger logger=Logger.getLogger("DbsLogger");
    String operation = request.getParameter("operation");
    HttpSession httpSession = request.getSession(false);
    //Based on operation for Generic ERP Voucher Report suitable actions are selected
    //from struts config and sets the parameters in session
    if(operation.equals("search_voucher")
      ||operation.equals("delete_voucher")
      ||operation.equals("page_voucher")
      ||operation.equals("reset_voucher")) {
      try{
        httpSession.setAttribute("radSelect",(String [])PropertyUtils.getSimpleProperty(form,"radSelect"));
      }catch (InvocationTargetException e){
        logger.error(e.toString());
      }catch (NoSuchMethodException e){
        logger.error(e.toString());
      }catch (IllegalAccessException e){
        logger.error(e.toString());
      }
      if(request.getParameter("cboInvoiceStatus")!=null && 
         !((String)request.getParameter("cboInvoiceStatus")).equals("")){
        httpSession.setAttribute("cboInvoiceStatus",request.getParameter("cboInvoiceStatus"));
      }
      
      if(request.getParameter("voucherZone")!=null && 
         !((String)request.getParameter("voucherZone")).equals("")){
        httpSession.setAttribute("voucherZone",request.getParameter("voucherZone"));
      }
      
      if(request.getParameter("cboInvoiceZone")!=null && 
         !((String)request.getParameter("cboInvoiceZone")).equals("")){
        httpSession.setAttribute("cboInvoiceZone",request.getParameter("cboInvoiceZone"));
      }

      if(request.getParameter("txtFromDate")!=null && 
         !((String)request.getParameter("txtFromDate")).equals("")){
        httpSession.setAttribute("txtFromDate",request.getParameter("txtFromDate"));
      }

      if(request.getParameter("txtToDate")!=null && 
         !((String)request.getParameter("txtToDate")).equals("")){
        httpSession.setAttribute("txtToDate",request.getParameter("txtToDate"));
      }
      
      httpSession.setAttribute("pageNumber",request.getParameter("txtPageNo"));       
      
      if (operation.equals("reset_voucher")) {
        if(httpSession.getAttribute("voucherArrayList")!=null ){
          httpSession.setAttribute("voucherArrayList",httpSession.getAttribute("voucherArrayList"));
        }
      }
    }
    
    //Based on operation for Generic ERP Voucher Report suitable actions are selected
    //from struts config and sets the parameters in session
    if(operation.equals("search_report")){
      if(request.getParameter("voucherZone")!=null && 
         !((String)request.getParameter("voucherZone")).equals("")){
        httpSession.setAttribute("voucherZone",request.getParameter("voucherZone"));
      }

      if(request.getParameter("txtFromDate")!=null && 
         !((String)request.getParameter("txtFromDate")).equals("")){
        httpSession.setAttribute("txtFromDate",request.getParameter("txtFromDate"));
      }

      if(request.getParameter("txtToDate")!=null && 
         !((String)request.getParameter("txtToDate")).equals("")){
        httpSession.setAttribute("txtToDate",request.getParameter("txtToDate"));
      }
    }
    if (operation==null){
      return new ActionForward("error");
    }   
    if (mapping.findForward(operation)==null){
      return new ActionForward("error");
    }
    return mapping.findForward(operation);
  }
}
