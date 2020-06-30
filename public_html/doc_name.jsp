<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo" />
<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="docNewForm" name="docNewForm" type="dms.web.actionforms.filesystem.DocNewForm" />

<%
String docType= docNewForm.getDocType();
%>
<html>
<head>
<title><bean:message key="title.NewDocument" /> </title>
<script src="general.js" ></script>
<jsp:include page="/style_sheet_include.jsp" />
<script language="JavaScript" type="text/JavaScript">

//This is to submit the request for creating new folder


function enter(thisField,e){
  var i;
  i=handleEnter(thisField,e);
 	if (i==1) {
    return folderNew(thisField.form);
  }
}

function DocNew(thisForm, davPath, docType){

    //To create new word document
    if (docType=='doc'){
      docName=thisForm.docName.value;
      if ((docName !='Untitled1.doc') && (docName !="") &&  (docName !=null)){
        docName=trim(thisForm.docName.value);
        var davPath;
        if (docName.indexOf(".doc")!=-1){
          davPath=davPath + docName;
        }else{
          davPath=(davPath + docName + ".doc");
        }
        var WordObj;
        WordObj = new ActiveXObject("Word.Application");
        var wordDoc=  WordObj.Documents.Add();
        wordDoc.SaveAs(davPath);
        WordObj.Visible = true;
        window.location.replace("blank.html");        
        //folderRefresh();
      }else{
          alert("Enter name to create document");
      }
    }
    
    //To create new xls document
    if (docType=='xls'){
      docName=thisForm.docName.value;
      if ((docName !='Untitled1.xls') && (docName !="") &&  (docName !=null)){
        docName=trim(thisForm.docName.value);
        var davPath;
        if (docName.indexOf(".xls")!=-1){
          davPath=davPath + docName;
        }else{
          davPath=(davPath + docName + ".xls");
        }
        var ExcelObj;
        ExcelObj = new ActiveXObject("Excel.Application");
        var ExcelDoc=  ExcelObj.Workbooks.Add();
        ExcelDoc.SaveAs(davPath);
        ExcelObj.Visible = true;
        window.location.replace("blank.html");        
        //folderRefresh();
      }else{
        alert("Enter name to create document");
      }
    }
    
    //To create new power point document
    if (docType=='ppt'){
      docName=thisForm.docName.value;
      if ((docName !='Untitled1.ppt') && (docName !="") &&  (docName !=null)){
        docName=trim(thisForm.docName.value);
        var davPath;
        if (docName.indexOf(".ppt")!=-1){
          davPath=davPath + docName;
        }else{
          davPath=(davPath + docName + ".ppt");
        }
        var PowerPointObj;
        PowerPointObj = new ActiveXObject("PowerPoint.Application");
        var powerPointDoc=  PowerPointObj.Presentations.Add();
        powerPointDoc.SaveAs(davPath);
        PowerPointObj.Visible = true;
        window.location.replace("blank.html");        
        //folderRefresh();
      }else{
        alert("Enter name to create document");
      }
    }
    
    //To create new Text document
    if (docType=='txt'){
      docName=thisForm.docName.value;
      if ((docName !='Untitled1.txt') && (docName !="") &&  (docName !=null)){
        docName=trim(thisForm.docName.value);
        var davPath;
        if (docName.indexOf(".txt")!=-1){
          davPath=davPath + docName;
        }else{
          davPath=(davPath + docName + ".txt");
        }
        var WordObj;
        WordObj = new ActiveXObject("Word.Application");
        WordObj.Visible = true;
        WordObj.Documents.Open(davPath);
        window.location.replace("blank.html");
        //folderRefresh();
      }else{
        alert("Enter name to create document");
      }
    }
}
</script>
</head>
<body style="margin:15px">
<html:form name="docNewForm" action="b4DocNewAction"  type="dms.web.actionforms.filesystem.DocNewForm" focus="docName">
<table id="tabContainer" width="100%" border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr>
<td>
	<table id="tabParent" width="500px" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
    	<td>
        <table id="tab" width="140px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="130px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.DocNew" /></div></td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
    </tr>
	</table>
  <table id="borderClrLvl_2" align="center" width="500px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" >
    <tr>
      <td>
        <table width="97%" border="0" cellpadding="0" cellspacing="1" align="center" >
        <tr>
          <td height="17px" colspan="2" ></td>
        </tr>      
        <tr>
          <td height="5px" colspan="2" ></td>
        </tr>      
        <tr>
          <td valign="top">
            <div align="right"><bean:message key="lbl.DocNew" />:&nbsp
            </div>
          </td>
          <td> 
            <html:text property="docName" style="width:385px;left-margin:4px" styleClass="borderClrLvl_2 componentStyle" ></html:text>
            <input type="text" name="txtDummy" id="txtDummy" style="display:none">
          </td>
        </tr>
        <tr>
          <td colspan="2" height="30px" align="right">
            <input type="button" name="btnOk" value="<bean:message key="btn.Ok" />" onclick="DocNew(this.form, '<%=userInfo.getDavPath()%>' + '<%=folderDocInfo.getCurrentFolderPath()%>' + '/' ,'<%=docNewForm.getDocType()%>')" style="width:70px" class="buttons">
            <html:button property="btnCancel" styleClass="buttons"  onclick='window.close()' style="width:70px"><bean:message key="btn.Cancel" /></html:button>
            <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=document_new_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>
          </td>
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
</html:form>
</body>
</html>