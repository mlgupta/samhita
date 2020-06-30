<%@ page import="dms.web.beans.user.UserPreferences" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="notificationsList" name="notificationsList" type="java.util.ArrayList" />

<bean:define id="pageCount" name="MyNotificationListForm" property="txtPageCount" />        

<bean:define id="pageNo" name="MyNotificationListForm" property="txtPageNo" />

<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />

<%
request.setAttribute("topic","introduction_to_workflow_html");
//Variable declaration

boolean firstTableRow;

firstTableRow = true;

%>

<html:html>
<head>
<title><bean:message key="title.MyNotification" /></title>
<script src="general.js"></script>
<script src="navigationbar.js"></script>
<script src="datepicker.js"></script>
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
  navBar.onclick="callPage('search_notification')";
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

<!-- Date Related Scripts-->

<script>

    var searchBeginDateFrom = new Calendar("searchBeginDateFrom",0,0);
    searchBeginDateFrom.noTimezone=true;
    searchBeginDateFrom.noTime=true;
    searchBeginDateFrom.onclick="selectedValues(searchBeginDateFrom,'txtSearchBeginDateFrom')";
    searchBeginDateFrom.onclear="clearValues('txtSearchBeginDateFrom')";
    searchBeginDateFrom.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    searchBeginDateFrom.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    searchBeginDateFrom.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    searchBeginDateFrom.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    searchBeginDateFrom.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    searchBeginDateFrom.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    searchBeginDateFrom.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    searchBeginDateFrom.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    searchBeginDateFrom.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    searchBeginDateFrom.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    searchBeginDateFrom.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    searchBeginDateFrom.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    searchBeginDateFrom.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    searchBeginDateFrom.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';
  
    var searchBeginDateTo = new Calendar("searchBeginDateTo",0,0);
    searchBeginDateTo.noTimezone=true;
    searchBeginDateTo.noTime=true;
    searchBeginDateTo.onclick="selectedValues(searchBeginDateTo,'txtSearchBeginDateTo')";
    searchBeginDateTo.onclear="clearValues('txtSearchBeginDateTo')";
    searchBeginDateTo.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    searchBeginDateTo.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    searchBeginDateTo.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    searchBeginDateTo.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    searchBeginDateTo.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    searchBeginDateTo.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    searchBeginDateTo.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    searchBeginDateTo.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    searchBeginDateTo.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    searchBeginDateTo.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    searchBeginDateTo.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    searchBeginDateTo.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    searchBeginDateTo.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    searchBeginDateTo.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';
    
      
    var searchEndDateFrom = new Calendar("searchEndDateFrom",0,0);
    searchEndDateFrom.noTimezone=true;
    searchEndDateFrom.noTime=true;
    searchEndDateFrom.onclick="selectedValues(searchEndDateFrom,'txtSearchEndDateFrom')";
    searchEndDateFrom.onclear="clearValues('txtSearchEndDateFrom')";
    searchEndDateFrom.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    searchEndDateFrom.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    searchEndDateFrom.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    searchEndDateFrom.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    searchEndDateFrom.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    searchEndDateFrom.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    searchEndDateFrom.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    searchEndDateFrom.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    searchEndDateFrom.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    searchEndDateFrom.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    searchEndDateFrom.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    searchEndDateFrom.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    searchEndDateFrom.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    searchEndDateFrom.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';
  
    var searchEndDateTo = new Calendar("searchEndDateTo",0,0);
    searchEndDateTo.noTimezone=true;
    searchEndDateTo.noTime=true;
    searchEndDateTo.onclick="selectedValues(searchEndDateTo,'txtSearchEndDateTo')";
    searchEndDateTo.onclear="clearValues('txtSearchEndDateTo')";
    searchEndDateTo.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    searchEndDateTo.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    searchEndDateTo.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    searchEndDateTo.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    searchEndDateTo.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    searchEndDateTo.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    searchEndDateTo.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    searchEndDateTo.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    searchEndDateTo.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    searchEndDateTo.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    searchEndDateTo.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    searchEndDateTo.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    searchEndDateTo.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    searchEndDateTo.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';
    
    
    function  selectedValues(dtPickerObj, txtDateTimeName){
        var dateString=dtPickerObj.getMonth();
        dateString+="/" +dtPickerObj.getDay();
        dateString+="/" +dtPickerObj.getYear();
       // dateString+=" " +dtPickerObj.getHours();
      //  dateString+=":" +dtPickerObj.getMinutes();
      //  dateString+=":" +dtPickerObj.getSeconds();
        eval("document.forms[0]."+txtDateTimeName).value=dateString;
    }  

    function  clearValues(txtDateTimeName){
      eval("document.forms[0]."+txtDateTimeName).value="";
    }

