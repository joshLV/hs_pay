 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html>
<head>
<title>订单退款：ORDERREFUND</title>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">
</head>
<body bgcolor="#FFFFFF" text="#000000">
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%
          '设置类型为订单退款
			    Dim type2
			    type2 = "OrderRefund"
			    
			    '获取页面传递过来的参数
		      amount = Request("amount")
          orderId	= Request("orderId")
          
          '设置签名数据
          Dim signData
          signData =  merchantId&requestId&signType&type2&version&orderId&amount
          
          '创建动态链接库客户端对象
          Set util = Server.CreateObject("IPOSM.SignUtil")
          
          '用密钥进行初始化
          Dim ret
		      ret = util.Init(signKey)
		      
		      '初始化成功
		      If(ret = 0) Then
		      	 '调用MD5签名算法得到hamc
		      	 If(signType = "MD5") Then
		      	    Dim signed
                signed = ""&Cstr(util.MD5Sign(signData))	
             End If
		         
		         '设置传输给中心平台的数据
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
		         
		         '向中心平台发起请求并得到返回信息
		         Dim msg
		         msg = ""&Cstr(util.SendAndRecv(req_url,transData5))
		         
		         '获取返回码
             dim returnCode
             returnCode = ""&Cstr(util.getValue(msg, "returnCode"))
             
             If(returnCode<>"000000")Then
          	    Response.Write("退款失败</br>")
                Response.Write(returnCode&" "&cstr(UTF2GB(util.getValue(msg, "message"))))
                Response.End
             End If
						 
						 '设置验签数据
						 dim vfsign
						 vfsign = ""&util.getValue(msg,"merchantId")&Cstr(util.getValue(msg,"payNo"))
						 vfsign2 = vfsign&(""&util.getValue(msg,"returnCode")&Cstr(util.getValue(msg,"message")))
						 vfsign3 = vfsign2&(""&util.getValue(msg,"signType")&Cstr(util.getValue(msg,"type")))
						 vfsign4 = vfsign3&(""&util.getValue(msg,"version")&Cstr(util.getValue(msg,"amount")))
						 vfsign5 = vfsign4&(""&util.getValue(msg,"orderId")&Cstr(util.getValue(msg,"status")))
             
             '获取中心平台返回的hamc
             dim hm
             hm = ""&Cstr(util.getValue(msg,"hmac"))
             
             '进行验签
             dim rec
             rec = util.Verify(vfsign5,hm)
             
             If(rec<>0) Then
                Response.Write("验签失败")
                Response.End
             End If
			       
			       Response.Write("================")
				     Response.Write("</br>")
				     Response.Write("交易成功")
				     Response.Write("</br>")
				     Response.Write("================")
				     Response.Write("</br>")
				     Response.Write("退款金额:"&cstr(util.getValue(msg, "amount")))  
				     Response.Write("</br>")
				     Response.Write("商户订单号:"&cstr(util.getValue(msg, "orderId")))
				     Response.Write("</br>")
				     Response.Write("退款结果:"&cstr(util.getValue(msg, "status")))
				     Response.Write("</br>")
		      End If
%>
</body>
</html>