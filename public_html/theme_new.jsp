<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%request.setAttribute("topic","theme_new_html");%>
<html:html>
<head>
<title><bean:message key="title.ThemeNew" /></title>
<STYLE type="text/css">
.tdSelected
{
    BACKGROUND-COLOR: #0A246A;
    COLOR:#FFFFFF;
}
.tdUnselected
{
    BACKGROUND-COLOR: #FFFFFF;
    COLOR:#000000;
}
</STYLE>
<script src="general.js"></script>
<script src="colorpalette.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
var colorPicker = new ColorPalette("colorPicker",100,300);
var bgcolorPicker = new ColorPalette("bgcolorPicker",88,320);


var colorPickerHeadingsObj=null;
var colorPickerBodyTextObj=null;
var colorPickerMenuTextObj=null;
var colorPickerElementTextObj=null;
var previousIdThemes=null;
var previousIdStyles=null;
var previousIdColorscheme=null;

var fontColorPicker=null;
var fontValueControl=null;

function tdClickThemes(obj) {
	previousIdThemes.className="tdUnselected"
	obj.className="tdSelected"
	previousIdThemes=obj;
}

function tdClickStyles(obj) {
	previousIdStyles.className="tdUnselected"
	obj.className="tdSelected"
	previousIdStyles=obj;
  document.forms[0].hdnStyle.value=obj.id;
  MM_findObj("colorDropInner").innerHTML=MM_findObj("colorDrop"+obj.id).innerHTML;
  var objColorScheme = eval("document.forms[0].cs4"+obj.id);
  if (typeof objColorScheme.length != "undefined"){
    previousIdColorscheme=MM_findObj(objColorScheme[0].value);
    tdClickColorSchemes(previousIdColorscheme);
  }else{
    previousIdColorscheme=MM_findObj(objColorScheme.value);
    tdClickColorSchemes(previousIdColorscheme);
  }
  
}

function tdClickColorSchemes(obj){
  var thisForm=document.forms[0];
  previousIdColorscheme.className="tdUnselected"
  if(typeof obj.length!="undefined"){
    thisForm.hdnColorScheme.value=obj[0].id;
    obj[0].className="tdSelected"
    previousIdColorscheme=obj[0];
  }else{
    thisForm.hdnColorScheme.value=obj.id;
    obj.className="tdSelected"
    previousIdColorscheme=obj;
  }
  
  var objHeading=eval("document.forms[0].prop4"+thisForm.hdnStyle.value+thisForm.hdnColorScheme.value+"Heading");
  var objBody=eval("document.forms[0].prop4"+thisForm.hdnStyle.value+thisForm.hdnColorScheme.value+"Body");
  var objMenu=eval("document.forms[0].prop4"+thisForm.hdnStyle.value+thisForm.hdnColorScheme.value+"Menu");
  var objElement=eval("document.forms[0].prop4"+thisForm.hdnStyle.value+thisForm.hdnColorScheme.value+"Element");

  if ( typeof objHeading.length != "undefined"){
    thisForm.hdnFontColorHeadings.value=objHeading[0].value;
  }else{
    thisForm.hdnFontColorHeadings.value=objHeading.value;
  }

  if ( typeof objBody.length != "undefined"){
    thisForm.hdnFontColorBodyText.value=objBody[0].value;
  }else{
    thisForm.hdnFontColorBodyText.value=objBody.value;
  }

  if ( typeof objMenu.length != "undefined"){
    thisForm.hdnFontColorMenuText.value=objMenu[0].value;
  }else{
    thisForm.hdnFontColorMenuText.value=objMenu.value;
  }

  if ( typeof objElement.length != "undefined"){
    thisForm.hdnFontColorElementText.value=objElement[0].value;
  }else{
    thisForm.hdnFontColorElementText.value=objElement.value;
  }

  colorPickerHeadingsObj=MM_findObj('colorPickerHeadings');
  colorPickerBodyTextObj=MM_findObj('colorPickerBodyText');
  colorPickerMenuTextObj=MM_findObj('colorPickerMenuText');
  colorPickerElementTextObj=MM_findObj('colorPickerElementText');
  colorPickerElementBgObj=MM_findObj('colorPickerElementBg');
  
  colorPickerHeadingsObj.bgColor=thisForm.hdnFontColorHeadings.value;
  colorPickerBodyTextObj.bgColor=thisForm.hdnFontColorBodyText.value;
  colorPickerMenuTextObj.bgColor=thisForm.hdnFontColorMenuText.value;
  colorPickerElementTextObj.bgColor=thisForm.hdnFontColorElementText.value;
  colorPickerElementBgObj.bgColor=thisForm.hdnFontColorElementBg.value;
}

