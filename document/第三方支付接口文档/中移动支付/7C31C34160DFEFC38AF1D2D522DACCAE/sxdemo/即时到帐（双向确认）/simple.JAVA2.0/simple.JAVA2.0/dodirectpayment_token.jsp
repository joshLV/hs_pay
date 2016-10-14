<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ page import="com.hisun.iposm.*,java.net.*"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ page import="java.util.HashMap"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>即时到账(双向确认)：DirectPayConfirm</title>
		<link href="sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<%
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//商户会计日期（自定义）
			String merAcDate = sdf.format(new Date()).substring(0, 8);
		
			String type = "DirectPayConfirm";
			String sessionId = "";
			HashMap urlMap = new HashMap();

			//编码设置
			if ("00".equals(characterSet)) {
				request.setCharacterEncoding("GBK");
			} else if ("01".equals(characterSet)) {
				request.setCharacterEncoding("GB2312");
			} else if ("02".equals(characterSet)) {
				request.setCharacterEncoding("UTF-8");
			}

			try {
				
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

				//-- 签名报文
				String signData = characterSet + callbackUrl + notifyUrl
						+ ipAddress + merchantId + requestId + signType + type
						+ version + amount + bankAbbr + currency + orderDate
						+ orderId + merAcDate + period + periodUnit
						+ merchantAbbr + productDesc + productId + productName
						+ productNum + reserved1 + reserved2 + userToken
						+ showUrl + couponsFlag;

				//out.println("签名报文：" + signData + "<br/>" + merAcDate);
				HiiposmUtil util = new HiiposmUtil();
				//数据签名
				String hmac = util.MD5Sign(signData, signKey);

				//-- 请求报文
				String buf = "characterSet=" + characterSet + "&callbackUrl="
						+ callbackUrl + "&notifyUrl=" + notifyUrl
						+ "&ipAddress=" + ipAddress + "&merchantId="
						+ merchantId + "&requestId=" + requestId + "&signType="
						+ signType + "&type=" + type + "&version=" + version
						+ "&amount=" + amount + "&bankAbbr=" + bankAbbr
						+ "&currency=" + currency + "&orderDate=" + orderDate
						+ "&orderId=" + orderId + "&merAcDate=" + merAcDate
						+ "&period=" + period + "&periodUnit=" + periodUnit
						+ "&merchantAbbr=" + merchantAbbr + "&productDesc="
						+ productDesc + "&productId=" + productId
						+ "&productName=" + productName + "&productNum="
						+ productNum + "" + "&reserved1=" + reserved1
						+ "&reserved2=" + reserved2 + "&userToken=" + userToken
						+ "&showUrl=" + showUrl + "&couponsFlag=" + couponsFlag;
				//-- 带上消息摘要
				buf = "hmac=" + hmac + "&" + buf;

				//发起http请求，并获取响应报文
				String res = util.sendAndRecv(req_url, buf, characterSet);
				//获得手机支付平台的消息摘要，用于验签,
				String hmac1 = util.getValue(res, "hmac");
				String vfsign = util.getValue(res, "merchantId")
						+ util.getValue(res, "requestId")
						+ util.getValue(res, "signType")
						+ util.getValue(res, "type")
						+ util.getValue(res, "version")
						+ util.getValue(res, "returnCode")
						+ URLDecoder.decode(util.getValue(res, "message"),
								"UTF-8") + util.getValue(res, "payUrl");

				//响应码
				String code = util.getValue(res, "returnCode");
				//下单交易成功
				if (!code.equals("000000")) {
					out.println("下单错误:" + code + URLDecoder.decode(util.getValue(res,"message"),"UTF-8"));
					return;
				}

				// -- 验证签名
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);

				if (!flag) {
					//request.getSession().setAttribute("message", "验证签名失败！");
					out.println("验签失败");
					return;
				}

				String payUrl = util.getValue(res, "payUrl");
				String submit_url = util.getRedirectUrl(payUrl);

				//RequestDispatcher rd2 = request.getRequestDispatcher("/submit_cmpay.jsp?cmpayUrl="+submit_url+"&SESSIONID="+sessionId);
				//RequestDispatcher rd2 = request.getRequestDispatcher("/submit_cmpay.jsp?sessionId="+sessionId);  
				//out.println("12");
				//rd2.forward(request,response);
				out.println("submit_url:" + submit_url);
				response.sendRedirect(submit_url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>
	</body>
</html>
