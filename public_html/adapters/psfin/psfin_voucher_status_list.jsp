<%@ page import="adapters.psfin.beans.PSFINAdapterConstant" %>
<%@ page import="adapters.psfin.actionforms.PSFINVoucherStatusListForm" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />        
<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<bean:define id="voucherArrayList" name="voucherArrayList" type="java.util.ArrayList" /> 
<bean:define id="pageCount" name="PSFINVoucherStatusListForm" property="txtPageCount" />
<bean:define id="pageNo" name="PSFINVoucherStatusListForm" property="txtPageNo" />

<%
request.setAttribute("topic","psvoucher_status_list_html");
//Variable declaration
boolean firstTableRow;
firstTableRow = true;
String status=((PSFINVoucherStatusListForm)request.getAttribute("PSFINVoucherStatusListForm")).getVoucherStatus();
%>

<html:html>
<head>
<title><bean:message key="title.PSVoucherStatus" /></title>
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
  var navBar=new NavigationBar("navBar");
  navBar.setPageNumber(<%=pageNo%>);
  navBar.setPageCount(<%=pageCount%>);
  navBar.onclick="callPage('page_voucher')";
  navBar.msgPageNotExist="<bean:message key="page.pageNumberNotExists" />";
	navBar.msgNumberOnly="<bean:message key="page.enterNumbersOnly"/>";    
	navBar.msgEnterPageNo="<bean:message key="page.enterPageNo"/>";
  navBar.msgOnFirst="<bean:message key="page.onFirst"/>";
	navBar.msgOnLast="<bean:message key="page.onLast"/>";
	navBar.lblPage="<bean:message key="lbl.Page"/>";
	navBar.lblOf="<bean:message key="lbl.Of"/>";
	navBar.tooltipPageNo="<bean:message key="tooltips.PageNo"/>";
  navBar.tooltipFirst="<bean:message key="tooltips.First"/>";
	navBar.tooltipNext="<bean:message key="tooltips.Next"/>";
	navBar.tooltipPrev="<bean:message key="tooltips.Previous"/>";
	navBar.tooltipLast="<bean:message key="tooltips.Last"/>";
	navBar.tooltipGo="<bean:message key="btn.Go"/>";
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
    
