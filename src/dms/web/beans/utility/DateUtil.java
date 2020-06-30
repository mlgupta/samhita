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
 * $Id: DateUtil.java,v 20040220.8 2006/03/17 08:44:27 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.utility;

/**
 *	Purpose: To get the formatted date. 
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    05-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil{

    public DateUtil() { }

    /**
     * Purpose   : Returns formatted date in dd-MMM-yyyy format.
     * @param    : date - Date
     * @returns  : formattedDate
     */
    public static String getFormattedDate(Date date){
        Date unformattedDate=date;
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(unformattedDate);
        return formattedDate;
    }

    /**
     * Purpose   : Returns formatted date in dd-MMM-yy format.
     * @param    : strDate - String
     * @returns  : date in dd-MMM-yyyy
     */
    public static Date parseDate(String strDate) throws ParseException{
        Date date;
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yy");
        date = dateFormat.parse(strDate);
        return date;
    }
} 
