<%@ Page Language="C#" AutoEventWireup="true" CodeFile="OrderRefundInput.aspx.cs" Inherits="MCGASPDOTNET.OrderRefundInput" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>退款</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>
<body>
		<form id="Form1" name="form1" runat="server" action="">
			<br/>
			<center>
				<input type="hidden" name="merCert" value=""/>
				<span class="title"><b>退款</b> </span>
				<br/>
				<br/>
				<table class="api">
				    <tr>
						<td class="field">
							退款金额
						</td>
						<td>
						    <asp:TextBox ID="amount" runat="server" ></asp:TextBox>
							<span class="remark">*</span>
							<asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server"  ControlToValidate="amount" Display="Dynamic">
                               <span class="remark">金额不能为空1</span>
                            </asp:RequiredFieldValidator>
                            <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server"  ControlToValidate="amount" ValidationExpression="^[1-9]\d*$" Display="Dynamic">
                               <span class="remark">金额以分为单位，只能为正整数1</span>
                            </asp:RegularExpressionValidator>
						</td>
					</tr>
					<tr>
						<td class="field">
							原订单号
						</td>
						<td>
						    <asp:TextBox ID="orderId" runat="server" ></asp:TextBox>
							<span class="remark">*</span>
							<asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server"  ControlToValidate="orderId" Display="Dynamic">
                               <span class="remark">订单号不能为空1</span>
                            </asp:RequiredFieldValidator>
						</td>
					</tr>
					<tr>
						<td class="field">
						</td>
						<td>
                            <asp:Button ID="Button1" runat="server"  Text="提交" OnClick="Button1_Click" />
						</td>
					</tr>
				</table>
			</center>
			<a id="HomeLink" class="home" href="Index.aspx">首页</a>
		</form>
	</body>
</html>
