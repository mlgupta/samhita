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
 * $Id: GenericVoucherStatusListAction.java,v 1.0 2006/06/21 06:42:00 suved Exp $
 *****************************************************************************
 */
package adapters.generic.actions;
/* adapters package references */
import adapters.beans.AdapterSecurity;
import adapters.beans.XMLBean;
import adapters.generic.actionforms.GenericVoucherStatusListForm;
import adapters.generic.beans.ListGenericVouchersBean;
import adapters.generic.beans.GenericAdapterConstant;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// Oracle Connection Pool
import oracle.jdbc.pool.OracleConnectionCacheImpl;
//Struts API
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
/**
 *	Purpose: To populate erp_voucher_status_list.jsp with the specified data
 *  @author             Suved Mishra
 *  @version            1.0
 * 	Date of creation:   2006/06/21
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GenericVoucherStatusListAction extends Action  {
  DbsLibraryService dbsLibraryService = null;
  DbsCleartextCredential dbsCleartextCredential = null;
  DbsLibrarySession dbsLibrarySession = null;
  private static String DATE_FORMAT="MM/dd/yyyy";
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

    UserInfo userInfo = null;
    HttpSession httpSession = null;
    int numRecords=0;
    int pageNumber=1;
    int pageCount=0;
    String toDate="";
    String fromDate="";
    String fromDateFormated="";
    String toDateFormated="";
    Date dCreateToDate=null;
    Date dCreateFromDate=null;
    DbsAccessControlList[] aclList=null;
    String[] aclListString=null;
    String invoiceZone="";
    String invoiceStatus="";
    // Validate the request parameters specified by the user
    
    httpSession = request.getSession(false);
    userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
    String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
    String relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+ File.separator+"Adapters.xml";
    if (AdapterSecurity.isAllowed(userInfo.getUserID(),(Hashtable)httpSession.getAttribute("managerSet"),relativePath)){
      GenericVoucherStatusListForm genericVoucherStatusListForm=new GenericVoucherStatusListForm();
      ActionErrors errors = new ActionErrors();
      try{
        logger.info("Entering GenericVoucherStatusListAction now...");
        UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
        OracleConnectionCacheImpl wfConnCache= (OracleConnectionCacheImpl)httpSession.getServletContext().getAttribute("wfConnCache");
        numRecords= userPreferences.getRecordsPerPage();
        if(httpSession.getAttribute("pageNumber")!=null){
          pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
          httpSession.removeAttribute("pageNumber");
        }
        XMLBean adapterXML= new XMLBean(relativePath);
        String prefix = adapterXML.getValue(GenericAdapterConstant.ITYPE,"PrefixName");
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        genericVoucherStatusListForm.setTimezone(TimeZone.getDefault().getID());
        
        logger.debug("Fetching ACL with Prefix for Generic ERP");
        aclList= SearchUtil.listAclSelect(dbsLibrarySession,prefix);
        
        logger.debug("aclList.length : " + aclList.length);
        
        //Finds the ACE based on the ACL array returned by search
        if (aclList.length>0){
          aclListString=new String[aclList.length];
          for(int index=0; index<aclList.length; index++){
            aclListString[index]=aclList[index].getAccessControlList().getName().toString();
            if(index==0){
              invoiceZone=aclListString[index];
            }
          }
          genericVoucherStatusListForm.setCboInvoiceZone(aclListString);
        }else{
          genericVoucherStatusListForm.setCboInvoiceZone(new String [] {"No Zone Present"});
        }
        
        if(httpSession.getAttribute("txtFromDate")!=null){
          fromDate=(String)httpSession.getAttribute("txtFromDate");
          dCreateFromDate=DateHelper.parse(fromDate,DATE_FORMAT);
          fromDateFormated="'" + DateHelper.format(dCreateFromDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtFromDate");
          genericVoucherStatusListForm.setTxtFromDate(fromDate);
        }

        if(httpSession.getAttribute("txtToDate")!=null){
          toDate=(String)httpSession.getAttribute("txtToDate");
          dCreateToDate=DateHelper.parse(toDate,DATE_FORMAT);
          toDateFormated="'" + DateHelper.format(dCreateToDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtToDate");
          genericVoucherStatusListForm.setTxtToDate(toDate);
        }
          
        if (httpSession.getAttribute("voucherZone")!=null){
          invoiceZone=(String)httpSession.getAttribute("voucherZone");
          genericVoucherStatusListForm.setVoucherZone(invoiceZone);
          httpSession.removeAttribute("voucherZone");
        }else{
          genericVoucherStatusListForm.setVoucherZone(invoiceZone);
        }
        
        if(httpSession.getAttribute("cboInvoiceStatus")!=null){
          invoiceStatus=(String)httpSession.getAttribute("cboInvoiceStatus");
          genericVoucherStatusListForm.setCboInvoiceStatus(new String[] {GenericAdapterConstant.INQUEUE});
          genericVoucherStatusListForm.setVoucherStatus(invoiceStatus);
          genericVoucherStatusListForm.setHdnType(invoiceStatus);
          httpSession.removeAttribute("cboInvoiceStatus");
        }else{
          genericVoucherStatusListForm.setVoucherStatus(GenericAdapterConstant.INQUEUE);
          invoiceStatus=GenericAdapterConstant.INQUEUE;
          genericVoucherStatusListForm.setHdnType(invoiceStatus);
        }
        
        ArrayList voucherArrayList= new ArrayList();
        ListGenericVouchersBean listVouchers= new ListGenericVouchersBean(wfConnCache);
    
        //Fetch the data from ListVoucher based on the search conditions and returns ArrayList with the data stored in GenericVoucherStatusListBean.java
        logger.debug("Fetching the data from ListVoucher based on search condition ");
        
        voucherArrayList=listVouchers.getList(GenericAdapterConstant.ITYPE,invoiceZone,invoiceStatus,fromDateFormated,toDateFormated,dbsLibrarySession,pageNumber,numRecords);

        logger.debug("voucherArrayList.size() :" +  voucherArrayList.size());
        if (numRecords!=0){
          pageCount=Integer.parseInt(listVouchers.pageCountString);

          if(pageNumber>pageCount){
            pageNumber=pageCount;
          }
        }

        if(httpSession.getAttribute("messages")!=null) {
          logger.debug("Saving action message in request stream");
          saveMessages(request,(ActionMessages)httpSession.getAttribute("messages"));
          httpSession.removeAttribute("messages");
        }
        if(httpSession.getAttribute("errors")!=null) {
          logger.debug("Saving action errors in request stream");    
          saveErrors(request,(ActionErrors)httpSession.getAttribute("errors"));
          httpSession.removeAttribute("errors");
        }
        genericVoucherStatusListForm.setTxtPageCount(new String().valueOf(pageCount));
        logger.debug("pageCount : " + pageCount);

        genericVoucherStatusListForm.setTxtPageNo(new String().valueOf(pageNumber));
        logger.debug("pageNumber : " + pageNumber);             
          
        httpSession.setAttribute("voucherArrayList",voucherArrayList);
        request.setAttribute("GenericVoucherStatusListForm",genericVoucherStatusListForm);
        logger.info("Exiting GenericVoucherStatusListAction now...");
      }catch(DbsException dbsException){
        logger.error("Exception occured in GenericVoucherStatusListAction");
        logger.error(dbsException.toString());
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }catch(Exception exception){
        logger.error("Exception occured in GenericVoucherStatusListAction");
        logger.error(exception.toString());
        ActionError editError=new ActionError("errors.catchall",exception);
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }
      if(!errors.isEmpty()) {
        saveErrors(request, errors);
        return (mapping.getInputForward());
      }
      return mapping.findForward("success");
    }
    return mapping.findForward("notallowed");
  }  
}
