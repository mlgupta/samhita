<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="dms.web.actions.filesystem.AdvanceSearchAction" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="advanceSearchForm" name="advanceSearchForm" type="dms.web.actionforms.filesystem.AdvanceSearchForm" />

<%
  Object [] formats = (Object [])request.getAttribute("formats");
%>
<html>
<head>
<jsp:include page="/style_sheet_include.jsp" />
<title><bean:message key="title.FolderDocList" /></title>
<script src="general.js"></script>
<script src="datePickerForLinks.js"></script>
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
<script language="JavaScript" type="text/JavaScript">
  function showHideDivLink(divName){ 
    MM_findObj(divName).style.display=='none'?MM_showHideLayers(divName,'','show'):MM_showHideLayers(divName,'','hide');
    MM_findObj(divName).style.display=='none'?document.forms[0].advancedOptionEnabled.value='false':document.forms[0].advancedOptionEnabled.value='true';
  }
</script>
<script>
// Called to search for documents and folders    
  function folderDocSearch(){
    document.forms[0].target = window.top.frames[1].name;
    document.forms[0].action = "advanceSearchAction.do";
    document.forms[0].submit();
  }
// Called to reset search    
  function SearchReset(){    
    document.forms[0].target=window.top.frames[1].name;
    document.forms[0].action="b4ToggleFolderSearchAction.do";
    document.forms[0].submit();    
  }
  //Called when Search Lookup button is clicked
  function lookuponclick(){
    openWindow("folderDocSelectB4Action.do?heading=<bean:message key="lbl.ChoosePath" />&foldersOnly=true&openerControl=txtLookinFolderPath","searchLookUp",495,390,0,0,true);
  }
</script>
<!-- Date Related Scripts-->
<script>
  var fromDatePicker = new Calendar("fromDatePicker",0,0);
  fromDatePicker.noTimezone=true;
  fromDatePicker.noTime=true;
  fromDatePicker.onclick="selectedValues(fromDatePicker,'txtFromDate')";
  fromDatePicker.onclear="clearValues('txtFromDate')";
  fromDatePicker.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
  fromDatePicker.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
  fromDatePicker.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
  fromDatePicker.tooltipDay='<bean:message key="tooltips.cal.Day" />';
  fromDatePicker.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
  fromDatePicker.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
  fromDatePicker.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
  fromDatePicker.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
  fromDatePicker.tooltipNow='<bean:message key="tooltips.cal.Now" />';
  fromDatePicker.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
  fromDatePicker.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
  fromDatePicker.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
  fromDatePicker.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
  fromDatePicker.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

  var toDatePicker = new Calendar("toDatePicker",0,0);
  toDatePicker.noTimezone=true;
  toDatePicker.noTime=true;
  toDatePicker.onclick="selectedValues(toDatePicker,'txtToDate')";
  toDatePicker.onclear="clearValues('txtToDate')";
  toDatePicker.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
  toDatePicker.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
  toDatePicker.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
  toDatePicker.tooltipDay='<bean:message key="tooltips.cal.Day" />';
  toDatePicker.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
  toDatePicker.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
  toDatePicker.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
  toDatePicker.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
  toDatePicker.tooltipNow='<bean:message key="tooltips.cal.Now" />';
  toDatePicker.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
  toDatePicker.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
  toDatePicker.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
  toDatePicker.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
  toDatePicker.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';  

  function  selectedValues(dtPickerObj, txtDateTimeName){
    var dateString=dtPickerObj.getMonth();
    dateString+="/" +dtPickerObj.getDay();
    dateString+="/" +dtPickerObj.getYear();
    eval("document.forms[0]."+txtDateTimeName).value=dateString;
  }  

  function  clearValues(txtDateTimeName){
    eval("document.forms[0]."+txtDateTimeName).value="";
  }
  
  function window_onload(){   
    var currentDate=null;
    if (typeof document.forms[0].txtFromDate !='undefined'){
      if(document.forms[0].txtFromDate.value!=""){
        currentDate=new Date(document.forms[0].txtFromDate.value);
      }else{
        currentDate=new Date();
      }

      fromDatePicker.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                  currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());

      if(document.forms[0].txtFromDate.value!=""){
        fromDatePicker.click();
      }
    }   

    if (typeof document.forms[0].txtToDate !='undefined'){
      if(document.forms[0].txtToDate.value!=""){
        currentDate=new Date(document.forms[0].txtToDate.value);
      }else{
        currentDate=new Date();
      }
      toDatePicker.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                      currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
      if(document.forms[0].txtToDate.value!=""){
        toDatePicker.click();
      }
    }
  }
