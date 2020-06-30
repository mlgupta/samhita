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
 * $Id: SubmitToAdapter.java,v 1.7 2006/02/02 12:33:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* Adapters Package references */
import adapters.beans.AddVouchersToQueue;
import adapters.generic.beans.GenericAdapterConstant;
import adapters.generic.wf.InitiateGenericWf;
import adapters.psfin.beans.PSFINAdapterConstant;
import adapters.psfin.wf.InitiatePSFINWf;
import adapters.pshr.beans.PSHRAdapterConstant;
import adapters.pshr.wf.InitiatePSHRWf;
import adapters.sap.beans.SAPAdapterConstant;
import adapters.sap.wf.InitiateSAPWf;
/* Java API */
import java.io.File;
import java.util.ArrayList;
/* Oracle Connection Pool API */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Logger API */
import org.apache.log4j.Logger;
/**
 * Purpose: Bean called to submit documents to adapter workflows.
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 14-01-2006
 * Last Modified Date : 07-03-2006
 * Last Modified By   : Suved Mishra
 */
public class SubmitToAdapter  {
  private String adapterFilePath;            // variable to store Adapters.xml path 
  private AddVouchersToQueue addVchrsToQ;    // represents AddVouchersToQueue bean
  private GetQueueCreated getQCreated;       // represents GetQueueCreated bean
  private Logger logger;                     // logger for verbose logging  
  private OracleConnectionCacheImpl wfConnCache; // Oracle Connection Pool
  private XMLBean xmlBean;                  // represents XMLBean

  public SubmitToAdapter() {
  }
  
  /**
   * Constructor for SubmitToAdapter Class
   * @param wfConnCache
   * @param adapterFilePath
   */
  public SubmitToAdapter(String adapterFilePath, OracleConnectionCacheImpl 
                         wfConnCache){
    this.adapterFilePath = adapterFilePath;
    logger = Logger.getLogger("DbsLogger");
    File file = new File(adapterFilePath);
    if( file.exists() ){
      xmlBean = new XMLBean(adapterFilePath);
      this.wfConnCache = wfConnCache;
      logger.info("SubmitToAdapter initialized successfully...");
    }else{
      xmlBean = null;
      this.wfConnCache = null;
      logger.info("SubmitToAdapter initialization failed...");
    }
  }

  /**
   * Purpose: It will bring the itemType from adapters.xml and call 
   *          GetQueueCreated to find out whether the que has been created.
   *          If not it will call appropriate InitiateXXXWf.java class 
   *          otherwise it will call appropriate AddVouchersToQueue.java.
   * @param:  userName,aclName,docIds.
   */
  public void submit(String userName,String aclName,Long [] docIds)
         throws Exception{
    ArrayList wfNames = null;
    String itemTypeName = null;
    String [] docsIds = getDocIds(docIds);
    if(xmlBean != null){
      wfNames = xmlBean.getAllNames();
      String prefixName = null;
      /* Find the appropriate itemType corresponding to aclName supplied */
      for( int index = 0; index < wfNames.size(); index++ ){
        prefixName = xmlBean.getValue((String)wfNames.get(index),"PrefixName");
        if( aclName.startsWith(prefixName) ){
          itemTypeName = (String)wfNames.get(index);
          break;
        }
      }
      if( itemTypeName!= null ){
        /* initialize GetQueueCreated bean */
        getQCreated = new GetQueueCreated(wfConnCache);
        String processName = getProcessName(itemTypeName);
        /* find out whether the que has been created */
        if( getQCreated.isQueueCreated(itemTypeName,aclName) ){
          /* if Q has been created it will call appropriate AddVouchersToQueue */
          addVchrsToQ = new AddVouchersToQueue(wfConnCache);
          if( processName != null ){
            addVchrsToQ.addVouchers(itemTypeName,processName,userName,aclName,
                                    docsIds);
            logger.debug("Vouchers submitted for addition ");
          }else{
            logger.debug("Unable to add vouchers");
          }
        }else{
          /* if Q has not been created it will call appropriate InitiateXXXWf */
          submitToInitiate(prefixName,itemTypeName,processName,userName,aclName,
                           docsIds);
          logger.debug("Submit to initialize done ...");
        }
      }else{
        logger.debug("Adapter not found ");
      }
    }else{
      logger.debug("Unable to submit to adapter");
    }
  }
  
