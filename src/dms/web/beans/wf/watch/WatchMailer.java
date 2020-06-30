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
 * $Id: WatchMailer.java,v 1.2 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.watch;
/* dms package references */ 
import dms.beans.DbsException;
import dms.web.beans.loginout.LoginBean;
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
/* mail API */
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/* Struts API */
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
/**
 *	Purpose:             To prepare mails and send them to recipients for watch.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class WatchMailer{
  String [] subscribersArr = null;
  Long poId = null;
  String relativePath = null;
  //Logger logger = null;
  public String emailIds = null;
  String senderEmailId = null;
  String userName = null;
  String poName = null;
  Scheduler sched=null;
  String smtpHost=null;
  String actionName= null;
  ParseXMLTagUtil parseUtil = null;  
  public String authenticate;
  public String SMTP_AUTH_USER;
  public String SMTP_AUTH_PWD;
  
  public WatchMailer(){
  }
  
  public WatchMailer( String[] subscribersArr, Long poId ,String relativePath,String userName,String actionName){
    this.subscribersArr = subscribersArr;
    this.poId = poId;
    this.relativePath = relativePath;
    this.userName = userName;
    this.actionName = actionName;
    //logger = Logger.getLogger("DbsLogger"); 
    System.out.println("WatchMailer instantiated successfully...");
  }
  
  private boolean getEmailIds(){
    parseUtil = new ParseXMLTagUtil(relativePath);
    String userName = parseUtil.getValue("sysaduser","Configuration");
    String userPwd = parseUtil.getValue("sysadpwd","Configuration");
    String domain = parseUtil.getValue("Domain","Configuration");
    String ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
    String ifsService = parseUtil.getValue("IfsService","Configuration");
    String serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
    LoginBean loginBean = new LoginBean(userName,userPwd);
    try{
      if( subscribersArr!= null && subscribersArr.length!=0 ){
        loginBean.setServiceParams(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
        loginBean.setdbsCtc();
        loginBean.startDbsService();
        loginBean.setUserSession();
        loginBean.setSubscribersArr(subscribersArr);
        emailIds = loginBean.getSubscribersEMailId();
        poName = loginBean.getPOName(poId);
        senderEmailId = loginBean.getSenderEmailId();
        parseUtil = null;
      }else{
        System.out.println("No subcribers found...");
        return false;
      }
    }catch( DbsException dbsEx ){
      System.out.println(dbsEx.toString());
      return false;
    }catch (Exception e){
      System.out.println(e.toString());
      return false;
    }finally{
      if( loginBean.dbsSession!=null ){
        loginBean.terminateService();
      }
      parseUtil = null;
    }
    return true;
  }

  public boolean mailForWatch(){
    try{
      if(getEmailIds()){
        System.out.println("EmailIds Obtained successfully");
        relativePath = relativePath.replaceFirst("GeneralActionParam",
                                                  "QuartzInitializerParam");
        //System.out.println("relativePath : "+relativePath);
        parseUtil = new ParseXMLTagUtil(relativePath);
        smtpHost = parseUtil.getValue("smtp-host","Configuration");
        //System.out.println("smtpHost : "+smtpHost);
        authenticate = parseUtil.getValue("authenticate","Configuration");
        //System.out.println("authenticate : "+authenticate);
        if(authenticate.equalsIgnoreCase("true")){
          SMTP_AUTH_USER = parseUtil.getValue("smtp-userId","Configuration");
          SMTP_AUTH_PWD = parseUtil.getValue("smtp-password","Configuration");
        }
    
        String to = emailIds;
        String subject = "Watch Notification Attention";
        String from = senderEmailId;
        String hello = "Hello All,";
        String lineOfAction = actionName+"\n\t\tPerformed By : "+userName;
        String regards = "Regards,";
        String sysAdmin = "System Administrator";
        String message = hello + "\n\t\t"+lineOfAction+"\n"+
                         regards+"\n"+sysAdmin;
  
        if (smtpHost == null || smtpHost.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_SMTP_HOST not specified.");
        if (to == null || to.trim().length() == 0)
                throw new IllegalArgumentException(
                        "PROP_RECIPIENT not specified.");
        if (from == null || from.trim().length() == 0)
                throw new IllegalArgumentException("PROP_SENDER not specified.");
  
        boolean success=sendMail(smtpHost,to,from,subject,message);
        if(success){                
          System.out.println("Mail sent successfully");                
        }else{
          System.out.println("Unable to send mail: ");
          return false;
        }
      }else{
        System.out.println("Unable to send mail to recipients");
        return false;
      }
    }catch (Exception e) {
      System.out.println(e.toString());
      //logger.debug(e.toString());
      return false;
    }
    parseUtil = null;
    return true;
  }
  
  public void deleteEntries(){
    relativePath = relativePath.replaceFirst("QuartzInitializerParam",
                                             "GeneralActionParam");
    ConnectionBean connBean = new ConnectionBean(relativePath);
    Statement stmt = connBean.getStatement(true);
    try{
      String query = "DELETE FROM watch_pos WHERE PO_ID="+poId;
      stmt.executeUpdate(query);
    }catch(SQLException sqlEx){
      System.out.println(sqlEx.toString());
    }finally{
      connBean.closeStatement();
      connBean.closeConnection();
    }
  }

  private boolean sendMail(String smtpHost,String to,String from,String subject,
                           String message)throws Exception{
    boolean success=false;
    try{            
      MimeMessage mimeMessage = prepareMimeMessage(smtpHost,to,from,subject, message); 
      if(mimeMessage!=null){
        Transport.send(mimeMessage);
        success=true;
      }
    }catch(Exception e){
      System.out.println(e.toString());
      throw e;
    }
    return success;
  }

  /**
   *	Purpose: Creates a mime message with the parameters passed.This mime message 
   *           contains all the required properties for the mail. 
   *           
   *  @param   smtpHost - The SMTP server IP/Domain name.
   *  @param   to - Email address of the recipient of mail.
   *  @param   from - Email address of the sender of mail.
   *  @param   subject - Subject of the message.
   *  @param   message - The message written in plane text.
   *  @return  Mime message which is actually sent.                 
   */
  private MimeMessage prepareMimeMessage(String smtpHost, String to,
          String from,String subject,String message) throws Exception{
      MimeMessage mimeMessage =null;
    try{
      Properties properties = new Properties();
      properties.put("mail.smtp.host", smtpHost);
      //System.out.println("SMTP Host="+smtpHost);
      Session session = null;
      if(authenticate.equalsIgnoreCase("true")){
       properties.put("mail.smtp.auth", "true");
       Authenticator auth = new SMTPAuthenticator();
       session = Session.getDefaultInstance(properties, auth);
      }else{
       session = Session.getDefaultInstance(properties, null);
      }
      
    mimeMessage = new MimeMessage(session);        
    Address[] toAddresses = InternetAddress.parse(to);
    mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);
    mimeMessage.setFrom(new InternetAddress(from));
    mimeMessage.setSubject(subject);
    
    mimeMessage.setSentDate(new Date());
    
    MimeBodyPart messageText = new MimeBodyPart();        

    messageText.setText(message);
    
    Multipart mimeMultiPart=new MimeMultipart();
    mimeMultiPart.addBodyPart(messageText);
    mimeMessage.setContent(mimeMultiPart); 
    }catch(Exception e){
      System.out.println(e.toString());
      throw e;
    }
    return mimeMessage;
  }
    
  /**
    * SimpleAuthenticator is used to do simple authentication
    * when the SMTP server requires it.
  */
  private class SMTPAuthenticator extends javax.mail.Authenticator{
    public PasswordAuthentication getPasswordAuthentication(){
      String username = SMTP_AUTH_USER;
      String password = SMTP_AUTH_PWD;
      return new PasswordAuthentication(username, password);
    }
  }
  
}