</script>
<script>
  function window_onload(){
  
    MM_preloadImages('<%=imagepath%>/butt_notify_details_over.gif');
  
    var currentDate=null;
    if(document.forms[0].txtSearchBeginDateFrom.value!=""){
        currentDate=new Date(document.forms[0].txtSearchBeginDateFrom.value);
    }else{
        currentDate=new Date();
    }
    
    searchBeginDateFrom.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    
    if(document.forms[0].txtSearchBeginDateFrom.value!=""){
        searchBeginDateFrom.click();
    }
        
    if(document.forms[0].txtSearchBeginDateTo.value!=""){
        currentDate=new Date(document.forms[0].txtSearchBeginDateTo.value);
    }else{
        currentDate=new Date();
    }
    searchBeginDateTo.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());                            
    if(document.forms[0].txtSearchBeginDateTo.value!=""){
        searchBeginDateTo.click();
    }               
    
    if(document.forms[0].txtSearchEndDateFrom.value!=""){
        currentDate=new Date(document.forms[0].txtSearchEndDateFrom.value);
    }else{
        currentDate=new Date();
    }
    
    searchEndDateFrom.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    
    if(document.forms[0].txtSearchEndDateFrom.value!=""){
        searchEndDateFrom.click();
    }
        
    if(document.forms[0].txtSearchEndDateTo.value!=""){
        currentDate=new Date(document.forms[0].txtSearchEndDateTo.value);
    }else{
        currentDate=new Date();
    }
    searchEndDateTo.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());                            
    if(document.forms[0].txtSearchEndDateTo.value!=""){
        searchEndDateTo.click();
    }               
  }
  
  function callPage(operation){

      var thisForm=document.forms[0]  ;
      thisForm.txtPageNo.value=navBar.getPageNumber();
      if(operation=="search_notification"){
          thisForm.action="myNotificationB4Action.do";
          thisForm.submit();
      }else if(operation=="notification_detail"){
           if(checkSelected(thisForm)){
          //alert(thisForm.radSelect.value);
            thisForm.action="notificationDetailsB4Action.do";
            thisForm.submit();
          }
      }else if(operation=="notification_close"){
          thisForm.action="myNotificationB4Action.do";
          thisForm.submit();
      }
  }
  
  function checkSelected(thisForm){

  if (typeof thisForm.radSelect!="undefined"){

    if (typeof thisForm.radSelect.length !="undefined"){

      for(index = 0 ; index < thisForm.radSelect.length ;index++){

        if(thisForm.radSelect[index].checked){

            return true;

        }

      }

    }else{

      if(thisForm.radSelect.checked){

          return true;

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
<body style="margin:0" onload="return window_onload();">

<html:form name="MyNotificationListForm" type="dms.web.actionforms.workflow.MyNotificationListForm" action="/relayAction.do"
         onsubmit="return validateMyNotificationListForm(this);" >

    <html:hidden name="MyNotificationListForm" property="txtPageNo" /> 
    <html:hidden property="operation" />

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
<br>
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

  <tr>
  <td align="center">
    <table id="tabParent" width="990px"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="152px">
            <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="mnu.WorkFlow" /></div></td>
                <td width="5px" class="imgTabRight"></td>
              </tr>
            </table>
          </td>
        </tr>
    </table>
    <table id="borderClrLvl_2" width="990px" border="0" cellpadding="0" cellspacing="0" class="borderClrLvl_2">
      <tr class="imgToolBar bgClrLvl_3" >
        <td width="80px" height="30px" >
          <a onclick="return callPage('notification_detail')" class="imgMyNotification" style="margin-left:5px;" onmouseout="this.className='imgMyNotification'" onmouseover="this.className='imgMyNotificationOver'" title="<bean:message key="tooltips.NotificationDetails" />" ></a>
<!--          <a onclick="return callPage('notification_close')" class="imgNotificationDelete" onmouseout="this.className='imgNotificationDelete'" onmouseover="this.className='imgNotificationDeleteOver'" title="<bean:message key="tooltips.CloseNotification" />" ></a> -->
      </td>
      <td align="right">
        <a class="imgGo" style="float:right;margin-right:5px" title="<bean:message key="btn.Go" />"  onclick="return callPage('search_notification')" > </a>
    </td>
  </tr>
  
  <tr class="bgClrLvl_4" >
	  <td colspan="2">
		<table width="98%"  border="0" align="center" cellpadding="1" cellspacing="0" class="borderClrLvl_2">
		  <tr>
			<td width="33%" valign="top">
			  <fieldset>
			  <legend align="left">Notification</legend>
				<table width="100%" border="0" cellpadding="0">
				  <tr>
					<td width="25%"><div align="right"><bean:message key="cbo.Status" />:</div></td>
					<td width="75%">
          <%String searchStatus=(String)request.getAttribute("status");%>
          
            <html:select property="cboStatus" styleClass="borderClrLvl_2 componentStyle" style="width:150px" >
            
              <% if(searchStatus.equals("OPEN")){ %>
                <option selected value="OPEN"><bean:message key="lbl.Open" /></option>
              <% }else{ %>
                <option  value="OPEN"><bean:message key="lbl.Open" /></option>
              <%}%>
              
              <% if(searchStatus.equals("CLOSED")){ %>
                <option selected value="CLOSED"><bean:message key="lbl.Closed" /></option>
              <% }else{ %>
                <option value="CLOSED"><bean:message key="lbl.Closed" /></option>
              <%}%>
               
              <% if(searchStatus.equals("CANCELED")){ %> 
                <option selected value="CANCELED"><bean:message key="lbl.Canceled" /></option>
              <% }else{ %>
                <option value="CANCELED"><bean:message key="lbl.Canceled" /></option>
              <%}%>
                  
              <% if(searchStatus.equals("ALL")){ %>     
                <option selected value="ALL"><bean:message key="lbl.All" /></option>
              <% }else{ %>
                <option value="ALL"><bean:message key="lbl.All" /></option>
              <%}%>
              
            </html:select>
					</td>
				  </tr>
				  <tr>
					<td height="23px"><div align="right"></div></td>
					<td>
            
					</td>
				  </tr>
				</table>
			  </fieldset>
			</td>
			<td width="33%">
			  <fieldset>
				<legend align="left"><bean:message key="lbl.BeginDate" /></legend>
				<table width="100%" border="0" cellpadding="0">
				  <tr>
					<td width="25%"><div align="right"><bean:message key="lbl.From" /></div></td>
					<td width="75 %">
            <html:hidden name="MyNotificationListForm" property="txtSearchBeginDateFrom" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
					  <script>
						searchBeginDateFrom.render();
					  </script>					  
					</td>
				  </tr>
				  <tr>
					<td><div align="right"><bean:message key="lbl.To" />:</div></td>
					<td>
            <html:hidden name="MyNotificationListForm" property="txtSearchBeginDateTo" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
					  <script>
						searchBeginDateTo.render();
					  </script>					  					  
					</td>
				  </tr>
				</table>
			  </fieldset>
			</td>
			<td width="33%">
			  <fieldset>
				<legend align="left"><bean:message key="lbl.EndDate" /></legend>
				<table width="100%" border="0" cellpadding="0">
				  <tr>
					<td width="25%">
					  <div align="right">
						<bean:message key="lbl.From" />
					  </div>
					</td>
					<td width="75%">
            <html:hidden name="MyNotificationListForm" property="txtSearchEndDateFrom" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
					  <script>
						searchEndDateFrom.render();
					  </script>					  					  
					</td>
				  </tr>
				  <tr>
					<td>
					  <div align="right">
						<bean:message key="lbl.To" />:
					  </div>
					</td>
  				<td>
            <html:hidden name="MyNotificationListForm" property="txtSearchEndDateTo" styleClass="borderClrLvl_2 componentStyle" style="width:120px"  />
					  <script>
						searchEndDateTo.render();
					  </script>					  					              
					</td>
				  </tr>
				</table>
			  </fieldset>
			</td>
		  </tr>
		  <tr><td height="3px"></td></tr>
		</table>
	  </td>
	</tr>
  
  <tr><td colspan="2" class="bgClrLvl_3" height="5px"></td></tr>

  <tr>
  
    <td colspan="2" align="center">
    
      <div class="imgData bgClrLvl_4" style="overflow:auto; width:100%; height:288px">
      
        <table  width="100%"  border="0" cellpadding="1" cellspacing="1" class="bgClrLvl_F" id="data">
        
          <tr>
          
            <th width="4%"><bean:message key="tbl.head.Select" /></th>
            
            <th width="10%"><bean:message key="tbl.head.Status" /></th>
            
            <th width="10%"><bean:message key="tbl.head.BeginDate" /></th>
            
            <th width="10%"><bean:message key="tbl.head.EndDate" /></th>
            
            <th width="66%"><bean:message key="tbl.head.Subject" /></th>
            
          </tr>
          
          <%if(notificationsList.size()>0){%>

            <logic:iterate name="notificationsList" id="notification" >

            <bean:define id="notificationId" name="notification" property="notificationId" scope="page" />            

             <%if (firstTableRow == true){ firstTableRow = false; %>

              <tr  class="bgClrLvl_4">

            <%}else{ firstTableRow = true; %>

              <tr  class="bgClrLvl_3">                  

            <%}%>       
                   
            <td>
            
              <div align="center" class="bgClrLvl_4">
              
                <html:radio property="radSelect" value="<%=notificationId%>" />
                
              </div>
              
            </td>
            
            <td align="left"><bean:write name="notification" property="status" /></td>
            
            <td align="left"><bean:write name="notification" property="beginDate" /></td>
            
            <td align="left"><bean:write name="notification" property="endDate" /></td>
            
            <td align="left"><bean:write name="notification" property="subject" /></td>
            
          </tr>
          
          </logic:iterate>
          
          <%}%>
          
          </table>
          
            <% if(notificationsList.size()==0) {%>

              <div style="position:relative; top:175px; text-align:center;" class="tabText">

                <bean:message key="info.no_item_found.no_item_found" />

              </div> 

            <%}%>
          
          
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
      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="990px" border="0" cellpadding="1" cellspacing="1" id="statusBar">
        <tr>
          <td height="23px" width="30px"><div class="imgStatusMsg"></div></td>
          
          <td height="23px" width="755px">
          
          <html:messages id="actionMessage" message="true">
            <bean:write  name="actionMessage"/>
          </html:messages>

          <html:messages id="actionError">
            <font color="red"> <bean:write  name="actionError"/></font>
          </html:messages>
          
          </td>
          
          <td height="23px" width="3px">
            <div style="float:left; width:1px;height:23px;" class="bgClrLvl_2"></div>
            <div style=" float:left; width:1px;height:23px;" class="bgClrLvl_F"></div>
          </td>
          <td height="23px" width="200px" align="right">
            <script>navBar.render();</script>
          </td>
        </tr>
      </table>
      <!-- statusBar table ends above-->
    </td>
  </tr>
</table>
<!-- tabContainer table ends above-->
</html:form>
</body>
</html:html>