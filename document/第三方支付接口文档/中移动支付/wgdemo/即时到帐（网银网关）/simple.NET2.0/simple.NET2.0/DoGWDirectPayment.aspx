<%@ Page Language="C#" AutoEventWireup="true" CodeFile="DoGWDirectPayment.aspx.cs" Inherits="MCGASPDOTNET.DoGWDirectPayment" %>

<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		<title>即时到帐（银行网关）结果</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
	</head>
   
   
	<body>
    <form id="form1" method="post"  runat="server">
    <br/>
			<center>
				<span class="title"><b>即时到帐（银行网关）结果</b> </span>
				<br/>
				<br/>
            <br />
           
             <table class="api">
               
                <tr>
                    <td>
                        <input type="hidden" id="characterSet" runat="server" />
                        <input type="hidden" id="callbackUrl"  runat="server" />
			            <input type="hidden" id="notifyUrl" runat="server" />
			            <input type="hidden" id="ipAddress" runat="server" />
			            <input type="hidden" id="merchantId" runat="server"  />
		              	<input type="hidden" id="requestId" runat="server"  />
		              	<input type="hidden" id="signType" runat="server" />
		            	<input type="hidden" id="type" runat="server" />
		            	<input type="hidden" id="version" runat="server" />
		             	<input type="hidden" id="hmac"  runat="server" />
                        <input type="hidden" id="amount" runat="server" />
                        <input type="hidden" id="bankAbbr" runat="server" />
                        <input type="hidden" id="currency" runat="server" />
                        <input type="hidden" id="orderDate" runat="server" />
                        <input type="hidden" id="orderId" runat="server" />
                        <input type="hidden" id="merAcDate" runat="server" />
                        <input type="hidden" id="period" runat="server" />
                        <input type="hidden" id="periodUnit" runat="server" />
                        <input type="hidden" id="merchantAbbr" runat="server" />
                        <input type="hidden" id="productDesc" runat="server" />
                        <input type="hidden" id="productId" runat="server" />
                        <input type="hidden" id="productName" runat="server" />
                        <input type="hidden" id="productNum" runat="server" />
                        <input type="hidden" id="reserved1" runat="server" />
                        <input type="hidden" id="reserved2" runat="server" />
                        <input type="hidden" id="userToken" runat="server" />
                        
                        <input type="hidden" id="showUrl" runat="server" />
                        <input type="hidden" id="couponsFlag" runat="server" />
                       </td>
                   </tr>
                   <tr>
                    
                    <td>
                        <input type="submit"  value="提交" />
                    </td>
                    
                   </tr>
                </table>
               
             </center>
             
             <a id="HomeLink" class="home" href="Index.aspx">首页</a>
    </form>
</body>
</html>
