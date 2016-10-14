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

	String orderid              = formatString(request.getParameter("orderid"));

	Map<String, String> result	= TZTService.payapiQueryByOrderid(orderid);
	String merchantaccount		= formatString(result.get("merchantaccount"));
	String orderidFromYeepay   	= formatString(result.get("orderid"));
	String yborderid			= formatString(result.get("yborderid"));
	String amount           	= formatString(result.get("amount"));
	String bindid         		= formatString(result.get("bindid"));
	String bindvalidthru        = formatString(result.get("bindvalidthru"));
	String bank        			= formatString(result.get("bank"));
	String bankcode     		= formatString(result.get("bankcode"));
	String closetime     		= formatString(result.get("closetime"));
	String bankcardtype        	= formatString(result.get("bankcardtype"));
	String lastno        		= formatString(result.get("lastno"));
	String identityid          	= formatString(result.get("identityid"));
	String identitytype         = formatString(result.get("identitytype"));
	String status      			= formatString(result.get("status"));
	String errorcode       		= formatString(result.get("errorcode"));
	String errormsg       		= formatString(result.get("errormsg"));
	String sign             	= formatString(result.get("sign"));
	String customError        	= formatString(result.get("customError"));

	if(!"".equals(errorcode)) {
		out.println("errorcode : " + errorcode + "<br><br>");
		out.println("errormsg : " + errormsg + "<br><br>");
		out.println("orderid : " + orderid);
		return;
	} else if(!"".equals(customError)) {
		out.println("customError : " + customError);
		return;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>4.4 支付结果查询</title>
</head>
	<body>
		<br /> <br />
		<table width="70%" border="0" align="center" cellpadding="5" cellspacing="0" 
							style="word-break:break-all; border:solid 1px #107929">
			<tr>
		  		<th align="center" height="30" colspan="5" bgcolor="#6BBE18">
					4.4 支付结果查询
				</th>
		  	</tr>

			<tr>
				<td width="25%" align="left">&nbsp;商户编号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=merchantaccount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">merchantaccount</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;商户订单号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=orderidFromYeepay%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">orderid</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;易宝流水号</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=yborderid%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">yborderid</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;订单金额「分」</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=amount%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">amount</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;绑卡ID</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=bindid%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">bindid</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;绑卡有效期</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=bindvalidthru%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">bindvalidthru</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;银行信息</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=bank%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">bank</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;银行缩写</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=bankcode%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">bankcode</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;支付时间</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=closetime%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">closetime</td> 
			</tr>


			<tr>
				<td width="25%" align="left">&nbsp;银行卡类型</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=bankcardtype%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">bankcardtype</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;卡号后4 位</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=lastno%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">lastno</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;用户标识</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=identityid%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">identityid</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;用户标识类型</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=identitytype%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">identitytype</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;状态</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=status%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">status</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;错误码</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=errorcode%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">errorcode</td> 
			</tr>

			<tr>
				<td width="25%" align="left">&nbsp;错误信息</td>
				<td width="5%"  align="center"> : </td> 
				<td width="50%" align="left"> <%=errormsg%> </td>
				<td width="5%"  align="center"> - </td> 
				<td width="15%" align="left">errormsg</td> 
			</tr>

		</table>

	</body>
</html>
