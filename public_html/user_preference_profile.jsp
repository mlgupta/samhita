<%@ page import="dms.beans.*" %>
<%@ page import="dms.web.beans.user.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%request.setAttribute("topic","user_preferences_html");
%>

<html:html> 
<head>
<title><bean:message key="title.UserPreferenceProfile" /></title>
<script src="general.js"></script>
<html:javascript formName="userPreferenceProfileForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
<script language="Javascript1.1" >

//function cancelActionPerformed(){
//    window.location.replace("folderDocMenuClickAction.do");
//}

function callPage(thisForm){ 
  if(validateUserPreferenceProfileForm(thisForm)){
    if(thisForm.txtPermittedTreeAccessLevel.value<=0){
      alert("<bean:message key="errors.userPreference.PermittedTreeAccessLevel"/>"); 
      return false;    
    }
    if(thisForm.txtItemsToBeDisplayedPerPage.value<=0){
      alert("<bean:message key="errors.userPreference.ItemsToBeDisplayedPerPage"/>");     
      return false;
    }
      thisForm.target="_self";
      thisForm.action="userPreferenceProfileAction.do";
      thisForm.submit();
  }
}

function setEncPass(){
  openWindow("","SetEncPass",200,500,0,0,true);
  document.forms[0].target = "SetEncPass";
  document.forms[0].action = "setEncryptionPasswordB4Action.do";
  document.forms[0].submit();
}

function setURLEncPass(){
  openWindow("","SetURLEncPass",200,500,0,0,true);
  document.forms[0].target= "SetURLEncPass";
  document.forms[0].action= "setURLEncryptPasswordB4Action.do";
  document.forms[0].submit();
}
function changeEncPass(){        
  openWindow("","ChangeEncPass",200,500,0,0,true);
  document.forms[0].target = "ChangeEncPass";
  document.forms[0].action = "changeEncryptionPasswordB4Action.do";
  document.forms[0].submit();
}
function preview_onclick(){
  openWindow("","preview",600,875,0,0,true);
  document.forms[0].target = "preview";
  document.forms[0].action = "userPreferenceThemePreviewAction.do" ;
  document.forms[0].submit();
}

