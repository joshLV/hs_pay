<%@  language="VBSCRIPT" codepage="936" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
    <link rel="stylesheet" type="text/css" href="css/sdk.css" />
    <title>ֱ��֧��(��������)��GWDirectPay</title>
</head>
<%
	Dim currDate
	currYear = Year(Date()) 
	currMonth=Month(Date())
	If(Len(currMonth)=1) Then
		currMonth = "0"&currMonth
	End If
	currDay = Day(Date())
	If(Len(currDay)=1) Then
		currDay = "0"&currDay
	End If
  currDate = currYear&currMonth&currDay
%>
<body>
    <form name="form1" method="post" action="dogwdirectpayment.asp">
        <center>
            <p>
                <input type="hidden" name="merCert" value="" />
                <span class="title">ֱ��֧��(��������)��GWDirectPay</span>
            </p>
            <table class="api">
               <tr>
						      <td class="field">
						    	�������
						      </td>
						      <td>
							       <input type="text" name="amount" maxlength='20' value="1000">
							       <font color="red">*�������Է�Ϊ��λ</font>
						      </td>
					     </tr>
					     <tr>
						      <td class="field">
							    ���д���
						      </td>
						      <td>
							       <select name="bankAbbr">
								        <option value="ICBC">��������</option>
								        <option value="CMB">��������</option>
								        <option value="CCB">��������</option>
								        <option value="ABC">ũҵ����</option>
								        <option value="BOC">�й�����</option>
								        <option value="SPDB">�Ϻ��ֶ���չ����</option>
								        <option value="BCOM">��ͨ����</option>
								        <option value="CMBC">��������</option>
								        <option value="CEBB">�������</option>
								        <option value="GDB">�㶫��չ����</option>
								        <option value="ECITIC">��������</option>
								        <option value="HXB">��������</option>
								        <option value="CIB">��ҵ����</option>
						
							       </select>
						     </td>
					   </tr>
					   <tr>
						   <td class="field">
							  ����
						   </td>
						   <td>
							    <select name="currency2">
								     <option value="00">
									   ������
								     </option>
								     
							    </select>
						   </td>
					  </tr>
					  <tr>
						   <td class="field">
							 ��������
						   </td>
						   <td>
							   <input type="text" name="orderDate" value="<%=currDate%>">
							   <font color="red">*�̻����������ʱ��; ����������������</font>
               </td>
					  </tr>
					  <tr>
						   <td class="field">
							 �̻�������
						   </td>
						   <td>
							    <input type="text" name="orderId" value = "<%=currDate&Int(Timer())%>">
							    <font color="red">*</font>
						   </td>
					 </tr>
					  <tr>
               <td class="field">
                  �̻��������
               </td>
               <td >
                   <input type = "text" name = "merAcDate" value = "<%=currDate%>"> <font color="red">*</font>
               </td>
           </tr>
					 <tr>
						  <td class="field">
							��Ч������
						  </td>
						  <td>
							   <input type="text" name="period" value="7">
							   <font color="red">*����</font>
						  </td>
					</tr>
          <tr>
             <td class="field">��Ч�ڵ�λ</td>
             <td>
                <select name="periodUnit">
                   <option value="00">
                      00-��
                   </option>
                   <option value="01">
                      01-Сʱ
                   </option>
                   <option value="02" selected="true">
                      02-��
                   </option>
                   <option value="03">
                      03-��
                   </option>
                </select>
              </td>
          </tr>
          <tr>
             <td class="field">
                ��Ʒչʾ����
             </td>

             <td >
                 <input type = "text" name = "merchantAbbr" value = "��Ʒչʾ����01"  >
             </td>
          </tr>
					<tr>
						<td class="field">
							��Ʒ����
						</td>
						<td>
							<input type="text" name="productDesc" value="��Ʒ����01">
						</td>
					</tr>
					<tr>
						<td class="field">
							��Ʒ���
						</td>
						<td>
							<input type="text" name="productId" value="��Ʒ���01">
						</td>
					</tr>
					<tr>
						<td class="field">
							��Ʒ����
						</td>
						<td>
							<input type="text" name="productName" value="������Ʒ01">
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
					   <td class="field">
					     ��Ʒ����
					   </td>
					   <td>
					      <input type="text" name="productNum" value="1"/>
					   </td>
					</tr>
					<tr>
						<td class="field">
							�����ֶ�1
						</td>
						<td>
							<input type="text" name="reserved1" value="��������1">
						</td>
					</tr>
          <tr>
             <td class="field">
             �����ֶ�2
             </td>
             <td>
                <input type="text" name="reserved2" value="��������2">
             </td>
          </tr>
					<tr>
						<td class="field">
							�û���ʶ
						</td>
						<td>
							<input type="text" name="userToken" value="15116410263">
							<font color="red">*</font>
						</td>
					</tr>
          <tr>
             <td class="field">��Ʒչʾ��ַ</td>
             <td>
                <input type="text" name="showUrl" value=""/>
             </td>
          </tr>
          <tr>
             <td class="field">Ӫ������ʹ�ÿ���</td>
             <td>
                 <select name="couponsFlag">
                    <option value="00">00-ʹ��ȫ��Ӫ������ </option>
                    <option value="10">10-��֧��ʹ�õ���ȯ </option>
                    <option value="20">20-��֧�ִ���ȯ </option>
                    <option value="30">30-��֧�ֻ���</option>
                    <option value="40">40-��֧������Ӫ������ </option>
                 </select>
             </td>
          </tr>
					<tr>
						<td class="field">
						</td>
						<td>
							<input type="Submit" value="�ύ" id="Submit" name="submit" />
						</td>
					</tr>
       </table>
    </center>
    <a id="HomeLink" class="home" href="index.asp">��ҳ</a>
    </form>
</body>
</html>
