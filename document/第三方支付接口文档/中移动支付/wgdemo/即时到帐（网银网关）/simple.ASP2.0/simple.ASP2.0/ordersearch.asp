 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html>
<head>
<title>������ѯ:OrderQuery</title>
<link rel="stylesheet" type="text/css" href="css/sdk.css" />
<meta http-equiv="Content-Type" content="text/html charset=gb2312"/>
</head>
<body>
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%        
          '��������Ϊ������ѯ
          Dim type2
          type2 = "OrderQuery"
          
          '��ȡҳ�洫�ݹ����Ĳ���
          orderId	 = Request("orderId")
          
          '����ǩ������
          Dim signData
          signData =  merchantId&requestId&signType&type2&version&orderId
          
          '������̬���ӿ�ͻ��˶���
		      Set util = Server.CreateObject("IPOSM.SignUtil")
		      
		      '����Կ���г�ʼ��
		      ret	= util.Init(signKey)
		      
		      '��ʼ���ɹ�
		      If(ret = 0) Then
		      	 '����MD5ǩ���㷨�õ�hamc
		      	 If(signType = "MD5") Then
		            signed = ""&Cstr(util.MD5Sign(signData))
		         End If
		         
		         '���ô��������ƽ̨������
		         tData = "hmac="&signed&"&merchantId="&merchantId&"&requestId="
		         tData1 = tData&(requestId&"&signType="&signType)
		         tData2 = tData1&("&type="&type2&"&version="&version&"&orderId="&orderId)
		         
		         '������ƽ̨�������󲢵õ�������Ϣ
		         msg = util.SendAndRecv(req_url,tData2)
		         
		         '��ȡ������
		         dim returnCode
             returnCode = ""&Cstr(util.getValue(msg, "returnCode"))
             
             If(returnCode<>"000000")Then
             	  Response.Write("��ѯʧ��</br>")
                Response.Write(returnCode&" "&cstr(UTF2GB(util.getValue(msg, "message"))))
                Response.End
             End If
             
             '������ǩ����
             dim vfsign
						 vfsign = ""&util.getValue(msg,"merchantId")&Cstr(util.getValue(msg,"payNo"))
						 vfsign2 = vfsign&(""&util.getValue(msg,"returnCode")&Cstr(util.getValue(msg,"message")))
						 vfsign3 = vfsign2&(""&util.getValue(msg,"signType")&Cstr(util.getValue(msg,"type")))
						 vfsign4 = vfsign3&(""&util.getValue(msg,"version")&Cstr(util.getValue(msg,"amount")))
						 vfsign5 = vfsign4&(""&util.getValue(msg,"amtItem")&Cstr(util.getValue(msg,"bankAbbr")))
						 vfsign6 = vfsign5&(""&util.getValue(msg,"mobile")&Cstr(util.getValue(msg,"orderId")))
						 vfsign7 = vfsign6&(""&util.getValue(msg,"payDate")&Cstr(util.getValue(msg,"reserved1")))
						 vfsign8 = vfsign7&(""&util.getValue(msg,"reserved2")&Cstr(util.getValue(msg,"status")))
						 vfsign9 = vfsign8&(""&util.getValue(msg,"payType")&Cstr(util.getValue(msg,"orderDate")))
						 vfsign10 = vfsign9&(""&Cstr(util.getValue(msg,"fee")))
             
             '��ȡ����ƽ̨���ص�hamc
             dim hm
             hm = ""&Cstr(util.getValue(msg,"hmac"))
             
             '������ǩ
             dim rec
             rec = util.Verify(vfsign10,hm)
             
             If(rec<>0) Then
                Response.Write("��ǩʧ��")
                Response.End
             End If
             
             Response.Write("</br>")
             Response.Write("================")
				     Response.Write("<br/>")
				     Response.Write("���׳ɹ�")
				     Response.Write("<br/>")
				     Response.Write("�̻���ţ�")
				     Response.Write(util.getValue(msg, "merchantId"))
				     Response.Write("<br/>")
				     Response.Write("��ˮ��:")
				     Response.Write(util.getValue(msg, "payNo"))
				     Response.Write("<br/>")
				     Response.Write("������:")
				     Response.Write(util.getValue(msg, "returnCode"))
				     Response.Write("<br/>")
				     Response.Write("������������Ϣ:")
				     Response.Write(UTF2GB(util.getValue(msg, "message")))
				     Response.Write("<br/>")
				     Response.Write("ǩ����ʽ:") 
				     Response.Write(util.getValue(msg, "signType"))
				     Response.Write("<br/>")
				     Response.Write("�ӿ����ͣ�")
				     Response.Write(util.getValue(msg, "type"))
				     Response.Write("<br/>")
				     Response.Write("�汾�ţ�")
				     Response.Write(util.getValue(msg, "version"))
				     Response.Write("<br/>")
				     Response.Write("֧����")
				     Response.Write(util.getValue(msg, "amount"))
				     Response.Write("<br/>")
				     Response.Write("�����ϸ��")
				     Response.Write(util.getValue(msg, "amtItem"))
				     Response.Write("<br/>")
				     Response.Write("֧�����У�")
				     Response.Write(util.getValue(msg, "bankAbbr"))
				     Response.Write("<br/>")
				     Response.Write("֧���ֻ��ţ�")
				     Response.Write(util.getValue(msg, "mobile"))
				     Response.Write("<br/>")
				     Response.Write("�̻������ţ�")
				     Response.Write(util.getValue(msg, "orderId"))
				     Response.Write("<br/>")
				     Response.Write("֧��ʱ�䣺")
				     Response.Write(util.getValue(msg, "payDate"))
				     Response.Write("<br/>")
				     Response.Write("�����ֶ�1��")
				     Response.Write(UTF2GB(util.getValue(msg, "reserved1")))
				     Response.Write("<br/>")
				     Response.Write("�����ֶ�2��")
				     Response.Write(UTF2GB(util.getValue(msg, "reserved2")))
				     Response.Write("<br/>")
				     Response.Write("֧�������")
				     Response.Write(util.getValue(msg, "status"))
				     Response.Write("<br/>")
				     Response.Write("�������ڣ�")
				     Response.Write(util.getValue(msg, "orderDate"))
				     Response.Write("<br/>")
				     Response.Write("���ã�")
				     Response.Write(util.getValue(msg, "fee"))
				     Response.Write("<br/>")    
		      End If
%>
</body>
</html>