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
 * $Id: PermissionBundleListBean.java,v 20040220.3 2006/03/14 05:47:52 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.security;

/**
 *	Purpose: To Populate the User List Table in permission_bundle_list.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   22-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
 
public class PermissionBundleListBean {
    private String permissionName;
    private String permissionDescription;

    /**
     * Purpose : Returns Name of the permissionName.
     * @return String
     */

    public String getPermissionName() {
        return permissionName;
    }

    /**
     * Purpose : Sets Name of the permissionName.
     * @param newpermissionName Value of name 
     */
    public void setPermissionName(String newPermissionName) {
        permissionName = newPermissionName;
    }

    /**
     * Purpose : Returns Name of the permissionDescription.
     * @return String
     */
    public String getPermissionDescription() {
        return permissionDescription;
    }

    /**
     * Purpose : Sets Name of the permissionDescription.
     * @param newpermissionDescription Value of name 
     */
    public void setPermissionDescription(String newPermissionDescription) {
        permissionDescription = newPermissionDescription;
    }
}