function showColorPicker(picker,control4Picker){
  var objColorPicker=MM_findObj("ColorPicker");
  var height=215;
  var width=350;
  var top=(window.screen.availHeight/2)-(height/2);
	var left=(window.screen.availWidth/2)-(width/2);
  objColorPicker.style.top=top+20+"px";
  objColorPicker.style.left=(left)+"px";
  objColorPicker.style.height=height+"px";
  objColorPicker.style.width=width+"px";
  objColorPicker.style.display='';
  fontColorPicker=picker;
  colorPicker.click(control4Picker.value);
  fontValueControl=control4Picker;
}

function hideColorPicker(){
  var objColorPicker=MM_findObj("ColorPicker");
  objColorPicker.style.display='none';  
}

function btnOkColorPicker_onclick(){
  var pickedColor=colorPicker.selectedPalette;
  fontColorPicker.bgColor=pickedColor;
  fontValueControl.value=pickedColor;
  hideColorPicker();
}

function btnCancelColorPicker_onclick(){
  hideColorPicker();
}

function window_onload(){
  var thisForm=document.forms[0];
  var tempColorScheme=thisForm.hdnColorScheme.value;

  previousIdThemes=MM_findObj('selectedObjThemes');

  previousIdStyles=MM_findObj(thisForm.hdnStyle.value);
  tdClickStyles(previousIdStyles);
 
  tdClickColorSchemes(MM_findObj(tempColorScheme));

  if (thisForm.hdnBackgroundColor.value!=""){
    bgcolorPicker.click(thisForm.hdnBackgroundColor.value);
  }
  bgcolorPicker.onclick="setBackgroundColor()";
  selectBackground();
}

function setBackgroundColor(){
  document.forms[0].hdnBackgroundColor.value=bgcolorPicker.selectedPalette;
}

function setBackgroundImage(){
  document.forms[0].hdnBackgroundImgae.value="";
}

function selectBackground(){
  if(document.forms[0].radBackground[0].checked){
      MM_showHideLayers('backgroundColorDrop','','show','backgroundImageDrop','','hide');
  }else{
      MM_showHideLayers('backgroundColorDrop','','hide','backgroundImageDrop','','show');    
  }
} 

function preview_onclick(){
  openWindow("","preview",600,875,0,0,true);
  document.forms[0].target = "preview";
  document.forms[0].action = "themePreviewAction.do" ;
  document.forms[0].submit();
}

//-->
</script>
<html:javascript formName="themeNewEditForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
<script language="Javascript1.1" >
  function callPage(){ 
    var thisForm=document.forms[0];
    thisForm.action="themeNewAction.do"
    thisForm.target="_self";
    if(validateThemeNewEditForm(thisForm)){
      thisForm.submit();
    }
  }
</script>
</head>
<body style="margin:0" onload="window_onload();">
<html:form name="themeNewEditForm" type="dms.web.actionforms.theme.ThemeNewEditForm" action="/themeNewAction" >
  <html:hidden name="themeNewEditForm" property="hdnStyle" />
  <html:hidden name="themeNewEditForm" property="hdnColorScheme" />
  <html:hidden name="themeNewEditForm" property="hdnBackgroundColor" />
  <html:hidden name="themeNewEditForm" property="hdnBackgroundImage" />
  <html:hidden name="themeNewEditForm" property="hdnFontColorHeadings" />
  <html:hidden name="themeNewEditForm" property="hdnFontColorBodyText" />
  <html:hidden name="themeNewEditForm" property="hdnFontColorMenuText" />
  <html:hidden name="themeNewEditForm" property="hdnFontColorElementText" />
  <html:hidden name="themeNewEditForm" property="hdnFontColorElementBg" />
