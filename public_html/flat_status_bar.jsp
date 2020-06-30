<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html>
  <head>
    <jsp:include page="/style_sheet_include.jsp" />
    <script src="navigationbar.js" ></script>
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
      navBar.setPageNumber(<%=folderDocInfo.getPageNumber()%>);
      navBar.setPageCount(<%=folderDocInfo.getPageCount()%>);
      navBar.onclick="folderDocNavigate()";
    
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
      
      //Called When Navigation bar buttons Clicked
      function folderDocNavigate(){
        window.top.frames[1].folderDocNavigate(navBar.getPageNumber());
      }
    </script>
    <title><bean:message key="title.StatusBar" /></title>
  </head>
  <body style="margin-left:5px;margin-top:0px;">
    <html:form action="statusBarAction" name="showContentForm" type="dms.web.actionforms.filesystem.ShowContentForm">
      <table class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="975px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
        <tr>
          <td height="20px" width="30px"><div class="imgStatusMsg"></div></td>
          <td width="752px">
            <html:messages id="actionMessage" message="true">
              <bean:write  name="actionMessage"/>
            </html:messages>
            <html:messages id="actionError">
              <font color="red"> <bean:write  name="actionError"/></font>
            </html:messages>
          </td>
          <td width="3px">
            <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
            <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
          </td>
          <td width="200px" align="right">
            <script>navBar.render();</script>
          </td>
        </tr>
      </table>
       <!-- statusBar table ends above-->
    </html:form>
  </body>
</html>
