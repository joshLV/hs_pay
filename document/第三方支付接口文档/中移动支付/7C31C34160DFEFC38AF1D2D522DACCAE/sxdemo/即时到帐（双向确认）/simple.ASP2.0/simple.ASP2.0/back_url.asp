<%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html charset=GB18030">
<title>֪ͨ������ʾ</title>
</head>
<body>
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%
          '��ȡ����ƽ̨���ݹ���������
	        merchantId = Request("merchantId")
	        payNo = Request("payNo")
	        returnCode = Request("returnCode")
	        message	= Request("message")
	        signType = Request("signType")
	        type2 = Request("type")
	        version = Request("version")
	        amount = Request("amount")
          amtItem = Request("amtItem")
          bankAbbr = Request("bankAbbr")
          mobile = Request("mobile")
          orderId = Request("orderId")
          payDate	= Request("payDate")
          reserved1 = Request("reserved1")
          reserved2	= Request("reserved2")
          status1 = Request("status")
          orderDate	= Request("orderDate")
          accountDate	= Request("accountDate")
          fee	= Request("fee")
          hmac1 = Request("hmac")
          
          '������̬���ӿ�ͻ��˶���
          Set util = Server.CreateObject("IPOSM.SignUtil")
          
          '������־·��
		      util.SetLogPath(logPath)
		      
		      '����Կ���г�ʼ��
		      ret	= util.Init(signKey)  
		      If(ret = 0) Then    
		         '������ǩ����
		         dim vfsign
		         vfsign = merchantId&payNo&returnCode&message&signType&type2&version&amount&amtItem&bankAbbr&mobile&orderId&payDate&accountDate&reserved1&reserved2&status1&orderDate&fee
             
             '������ǩ
             dim rec
             rec = util.Verify(vfsign,hmac1)
             If(rec<>0) Then
                Response.Write("��ǩʧ��</br>")
                Response.Write(UTF2GB(message))
                Response.End
             End If
		         
		         Response.Write("�̻����:"&merchantId)
      	     Response.Write("</br>")
			       Response.Write("֧�����:"&amount)
			       Response.Write("</br>")
			       Response.Write("֧������:"&bankAbbr)
			       Response.Write("</br>")
			       Response.Write("֧����:"&mobile)
			       Response.Write("</br>")
			       Response.Write("�����ֶ�1:"&UTF2GB(reserved1))
			       Response.Write("</br>")
			       Response.Write("�����ֶ�2:"&UTF2GB(reserved2))
			       Response.Write("</br>")
			    End If     
%>
</body>
</html>

