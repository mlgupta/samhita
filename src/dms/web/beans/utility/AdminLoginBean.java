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
 * $Id: AdminLoginBean.java,v 20040220.4 2006/03/14 05:29:00 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;
/**
 *	Purpose: To get the admin session where administration mode is required for Non - Admin user.
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
/* dms package references */
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
/* Java API */
import javax.servlet.http.HttpSession;
/* Logger API */
import org.apache.log4j.Logger;
public class AdminLoginBean {
    private String systemAdminUserID;
    private String systemAdminUserPassword;

    public AdminLoginBean() {}

    /**
     * Purpose : Returns system user ID.
     * @return : String
     */
/*    public String getSystemAdminUserID() {
        return systemAdminUserID;
    }

    public void setSystemAdminUserID(String newSystemAdminUserID) {
        systemAdminUserID = newSystemAdminUserID;
    }

    public String getSystemAdminUserPassword() {
        return systemAdminUserPassword;
    }

    public void setSystemAdminUserPassword(String newSystemAdminUserPassword) {
        systemAdminUserPassword = newSystemAdminUserPassword;
    }
*/
    public DbsLibrarySession getAdminLibrarySession(HttpSession httpSession, DbsLibrarySession dbsLibrarySession){
        Logger logger = Logger.getLogger("DbsLogger");
        DbsLibrarySession dbsAdminLibrarySession;
        try{
            DbsDirectoryUser dbsUser= dbsLibrarySession.getUser();
        
            String systemAdminUserID = httpSession.getServletContext().getInitParameter("SystemUserId");
            String systemAdminUserPassword = httpSession.getServletContext().getInitParameter("SystemUserPassword");
            DbsCleartextCredential systemAdminCredential = new DbsCleartextCredential(systemAdminUserID,systemAdminUserPassword);
        
            String ifsService = httpSession.getServletContext().getInitParameter("IfsService");
            
            DbsLibraryService dbsLibraryService = DbsLibraryService.findService("IfsDefaultService");
            dbsAdminLibrarySession = dbsLibraryService.connect(systemAdminCredential,null);
            dbsAdminLibrarySession.impersonateUser(dbsUser);
            dbsAdminLibrarySession.setAdministrationMode(true);        
        }catch(DbsException dex){
            logger.error(dex.getMessage());
            dbsAdminLibrarySession = null;
        }
        return dbsAdminLibrarySession;
    }

    public void closeAdminSession(DbsLibrarySession dbsLibrarySession) throws DbsException{
        if (dbsLibrarySession!=null){
            dbsLibrarySession.impersonateUser(null);
            dbsLibrarySession.setAdministrationMode(false);
            dbsLibrarySession.disconnect();
        }
    }
}
