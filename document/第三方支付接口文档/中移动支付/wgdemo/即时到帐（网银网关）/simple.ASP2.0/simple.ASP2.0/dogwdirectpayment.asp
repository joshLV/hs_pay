 <%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>直接支付(普通)：DirectPay</title>
<link rel="stylesheet" type="text/css" href="css/sdk.css" />
<meta http-equiv="Content-Type" content="text/html charset=gb2312">
</head>
<body bgcolor="#FFFFFF" text="#000000">
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%        
          '设置支付类型为直接支付(银行网关)
	        Dim type2
	        type2 = "GWDirectPay"
          
          '获取页面传递过来的参数
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
          showUrl	 = Request("showUrl")
          couponsFlag	= Request("couponsFlag")
          
          '设置签名数据
          signData = characterSet&callbackUrl&notifyUrl&ipAddress&merchantId&requestId&signType
          signData1 = signData&type2&version&amount&bankAbbr&currency2&orderDate&orderId&merAcDate&period
          signData2 = signData1&periodUnit&merchantAbbr&productDesc
          signData3 = signData2&productId&productName&productNum&reserved1
          signData4 = signData3&reserved2&userToken&showUrl&couponsFlag	
          
          '创建动态链接库客户端对象		  
          Set util = Server.CreateObject("IPOSM.SignUtil")
          
          '设置日志路径
		      util.SetLogPath(logPath)
          
          '用密钥进行初始化
          ret	= util.Init(signKey)
          
          '初始化成功
          If(ret = 0) Then
          	 '调用MD5签名算法得到hamc
          	 If(signType = "MD5") Then
                signed = ""&Cstr(util.MD5Sign(signData4)) 
             End If
          End If
  
%>
   <form method="post" action="<%=req_url%>">
			<input type="hidden" name="characterSet" value="<%=characterSet%>" />
			<input type="hidden" name="callbackUrl" value="<%=callbackUrl%>" />
			<input type="hidden" name="notifyUrl" value="<%=notifyUrl%>" />
			<input type="hidden" name="ipAddress" value="<%=ipAddress%>" />
			<input type="hidden" name="merchantId" value="<%=merchantId%>" />
			<input type="hidden" name="requestId" value="<%=requestId%>" />
			<input type="hidden" name="signType" value="<%=signType%>" />
			<input type="hidden" name="type" value="<%=type2%>" />
			<input type="hidden" name="version" value="<%=version%>" />
			<input type="hidden" name="hmac" value="<%=signed%>" />
			<input type="hidden" name="amount" value="<%=amount%>" />
			<input type="hidden" name="bankAbbr" value="<%=bankAbbr%>" />
			<input type="hidden" name="currency" value="<%=currency2%>" />
			<input type="hidden" name="orderDate" value="<%=orderDate%>" />
			<input type="hidden" name="orderId" value="<%=orderId%>" />
			<input type="hidden" name="merAcDate" value="<%=merAcDate%>" />
			<input type="hidden" name="period" value="<%=period%>" />
			<input type="hidden" name="periodUnit" value="<%=periodUnit%>" />
			<input type="hidden" name="merchantAbbr" value="<%=merchantAbbr%>" />
			<input type="hidden" name="productDesc" value="<%=productDesc%>" />
			<input type="hidden" name="productId" value="<%=productId%>" />
			<input type="hidden" name="productName" value="<%=productName%>" />
			<input type="hidden" name="productNum" value="<%=productNum%>" />
			<input type="hidden" name="reserved1" value="<%=reserved1%>" />
			<input type="hidden" name="reserved2" value="<%=reserved2%>" />
			<input type="hidden" name="userToken" value="<%=userToken%>" />
			<input type="hidden" name="showUrl" value="<%=showUrl%>" />
			<input type="hidden" name="couponsFlag" value="<%=couponsFlag%>" />
			<input type="submit" value="确认" />
		</form>
</body>
</html>