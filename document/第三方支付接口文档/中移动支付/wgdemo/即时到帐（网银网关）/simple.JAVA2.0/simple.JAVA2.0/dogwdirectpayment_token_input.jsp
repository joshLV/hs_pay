<%@ page language="java" import="java.util.*,java.text.*"
	pageEncoding="GBK"%>
<%
       //ȡ���̻���ǰϵͳʱ��
       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
       //�̻�������
       String orderId = sdf.format(new Date());
       //�̻���������
       String orderDate = orderId.substring(0,8);
       
%>

<html>
	<head>
		<title>ֱ��֧��(��������)GWDirectPay</title>
		<link href="css/sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<form name="form1" method="post" action="dogwdirectpayment_token.jsp">
			<br>
			<center>
				<font size=2 color=black face=Verdana><b>ֱ��֧��(��������)GWDirectPay</b>
				</font>
				<br>
				<br>
				<table class="api">
					<tr>
						<td class="field">
							�������
						</td>
						<td>
							<input type="text" name="amount" maxlength='20' value="1">
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
							<select name="currency">
								<option value="00">
									00-CNY-������
								</option>
					
							</select>
						</td>
					</tr>
					<tr>
						<td class="field">
							��������
						</td>
						<td>
							<input type="text" name="orderDate" value="<%=orderDate%>">
							<font color="red">*�̻����������ʱ��; ����������������</font>

						</td>
					</tr>
					<tr>
						<td class="field">
							�̻�������
						</td>
						<td>
							<input type="text" name="orderId" value="<%=orderId%>">
							<font color="red">*</font>
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
                        <option value="02" selected>
                           02-��
                        </option>
                        <option value="03">
                           03-��
                        </option>
                     </select>
                  </td>
               </tr>
               <tr>
                  <td class="field">�̻�չʾ����</td>
                  <td><input type="text" name="merchantAbbr" value="�̻�չʾ����1"/></td>
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
							<input type="text" name="userToken" value="13548649407">
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
                     <input type="text" name="couponsFlag" value=""/>
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
			<a id="HomeLink" class="home" href="index.jsp">��ҳ</a>
		</form>
	</body>
</html>
