<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
  request.setAttribute("topic","adapters_preferences_html");
%>
<% if(session.getAttribute("UserInfo")==null){ %>
  <jsp:forward page="login.jsp" >
       <jsp:param name='sessionExpired' value='true' />
  </jsp:forward>
<% }; %>
<% boolean isEnableLookUp = ((Boolean)request.getAttribute("enableLookUp")).booleanValue();%>
<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences"/>

<html:html> 
<head>
<title><bean:message key="title.AdapterPreferences" /></title>
<script src="general.js"></script>
<script language="Javascript1.1" >

function callPage(thisForm){ 
  thisForm.target="_self";
  for( index = 0 ; index < thisForm.lstAvailableAdapters.length ;index++ ){
    thisForm.lstAvailableAdapters[index].selected = true;
  }
  for( index = 0 ; index < thisForm.lstEnabledAdapters.length ;index++ ){
    thisForm.lstEnabledAdapters[index].selected = true;
  }
  thisForm.action ="adapterPreferencesAction.do"; 
  thisForm.submit();
}

function moveBetweenListBox(thisForm, flag){
  var index=0;
  if(flag=="LA2RA"){
    var source=thisForm.lstAvailableAdapters;    
    var target=thisForm.lstEnabledAdapters;
  }else {
    var source=thisForm.lstEnabledAdapters;    
    var target=thisForm.lstAvailableAdapters;
  }
  var length=source.length;
  while(index <length){     
    if(source[index].selected){
      var opt= new Option(source[index].value,source[index].value,false,false);
      target.options[target.options.length]=opt;   
      source.remove(index);
      length=source.length;   
    }else{
      index++;
    }
  }
}

function moveAllBetweenListBox(thisForm, flag){
  var index=0;
  if(flag=="LA2RA"){
    var source=thisForm.lstAvailableAdapters;    
    var target=thisForm.lstEnabledAdapters;
  }else {
    var source=thisForm.lstEnabledAdapters;    
    var target=thisForm.lstAvailableAdapters;
  }
  var length=source.length;
  while(index <length){     
    var opt= new Option(source[index].value,source[index].value,false,false);
    target.options[target.options.length]=opt;   
    source.remove(index);
    length=source.length;   
  }
}

//Called when Search Lookup button is clicked
function lookuponclick(){
  openWindow("folderDocSelectB4Action.do?heading=<bean:message key="lbl.ChoosePath" />&foldersOnly=true&openerControl=txtFolderPath","searchLookUp",495,390,0,0,true);
}

function clearField(me){
  if( (me.value == "---Specify New Key---") ||
      (trim(me.value).length == 0) ){
    me.value="";
    me.focus();
  }
}

function displayManagedFolderKey(){
  document.forms[0].hdnPrefix.value = document.forms[0].cboEnabledAdapters.value;
  if( document.forms[0].txtFolderPath.value.length == 0 ){
    alert("<bean:message key="msg.NoFolderSelected" />");
    return false;
  }
  if( (document.forms[0].newScreenKey.value == "---Specify New Key---") ||
      (trim(document.forms[0].newScreenKey.value).length == 0) ){
    alert("<bean:message key="msg.Please.specify.screen.key" />");
    document.forms[0].newScreenKey.value="";
    document.forms[0].newScreenKey.focus();
    return false;
  }
  //openWindow("","managedFolderKey",135,300,0,0,true);
  document.forms[0].target = "_self";
  document.forms[0].action = "displayManagedFolderKeyAction.do";
  document.forms[0].submit();
}

function viewManagedFolders(){
  document.forms[0].hdnPrefix.value = document.forms[0].cboEnabledAdapters.value;
  openWindow("","viewManagedFolders",330,550,0,0,true);
  document.forms[0].target = "viewManagedFolders";
  document.forms[0].action = "listManagedFoldersAction.do";
  document.forms[0].submit();
}

</script>
</head>
<body style="margin:0">
<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->
<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="95px">
      <!--Header Starts-->
        <jsp:include page="header.jsp" /> 
      <!--Header Ends-->
	</td>
  </tr>
