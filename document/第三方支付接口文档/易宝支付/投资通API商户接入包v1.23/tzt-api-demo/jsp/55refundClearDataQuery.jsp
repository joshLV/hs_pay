<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" import="java.util.HashMap" import="com.yeepay.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%! String formatString(String text){
		return text==null ? "" : text.trim();
	}
%>
<%
	request.setCharacterEncoding("UTF-8");

	String startdate              	= formatString(request.getParameter("startdate"));
	String enddate	              	= formatString(request.getParameter("enddate"));

	String sysPath 				  	= request.getRealPath("");	//获得当前路径

	Map<String, String> result	  	= TZTService.getPathOfRefundClearData(startdate, enddate, sysPath);
	String filePath					= result.get("filePath");
	String error_code				= result.get("error_code");
	String error					= result.get("error");
	String customError				= formatString(result.get("customError"));

	if(!"".equals(error_code)) {
		out.println("error_code : " + error_code);
		out.println("<br>error : " + error);
		return;
	} else if(!"".equals(customError)) {
		out.println("customError : " + customError);
		return;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款对账文件下载</title>
</head>
	<body>
		<br /> <br />
		<table width="70%" border="0" align="center" cellpadding="5" cellspacing="0" 
							style="word-break:break-all; border:solid 1px #107929">
			<tr>
		  		<th align="center" height="30" colspan="5" bgcolor="#6BBE18">
					<%=startdate%> —— <%=enddate%> ：退款对账文件
				</th>
		  	</tr>
			
			<tr>
				<td width="15%" align="left">&nbsp;</td>
				<td width="5%"  align="center">&nbsp;</td> 
				<td width="80%" align="left">&nbsp;</td>
			</tr>

			<tr>
				<td width="20%" align="left">&nbsp;文件路径</td>
				<td width="5%"  align="center"> : </td> 
				<td width="75%" align="left"> 
						<%=filePath%> 
				</td>
			</tr>

			<tr>
				<td width="15%" align="left">&nbsp;</td>
				<td width="5%"  align="center">&nbsp;</td> 
				<td width="80%" align="left">&nbsp;</td>
			</tr>

		</table>

	</body>
</html>
