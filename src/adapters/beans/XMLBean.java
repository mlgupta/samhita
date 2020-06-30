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
 * $Id: XMLBean.java,v 1.9 2006/02/02 12:32:58 IST suved Exp $
 *****************************************************************************
 */
package adapters.beans;
/* Java API */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/* Java Transformer API */
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
/* XML Parserv2 */
import oracle.xml.parser.v2.DOMParser;
/* DOM API */
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
/* SAX API */
import org.xml.sax.SAXException;
/**
 * Purpose            : Bean to perform operations on xml files pertaining to 
 *                      adapters.
 * @author            : Maneesh Mishra
 * @version           : 1.0
 * Date of Creation   : 09-01-06
 * Last Modified Date : 
 * Last Modified By   : 
 */

public class XMLBean {

  private String filePath=null;
  private Document doc = null;
  Logger logger = Logger.getLogger("DbsLogger");
  public XMLBean(){
    
  }
  
  /**
   * Constructor for XMLBean
   * @param xmlFile
   */
  public XMLBean(String xmlFile){
    filePath = xmlFile;   
    String url = "file:" + new File(filePath).getAbsolutePath();
    try{
      // Create a DOM Parser
      DOMParser parser = new DOMParser();
      // Set parser options
      parser.setErrorStream(System.err);
      // Parser the incoming file (URL)
      parser.parse(url);
      // Obtain the document
      doc = parser.getDocument();
    }catch (IOException ioe) {
      ioe.printStackTrace();
    }catch (SAXException saxe) {
      saxe.printStackTrace();
    }
  }
     

