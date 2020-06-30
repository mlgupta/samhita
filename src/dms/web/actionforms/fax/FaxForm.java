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
 * $Id: FaxForm.java,v 20040220.3 2006/03/17 08:20:13 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.fax;
//Struts API
import dms.web.beans.scheduler.DateHelper;
// Java API
import java.util.Date;
// Struts API
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of
 *           FaxForm in faxing.jsp
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    03-04-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class FaxForm extends ValidatorForm {
    private String txtTo;
    private String txtCompanyName;
    private String txtFaxNumber;
    private String[] lstAttachment;
    private String txtSendTime=DateHelper.format(new Date(),"MM/dd/yyyy HH:mm:ss");
    private String day;
    private String month;
    private String year;
    private String hours;
    private String minutes;
    private String timezone;
    private String seconds;
    private String txaComments;

    public String getDay()
    {
        return day;
    }

    public void setDay(String newDay)
    {
        day = newDay;
    }

    public String getHours()
    {
        return hours;
    }

    public void setHours(String newHours)
    {
        hours = newHours;
    }

    public String[] getLstAttachment()
    {
        return lstAttachment;
    }

    public void setLstAttachment(String[] newLstAttachment)
    {
        lstAttachment = newLstAttachment;
    }

    public String getMinutes()
    {
        return minutes;
    }

    public void setMinutes(String newMinutes)
    {
        minutes = newMinutes;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String newMonth)
    {
        month = newMonth;
    }

    public String getSeconds()
    {
        return seconds;
    }

    public void setSeconds(String newSeconds)
    {
        seconds = newSeconds;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String newTimezone)
    {
        timezone = newTimezone;
    }



    public String getTxtCompanyName()
    {
        return txtCompanyName;
    }

    public void setTxtCompanyName(String newTxtCompanyName)
    {
        txtCompanyName = newTxtCompanyName;
    }

    public String getTxtFaxNumber()
    {
        return txtFaxNumber;
    }

    public void setTxtFaxNumber(String newTxtFaxNumber)
    {
        txtFaxNumber = newTxtFaxNumber;
    }

    public String getTxtSendTime()
    {
        return txtSendTime;
    }

    public void setTxtSendTime(String newTxtSendTime)
    {
        txtSendTime = newTxtSendTime;
    }

    public String getTxtTo()
    {
        return txtTo;
    }

    public void setTxtTo(String newTxtTo)
    {
        txtTo = newTxtTo;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String newYear)
    {
        year = newYear;
    }

    public String getTxaComments()
    {
        return txaComments;
    }

    public void setTxaComments(String newTxaComments)
    {
        txaComments = newTxaComments;
    }

}
