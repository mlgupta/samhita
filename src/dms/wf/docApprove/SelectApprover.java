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
 * $Id: SelectApprover.java,v 1.6 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* dms package references */
import dms.web.beans.utility.WfAclUtil;
/* Workflow API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
import oracle.apps.fnd.wf.WFTwoDArray;
/**
 *	Purpose:             To select next approver for approval workflow .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    27-04-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class SelectApprover extends WFFunctionAPI {
  WFTwoDArray dataSource = null;
  
  public boolean execute(WFContext pWCtx){
    ErrorStack es = pWCtx.getWFErrorStack(); 
    WfAclUtil nextApprover=null;          
    try{
      WFAttribute requestor = new WFAttribute(); 
      WFAttribute forward_from = new WFAttribute();
      WFAttribute forward_to = new WFAttribute();
      WFAttribute relativePath = new WFAttribute();
      WFAttribute acl_Name = new WFAttribute(); 
      WFAttribute lIAttr = new WFAttribute(); 
      loadActivityAttributes(pWCtx, itemType, itemKey, actID); 
      loadItemAttributes(pWCtx); 
      requestor = getItemAttr("REQUESTOR"); 
      forward_from = getItemAttr("FORWARD_FROM");
      forward_to = getItemAttr("FORWARD_TO");
      relativePath = getItemAttr("RELATIVE_PATH");
      acl_Name = getItemAttr("ACL_NAME");       
      
      String approverName=null;
      System.out.println("acl_Name : "+(String)acl_Name.getValue());
      nextApprover=new WfAclUtil((String)requestor.getValue(),(String)relativePath.getValue(),(String)acl_Name.getValue());
      
      if(forward_from==null  ||  
        (((String)forward_from.getValue()).trim().equalsIgnoreCase(""))){
        if(nextApprover.getWfAclName()!=null && nextApprover.getWfAces()){
          approverName=nextApprover.getNextApprover(null);
          if(approverName!=null){
            forward_to.value((Object)approverName);
            setItemAttrValue(pWCtx, forward_to);
            resultOut= "T";
          }else{
            resultOut= "F";
          }
        }else{
          resultOut= "F";
        }
      }else{
        if(nextApprover.getWfAclName()!=null && nextApprover.getWfAces()){
          approverName=nextApprover.getNextApprover((String)forward_from.getValue());
          forward_to.value((Object)approverName);
          setItemAttrValue(pWCtx, forward_to); 
          resultOut= "T";
        }else{
          resultOut= "F";
        }
      }
    }catch (Exception e){ 
      e.printStackTrace();
      es.addMessage("WF","WF_FN_ERROR"); 
      es.addToken("MODULE",this.getClass().getName()); 
      es.addToken("ITEMTYPE",itemType); 
      es.addToken("ITEMKEY",itemKey); 
      es.addToken("ACTID",actID.toString()); 
      es.addToken("FUNCMODE",funcMode); 
      es.addToken("ERRMESSAGE",e.getMessage());
      resultOut="F";
      nextApprover.closeThisWfSession();
      return false; 
    }
    nextApprover.closeThisWfSession();
    return true; 
  }
}