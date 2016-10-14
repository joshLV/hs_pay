<%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!--#include file ="params.asp"-->
<!--#include file ="publicFunction.asp"-->
<%        
          '获取中心平台传递过来的数据
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
          
          '创建动态链接库客户端对象
          Set util = Server.CreateObject("IPOSM.SignUtil")
          
          '设置日志路径
		      util.SetLogPath(logPath)
		      
		      '用密钥进行初始化		      
		      ret = util.Init(signKey)
		      
		      '初始化成功  
		      If(ret = 0) Then 
		      	 '设置验签数据   
		         dim vfsign
		         vfsign = merchantId&payNo&returnCode&message&signType&type2&version&amount&amtItem&bankAbbr&mobile&orderId&payDate&accountDate&reserved1&reserved2&status1&orderDate&fee
             
             '进行验签
             dim rec
             rec = util.Verify(vfsign,hmac1)
             
             If(rec<>0) Then
                Response.Write("验签失败</br>")
                Response.End
             End If
		         
		         Response.Write("SUCCESS")
			    End If    
%>