<!-- This page contains 3 outermost tables, 'headerIncluder', 'errorContainer' and 'tabContainer' -->
<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
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
<td align="center" >
	<table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">
		<tr>
    	<td>
			<table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="5px" class="imgTabLeft"></td>
          <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.NewTheme" /></div></td>
          <td width="5px" class="imgTabRight"></td>
        </tr>
      </table>
		</td>
  		</tr>
	</table>
	<table width="700px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">
      <tr>
        <td height="5px" colspan="2" align="center"></td>
      </tr>
      <tr>
        <td colspan="2" align="center">
          <table width="660px" height="220px" class="borderClrLvl_2" border="0" cellspacing="2px">
		  <!-- This table contains 4 tr with Theme Elements, Choices, Preview -->
            <tr>
              <td><bean:message key="lbl.ThemeElements" /></td>
              <td><bean:message key="lbl.Choices" /></td>
              <td align="right">
                <html:button property="btnPreview" styleClass="buttons" onclick="preview_onclick();"><bean:message key="btn.Preview"/></html:button>
              </td>
            </tr>
            <tr>
              <td class="borderClrLvl_2" width="35%" height="120px" valign="top"  bgcolor="#FFFFFF" align="center">
              <div  style="overflow:auto; width:100%;height:120px;">
                <table width="100%" border="0" cellpadding="0" cellspacing="1">
                  <tr>
                    <td width="20px" ><div class="imgStyles"></div></td>
                    <td id="selectedObjThemes" class="tdSelected"  style="cursor:default" onClick="tdClickThemes(this); MM_showHideLayers('styleDrop','','show','fontDrop','','hide','backgroundDrop','','hide');">
                    &nbsp;<bean:message key="lbl.Styles" /></td>
                  </tr>
                  <tr>
                    <td><div class="imgFontSelect"></div></td>
                    <td class="tdUnselected"  style="cursor:default" onClick="tdClickThemes(this); MM_showHideLayers('styleDrop','','hide','fontDrop','','show','backgroundDrop','','hide');">
                    &nbsp;<bean:message key="lbl.Fonts" /></td>
                  </tr>
                  <tr>
                    <td><div class="imgBackgroundSelect"></div></td>
                    <td class="tdUnselected"  style="cursor:default" onClick="tdClickThemes(this); MM_showHideLayers('styleDrop','','hide','fontDrop','','hide','backgroundDrop','','show');">
                    &nbsp;<bean:message key="lbl.Backgrounds" /></td>
                  </tr>
                </table>
              </div>
                <!-- themeElements table ends-->
              </td>
			  
              <!-- This is 2nd td related to choice -->
              <td colspan="2" class="borderClrLvl_2" width="65%"  valign="top"  bgcolor="#FFFFFF" align="center">
                <div id="styleDrop" style="display:'';height:120px;width:100% ">
                  <div id="styleDropInner" style="margin-left:1px;display:''; overflow:auto; height:120px;float:left;width:49% ">
                    <table id="styles" width="100%"  border="0" cellpadding="0" cellspacing="1" align="left">
                    <logic:iterate name="styles" id="style">
                      <tr>
                        <td width="20px"><div class="imgStyle<bean:write name="style" property="name" />"></div></td>
                        <td id="<bean:write name="style" property="name" />" class="tdUnSelected" style="cursor:default" onClick="tdClickStyles(this);">&nbsp;<bean:write name="style" property="value" /></td>
                      </tr>
                      </logic:iterate>
                    </table>
                    <!-- styles table ends -->
                  </div>
                  <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
                  <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
                  <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
                  <div id="colorDropInner"  style="margin-left:1px;display:''; overflow:auto; height:120px;width:49%;float:left;"></div>
                </div>
                  <logic:iterate name="colorSchemes" id="styles">
                    <div id="colorDrop<bean:write name="styles" property="name" />" style="display:none;">
                      <table id="colors<bean:write name="styles" property="name" />" width="100%"  border="0" cellpadding="0" cellspacing="1" align="left">
                       <logic:iterate name="styles" property="list" id="colorScheme">
                        <tr>
                          <td width="20px"><div class="imgColorscheme<bean:write name="colorScheme" property="name" />"></div></td>
                          <td id="<bean:write name="colorScheme" property="name" />" class="tdUnSelected" style="cursor:default" onClick="tdClickColorSchemes(this);">&nbsp;<bean:write name="colorScheme" property="value" /></td>
                          <input type="hidden" id="cs4<bean:write name="styles" property="name" />" name="cs4<bean:write name="styles" property="name" />" value="<bean:write name="colorScheme" property="name" />" />
                          <logic:iterate name="colorScheme" property="list" id="properties">
                            <input type="hidden" id="prop4<bean:write name="styles" property="name" /><bean:write name="colorScheme" property="name" /><bean:write name="properties" property="name" />" name="prop4<bean:write name="styles" property="name" /><bean:write name="colorScheme" property="name" /><bean:write name="properties" property="name" />" value="<bean:write name="properties" property="value" />" />
                          </logic:iterate>
                        </tr>
                        </logic:iterate>
                      </table>
                    </div>
                  </logic:iterate> 
                </div>
                <div id="fontDrop" style="display:none; overflow:auto; height:120px;">
                  <table id="fonts" width="100%" height="100%"  border="0" align="left">
                    <tr>
                      <th colspan="4" align="center"><bean:message key="tbl.head.SelectFontwithColor" /></th>                      
                    </tr>
                    <tr>
                      <td width="46%"><div align="right"><bean:message key="lbl.Headings" /></div></td>
                      <td width="28%">
                        <html:select  name="themeNewEditForm" style="width:100%" property="cboHeadings" styleClass="borderClrLvl_2 componentStyle">
                         <html:options collection="fonts" property="name" labelProperty="value" />   
                       </html:select>
                      </td>
                      <td width="26%"  >
                        <table height="18px" width="18px" id="colorPickerHeadings" class="borderClrLvl_2"  onclick="showColorPicker(colorPickerHeadingsObj,document.forms[0].hdnFontColorHeadings);"  style="cursor:hand;" title='<bean:message key="tooltips.Click2SelectFontColor" />'>
                        <tr><td >&nbsp;</td></tr>
                        </table>
                      </td>

                    </tr>
                    <tr>
                      <td><div align="right"><bean:message key="lbl.BodyText" /></div></td>
                      <td>
                        <html:select  name="themeNewEditForm" style="width:100%" property="cboBodyText" styleClass="borderClrLvl_2 componentStyle">
                         <html:options collection="fonts" property="name" labelProperty="value" />   
                        </html:select>
                      </td>
                      <td>
                        <table height="18px" width="18px" id="colorPickerBodyText"  class="borderClrLvl_2" onclick="showColorPicker(colorPickerBodyTextObj,document.forms[0].hdnFontColorBodyText);" style="cursor:hand;" title='<bean:message key="tooltips.Click2SelectFontColor" />'>
                        <tr><td >&nbsp;</td></tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td><div align="right"><bean:message key="lbl.BoldText" /></div></td>
                      <td>
                        <html:select  name="themeNewEditForm" style="width:100%" property="cboMenuText"  styleClass="borderClrLvl_2 componentStyle">
                         <html:options collection="fonts" property="name" labelProperty="value" />   
                        </html:select>
                      </td>
                      <td>
                        <table height="18px" width="18px" id="colorPickerMenuText"  class="borderClrLvl_2" onclick="showColorPicker(colorPickerMenuTextObj,document.forms[0].hdnFontColorMenuText);" style="cursor:hand;" title='<bean:message key="tooltips.Click2SelectFontColor" />'>
                        <tr><td >&nbsp;</td></tr>
                        </table>
                      </td>
                      <td >&nbsp;</td>
                    </tr>
                    <tr>
                      <td><div align="right"><bean:message key="lbl.ElementText" /></div></td>
                      <td>
                        <html:select name="themeNewEditForm" style="width:100%" property="cboElementText"  styleClass="borderClrLvl_2 componentStyle">
                         <html:options collection="fonts" property="name" labelProperty="value" />   
                        </html:select>
                      </td>
                      <td >
                        <table height="18px" width="18px" id="colorPickerElementText"  class="borderClrLvl_2" onclick="showColorPicker(colorPickerElementTextObj,document.forms[0].hdnFontColorElementText);" style="cursor:hand;float:left" title='<bean:message key="tooltips.Click2SelectFontColor" />'>
                          <tr><td >&nbsp;</td></tr>
                        </table>

                        <table height="18px" width="18px" id="colorPickerElementBg" class="borderClrLvl_2" onclick="showColorPicker(colorPickerElementBgObj,document.forms[0].hdnFontColorElementBg);" style="margin-left:3px;cursor:hand;float:left" title='<bean:message key="tooltips.Click2SelectBgColor" />'>
                          <tr><td >&nbsp;</td></tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                  <!-- fonts table ends-->
                </div>
                <div id="backgroundDrop" style="display:none; height:120px;">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="center">
                      <table  border="0" width="100%">
                        <tr>
                          <th  align="center" valign="top">
                            <bean:message key="lbl.SelectABackground" />&nbsp;&nbsp;&nbsp;[
                            <html:radio  name="themeNewEditForm" property="radBackground" value="color" onclick="selectBackground();" />
                            <bean:message key="rad.Background.Color" />&nbsp;&nbsp;&nbsp;/
                            <html:radio  name="themeNewEditForm" property="radBackground" value="imgae" onclick="selectBackground();" />
                            <bean:message key="rad.Background.Image" />&nbsp;&nbsp;&nbsp;]
                          </th>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="center">
                      <table  border="0" width="100%">
                        <tr><td align="center">
                        <div id="backgroundColorDrop" style="display:'';">
                        <script>bgcolorPicker.renderPalette();</script>
                        </div>
                        <div id="backgroundImageDrop"  style="height:85px;width:100% display:none;">
                        <iframe src="theme_bg_imgs.jsp" frameborder="0" style="height:85px; width:100%">
                        </iframe>
                        </div>
                        </td></tr>
                      </table>
                    </td>
                  </tr>
                </table>

                </div>
              </td>
            </tr>
            
            <!--This is the 3rd tr of table ... contains the preview divs -->
            <tr>
              <td class="borderClrLvl_2" colspan="3" height="220px" align="center" valign="top"  bgcolor="#FFFFFF">
                <!--preview_image iframe starts -->
                <iframe src="theme_preview_image.jsp" frameborder="0" style="margin-top:5px; display:'';height:100%; width:100%">
                </iframe>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="680px" height="30px">
          <div align="right">
            <bean:message key="txt.ThemeName" />
            <html:text name="themeNewEditForm" property="txtThemeName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" maxlength="20" />
            <html:button property="btnSave" styleClass="buttons" style="width:70px" onclick="return callPage()" ><bean:message key="btn.Save" /></html:button>
            <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="history.go(-1);"><bean:message key="btn.Cancel" /></html:button>
          </div>
        </td>
        <td width="20px">&nbsp;</td>
      </tr>
    </table>
    <!-- borderClrLvl_2 table ends above-->
    </td>
    </tr>
      <tr><td height="2px"></td></tr>
      <tr>
        <td align="center">
          <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="700px" height="20px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
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
                  <bean:write  name="msg"/>
                </html:messages>
              </td>
              </tr>
          </table>
          <!-- statusBar table ends above-->
        </td>
      </tr>
