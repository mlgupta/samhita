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
 * $Id: AdapterWebtopAction.java,v 1.5 2006/02/02 12:22:58 IST rajan Exp $
 *****************************************************************************
 */
package adapters.actions;
/* adapter package references */
import adapters.beans.AdapterWebtopBean;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsAccessControlEntry;
import dms.beans.DbsAccessControlList;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.FetchAdapters;
import dms.web.beans.utility.SearchUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
/* Servlet API */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/** 
 * Purpose: To Check the Adapter whether its enable or disable, Creates the 
 *          hashTable with the Adapter type as Key and values as ArrayList
 * @author : Rajan Kamal Gupta
 * @version: 1.0
 * Date of Creation   :  09-01-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class AdapterWebtopAction extends Action  {
  DbsLibrarySession dbsLibrarySession = null;
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

      //variable declaration
      ExceptionBean exceptionBean;
      String forward = "success";
      HttpSession httpSession = null;
      DbsAccessControlList[] aclList=null;
      String buildSearchClause=null;
      UserInfo userInfo=null;
      int beanIndex=0; 
      String relativePath = null; 
      try{
        logger.info("Entering AdapterWebtpAction now...");
        httpSession = request.getSession(false);
        userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
        dbsLibrarySession = userInfo.getDbsLibrarySession();
        logger.info("AdapterWebtopAction Identifying adapters available and enabled");
        
        if(httpSession.getAttribute("managerSet")!=null){
            httpSession.removeAttribute("managerSet");
          }
        //Getting the path of the Adapters.xml from context
        String contextPath=(String)httpSession.getServletContext().getAttribute("contextPath");
        relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+ File.separator+"Adapters.xml";
        XMLBean xmlBean = new XMLBean(relativePath);
        
        //Fetches the values for tag PrefixName and Enabled from Adapters.xml and stores in Hashtable
        Hashtable hsEnable = new Hashtable();
        ArrayList allNames= xmlBean.getAllNames();
        int prefixCount=0;
        for (int index=0;index<allNames.size();index++) {
          hsEnable.put(xmlBean.getValue(allNames.get(index).toString(),"PrefixName"),xmlBean.getValue(allNames.get(index).toString(),"Enabled"));
          prefixCount++;
        }//for loop ends here
        xmlBean = null;
        
        Hashtable hsAdapter= new Hashtable();

        //Prepares hastable hsAdapter based on prefixName available in the form key and array of ACE's, 
        if (!hsEnable.isEmpty()){
          Enumeration e = hsEnable.keys();
          while(e.hasMoreElements()){
            buildSearchClause=null;
            Object obj= e.nextElement();
            logger.debug("Keys : " + obj.toString() + " values : " + hsEnable.get(obj));
            logger.debug(hsEnable.get(obj).toString());
            
            //Finds the available ACL with the prefix if the adapter is enabled in Adapters.xml
            if (hsEnable.get(obj).toString().equals("True")){
              buildSearchClause=obj.toString();

              aclList= SearchUtil.listAclSelect(dbsLibrarySession,buildSearchClause);
              
              //Finds the ACE based on the ACL array returned by search
              if (aclList.length>0){
                ArrayList aceList=new ArrayList();
                for(int index=0;index < aclList.length ; index ++) {
                  DbsAccessControlEntry[] acesInAcl=aclList[index].getAccessControlEntrys();
                  int totalAcesInAcl = ( acesInAcl == null )?0:acesInAcl.length;
                  logger.debug("acesInAcl.length : " + totalAcesInAcl);
                
                  for(int i=0; i < totalAcesInAcl; i++){
                    logger.debug(acesInAcl[i].getGrantee().getName());
                    aceList.add(acesInAcl[i].getGrantee().getName());
                  }
                }
                logger.debug("aceList.size()" + aceList.size());
                String prefix = new String(obj.toString());
                logger.debug("Prefix : "+prefix);
                hsAdapter.put(prefix, new ArrayList(aceList));
              }
            }
          }//while block ends here
          httpSession.setAttribute("managerSet",hsAdapter);
          Enumeration enumeration=hsAdapter.keys();
          
          ArrayList aceAvailable =new ArrayList();
          while(enumeration.hasMoreElements()){
            Object objElement= enumeration.nextElement();

            ArrayList test= (ArrayList) hsAdapter.get(objElement);
            Iterator iterator= test.iterator();
            logger.debug("Accessing from HashTable");
            
            while(iterator.hasNext()){
              String userID=null;
              userID=iterator.next().toString();
              if (userInfo.getUserID().equalsIgnoreCase(userID)) {
                  aceAvailable.add(objElement.toString());
              }
            }
          }
          logger.debug("aceAvailable.size() : "+aceAvailable.size());
          AdapterWebtopBean[] adapterWebtopBean=new AdapterWebtopBean[aceAvailable.size()];
          ArrayList adapters=new ArrayList();    
          xmlBean = new XMLBean(relativePath);
          for (int i=0 ; i< aceAvailable.size(); i++ ) {
            adapterWebtopBean[i]=new AdapterWebtopBean();
            adapterWebtopBean[i].setAdapterPrefixName(aceAvailable.get(i).toString());
            for( int j = 0; j < allNames.size(); j++ ){
              if(xmlBean.getValue(allNames.get(j).toString(),"PrefixName").equals(
                  adapterWebtopBean[i].getAdapterPrefixName())){
                adapterWebtopBean[i].setAdapterName(xmlBean.getValue(
                                      allNames.get(j).toString(),"Name"));
                adapterWebtopBean[i].setAdapterStatusListActionName(
                                      xmlBean.getValue(
                                      allNames.get(j).toString(),"ConstantName").toLowerCase()+
                                      "VoucherStatusListAction.do");
              }
            }
            adapters.add(adapterWebtopBean[i]);                    
          }
          logger.debug("Arraylist size of enabled Adapters : "+adapters.size());
          request.setAttribute("adapters",adapters);
        }// if block ends here
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.SelectFileToUpload");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        saveMessages(request,actionMessages);
  
      }catch(DbsException dex){
        dex.printStackTrace();
        exceptionBean= new ExceptionBean(dex);
        logger.error("Exception occurred in AdapterWebtpAction...");
        logger.error(exceptionBean.getMessage());
        logger.debug(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
      }catch(Exception ex){
        ex.printStackTrace();
        exceptionBean= new ExceptionBean(ex);
        logger.error("Exception occurred in AdapterWebtpAction...");
        logger.error(exceptionBean.getMessage());
        logger.debug(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
      }
      request.setAttribute("relativePath",relativePath);
      logger.info("Exiting AdapterWebtpAction now...");
      return mapping.findForward("success");
    }
}
