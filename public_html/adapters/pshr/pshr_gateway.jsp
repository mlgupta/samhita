<%@ page import="adapters.beans.*" %>
<%@ page import="adapters.pshr.beans.*" %>
<%@ page import="adapters.pshr.wf.*" %>
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
        String aclName=null;
        String docLink=null;
        String xmlResponse = null;
        BufferedReader reader= request.getReader();
        StringBuffer xmlBuff = new StringBuffer();
        String xmlString=null;
        logger = Logger.getLogger("DbsLogger");
        while((xmlString=reader.readLine())!=null){
            xmlBuff.append(xmlString);
        }  
        out.println(xmlBuff.toString());
        PSHRXMLRequestBean requestXML=new PSHRXMLRequestBean(xmlBuff.toString());
        //XMLRequestBean requestXML=new XMLRequestBean(new FileInputStream(new File("request.xml")));
        req = requestXML.getRequest("Request");
        req=req.trim();
        if(req.length()==0 ){
          throw new Exception("Malformed XML");
        }
        OracleConnectionCacheImpl occi=(OracleConnectionCacheImpl)application.getAttribute("wfConnCache");
        if(req.equalsIgnoreCase("GetNextImage")){
             String errorString = null;
             userId=requestXML.getRequest("UserId").trim();
             password=requestXML.getRequest("Password").trim();
             aclName=requestXML.getRequest("AclName").trim();             
             if(userId.length()==0 || password.length()==0 || aclName.length()==0){
                 throw new Exception("Malformed XML");
             }
             try{
               GetPSHRVoucherFromQueue nextImage=new GetPSHRVoucherFromQueue(occi);
               docId=nextImage.getVoucher(PSHRAdapterConstant.ITYPE,aclName);
               try{
                   ObtainLink link = new ObtainLink(application,userId,password,docId);
                   docLink=link.getLink(request.getRequestURI(),"adapters/pshr/pshr_gateway.jsp"); 
               }catch(Exception e){
                 logger.error(e.toString());
                 errorString=e.toString().trim();
                 PSHRResetVouchers voucherReset = new PSHRResetVouchers(occi);
                 voucherReset.reset(PSHRAdapterConstant.ITYPE,aclName,new String[] {docId});
               }
             }catch(Exception e){
                logger.error(e.toString());
                errorString=e.toString().trim();           
             }//nextImage.getVoucher();
             if(docId!=null && docLink!=null){
                  StringBuffer sb = new StringBuffer();
                  sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
                  sb.append("\n");
                  sb.append("<root Response = 'NextImage' ImageDocumentID = '");         
                  sb.append(docId);
                  sb.append("' ImageURL = '");
                  String imageURL = docLink ;
                  sb.append(imageURL);
                  sb.append("' />");
                  xmlResponse = sb.toString();          
                 //xmlResponse = xmlResponse.replaceAll("<","&lt");
                 //xmlResponse = xmlResponse.replaceAll(">","&gt");
              }else if(errorString.endsWith("QueueEmpty")){
                  StringBuffer sb = new StringBuffer();
                  sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
                  sb.append("\n");
                  sb.append("<root Response = 'QueueEmpty' ImageDocumentID = '' ");
                  sb.append(" ImageURL = '' ");             
                  sb.append(" />");
                  xmlResponse = sb.toString();          
                  //xmlResponse = xmlResponse.replaceAll("<","&lt");
                  //xmlResponse = xmlResponse.replaceAll(">","&gt");              
              
              }else {          
                  StringBuffer sb = new StringBuffer();
                  sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
                  sb.append("\n");
                  sb.append("<root Response = 'GetNextImageFailure' ImageDocumentID = '' ");
                  sb.append(" ImageURL = '' ");             
                  sb.append(" />");
                  xmlResponse = sb.toString();          
                  //xmlResponse = xmlResponse.replaceAll("<","&lt");
                  //xmlResponse = xmlResponse.replaceAll(">","&gt");          
              }
              request.setAttribute("XmlResponse",xmlResponse);
              %>
               <jsp:forward page="pshr_response.jsp" /> 
              <%
                     
        }
        else if(req.equalsIgnoreCase("ImageProcessed")){
             userId=requestXML.getRequest("UserId").trim();
             password=requestXML.getRequest("Password").trim();
             voucherId=requestXML.getRequest("VoucherId").trim();
             docId=requestXML.getRequest("ImageDocumentId").trim();
             aclName=requestXML.getRequest("AclName").trim();
             if(userId.length()==0 || password.length()==0 || voucherId.length()==0 || docId.length()==0 || aclName.length()==0){
                 throw new Exception("Malformed XML");
             }
             request.setAttribute("userId",userId);
             request.setAttribute("password",password);
             request.setAttribute("voucherId",voucherId);
             request.setAttribute("docId",docId);
             request.setAttribute("aclName",aclName);
           %>
           <jsp:forward page="/pshrRenameVoucherAction.do" />
           <%
        }else if(req.equalsIgnoreCase("DeleteImage")){         
             aclName=requestXML.getRequest("AclName").trim();         
             docId=requestXML.getRequest("ImageDocumentId").trim();
             if(docId.length()==0 || aclName.length()==0){
                 throw new Exception("Malformed XML");
             }
             Long[] docIdsinLong=new Long[]{new Long(docId)};
             DeletePSHRVoucher deleteVoucher= new DeletePSHRVoucher(occi,null,null,docIdsinLong,aclName,application);
             boolean status=false;
             if(deleteVoucher.getDeleteStatus()){
                  StringBuffer sb = new StringBuffer();
                  sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
                  sb.append("\n");
                  sb.append("<root Response = 'DeleteImageSuccess' ");               
                  sb.append(" />");
                  xmlResponse = sb.toString();          
                  //xmlResponse = xmlResponse.replaceAll("<","&lt");
                  //xmlResponse = xmlResponse.replaceAll(">","&gt");
              }else{
                  StringBuffer sb = new StringBuffer();              
                  sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
                  sb.append("\n");
                  sb.append("<root Response = 'DeleteImageFailure' ");               
                  sb.append(" />");
                  xmlResponse = sb.toString();          
                  //xmlResponse = xmlResponse.replaceAll("<","&lt");
                  //xmlResponse = xmlResponse.replaceAll(">","&gt");
              
              }
              request.setAttribute("XmlResponse",xmlResponse);
              %>
               <jsp:forward page="pshr_response.jsp" /> 
              <%
                     
        }else if(req.equalsIgnoreCase("GetManagedImage")){       
             userId=requestXML.getRequest("UserId").trim();
             password=requestXML.getRequest("Password").trim();
             docId=requestXML.getRequest("ImageDocumentId").trim();
             docLink=requestXML.getRequest("ImageURL").trim();
             voucherId=requestXML.getRequest("VoucherId").trim();
             String key = requestXML.getRequest("Key").trim();
             if(userId.length()==0 || password.length()==0 || voucherId.length()==0 || key.length()==0){
                 throw new Exception("Malformed XML");
             }
             request.setAttribute("userId",userId);
             request.setAttribute("password",password);
             request.setAttribute("key",key);
             request.setAttribute("docId",docId);
             request.setAttribute("voucherId",voucherId);
           %>
           <jsp:forward page="/pshrReplaceVoucherB4Action.do" />
           <%
           
        }
    }catch(Exception e){
       logger.error(e.toString());
       StringBuffer sb = new StringBuffer();              
       sb.append("<?xml version = '1.0' encoding = 'UTF-8'?>");
       sb.append("\n");
       sb.append("<root Response = 'Malformed XML' ");               
       sb.append(" />");
       errorResponse = sb.toString();
       request.setAttribute("XmlResponse",errorResponse);
         %>
         <jsp:forward page="pshr_response.jsp" /> 
        <%
    }

%>
