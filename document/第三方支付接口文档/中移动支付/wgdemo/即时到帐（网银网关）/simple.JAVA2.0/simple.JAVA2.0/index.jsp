
<%@ page language="java" import="java.net.*,java.util.*,java.text.*"
	pageEncoding="gbk"%>
<%@ include file="globalParam.jsp" %>

<html>
	<head>
		<title>�̻�����������ʾ</title>
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
								�̻�[<%=merchantId%>]
								<br/>����������ʾ
							</h4>
						</p>
						<hr>
					</th>
				</tr>
				<tr>
					<td>
						<a href="dodirectpayment_token_input.jsp">��ʱ����(˫��ȷ��)</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="dogwdirectpayment_token_input.jsp">ֱ��֧��(��������)</a>
					</td>
				</tr>
				<tr>
 					<td>
 						<a href="dowapdirectpayment_token_input.jsp">ֱ��֧��WAP(˫��ȷ��)</a>
 					</td>
				</tr>
			   <tr>
               <td>
                  <a href="dodirectpayment_sms_input.jsp">��ʱ����(����)</a>
               </td>
            </tr>
				<tr>
					<td>
						<a href="ordersearch_input.jsp">������ѯ</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="orderrefund_input.jsp">�˿�</a>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>







