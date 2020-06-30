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
 * $Id: MoreApproversPresent.java,v 1.7 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* dms package references */  
import dms.web.beans.utility.WfAclUtil;
/* Java API */
import java.math.BigDecimal;
/* Oracle API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
import oracle.apps.fnd.wf.WFTwoDArray;
import oracle.apps.fnd.wf.engine.WFNotificationAPI;
/**
 *	Purpose:             To determine if there are more approvers remaining for 
 *                       approval workflow .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    27-04-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class MoreApproversPresent extends WFFunctionAPI {
  WFTwoDArray dataSource = null;
  BigDecimal myNid=null;

  public boolean execute(WFContext pWCtx){
    ErrorStack es = pWCtx.getWFErrorStack();
    WfAclUtil nextApprover = null;
    try{
      WFAttribute requestor = new WFAttribute();
      WFAttribute forward_from = new WFAttribute();
      WFAttribute relativePath = new WFAttribute();
      WFAttribute acl_Name = new WFAttribute();
      WFAttribute lIAttr = new WFAttribute(); 
      loadActivityAttributes(pWCtx, itemType, itemKey, actID);
      dataSource= WFNotificationAPI.getNotifications(pWCtx, itemType, itemKey);
      myNid= new BigDecimal((String)dataSource.getData(0,0));
      loadItemAttributes(pWCtx); 
      requestor = getItemAttr("REQUESTOR");
      forward_from = getItemAttr("FORWARD_FROM");
      relativePath = getItemAttr("RELATIVE_PATH");
      acl_Name = getItemAttr("ACL_NAME");
      String approverName=null;
      nextApprover=new WfAclUtil((String)requestor.getValue(),(String)relativePath.getValue(),(String)acl_Name.getValue());
      if(forward_from==null || (((String)forward_from.getValue()).trim().equalsIgnoreCase(""))){
        if(nextApprover.getWfAclName()!=null && nextApprover.getWfAces()){
          approverName=nextApprover.getNextApprover(null);
          if(nextApprover.isLast((String)forward_from.getValue())){
            resultOut="N";
          }else{
            resultOut="Y";
          }
        }
      }else{
        if(nextApprover.getWfAclName()!=null && nextApprover.getWfAces()){
          if(nextApprover.isLast((String)forward_from.getValue())){
            resultOut="N";
          }else{
            resultOut="Y";
          }  
        }
      }
    }catch(Exception e){ 
      es.addMessage("WF","WF_FN_ERROR"); 
      es.addToken("MODULE",this.getClass().getName()); 
      es.addToken("ITEMTYPE",itemType); 
      es.addToken("ITEMKEY",itemKey); 
      es.addToken("ACTID",actID.toString()); 
      es.addToken("FUNCMODE",funcMode); 
      es.addToken("ERRMESSAGE",e.getMessage()); 
      nextApprover.closeThisWfSession();
      return false; 
    }
    nextApprover.closeThisWfSession();
    return true; 
  }
}