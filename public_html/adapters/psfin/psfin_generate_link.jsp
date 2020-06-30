<%@ page import="dms.web.beans.user.*" %>
<%@ page import="dms.web.beans.filesystem.*" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences" />
<bean:define id="currentFolderPath" name="FolderDocInfo" property="currentFolderPath" type="String" />
<bean:define id="currentFolderId" name="FolderDocInfo" property="currentFolderId" />

<html:html>
<head>
<title><bean:message key="title.doc.generate.url" /></title>
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js"></script>
<script language="javascript" type="text/javascript">
<!--
function getElem(cntrl,thisForm,copyThis){
  var browserName=navigator.appName; 
  if (browserName=="Microsoft Internet Explorer"){
    var index = cntrl.indexOf('[')+1;
    var textLinkValObj = null;
    if(copyThis == 1){
      textLinkValObj = "txtDocId["+cntrl.charAt(index)+"]";
    }else{
      textLinkValObj = "txtLinkGenerated["+cntrl.charAt(index)+"]";
    }
    var meintext = document.getElementById(textLinkValObj).value;
    copy_clip(meintext);
  }else{
    alert("ClipBoard feature is only available with Internet Explorer")
  }
  return;

}
function copy_clip(meintext){

 if (window.clipboardData)
   {

   // the IE-manier
   window.clipboardData.setData("Text", meintext);

   // waarschijnlijk niet de beste manier om Moz/NS te detecteren;
   // het is mij echter onbekend vanaf welke versie dit precies werkt:
   }
   else if (window.netscape)
   {

   // dit is belangrijk maar staat nergens duidelijk vermeld:
   // you have to sign the code to enable this, or see notes below
   netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');

   // maak een interface naar het clipboard
   var clip = Components.classes['@mozilla.org/widget/clipboard;1']
                 .createInstance(Components.interfaces.nsIClipboard);
   if (!clip) return;

   // maak een transferable
   var trans = Components.classes['@mozilla.org/widget/transferable;1']
                  .createInstance(Components.interfaces.nsITransferable);
   if (!trans) return;

   // specificeer wat voor soort data we op willen halen; text in dit geval
   trans.addDataFlavor('text/unicode');

   // om de data uit de transferable te halen hebben we 2 nieuwe objecten
   // nodig om het in op te slaan
   var str = new Object();
   var len = new Object();

   var str = Components.classes["@mozilla.org/supports-string;1"]
                .createInstance(Components.interfaces.nsISupportsString);

   var copytext=meintext;

   str.data=copytext;

   trans.setTransferData("text/unicode",str,copytext.length*2);

   var clipid=Components.interfaces.nsIClipboard;

   if (!clip) return false;

   clip.setData(trans,null,clipid.kGlobalClipboard);

   }
   //alert("Following info was copied to your clipboard:\n\n" + meintext);
   return false;
}
//-->
</script>

<script>
  function closewindow(){
    window.close();
  }
</script>
</head>

<body style="margin:7px">
<html:form action="/docDoNothingAction" name="showContentForm" type="dms.web.actionforms.filesystem.ShowContentForm" >
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
              <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.Links" /></div></td>
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
          <div style="overflow:auto;width:94%;height:140px" class="bgClrLvl_4 borderClrLvl_2">
            <table width="97%"  style="margin:3px" align="left" border="0"  cellpadding="0" cellspacing="0" id="innerTabContainer">
              <tr><td height="3px" class="bgClrLvl_4" colspan="3" ></td></tr>
              <logic:iterate indexId="index" id="folderDocList" name="DocLinkLists" type="dms.web.beans.filesystem.FolderDocList" > 
              <bean:define id="txtLinkGenerated" name="folderDocList" property="txtLinkGenerated" />
              <bean:define id="txtDocId" name="folderDocList" property="id" />
              <tr class="bgClrLvl_3" >            
                <td align="right" width="20%" nowrap="nowrap" height="22px">
                  <bean:message key="lbl.DocumentName" />
                </td> 
                <td width="35%" align="left">
                  <a target="viewContent" class="menu" style="margin-left:2px;" href="<%=txtLinkGenerated%>"/><bean:write name="folderDocList" property="name" /></a>
                </td>
                <td width="24%" align="right" nowrap="nowrap">
                  <bean:message key="lbl.DocumentId" />
                </td>
                <td width="21%" align="left">
                  <input type="TEXT" id="txtDocId[<%=index%>]" name="txtDocId[<%=index%>]" class="borderClrLvl_2 componentStyle bgClrLvl_3 " style="width:60px; margin-left:2px;" value="<%=txtDocId%>" readonly="true" >
                  <html:button indexed="true" property="btnOK" onclick="JavaScript:getElem(this.name,this.form,1);" styleClass="buttons" style="width:35px;"><bean:message key="btn.Copy" /></html:button> 
                </td>
              </tr>
              <tr class="bgClrLvl_3" >
                <td align="right" height="22px">
                  <bean:message key="lbl.Link"/>
                </td>
                <td colspan="3">
                  <input type="TEXT" id="txtLinkGenerated[<%=index%>]" name="txtLinkGenerated[<%=index%>]" class="borderClrLvl_2 componentStyle bgClrLvl_3 " style="width:410px; margin-left:2px;" value="<%=txtLinkGenerated%>" readonly="true" >
                  <html:button indexed="true" property="btnOK" onclick="JavaScript:getElem(this.name,this.form,2);" styleClass="buttons" style="width:35px;"><bean:message key="btn.Copy" /></html:button> 
                </td>
              </tr>
              <tr><td height="3px" class="bgClrLvl_4" ></tr>
              </logic:iterate>
              <tr><td height="5px" class="bgClrLvl_4" colspan="2"></td></tr>
            </table>
          </div>
        </td>
      </tr>
      <tr>
        <td height="30px" width="68%">&nbsp;</td>
        <td align="right" height="30px" width="32%" >
          <html:button property="btnOK" onclick="closewindow()" styleClass="buttons" style="width:70px"><bean:message key="btn.Ok" /></html:button>
          <html:button property="btnHelp" onclick="openWindow('help?topic=document_generate_links_html','Help',650,800,0,0,true);" styleClass="buttons" style="width:70px;margin-right:20px"><bean:message  key="btn.Help" /></html:button>
        </td>
      </tr>
    </table>
  </td>
</tr>
</table>
</html:form>
</body>
</html:html>