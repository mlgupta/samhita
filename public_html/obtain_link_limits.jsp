<%@ page import="dms.web.beans.user.*" %>
<%@ page import="dms.web.beans.filesystem.*" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences" />
<bean:define id="currentFolderId" name="FolderDocInfo" property="currentFolderId" />

<%
  int sizeIndex = ((java.util.ArrayList)request.getAttribute("docLinkDetails")).size();
  Long notSelectedVal = new Long(0);
  boolean folders = false;
%>
<html:html>
<head>
<title><bean:message key="title.doc.generate.url" /></title>
<jsp:include page="style_sheet_include.jsp" />
<script src="general.js"></script>
<script src="timezones.js"></script>
<script src="datePickerForLinks.js"></script>
<script>
  function closewindow(){
    document.forms[0].submit();
    window.location.replace("blank.html");
  }
</script>
<!-- Date Related Scripts-->
<script>
  
  <%
    String datePickerName="";
    for(int dtpIndex=0;dtpIndex<sizeIndex;dtpIndex++ ){
        datePickerName="timeLimitDatePicker"+dtpIndex;
  %>
  var <%=datePickerName%> = new Calendar("<%=datePickerName%>",0,0);
  
  <%=datePickerName%>.noTimezone=true;
  <%=datePickerName%>.noTime=true;
  <%=datePickerName%>.onclick="selectedValues(<%=datePickerName%>,'txtDateTimeName[<%=dtpIndex%>]',<%=dtpIndex%>)";
  <%=datePickerName%>.onclear="clearValues('txtDateTimeName[<%=dtpIndex%>]')";
  <%=datePickerName%>.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
  <%=datePickerName%>.tooltipCancel='<bean:message key="tooltips.cal.Close" />';
  <%=datePickerName%>.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
  <%=datePickerName%>.tooltipDay='<bean:message key="tooltips.cal.Day" />';
  <%=datePickerName%>.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
  <%=datePickerName%>.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
  <%=datePickerName%>.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
  <%=datePickerName%>.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
  <%=datePickerName%>.tooltipNow='<bean:message key="tooltips.cal.Now" />';
  <%=datePickerName%>.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
  <%=datePickerName%>.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
  <%=datePickerName%>.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
  <%=datePickerName%>.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
  <%=datePickerName%>.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';
  <%
  }
  %>
  function  selectedValues(dtPickerObj, txtDateTimeName, datePkrIndex){
    var dateString=dtPickerObj.getMonth();
    dateString+="/" +dtPickerObj.getDay();
    dateString+="/" +dtPickerObj.getYear();
    dateString+=" " +dtPickerObj.getHours();
    dateString+=":" +dtPickerObj.getMinutes();
    dateString+=":" +dtPickerObj.getSeconds();
    MM_findObj(txtDateTimeName).value=dateString;
    MM_findObj('chkSelect['+datePkrIndex+']').checked=false;
  }  

  function  clearValues(txtDateTimeName){
    MM_findObj(txtDateTimeName).value="";
  }
  
  function window_onload(){
    
    <%
    
    String hdnDateName="";
    for(int dtpIndex=0;dtpIndex<sizeIndex;dtpIndex++ ){
        datePickerName="timeLimitDatePicker"+dtpIndex;
        hdnDateName="txtDateTimeName[" + dtpIndex + "]";
    %>
      var varDateTimeName<%=dtpIndex%> = MM_findObj('<%=hdnDateName%>');
      if (typeof varDateTimeName<%=dtpIndex%> !='undefined'){
        if(varDateTimeName<%=dtpIndex%>.value!=""){
            currentDate=new Date(varDateTimeName<%=dtpIndex%>.value);
        }else{
            currentDate=new Date();
        }
        <%=datePickerName%>.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                    currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
        <%=datePickerName%>.setTimezone(document.forms[0].hdnTimeZone.value);
        if(varDateTimeName<%=dtpIndex%>.value!=""){
            <%=datePickerName%>.click();
        }
      }
    <%
    }
    %>
    
  } 
  
  function generateLinks(){
    var dateObj = null;
    var linkLimitObj = null;
    var chkSelectObj = null;
    var chkd = null;
    var documentNameObj = null;
    <%for( int index =0; index < sizeIndex ; index++ ){ %>
      dateObj = MM_findObj('txtDateTimeName[<%=index%>]');
      linkLimitObj = MM_findObj('combotext[<%=index%>]');
      chkSelectObj = MM_findObj('chkSelect[<%=index%>]');
      chkd = (chkSelectObj.checked)?true:false;
      documentNameObj = MM_findObj('documentName[<%=index%>]');
      if( (dateObj.value == "" ) && (linkLimitObj.value == "" ) && (!chkd) ){
        alert("Please specify an option for "+documentNameObj.value);
        return;
      }
      
      if( (linkLimitObj.value != "unlimited") && ( !chkd ) ){
        if(isNumericOnly(linkLimitObj.value) == false){
          alert("Please enter numeric value for "+documentNameObj.value);
          linkLimitObj.value="";
          linkLimitObj.focus();
          return;
        }
      }
    <%}%>
    
    document.forms[0].target = "_self";
    document.forms[0].action = "docGenerateLinkAction.do";
    document.forms[0].submit();
  }
  
  function checkForNever(me,index){
    var linkLimitObj = null;
    if( me.checked ){
      eval('timeLimitDatePicker'+index).clear();
      linkLimitObj = MM_findObj('combotext['+index+']');
      linkLimitObj.value="";
    }
  }
  
  function unCheckNever(index){
    MM_findObj('chkSelect['+index+']').checked=false;
    return;
  }
