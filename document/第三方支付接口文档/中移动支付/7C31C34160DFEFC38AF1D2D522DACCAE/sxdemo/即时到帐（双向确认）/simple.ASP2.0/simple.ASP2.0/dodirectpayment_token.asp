 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>ֱ��֧��(TOKEN)��DODIRECTPAYMENT</title>
<link rel="stylesheet" type="text/css" href="css/sdk.css" />
<meta http-equiv="Content-Type" content="text/html charset=gb2312"/>
</head>
<body>
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%
          '����֧������Ϊֱ��֧��(TOKEN)
		      Dim type2
	        type2 = "DirectPayConfirm"
          
          '��ȡҳ�洫�ݹ����Ĳ���
	        amount = Request("amount")
	        bankAbbr = Request("bankAbbr")
          currency2	= Request("currency2")
          orderDate	= Request("orderDate")
          orderId	= Request("orderId")
          merAcDate	= Request("merAcDate")
          period = Request("period")
          periodUnit = Request("periodUnit")
          merchantAbbr = Request("merchantAbbr")
          productDesc	= Request("productDesc")
          productId	= Request("productId")
          productName	= Request("productName")
          productNum = Request("productNum")
          reserved1	= Request("reserved1")
          reserved2	= Request("reserved2")
          userToken	= Request("userToken")
          showUrl	= Request("showUrl")
          couponsFlag	= Request("couponsFlag")
          
          '����ǩ������
          signData = characterSet&callbackUrl&notifyUrl&ipAddress&merchantId&requestId&signType
          signData1 = signData&type2&version&amount&bankAbbr&currency2&orderDate&orderId&merAcDate&period
          signData2 = signData1&periodUnit&merchantAbbr&productDesc
          signData3 = signData2&productId&productName&productNum&reserved1
          signData4 = signData3&reserved2&userToken&showUrl&couponsFlag
          
          '������̬���ӿ�ͻ��˶���
          Set util  = Server.CreateObject("IPOSM.SignUtil")
		      
		      '����Կ���г�ʼ��
		      ret = util.Init(signKey)
		      
		      '��ʼ���ɹ�
		      If(ret = 0) Then
		      	 '����MD5ǩ���㷨�õ�hamc
		      	 If(signType = "MD5") Then
		            signed = ""&Cstr(util.MD5Sign(signData4))
		         End If
		         '���ô��������ƽ̨������
		         tData = "characterSet="&characterSet&"&callbackUrl="&callbackUrl&"&notifyUrl="
		         tData1 = tData&(notifyUrl&"&ipAddress="&ipAddress&"&merchantId="&merchantId)
		         tData2 = tData1&("&requestId="&requestId&"&signType="&signType&"&type="&type2)
		         tData3 = tData2&("&version="&version&"&amount="&amount&"&bankAbbr="&bankAbbr)
		         tData4 = tData3&("&currency="&currency2&"&orderDate="&orderDate&"&orderId=")
		         tData5 = tData4&(orderId&"&merAcDate="&merAcDate&"&period="&period&"&periodUnit="&periodUnit&"&merchantAbbr="&merchantAbbr&"&productDesc=")
		         tData6 = tData5&(productDesc&"&productId="&productId&"&productName="&productName)
		         tData7 = tData6&("&productNum="&productNum&"&reserved1="&reserved1&"&reserved2=")
		         tData8 = tData7&(reserved2&"&userToken="&userToken&"&showUrl="&showUrl&"&couponsFlag=")
		         tData9 = tData8&couponsFlag&"&hmac="&signed
		         
		         '������ƽ̨�������󲢵õ�������Ϣ
		         msg = ""&Cstr(util.SendAndRecv(req_url,tData9))
		         
		         '��ȡ������
		         dim returnCode
             returnCode = ""&Cstr(util.getValue(msg, "returnCode"))
             
             If(returnCode<>"000000")Then
             	  Response.Write("�µ�ʧ��</br>")
                Response.Write(returnCode&" "&cstr(UTF2GB(util.getValue(msg, "message"))))
                Response.End
             End If
             
             '������ǩ����
             dim vfsign
						 vfsign = ""&util.getValue(msg,"merchantId")&Cstr(util.getValue(msg,"requestId"))
						 vfsign2= vfsign&(""&util.getValue(msg,"signType")&Cstr(util.getValue(msg,"type")))
						 vfsign3= vfsign2&(""&util.getValue(msg,"version")&Cstr(util.getValue(msg,"returnCode")))
						 vfsign4= vfsign3&(""&util.getValue(msg,"message")&Cstr(util.getValue(msg,"payUrl")))
						 
						 '��ȡ����ƽ̨���ص�hamc
						 dim hm
             hm = ""&Cstr(util.getValue(msg,"hmac"))
             
             '������ǩ
             dim rec
             If(signType = "MD5") Then
                rec = util.Verify(vfsign4,hm)
             End If
             If(rec<>0) Then
                Response.Write("��ǩʧ��")
                Response.End
             End If
             
             '����������ƽ̨���ص�URL���������ض���
             dim payUrl
		         payUrl = ""&Cstr(util.getValue(msg, "payUrl")) 
						 dim redirctUrl
						 redirctUrl = ""&util.getRedirectUrl(payUrl,"url")&"?sessionId="&Cstr(util.getRedirectUrl(payUrl,"sessionId"))
						 Response.Redirect(redirctUrl)		         
		      End If
%>
</body>
</html>