<html:javascript formName="psfinVoucherStatusListForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
<script language="Javascript1.1" >

  function callPage(relayOperation){
    var thisForm=document.forms[0];
    thisForm.txtPageNo.value=navBar.getPageNumber();
    if(relayOperation =="reset_voucher"){
      if(checkSelected(thisForm)){
        thisForm.operation.value=relayOperation;
        thisForm.submit();
      }
    }else{  
      thisForm.operation.value=relayOperation;
      thisForm.submit();
    }
  }
  
  function checkSelected(thisForm){
    if (typeof thisForm.radSelect!="undefined"){
      if (typeof thisForm.radSelect.length !="undefined"){
        if(thisForm.hdnType.value == '<bean:message key="lbl.Inprocess"/>'){
          var itemSelected=0;
          for(index = 0; index < thisForm.radSelect.length; index++){
            if(thisForm.radSelect[index].checked){
              return true;
              itemSelected++;
            }
          }
          if(itemSelected==0){
            alert('<bean:message key="errors.radSelect.required"/>');
            return false;
          }
        }else{
          return false;
        }
      }else{
        if(thisForm.radSelect.checked){
          return true;
        }else{
          alert("Only Inprocess Items can be reset to Inqueue");
          return false;
        }
      }
    }else{
      alert('<bean:message key="errors.radSelect.noitem"/>');
      return false;
    }
    alert('<bean:message key="errors.radSelect.required"/>');
    return false;
  }

  function checkAll(thisForm){
    var checkValue = (thisForm.chkAll.checked)?true:false;
    var totalRows = thisForm.radSelect.length;
    var count=0;                
    if( typeof totalRows == "undefined" ){
      thisForm.radSelect.checked=checkValue;
    }else{
      for (count=0;count<totalRows;count++) {
       thisForm.radSelect[count].checked=checkValue;
      }
    }
  }

  function unCheckChkAll(me){
    var thisForm=me.form;
    var totalCount =thisForm.radSelect.length;
    var checkValue = (me.checked)?true:false;
    var isAllSelected=false;
    if(checkValue){        
      for (var count=0;count<totalCount;count++) {
        if(thisForm.radSelect[count].checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
          break;
        }
      }      
    }
    thisForm.chkAll.checked=isAllSelected;
  }


  function enter(thisField,e){
    var i;
    i=handleEnter(thisField,e);
    if (i==1) {
      return callPage('search_voucher');
    }
  }

  function window_onload(){
    MM_preloadImages('<%=imagepath%>/butt_reset_invoice_over.gif','<%=imagepath%>/butt_delete_invoice_over.gif');
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

</head>

<body style="margin:0" onLoad="window_onload();">

<html:form name="PSFINVoucherStatusListForm" type="adapters.psfin.actionforms.PSFINVoucherStatusListForm" action="/adapterRelayAction"  >

<html:hidden name="PSFINVoucherStatusListForm" property="operation" />
<html:hidden name="PSFINVoucherStatusListForm" property="timezone" />
<html:hidden name="PSFINVoucherStatusListForm" property="txtPageNo" />
<html:hidden name="PSFINVoucherStatusListForm" property="hdnType" />


<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

<table width="100%"  border="0" cellspacing="0" cellpadding="0" id="headerIncluder">
  <tr>
    <td height="95px">
      <!--Header Starts-->
      <jsp:include page="/adapters/header.jsp" />
      <!--Header Ends-->
    </td>
  </tr>
</table>
<br>
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
  <tr>
    <td align="center">
      <table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="152px">
            <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.VoucherList" /></div></td>
                <td width="5px" class="imgTabRight"></td>
              </tr>
            </table>
          </td>
          <td>
            <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="140px" class="imgTabTile"><div align="center" class="tabTextLink"><a class="tabTextLink" href="psfinVoucherReportAction.do"><bean:message key="lbl.VoucherReport" /></a></div></td>
                <td width="5px" class="imgTabRight"></td>
              </tr>
            </table>
          </td>
        </tr>
    </table>

    <table id="borderClrLvl_2" width="700px" border="0" cellpadding="0" cellspacing="0" class="borderClrLvl_2">
      <tr class="imgToolBar bgClrLvl_3" >
        <td height="30px" colspan="2" align="center">
    		<fieldset style="width:670px; margin-top:10px">
			  <legend align="left"><bean:message key="lbl.Search" />:</legend>
			  <table width="100%" border="0" cellpadding="2" cellspacing="2">
			  <tr>
				<td width="14%" align="right"><bean:message key="lbl.Status" />:</td>
				<td width="31%">
          <bean:define id="checkVoucherStatus" name="PSFINVoucherStatusListForm" property="voucherStatus" />
          <html:select  name="PSFINVoucherStatusListForm" property="cboInvoiceStatus" style="width:160px" styleClass="borderClrLvl_2 componentStyle bgClrLvl_5">
            
            <%
              if(checkVoucherStatus.equals(PSFINAdapterConstant.INQUEUE)){
            %>
              <option selected value="<%=PSFINAdapterConstant.INQUEUE%>"><bean:message key="lbl.Inqueue" /></option>
            <%}else{%>
              <option value="<%=PSFINAdapterConstant.INQUEUE%>"><bean:message key="lbl.Inqueue" /></option>
            <%}%>
            
            <%
              if(checkVoucherStatus.equals(PSFINAdapterConstant.INPROCESS)){
            %>
              <option selected value="<%=PSFINAdapterConstant.INPROCESS%>"><bean:message key="lbl.Inprocess" /></option>
            <%}else{%>
              <option value="<%=PSFINAdapterConstant.INPROCESS%>"><bean:message key="lbl.Inprocess" /></option>
            <%}%>
            
            <%
              if(checkVoucherStatus.equals(PSFINAdapterConstant.PROCESSED)){
            %>
              <option selected value="<%=PSFINAdapterConstant.PROCESSED%>"><bean:message key="lbl.Processed" /></option>
            <%}else{%>
              <option value="<%=PSFINAdapterConstant.PROCESSED%>"><bean:message key="lbl.Processed" /></option>
            <%}%>
            
          </html:select>				
        </td>
			    <td width="14%" align="right"><bean:message key="lbl.FromDate" />:</td>
			    <td width="28%">
            
            <html:hidden name="PSFINVoucherStatusListForm" property="txtFromDate" styleClass="borderClrLvl_2 componentStyle" style="width:120px" />
              <script>
                createDateFrom.render();
              </script>
          </td>
			    <td width="13%">
		        <a onClick="return callPage('search_voucher')" class="imgGo" title="<bean:message key="btn.Go" />" tabindex="5" ></a>
          </td>
			  </tr>
			  <tr>
				<td align="right"><bean:message key="lbl.Zone" />:</td>
				<td>
          <html:select name="PSFINVoucherStatusListForm" property="voucherZone" style="width:160px" styleClass="borderClrLvl_2 componentStyle bgClrLvl_5">
            <html:options name="PSFINVoucherStatusListForm" property="cboInvoiceZone" />
          </html:select>				
        </td>
			    <td align="right"><bean:message key="lbl.ToDate" />:</td>
			    <td>
            <html:hidden name="PSFINVoucherStatusListForm" property="txtToDate" styleClass="borderClrLvl_2 componentStyle" style="width:120px" />
					  <script>createDateTo.render()</script>
          </td>
			    <td>&nbsp;</td>
			  </tr>
			</table>
			</fieldset>
		
		</td>
        </tr>
      <tr class="imgToolBar bgClrLvl_3" >
        <td width="293" height="30px">
          <a onClick="return callPage('reset_voucher')" class="imgResetInvoice" style="margin-left:5px;" onMouseOut="this.className='imgResetInvoice'" onMouseOver="this.className='imgResetInvoiceOver'" title="<bean:message key="tooltips.ResetInvoice" />" tabindex="1"></a>
        </td>
      </tr>
      <tr>
        <td colspan="3" align="center">
        <div class="imgData bgClrLvl_4" style="overflow:auto; width:100%; height:357px">
          <table class="bgClrLvl_F" id="data" width="100%" border="0" cellpadding="0" cellspacing="1">
            <tr>
              <th width="6%" height="18px" >
                <%if(status.equalsIgnoreCase(PSFINAdapterConstant.INPROCESS)){%>
                  <input type=checkbox name="chkAll"  style="height:12px" value="" onclick="checkAll(this.form)"/>
                <%}else{%>          
                  <input type=checkbox name="chkAll" disabled style="height:12px" value="" onclick="checkAll(this.form)"/>
                <%}%>                
              </th>
              <th width="39%"><bean:message key="tbl.head.DocumentName" /></th>
              <th width="13%"><bean:message key="tbl.head.CurrentStatus" /></th>
              <th width="14%"><bean:message key="tbl.head.InqueueDate" /></th>
              <th width="14%"><bean:message key="tbl.head.InprocessDate" /></th>
              <th width="14%"><bean:message key="tbl.head.ProcessedDate" /></th>
            </tr>
                <% if(voucherArrayList.size()>0) {%>
                  <logic:iterate name="voucherArrayList" id="voucherArrayLists">
                    <%if (firstTableRow == true){ firstTableRow = false; %>
                      <tr class="bgClrLvl_4">
                    <%}else{ firstTableRow = true; %>
                      <tr class="bgClrLvl_3">
                    <%}%>
                  <td>
                    <bean:define id="psVoucherStatus" name="voucherArrayLists" property="psVoucherStatus" />
                    <bean:define id="docId" name="voucherArrayLists" property="docID" />
                    <div align="center">
                      <logic:equal name="psVoucherStatus" value="InProcess" >
                        <html:multibox name="PSFINVoucherStatusListForm" property="radSelect" value="<%=docId%>" />
                      </logic:equal>
                      <logic:notEqual name="psVoucherStatus" value="InProcess" >
                        <html:multibox name="PSFINVoucherStatusListForm" property="radSelect" disabled="true" value="<%=docId%>" />
                      </logic:notEqual>
                    </div>
                  </td>
                  
                  <td><bean:write name="voucherArrayLists" property="docName" /></td>
                  <td><bean:write name="voucherArrayLists" property="psVoucherStatus" /></td>
                  <td><bean:write name="voucherArrayLists" property="dateInqueue" /></td>
                  <td><bean:write name="voucherArrayLists" property="dateInprocess" /></td>
                  <td><bean:write name="voucherArrayLists" property="dateProcessed" /></td>
                </tr>
              </logic:iterate>
            <%}else{%>
              <tr class="bgClrLvl_4">
                <td colspan="6">
                  <div style="width:100%; position:relative; top:175px; text-align:center;" class="tabText"><bean:message key="info.no_item_found.no_item_found" /></div> 
                </td>
              </tr>
            <%}%>
          </table>
        </div>        
      </td>
      </tr>
    </table>

<!-- borderClrLvl_2 table ends above-->
  </td>
</tr>
	<tr><td height="2px"></td></tr>
	<tr>
    <td align="center">
      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="700px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
        <tr>
          <td width="30px"><div class="imgStatusMsg"></div></td>
          <td width="467px">
            <logic:messagesPresent>
              <bean:message key="errors.header"/>
              <html:messages id="error">
                <font color="red"><bean:write name="error"/></font>
              </html:messages>
            </logic:messagesPresent>
            <html:messages id="msg" message="true">
              <bean:write  name="msg"/>
            </html:messages>
          </td>
          <td width="3px">
            <div style="float:left; width:1px;height:22px;" class="bgClrLvl_2"></div>
            <div style=" float:left; width:1px;height:22px;" class="bgClrLvl_F"></div>
          </td>
          <td width="200px" align="right">
            <script>navBar.render();</script>
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
