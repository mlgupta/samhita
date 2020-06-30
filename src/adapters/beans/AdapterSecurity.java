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
 * $Id: AdapterSecurity.java,v 1.1 2006/02/02 06:42:00 Maneesh Mishra Exp $
 *****************************************************************************
 */ 
package adapters.beans;
/* Adapters package references */
import adapters.beans.AdaptersConstants;
/* Java API */
import java.util.ArrayList;
import java.util.Hashtable;
/* Struts API */
import org.apache.log4j.Logger;
/**
 *	Purpose: To restrict unauthorised entry to the various adapters' voucher 
 *           status and voucher report jsps.
 *  @author              Maneesh Mishra
 *  @version             1.0
 * 	Date of creation:    2006/02/02
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  2006/03/08
 *  The last modification consists of fetching ITypes of adapters from 
 *  AdapterConstants.java for PSFin,PSHR and SAP ( newly added adapters ). 
 */
public class AdapterSecurity  {
  public static boolean isAllowed(String userId,Hashtable managerTable,
                                  String relativePath){
    boolean isAllowed = false;
    Logger logger = Logger.getLogger("DbsLogger");
    try{
      XMLBean adapterXML=new XMLBean(relativePath);
      ArrayList iTypeNames = adapterXML.getAllNames();
      String prefix=null;
      for(int index = 0 ; index < iTypeNames.size() ; index++){
        String iTypeName=(String)iTypeNames.get(index);
        //if( AdaptersConstants.PSFIN_ITYPE.equalsIgnoreCase(iTypeName)){
          prefix=adapterXML.getValue(iTypeName,"PrefixName");
        //}else if( AdaptersConstants.PSHR_ITYPE.equalsIgnoreCase(iTypeName) ){
          //prefix=adapterXML.getValue(iTypeName,"PrefixName");
        //}else if( AdaptersConstants.SAP_ITYPE.equalsIgnoreCase(iTypeName) ) {
          //prefix=adapterXML.getValue(iTypeName,"PrefixName");
        //}else if( AdaptersConstants.ERP_ITYPE.equalsIgnoreCase(iTypeName) ){
          //prefix=adapterXML.getValue(iTypeName,"PrefixName");
        //}else{
          // for other adapters
        //}
        logger.debug("Prefix : "+prefix);
        if(prefix!=null){
          for(int prefixIndex = 0 ; prefixIndex < managerTable.size() ; prefixIndex++){
            ArrayList managerList = (ArrayList)managerTable.get(prefix);
            int managerListSize = (managerList == null)?0:managerList.size();
            for(int manIndex = 0 ; manIndex < managerListSize; manIndex++){
              if(userId.equalsIgnoreCase((String)managerList.get(manIndex))){
                isAllowed = true;
                break;
              }
            }
            if(isAllowed){
              return isAllowed; 
            }
          }
        }
      }
      
    }catch(Exception e){
      e.printStackTrace();
      logger.error(e.toString());
    }
    return isAllowed;
  }
}
