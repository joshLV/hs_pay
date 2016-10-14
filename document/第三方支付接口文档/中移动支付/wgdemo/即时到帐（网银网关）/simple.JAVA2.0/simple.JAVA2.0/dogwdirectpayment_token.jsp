<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ page import="com.hisun.iposm.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ page import="java.net.*"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>直接支付(银行网关)GWDirectPay</title>
		<link href="sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//商户会计日期（自定义）
			String merAcDate = sdf.format(new Date()).substring(0, 8);
			
			String type = "GWDirectPay";

			//设置编码
			if ("00".equals(characterSet)) {
				request.setCharacterEncoding("GBK");
			} else if ("01".equals(characterSet)) {
				request.setCharacterEncoding("GB2312");
			} else if ("02".equals(characterSet)) {
				request.setCharacterEncoding("UTF-8");
			}

			String amount = request.getParameter("amount");
			String bankAbbr = request.getParameter("bankAbbr");
			String currency = request.getParameter("currency");
			String orderDate = request.getParameter("orderDate");
			String orderId = request.getParameter("orderId");
			String period = request.getParameter("period");
			String periodUnit = request.getParameter("periodUnit");
			String merchantAbbr = request.getParameter("merchantAbbr");
			String productDesc = request.getParameter("productDesc");
			String productId = request.getParameter("productId");
			String productName = request.getParameter("productName");
			String productNum = request.getParameter("productNum");
			String reserved1 = request.getParameter("reserved1");
			String reserved2 = request.getParameter("reserved2");
			String userToken = request.getParameter("userToken");
			String payType = request.getParameter("payType");
			String showUrl = request.getParameter("showUrl");
			String couponsFlag = request.getParameter("couponsFlag");
			
			out.println(reserved1);

			//数据签名报文
			String signData = characterSet + callbackUrl + notifyUrl
					+ ipAddress + merchantId + requestId + signType + type
					+ version + amount + bankAbbr + currency + orderDate
					+ orderId + merAcDate + period + periodUnit + merchantAbbr
					+ productDesc + productId + productName + productNum
					+ reserved1 + reserved2 + userToken + showUrl + couponsFlag;

			HiiposmUtil util = new HiiposmUtil();
			//数据签名，hmac为签名后的消息摘要
			String hmac = util.MD5Sign(signData, signKey);
		%>
		<form method="post" action="<%=req_url%>">
			<input type="hidden" name="characterSet" value="<%=characterSet%>" />
			<input type="hidden" name="callbackUrl" value="<%=callbackUrl%>" />
			<input type="hidden" name="notifyUrl" value="<%=notifyUrl%>" />
			<input type="hidden" name="ipAddress" value="<%=ipAddress%>" />
			<input type="hidden" name="merchantId" value="<%=merchantId%>" />
			<input type="hidden" name="requestId" value="<%=requestId%>" />
			<input type="hidden" name="signType" value="<%=signType%>" />
			<input type="hidden" name="type" value="<%=type%>" />
			<input type="hidden" name="version" value="<%=version%>" />
			<input type="hidden" name="hmac" value="<%=hmac%>" />
			<input type="hidden" name="amount" value="<%=amount%>" />
			<input type="hidden" name="bankAbbr" value="<%=bankAbbr%>" />
			<input type="hidden" name="currency" value="<%=currency%>" />
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
