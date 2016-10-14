
<%
	//本地应用上下文地址
	String localAddr = "http://222.240.192.206:8080/S2";
	//编码格式
	String characterSet = "00"; //00--GBK;01--GB2312;02--UTF-8
	//页面通知地址
	String callbackUrl = localAddr + "/callback.jsp";
	//后台通知地址
	String notifyUrl = localAddr + "/notify_url.jsp";
	//获取用户的IP地址，作为防钓鱼的一种方法
	String clientIp = request.getHeader("x-forwarded-for");
	if ((clientIp == null) || (clientIp.length() == 0)
			|| ("unknown".equalsIgnoreCase(clientIp))) {
		clientIp = request.getHeader("Proxy-Client-IP");
	}
	if ((clientIp == null) || (clientIp.length() == 0)
			|| ("unknown".equalsIgnoreCase(clientIp))) {
		clientIp = request.getHeader("WL-Proxy-Client-IP");
	}
	if ((clientIp == null) || (clientIp.length() == 0)
			|| ("unknown".equalsIgnoreCase(clientIp))) {
		clientIp = request.getRemoteAddr();
	}
	String ipAddress = clientIp;
	//商户请求编号
	String requestId = String.valueOf(System.currentTimeMillis());
	String signType = "MD5";
	String version = "2.0.0";
	//商户编号
	String merchantId = "商户号id";
	//商户密钥
	String signKey = "商户密钥";
	String req_url = "https://ipos.10086.cn/ips/cmpayService";
	
%>
