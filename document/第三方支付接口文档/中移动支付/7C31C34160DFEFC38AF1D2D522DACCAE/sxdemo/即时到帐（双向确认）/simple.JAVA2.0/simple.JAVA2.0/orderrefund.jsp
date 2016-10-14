<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.hisun.iposm.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>API退款</title>
		<link href="sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<%
			String type = "OrderRefund";
			request.setCharacterEncoding("GBK");
			try {
				String amount = request.getParameter("amount");
				String orderId = request.getParameter("orderId");
				
				HiiposmUtil util = new HiiposmUtil();
				
				//-- 签名
				String signData = merchantId + requestId + signType
						+ type + version + orderId + amount;
				String hmac = util.MD5Sign(signData, signKey);
				
				//-- 请求报文
				String buf = "merchantId=" + merchantId + "&requestId=" + requestId
				           + "&signType=" + signType + "&type=" + type
				           + "&version=" + version + "&orderId=" + orderId
				           + "&amount=" + amount;
            buf = "hmac=" + hmac + "&" + buf;
				
            //发起http请求，并获取响应报文
				String res = util.sendAndRecv(req_url, buf, characterSet);

            //手机支付返回报文的消息摘要，用于商户验签
				String hmac1 = util.getValue(res, "hmac");
				String vfsign = util.getValue(res, "merchantId")
				      + util.getValue(res, "payNo")
				      + util.getValue(res, "returnCode")
						+ URLDecoder.decode(util.getValue(res, "message"), "UTF-8")
						+ util.getValue(res, "signType")
						+ util.getValue(res, "type")
						+ util.getValue(res, "version")
						+ util.getValue(res, "amount")
						+ util.getValue(res, "orderId")
						+ util.getValue(res, "status");

				//获取返回码
				String code = util.getValue(res, "returnCode");
				if (!code.equals("000000")) {
					out.println("退款失败："+code+" "+URLDecoder.decode(util.getValue(res,"message"),"UTF-8"));
					return;
				}

				// -- 验证签名
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);

				if (!flag) {
					out.println("验签中心平台失败");
					return;
				}

				out.println("================");
				out.println("</br>");
				out.println("交易成功");
				out.println("</br>");
				out.println("================");
				out.println("</br>");
				out.println("退款金额:" + util.getValue(res, "amount"));
				out.println("</br>");
				out.println("商户订单号:" + util.getValue(res, "orderId"));
				out.println("</br>");
				out.println("退款结果:" + util.getValue(res, "status"));
				out.println("</br>");
			} catch (Exception e) {
				out.println("交易异常:" + e.getMessage());
			}
		%>

	</body>
</html>
