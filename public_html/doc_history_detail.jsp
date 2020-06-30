<%@ page import="dms.web.actionforms.filesystem.*" %>



<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="documentHistoryDetail" name="documentHistoryDetail" type="dms.web.beans.filesystem.DocumentHistoryDetail" />

<html:html>

<head>

<script src="general.js"></script>

<title><bean:message key="title.DocumentHistoryDetail" /></title>

<jsp:include page="/style_sheet_include.jsp" />

</head>

<body style="margin:0">



<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

<br>

<tr>

<td >

	<table id="tabParent" align="center" width="350px"  border="0" cellpadding="0" cellspacing="0">

		<tr>

    	<td>

			<table id="tab" width="110px" border="0" cellpadding="0" cellspacing="0">

      		<tr>

           <td width="5px" class="imgTabLeft"></td>

           <td width="100px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.Property" /></div></td>

           <td width="5px" class="imgTabRight"></td>  

      		</tr>

    		</table>

		</td>

  		</tr>

	</table>

<table id="borderClrLvl_2" width="350px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" >

  <tr>

    <td align="center">

      <table width="97%" border="0" cellpadding="0" cellspacing="0" >

        <tr><td height="20px" colspan="2" ></td></tr>

        <tr>

          <td width="22%"><a style="float:right" class="imgDocument"></a></td>

          <td width="78%"><bean:write name="documentHistoryDetail" property="docName" /></td>

        </tr>

        <tr><td height="10px" colspan="2" ></td></tr>

        <tr>

          <td class="bgClrLvl_2" height="1px" width="100%" colspan="2" align="center"></td>

        </tr>

        <tr><td height="10px" colspan="2" ></td></tr>

        <tr>

          <td><bean:message key="lbl.Status" /></td>

          <td>&nbsp;:&nbsp;<bean:write name="documentHistoryDetail" property="actionType" /></td>

        </tr>

        <tr><td height="5x" colspan="2" ></td></tr>

        <tr>

          <td><bean:message key="lbl.Date" /></td>

          <td>&nbsp;:&nbsp;<bean:write name="documentHistoryDetail" property="versionDate" /></td>

        </tr>

        <tr><td height="5px" colspan="2" ></td></tr>

        <tr>

          <td><bean:message key="lbl.VersionedNumber" /></td>

          <td>&nbsp;:&nbsp;<bean:write name="documentHistoryDetail" property="versionNumber" /></td>

        </tr>

        <tr><td height="10px" colspan="2" ></td></tr>

        <tr>

          <td class="bgClrLvl_2" height="1px" width="100%" colspan="2" align="center"></td>

        </tr>

        <tr><td height="10px" colspan="2" ></td></tr>

        <tr>

          <td><bean:message key="lbl.UserName" /></td>

          <td>&nbsp;:&nbsp;<bean:write name="documentHistoryDetail" property="userName" /></td>

        </tr>

        <tr><td height="5px" colspan="2" ></td></tr>

        <tr>

          <td><bean:message key="lbl.Comment" /></td>

          <td><html:textarea name="documentHistoryDetail" style="width:100%;" rows="6" property="comment" readonly="true"   styleClass="borderClrLvl_2  componentStyle bgClrLvl_5 " /> </td>

        </tr>

        <tr><td height="10px" colspan="2" ></td></tr>

        <tr>

          <td height="30px" colspan="2" align="right"> 

            <html:button property="btnClose" onclick="window.close()" styleClass="buttons" style="width:70px"  ><bean:message key="btn.Close" /></html:button>

            <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=document_versioned_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>

          </td>

        </tr>

      </table>

    </td>

    </tr>

    </table>

    <!-- blueBorder table ends above-->

</td>

</tr>

</table>

<!-- tabContainer table ends above-->

</body>

</html:html>