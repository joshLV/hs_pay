<%
	characterSet = "00" 
	callbackUrl = "http://222.240.192.205:60011/back_url.asp"
	notifyUrl = "http://222.240.192.205:60011/notifyUrl.asp"
	requestId = now()
	signType = "MD5"
	version = "2.0.0"
  logPath = Server.MapPath(".")&"\\log1"
  merchantId = "商户号"
  signKey = "商户密钥"
  req_url = "https://ipos.10086.cn/ips/cmpayService"
%>