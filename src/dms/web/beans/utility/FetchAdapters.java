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
 * $Id: FetchAdapters.java,v 1.4 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;
/* Adapters package references */
import adapters.beans.XMLBean;
/* dms package references */
import dms.web.beans.security.AclPrefixBean;
/* Java API */
import java.io.File;
import java.util.ArrayList;

/**
 * Purpose: Bean called to fetch adapters according to given terms & conditions.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 14-01-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class FetchAdapters  {
  private String adapterFilePath;
  private XMLBean xmlBean;
  public FetchAdapters() {
  }
  
  public FetchAdapters( String adapterFilePath ){
    this.adapterFilePath = adapterFilePath;
    File file = new File(adapterFilePath);
    if( file.exists() ){
      xmlBean = new XMLBean(adapterFilePath);
    }else{
      xmlBean = null;
    }
  }

  /**
   * Purpose: Fetches all adapters including approvers as individual bean 
   *          containing adapterName and corresponding prefix.
   * @param: none
   */
  public ArrayList fetchList(){
    
    ArrayList adapters = new ArrayList();
    if( xmlBean != null ){
      ArrayList wfNames = xmlBean.getAllNames();
      AclPrefixBean prefixBean = null;
      /* add the general acl category,coz this has to be present always */
      prefixBean = new AclPrefixBean();
      prefixBean.setAdapterName("NonAdapter ACL");
      prefixBean.setAclPrefix("nonadapter");
      adapters.add(prefixBean);
      /* and approver workflow data */
      prefixBean = new AclPrefixBean();
      prefixBean.setAdapterName(getApproverWfName());
      prefixBean.setAclPrefix(getApproverPrefix());
      adapters.add(prefixBean);
      for( int index = 0; index < wfNames.size(); index++ ){
        /* add those adapters that are enabled */
        if( xmlBean.getValue(((String)wfNames.get(index)),"Enabled").equalsIgnoreCase("True") ){
          prefixBean = new AclPrefixBean();
          prefixBean.setAdapterName(xmlBean.getValue(((String)wfNames.get(index)),
                                    "Name"));
          prefixBean.setAclPrefix(xmlBean.getValue(((String)wfNames.get(index)),
                                  "PrefixName"));
          adapters.add(prefixBean);
        }
      }
    }
    return adapters;
  }

  /**
   * Purpose: Fetches all enabled adapters.
   * @param: none
   */
  public ArrayList fetchEnabledAdaptersList(){
    
    ArrayList adapters = new ArrayList();
    if( xmlBean != null ){
      ArrayList wfNames = xmlBean.getAllNames();
      for( int index = 0; index < wfNames.size(); index++ ){
        /* add those adapters that are enabled */
        if( xmlBean.getValue(((String)wfNames.get(index)),"Enabled").equalsIgnoreCase("True") ){
          adapters.add(xmlBean.getValue(((String)wfNames.get(index)),"Name"));
        }
      }
    }
    return adapters;
  }

  /**
   * Purpose: Fetches all available adapters excluding approver and watch.
   * @param: none
   */
  public ArrayList fetchAvailableAdaptersList(){
    ArrayList adapters = new ArrayList();
    if( xmlBean != null ){
      ArrayList wfNames = xmlBean.getAllNames();
      for( int index = 0; index < wfNames.size(); index++ ){
        /* add those adapters that are enabled */
        adapters.add(xmlBean.getValue(((String)wfNames.get(index)),"Name"));
      }
    }
    return adapters;
  }

  /**
   * Purpose:Fetches all adapter prefixes including approvers.
   * @param: none
   */
  public String getAllPrefixes(){
    String str = null;
    StringBuffer sb = new StringBuffer();
    if( xmlBean != null ){
      ArrayList wfNames = xmlBean.getAllNames();
      for( int index = 0; index < wfNames.size(); index++ ){
        sb.append(xmlBean.getValue(((String)wfNames.get(index)),"PrefixName"));
        sb.append(",");
      }
      sb.append(getApproverPrefix());
      /*if( sb.length() != 0 ){
        sb = sb.deleteCharAt(sb.length()-1);
      }*/
    }
    str = (sb.length() == 0)?new String(","):sb.toString();
    return str;
  }

  /**
   * Purpose:Fetches all enabled adapter prefixes including approvers.
   * @param: none
   */
  public String getEnabledPrefixes(){
    String str = null;
    StringBuffer sb = new StringBuffer();
    if( xmlBean!= null ){
      ArrayList wfNames = xmlBean.getAllNames();
      for( int index = 0; index < wfNames.size(); index++ ){
        if( xmlBean.getValue(((String)wfNames.get(index)),"Enabled").equalsIgnoreCase("True") ){
          sb.append(xmlBean.getValue(((String)wfNames.get(index)),"PrefixName"));
          sb.append(",");
        }
      }
      //sb.append(getApproverPrefix());
      if( sb.length() != 0 ){
        sb = sb.deleteCharAt(sb.length()-1);
      }
    }
    str = (sb.length() == 0)?new String(""):sb.toString();
    return str;
  }

  /**
   * Purpose:Fetches approver WorkFlow Prefix from Workflows.xml.
   * @param: aclName
   */
  private String getApproverPrefix(){
    String workFlowPath = this.adapterFilePath.replaceFirst("Adapters","Workflows");
    XMLBean xmlBean1 = new XMLBean(workFlowPath);
    String [] prefixes = xmlBean1.getAllValuesForTag("PrefixName");
    return prefixes[0];
  }

  /**
   * Purpose:Fetches approver WorkFlow Name from Workflows.xml.
   * @param: aclName
   */
  private String getApproverWfName(){
    String workFlowPath = this.adapterFilePath.replaceFirst("Adapters","Workflows");
    XMLBean xmlBean1 = new XMLBean(workFlowPath);
    String [] WfNames = xmlBean1.getAllValuesForTag("Name");
    return WfNames[0];
  }

  /**
   * Purpose:Fetches itemType for a given aclName.
   * @param: aclName
   */
  public String getItemType( String aclName ){
    String itemType = null;
    String prefix = null;
    if( xmlBean != null ){
      ArrayList wfNames = xmlBean.getAllNames();
      for( int index = 0; index < wfNames.size(); index++ ){
        prefix = xmlBean.getValue(((String)wfNames.get(index)),"PrefixName");
        if( aclName.startsWith(prefix) ){
          itemType = (String)wfNames.get(index);
        }
      }      
    }
    return itemType;
  }
}