<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Index.aspx.cs" Inherits="MCGASPDOTNET.Index" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>�̻�����������ʾ</title>
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
								�̻�<asp:label ID="label1" runat="server"/>����������ʾ
							</h4>
						
						<hr/>
					</th>
				</tr>
				<tr>
					<td align="center">
							�̻���:
							<input type="text" id="merchantId1" runat="server" />
						
					</td>
				</tr>
                				<tr>
					<td>
						<a href="DoGWDirectPaymentInput.aspx">��ʱ���ʣ��������أ�</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="DoDirectPaymentConfirmInput.aspx">��ʱ����(˫��ȷ��)</a>
					</td>
				</tr>
                			<tr>
					<td>
						<a href="DoWAPDirectPaymentConfirmInput.aspx">��ʱ����(WAP)</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="DoDirectPaymentOfflineInput.aspx">��ʱ���ʣ����ţ�</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="OrderQueryInput.aspx">������ѯ</a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="OrderRefundInput.aspx">�˿�</a>
					</td>
				</tr>
				
			</table>
		</center>
	</body>
</html>
