 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 
<html>
<head>
<title>订单查询:OrderQuery</title>
<link rel="stylesheet" type="text/css" href="css/sdk.css" />
<meta http-equiv="Content-Type" content="text/html charset=gb2312"/>
</head>
<body>
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%        
          '设置类型为订单查询
          Dim type2
          type2 = "OrderQuery"
          
          '获取页面传递过来的参数
          orderId	 = Request("orderId")
          
          '设置签名数据
          Dim signData
          signData =  merchantId&requestId&signType&type2&version&orderId
          
          '创建动态链接库客户端对象
		      Set util = Server.CreateObject("IPOSM.SignUtil")
		      
		      '用密钥进行初始化
		      ret	= util.Init(signKey)
		      
		      '初始化成功
		      If(ret = 0) Then
		      	 '调用MD5签名算法得到hamc
		      	 If(signType = "MD5") Then
		            signed = ""&Cstr(util.MD5Sign(signData))
		         End If
		         
		         '设置传输给中心平台的数据
		         tData = "hmac="&signed&"&merchantId="&merchantId&"&requestId="
		         tData1 = tData&(requestId&"&signType="&signType)
		         tData2 = tData1&("&type="&type2&"&version="&version&"&orderId="&orderId)
		         
		         '向中心平台发起请求并得到返回信息
		         msg = util.SendAndRecv(req_url,tData2)
		         
		         '获取返回码
		         dim returnCode
             returnCode = ""&Cstr(util.getValue(msg, "returnCode"))
             
             If(returnCode<>"000000")Then
             	  Response.Write("查询失败</br>")
                Response.Write(returnCode&" "&cstr(UTF2GB(util.getValue(msg, "message"))))
                Response.End
             End If
             
             '设置验签数据
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
             
             '获取中心平台返回的hamc
             dim hm
             hm = ""&Cstr(util.getValue(msg,"hmac"))
             
             '进行验签
             dim rec
             rec = util.Verify(vfsign10,hm)
             
             If(rec<>0) Then
                Response.Write("验签失败")
                Response.End
             End If
             
             Response.Write("</br>")
             Response.Write("================")
				     Response.Write("<br/>")
				     Response.Write("交易成功")
				     Response.Write("<br/>")
				     Response.Write("商户编号：")
				     Response.Write(util.getValue(msg, "merchantId"))
				     Response.Write("<br/>")
				     Response.Write("流水号:")
				     Response.Write(util.getValue(msg, "payNo"))
				     Response.Write("<br/>")
				     Response.Write("返回码:")
				     Response.Write(util.getValue(msg, "returnCode"))
				     Response.Write("<br/>")
				     Response.Write("返回码描述信息:")
				     Response.Write(UTF2GB(util.getValue(msg, "message")))
				     Response.Write("<br/>")
				     Response.Write("签名方式:") 
				     Response.Write(util.getValue(msg, "signType"))
				     Response.Write("<br/>")
				     Response.Write("接口类型：")
				     Response.Write(util.getValue(msg, "type"))
				     Response.Write("<br/>")
				     Response.Write("版本号：")
				     Response.Write(util.getValue(msg, "version"))
				     Response.Write("<br/>")
				     Response.Write("支付金额：")
				     Response.Write(util.getValue(msg, "amount"))
				     Response.Write("<br/>")
				     Response.Write("金额明细：")
				     Response.Write(util.getValue(msg, "amtItem"))
				     Response.Write("<br/>")
				     Response.Write("支付银行：")
				     Response.Write(util.getValue(msg, "bankAbbr"))
				     Response.Write("<br/>")
				     Response.Write("支付手机号：")
				     Response.Write(util.getValue(msg, "mobile"))
				     Response.Write("<br/>")
				     Response.Write("商户订单号：")
				     Response.Write(util.getValue(msg, "orderId"))
				     Response.Write("<br/>")
				     Response.Write("支付时间：")
				     Response.Write(util.getValue(msg, "payDate"))
				     Response.Write("<br/>")
				     Response.Write("保留字段1：")
				     Response.Write(UTF2GB(util.getValue(msg, "reserved1")))
				     Response.Write("<br/>")
				     Response.Write("保留字段2：")
				     Response.Write(UTF2GB(util.getValue(msg, "reserved2")))
				     Response.Write("<br/>")
				     Response.Write("支付结果：")
				     Response.Write(util.getValue(msg, "status"))
				     Response.Write("<br/>")
				     Response.Write("订单日期：")
				     Response.Write(util.getValue(msg, "orderDate"))
				     Response.Write("<br/>")
				     Response.Write("费用：")
				     Response.Write(util.getValue(msg, "fee"))
				     Response.Write("<br/>")    
		      End If
%>
</body>
</html>