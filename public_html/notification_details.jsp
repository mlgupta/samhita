<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>

<head>

<title><bean:message key="title.NotificationDetails" /></title>

<script src="general.js"></script>
<html:javascript formName="notificationDetailsForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
<script language="Javascript1.1" >

function callPage(thisForm,userAction){
    thisForm.userAction.value=userAction;
    thisForm.submit();  
  }
</script>

</head>

<body style="margin:0" >
<html:form name="NotificationDetailsForm" method="post" type="dms.web.actionforms.workflow.NotificationDetailsForm" action="/notificationDetailsAction" onsubmit="return validateNotificationDetailsForm(this);" >
<html:hidden name="NotificationDetailsForm" property="notificationId" /> 
<html:hidden name="NotificationDetailsForm" property="itemType" />
<html:hidden name="NotificationDetailsForm" property="itemKey" />
<html:hidden name="NotificationDetailsForm" property="userAction" /> 
<html:hidden name="NotificationDetailsForm" property="status" />
  
  <table width="100%"  border="0" cellspacing="0" cellpadding="0" id="headerIncluder">
    <tr>
      <td height="95px">
      <!--Header Starts-->
      <jsp:include page="/header.jsp" />
      <!--Header Ends-->
    </td>
    </tr>
  </table>
  <br>
  <!-- This page contains 2 outermost tables, named 'errorContainer' and 'tabContainer' -->
  <table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->  
    <tr>
      <td align="center">
        <table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="122px">
              <table id="tab1" width="120px" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="5px" class="imgTabLeft"></td>
                  <td width="110px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.NotificationDetails" /></div></td>
                  <td width="5px" class="imgTabRight"></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <table  width="700px" border="0" cellpadding="1" cellspacing="1" bgcolor="#e8e8e8" class="borderClrLvl_2 bgClrLvl_4" >
          <tr><td height="20px" colspan="2"></td></tr>
          <tr>
            <td>&nbsp;</td>
            <td  align="right">
               
                <html:button property="btnBack" styleClass="buttons" onclick="history.go(-1);" style="width:70px; margin-right:50px"  tabindex="8"><bean:message key="btn.Back" /></html:button>
             
            </td>
          </tr>
          <tr>        
          
          <td  width="200px" align="center" ><div align="right"><bean:message key="txt.NotificationName" />&nbsp;</div></td>
            <td width="500px" align="center" >
              <div align="left">
                <html:text name="NotificationDetailsForm" property="txtNotificationName" styleClass="borderClrLvl_2 componentStyle" style="width:445px;"  tabindex="1"/>
              </div>
            </td>
          </tr>
          <tr>
            <td align="center" ><div align="right"><bean:message key="txt.NotificationSent" />&nbsp;</div></td>
            <td >
              <div align="left">
                <html:text name="NotificationDetailsForm" property="txtNotificationSent" styleClass="borderClrLvl_2 componentStyle" style="width:445px;" tabindex="2"/>
              </div>
            </td>
          </tr>
          <tr>
            <td ><div align="right"><bean:message key="txt.NotificationSubject" />&nbsp;</div></td>
            <td >
              <div align="left">
                <html:text name="NotificationDetailsForm" property="txtNotificationSubject" styleClass="borderClrLvl_2 componentStyle" style="width:445px;" tabindex="3" />
              </div>
            </td>
          </tr>
          <tr>
            <td valign="top" height="128px"><div align="right"><bean:message key="txa.NotificationComments" />&nbsp;</div></td>
            <td height="110px">
              <div align="left">
                <html:textarea name="NotificationDetailsForm" property="txaNotificationComments" styleClass="borderClrLvl_2 componentStyle" style="width:445px; height:160px" tabindex="5"></html:textarea>
              </div>
            </td>
          </tr>
          <tr>
             <% if(((Boolean)request.getAttribute("isApprover")).booleanValue() && ((String)request.getAttribute("status")).equalsIgnoreCase("OPEN")){%>
            <td valign="top" ><div align="right"><bean:message key="txa.Note" />&nbsp;</div></td>
            <td  valign="top">
              <div align="left">
              
                <html:textarea   name="NotificationDetailsForm" property="note"  styleClass="borderClrLvl_2 componentStyle" style="width:445px; height:100px" tabindex="6"></html:textarea>
                <%}%>
              </div>
            </td>
          </tr>
           <tr>
            <td valign="top" ><div align="right"> </div></td>
            <td valign="top">
               <% if(((Boolean)request.getAttribute("isApprover")).booleanValue() && ((String)request.getAttribute("status")).equalsIgnoreCase("OPEN")){%>
                <bean:message key="msg.ApproverNote" />
               <%}%>
            </td>
          </tr>
          <tr>
            <td height="30px" > 
              <div align="right">&nbsp;</div>
            </td>
            <% if(((String)request.getAttribute("status")).equalsIgnoreCase("OPEN")){
                         if(((Boolean)request.getAttribute("isApprover")).booleanValue()){%>
            <td>
              <div align="right">
                <a target="viewContent" class="menu" href="<%=((String)request.getAttribute("docUrl"))%>"/><bean:message key="msg.LaunchFile" /></a>
                <html:button property="btnApprove" styleClass="buttons" onclick="callPage(this.form,'APPROVE');" style="width:70px;" tabindex="6"><bean:message key="btn.Approve" /></html:button>
                <html:button property="btnReject" styleClass="buttons" onclick="callPage(this.form,'REJECT');" style="width:70px;margin-right:50px"  tabindex="7"><bean:message key="btn.Reject" /></html:button>
                
              </div>
            </td>
            <%}else{%>
            <td>
              <div align="right">
              <html:button property="btnClose" styleClass="buttons" onclick="callPage(this.form,'CLOSE');" style="width:70px;margin-right:50px"  tabindex="8"><bean:message key="btn.Close" /></html:button>
              </div>
            </td>            
            <%}
            }%>
          </tr>
        </table>
      </td>
    </tr>
    </tr>
    <tr><td height="2px"></td></tr>
    <tr>
      <td align="center">
        <table class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="700px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
          <tr>
          <td height="20px" width="30px"><div class="imgStatusMsg"></div></td>
          <td>
            <logic:messagesPresent >
              <bean:message key="errors.header"/>
              <html:messages id="error">
                <font color="red"><bean:write name="error"/></font>            
              </html:messages>
            </logic:messagesPresent>
            <html:messages id="msg" message="true">
              <bean:write  name="msg"/>
            </html:messages>
          </td>
          </tr>
        </table>
        <!-- statusBar table ends above-->
      </td>
    </tr>  
  </table>
</html:form>

<!-- tabContainer table ends above-->

</body>

</html:html>