  /**
   * function to print node for a given workflow name 
   * @param workflowName
  */
  public void printNode(String workflowName){
    /* fetch the DOM root */
    Element root = doc.getDocumentElement();
    /* fetch all workflow nodes */
    NodeList workflowNodeList = root.getChildNodes();
    /* loop through each node to fetch child nodes and print them */
    for(int index=0;index < workflowNodeList.getLength();index++){
      Node workflowNode = workflowNodeList.item(index);
      if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
         (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
          workflowName)){
        //System.out.println((workflowNode.getAttributes()).item(0).getNodeName());
        //System.out.println((workflowNode.getAttributes()).item(0).getNodeValue());
        NodeList childList =workflowNode.getChildNodes();
        for(int childIndex=0;childIndex < childList.getLength();childIndex++){
          Node node = childList.item(childIndex);  
          if(node.getNodeType()==Node.ELEMENT_NODE){ 
            //System.out.println(node.getNodeName());
            /* print until all child nodes have been exhausted */
            if(node.getLastChild()!=null){
              //System.out.println(node.getLastChild().getNodeValue());
            }                      
          }         
        } 
      }
    }
  }
  
  /**
   * function to add node under a given workflow
   * @param newVal
   * @param nodeName
   * @param workflowName
   */
  public void addAdapterWorkflowNode(String workflowName,String nodeName,String attribute,String attrValue){
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
     // NodeList workflowNodeList = root.getChildNodes();
      
          /* if correct node found , then */ 
          /*Step 1: create a new element using nodeName*/
          Element newElement=doc.createElement(nodeName);
          /* Step 4: Append the newly created element to it's parent , 
           *         viz: workflowNode */
           if(attribute != null){            
             newElement.setAttribute(attribute,attrValue);
           }
          root.appendChild((Node)newElement);
          /* Step 5: Rewrite the xml file using stream result and Transformer */
          File file = new File(filePath);
          Result result= new StreamResult(file);
          Transformer xtransformer = 
                TransformerFactory.newInstance().newInstance().newTransformer();
          Source source = new DOMSource(doc);
          xtransformer.setOutputProperty(OutputKeys.INDENT,"yes");
          xtransformer.transform(source,result);  
      
    }catch (Exception e) {
      e.printStackTrace();
      logger.error(e.toString()
      );
    }
  }
    
    
  /**
   * function to add node under a given workflow
   * @param newVal
   * @param nodeName
   * @param workflowName
   */
  public void addNode(String workflowName,String nodeName,String newVal){
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through to obtain the correct workflow node */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workflowName)){
          /* if correct node found , then */ 
          /*Step 1: create a new element using nodeName*/
          Element newElement=doc.createElement(nodeName);
          /* Step 2: create a new text object using newVal */
          Text nodeVal = doc.createTextNode(newVal);
          /* Step 3: Append the text object to the newly created element */
          newElement.appendChild((Node)nodeVal);
          /* Step 4: Append the newly created element to it's parent , 
           *         viz: workflowNode */
          workflowNode.appendChild(newElement);
          /* Step 5: Rewrite the xml file using stream result and Transformer */
          File file = new File(filePath);
          Result result= new StreamResult(file);
          Transformer xtransformer = 
                TransformerFactory.newInstance().newInstance().newTransformer();
          Source source = new DOMSource(doc);
          xtransformer.setOutputProperty(OutputKeys.INDENT,"yes");
          xtransformer.transform(source,result);
        }
      }
    }catch (Exception e) {
      e.printStackTrace();
      logger.error(e.toString()
      );
    }
  }
    
  /**
   * Function to get the value of a given tag for a workflow name
   * @return String containing value of a given tag for a workflow name 
   * @param tagName
   * @param workflowName
   */
  public String getValue(String workflowName,String tagName){
    String prefix = null;   /* variable to store workflow prefix */
    /* fetch the DOM root */
    Element root = doc.getDocumentElement();
    /* fetch all workflow nodes */
    NodeList workflowNodeList = root.getChildNodes();
    /* loop through the workflow nodes to find the correct workflow node */
    for(int index=0;index < workflowNodeList.getLength();index++){
      Node workflowNode = workflowNodeList.item(index);
      if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
         (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
          workflowName)){
        /* if correct node found, then obtain it's child nodes */
        NodeList childList =workflowNode.getChildNodes();
        /* loop through the child nodes to fetch the correct child node as 
         * specified by tagName */
        for(int childIndex=0;childIndex < childList.getLength();childIndex++){
          Node node = childList.item(childIndex);  
          if(node.getNodeType()==Node.ELEMENT_NODE){ 
            if(node.getNodeName().equalsIgnoreCase(tagName)){
              /* fetch the node value for the correct child node found */
              if(node.getLastChild()!=null){
                prefix= node.getLastChild().getNodeValue();
              }
            }
          }         
        } 
      }
    }
    return prefix;
  }

  /**
   * Function for obtaining all values of a given tag
   * @return String[] containing all values of a given tag  
   * @param tagName
   */
  public String[] getAllValuesForTag(String tagName){
    int numElements=0; /* stores the number of times tagName occurred in a xml file */
    int tagsIndex=0; /* acts as index for String [] tagValues */
    /* fetch the DOM root */
    Element root = doc.getDocumentElement();
    /* fetch all workflow nodes */
    NodeList workflowNodeList = root.getChildNodes();
    /* loop through to find the number of occurences of tagName and store in 
     * numElements */
    for(int numIndex = 0 ;numIndex < workflowNodeList.getLength() ; numIndex++){
      if(workflowNodeList.item(numIndex).getNodeType()==Node.ELEMENT_NODE ){
        numElements++;
      }
    }
    /* declare and initialize String [] tagValues to store all values of tagName */
    String[] tagValues = new String[numElements];
    //System.out.println("Number of workflows="+numElements);
    /* loop through all workflow nodes to find and store the value of tagName */
    for(int index=0;index < workflowNodeList.getLength();index++){
      Node workflowNode = workflowNodeList.item(index);
      if(workflowNode.getNodeType()==Node.ELEMENT_NODE ){
        /* fetch child nodes for each workflow name */
        NodeList childList =workflowNode.getChildNodes();
        /* loop through child nodes to find the correct tag and fetch it's value */
        for(int childIndex=0;childIndex < childList.getLength();childIndex++){
          Node node = childList.item(childIndex);  
          if(node.getNodeType()==Node.ELEMENT_NODE){ 
            if(node.getNodeName().equalsIgnoreCase(tagName)){
              if(node.getLastChild()!=null ){
                tagValues[tagsIndex++]= node.getLastChild().getNodeValue();
              }
            }
          }         
        } 
      }
    }
    return tagValues;
  }
    
    
  /**
   * Function to get all workflow names
   * @return ArrayList containing all workflow names 
   */
  public ArrayList getAllNames(){
    ArrayList allNames = new ArrayList(); /* arraylist to store workflow names */
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through to fetch itemTypeName for each workflow name */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if(workflowNode.getNodeType()==Node.ELEMENT_NODE ){                  
          allNames.add((workflowNode.getAttributes()).item(0).getNodeValue());                 
        }
      }
    }catch (Exception e) {
      e.printStackTrace();
      logger.error(e.toString());
    }     
    return allNames ;
  }
    
    
  /**
   * Function to set value for a given tag
   * @param tagValue
   * @param tagName
   * @param workflowName
   */
  public void setValue(String workflowName,String tagName,String tagValue){
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through the workflow node list */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workflowName)){
          /* obtain the child nodes for the correct workflow node */
          NodeList childList =workflowNode.getChildNodes();
          /* loop through the child nodes to find the correct tagName */
          for(int childIndex=0;childIndex < childList.getLength();childIndex++){
            Node node = childList.item(childIndex);  
            if(node.getNodeType()==Node.ELEMENT_NODE){
              /* finally, set the tag value to tagValue */
              if(node.getNodeName().equalsIgnoreCase(tagName)){
                if(node.getLastChild()!=null){
                   node.getLastChild().setNodeValue(tagValue);
                }else{
                 Text newText = doc.createTextNode(tagValue);
                 node.appendChild(newText);
                }
              }
            }         
          } 
        }
      }
      /* rewrite the xml file using stream result, transformer, DOMSource */
      File file = new File(filePath);
      Result result= new StreamResult(file);
      Transformer xtransformer = 
                  TransformerFactory.newInstance().newInstance().newTransformer();
      Source source = new DOMSource(doc);
      xtransformer.setOutputProperty(OutputKeys.INDENT,"yes");
      xtransformer.transform(source,result);
    }catch (TransformerConfigurationException tce) {
      logger.error(tce.toString());
    }catch (TransformerException te) {
      logger.error(te.toString());
    }    
  }
  
    public boolean itemTypePresent(String workflowName){
    boolean isItemTypePresent = false;
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through the workflow node list */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workflowName)){
              isItemTypePresent=true;
              break ;
            }
      }
    }catch(Exception ex){
      logger.error(ex.toString());
    }
    return isItemTypePresent;
    }

  /* added by Suved to add a <key> tag under <keys> for a given workflowName */
  /**
   * Function to add a <key> tag under <keys> for a given workflowName.
   * @param newNodeValue
   * @param attrValue
   * @param newNodeName
   * @param tagName
   * @param workFlowName
   */
  public void addNodeUnderTag( String workFlowName ,String tagName ,
              String newNodeName,String attrValue,String newNodeValue){
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through the workflow node list to fetch the correct workflow tag */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workFlowName)){
          /* obtain the child nodes for the correct workflow tag */
          NodeList childNodes = workflowNode.getChildNodes();
          int childNodesSize = ( childNodes == null )?0:childNodes.getLength();
          /* loop through the child nodes to find the correct child under which 
           * a new node is to be added */
          for( int childIndex = 0; childIndex < childNodesSize; childIndex++){
            Node childNode = childNodes.item(childIndex);
            if( childNode.getNodeType() == Node.ELEMENT_NODE ){
              if( childNode.getNodeName().equalsIgnoreCase(tagName) ){
                /* Step 1: create a new element specified by newNodeName */
                Element newElement=doc.createElement(newNodeName);
                /* Step 2 : set attribute as "keyId" and it's value to attrValue */
                newElement.setAttribute("keyid",attrValue);
                /* Step 3 : create a new text object specified by newNodeValue */
                Text nodeVal = doc.createTextNode(newNodeValue);
                /* Step 4 : append the text object to the newly created element */
                newElement.appendChild((Node)nodeVal);
                /* Step 5 : append the newly created element to it's parent */
                childNode.appendChild(newElement);
              }
            }
          }
          /* rewrite the xml file using stream result, transformer, DOMSource */
          File file = new File(filePath);
          Result result= new StreamResult(file);
          Transformer xtransformer = 
                TransformerFactory.newInstance().newInstance().newTransformer();
          Source source = new DOMSource(doc);
          xtransformer.setOutputProperty(OutputKeys.INDENT,"yes");
          xtransformer.transform(source,result);
        }
      }
    }catch(Exception ex){
      logger.error(ex.toString());
    }
  }
    
  /* added by Suved for obtaining values of <key> tag under a given workflow */
  /**
   * Function for obtaining values of <key> tag under a given workflow
   * @return String[] containing values of <key> tag under a given workflow 
   * @param tagName
   * @param workFlowName
   */
  public String[] getKeyTagValues( String workFlowName,String tagName ){
    String[] tagValue = null;              // String [] to return tag values
    ArrayList listOfValues = new ArrayList();  // arraylist to store tag values
    boolean foundTag = false;              // boolean flag    
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through to find the correct workflow tag */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workFlowName)){
          /* fetch all child nodes for the correct workflow tag */
          NodeList childList = workflowNode.getChildNodes();
          /* loop through to fetch the correct tag specified by tagName */
          for(int childIndex=0;childIndex < childList.getLength();childIndex++){
            Node node = childList.item(childIndex);  
            if(node.getNodeType()==Node.ELEMENT_NODE && node.hasChildNodes()){
              NodeList keysNode = node.getChildNodes();
              for( int keyIndex = 0; keyIndex < keysNode.getLength(); keyIndex++ ){
                Node keyNode = keysNode.item(keyIndex); 
                if( (keyNode.getNodeType()==Node.ELEMENT_NODE) && 
                    (keyNode.getNodeName().equalsIgnoreCase(tagName)) ){
                  /* store the node value in the arraylist  */
                  if( keyNode.getLastChild() != null ){
                    listOfValues.add(keyNode.getChildNodes().item(0).getNodeValue());
                    foundTag = true;
                  }
                }
              }
              if(foundTag){
                /* convert the arraylist to String [] */
                tagValue = new String[listOfValues.size()];
                for(int strIndex = 0; strIndex < listOfValues.size(); strIndex++){
                  tagValue[strIndex] = (String)listOfValues.get(strIndex);
                }
              }
            }
            if( foundTag ){
              break;
            }
          } 
        }
        if( foundTag ){
          break;
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return tagValue;
  }


  /* added by Suved for obtaining ids of <key> tag under a given workflow */
  /**
   * Function for obtaining ids of <key> tag under a given workflow
   * @return String[] containing ids of <key> tag under a given workflow
   * @param tagName
   * @param workFlowName
   */
  public String[] getKeyTagIds( String workFlowName,String tagName){
    String[] tagIds = null;  // String [] to return tag ids
    ArrayList listOfAttrs = new ArrayList();  // arraylist to store tag ids
    boolean foundTag = false;                 // boolean flag
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through to find the correct workflow tag */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workFlowName)){                
          /* fetch all child nodes for the correct workflow tag */
          NodeList childList =workflowNode.getChildNodes();
          /* loop through to fetch the correct tag specified by tagName */
          for(int childIndex=0;childIndex < childList.getLength();childIndex++){
            Node node = childList.item(childIndex);  
            if(node.getNodeType()==Node.ELEMENT_NODE && node.hasChildNodes()){
              NodeList keysNode = node.getChildNodes();
              for( int keyIndex = 0; keyIndex < keysNode.getLength(); keyIndex++ ){
                Node keyNode = keysNode.item(keyIndex); 
                if( (keyNode.getNodeType()==Node.ELEMENT_NODE) && 
                    (keyNode.getNodeName().equalsIgnoreCase(tagName)) ){
                  /* store the node attribute in the arraylist  */
                  listOfAttrs.add(keyNode.getAttributes().item(0).getNodeValue());
                  foundTag = true;
                }
              }
              if(foundTag){
                /* convert the arraylist to String [] */
                tagIds = new String[listOfAttrs.size()];
                for(int strIndex = 0; strIndex < listOfAttrs.size(); strIndex++){
                  tagIds[strIndex] = (String)listOfAttrs.get(strIndex);
                }
              }
            }
            if( foundTag ){
              break;
            }
          } 
       
        }
        if( foundTag ){
          break;
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return tagIds;
  }


  public ArrayList getKeyTagIdsNodes( String workFlowName,String tagName){
  
    ArrayList listOfTagNodes = new ArrayList();  // arraylist to store tag 
    boolean foundTag = false;                 // boolean flag
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all workflow nodes */
      NodeList workflowNodeList = root.getChildNodes();
      /* loop through to find the correct workflow tag */
      for(int index=0;index < workflowNodeList.getLength();index++){
        Node workflowNode = workflowNodeList.item(index);
        if((workflowNode.getNodeType()==Node.ELEMENT_NODE) && 
           (workflowNode.getAttributes()).item(0).getNodeValue().equalsIgnoreCase(
            workFlowName)){                
          /* fetch all child nodes for the correct workflow tag */
          NodeList childList =workflowNode.getChildNodes();
          /* loop through to fetch the correct tag specified by tagName */
          for(int childIndex=0;childIndex < childList.getLength();childIndex++){
            Node node = childList.item(childIndex);  
            if(node.getNodeType()==Node.ELEMENT_NODE && node.hasChildNodes()){
              NodeList keysNode = node.getChildNodes();
              for( int keyIndex = 0; keyIndex < keysNode.getLength(); keyIndex++ ){
                Node keyNode = keysNode.item(keyIndex); 
                if( (keyNode.getNodeType()==Node.ELEMENT_NODE) && 
                    (keyNode.getNodeName().equalsIgnoreCase(tagName)) ){
                  /* store the node in the arraylist  */
                  listOfTagNodes.add(keyNode);
                  foundTag = true;
                }
              }
            }
            if( foundTag ){
              break;
            }
          } 
       
        }
        if( foundTag ){
          break;
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return listOfTagNodes;
  }


  /**
   * Function to remove a key node
   * @return boolean indicating whether a key node has been removed
   * @param keyId
   * @param tagName
   */
  public boolean removeKeyNode( String tagName,String keyId){
    boolean foundTag = false;
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all elements corresponding to tagName specified */
      NodeList elems = doc.getElementsByTagName(tagName);
      int elemsSize = ( elems == null )?0:elems.getLength();
      /* loop through to find the correct node for removal */
      for( int index = 0; index < elemsSize; index++ ){
        Element elem = (Element)elems.item(index);
        if( elem.getAttribute("keyid").equalsIgnoreCase(keyId) ){
          try{
            /* remove the element from it's parent node */
            elem.getParentNode().removeChild(elem);
            /* rewrite the xml file using stream result, transformer, DOMSource */
            File file = new File(filePath);
            Result result= new StreamResult(file);
            Transformer xtransformer = 
                TransformerFactory.newInstance().newInstance().newTransformer();
            Source source = new DOMSource(doc);
            xtransformer.transform(source,result);
            /* normalize the DOM doc to adjust the tree after node removal */
            doc.normalize();
            foundTag = true;
            return foundTag;
          }catch (Exception e) {
            logger.error(e.toString());
          }
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return foundTag;
  }


  /* added by Suved for obtaining value of all <key> tags in a given xml file */
  /**
   * Function for obtaining value of all <key> tags in a given xml file
   * @return String [] containing value of all <key> tags in a given xml file
   * @param tagName
   */
  public String [] getAllKeyTagValues(String tagName){
    String [] allKeyTagValues = null; // String [] to store key tag values
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all elements corresponding to tagName specified */
      NodeList elems = doc.getElementsByTagName(tagName);
      int elemsSize = ( elems == null )?0:elems.getLength();
      allKeyTagValues = new String[elemsSize];
      /* loop through to fetch value of all <key> tags */
      for( int index = 0; index < elemsSize; index++ ){
        Element elem = (Element)elems.item(index);
        if( elem.hasChildNodes() ){
          allKeyTagValues[index] = elem.getChildNodes().item(0).getNodeValue();
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return allKeyTagValues;
  }


  /* added by Suved for obtaining keyid of all <key> tags in a given xml file */
  /**
   * Function for obtaining keyid of all <key> tags in a given xml file
   * @return String [] containing keyid of all <key> tags in a given xml file
   * @param tagName
   */
  public String [] getAllKeyTagIds(String tagName){
    String [] allKeyTagIds = null;  // String [] to store key tag ids
    try{
      /* fetch the DOM root */
      Element root = doc.getDocumentElement();
      /* fetch all elements corresponding to tagName specified */
      NodeList elems = doc.getElementsByTagName(tagName);
      int elemsSize = ( elems == null )?0:elems.getLength();
      allKeyTagIds = new String[elemsSize];
      /* loop through to find attribute "keyId" for every tag and store */
      for( int index = 0; index < elemsSize; index++ ){
        Element elem = (Element)elems.item(index);
        allKeyTagIds[index] = elem.getAttribute("keyid");
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return allKeyTagIds;
  }
  
  
  public String getKeyForKeyId(String tagName,String keyId,String aclName){
    
    String keyValue = null;
    ArrayList listOfNodes = null; 
    try{
      ArrayList workflowNodes = getAllNames();
      int sizeOfNodes = ( workflowNodes == null )?0:workflowNodes.size();
      for( int index = 0; index < sizeOfNodes; index++ ){
        if( aclName.startsWith(getValue(((String)workflowNodes.get(index)),"PrefixName")) ){
          listOfNodes = getKeyTagIdsNodes((String)workflowNodes.get(index),"key");
          sizeOfNodes = ( listOfNodes == null )?0:listOfNodes.size();
          for( int i = 0; i < sizeOfNodes; i++ ){
            if( ((Node)listOfNodes.get(i)).getAttributes().item(0).getNodeValue().equals(keyId) ){
              keyValue = ((Node)listOfNodes.get(i)).getChildNodes().item(0).getNodeValue(); 
            }
          }
        }
      }
    }catch (Exception e) {
     logger.error(e.toString()); 
    }
   return keyValue;
  }
  
  
   /*public static void main(String[] args) {
    String fileName = null;
    XMLBean oxde = null;
    fileName = args[1];
    oxde = new XMLBean(fileName);
    if(!oxde.itemTypePresent(args[2])){
        if(args[0].equals("1")){      
          oxde.addAdapterWorkflowNode("Adapters","Adapter","ItemTypeName",args[2]);
          oxde.addNode(args[6],"Name",args[3]);
          oxde.addNode(args[6],"Enabled",args[4]);
          oxde.addNode(args[6],"PrefixName",args[5]);
          oxde.addNode(args[6],"keys","");
          oxde.addNode(args[6],"ConstantName",args[7]);
        }else if(args[0].equals("2")){      
          oxde.addAdapterWorkflowNode("workflows","workflow","ItemTypeName",args[2]);
          oxde.addNode(args[7],"ProcessName",args[3]);
          oxde.addNode(args[7],"Name",args[4]);
          oxde.addNode(args[7],"Description",args[5]);
          oxde.addNode(args[7],"PrefixName",args[6]);      
        }
    }
    if( args[0].equals("1") ){
      ArrayList allNodes = oxde.getAllNames();
      int size = ( allNodes == null )?0:allNodes.size();
      for( int index = 0; index < size; index++ ){
        if( oxde.getValue((String)allNodes.get(index),"ConstantName") == null ){
          if( ((String)allNodes.get(index)).equals("PSF_TYPE") ){
            oxde.addNode((String)allNodes.get(index),"ConstantName","PSFIN");
          }else if( ((String)allNodes.get(index)).equals("PSH_TYPE") ){
            oxde.addNode((String)allNodes.get(index),"ConstantName","PSHR");
          }else if( ((String)allNodes.get(index)).equals("SAP_TYPE") ){
            oxde.addNode((String)allNodes.get(index),"ConstantName","SAP");
          }else if( ((String)allNodes.get(index)).equals("ERP_TYPE") ){
            oxde.addNode((String)allNodes.get(index),"ConstantName","generic");
          }
        }else{
          System.out.println("Node ConstantName already present");
        }
      }
    }
  }*/
}
