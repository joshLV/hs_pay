<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Index.aspx.cs" Inherits="MCGASPDOTNET.Index" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>商户联机交易演示</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>

	<body>
	    
		<center>
		
			<h4>
				Welcome to the CMPAY <span style="color:Red;">ASP.net</span> SDK Samples Main Page
			</h4>
			<table>
				<tr>
					<th>
						
							<h4>
								商户<asp:label ID="label1" runat="server"/>联机交易演示
							</h4>
						
						<hr/>
					</th>
				</tr>
				<tr>
					<td align="center">
							商户号:
							<input type="text" id="merchantId1" runat="server" />
						
					</td>
				</tr>
                				<tr>
					<td>
						<a href="DoGWDirectPaymentInput.aspx">即时到帐（银行网关）</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="DoDirectPaymentConfirmInput.aspx">即时到帐(双向确认)</a>
					</td>
				</tr>
                			<tr>
					<td>
						<a href="DoWAPDirectPaymentConfirmInput.aspx">即时到帐(WAP)</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="DoDirectPaymentOfflineInput.aspx">即时到帐（短信）</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="OrderQueryInput.aspx">订单查询</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="OrderRefundInput.aspx">退款</a>
					</td>
				</tr>
				
			</table>
		</center>
	</body>
</html>
