<%@ Page Language="C#" AutoEventWireup="true"  EnableViewStateMac="false" CodeFile="OrderQuery.aspx.cs" Inherits="MCGASPDOTNET.OrderQuery" %>

<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		<title>订单查询结果</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>

	<body>
    <form id="form1" runat="server">
    <br/>
			<center>
				<span class="title"><b>订单查询结果</b> </span>
				<br/>
				<br/>
            <br />
            <%
              if (!String.IsNullOrEmpty(PerrMsg.Text))
              {  
             %>
            <table id="errorTable" class="api" runat="server">
            <tr>
               <td class="field">错误消息</td>
               <td><asp:TextBox ID="PerrMsg" runat="server" Width="290px"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">返回码</td>
               <td><asp:TextBox ID="PreturnCode" runat="server" ReadOnly="True" Width="290px"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">返回消息</td>
               <td><asp:TextBox ID="Pmessage" runat="server" Width="290px"></asp:TextBox></td>
            </tr>
         
            </table>
            <%
                }
                else
                {
                
            %>
            <table id="ResTable" class="api" runat="server">
            
            <tr>
               <td class="field">订单金额</td>
               <td><asp:TextBox ID="Pamount" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">金额明细</td>
               <td><asp:TextBox ID="PamtItem" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">支付银行</td>
               <td><asp:TextBox ID="PbankAbbr" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">用户手机号码</td>
               <td><asp:TextBox ID="Pmobile" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">商户订单号</td>
               <td><asp:TextBox ID="PorderId" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
             <tr>
               <td class="field">支付日期</td>
               <td><asp:TextBox ID="PpayDate" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            
             <tr>
               <td class="field">保留字段1</td>
               <td><asp:TextBox ID="Preserved1" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
             <tr>
               <td class="field">保留字段2</td>
               <td><asp:TextBox ID="Preserved2" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
             <tr>
               <td class="field">支付结果</td>
               <td><asp:TextBox ID="Pstatus" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            
            <tr>
               <td class="field">订单提交日期</td>
               <td><asp:TextBox ID="PorderDate" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">费用</td>
               <td><asp:TextBox ID="Pfee" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            </table>
            <%
                }    
            %>
            <br />
             </center>
             <a id="HomeLink" class="home" href="Index.aspx">首页</a>
    </form>
</body>
</html>