function removeFromList(thisForm){
  var docID=null;
  var index=0;
  var count=0;
  var length=thisForm.lstWatchDocumentID.length;
  if( length!=0 ){
  while(index <length){
    if(thisForm.lstWatchDocumentID[index].selected){
      if (docID==null){
        docID=thisForm.lstWatchDocumentID[index].value;
      }else{
        docID= docID + "," + thisForm.lstWatchDocumentID[index].value;
      }
      thisForm.lstWatchDocumentID.remove(index);
      length=thisForm.lstWatchDocumentID.length;
    }else{
      index++;
    }
  }
  if( docID == null ){
    alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
    return false;
  }
  thisForm.hdnSelectedDocID.value=docID;
  document.forms[0].target = "_self";
  document.forms[0].action = "manageWatchAction.do?operation=delete";
  document.forms[0].submit();
  }else{
  	alert("<bean:message key="errors.noitem.to.del" />");
    return false;
  }
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
<html:form  action="/userPreferenceProfileB4Action" 
            onsubmit="return validateUserPreferenceProfileForm(this);" > 

<html:hidden property="hdnSelectedDocID" value="" />
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'borderClrLvl_2' -->
<br>
<tr>
<td align="center">
	<table id="tabParent" width="720px"  border="0" cellpadding="0" cellspacing="0">
    <bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
		<tr>
    	<td width="151px">
        <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.UserPreference" /></div></td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
      <% if(userInfo.isSystemAdmin()){%> 
    	<td width="151px">
        <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="140px" class="imgTabTile">
              <div align="center" class="tabText">
                <a class="tabTextLink" style="cursor:pointer;cursor:hand" href="adapterPreferencesB4Action.do"><bean:message key="lbl.AdapterPreference" /></a>
              </div>
            </td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
      <%}%>
      <td>&nbsp;</td>
  	</tr>
	</table>
	<table id="borderClrLvl_2"  width="720px" border="0" cellpadding="0" cellspacing="0" class="imgData borderClrLvl_2 bgClrLvl_4">
    <tr><td align="center" height="10px"></td></tr>
		<tr>
      <td align="center">
        <fieldset style="width:680px">
          <legend align="left" class="tabText"><bean:message key="lbl.MyProfile" /></legend>
          <table width="660px" border="0" id="profile" cellspacing="1" cellpadding="1">
            <tr>
              <td colspan="4">
              <table width="660px" border="0" cellpadding="0">
                <tr>
                  <td width="287px">&nbsp;</td>
                  <td width="142px" align="right"><bean:message key="lbl.Watch.Documents" /></td>
                  <td width="200px" rowspan="5" valign="top" >
                  <%if( !(((Boolean)request.getAttribute("tableDropped")).booleanValue()) ){%>
                    <html:select name="userPreferenceProfileForm" property="lstWatchDocumentID" size="7"  multiple="true" style="width:200px;" styleClass="borderClrLvl_2 componentStyle" >
                      <html:options property="lstWatchDocumentID" labelProperty="lstWatchDocumentName"  /> 
                    </html:select>
                  </td>
                  <td width="21px" rowspan="5" valign="top">
                    <html:button value="+" property="btnAdd" styleClass="buttons" style="width:20px;display:none;"  onclick="openWindow('changePasswordB4Action.do','userPassword',200,500,0,0,true)"></html:button>
                    <html:button value="-" property="btnRemove" styleClass="buttons" style="width:20px; margin-top:80px" onclick="removeFromList(this.form)" title="Remove Watch" />
                  </td>
                  <%}else{%>
                    <html:textarea rows="7" style="width:200px;" styleClass="borderClrLvl_2 componentStyle" name="userPreferenceProfileForm" property="lstWatchDocumentName" />
                  <%}%>
                </tr>
                <tr>
                  <td align="right">
				  <!--  <logic:present  name="isChangePassEnabled" scope="request" >
                 </logic:present> -->
                   <html:button  property="btnChangeLoginPassword" styleClass="buttons" style="width:200px" onclick="openWindow('changePasswordB4Action.do','userPassword',200,500,0,0,true)">
                     <bean:message key="btn.ChangeLoginPassword" />
        				   </html:button>
                  </td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="right">
    			         <% if(((Boolean)request.getAttribute(DbsDirectoryUser.ENCRYPTION_ENABLED)).booleanValue()){ %>
                      <html:button  property="btnChangeEncryptDecryptPassword" styleClass="buttons" style="width:200px"  onclick="changeEncPass()" ><bean:message key="btn.ChangeEncryptDecryptPassword" /></html:button>
                    <%}else{%>
                      <html:button  property="btnSetEncryptDecryptPassword" styleClass="buttons" style="width:200px" onclick="setEncPass()" ><bean:message key="btn.SetEncryptDecryptPassword" /></html:button>                    
                    <%}%>
                  </td>
                  <td >&nbsp;</td>
                </tr>
                <%if(!(((Boolean)request.getAttribute("hideURLOption")).booleanValue())){ %>
                <tr>
                  <td align="right" style="width:200px,display:none">
                    <%if(((Boolean)request.getAttribute("URLOptionVisibility")).booleanValue()) {%>
                      <html:button property="btnSetURLEncryptDecryptPassword" styleClass="buttons" style="width:200px" onclick="setURLEncPass()" ><bean:message key="btn.SetURLEncryptDecryptPassword" /></html:button> 
                    <%} else {%>
                      <html:button property="btnSetURLEncryptDecryptPassword" styleClass="buttons" style="width:200px" disabled="true" ><bean:message key="btn.SetURLEncryptDecryptPassword" /></html:button>
                    <% } %>
                  </td>
                  <td >&nbsp;</td>
                </tr>
                <% } %>
                <tr>
                  <td>&nbsp;</td>
                  <td >&nbsp;</td>
                </tr>
              </table>
            </td>
            </tr>
            <tr>
              <td colspan="4">
                <table width="660px" border="0" cellpadding="0">
                  <!--empty table for horizontal Line -->
                  <tr>
                    <td height="10px" >&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="bgClrLvl_2" height="1px"></td>
                  </tr>
                  <tr>
                    <td height="10px" >&nbsp;</td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="100"><div align="right"><bean:message key="txt.UserName" /></div></td>
              <td width="200" class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="userName" /></td>
              <td width="160"><div align="right"><bean:message key="txt.EmailAddress" /></div></td>
              <td width="200" class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="emailAddress" /></td>
            </tr>
            <tr>
              <td><div align="right"><bean:message key="txa.Description" /></div></td>
              <td rowspan="5" class="borderClrLvl_2">
                <div width="170px" height="100px" style="overflow:auto;word-wrap:break-word" >&nbsp;
                  <bean:write name="userPreference" property="description" />
                </div>              </td>
              <td><div align="right"><bean:message key="txt.MailFolder" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="mailFolder" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td><div align="right"><bean:message key="txt.AccessControlList" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="accessControlList" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td><div align="right"><bean:message key="txt.DefaultDocumentAcl" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="defaultDocumentAcl" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td><div align="right"><bean:message key="txt.DefaultFolderAcl" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="defaultFolderAcl" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td><div align="right"><bean:message key="txt.HomeFolder" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="homeFolder" /></td>
              <td><div align="right"><bean:message key="txt.Language" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="language" /></td>
            </tr>
            <tr>
              <td><div align="right"><bean:message key="txt.Quota" /></div></td>
              <td class="borderClrLvl_2"> &nbsp;
                <bean:write name="userPreference" property="storage" />              </td>
              <td>
                <div align="right"><bean:message key="lbl.CharcterSet" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="characterSet" /></td>
            </tr>
            <tr>
              <td><div align="right"></div></td>
              <td>&nbsp;</td>
              <td><div align="right"><bean:message key="lbl.Locale" /></div></td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="locale" /></td>
            </tr>
            <tr>
              <td>
                <div align="right"><bean:message key="lbl.Group" />:</div>              </td>
              <td>
                <html:select  property="cboGroup" styleClass="borderClrLvl_2 componentStyle" style="width:200px">
                  <html:options name="userPreferenceProfileForm" property="cboGroup" />    
                </html:select>              </td>
              <td>
                <div align="right"><bean:message key="lbl.TimeZone" /></div>              </td>
              <td class="borderClrLvl_2">&nbsp;<bean:write name="userPreference" property="timeZone" /></td>
            </tr>
          </table>
          <table width="660px" border="0" cellpadding="0">
            <tr>
            <td height="10px" ></td>
            </tr>
            <tr>
            <td class="bgClrLvl_2" height="1px"></td>
            </tr>
            <tr>
            <td height="10px" ></td>
            </tr>

          </table>
          <table width="660px" border="0" cellpadding="0" cellspacing="1">
            <tr>
              <td width="100px" align="right"><bean:message key="lbl.SelectaTheme" />:  </td>
              <td width="260px" >
                <html:select name="userPreferenceProfileForm" property="cboSelectTheme" styleClass="borderClrLvl_2 componentStyle" style="width:200px">
                  <html:option value="Default"  ><bean:message key="cbo.Default" /> </html:option>
                  <html:options name="themeList" />
                </html:select>
                <html:button property="btnPreview" styleClass="buttons" style="width:50px" onclick="return preview_onclick();"><bean:message key="btn.Preview" /></html:button>
              </td>
              <td align="right" width="260px"><b>*</b>&nbsp;<bean:message key="txt.PermittedTreeAccessLevel" />: </td>
              <td width="40px" ><html:text name="userPreferenceProfileForm" property="txtPermittedTreeAccessLevel" styleClass="borderClrLvl_2 componentStyle" style="width:35px; text-align:right" maxlength="4" onkeypress="return integerOnly(event);" /></td>
            </tr>
           
            <tr>
              <td align="right"><bean:message key="cbo.NavigationType" />: </td>
              <td>
                <html:select name="userPreferenceProfileForm" property="cboNavigationType" styleClass="borderClrLvl_2 componentStyle" style="width:200px">
                  <html:option value="<%=UserPreferences.FLAT_NAVIGATION %>"><bean:message key="cbo.FlatNavigation" /> </html:option>
                  <html:option value="<%=UserPreferences.TREE_NAVIGATION %>"><bean:message key="cbo.TreeNavigation" /> </html:option>
                </html:select>
              </td>
              <td align="right"><bean:message key="txt.ItemsToBeDisplayedPerPage" />: </td>
              <td>
              <html:text name="userPreferenceProfileForm" property="txtItemsToBeDisplayedPerPage" styleClass="borderClrLvl_2 componentStyle" style="width:35px; text-align:right" maxlength="4" onkeypress="return integerOnly(event);"/></td>
            </tr>
            <!-- Checkbox added for open doc option -->
             <tr>
              <td style="width:2px,display:none"></td> 
              <td>
                <html:checkbox name="userPreferenceProfileForm" property="boolChkOpenDocInNewWin" onclick="this.checked?boolChkOpenDocInNewWin=true:boolChkOpenDocInNewWin=false">
                  <bean:message key="chk.OpenDocInNewWindow"/>
                </html:checkbox>
              </td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
             </tr> 
          </table>
        </fieldset>
      </td>
    </tr>
    <tr>
      <td align="center" height="30px">
        <table width="710px" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              &nbsp;&nbsp;&nbsp;<bean:message key="info.userpreference" />
            </td>
            <td>
              <div align="right">
                <html:button property="btnOk" styleClass="buttons" style="width:70px" onclick="return callPage(this.form);"><bean:message key="btn.Ok" /></html:button>
                <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="history.go(-1);"><bean:message key="btn.Cancel" /></html:button>&nbsp;&nbsp;&nbsp;
              </div>
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
          <td>
            <logic:messagesPresent >
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