</script>
<script lang="JavaScript">
<!--
  var fActiveMenu = false;
  var oOverMenu = false;
  
  function mouseSelect(e){
    if(fActiveMenu){
      if (oOverMenu == false){
        oOverMenu = false;
        document.getElementById(fActiveMenu).style.display = "none";
        fActiveMenu = false;
        return false;
      }
      return false;
    }
    return true;
  }
  
  function menuActivate(idEdit, idMenu, idSel, comboIndex ){
    if (fActiveMenu) return mouseSelect(0);
    oMenu = document.getElementById(idMenu);
    oMenu.style.display = "";
    fActiveMenu = idMenu;
    document.getElementById(idSel).focus();
    MM_findObj('chkSelect['+comboIndex+']').checked=false;
    return false;
  }
  
  function textSet(idEdit, text, comboIndex ){
    document.getElementById(idEdit).value = text;
    oOverMenu = false;
    mouseSelect(0);
    document.getElementById(idEdit).focus();
    MM_findObj('chkSelect['+comboIndex+']').checked=false;
  }
  
  function comboKey(idEdit, idSel, comboIndex){ 
    if (window.event.keyCode == 13 || window.event.keyCode == 32 || 
        (window.event.keyCode >=48 && window.event.keyCode <=57 )){
      textSet(idEdit,idSel.value,comboIndex);
    }else if (window.event.keyCode == 27){
      mouseSelect(0);
      document.getElementById(idEdit).focus();
    }
  }
  document.onmousedown = mouseSelect;
//-->
</script>

</head>
<body style="margin:7px" onLoad="return window_onload();">

