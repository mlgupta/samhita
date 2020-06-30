<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="aclselects" name="aclselects" type="java.util.ArrayList" />  

<%
request.setAttribute("topic","security_introduction_html");
//Variable declaration
boolean firstTableRow;
firstTableRow = true;
String approverWfPrefix = (String)request.getAttribute("approverWfPrefix");
%>

<html:html>
<head>
<title><bean:message key="title.Select.WorkFlow" /></title>
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js" ></script>
<html:javascript formName="aclListSelectForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
<script language="Javascript1.1" >

function cancelActionPerformed(){
  window.close();
}

function okActionPerformed(thisForm){
  var selectedValue=checkSelected(thisForm);
  var approverWfPrefix = '<%=approverWfPrefix%>';
  if (selectedValue){
    opener.submitToWorkFlow(selectedValue,approverWfPrefix);
    window.location.replace("blank.html");
  }
}      

function checkSelected(thisForm){
  if (typeof thisForm.radSelect!="undefined"){
    if (typeof thisForm.radSelect.length !="undefined"){
      for(index = 0 ; index < thisForm.radSelect.length ;index++){  
        if(thisForm.radSelect[index].checked){   
          return thisForm.radSelect[index].value;
        }
      }
    }else{
      if(thisForm.radSelect.checked){   
        return thisForm.radSelect.value;
      }
    }
  }else{
    alert('<bean:message key="errors.radSelect.noitem"/>');         
    return false;
  }      
  alert('<bean:message key="errors.radSelect.required"/>');         
  return false;
}

</script>
</head>

<body style="margin:15px">
<html:form name="aclListSelectForm" type="dms.web.actionforms.security.AclListSelectForm" action="/relayAction" >    
<table id="tabParent" align="center"  width="580px"  border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td>
      <table width="180px" border="0" cellpadding="0" cellspacing="0" id="tab">
        <tr>
          <td width="5px" class="imgTabLeft"></td>
          <td width="170px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.SelectWorkFlow" /></div></td>
          <td width="5px" class="imgTabRight"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table width="580px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">
  <tr>
    <td>
    <table width="97%" border="0" align="center" cellpadding="0" cellspacing="0" id="innerContainer">
      <tr>
        <td>
          <table width="100%" border="0" align="center">
            <tr>
              <td colspan="2" height="10px"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="10px"></td>
      </tr>
      <tr> 
        <td align="center">
          <div class="borderClrLvl_2 bgClrLvl_4" style="overflow:auto; width:100%; height:244px">
            <table class="bgClrLvl_F" id="data" width="100%" border="0" cellpadding="0" cellspacing="1">
              <tr>
                <th width="8%" height="18px"><bean:message key="tbl.head.Select" /></th>
                <th width="43%"><bean:message key="tbl.head.Name" /></th>
                <th width="28%"><bean:message key="tbl.head.Owner" /></th>
                <th width="21%"><bean:message key="tbl.head.CreateDate" /></th>
              </tr>
              <% if(aclselects.size()>0) {%>
              <logic:iterate name="aclselects" id="aclselect">
              <bean:define id="name" name="aclselect" property="aclName" scope="page" />
                <%if (firstTableRow == true){ firstTableRow = false; %>
                  <tr class="bgClrLvl_4">
                <%}else{ firstTableRow = true; %>
                  <tr class="bgClrLvl_3">                  
                <%}%>
                <td>
                  <div align="center">
                    <html:radio property="radSelect" value="<%=name%>" tabindex="1"/>
                  </div>
                </td>
                <td><bean:write name="aclselect" property="aclName" /></td>
                <td><bean:write name="aclselect" property="owner" /></td>
                <td><bean:write name="aclselect" property="createDate" /></td>
              </tr>
              </logic:iterate>
              <%}%>
              </table>
              <% if(aclselects.size()==0) {%>
                <div style="position:relative; top:125px; text-align:center;" class="tabText">
                  <bean:message key="info.no_item_found.no_item_found" />
                </div> 
              <%}%>
              </div>
            </td>
          </tr>
        <tr>
          <td height="5px"></td>
        </tr>
        <tr>
          <td align="center" height="30px"> 
            <div align="right"> 
              <html:button property="btnSelect" styleClass="buttons" style="width:70px" onclick="okActionPerformed(this.form)" tabindex="4"><bean:message key="btn.Select" /></html:button>  
              <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="cancelActionPerformed()" tabindex="5"><bean:message key="btn.Cancel" /></html:button>
              <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=security_introduction_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>
            </div>
          </td>
        </tr>	
      </table>
    </td>
  </tr>	
</table>
 <!-- borderClrLvl_2 table ends above-->
<table border="0" cellpadding="0" cellspacing="0" >
  <tr><td height="2px"></td></tr>
</table>
<table align="center" class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="580px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
  <tr>
    <td width="30px"><div class="imgStatusMsg"></div></td>
    <td width="550px">
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
</html:form>
</body>
</html:html>