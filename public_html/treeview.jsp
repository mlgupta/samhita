<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="currentFolderId" name="FolderDocInfo" property="currentFolderId" />
<bean:define id="jsFileLinks" name="Treeview" property="jsFileLinks" />
<bean:define id="jsFileName" name="FolderDocInfo" property="jsFileName" />

<html:html>
<head>
<jsp:include page="/style_sheet_include.jsp" />
<title><bean:message key="title.FolderDocList" /></title>
<script src="general.js"></script>
<!-- Begin (Tree View Related js Files) -->
<script src="useragent.js"></script>
<script src="treeview.js"></script>
<script src="<%=jsFileName%>"></script>
<%=jsFileLinks%>
<!-- End (Tree View Related js Files)  -->
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
  function window_onload(){   
    //To bring the Selected folder on the Top in the tree view
    var  currentFolderObj=findObj("<%=currentFolderId%>");
    if (currentFolderObj!=null){
      document.body.scrollTop=currentFolderObj.navObj.offsetTop;
    }
  }

  function userDefinedClickOnFolder(folderId){
    window.top.frames[1].location.replace("folderDocFolderClickAction.do?currentFolderId="+ folderId);
  }

</script>
</head>
<body style="margin-left:5px;" onload="window_onload();" class="imgData bgClrLvl_4" >
  <!--<table id="tabParentTree" width="220px" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="220px">
        <table id="foldersTab" width="220px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="210px" class="imgTabTile"><div align="left" class="tabText"><bean:message key="lbl.Folders" /></div></td>
            <td width="5px" class="imgTabRight"></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="borderClrLvl_2 imgData bgClrLvl_4" valign="top">-->
        <div id="treeview" align="center" style="position:relative;height:385px;width:205px;margin:0px;z-index:0">
          <!-- Begin Tree View Generation-->    
          <div style="width:100%;">
            <script>
              SetCookie("clickedFolder4List", "<%=currentFolderId%>");
              SetCookie("highlightedTreeviewLink", "<%=currentFolderId%>");
              initializeDocument();  
            </script>                  
          </div>
          <!-- End Tree View Generation-->
        </div>
      <!--</td>
    </tr>
  </table>-->
</body>
</html:html>