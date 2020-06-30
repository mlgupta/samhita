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
 * $Id: MailForm.java,v 20040220.5 2006/03/16 07:40:46 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.mail;
/* dms package references */
import dms.web.beans.scheduler.DateHelper;
/* Java API */
import java.util.Date;
/* Struts API */
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store and retrive the values of the html controls of MailForm in 
 *           mailing.jsp
 * 
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    03-04-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class MailForm extends ValidatorForm {
    private String txtTo;
    private String txtCc;
    private String txtBCc;
    private String txtSubject;
    private String txaMail;
    private String[] lstAttachment;
    private String txtSendTime=DateHelper.format(new Date(),"MM/dd/yyyy HH:mm:ss");
    private String day;
    private String month;
    private String year;
    private String hours;
    private String minutes;
    private String timezone;
    private String seconds;

    /**
     * Purpose   : Returns txtTo.
     * @return   : String
     */
    public String getTxtTo() {
        return txtTo;
    }

    /**
     * Purpose   : Sets the value of txtTo.
     * @param    : newTxtTo Value of txtTo from the form
     */
    public void setTxtTo(String newTxtTo) {
        txtTo = newTxtTo;
    }

    /**
     * Purpose   : Returns txtCc.
     * @return   : String
     */
    public String getTxtCc() {
        return txtCc;
    }

    /**
     * Purpose   : Sets the value of txtCc.
     * @param    : newTxtCc Value of txtCc from the form
     */
    public void setTxtCc(String newTxtCc) {
        txtCc = newTxtCc;
    }

    /**
     * Purpose   : Returns txtBCc.
     * @return   : String
     */
    public String getTxtBCc() {
        return txtBCc;
    }

    /**
     * Purpose   : Sets the value of txtBCc.
     * @param    : newTxtBCc Value of txtBCc from the form
     */
    public void setTxtBCc(String newTxtBCc) {
        txtBCc = newTxtBCc;
    }

    /**
     * Purpose   : Returns txtSubject.
     * @return   : String
     */
    public String getTxtSubject() {
        return txtSubject;
    }

    /**
     * Purpose   : Sets the value of txtSubject.
     * @param    : newTxtSubject Value of txtSubject from the form
     */
    public void setTxtSubject(String newTxtSubject) {
        txtSubject = newTxtSubject;
    }

    /**
     * Purpose   : Returns txaMail.
     * @return   : String
     */
    public String getTxaMail() {
        return txaMail;
    }

    /**
     * Purpose   : Sets the value of txaMail.
     * @param    : newTxaMail Value of txaMail from the form.
     */
    public void setTxaMail(String newTxaMail) {
        txaMail = newTxaMail;
    }

    /**
     * Purpose   : Returns array of Attachment.
     * @return   : An String Array.
     */
    public String[] getLstAttachment() {
        return lstAttachment;
    }

    /**
     * Purpose   : Sets the value of lstAttachment.
     * @param    : newLstAttachment Value of lstAttachment from the form
     */
    public void setLstAttachment(String[] newLstAttachment) {
        lstAttachment = newLstAttachment;
    }

    public String getTxtSendTime()
    {
        return txtSendTime;
    }

    public void setTxtSendTime(String newTxtSendTime)
    {
        txtSendTime = newTxtSendTime;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String newDay)
    {
        day = newDay;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String newMonth)
    {
        month = newMonth;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String newYear)
    {
        year = newYear;
    }

    public String getHours()
    {
        return hours;
    }

    public void setHours(String newHours)
    {
        hours = newHours;
    }

    public String getMinutes()
    {
        return minutes;
    }

    public void setMinutes(String newMinutes)
    {
        minutes = newMinutes;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String newTimezone)
    {
        timezone = newTimezone;
    }

    public String getSeconds()
    {
        return seconds;
    }

    public void setSeconds(String newSeconds)
    {
        seconds = newSeconds;
    }
}
