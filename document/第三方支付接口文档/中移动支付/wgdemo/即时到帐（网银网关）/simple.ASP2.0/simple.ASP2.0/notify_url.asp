<%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
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
		      ret = util.Init(signKey)
		      
		      '��ʼ���ɹ�  
		      If(ret = 0) Then 
		      	 '������ǩ����   
		         dim vfsign
		         vfsign = merchantId&payNo&returnCode&message&signType&type2&version&amount&amtItem&bankAbbr&mobile&orderId&payDate&accountDate&reserved1&reserved2&status1&orderDate&fee
             
             '������ǩ
             dim rec
             rec = util.Verify(vfsign,hmac1)
             
             If(rec<>0) Then
                Response.Write("��ǩʧ��</br>")
                Response.End
             End If
		         
		         Response.Write("SUCCESS")
			    End If    
%>