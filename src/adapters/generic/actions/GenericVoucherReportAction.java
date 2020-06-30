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
 * $Id: GenericVoucherReportAction.java,v 1.0 2006/06/21 06:42:00 suved Exp $
 *****************************************************************************
 */ 
package adapters.generic.actions;
/* adapter package references */
import adapters.beans.AdapterSecurity;
import adapters.beans.XMLBean;
import adapters.generic.actionforms.GenericVoucherReportForm;
import adapters.generic.beans.GenericAdapterConstant;
import adapters.generic.wf.GetGenericVoucherReportWf;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.web.beans.scheduler.DateHelper;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.File;
import java.io.IOException;
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
 * Purpose: To populate erp_voucher_report.jsp with the specified data
 * @author              Suved Mishra
 * @version             1.0
 * Date of creation:    2006/06/21
 * Last Modified by :
 * Last Modified Date:
 */
public class GenericVoucherReportAction extends Action  {
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
    String voucherZone="";
    
    httpSession = request.getSession(false);
    userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
    String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
    String relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+ 
                         File.separator+"Adapters.xml";
    
    if (AdapterSecurity.isAllowed(userInfo.getUserID(),(Hashtable)httpSession.getAttribute("managerSet"),relativePath)){
      // Validate the request parameters specified by the user
      GenericVoucherReportForm genericVoucherReportForm=new GenericVoucherReportForm();
      ActionErrors errors = new ActionErrors();
      try{
        logger.info("Entering GenericVoucherReportAction,Serving JSP for Voucher Report");
        OracleConnectionCacheImpl wfConnCache= (OracleConnectionCacheImpl)httpSession.getServletContext().getAttribute("wfConnCache");
        
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        genericVoucherReportForm.setTimezone(TimeZone.getDefault().getID());
        XMLBean xmlBean = new XMLBean(relativePath);
        String erp_prefix = xmlBean.getValue(GenericAdapterConstant.ITYPE,"PrefixName");
        aclList= SearchUtil.listAclSelect(dbsLibrarySession,erp_prefix);
          //Finds the ACL array returned by search and set default
          logger.debug("aclList.length : " + aclList.length);
          if (aclList.length>0){
            aclListString=new String[aclList.length];
            for(int index=0; index<aclList.length; index++){
              logger.debug("ACL Name : " + aclList[index].getAccessControlList().getName().toString());
              aclListString[index]=aclList[index].getAccessControlList().getName().toString();
              if(index==0){
                voucherZone=aclListString[index];
              }
            }
            genericVoucherReportForm.setCboVoucherZone(aclListString);
          }else{
            genericVoucherReportForm.setCboVoucherZone(new String [] {"No Zone Present"});
          }

        if(httpSession.getAttribute("txtFromDate")!=null){
          fromDate=(String)httpSession.getAttribute("txtFromDate");
          dCreateFromDate=DateHelper.parse(fromDate,DATE_FORMAT);
          fromDateFormated="'" + DateHelper.format(dCreateFromDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtFromDate");
          genericVoucherReportForm.setTxtFromDate(fromDate);
        }

        if(httpSession.getAttribute("txtToDate")!=null){
          toDate=(String)httpSession.getAttribute("txtToDate");
          dCreateToDate=DateHelper.parse(toDate,DATE_FORMAT);
          toDateFormated="'" + DateHelper.format(dCreateToDate,"dd-MMM-yyyy") + "'";
          httpSession.removeAttribute("txtToDate");
          genericVoucherReportForm.setTxtToDate(toDate);
        }

        if (httpSession.getAttribute("voucherZone")!=null){
          voucherZone=(String)httpSession.getAttribute("voucherZone");
          genericVoucherReportForm.setVoucherZone(voucherZone);
          httpSession.removeAttribute("voucherZone");
        }else{
          genericVoucherReportForm.setVoucherZone(voucherZone);
        }
        
        GetGenericVoucherReportWf getVoucherReport= new GetGenericVoucherReportWf(wfConnCache);
        Hashtable voucherReportHashtable=new Hashtable();
        //Fetch the data from GetVoucher Report based on the search conditions and store it in the hashtable
        voucherReportHashtable=getVoucherReport.getReport(GenericAdapterConstant.ITYPE,voucherZone,toDateFormated,fromDateFormated);
        
        //Extract data from hashtable and store it in GenericVoucherReportForm
        if(!voucherReportHashtable.isEmpty()){
          genericVoucherReportForm.setTxtInQueueVoucher(voucherReportHashtable.get("NUM_INQUEUE").toString());
          genericVoucherReportForm.setTxtInProcessVoucher(voucherReportHashtable.get("NUM_INPROCESS").toString());
          genericVoucherReportForm.setTxtProcessedVoucher(voucherReportHashtable.get("NUM_PROCESSED").toString());
          genericVoucherReportForm.setTxtTotalVouchers(voucherReportHashtable.get("NUM_TOTAL").toString());
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
  
        request.setAttribute("GenericVoucherReportForm",genericVoucherReportForm);
        logger.info("Exiting GenericVoucherReportAction successfully");
      }catch(DbsException dbsException){
        logger.error("Exception occured in GenericVoucherReportAction");
        ActionError editError=new ActionError("errors.catchall",dbsException.getErrorMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,editError);
      }catch(Exception exception){
        logger.error("Exception occured in GenericVoucherReportAction");
        ActionError editError=new ActionError("errors.catchall",exception);
        exception.printStackTrace();
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