</table>
<html:form  action="/adapterPreferencesAction" name="adapterPreferenceForm" type="adapters.actionforms.AdapterPreferenceForm"> 
<html:hidden name="adapterPreferenceForm" property="hdnPrefix" />
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'borderClrLvl_2' -->
<br>
<tr>
<td align="center">
	<table id="tabParent" width="720px"  border="0" cellpadding="0" cellspacing="0">
		<tr>
    	<td width="151px">
        <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="140px" class="imgTabTile"><div align="center" class="tabText"><a class="tabTextLink" style="cursor:pointer;cursor:hand" href="userPreferenceProfileB4Action.do"><bean:message key="lbl.UserPreference" /></a></div></td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
    	<td width="151px">
        <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="140px" class="imgTabTile">
              <div align="center" class="tabText"><bean:message key="lbl.AdapterPreference" /></div>
            </td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
      <td>&nbsp;</td>
  	</tr>
	</table>
	<table id="borderClrLvl_2"  width="720px" border="0" cellpadding="0" cellspacing="0" class="imgData borderClrLvl_2 bgClrLvl_4">
    <tr><td align="center" height="10px"></td></tr>
		<tr>
      <td align="center">
        <fieldset style="width:680px">
          <legend align="left" class="tabText"><bean:message key="lbl.ManageAdapters" /></legend>
          <table id="listParent" width="460px" border="0">
            <tr>
              <td width="215px"><div align="center"><bean:message key="lbl.AvailableAdapters" /></div></td>
              <td width="30px">&nbsp;</td>
              <td width="215px"><div align="center"><bean:message key="lbl.EnabledAdapters" /></div></td>
            </tr>
            <tr>
              <td>
                <html:select name="adapterPreferenceForm" property="lstAvailableAdapters" size="10"  multiple="true" style="width:215px; height:125px" styleClass="borderClrLvl_2 componentStyle" >              
                  <html:options name="adapterPreferenceForm" property="lstAvailableAdapters" /> 
                </html:select>              
              </td>
              <td>
                <table id="nav" width="100%"  border="0">
                  <tr>
                    <td><html:button property="btnLeftPB2RightPB" styleClass="buttons" style="width:30px" value="&gt;"  titleKey="tooltips.Move" onclick="moveBetweenListBox(this.form, 'LA2RA')" /></td>
                  </tr>
                  <tr>
                    <td><html:button property="btnLeftPB2RightPBAll" styleClass="buttons" style="width:30px" value="&gt;&gt;" titleKey="tooltips.MoveAll" onclick="moveAllBetweenListBox(this.form, 'LA2RA')" /></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td><html:button property="btnRightPB2LeftPBAll" styleClass="buttons" style="width:30px" value="&lt;&lt;" titleKey="tooltips.RemoveAll" onclick="moveAllBetweenListBox(this.form, 'RA2LA')" /></td>
                  </tr>
                  <tr>
                    <td><html:button property="btnRightPB2LeftPB" styleClass="buttons" style="width:30px" value="&lt;" titleKey="tooltips.Remove" onclick="moveBetweenListBox(this.form, 'RA2LA')"/></td>
                  </tr>
                </table>
              </td>
              <td>
                <html:select name="adapterPreferenceForm" property="lstEnabledAdapters" size="10"  multiple="true" style="width:215px; height:125px" styleClass="borderClrLvl_2 componentStyle" >              
                  <html:options name="adapterPreferenceForm" property="lstEnabledAdapters" /> 
                </html:select>              
              </td>
            </tr>
            <tr>
              <td colspan="3" align="right">
                <html:button property="btnSaveAdapters" styleClass="buttons" style="width:70px" onclick="return callPage(this.form);"><bean:message key="btn.Save" /></html:button>
              </td>
            </tr>
          </table>
          <table width="98%">
            <tr>
              <td colspan="3">
                <table width="100%" border="0" cellpadding="0">
                  <!--empty table for horizontal Line -->
                  <tr>
                    <td class="bgClrLvl_2" height="1px"></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          <bean:define id="cboAdapters" name="cboAdapters" type="java.util.ArrayList" />
          <% if( cboAdapters.size() > 0 ) {%>
          <table width="640px" border="0" cellpadding="1" cellspacing="1" id="adaptersFolderManagement">
            <div id="manageFolders" style="width:638px;">
              <tr>
                <td width="93px" align="right">
                  <html:checkbox name="adapterPreferenceForm" property="enableLookUp" onclick="this.checked?MM_showHideLayers('manageShowHide','','show'):MM_showHideLayers('manageShowHide','','hide')" />
                </td>
                <td height="22px" width="110px" align="right"><bean:message key="lbl.ManageFolderFor" /></td>
                <td height="22px" width="264px" align="left">
                  <html:select property="cboEnabledAdapters" styleClass="borderClrLvl_2 componentStyle" style="width:250px">
                    <logic:iterate id="cboAdapter" name="cboAdapters">
                    <bean:define id="prefix" name="cboAdapter" property="aclPrefix" />
                    <option selected="selected" value="<%=prefix%>"><bean:write name="cboAdapter" property="adapterName" /></option>
                    </logic:iterate>
                  </html:select>
                </td>
                <td height="22px" width="151px">
                  <div align="left">
                    <html:button property="btnView" onclick="viewManagedFolders();" style="width:70px;" styleClass="buttons" ><bean:message key="btn.View" /></html:button>
                  </div>
                </td>
              </tr>
            </div>
          </table>
          <table width="640px" border="0">
            <tr>
              <td height="22px" width="100%">
                <div id="manageShowHide" style="<%=isEnableLookUp?"display:":"display:none"%>" width="638px;" >
                <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" class="bdrBottomColor_336633">
                  <tr>
                    <td width="33%" align="right"><bean:message key="lbl.Specify.New.Screen.key" /></td>
                    <td width="43%">
                      <html:text name="adapterPreferenceForm" property="newScreenKey" onfocus="return clearField(this);" styleClass="borderClrLvl_2 componentStyle" style="width:252px; height:20px;" />
                    </td>
                    <td width="24%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="3"></td>
                  </tr>
                  <tr>
                    <td width="33%" align="right"><bean:message key="lbl.SelectFolder" /></td>
                    <td width="43%">
                      <html:text name="adapterPreferenceForm" property="txtFolderPath" styleClass="borderClrLvl_2 componentStyle" style="width:230px; height:20px;" readonly="true" />
                      <html:button onclick="lookuponclick();" property="btnLookUp" style="width:20px; height:18px; cursor:pointer; cursor:hand;" title="Look In..." styleClass="buttons" value="..." />
                    </td>
                    <td width="24%" align="left">
                      <html:button property="btnSaveKey" styleClass="buttons" style="width:70px" onclick="displayManagedFolderKey();"><bean:message key="btn.Save" /></html:button>
                      <!--<a onClick="displayManagedFolderKey();" title="<bean:message key="tooltips.add.key.for.folder.selected" />" class="imgGo"></a>-->
                    </td>
                  </tr>
                </table>
                </div>
              </td>
            </tr>
          </table>
          <%}%>
        </fieldset>
      </td>
    </tr>
    <tr>
      <td align="center" height="30px">
        <table width="710px" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>&nbsp;</td>
            <td align="right">
              <!--<html:button property="btnOk" styleClass="buttons" style="width:70px" onclick="return callPage(this.form);"><bean:message key="btn.Ok" /></html:button>-->
              <html:button property="btnCancel" styleClass="buttons" style="width:70px;margin-right:5px;" onclick="history.go(-1);"><bean:message key="btn.Cancel" /></html:button>&nbsp;&nbsp;&nbsp;
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <!-- borderClrLvl_2 table ends above-->
  </td>
</tr>
<tr><td height="2px"></td></tr>
<tr>
  <td align="center">
    <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4" height="20px" width="720px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
      <tr>
      <td width="30px"><div class="imgStatusMsg"></div></td>
      <td width="690px">
        <logic:messagesPresent>
        <bean:message key="errors.header"/>
          <html:messages id="error">
            <font color="red"><bean:write name="error"/></font>
          </html:messages>
        </logic:messagesPresent>
        <html:messages id="msg" message="true">
          <bean:write name="msg"/>
        </html:messages>
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
