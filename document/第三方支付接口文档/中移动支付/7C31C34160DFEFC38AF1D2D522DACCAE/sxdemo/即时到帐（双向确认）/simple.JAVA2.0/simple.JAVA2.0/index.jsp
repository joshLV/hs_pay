
<%@ page language="java" import="java.net.*,java.util.*,java.text.*"
	pageEncoding="gbk"%>
<%@ include file="globalParam.jsp" %>

<html>
	<head>
		<title>商户联机交易演示</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<center>
			<h4>
				Welcome to the CMPAY <font color="red">JSP</font> SDK Simple Edition Main Page
			</h4>
			<table>
				<tr>
					<th>
						<p>
							<h4>
								商户[<%=merchantId%>]
								<br/>联机交易演示
							</h4>
						</p>
						<hr>
					</th>
				</tr>
				<tr>
					<td>
						<a href="dodirectpayment_token_input.jsp">即时到账(双向确认)</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="dogwdirectpayment_token_input.jsp">直接支付(银行网关)</a>
					</td>
				</tr>
				<tr>
 					<td>
 						<a href="dowapdirectpayment_token_input.jsp">直接支付WAP(双向确认)</a>
 					</td>
				</tr>
			   <tr>
               <td>
                  <a href="dodirectpayment_sms_input.jsp">即时到账(短信)</a>
               </td>
            </tr>
				<tr>
					<td>
						<a href="ordersearch_input.jsp">订单查询</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="orderrefund_input.jsp">退款</a>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>