  /**
   * Purpose: Fetches process name given the itemTypeName.
   * @param:  itemTypeName
   */
  private String getProcessName( String itemTypeName ){
    String processName = null;
    XMLBean xmlBean1 = null;
    String workFlowFilePath = null;
    
    workFlowFilePath = adapterFilePath.replaceFirst("Adapters","Workflows");
    File file = new File(workFlowFilePath);
    if( file.exists() ){
      xmlBean1 = new XMLBean(workFlowFilePath);
      processName = xmlBean1.getValue(itemTypeName,"ProcessName");
    }else{
      logger.debug("Unable to find workflows file");
    }
    return processName;
  }

  /**
   * Purpose: Converts Long[] ids to String[] ids and fetches them.
   * @param:  Long[] ids
   */
  private String[] getDocIds( Long [] docIds ){
    String [] docs = new String[docIds.length];
    for( int index = 0; index < docIds.length; index++ ){
      docs[index] = docIds[index].toString();
    }
    return docs;
  }
  
  /**
   * Purpose: This function is responsible for submitting all data to 
   *          appropriate initiate functions for adapters.
   * @param:  prefixName,itemTypeName,processName,userName,aclName,docIds.
   */
  private void submitToInitiate( String prefixName, String itemTypeName, 
        String processName, String userName, String aclName, String[] docsIds ) throws Exception {
    
    String relativePath = this.adapterFilePath.replaceFirst("Adapters",
                                                          "GeneralActionParam");
    // fetch managers for the acl...
    FetchManagers fetchManagers = new FetchManagers(relativePath);
    String managers = fetchManagers.getManagers(aclName);
    XMLBean adapterXML= new XMLBean(adapterFilePath);
    /* fetch the desired prefixes */
    String psfin_prefix = adapterXML.getValue(PSFINAdapterConstant.ITYPE,
                                            "PrefixName");
    String pshr_prefix = adapterXML.getValue(PSHRAdapterConstant.ITYPE,
                                             "PrefixName"); 
    String sap_prefix = adapterXML.getValue(SAPAdapterConstant.ITYPE,
                                             "PrefixName"); 
    String erp_prefix = adapterXML.getValue(GenericAdapterConstant.ITYPE,
                                             "PrefixName"); 
                                             
    if( managers != null ){
      
      if( prefixName.equalsIgnoreCase(psfin_prefix) ){
        // People Soft Financials initialization code goes here
        InitiatePSFINWf initiatePSFIN = new InitiatePSFINWf(this.wfConnCache);
        initiatePSFIN.startReqWF(itemTypeName,processName,userName,aclName,
                               managers,docsIds);
        logger.debug("People Soft Finance Wf Initiated successfully ");
      }else if( prefixName.equalsIgnoreCase(pshr_prefix) ){
        // People Soft HR initialization code goes here
        InitiatePSHRWf initiatePSHR = new InitiatePSHRWf(this.wfConnCache);
        initiatePSHR.startReqWF(itemTypeName,processName,userName,aclName,
                                managers,docsIds);
        logger.debug("People Soft HR Wf Initiated successfully ");
      }else if( prefixName.equalsIgnoreCase(sap_prefix) ){
        // SAP initialization code goes here
        InitiateSAPWf initiateSap = new InitiateSAPWf(this.wfConnCache);
        initiateSap.startReqWF(itemTypeName,processName,userName,aclName,
                                managers,docsIds);
        logger.debug("SAP Wf Initiated successfully ");
      }else if( prefixName.equalsIgnoreCase(erp_prefix) ) {
        // Generic ERP initialization code goes here
        InitiateGenericWf initiateErp = new InitiateGenericWf(this.wfConnCache);
        initiateErp.startReqWF(itemTypeName,processName,userName,aclName,
                                managers,docsIds);
        logger.debug("Generic ERP Wf Initiated successfully ");
        
      }else{
        // other adapters to be added here
      }
    }else{
      logger.debug("No Managers Found , hence no initialization done");
    }
  }
}
