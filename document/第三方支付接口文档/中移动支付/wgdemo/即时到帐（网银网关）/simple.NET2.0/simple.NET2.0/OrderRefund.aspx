<%@ Page Language="C#" AutoEventWireup="true"  EnableViewStateMac="false" CodeFile="OrderRefund.aspx.cs" Inherits="MCGASPDOTNET.OrderRefund" %>

<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		<title>�����˿���</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>

	<body>
    <form id="form1" runat="server">
    <br/>
			<center>
				<span class="title"><b>�����˿���</b> </span>
				<br/>
				<br/>
            <br />
            <%
              if (!String.IsNullOrEmpty(PerrMsg.Text))
              {  
             %>
            <table id="errorTable" class="api" runat="server">
            <tr>
               <td class="field">������Ϣ</td>
               <td><asp:TextBox ID="PerrMsg" runat="server" Width="290px"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">������</td>
               <td><asp:TextBox ID="PreturnCode" runat="server" ReadOnly="True" Width="290px"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">������Ϣ</td>
               <td><asp:TextBox ID="Pmessage" runat="server" Width="290px"></asp:TextBox></td>
            </tr>
            
            </table>
            <%
                }
                else
                {
                
            %>
            <table id="ResTable" class="api" runat="server" >
            <tr>
               <td class="field">�˿���</td>
               <td><asp:TextBox ID="Pamount" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">�̻�������</td>
               <td><asp:TextBox ID="PorderId" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
            <tr>
               <td class="field">�˿�״̬</td>
               <td><asp:TextBox ID="Pstatus" runat="server" ReadOnly="True"></asp:TextBox></td>
            </tr>
          
            </table>
            <%
                }    
            %>
            <br />
             </center>
             <a id="HomeLink" class="home" href="Index.aspx">��ҳ</a>
    </form>
</body>
</html>
