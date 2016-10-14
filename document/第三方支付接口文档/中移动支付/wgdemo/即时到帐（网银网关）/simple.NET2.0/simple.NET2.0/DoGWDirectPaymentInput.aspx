<%@ Page Language="C#" AutoEventWireup="true" CodeFile="DoGWDirectPaymentInput.aspx.cs"
    Inherits="MCGASPDOTNET.DoGWDirectPaymentInput" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>即时到帐（银行网关）</title>
    <link href="css/sdk.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
</head>
<body>
    <form id="Form2" runat="server" action="">
        <br />
        <center>
            <input type="hidden" name="merCert" value="" />
            <span class="title"><b>即时到帐（银行网关）</b> </span>
            <br />
            <br />
            <table class="api">
                <tr>
                    <td class="field">
                        订单金额
                    </td>
                    <td >
                        <asp:TextBox ID="amount" runat="server" ></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server"  ControlToValidate="amount" Display="Dynamic">
                        <span class="remark">金额不能为空</span>
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server"  ControlToValidate="amount" ValidationExpression="^[1-9]\d*$" Display="Dynamic">
                           <span class="remark">金额以分为单位，只能为正整数</span>
                        </asp:RegularExpressionValidator>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        银行代码
                    </td>
                    <td>
                        <asp:DropDownList ID="bankAbbr" runat="server">
                            <asp:ListItem Selected="True" Value="ICBC">工商银行</asp:ListItem>
                            <asp:ListItem Value="CMB">招商银行</asp:ListItem>
                            <asp:ListItem Value="CCB">建设银行</asp:ListItem>
                            <asp:ListItem Value="ABC">农业银行</asp:ListItem>
                            <asp:ListItem Value="BOC">中国银行</asp:ListItem>
                            <asp:ListItem Value="SPDB">上海浦东发展银行</asp:ListItem>
                            <asp:ListItem Value="BCOM">交通银行</asp:ListItem>
                            <asp:ListItem Value="CMBC">民生银行</asp:ListItem>
                            <asp:ListItem Value="CEBB">光大银行</asp:ListItem>
                            <asp:ListItem Value="GDB">广东发展银行</asp:ListItem>
                            <asp:ListItem Value="ECITIC">中信银行</asp:ListItem>
                            <asp:ListItem Value="HXB">华夏银行</asp:ListItem>
                            <asp:ListItem Value="CIB">兴业银行</asp:ListItem>
                         
                        </asp:DropDownList>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        币种
                    </td>
                    <td>
                        <asp:DropDownList ID="currency" runat="server">
                            <asp:ListItem Selected="True" Value="00">CNY-可提现</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        订单提交日期
                    </td>
                    <td>
                        <asp:TextBox ID="orderDate" runat="server"></asp:TextBox><span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ControlToValidate="orderDate" Display="Dynamic">
                            <span class="remark">订单日期不能为空</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        商户订单号
                    </td>
                    <td>
                        <asp:TextBox ID="orderId" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" ControlToValidate="orderId" Display="Dynamic">
                            <span class="remark">商户订单号不能为空</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        商户会计日期
                    </td>
                    <td>
                        <asp:TextBox ID="merAcDate" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator4" runat="server" ControlToValidate="merAcDate" Display="Dynamic">
                            <span class="remark">商户会计日期不能为空</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        有效期数量
                    </td>
                    <td>
                        <asp:TextBox ID="period" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator5" runat="server" ControlToValidate="period" Display="Dynamic">
                            有效期数量不能为空
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="RegularExpressionValidator2" runat="server"  ControlToValidate="period" ValidationExpression="^[1-9]\d*$" Display="Dynamic">
                           有效期数量只能为正整数
                        </asp:RegularExpressionValidator>
                    
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        有效期单位
                    </td>
                    <td>
                        <asp:DropDownList ID="periodUnit" runat="server">
                            <asp:ListItem Value="00">00-分</asp:ListItem>
                            <asp:ListItem Value="01">01-小时</asp:ListItem>
                            <asp:ListItem Selected="True" Value="02">02-日</asp:ListItem>
                            <asp:ListItem Value="03">03-月</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                 <tr>
                    <td class="field">
                        商户展示名称
                    </td>
                    <td>
                        <asp:TextBox ID="merchantAbbr" runat="server" ></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        产品描述
                    </td>
                    <td>
                        <asp:TextBox ID="productDesc" runat="server" ></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        产品编号
                    </td>
                    <td>
                        <asp:TextBox ID="productId" runat="server"></asp:TextBox>
                       
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        产品名称
                    </td>
                    <td>
                        <asp:TextBox ID="productName" runat="server" ></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator6" runat="server" ControlToValidate="productName" Display="Dynamic">
                            产品名称不能为空
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        产品数量
                    </td>
                    <td>
                        <asp:TextBox ID="productNum" runat="server"></asp:TextBox>
                       
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        保留字段1
                    </td>
                    <td>
                        <asp:TextBox ID="reserved1" runat="server" ></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        保留字段2
                    </td>
                    <td>
                        <asp:TextBox ID="reserved2" runat="server" ></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        用户标识
                    </td>
                    <td>
                        <asp:TextBox ID="userToken" runat="server"></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        商户展示地址
                    </td>
                    <td>
                        <asp:TextBox ID="showUrl" runat="server"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        营销工具使用控制
                    </td>
                    <td>
                        <asp:DropDownList ID="couponsFlag" runat="server">
                            <asp:ListItem Selected="True" Value="00">00-使用全部营销工具</asp:ListItem>
                            <asp:ListItem Value="10">10-不支持使用电子券</asp:ListItem>
                            <asp:ListItem Value="20">20-不支持代金券</asp:ListItem>
                            <asp:ListItem Value="30">30-不支持积分</asp:ListItem>
                            <asp:ListItem Value="40">40-不支持所有营销工具</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
               
                <tr>
                    <td class="field">
                    </td>
                    <td>
                        <asp:Button ID="Button1" runat="server" Text="提交" OnClick="Button1_Click" />
                        
                    </td>
                    
                </tr>
                
               
            </table>
        </center>
        <a id="HomeLink" class="home" href="Index.aspx">首页</a>
    </form>
</body>
</html>
