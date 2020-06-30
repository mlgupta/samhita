<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="dms.web.beans.filesystem.*" %>
<bean:define id="jsFileLinks" name="Treeview4Select" property="jsFileLinks" />
<bean:define id="jsFileName" name="Treeview4Select" property="jsFileName" />

<%
String currentFolderDocId4Select  = null;
boolean isDocument=false;
String heading="";

if (session.getAttribute("currentFolderDocId4Select")!=null){
  currentFolderDocId4Select =((Long) session.getAttribute("currentFolderDocId4Select")).toString();
} 
if (request.getAttribute("isDocument")!=null){
  isDocument =((Boolean)request.getAttribute("isDocument")).booleanValue();
}
if (session.getAttribute("heading")!=null){
  heading =(String)session.getAttribute("heading");
}

%>
<html:html>
<head>

<title><bean:message key="title.FoldersDocuments" /></title>
<jsp:include page="/style_sheet_include.jsp" />
<!-- Begin (Tree View Related js Files) -->
<script src="useragent.js"></script>
<script src="treeview_select.js"></script>
<script src="general.js"></script>
<script src="<%=jsFileName%>"></script>
<%=jsFileLinks%>
<!-- End (Tree View Related js Files)  -->
<script>
function window_onload(){
 document.forms[0].btnSelect.disabled=!((eval(document.forms[0].hdnFoldersOnly.value))  && !eval("<%=isDocument%>"))
}
function goTo(){
    document.forms[0].action="folderDocSelectGoAction.do";
    document.forms[0].submit();
}

//Called when new button is clicked
function folderNew() { 
    openWindow("","NewFolder1",150,530,0,0,true);
    document.forms[0].target = "NewFolder1";
    document.forms[0].action = "b4FolderDocSelectNewFolderAction.do";
    document.forms[0].submit();
}

/*
function folderNew() { 
    var folderName;
    folderName = prompt('<bean:message key="lbl.Folder" />');
    if (folderName != null ){
        document.forms[0].hdnFolderName.value = folderName
        document.forms[0].action = "folderDocSelectNewFolderAction.do";
        document.forms[0].submit();
    }
}  
*/

function selectOnclick(){
  var controlName=document.forms[0].hdnOpenerControl.value;
  var control=null;
  
  if(controlName!=""){
    control=eval("opener.document.forms[0]." +controlName);
    if (typeof control!="undefined"){
      if((control.type=="select-multiple")||(control.type=="select")){
        for(var openerIndex=0;openerIndex<control.options.length; openerIndex++){
          if(control.options[openerIndex].value==document.forms[0].hdnFolderDocument.value){                  
            window.close();
            return false;
          }       
        }
        var opt=opener.document.createElement("OPTION");
        opt.text=document.forms[0].hdnFolderDocument.value;
        opt.value=document.forms[0].hdnFolderDocument.value;
        var newIndex=control.options.length ;
        control.options[newIndex]=opt;
      }else{
        control.value=document.forms[0].hdnFolderDocument.value;
        if( control.value.length == 0 ){
          control.value="/";  
        }
        if (typeof opener.copyMoveFolderDocTo!="undefined"){
          if ((opener.document.forms[0].hdnActionType.value == <%=FolderOperation.COPY%>)||(opener.document.forms[0].hdnActionType.value == <%=FolderOperation.MOVE%>)) {
            opener.copyMoveFolderDocTo();
          }
        }
      }
      window.close();
      return true;
    }
  }
}
</script>
</head>
<body style="margin:15px" onload="window_onload();">
<html:form action="/folderDocSelectNewFolderAction" >
<html:hidden name="folderDocSelectForm" property="hdnFoldersOnly"/>
<html:hidden name="folderDocSelectForm" property="hdnOpenerControl"/>
<html:hidden name="folderDocSelectForm" property="hdnFolderName"/>
<html:hidden name="folderDocSelectForm" property="hdnFolderDocument"/>

<!-- This page contains 2 outermost tables, named 'errorContainer' and 'tabContainer' -->
<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr>
  <td align="center">
    <table id="tabParent" width="350px"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
          <table id="tab" width="140px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="130px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.FoldersDocuments" /></div></td>
              <td width="5px" class="imgTabRight"></td>            
            </tr>
          </table>
        </td>
  		</tr>
    </table>
    <table id="borderClrLvl_2" width="350px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2"  >
        <tr>
          <td >
          <table width="90%"  border="0" cellpadding="0" cellspacing="0" align="center">
            <tr>
              <td height="10px"></td>
            </tr>
            <tr>
              <td class="tabText"><%=heading%></td>
            </tr>
            <tr>
              <td height="10px"></td>
            </tr>
            <tr>
              <td >
                <span style="float:left"><bean:message key="lbl.Path" /> <html:text property="folderDocument"  name="folderDocSelectForm" styleClass="borderClrLvl_2 componentStyle " style="width:255px;" /></span>
                <a onclick="goTo();" class="imgGo" title="<bean:message key="btn.Go" />" ></a>
              </td>
            </tr>
            <tr>
              <td height="10px"></td>
            </tr>

            <tr>
              <td >
                <div id="treeview" align="center" class="borderClrLvl_2" style="overflow:auto;height:325px;width:320px;">
                  <!-- Begin Tree View Generation-->    
                  <div style="width:95%;" >
                  <script>                      
                      openerControl=document.forms[0].hdnOpenerControl.value;
                      foldersOnly=document.forms[0].hdnFoldersOnly.value ;
                      SetCookie("highlightedTreeviewLink", "<%=currentFolderDocId4Select %>");
                      initializeDocument();
                      <%if(currentFolderDocId4Select !=null){ %>
                        getElById("treeview").scrollTop=findObj("<%=currentFolderDocId4Select %>").navObj.offsetTop;
                      <%}%>                  
                  </script>
                  </div>
                  <!-- End Tree View Generation-->
                </div>
              </td>
            </tr>
            <tr>
              <td height="10px"></td>
            </tr>
            <tr>
              <td align="right">
                <html:button property="btnSelect"  onclick="selectOnclick();" styleClass="buttons" style="width:70px" ><bean:message key="btn.Select" /></html:button>                
                <html:button property="btnCancel" onclick="window.close();" styleClass="buttons" style="width:70px" ><bean:message key="btn.Cancel" /></html:button>
                <% if(isDocument){ %>
                  <html:button property="btnNewFolder" onclick="folderNew();" styleClass="buttons" style="width:70px" disabled="true"  >New Folder</html:button>
                <%}else{%>
                  <html:button property="btnNewFolder" onclick="folderNew();" styleClass="buttons" style="width:70px" >New Folder</html:button>
                <%}%>
                <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=folder_doc_copy_move_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>                                                            
              </td>
            </tr>
            <tr>
              <td height="10px"></td>
            </tr>
          </table>
        </td>
    </tr>
  </table>
  <!-- borderClrLvl_2 table ends above-->
</td>
</tr>
</table>
<!-- tabContainer table ends above-->
<table cellpadding="0" cellspacing="0" border="0">
<tr>
<td height="3px">
</td>
</tr>
</table>
<table align="center" class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="350px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
    <tr>
    <td><div class="imgStatusMsg"></div></td>
    <td>
    <logic:messagesPresent>
      <html:messages id="actionMessage" message="true">
          <bean:write  name="actionMessage"/>
      </html:messages>
          
      <html:messages id="actionError">
          <font color="red"> <bean:write  name="actionError"/></font>
      </html:messages>
    </logic:messagesPresent>
    </td>
    </tr>
</table>
<!-- statusBar table ends above-->

</html:form>
</body>

</html:html>