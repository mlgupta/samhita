<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	font-weight: normal;
}
-->
</style>
</head>

<body>
<table width="650" height="215" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><img src="themes/images/img_preview.gif" width="650" height="215"></td>
  </tr>
</table>
<div id="Layer1" style="position:absolute; width:135px; height:15px; left: 13px; top: 27px; text-align:center;">
  <bean:message key="lbl.Headings" />
</div>
<div id="Layer2" style="position:absolute; width:135px; height:15px; left: 13px; top: 62px; text-align:center;">
 <bean:message key="lbl.BodyText" />   
</div>
<div id="Layer3" style="position:absolute; width:135px; height:15px; left: 13px; top: 97px; text-align:center;">
 <bean:message key="lbl.BoldText" />     
</div>
<div id="Layer4" style="position:absolute; width:135px; height:15px; left: 13px; top: 132px; text-align:center;">
 <bean:message key="lbl.ElementText" />        
</div>
<div id="Layer5" style="position:absolute; width:135px; height:15px; left: 13px; top: 167px; text-align:center;">
 <bean:message key="lbl.Backgrounds" />           
</div>
</body>
</html>