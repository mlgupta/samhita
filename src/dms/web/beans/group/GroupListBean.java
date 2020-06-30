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
 * $Id: GroupListBean.java,v 20040220.4 2006/03/14 06:15:19 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.group; 
/**
 *	Purpose: To populate Group List table
 *  in group_list.jsp
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   05-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class GroupListBean {
    private String name;
    private String quota;
    private String createDate;
    private String owner;
    private String acl;
    private String isDisabled;

    /**
     * Purpose   : Returns name of the group.
     * @return String
     */
    public String getName() {
        return name;
    }

   /**
     * Purpose   : Sets the  name of group.
     * @param newName Value of name 
     */
    public void setName(String newName) {
        name = newName;
    }
    
   /**
     * Purpose   : Returns  content quota of the group.
     * @return String
     */
    public String getQuota() {
        return quota;
    }

   /**
     * Purpose   : Sets the  content quota of group.
     * @param newQuota Value of quota 
     */
    public void setQuota(String newQuota) {
        quota = newQuota;
    }

   /**
     * Purpose   : Returns  createDate of the group.
     * @return String
     */
    public String getCreateDate() {
        return createDate;
    }
    
    /**
     * Purpose   : Sets the  createDate of group.
     * @param newCreateDate Value of createDate 
     */
    public void setCreateDate(String newCreateDate) {
        createDate = newCreateDate;
    }

   /**
     * Purpose   : Returns  owner of the group.
     * @return String
     */
    public String getOwner() {
        return owner;
    }

   /**
     * Purpose   : Sets the  owner of group.
     * @param newOwner Value of owner 
     */
    public void setOwner(String newOwner) {
        owner = newOwner;
    }

   /**
     * Purpose   : Returns Acl of the group.
     * @return String
     */
    public String getAcl() {
        return acl;
    }

    /**
     * Purpose   : Sets the  acl for group.
     * @param newAcl Value of acl 
     */
    public void setAcl(String newAcl) {
        acl = newAcl;
    }

    public String getIsDisabled()
    {
        return isDisabled;
    }

    public void setIsDisabled(String newIsDisabled)
    {
        isDisabled = newIsDisabled;
    }
}
