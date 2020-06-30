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
 * $Id: AclListBean.java,v 20040220.5 2006/03/14 05:47:52 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.security;

/**
 *	Purpose: To Populate the User List Table in acl_list.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
 
public class AclListBean {
    private String aclName;
    private String owner;
    private String securingAcl;
    private String modified;
    private Boolean isShared;
    private String isDisabled;
 
    /**
     * Purpose : Returns Name of the AclName.
     * @return : String
     */
    public String getAclName() {
        return aclName;
    }

    /**
     * Purpose : Sets Name of the AclName.
     * @param  : newAclName Value of name 
     */
    public void setAclName(String newAclName) {
        aclName = newAclName;
    }

    /**
     * Purpose : Returns Name of the Owner.
     * @return : String
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Purpose : Sets Name of the Owner.
     * @param  : newOwner Value of name 
     */
    public void setOwner(String newOwner) {
        owner = newOwner;
    }

    /**
     * Purpose : Returns Name of the SecuringAcl.
     * @return : String
     */
    public String getSecuringAcl() {
        return securingAcl;
    }

    /**
     * Purpose : Sets Name of the SecuringAcl.
     * @param  : newSecuringAcl Value of name 
     */
    public void setSecuringAcl(String newSecuringAcl) {
        securingAcl = newSecuringAcl;
    }

    /**
     * Purpose : Returns Name of the Modified.
     * @return : String
     */
    public String getModified() {
        return modified;
    }

    /**
     * Purpose : Sets Name of the Modified.
     * @param  : newModified Value of name 
     */
    public void setModified(String newModified) {
        modified = newModified;
    }

   /**
    * Purpose : Returns Name of the IsShared.
    * @return : String
    */
    public Boolean getIsShared() {
        return isShared;
    }

    /**
     * Purpose : Sets Name of the IsShared.
     * @param  : newIsShared Value of name 
     */
    public void setIsShared(Boolean newIsShared) {
        isShared = newIsShared;
    }

   /**
    * Purpose : Returns Name of the isDisabled.
    * @return : String
    */
    public String getIsDisabled(){
        return isDisabled;
    }

    /**
     * Purpose : Sets Name of the isDisabled.
     * @param  : newisDisabled Value of name 
     */
    public void setIsDisabled(String newIsDisabled){
        isDisabled = newIsDisabled;
    }
}
