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
 * $Id: PSHRVoucherStatusListAction.java,v 1.0 2006/03/07 06:42:00 suved Exp $
 *****************************************************************************
 */ 
package adapters.pshr.actions;
/* adapters package references */
import adapters.beans.AdapterSecurity;
import adapters.beans.XMLBean;
import adapters.pshr.actionforms.PSHRVoucherStatusListForm;
import adapters.pshr.beans.ListPSHRVouchers;
import adapters.pshr.beans.PSHRAdapterConstant;
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
 *	Purpose: To populate pshr_voucher_status_list.jsp with the specified data
 *  @author             Suved Mishra
 *  @version            1.0
 * 	Date of creation:   2006/03/07
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class PSHRVoucherStatusListAction extends Action  {
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
      PSHRVoucherStatusListForm psHRVoucherStatusListForm=new PSHRVoucherStatusListForm();    
      ActionErrors errors = new ActionErrors();
      try{
        logger.info("Entering PSHRVoucherStatusListAction now...");
        UserPreferences userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");        
        OracleConnectionCacheImpl wfConnCache= (OracleConnectionCacheImpl)httpSession.getServletContext().getAttribute("wfConnCache");
        numRecords= userPreferences.getRecordsPerPage();
        if(httpSession.getAttribute("pageNumber")!=null){
          pageNumber=Integer.parseInt(httpSession.getAttribute("pageNumber").toString());
          httpSession.removeAttribute("pageNumber");
        }
        XMLBean adapterXML= new XMLBean(relativePath);
        String prefix = adapterXML.getValue(PSHRAdapterConstant.ITYPE,"PrefixName");              
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        psHRVoucherStatusListForm.setTimezone(TimeZone.getDefault().getID());
        
        logger.debug("Fetching ACL with Prefix for Peoplesoft");
        aclList= SearchUtil.listAclSelect(dbsLibrarySession,prefix);
        
        logger.debug("aclList.length : " + aclList.length);
        
        //Finds the ACE based on the ACL array returned by search
        if (aclList.length>0){
          aclListString=new String[aclList.length];
          for(int index=0; index<aclList.length; index++){
            aclListString[index]=aclList[index].getAccessControlList().getName().toString();
            if(index==0){
              invoiceZone=aclListString[index]=aclList[index].getAccessControlList().getName().toString();
            }
          }
          psHRVoucherStatusListForm.setCboInvoiceZone(aclListString);
        }else{
          psHRVoucherStatusListForm.setCboInvoiceZone(new String [] {"No Zone Present"});
        }
        
        if(httpSession.getAttribute("txtFromDate")!=null){
          fromDate=(String)httpSession.getAttribute("txtFromDate");
          dCreateFromDate=DateHelper.parse(fromDate,DATE_FORMAT);
          fromDateFormated="'" + DateHelper.format(dCreateFromDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtFromDate");
          psHRVoucherStatusListForm.setTxtFromDate(fromDate);
        }

        if(httpSession.getAttribute("txtToDate")!=null){
          toDate=(String)httpSession.getAttribute("txtToDate");
          dCreateToDate=DateHelper.parse(toDate,DATE_FORMAT);
          toDateFormated="'" + DateHelper.format(dCreateToDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtToDate");
          psHRVoucherStatusListForm.setTxtToDate(toDate);
        }
          
        if (httpSession.getAttribute("voucherZone")!=null){
          invoiceZone=(String)httpSession.getAttribute("voucherZone");
          psHRVoucherStatusListForm.setVoucherZone(invoiceZone);
          httpSession.removeAttribute("voucherZone");
        }else{
          psHRVoucherStatusListForm.setVoucherZone(invoiceZone);
        }
        
        if(httpSession.getAttribute("cboInvoiceStatus")!=null){
          invoiceStatus=(String)httpSession.getAttribute("cboInvoiceStatus");
          psHRVoucherStatusListForm.setCboInvoiceStatus(new String[] {PSHRAdapterConstant.INQUEUE});
          psHRVoucherStatusListForm.setVoucherStatus(invoiceStatus);
          psHRVoucherStatusListForm.setHdnType(invoiceStatus);
          httpSession.removeAttribute("cboInvoiceStatus");
        }else{
          psHRVoucherStatusListForm.setVoucherStatus(PSHRAdapterConstant.INQUEUE);
          invoiceStatus=PSHRAdapterConstant.INQUEUE;
          psHRVoucherStatusListForm.setHdnType(invoiceStatus);
        }
        
        ArrayList voucherArrayList= new ArrayList();
        ListPSHRVouchers listVouchers= new ListPSHRVouchers(wfConnCache);
    
        //Fetch the data from ListVoucher based on the search conitions and returns ArrayList with the data stored in PSFINVoucherStatusListBean.java
        logger.debug("Fetching the data from ListVoucher based on search condition ");
        
        voucherArrayList=listVouchers.getList(PSHRAdapterConstant.ITYPE,invoiceZone,invoiceStatus,fromDateFormated,toDateFormated,dbsLibrarySession,pageNumber,numRecords); 
        
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
        psHRVoucherStatusListForm.setTxtPageCount(new String().valueOf(pageCount));
        logger.debug("pageCount : " + pageCount);                        

        psHRVoucherStatusListForm.setTxtPageNo(new String().valueOf(pageNumber));
        logger.debug("pageNumber : " + pageNumber);             
          
        httpSession.setAttribute("voucherArrayList",voucherArrayList);
        request.setAttribute("PSHRVoucherStatusListForm",psHRVoucherStatusListForm);
        logger.info("Exiting PSHRVoucherStatusListAction now...");
      }catch(DbsException dbsException){
        logger.error("Exception occured in PSHRVoucherStatusListAction");
        logger.error(dbsException.toString());
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }catch(Exception exception){
        logger.error("Exception occured in PSHRVoucherStatusListAction");
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