<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<bean:define id="genericVoucherReportForm" name="GenericVoucherReportForm" type="adapters.generic.actionforms.GenericVoucherReportForm" />

<%
request.setAttribute("topic","psvoucher_report_html");
String totalVouchers=genericVoucherReportForm.getTxtTotalVouchers();
String inProcessVouchers= genericVoucherReportForm.getTxtInProcessVoucher();
String processedVouchers= genericVoucherReportForm.getTxtProcessedVoucher();
String inQueueVouchers= genericVoucherReportForm.getTxtInQueueVoucher();
//Variable declaration
boolean firstTableRow;
firstTableRow = true;
%>

<html:html>
<head>
<title><bean:message key="title.VoucherReport" /></title>

<script src="general.js" ></script>
<script src="datepicker.js" ></script>
<script src="navigationbar.js" ></script>
<script src="timezones.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
  var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
  if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
  if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

//-->

</script>

<script>
    var createDateFrom = new Calendar("createDateFrom",0,0);
    createDateFrom.onclick="selectedValues(createDateFrom,'txtFromDate')";
    createDateFrom.onclear="clearValues('txtFromDate')";
    //createDateFrom.timezoneDisabled=true;
    createDateFrom.noTimezone=true;
    createDateFrom.noTime=true;
    createDateFrom.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    createDateFrom.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    createDateFrom.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    createDateFrom.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    createDateFrom.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    createDateFrom.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    createDateFrom.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    createDateFrom.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    createDateFrom.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    createDateFrom.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    createDateFrom.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    createDateFrom.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    createDateFrom.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    createDateFrom.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

    var createDateTo = new Calendar("createDateTo",0,0);
    createDateTo.onclick="selectedValues(createDateTo,'txtToDate')";
    createDateTo.onclear="clearValues('txtToDate')";
    //createDateTo.timezoneDisabled=true;
    createDateTo.noTimezone=true;
    createDateTo.noTime=true;
    createDateTo.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    createDateTo.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    createDateTo.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    createDateTo.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    createDateTo.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    createDateTo.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    createDateTo.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    createDateTo.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    createDateTo.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    createDateTo.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    createDateTo.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    createDateTo.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    createDateTo.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    createDateTo.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

    function  selectedValues(dtPickerObj, txtDateTimeName){
      var dateString=dtPickerObj.getMonth();
      dateString+="/" +dtPickerObj.getDay();
      dateString+="/" +dtPickerObj.getYear();
      dateString+=" " +dtPickerObj.getHours();
      dateString+=":" +dtPickerObj.getMinutes();
      dateString+=":" +dtPickerObj.getSeconds();
      eval("document.forms[0]."+txtDateTimeName).value=dateString;
    }

    function  clearValues(txtDateTimeName){
      eval("document.forms[0]."+txtDateTimeName).value="";
    }

</script>

<script language="Javascript1.1" >

  function callPage(relayOperation){
    var thisForm=document.forms[0];
    if((relayOperation!="search_report")){
      thisForm.operation.value=relayOperation;
      thisForm.submit();
    }else{
      thisForm.operation.value=relayOperation;
      thisForm.submit();
    }
  }
  
  function enter(thisField,e){
    var i;
    i=handleEnter(thisField,e);
    if (i==1) {
      return callPage('search_report');
    }
  }

  function window_onload(){
    var currentDate=null;
    if(document.forms[0].txtFromDate.value!=""){
      currentDate=new Date(document.forms[0].txtFromDate.value);
    }else{
      currentDate=new Date();
    }

    createDateFrom.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(), currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    createDateFrom.setTimezone(document.forms[0].timezone.value);

    if(document.forms[0].txtFromDate.value!=""){
      createDateFrom.click();
    }

    if(document.forms[0].txtToDate.value!=""){
      currentDate=new Date(document.forms[0].txtToDate.value);
    }else{
      currentDate=new Date();
    }

    createDateTo.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());

    createDateTo.setTimezone(document.forms[0].timezone.value);

    if(document.forms[0].txtToDate.value!=""){
        createDateTo.click();
    }
  }

