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
 * $Id: AclSelectListBean.java,v 20040220.4 2006/03/14 05:47:52 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.security;

/**
 *	Purpose: To Populate the Acl Select List Table in acl_list_select.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   24-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class AclSelectListBean {
    private String aclName;
    private String owner;
    private String createDate;
    private Long aclId;
 
    /**
     * Purpose : Returns Value of the aclName.
     * @return : String
     */
    public String getAclName() {
        return aclName;
    }

    /**
     * Purpose : Sets Value of the aclName.
     * @param  : newAclName Value of aclName. 
     */
    public void setAclName(String newAclName) {
        aclName = newAclName;
    }

    /**
     * Purpose : Returns Value of the owner.
     * @return : String
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Purpose : Sets Value of the owner.
     * @param  : newOwner Value of owner.
     */
    public void setOwner(String newOwner) {
        owner = newOwner;
    }

    /**
     * Purpose : Returns value of the createDate.
     * @return : String
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Purpose : Sets Value of the createDate.
     * @param  : newCreateDate Value of createDate. 
     */
    public void setCreateDate(String newCreateDate) {
        createDate = newCreateDate;
    }

    /**
     * Purpose : Returns Value of the aclId.
     * @return : Long
     */
    public Long getAclId() {
        return aclId;
    }

    /**
     * Purpose : Sets Value of the aclId.
     * @param  : newAclId Value of aclId. 
     */
    public void setAclId(Long newAclId) {
        aclId = newAclId;
    }
}
