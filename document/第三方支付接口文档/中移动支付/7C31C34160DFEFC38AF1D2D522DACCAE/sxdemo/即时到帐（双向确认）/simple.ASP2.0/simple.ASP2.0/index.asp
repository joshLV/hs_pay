<%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ޱ����ĵ�</title>
</head>

<body>
<%
		Dim merchantId
		merchantId = Request("merchantId") 

		If merchantId = "" Then 
			merchantId = "888073157340001"
		End If
		Session("merchantId") = merchantId
%>
<table width = "75%" border = "0" align = "center">
   <tr>
       <td>
           <div align = "center">
               <p>
                   <font color = "#0000FF">Hisun IPOS</font>
               </p>
   
               <p>
                   <font color = "#0000FF">�̻�[<%Response.Write(merchantId)%>]����������ʾ</font>
               </p>
   
               <hr>
           </div>
       </td>
   </tr>
   <tr>
      <td align = "center">
          <form action="index.asp">
          �̻���: <input name="merchantId" value="<%=merchantId%>"/>
                  <input type="submit" value="�����̻���"/>
          </form>
      </td>
   </tr>
       <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dogwdirectpayment_input.asp">ֱ��֧��(��������)</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dodirectpayment_token_input.asp">ֱ��֧��(˫��ȷ��)</a></font>
           </div>
       </td>
   </tr>
       <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dowapdirectpayment_token_input.asp">ֱ��֧��(WAP)</a></font>
           </div>
       </td>
   </tr>
    <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dodirectpayment_sms_input.asp">ֱ��֧��(����)</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "ordersearch_input.asp">������ѯ</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
                <font color = "#000000"><a href = "orderrefund_input.asp">�˿�</a></font>
           </div>
       </td>
   </tr> 
</table> 
</body>
</html>