</table>
<!-- tabContainer table ends -->
<!-- Color Picker Layer -->
<div id="ColorPicker"  style="position:absolute;display:none;" >
  <table id="ColorPickertabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <!-- This table contains 1 tr with 1 td containing tables, 'ColorPickertabParent' and 'ColorPickerblueBorder' -->
  <tr>
  <td align="center">
    <table id="ColorPickertabParent" width="350px"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
        <table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.ColorPicker" /></div></td>
              <td width="5px" class="imgTabRight"></td>
            </tr>
          </table>
      </td>
        </tr>
    </table>
    <table id="ColorPickerblueBorder" width="350px" border="0" cellpadding="0" cellspacing="0"  class="imgData borderClrLvl_2 bgClrLvl_4">
          <tr>
      <td colspan="2" align="center"><br>
        <table id="colorGenerator" width="301px" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="301" height="25px">
            <bean:message key="lbl.SelectaColor" />
            </td>
          </tr>
          <tr>
            <td align="center">
            <div id="color"  style="width:300px; display:'';">
              <!--Script below generates a dynamic color table of 216 web-safe colors -->
              <SCRIPT language=JavaScript>
              colorPicker.renderPalette();
              </SCRIPT>	
            </div>
            </td>
          </tr>
        </table>
      </td>
      </tr>
      <tr>
          <td height="10px" colspan="2" ></td>
        </tr>
          <tr>
          <td class="bgClrLvl_2" height="1px"  colspan="2" align="center"></td>
          </tr>
          <tr>
            <td width="320px" height="30px" align="center"><div align="right">
              <html:button property="btnOkColorPicker" styleClass="buttons" style="width:70px" onclick="btnOkColorPicker_onclick();" ><bean:message key="btn.Ok" /></html:button>
              <html:button property="btnCancelColorPicker" styleClass="buttons" style="width:70px" onclick="btnCancelColorPicker_onclick();" ><bean:message key="btn.Cancel" /></html:button>
            </div></td>
          <td width="30px" align="center">&nbsp;</td>
          </tr>
		
  </table>
  <!-- ColorPickerblueBorder table ends above-->
  </td>
  </tr>
  </table>
  <!-- ColorPickertabContainer table ends above-->
<div>
</html:form>
</body>
</html:html>