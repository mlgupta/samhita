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
 * $Id: GenericXMLRequestBean.java,v 1.0 2006/06/23 04:41:36 suved Exp $
 *****************************************************************************
 */
package adapters.generic.beans;
/* Java API */
import java.io.ByteArrayInputStream;
import java.io.InputStream;
/* Oracle xmlParserv2 API */
import oracle.xml.parser.v2.DOMParser;
/* Logger API */
import org.apache.log4j.Logger;
/* DOM API */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * Purpose            : Bean to read request attributes from xml file for Generic ERP
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 23-06-2006
 * Last Modified Date : 
 * Last Modified By   : 
 */
public class GenericXMLRequestBean  {
  private String xmlString = null;  // xml file path as string
  private Document doc = null;      // DOM document object  
  private Logger logger = null;     // logger for verbose logging
  
  /**
   * Constructor for GenericXMLRequestBean Class
   * @param xmlStream
   */
  public GenericXMLRequestBean(InputStream xmlStream )throws Exception{
    try{
      logger = Logger.getLogger("DbsLogger");
      // Create a DOM Parser
      DOMParser parser = new DOMParser();      
      // Set parser options
      parser.setErrorStream(System.err);      
      // Parser the incoming file (URL)
      parser.parse(xmlStream);      
      // Obtain the document
      doc = parser.getDocument();
    }catch (Exception e) {
      logger.error(e.toString());
      throw e;
    }
  }
  
  /**
   * Constructor for GenericXMLRequestBean Class
   * @param xmlString
   */
  public GenericXMLRequestBean(String xmlString ) throws Exception{
    try{
      logger = Logger.getLogger("DbsLogger");
      logger.debug("XML is: "+xmlString);
      ByteArrayInputStream bis = new ByteArrayInputStream(xmlString.getBytes());
      // Create a DOM Parser
      DOMParser parser = new DOMParser();      
      // Set parser options
      parser.setErrorStream(System.err);      
      // Parser the incoming file (URL)
      parser.parse(bis);      
      // Obtain the document
      doc = parser.getDocument();
    }catch (Exception e) {
      logger.error(e.toString());
      throw e;
    }
  }
  
  /**
   * Function to obtain request attribute corresponding to attributeName specified
   * @return 
   * @param attributeName
   */
  public String getRequest(String attributeName)throws Exception{
    String request=null;
    try{
      Element root = doc.getDocumentElement();
      request=root.getAttribute(attributeName);        
    }catch (Exception e) {
      logger.error(e.toString());
      throw e;
    }
    return request; 
  }
}
