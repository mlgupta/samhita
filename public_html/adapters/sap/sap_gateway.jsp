<%@ page import="adapters.beans.*" %>
<%@ page import="adapters.sap.beans.*" %>
<%@ page import="adapters.sap.wf.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.apache.log4j.*" %>
<%  
  String errorResponse = null;
  Logger logger = null;
  try{
    String req=null;
    String userId=null;
    String password=null;
    String voucherId=null;
    String docId=null;
    String xmlResponse = null;
    String aclName = null;
    BufferedReader reader= request.getReader();
    StringBuffer xmlBuff = new StringBuffer();
    String xmlString=null;
    logger = Logger.getLogger("DbsLogger");
    while((xmlString=reader.readLine())!=null){
      xmlBuff.append(xmlString);
    }  
    out.println(xmlBuff.toString());
    SAPXMLRequestBean requestXML=new SAPXMLRequestBean(xmlBuff.toString());
    req = requestXML.getRequest("REQUEST");
    req=req.trim();
    if(req.length()==0 ){
      throw new Exception("Malformed XML");
    }
    if(req.equalsIgnoreCase("GETMANAGEDIMAGE")){       
      userId=requestXML.getRequest("USERID").trim();
      password=requestXML.getRequest("PASSWORD").trim();
      docId=requestXML.getRequest("IMAGEDOCUMENTID").trim();            
      voucherId=requestXML.getRequest("VOUCHERID").trim();
      String screenId = requestXML.getRequest("SCREENID").trim();      
      aclName=requestXML.getRequest("ACLNAME").trim();
      if(userId.length()==0 || password.length()==0 || voucherId.length()==0 ||
         screenId.length()==0 || aclName.length()==0){
        throw new Exception("Malformed XML");
      }
      request.setAttribute("userId",userId);
      request.setAttribute("password",password);
      request.setAttribute("screenId",screenId);
      request.setAttribute("docId",docId);
      request.setAttribute("voucherId",voucherId);
      request.setAttribute("aclName",aclName);
      %>
        <jsp:forward page="/sapReplaceVoucherB4Action.do" />
      <%
    }else if( req.equalsIgnoreCase("SHOWIMAGE") ){
      userId=requestXML.getRequest("USERID").trim();
      password=requestXML.getRequest("PASSWORD").trim();
      voucherId=requestXML.getRequest("VOUCHERID").trim();
      String screenId = requestXML.getRequest("SCREENID").trim(); 
      aclName=requestXML.getRequest("ACLNAME").trim();
      if(userId.length()==0 || password.length()==0 || voucherId.length()==0 ||
         screenId.length()==0 || aclName.length()==0){
        throw new Exception("Malformed XML");
      }
      request.setAttribute("userId",userId);
      request.setAttribute("password",password);
      request.setAttribute("screenId",screenId);
      request.setAttribute("voucherId",voucherId);
      request.setAttribute("aclName",aclName);
      %>
        <jsp:forward page="/sapShowVoucherAction.do" />
      <%
    }
  }catch(Exception e){
   logger.error(e.toString());
   StringBuffer sb = new StringBuffer();              
   sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
   sb.append("\n");
   sb.append("<root RC = '-1' Response = 'Malformed XML' ");               
   sb.append(" />");
   errorResponse = sb.toString();
   request.setAttribute("XmlResponse",errorResponse);
   %>
     <jsp:forward page="sap_response.jsp" /> 
   <%
  }
%>