<html:form action="/docNothingAction" name="linkDetailsForm" type="dms.web.actionforms.filesystem.LinkDetailsForm" >
<html:hidden property="hdnTimeZone" name="linkDetailsForm" />
<table id="tabContainer" align="center" width="95%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr><td height="5px"></td></tr>
<tr>
  <td >
    <table id="tabParent" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
          <table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.items.Links" /></div></td>
              <td width="5px" class="imgTabRight"></td>            
            </tr>
          </table>
        </td>
  		</tr>
    </table>
    <table id="borderClrLvl_2" align="center" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2"  >
      <tr><td height="15px" colspan="2"></td></tr>
      <tr>
        <td align="center" colspan="2">
          <div align="center" style="overflow:auto;width:90%;height:320px" class="bgClrLvl_4 borderClrLvl_2">
            <table width="97%"  style="margin:3px" align="left" border="0"  cellpadding="0" cellspacing="0" id="innerTabContainer">
              <tr><td height="2px" class="bgClrLvl_4" ></td></tr>
              <logic:iterate id="docLinkDetail" name="docLinkDetails" indexId="index" type="dms.web.beans.filesystem.FolderDocList" >
                <bean:define id="id" name="docLinkDetail" property="id" />
                <bean:define id="itemClassName" name="docLinkDetail" property="className" type="java.lang.String" />
                <tr>
                  <td colspan="5">
                    <table id="iterationTab" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr class="bgClrLvl_3" >            
                        <td align="right" width="15%" nowrap="nowrap" height="25px" >
                          <% if( itemClassName.equalsIgnoreCase("Document") ){%>
                            <bean:message key="lbl.DocumentName" />
                          <%}else{%>
                            <bean:message key="lbl.FolderName" />
                          <%}%>
                        </td> 
                        <td colspan="6">
                          <span style="margin:5px;">
                            <bean:write name="docLinkDetail" property="name" />
                            <input type="HIDDEN" id="txtDocId[<%=index%>]" name="txtDocId[<%=index%>]" value="<%=id%>" >
                            <input type="HIDDEN" id="documentName[<%=index%>]" name="documentName[<%=index%>]" value="<bean:write name="docLinkDetail" property="name" />" >
                          </span>
                        </td>
                      </tr>
                      <bean:define id="type" name="docLinkDetail" property="type" />
                      <tr class="bgClrLvl_3" >
                        <td width="23%" align="right">
                          <bean:message key="lbl.TimeLimit"/>
                        </td>
                        <td width="35%">
                          <div style="margin-left:4px">
                            <input type="HIDDEN" id="txtDateTimeName[<%=index%>]" name="txtDateTimeName[<%=index%>]" class="borderClrLvl_2 componentStyle" style="width:2px;">
                            <script><%="timeLimitDatePicker"+index%>.render();</script>
                          </div>
                        </td>
                        <td align="right" width="15%">
                          <bean:message key="lbl.NumericLimit"/>&nbsp;
                        </td>
                        <td width="12%" valign="top">
                          <input type="text" id="combotext[<%=index%>]" name="combotext[<%=index%>]" class="borderClrLvl_2 componentStyle bgClrLvl_3" size="20" style="width:100%;" value="5" onkeypress="return unCheckNever(<%=index%>);" >
                        </td>
                        <td width="2%" valign="top">
                          <img src="themes/standard/dropdown.gif" width="15" height="18" onClick="JavaScript:menuActivate('combotext['+<%=index%>+']', 'combodiv['+<%=index%>+']', 'txtLinkLimit['+<%=index%>+']', <%=index%>)">
                        </td>
                        <td width="1%">&nbsp;</td>
                        <td width="12%" align="left">
                          <bean:message key="lbl.NumericDownload" />
                        </td>
                      </tr>
                      <tr class="bgClrLvl_3">
                        <td align="right" height="22px">
                         
                        </td>
                        <td valign="top">
                          <input type="CHECKBOX" id="chkSelect[<%=index%>]" name="chkSelect[<%=index%>]" value="<%=id%>" onClick=" return checkForNever(this,<%=index%>);" ><bean:message key="lbl.Link.Never.Expires" />
                        </td>
                        <td>&nbsp;</td>
                        <td valign="top">
                          <div>
                            <div id="combodiv[<%=index%>]" style="display:none; position:absolute; z-index:2" onMouseOver="javascript:oOverMenu='combodiv['+<%=index%>+']';" onMouseOut="javascript:oOverMenu=false;">
                              <select size="11" id="txtLinkLimit[<%=index%>]" name="txtLinkLimit[<%=index%>]" style="width:87px; border-style: none" onclick="JavaScript:textSet('combotext['+<%=index%>+']',this.value,<%=index%>);" onkeypress="JavaScript:comboKey('combotext['+<%=index%>+']', this,<%=index%>);">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="5" selected="selected">5</option>
                                <option value="10">10</option>
                                <option value="15">15</option>
                                <option value="20">20</option>
                                <option value="30">30</option>
                                <option value="40">40</option>
                                <option value="50">50</option>
                                <option value="100">100</option>
                                <option value="unlimited">Unlimited</option>
                              </select>
                            </div> 
                          </div>
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr><td colspan="5" class="bgClrLvl_4" height="2px;"></td></tr>
              </logic:iterate>
              <tr><td height="2px" class="bgClrLvl_4" colspan="2"></td></tr>
            </table>
          </div>
        </td>
      </tr>
      <tr>
        <td height="30px" width="68%">&nbsp;</td>
        <td align="center" height="30px" width="32%" >
          <html:button property="btnOK" onclick="generateLinks();" styleClass="buttons" style="width:70px" ><bean:message key="btn.Generate" /></html:button>
          <html:button property="btnHelp" onclick="openWindow('help?topic=document_generate_links_html','Help',650,800,0,0,true);" styleClass="buttons" style="width:70px"><bean:message  key="btn.Help" /></html:button>
        </td>
      </tr>
    </table>
  </td>
</tr>
</table>
</html:form>

</body>
</html:html>
