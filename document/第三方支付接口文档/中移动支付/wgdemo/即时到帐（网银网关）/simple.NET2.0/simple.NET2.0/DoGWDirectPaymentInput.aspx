<%@ Page Language="C#" AutoEventWireup="true" CodeFile="DoGWDirectPaymentInput.aspx.cs"
    Inherits="MCGASPDOTNET.DoGWDirectPaymentInput" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>��ʱ���ʣ��������أ�</title>
    <link href="css/sdk.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
</head>
<body>
    <form id="Form2" runat="server" action="">
        <br />
        <center>
            <input type="hidden" name="merCert" value="" />
            <span class="title"><b>��ʱ���ʣ��������أ�</b> </span>
            <br />
            <br />
            <table class="api">
                <tr>
                    <td class="field">
                        �������
                    </td>
                    <td >
                        <asp:TextBox ID="amount" runat="server" ></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server"  ControlToValidate="amount" Display="Dynamic">
                        <span class="remark">����Ϊ��</span>
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server"  ControlToValidate="amount" ValidationExpression="^[1-9]\d*$" Display="Dynamic">
                           <span class="remark">����Է�Ϊ��λ��ֻ��Ϊ������</span>
                        </asp:RegularExpressionValidator>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        ���д���
                    </td>
                    <td>
                        <asp:DropDownList ID="bankAbbr" runat="server">
                            <asp:ListItem Selected="True" Value="ICBC">��������</asp:ListItem>
                            <asp:ListItem Value="CMB">��������</asp:ListItem>
                            <asp:ListItem Value="CCB">��������</asp:ListItem>
                            <asp:ListItem Value="ABC">ũҵ����</asp:ListItem>
                            <asp:ListItem Value="BOC">�й�����</asp:ListItem>
                            <asp:ListItem Value="SPDB">�Ϻ��ֶ���չ����</asp:ListItem>
                            <asp:ListItem Value="BCOM">��ͨ����</asp:ListItem>
                            <asp:ListItem Value="CMBC">��������</asp:ListItem>
                            <asp:ListItem Value="CEBB">�������</asp:ListItem>
                            <asp:ListItem Value="GDB">�㶫��չ����</asp:ListItem>
                            <asp:ListItem Value="ECITIC">��������</asp:ListItem>
                            <asp:ListItem Value="HXB">��������</asp:ListItem>
                            <asp:ListItem Value="CIB">��ҵ����</asp:ListItem>
                         
                        </asp:DropDownList>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        ����
                    </td>
                    <td>
                        <asp:DropDownList ID="currency" runat="server">
                            <asp:ListItem Selected="True" Value="00">CNY-������</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        �����ύ����
                    </td>
                    <td>
                        <asp:TextBox ID="orderDate" runat="server"></asp:TextBox><span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ControlToValidate="orderDate" Display="Dynamic">
                            <span class="remark">�������ڲ���Ϊ��</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        �̻�������
                    </td>
                    <td>
                        <asp:TextBox ID="orderId" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" ControlToValidate="orderId" Display="Dynamic">
                            <span class="remark">�̻������Ų���Ϊ��</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        �̻��������
                    </td>
                    <td>
                        <asp:TextBox ID="merAcDate" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator4" runat="server" ControlToValidate="merAcDate" Display="Dynamic">
                            <span class="remark">�̻�������ڲ���Ϊ��</span>
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        ��Ч������
                    </td>
                    <td>
                        <asp:TextBox ID="period" runat="server"></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator5" runat="server" ControlToValidate="period" Display="Dynamic">
                            ��Ч����������Ϊ��
                        </asp:RequiredFieldValidator>
                        <asp:RegularExpressionValidator ID="RegularExpressionValidator2" runat="server"  ControlToValidate="period" ValidationExpression="^[1-9]\d*$" Display="Dynamic">
                           ��Ч������ֻ��Ϊ������
                        </asp:RegularExpressionValidator>
                    
                    </td>
                </tr>
                
                <tr>
                    <td class="field">
                        ��Ч�ڵ�λ
                    </td>
                    <td>
                        <asp:DropDownList ID="periodUnit" runat="server">
                            <asp:ListItem Value="00">00-��</asp:ListItem>
                            <asp:ListItem Value="01">01-Сʱ</asp:ListItem>
                            <asp:ListItem Selected="True" Value="02">02-��</asp:ListItem>
                            <asp:ListItem Value="03">03-��</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
                 <tr>
                    <td class="field">
                        �̻�չʾ����
                    </td>
                    <td>
                        <asp:TextBox ID="merchantAbbr" runat="server" ></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        ��Ʒ����
                    </td>
                    <td>
                        <asp:TextBox ID="productDesc" runat="server" ></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        ��Ʒ���
                    </td>
                    <td>
                        <asp:TextBox ID="productId" runat="server"></asp:TextBox>
                       
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        ��Ʒ����
                    </td>
                    <td>
                        <asp:TextBox ID="productName" runat="server" ></asp:TextBox>
                        <span class="remark">*</span>
                        <asp:RequiredFieldValidator ID="RequiredFieldValidator6" runat="server" ControlToValidate="productName" Display="Dynamic">
                            ��Ʒ���Ʋ���Ϊ��
                        </asp:RequiredFieldValidator>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        ��Ʒ����
                    </td>
                    <td>
                        <asp:TextBox ID="productNum" runat="server"></asp:TextBox>
                       
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        �����ֶ�1
                    </td>
                    <td>
                        <asp:TextBox ID="reserved1" runat="server" ></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        �����ֶ�2
                    </td>
                    <td>
                        <asp:TextBox ID="reserved2" runat="server" ></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        �û���ʶ
                    </td>
                    <td>
                        <asp:TextBox ID="userToken" runat="server"></asp:TextBox>
                        
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        �̻�չʾ��ַ
                    </td>
                    <td>
                        <asp:TextBox ID="showUrl" runat="server"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                        Ӫ������ʹ�ÿ���
                    </td>
                    <td>
                        <asp:DropDownList ID="couponsFlag" runat="server">
                            <asp:ListItem Selected="True" Value="00">00-ʹ��ȫ��Ӫ������</asp:ListItem>
                            <asp:ListItem Value="10">10-��֧��ʹ�õ���ȯ</asp:ListItem>
                            <asp:ListItem Value="20">20-��֧�ִ���ȯ</asp:ListItem>
                            <asp:ListItem Value="30">30-��֧�ֻ���</asp:ListItem>
                            <asp:ListItem Value="40">40-��֧������Ӫ������</asp:ListItem>
                        </asp:DropDownList>
                    </td>
                </tr>
               
                <tr>
                    <td class="field">
                    </td>
                    <td>
                        <asp:Button ID="Button1" runat="server" Text="�ύ" OnClick="Button1_Click" />
                        
                    </td>
                    
                </tr>
                
               
            </table>
        </center>
        <a id="HomeLink" class="home" href="Index.aspx">��ҳ</a>
    </form>
</body>
</html>
