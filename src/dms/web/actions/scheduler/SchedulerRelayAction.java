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
 * $Id: SchedulerRelayAction.java,v 20040220.6 2006/03/17 08:44:36 suved Exp $
 *****************************************************************************
 */ 
package dms.web.actions.scheduler;

/**
 *	Purpose: To look for an "operation" parameter and use it to find a forward 
 *           in the mapping and then use it.
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
//Servlet API
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//Struts API
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SchedulerRelayAction extends Action {

   /**
    * This is the main action called from the Struts framework.
    * @param mapping The ActionMapping used to select this instance.
    * @param form The optional ActionForm bean for this request.
    * @param request The HTTP Request we are processing.
    * @param response The HTTP Response we are processing.
    */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String operation = request.getParameter("operation");
        HttpSession httpSession = request.getSession(false);

        if(operation.equals("job_reschedule")
          ||operation.equals("job_delete")
          ||operation.equals("scheduler_start_stop")          
          ||operation.equals("page_scheduler")
          ||operation.equals("search_scheduler")          
          ||operation.equals("cancel_scheduler")) {
              httpSession.setAttribute("radSelect",request.getParameter("radSelect"));
              
              if(request.getParameter("txtSearchByUserName")!=null && !((String)request.getParameter("txtSearchByUserName")).equals("")){
                httpSession.setAttribute("txtSearchByUserName",request.getParameter("txtSearchByUserName"));
              }

              if(request.getParameter("cboSearchByJobType")!=null && !((String)request.getParameter("cboSearchByJobType")).equals("")){
                httpSession.setAttribute("cboSearchByJobType",request.getParameter("cboSearchByJobType"));
              }

              if(request.getParameter("txtCreateFromDate")!=null && !((String)request.getParameter("txtCreateFromDate")).equals("")){
                httpSession.setAttribute("txtCreateFromDate",request.getParameter("txtCreateFromDate"));
              }

              if(request.getParameter("txtCreateToDate")!=null && !((String)request.getParameter("txtCreateToDate")).equals("")){
                httpSession.setAttribute("txtCreateToDate",request.getParameter("txtCreateToDate"));
              }

              if(request.getParameter("txtDispatchFromDate")!=null && !((String)request.getParameter("txtDispatchFromDate")).equals("")){
                httpSession.setAttribute("txtDispatchFromDate",request.getParameter("txtDispatchFromDate"));
              }

              if(request.getParameter("txtDispatchToDate")!=null && !((String)request.getParameter("txtDispatchToDate")).equals("")){
                httpSession.setAttribute("txtDispatchToDate",request.getParameter("txtDispatchToDate"));
              }

              if(operation.equals("scheduler_start_stop")){
                if(request.getParameter("action")!=null && !((String)request.getParameter("action")).equals("")){
                    httpSession.setAttribute("action",request.getParameter("action"));
                }
              }
              
              httpSession.setAttribute("pageNumber",request.getParameter("txtPageNo"));       
            
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
