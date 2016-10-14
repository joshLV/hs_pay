<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" import="java.util.HashMap" import="com.yeepay.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%! String formatString(String text){
		return text==null ? "" : text.trim();
	}
%>
<%
	String orderid              = formatString(request.getParameter("orderid"));
	String validatecode			= formatString(request.getParameter("validatecode"));

	Map<String, String> params	= new HashMap<String, String>();
	params.put("validatecode", validatecode);
    params.put("orderid", orderid);

	Map<String, String> result	= TZTService.smsConfirm(params);
	String merchantaccount		= formatString(result.get("merchantaccount"));
	String orderidFromYeepay	= formatString(result.get("orderid"));
	String yborderid			= formatString(result.get("yborderid"));
	String amount				= formatString(result.get("amount"));
	String sign					= formatString(result.get("sign"));
	String error_code			= formatString(result.get("error_code"));
	String error_msg			= formatString(result.get("error_msg"));
	String customError			= formatString(result.get("customError"));

	if(!"".equals(error_code)) {
		out.println("error_code : " + error_code);
		out.println("<br>error_msg : " + error_msg);
		return;
	} else if(!"".equals(customError)) {
		out.println("customError : " + customError);
		return;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>4.2.3 确认支付返回结果</title>
</head>
	<body>
		<br /> <br />
		<table width="80%" border="0" align="center" cellpadding="10" cellspacing="0" 
							style="word-break:break-all; border:solid 1px #107929">
			<tr>
		  		<th align="center" height="30" colspan="5" bgcolor="#6BBE18">
					4.2.3 确认支付返回结果
				</th>
		  	</tr>

			<tr>
				<td width="15%" align="left">&nbsp;商户编号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=merchantaccount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">merchantaccount</td> 
			</tr>

			<tr>
				<td width="15%" align="left">&nbsp;商户订单号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=orderidFromYeepay%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">orderid</td> 
			</tr>

			<tr>
				<td width="15%" align="left">&nbsp;易宝流水号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=yborderid%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">yborderid</td> 
			</tr>

			<tr>
				<td width="15%" align="left">&nbsp;交易金额</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=amount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="25%" align="left">amount</td> 
			</tr>
		</table>

	</body>
</html>
