<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%request.setAttribute("topic","user_introduction_html");%>

<html:html>

<head>

<title><bean:message key="title.DefaultUserDefiniton" /></title>



<script language="JavaScript" type="text/JavaScript">

<!--

function MM_openBrWindow(theURL,winName,features) { //v2.0

  window.open(theURL,winName,features);

}

//-->

</script>

</head>

<body style="margin:0">

<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

<table width="100%"  border="0" cellspacing="0" cellpadding="0" id="headerIncluder">

  <tr>

    <td height="95px">

		<!--Header Starts-->

		<jsp:include page="/header.jsp" />

		<!--Header Ends-->

	</td>

  </tr>

</table>



<table id="errorContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

  <tr>

    <td>

        <html:errors />

	<!--Error Comes here-->

	</td>

  </tr>

</table>



<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

<br>

<tr>

<td align="center">

	<table id="tabParent" width="965"  border="0" cellpadding="0" cellspacing="0">

		<tr>

    	<td align="center">

			<table id="tab" width="150" border="0" cellpadding="0" cellspacing="0">

      		<tr>

        	<td width="5"><html:img src="themes/standard/heading_left.gif" width="5" height="18" /></td>

        	<td width="140" background="themes/standard/heading_tile.gif"><div align="center" class="heading1">Default User</div></td>

        	<td width="5"><html:img src="themes/standard/heading_right.gif" width="5" height="18" /></td>

      		</tr>

    		</table>

		</td>

  		</tr>

	</table>



	<table id="blueBorder" bgcolor="#F0F7FF" width="965" border="0" cellpadding="0" cellspacing="0" class="blueBorder">

    <html:form action="/userDefaultDefAction">

      <tr>

        <td height="20px" align="center"><div align="right">&nbsp;</div></td>

      </tr>

      <tr>

        <td align="center">

		<fieldset style="width:950px;">

		<legend align="left" class="fieldSetHeading"></legend>

            

		<table width="950" border="0" id="profile">

              <tr>

                <td width="10%"><div align="right"><bean:message key="txt.HomeFolder" /></div></td>

                <td width="22%"><html:text property="txtHomeFolder" styleClass="blueBorder" style="width:200px" /></td>            

                <td width="10%"><div align="right"><bean:message key="txt.EmailAddress" /></div></td>

                <td width="31%"><html:radio property="radEmail" value="0"/><bean:message key="rad.EnabledMailFolder" />

                <html:text property="txtEmailAddress" styleClass="blueBor+der" style="width:105px" /></td>            

                <td width="10%"><div align="right"><bean:message key="cbo.Language" /></div></td>

                <td width="17%"><html:select property="cboLanguage" styleClass="blueBorder" style="width:155px"></html:select></td>

              </tr>

              <tr>

                <td>

                  <div align="right"><bean:message key="rad.Quota" /></div></td>

                <td><html:radio property="radQuota" value="0"/><bean:message key="rad.Quota.Limited" /> &nbsp;( <bean:message key="txt.Storage" />

                    <html:text property="txtStorage" styleClass="blueBorder" style="width:45px" /><bean:message key="rad.Quota.Mb" /> )</td>

                <td>

                  <div align="right"></div></td>

                <td valign="top"><html:radio property="radEmail" value="0"/><bean:message key="rad.Disabled" /></td>

                <td><div align="right"><bean:message key="cbo.CharacterSet" /></div></td>

                <td valign="top"><html:select property="cboCharacterSet" styleClass="blueBorder" style="width:155px"></html:select></td>

              </tr>

              <tr>

                <td><div align="right"></div></td>

                <td><html:radio property="radQuota" value="0"/><bean:message key="rad.Quota.Unlimited" />&nbsp;&nbsp; </td>

                <td><div align="right"><bean:message key="rad.Password" /></div></td>

                <td><html:radio property="radPassword" value="0"/><bean:message key="rad.CanBeChanged" /></td>

                <td><div align="right"><bean:message key="cbo.Locale" /></div></td>

                <td><html:select property="cboLocale" styleClass="blueBorder" style="width:155px"></html:select></td>

              </tr>

              <tr>

                <td>

				<div align="right"></div>

				</td>

                <td>&nbsp;</td>

                <td>

				<div align="right"></div>

				</td>

                <td><html:radio property="radPassword" value="0"/><bean:message key="rad.CantBeChanged" /></td>

                <td><div align="right"><bean:message key="cbo.TimeZone" /></div></td>

                <td><html:select property="cboTimeZone" styleClass="blueBorder" style="width:155px"></html:select></td>

              </tr>

              <tr>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

              </tr>

            </table>

		</fieldset>

        </td>

		</tr>

		<tr>

		<td align="center" height="25px">

        <div align="right">

          <html:button property="btnSave" styleClass="buttons" style="width:70px"><bean:message key="btn.Save" /></html:button>

          <html:button property="btnCancel" styleClass="buttons" style="width:70px"><bean:message key="btn.Cancel" /></html:button>&nbsp;&nbsp; 

		</div>

		</td>

		</tr>

	</table>

</html:form>    

<!-- blueBorder table ends above-->

</td>

</tr>

</table>

<!-- tabContainer table ends above-->

</body>

</html:html>