</script>
<script>
  <!--
    strImage = "";
    strToWrite = "";
    arValues = new Array(<%=totalVouchers%>, <%=inQueueVouchers%>,<%=inProcessVouchers%>,<%=processedVouchers%>);
    totVou=<%=totalVouchers%> ;
    arLegends = new Array("Total","InQueue", "InProcess", "Processed");
    chartHeight = 300;
    maxHeight = 0;
    for(i = 0; i < arValues.length; i++) {
     if(arValues[i] > maxHeight)
        maxHeight = arValues[i];
    }
    //alert(maxHeight);
    strToWrite = "<table border='0' cellpadding='1' cellspacing='0' ><tr align='center' >";
    strToWrite +="<td class='borderRight666666' valign='bottom' align='right' ><table width='10px' border='0'>"
    
    for( var index = 0 ; index < 11 ; index++){
      if(index==0 && totVou != 0){
        strToWrite += "<tr><td height='26px' align='right'>"+parseInt(maxHeight)+"-</td></tr>"           
      }else if(index == 5){
        strToWrite+="<tr><td height='26px' valign='top' align='right'>No. of vouchers</td></tr>"
      }else{
        strToWrite+="<tr><td height='26px' valign='top' align='right'></td></tr>"
      }
    }
    
    strToWrite += "</table></td>"
    for(i = 0; i < arValues.length; i++) {
      strImage = "themes/standard/bar_color.gif";
      strToWrite += "<td  valign='bottom' >" + arValues[i] + "<br>";
      strToWrite += "<img src='" + strImage + "' height=" + parseInt((arValues[i] / maxHeight) * chartHeight) + " width=30 hspace=40></td>";
    }
    strToWrite += "</tr><tr>";
    strToWrite +="<td class='borderRight666666 borderTop666666' valign='bottom' >&nbsp</td>"
    for(i = 0; i<arLegends.length; i++) {
      if(i==0){
        strToWrite += "<td class='borderTop666666' width='40'> <div style='margin-left:40px' >" + arLegends[i] + "</div> </td>";
      }else{
        strToWrite += "<td class='borderTop666666' width='40'> <div style='margin-left:30px' >" + arLegends[i] + "</div> </td>";
      }
    }
    strToWrite += "</tr></table>";

    // -->
</script>
</head>
<body style="margin:0" onLoad="window_onload();">
<html:form name="genericVoucherReportForm" type="adapters.generic.actionforms.GenericVoucherReportForm" action="/genericRelayAction" >

<html:hidden property="operation" />
<html:hidden name="GenericVoucherReportForm" property="timezone" />

<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="95px">
		<!--Header Starts-->
		<jsp:include page="/adapters/header.jsp" />
		<!--Header Ends-->
	</td>
  </tr>
</table>
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<br>
<tr>
  <td align="center">
    <table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="152px">
          <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="140px" class="imgTabTile"><div align="center" class="tabText"><a class="tabTextLink" href="genericVoucherStatusListAction.do"><bean:message key="lbl.VoucherList" /></a> </div></td>
              <td width="5px" class="imgTabRight"></td>
            </tr>
          </table>
        </td>
        <td>
          <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="140px" class="imgTabTile"><div align="center" class="tabTextLink"><bean:message key="lbl.VoucherReport" /></div></td>
              <td width="5px" class="imgTabRight"></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  
    <table width="700px" border="0" cellpadding="0" cellspacing="0" class=" bgClrLvl_3 borderClrLvl_2">
      <tr>
			<td colspan="2" align="center" width="700px">
        
			  <fieldset style="width:570px; margin-top:20px">
			  
        <legend align="left" class="fieldSetHeading"><bean:message key="lbl.Search" />:</legend>
			  <table width="100%" border="0" cellpadding="2" cellspacing="2">
			  <tr>
          <td width="15%" align="right"><bean:message key="lbl.Zone" />:</td>
          <td width="34%">
            <html:select  name="GenericVoucherReportForm" property="voucherZone" style="width:160px" styleClass="borderClrLvl_2 componentStyle bgClrLvl_5">
              <html:options name="GenericVoucherReportForm" property="cboVoucherZone" />
            </html:select>				
          </td>
			    <td width="16%" align="right"><bean:message key="lbl.FromDate" />:</td>
			    <td width="35%">
            <html:hidden name="GenericVoucherReportForm" property="txtFromDate" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
              <script>
                createDateFrom.render();
              </script>
          </td>
			  </tr>
			  <tr>
          <td align="right">&nbsp;</td>
          <td></td>
			    <td align="right"><bean:message key="lbl.ToDate" />:</td>
			    <td>
            <html:hidden name="GenericVoucherReportForm" property="txtToDate" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
              <script>
                createDateTo.render();
              </script>
          </td>
			  </tr>
        </table>
			  </fieldset>
			</td>
		</tr>
		<tr>
		  <td colspan="2" align="center" height="25px">
		    <html:button property="btnGenerateReport" styleClass="buttons" style="width:70px" onclick="return callPage('search_report')" ><bean:message key="btn.Generate" /></html:button>
		  </td>
	  </tr>
		<tr>
			<td colspan="2" align="center">
    <fieldset style="width:570px; margin-top:20px">
<script><!--
  document.write(strToWrite);
// -->

</script>
<tr><td></td></tr>
</fieldset>
  </td>
</tr>
<tr>
  <td width="700" height="25px" align="right" ></td>
  <td width="10">&nbsp;</td>
</tr>		      
</table>
<!-- Border table ends above-->
</td>
</tr>
</table>
<!-- tabContainer table ends above-->
</html:form>
</body>
</html:html>
