 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html>
<head>
<title>�����˿ORDERREFUND</title>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">
</head>
<body bgcolor="#FFFFFF" text="#000000">
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%
          '��������Ϊ�����˿�
			    Dim type2
			    type2 = "OrderRefund"
			    
			    '��ȡҳ�洫�ݹ����Ĳ���
		      amount = Request("amount")
          orderId	= Request("orderId")
          
          '����ǩ������
          Dim signData
          signData =  merchantId&requestId&signType&type2&version&orderId&amount
          
          '������̬���ӿ�ͻ��˶���
          Set util = Server.CreateObject("IPOSM.SignUtil")
          
          '����Կ���г�ʼ��
          Dim ret
		      ret = util.Init(signKey)
		      
		      '��ʼ���ɹ�
		      If(ret = 0) Then
		      	 '����MD5ǩ���㷨�õ�hamc
		      	 If(signType = "MD5") Then
		      	    Dim signed
                signed = ""&Cstr(util.MD5Sign(signData))	
             End If
		         
		         '���ô��������ƽ̨������
		         Dim transData1
		         Dim transData2
		         Dim transData3
		         Dim transData4
		         Dim transData5
		         transData1 = "hmac="&signed&"&merchantId="&merchantId&"&requestId="&requestId
		         transData2 = "&signType="&signType&"&type="&type2
		         transData3 = "&version="&version&"&orderId="&orderId&"&amount="&amount
		         transData4 = transData1&transData2
		         transData5 = transData4&transData3
		         
		         '������ƽ̨�������󲢵õ�������Ϣ
		         Dim msg
		         msg = ""&Cstr(util.SendAndRecv(req_url,transData5))
		         
		         '��ȡ������
             dim returnCode
             returnCode = ""&Cstr(util.getValue(msg, "returnCode"))
             
             If(returnCode<>"000000")Then
          	    Response.Write("�˿�ʧ��</br>")
                Response.Write(returnCode&" "&cstr(UTF2GB(util.getValue(msg, "message"))))
                Response.End
             End If
						 
						 '������ǩ����
						 dim vfsign
						 vfsign = ""&util.getValue(msg,"merchantId")&Cstr(util.getValue(msg,"payNo"))
						 vfsign2 = vfsign&(""&util.getValue(msg,"returnCode")&Cstr(util.getValue(msg,"message")))
						 vfsign3 = vfsign2&(""&util.getValue(msg,"signType")&Cstr(util.getValue(msg,"type")))
						 vfsign4 = vfsign3&(""&util.getValue(msg,"version")&Cstr(util.getValue(msg,"amount")))
						 vfsign5 = vfsign4&(""&util.getValue(msg,"orderId")&Cstr(util.getValue(msg,"status")))
             
             '��ȡ����ƽ̨���ص�hamc
             dim hm
             hm = ""&Cstr(util.getValue(msg,"hmac"))
             
             '������ǩ
             dim rec
             rec = util.Verify(vfsign5,hm)
             
             If(rec<>0) Then
                Response.Write("��ǩʧ��")
                Response.End
             End If
			       
			       Response.Write("================")
				     Response.Write("</br>")
				     Response.Write("���׳ɹ�")
				     Response.Write("</br>")
				     Response.Write("================")
				     Response.Write("</br>")
				     Response.Write("�˿���:"&cstr(util.getValue(msg, "amount")))  
				     Response.Write("</br>")
				     Response.Write("�̻�������:"&cstr(util.getValue(msg, "orderId")))
				     Response.Write("</br>")
				     Response.Write("�˿���:"&cstr(util.getValue(msg, "status")))
				     Response.Write("</br>")
		      End If
%>
</body>
</html>