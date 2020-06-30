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
 * $Id: QuartzInitializerServlet.java,v 20040220.17 2006/03/17 08:44:12 suved Exp $
 *****************************************************************************
 */
package dms.web.servlets;
/* dms package references */
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/* Logger API */
import org.apache.log4j.Logger;
/* Quartz API */
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 *	Purpose:  A Servlet that is used to initialize Quartz Scheduler, if configured as a
 *            load-on-startup servlet in a web application.
 * 
 * @author              Mishra Maneesh
 * @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :    
 * 	Last Modified Date:    
 */
public class QuartzInitializerServlet extends HttpServlet {

    Logger logger= Logger.getLogger("DbsLogger");

    public void init(ServletConfig cfg) throws javax.servlet.ServletException {
        super.init(cfg);
        String prefix =  cfg.getServletContext().getRealPath("/");
        String relPath= prefix+"WEB-INF"+File.separator+"params_xmls"+File.separator+"QuartzInitializerParam.xml";

        ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relPath);
        
        String quartzFile = parseUtil.getValue("quartz-init-file","Configuration");
        String smtpHost= parseUtil.getValue("smtp-host","Configuration");
        String smtpUserId = parseUtil.getValue("smtp-userId","Configuration");
        String smtpPassword = parseUtil.getValue("smtp-password","Configuration");
        String authenticate = parseUtil.getValue("authenticate","Configuration");
        logger.info("Quartz Initializer Servlet loaded, initializing Scheduler...");
       
        try {
            //System.out.println((new File(new URI("")).listFiles())[0].getAbsolutePath());
             
            String schedulerPath=prefix+quartzFile;
            StdSchedulerFactory stFact= new StdSchedulerFactory(schedulerPath);
            
            Scheduler sched=stFact.getDefaultScheduler();
            
            sched.getContext().put("smtp-host",smtpHost);
            sched.getContext().put("smtp-userId",smtpUserId);
            sched.getContext().put("smtp-password",smtpPassword);
            sched.getContext().put("authenticate",authenticate);
            sched.start();
            
            cfg.getServletContext().setAttribute("scheduler",sched);
            cfg.getServletContext().setAttribute("smtpHost",smtpHost);
            cfg.getServletContext().setAttribute("schedulerPath",schedulerPath);
            
            logger.info("Quartz Initialized and Started successfully");
        } catch (Exception e) {
            logger.info("Quartz Scheduler failed to initialize: " + e.toString());
            throw new ServletException(e);
        }
        
        System.out.println("Quartz Initialized");
    }

    public void destroy() {
        try {
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            if (sched != null){
             System.out.println("Quartz Scheduler B4 successful shutdown.");
            sched.shutdown();
             System.out.println("Quartz Scheduler After successful shutdown.");
            }else{
              System.out.println("Quartz Scheduler Not Running");
            }
        } catch (Exception e) {
           System.out.println("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
            e.printStackTrace();
        }

        System.out.println("Quartz Scheduler successful shutdown.");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
