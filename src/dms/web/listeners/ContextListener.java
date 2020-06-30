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
 * $Id: ContextListener.java,v 20040220.11 2006/03/14 05:27:17 suved Exp $
 *****************************************************************************
 */
package dms.web.listeners; 

/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/* Oracle Connection Pool */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Struts API */
import org.apache.log4j.Logger;

/**
 *	Purpose: This class is used to  dispose CMSDK Library service in correct manner.
 *           This is a listener class whose contextInitialized() and contextDestroyed() methods are 
 *           called corrospondingly whenever the servlet context is loaded into the memory and destroyed.
 * 
 * @author              Mishra Maneesh
 * @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */ 
public class ContextListener implements ServletContextListener
{
   ServletContext servletContext;
   Logger logger= Logger.getLogger("DbsLogger");

   /**
     * Purpose : To provide definition for contextInitialized(ServletContextEvent) function in ServletContextListener
     *           interface.This function is called whenever the servlet context is loaded.
     * @param  : sce - ServletContextEvent 
     * 
     */
   public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing Servlet Context");
        servletContext = sce.getServletContext();
        logger.info("Initializing Servlet Context Complete");
   }

   /**
     * Purpose : To provide definition for contextDestroyed(ServletContextEvent) function in ServletContextListener
     *           interface.This function is called whenever the servlet context is destroyed.
     *           Here the CMSDK Library Service is disposed off as soon as servlet context is destroyed.
     * @param  : sce - ServletContextEvent 
     * 
     */ 
   public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Destroying Servlet Context...");
        servletContext = sce.getServletContext();
        String prefix =  servletContext.getRealPath("/");
        String relPath=prefix+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
        ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relPath);
        DbsLibraryService dbsLibraryService=null;
        String ifsService = parseUtil.getValue("IfsService","Configuration"); 
        String ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration"); 
        OracleConnectionCacheImpl occi = (OracleConnectionCacheImpl )servletContext.getAttribute("wfConnCache");        
        try{
            if(DbsLibraryService.isServiceStarted(ifsService)){
                logger.info(ifsService + " is running");
                logger.info(ifsService + " going to stop");
                dbsLibraryService = DbsLibraryService.findService("IfsDefaultService");
                dbsLibraryService.dispose(ifsSchemaPassword);
                logger.info("Library Service '"+ifsService+"' successfully disposed.");
                occi.closeConnections();
                logger.info("Pooled connections for workflow successfully closed.");
            }
        }catch(DbsException dbsException){
            ExceptionBean exceptionBean = new ExceptionBean(dbsException);
            logger.error(exceptionBean.getMessage());
            logger.debug(exceptionBean.getErrorTrace());
        }                  
        logger.info("Destroying Servlet Context complete");
   }

  
}
