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
 * $Id: GeneralActionServlet.java,v 20040220.41 2006/06/08 12:54:12 suved Exp $
 *****************************************************************************
 */
/*package name*/ 
package dms.web.actionservlets;
/*import statement*/
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.agents.DocumentLogAgent;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Oracle API */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts.action.ActionServlet;
/*Class declaration and definition*/
/**
 *	Purpose: Action Servlet for redirecting every action in Samhita
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    07-01-2004
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class GeneralActionServlet extends ActionServlet{
    private static ArrayList popupActionList = new ArrayList();
    String nonAdminSecurity =null;
    String jobRetrialInterval=null;
    String jobRetrialCount=null;
    static {
        popupActionList.add("b4DocUploadAction.do");
        popupActionList.add("docHistoryDeleteAction.do");
        popupActionList.add("docHistoryDetailAction.do");
        popupActionList.add("docHistoryListAction.do");
        popupActionList.add("docHistoryRollbackAction.do");
        popupActionList.add("folderDocB4ApplyAclAction.do");
        popupActionList.add("folderDocB4CheckInAction.do");
        popupActionList.add("folderDocB4CheckOutAction.do");
        popupActionList.add("folderDocB4CopyMoveAction.do");
        popupActionList.add("folderDocB4PropertyAction.do");
        popupActionList.add("folderDocB4RenameAction.do");
        popupActionList.add("userListSelectAdminAction.do");
        popupActionList.add("groupListSelectAdminAction.do");
        popupActionList.add("aclListSelectAction.do");
        popupActionList.add("folderDocSelectB4Action.do");
        popupActionList.add("changeEncryptionPasswordB4Action.do");
        popupActionList.add("setEncryptionPasswordB4Action.do");   
        popupActionList.add("changePasswordB4Action.do");        
        popupActionList.add("themePreviewAction.do");                
        popupActionList.add("userPreferenceThemePreviewAction.do");
        popupActionList.add("setURLEncryptPasswordAction.do");
        popupActionList.add("b4DocNewAction.do");
        popupActionList.add("b4ReplaceVoucherAction.do");
    }

    private ServletContext  context = null;
    private String prefix = null;
    private String relPath = null;
    private ParseXMLTagUtil parseUtil = null;
    private String ifsService;
    private String ifsSchemaPassword;
    private String serviceConfiguration;
    private String domain;

    public void init(ServletConfig config) throws ServletException {
        DocumentLogAgent docLoggingAgent=null;
        try{
            super.init(config);
            log("Initializing Logger...");
            context = config.getServletContext();
            prefix =  context.getRealPath("/");
            relPath=prefix+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
  //            docLoggingAgent.generalActionParamLocation=relPath;
            parseUtil= new ParseXMLTagUtil(relPath);
            docLoggingAgent.generalActionParamLocation=relPath;
            //parseUtil= new ParseXMLTagUtil(relPath);
            String logFile= parseUtil.getValue("log4j-init-file","Configuration");
            nonAdminSecurity= parseUtil.getValue("nonadminsecurity","Configuration");
            String faxtempdir= parseUtil.getValue("faxtempdir","Configuration");
            jobRetrialInterval= parseUtil.getValue("jobRetrialInterval","Configuration");
            jobRetrialCount= parseUtil.getValue("jobRetrialCount","Configuration");
            OracleConnectionCacheImpl wfConnCache = new OracleConnectionCacheImpl();
            wfConnCache.setDriverType(parseUtil.getValue("DriverType","Configuration"));
 
            // Sets the database server name
            wfConnCache.setServerName(parseUtil.getValue("ServerName","Configuration"));
       
            // Sets the database name
            wfConnCache.setDatabaseName(parseUtil.getValue("DatabaseName","Configuration"));
       
            // Sets the port number
            wfConnCache.setPortNumber(new Integer(parseUtil.getValue("PortNumber","Configuration")).intValue());
       
            // Sets the user name
            wfConnCache.setUser(parseUtil.getValue("User","Configuration"));
       
            // Sets the password
            wfConnCache.setPassword(parseUtil.getValue("Password","Configuration"));
            wfConnCache.setCacheScheme(OracleConnectionCacheImpl.DYNAMIC_SCHEME);
            wfConnCache.setMinLimit(Integer.parseInt(parseUtil.getValue("WFConnMinLimit","Configuration")));
            wfConnCache.setMaxLimit(Integer.parseInt(parseUtil.getValue("WFConnMaxLimit","Configuration")));
            context.setAttribute("wfConnCache",wfConnCache);
            
            try{
              //docLoggingAgent.startAgent();
              log("LoggingAgent started: ");
            }catch(Exception ex){             
              log("Exception occurred in Document Log Agent Initialization");             
            }
            
            context.setAttribute("nonAdminSecurity",nonAdminSecurity);
            context.setAttribute("faxtempdir",prefix+faxtempdir);
            context.setAttribute("contextPath",prefix);
            context.setAttribute("jobRetrialInterval",jobRetrialInterval);
            context.setAttribute("jobRetrialCount",jobRetrialCount);
            context.setAttribute("docLoggingAgent",docLoggingAgent);
            if(logFile != null) {
                PropertyConfigurator.configure(prefix + logFile);
            }else{
                log("Unable to find log4j-initialization-file : " + logFile);
            }
            log("Logger initialized successfully");
            //InitiatePSFWf ipwf = new InitiatePSFWf((OracleConnectionCacheImpl)context.getAttribute("wfConnCache"));
            //ipwf.startReqWF("PSF_TYPE","PSF_PROC","BLEWIS","PSF_NORTH_ACL","CDOUGLAS SPIERSON KWALKER",new String[]{"11099","34123","12313","356345"});
            
           // AddVouchersToQueue avtq = new AddVouchersToQueue((OracleConnectionCacheImpl)context.getAttribute("wfConnCache"));
            //avtq.addVouchers("PSF_TYPE","PSF_PROC","BLEWIS","PSF_NORTH_ACL",new String[]{"111099","314123","112313","3156345"});
            
            
            //GetQueueCreated gr = new GetQueueCreated(context);
            //System.out.println("WF result="+ gr.isQueueCreated("PSF_TYPE","PSF_NORTH_ACL"));
        }catch(Exception e){
            log(" Unable to initialize logger : " + e.toString());
        }
    }

    //All the request will pass through this method
    public void process(HttpServletRequest request,HttpServletResponse response) {
        
        Logger logger = Logger.getLogger("DbsLogger");
        HttpSession httpSession=request.getSession(true);
        UserInfo userInfo=(UserInfo)httpSession.getAttribute("UserInfo");
        String requestedURI=request.getRequestURI();
        String redirector = parseUtil.getValue("redirector","Configuration");
        logger.debug("Entering process() now...");
        logger.debug("Processing URI : \""+ requestedURI + "\" in GeneralActionServlet");


        if(userInfo==null && !(request.getRequestURI().endsWith("loginAction.do")) && !(request.getRequestURI().endsWith("relayAction.do")) && !(request.getRequestURI().endsWith("ContentAction.do"))  && !(request.getRequestURI().endsWith("DownloadAction.do")) && !(request.getRequestURI().endsWith("VoucherB4Action.do"))  && !(request.getRequestURI().endsWith("VoucherAction.do"))  &&  !(request.getRequestURI().endsWith("b4ImageUploadAction.do"))){
            try{  
                logger.debug("Trying to access non admin pages when session has expired");
                String actionName = requestedURI.substring((requestedURI.lastIndexOf("/") + 1));
                if(popupActionList.contains(actionName)){
                    logger.debug("Serving : session_expired_4_pop_up.jsp");
                    response.sendRedirect(redirector+requestedURI.substring(0,requestedURI.lastIndexOf("/")+1)+"session_expired_4_pop_up.jsp");
                }else{
                    logger.debug("Serving : login.jsp");
                    response.sendRedirect(redirector+requestedURI.substring(0,requestedURI.lastIndexOf("/")+1)+"login.jsp?sessionExpired=true");
                }
            }catch(Exception ex){
                logger.error("An Exception occurred in GeneralActionServlet...");
                logger.error(ex.toString());
            }
      }else{
          try{
              boolean isNonAdminAllowed=false;
              
              if(nonAdminSecurity.equals("1")){
                isNonAdminAllowed=((requestedURI.indexOf("AdminAction.do")!= -1) && requestedURI.indexOf("acl")==-1 && requestedURI.indexOf("ace")==-1&& requestedURI.indexOf("permission")==-1);                
              }else if(nonAdminSecurity.equals("0")){
                isNonAdminAllowed=(requestedURI.indexOf("AdminAction.do")!= -1);
              }              
              //if((requestedURI.indexOf("AdminAction.do")!= -1) && requestedURI.indexOf("acl")==-1 && requestedURI.indexOf("ace")==-1&& requestedURI.indexOf("permission")==-1){
              if(isNonAdminAllowed){
                 if(userInfo==null){
                    logger.debug("Trying to access Admin pages when session has expired or user is not logged in");
                    logger.debug("Serving : login.jsp");
                    response.sendRedirect(redirector+requestedURI.substring(0,requestedURI.lastIndexOf("/")+1)+"login.jsp?sessionExpired=true"); 
                 }else{
                    DbsLibrarySession dbsSession=((UserInfo)(httpSession.getAttribute("UserInfo"))).getDbsLibrarySession();
                    boolean isAdminEnabled=dbsSession.getUser().isAdminEnabled();
                    if(isAdminEnabled==false){
                        logger.debug("Non Admin User is logged in but he is trying to access admin pages");
                        logger.debug("Serving : restricted_resource.jsp");
                        response.sendRedirect(redirector+requestedURI.substring(0,requestedURI.lastIndexOf("/")+1)+"restricted_resource.jsp"); 
                    }else{
                        logger.debug("dbsLibrarySession id in process: "+dbsSession.getId());
                        super.process(request,response);
                    }
                 }
              }else{
                  if(userInfo!=null)
                    logger.debug("dbsLibrarySession id in process if userInfo not null: "+userInfo.getDbsLibrarySession().getId());   
                  super.process(request,response);
              }
          }catch(DbsException dbsException){
              logger.error("An Exception occurred in GeneralActionServlet...");
              logger.error(dbsException.toString());
          }catch(Exception ex){
              logger.error("An Exception occurred in GeneralActionServlet...");
              logger.error(ex.toString());
          }
      }
  }
}