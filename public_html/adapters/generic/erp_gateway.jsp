
<%@ page import="adapters.beans.*" %>
<%@ page import="adapters.generic.beans.*" %>
<%@ page import="adapters.generic.wf.*" %>
<%@ page import="java.io.*" %>
<%@ page import="oracle.jdbc.pool.*" %>
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
    String docLink=null;
    BufferedReader reader= request.getReader();
    StringBuffer xmlBuff = new StringBuffer();
    String xmlString=null;
    logger = Logger.getLogger("DbsLogger");
    while((xmlString=reader.readLine())!=null){
      xmlBuff.append(xmlString);
    }  
    out.println(xmlBuff.toString());
    GenericXMLRequestBean requestXML=new GenericXMLRequestBean(xmlBuff.toString());
    logger.debug(xmlBuff.toString());
    req = requestXML.getRequest("REQUEST");
    req=req.trim();
    if(req.length()==0 ){
      throw new Exception("Malformed XML");
    }
    OracleConnectionCacheImpl occi=(OracleConnectionCacheImpl)application.getAttribute("wfConnCache");
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
        <jsp:forward page="/genericReplaceVoucherB4Action.do" />
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
        <jsp:forward page="/genericShowVoucherAction.do" />
      <%
    }else if(req.equalsIgnoreCase("IMAGEPROCESSED")){
      userId=requestXML.getRequest("USERID").trim();
      password=requestXML.getRequest("PASSWORD").trim();
      voucherId=requestXML.getRequest("VOUCHERID").trim();
      docId=requestXML.getRequest("IMAGEDOCUMENTID").trim();
      String screenId = requestXML.getRequest("SCREENID").trim(); 
      aclName=requestXML.getRequest("ACLNAME").trim();
      if(userId.length()==0 || password.length()==0 || voucherId.length()==0 || 
         docId.length()==0 || aclName.length()==0){
         throw new Exception("Malformed XML");
      }
      request.setAttribute("userId",userId);
      request.setAttribute("password",password);
      request.setAttribute("voucherId",voucherId);
      request.setAttribute("docId",docId);
      request.setAttribute("screenId",screenId);
      request.setAttribute("aclName",aclName);
      %>
        <jsp:forward page="/genericRenameVoucherAction.do" />
      <%
    }else if(req.equalsIgnoreCase("DELETEIMAGE")){         
      aclName=requestXML.getRequest("ACLNAME").trim();         
      docId=requestXML.getRequest("IMAGEDOCUMENTID").trim();
      if(docId.length()==0 || aclName.length()==0){
        throw new Exception("Malformed XML");
      }
      Long[] docIdsinLong=new Long[]{new Long(docId)};
      DeleteGenericVoucherBean deleteVoucher= new DeleteGenericVoucherBean(occi,null,null,docIdsinLong,aclName,application);
      boolean status=false;
      if(deleteVoucher.getDeleteStatus()){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root RC = '0' Response = 'DELETEIMAGESUCCESS' ");               
        sb.append(" />");
        xmlResponse = sb.toString();          
      }else{
        StringBuffer sb = new StringBuffer();              
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root RC = '1' Response = 'DELETEIMAGEFAILURE' ");               
        sb.append(" />");
        xmlResponse = sb.toString();          
      }
      request.setAttribute("XmlResponse",xmlResponse);
      %>
      <jsp:forward page="erp_response.jsp" /> 
      <%
    }else if(req.equalsIgnoreCase("GETNEXTIMAGE")){
      String errorString = null;
      userId=requestXML.getRequest("USERID").trim();
      logger.debug("userId : "+userId);
      password=requestXML.getRequest("PASSWORD").trim();
      logger.debug("password : "+password);
      aclName=requestXML.getRequest("ACLNAME").trim();             
      logger.debug("aclName : "+aclName);
      if(userId.length()==0 || password.length()==0 || aclName.length()==0){
        throw new Exception("Malformed XML");
      }
      try{
        GetGenericVoucherFromQueueWf nextImage=new GetGenericVoucherFromQueueWf(occi);
        docId=nextImage.getVoucher(GenericAdapterConstant.ITYPE,aclName);
        try{
          ObtainLink link = new ObtainLink(application,userId,password,docId);
          docLink=link.getLink(request.getRequestURI(),"adapters/generic/erp_gateway.jsp"); 
        }catch(Exception e){
          logger.error(e.toString());
          errorString=e.toString().trim();
          GenericResetVouchersWf voucherReset = new GenericResetVouchersWf(occi);
          voucherReset.reset(GenericAdapterConstant.ITYPE,aclName,new String[] {docId});
        }
        if(docId != null && docLink!= null){
          FindGenericVoucherBean voucher = new FindGenericVoucherBean(application.getRealPath("/")+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml");
          voucher.enterVoucherData(docId,docLink);
        }
      }catch(Exception e){
        logger.error(e.toString());
        errorString=e.toString().trim();           
      }
      if(docId!=null && docLink!=null){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root RC = '0' Response = 'NEXTIMAGE' IMAGEDOCUMENTID = '");         
        sb.append(docId);
        sb.append("' IMAGEURL = '");
        String imageURL = docLink ;
        sb.append(imageURL);
        sb.append("' />");
        xmlResponse = sb.toString();          
      }else if(errorString.endsWith("QueueEmpty")){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root RC = '1' Response = 'QueueEmpty' IMAGEDOCUMENTID = '' ");
        sb.append(" IMAGEURL = '' ");             
        sb.append(" />");
        xmlResponse = sb.toString();          
      }else{          
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
        sb.append("\n");
        sb.append("<root Response = 'GETNEXTIMAGEFAILURE' IMAGEDOCUMENTID = '' ");
        sb.append(" IMAGEURL = '' ");             
        sb.append(" />");
        xmlResponse = sb.toString();          
      }
      request.setAttribute("XmlResponse",xmlResponse);
      %>
       <jsp:forward page="erp_response.jsp" /> 
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
     <jsp:forward page="erp_response.jsp" /> 
   <%
  }
%>
