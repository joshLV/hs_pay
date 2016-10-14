<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" import="java.util.HashMap" import="java.text.SimpleDateFormat" import="com.yeepay.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%! String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}
%>
<%
	Map<String, String> result	= TZTService.queryDrawValidAmount();
	String validamount			= formatString(result.get("validamount"));
	String merchantaccount		= formatString(result.get("merchantaccount"));
	String error_code			= formatString(result.get("error_code"));
	String error_msg			= formatString(result.get("error_msg"));
	String customError			= formatString(result.get("customError"));

	if(!"".equals(customError)) {
		out.println("customError : " + customError);
		return;
	} else if(!"".equals(error_code)) {
		out.println("error_code : " + error_code);
		out.println("<br> error_msg : " + error_msg);
		return;
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商户可用打款余额</title>
</head>
	<body>
		<br /> <br />
		<table width="70%" border="0" align="center" cellpadding="5" cellspacing="0" 
							style="word-break:break-all; border:solid 1px #107929">
			<tr>
		  		<th align="center" height="30" colspan="5" bgcolor="#6BBE18">
					商户可用打款余额
				</th>
		  	</tr>

			<tr>
				<td width="25%" align="right">&nbsp;商户编号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="40%" align="left"> <%=merchantaccount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">merchantaccount</td> 
			</tr>

			<tr>
				<td width="25%" align="right">&nbsp;可用打款余额</td>
				<td width="5%"  align="center"> : </td> 
				<td width="40%" align="left"> <%=validamount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">validamount</td> 
			</tr>

			<tr>
				<td width="25%" align="right">&nbsp;--</td>
				<td width="5%"  align="center"> : </td> 
				<td width="40%" align="left">测试商编关闭了出款功能，故为0.</td>
				<td width="5%"  align="center">&nbsp;</td> 
				<td width="25%" align="left">&nbsp;</td> 
			</tr>

		</table>

	</body>
</html>
