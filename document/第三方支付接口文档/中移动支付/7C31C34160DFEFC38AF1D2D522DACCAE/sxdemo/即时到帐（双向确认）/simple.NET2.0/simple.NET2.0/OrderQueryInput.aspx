<%@ Page Language="C#" AutoEventWireup="true"   EnableViewStateMac="false" CodeFile="OrderQueryInput.aspx.cs" Inherits="MCGASPDOTNET.OrderQueryInput" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>������ѯ</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>

	<body>
		<form id="form1" runat="server" action="">
			<br/>
			<center>
				<input type="hidden" name="merCert" value=""/>
				<span class="title"><b>������ѯ</b> </span>
				<br/>
				<br/>
				<table class="api">
					<tr>
						<td class="field">
							������
						</td>
						<td>
						    <asp:TextBox ID="orderId" runat="server" ></asp:TextBox>
							<span class="remark">*</span>
							<asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server"  ControlToValidate="orderId" Display="Dynamic">
                               <span class="remark">�����Ų���Ϊ��</span>
                            </asp:RequiredFieldValidator>
						</td>
					</tr>
					<tr>
						<td class="field">
						</td>
						<td>
                            <asp:Button ID="Button1" runat="server"  Text="�ύ" OnClick="Button1_Click" />
						</td>
					</tr>
				</table>
			</center>
			<a id="HomeLink" class="home" href="Index.aspx">��ҳ</a>
		</form>
	</body>
</html>

