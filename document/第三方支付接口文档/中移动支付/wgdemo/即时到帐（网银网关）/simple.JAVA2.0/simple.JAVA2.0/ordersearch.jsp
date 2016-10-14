<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.hisun.iposm.*"%>
<%@ page import="javax.servlet.RequestDispatcher"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ page import="java.util.HashMap"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>API订单查询</title>
		<link href="sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<%
			String type = "OrderQuery";

			request.setCharacterEncoding("GBK");
			try {
				String orderId = request.getParameter("orderId");

				//-- 签名
				String signData = merchantId + requestId + signType + type
						+ version + orderId;

				HiiposmUtil util = new HiiposmUtil();
				String hmac = util.MD5Sign(signData, signKey);

				//-- 请求报文
				String buf = "merchantId=" + merchantId + "&requestId="
						+ requestId + "&signType=" + signType + "&type=" + type
						+ "&version=" + version + "&orderId=" + orderId;
				buf = "hmac=" + hmac + "&" + buf;

				//发起http请求，并获取响应报文
				String res = util.sendAndRecv(req_url, buf, characterSet);

				//获取手机支付平台返回的签名消息摘要，用来验签
				String hmac1 = util.getValue(res, "hmac");

				String vfsign = util.getValue(res, "merchantId")
						+ util.getValue(res, "payNo")
						+ util.getValue(res, "returnCode")
						+ URLDecoder.decode(util.getValue(res, "message"),
								"UTF-8")
						+ util.getValue(res, "signType")
						+ util.getValue(res, "type")
						+ util.getValue(res, "version")
						+ util.getValue(res, "amount")
						+ util.getValue(res, "amtItem")
						+ util.getValue(res, "bankAbbr")
						+ util.getValue(res, "mobile")
						+ util.getValue(res, "orderId")
						+ util.getValue(res, "payDate")
						+ URLDecoder.decode(util.getValue(res, "reserved1"),
								"UTF-8")
						+ URLDecoder.decode(util.getValue(res, "reserved2"),
								"UTF-8") + util.getValue(res, "status")
						+ util.getValue(res, "payType")
						+ util.getValue(res, "orderDate")
						+ util.getValue(res, "fee");

				String code = util.getValue(res, "returnCode");
				if (!code.equals("000000")) {
					out.println("错误码：" + code);
					out.println("错误信息："
							+ URLDecoder.decode(util.getValue(res, "message"),
									"UTF-8"));
					return;
				}

				// -- 验证签名
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);

				if (!flag) {
					out.println("错误码：" + code);
					out.println("错误信息："
							+ URLDecoder.decode(util.getValue(res, "message"),
									"UTF-8"));
					return;
				}

				out.println("================");
				out.println("<br/>");
				out.println("交易成功");
				out.println("<br/>");
				out.println("================");
				out.println("商户编号：" + util.getValue(res, "merchantId"));
				out.println("<br/>");
				out.println("流水号:" + util.getValue(res, "payNo"));
				out.println("<br/>");
				out.println("返回码:" + util.getValue(res, "returnCode"));
				out.println("<br/>");
				out.println("返回码描述信息:" + util.getValue(res, "message"));
				out.println("<br/>");
				out.println("签名方式：<font color='red'>"
						+ util.getValue(res, "signType") + "</font>");
				out.println("<br/>");
				out.println("接口类型：" + util.getValue(res, "type"));
				out.println("<br/>");
				out.println("版本号：" + util.getValue(res, "version"));
				out.println("<br/>");
				out.println("支付金额：" + util.getValue(res, "amount"));
				out.println("<br/>");
				out.println("金额 明细：" + util.getValue(res, "amtItem"));
				out.println("<br/>");
				out.println("支付银行：" + util.getValue(res, "bankAbbr"));
				out.println("<br/>");
				out.println("支付手机号：" + util.getValue(res, "mobile"));
				out.println("<br/>");
				out.println("商户订单号：" + util.getValue(res, "orderId"));
				out.println("<br/>");
				out.println("支付时间：" + util.getValue(res, "payDate"));
				out.println("<br/>");
				out.println("保留字段1：" + util.getValue(res, "reserved1"));
				out.println("<br/>");
				out.println("保留字段2：" + util.getValue(res, "reserved2"));
				out.println("<br/>");
				out.println("支付结果：" + util.getValue(res, "status"));
				out.println("<br/>");
				out.println("支付方式：" + util.getValue(res, "payType"));
				out.println("<br/>");
				out.println("订单日期：" + util.getValue(res, "orderDate"));
				out.println("<br/>");
				out.println("费用：" + util.getValue(res, "fee"));
				out.println("<br/>");
				out.println("签名数据：" + util.getValue(res, "hmac"));
				out.println("<br/>");
				out.println("" + util.getValue(res, ""));
				out.println("<br/>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>

	</body>
</html>
