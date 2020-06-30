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
 * $Id: ApproveRequisition.java,v 1.7 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* dms package references */
import dms.web.beans.wf.docApprove.ChangeDocWfStatusBean;
/* Oracle API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
/**
 *	Purpose:             To approve requisition for approval workflow .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    27-04-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class ApproveRequisition extends WFFunctionAPI {
  ChangeDocWfStatusBean docWfStatusBean = null;
  
  public boolean execute(WFContext pWCtx){
    ErrorStack es = pWCtx.getWFErrorStack();            
    try{
      WFAttribute lAAttr = new WFAttribute(); 
      WFAttribute relativePath = new WFAttribute(); 
      WFAttribute docId = new WFAttribute();
      WFAttribute forwardTo = new WFAttribute();
      loadActivityAttributes(pWCtx, itemType, itemKey, actID); 
      loadItemAttributes(pWCtx);
      relativePath = getItemAttr("RELATIVE_PATH"); 
      docId = getItemAttr("DOC_ID");
      forwardTo = getItemAttr("FORWARD_TO");
      docWfStatusBean = new ChangeDocWfStatusBean((String)relativePath.getValue(),new Long((String)(docId.getValue())),"Approved");
      docWfStatusBean.forwardTo= (String)forwardTo.getValue();
      System.out.println("forwardTo: "+forwardTo);
      if(docWfStatusBean.changeWorkflowStatus()){
        docWfStatusBean.logEvent("Requisition Process Completed Successfully");
        docWfStatusBean.closeThisWfSession();                
      }else{
        docWfStatusBean.closeThisWfSession();              
      }        
    }catch (Exception e){ 
      es.addMessage("WF","WF_FN_ERROR"); 
      es.addToken("MODULE",this.getClass().getName());
      es.addToken("ITEMTYPE",itemType); 
      es.addToken("ITEMKEY",itemKey);
      es.addToken("ACTID",actID.toString());
      es.addToken("FUNCMODE",funcMode); 
      es.addToken("ERRMESSAGE",e.getMessage()); 
      return false;
    }
    return true; 
  }
}