</script>
</head>
<body style="margin-left:5px;margin-top:0px;" class="imgData bgClrLvl_4" onload="window_onload();">
<html:form action="/advanceSearchAction">
<div style="height:385px;width:210px;left:0px;top:0px;">
  <table id="outermost" width="210px" border="0" cellpadding="0" cellspacing="2">
    <tr><td height="5px"></td></tr>
    <tr> 
      <td valign="top">
        <table border="0" cellpadding="0" cellspacing="1">
          <tr>
            <td>
              <bean:message key="lbl.SearchforFoldersAndDocuments" />
            </td>
          </tr>
          <tr>
            <td>
              <html:text name="advanceSearchForm" property="txtFolderOrDocName" styleClass="borderClrLvl_2 componentStyle" style="width:195px" /><br/>
            </td>
          </tr>
          <tr>
            <td>
              <bean:message key="lbl.YouCanUseWildCard" />
            </td>
          </tr>
          <tr><td height="5px"></td></tr>                      
          <tr>
            <td>
              <bean:message key="lbl.DocDescription" />
            </td>
          </tr>
          <tr>
            <td>
              <html:text name="advanceSearchForm" property="txtDocDescription" styleClass="borderClrLvl_2 componentStyle" style="width:195px" /><br/>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr><td height="5px"></td></tr>
    <tr>
      <td>
        <table border="0" cellpadding="0" cellspacing="1">
          <tr>
            <td>
              <bean:message key="lbl.ContainingText" />
            </td>
          </tr>
          <tr>
            <td>
              <html:text name="advanceSearchForm" property="txtContainingText" styleClass="borderClrLvl_2 componentStyle" style="width:195px" onkeypress="return alphaNumericOnly(event);"/><br/>        
            </td>
          </tr>
        </table>                  
      </td>
    </tr>
    <tr><td height="5px"></td></tr>
    <tr>
      <td>
        <table border="0" cellpadding="0" cellspacing="1">
          <tr>
            <td>
              <bean:message key="lbl.LookIn" />
            </td>
          </tr>
          <tr>
            <td>
              <html:text name="advanceSearchForm" property="txtLookinFolderPath" styleClass="borderClrLvl_2 componentStyle" style="width:170px" />
              <html:button  property="btnLookup" onclick="lookuponclick();" styleClass="buttons"  value="..." style="width:20px; height:17px;" ></html:button>      
            </td>
          </tr>
        </table>                                    
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <div align="left">
          <html:button property="btnSearch" onclick="folderDocSearch()" styleClass="buttons" style="width:60px" ><bean:message key="btn.Search" /></html:button>
          <html:button property="btnStop" styleClass="buttons" style="width:60px" ><bean:message key="btn.Stop" /></html:button>     
          <html:button property="btnReset" onclick="SearchReset()" styleClass="buttons" style="width:60px" ><bean:message key="btn.Reset" /></html:button>
        </div>
      </td>
    </tr>
    <tr><td height="5px"></td></tr>
    <tr>
      <td>
        <a href="#" class="menu" onClick="showHideDivLink('searchDrop')"><bean:message key="lbl.SearchOption" /></a>
        <html:hidden name="advanceSearchForm" property="advancedOptionEnabled" />
      </td>
    </tr>
    <tr>
      <td>
        <div id="searchDrop" class="imgData bgClrLvl_4" style="<%=advanceSearchForm.isAdvancedOptionEnabled()?"display:":"display:none"%>;width:210px;">
          <table id="search" class="borderClrLvl_2" width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><html:checkbox name="advanceSearchForm" property="chkDateSelected" onclick="this.checked?MM_showHideLayers('dateDrop','','show'):MM_showHideLayers('dateDrop','','hide')" /><bean:message key="chk.Date" /></td>
            </tr>
            <tr>
              <td>
                <div id="dateDrop" style="<%=advanceSearchForm.isChkDateSelected()?"display:":"display:none"%>;">
                  <table id="date" width="100%"  border="0">
                    <tr>
                      <td>
                        <div style="margin-left:33px">
                          <html:select property="cboDateOption"  value="" styleClass="borderClrLvl_2 componentStyle" style="width:137px">
                            <html:option value="<%=AdvanceSearchAction.LASTMODIFIEDDATE%>"><bean:message key="lbl.FilesModified" /></html:option>
                            <html:option value="<%=AdvanceSearchAction.CREATEDATE%>"><bean:message key="lbl.FilesCreated" /></html:option>                                
                          </html:select>                
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <table border="0" cellpadding="0" cellspacing="1">
                          <tr>
                            <td align="right" width="30px"><bean:message key="txt.FromDate" />&nbsp;</td>  
                            <td>
                              <html:hidden name="advanceSearchForm" property="txtFromDate" styleClass="borderClrLvl_2 componentStyle" style="width:75px" />
                              <script>fromDatePicker.render();</script>                      
                             </td> 
                           </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <table border="0" cellpadding="0" cellspacing="1">
                          <tr>
                            <td align="right" width="30px"><bean:message key="txt.ToDate" />&nbsp;</td>  
                            <td>
                              <html:hidden name="advanceSearchForm" property="txtToDate" styleClass="borderClrLvl_2 componentStyle" style="width:75px" />
                              <script> toDatePicker.render();</script>                      
                            </td> 
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                  <!--date table ends-->
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <html:checkbox  name="advanceSearchForm" property="chkDocTypeSelected"  onclick="this.checked?MM_showHideLayers('typeDrop','','show'):MM_showHideLayers('typeDrop','','hide')" /><bean:message key="chk.Type" />
              </td>
            </tr>
            <tr>
              <td>
                <div id="typeDrop" style="<%=advanceSearchForm.isChkDocTypeSelected()?"display:":"display:none"%>;margin-left:33px;">
                  <html:select name="advanceSearchForm" property="cboDocType" styleClass="borderClrLvl_2 componentStyle" style="width:165px">
                    <html:options collection="formats"  property="id"  labelProperty="mimeType" />
                  </html:select>                
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <html:checkbox name="advanceSearchForm" property="chkSizeSelected"   onclick="this.checked?MM_showHideLayers('sizeDrop','','show'):MM_showHideLayers('sizeDrop','','hide')" /><bean:message key="chk.Size" />
              </td>
            </tr>
            <tr>
              <td>
                <div id="sizeDrop" style="<%=advanceSearchForm.isChkSizeSelected()?"display:":"display:none"%>;margin-left:33px;">
                  <html:select name="advanceSearchForm" property="cboSizeOption" styleClass="borderClrLvl_2 componentStyle" style="width:75px">
                    <html:option value="<%=AdvanceSearchAction.ATLEAST%>"><bean:message key="lbl.AtLeast" /></html:option>
                    <html:option value="<%=AdvanceSearchAction.ATMOST%>"><bean:message key="lbl.AtMost" /></html:option>
                  </html:select>
                  <html:text property="txtDocSize" name="advanceSearchForm" styleClass="borderClrLvl_2 componentStyle" style="width:72px;text-align:right;" maxlength="10" onkeypress="return integerOnly(event);" /><bean:message key="lbl.KB" />
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <html:checkbox name="advanceSearchForm" property="chkAdvanceFeatureSelected"  onclick="this.checked?MM_showHideLayers('advanceDrop','','show'):MM_showHideLayers('advanceDrop','','hide')" /><bean:message key="chk.AdvanceOption" />
              </td>
            </tr>
            <tr>
              <td>
                <div id="advanceDrop" style="<%=advanceSearchForm.isChkAdvanceFeatureSelected()?"display:":"display:none"%>;width:90%">
                  <table id="advance" width="100%"  border="0" cellspacing="0" cellpadding="0"> 
                    <tr>
                      <td>
                        <html:checkbox style="margin-left:30px" name="advanceSearchForm" property="chkSubFoldersSearch"  /><bean:message key="chk.SearchSubFolder" />                
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <html:checkbox style="margin-left:30px" name="advanceSearchForm" property="chkCaseSensitiveSearch" /><bean:message key="chk.CaseSensitive" />                
                      </td>
                    </tr>
                  </table>
                <!-- advance table ends-->
                </div>
              </td>
            </tr>
          </table>
            <!-- search table ends-->
        </div>
      </td>
    </tr>
  </table>
</div>
</html:form>
</body>
</html>