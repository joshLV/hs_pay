<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.hisun.iposm.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>API�˿�</title>
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
				
				//-- ǩ��
				String signData = merchantId + requestId + signType
						+ type + version + orderId + amount;
				String hmac = util.MD5Sign(signData, signKey);
				
				//-- ������
				String buf = "merchantId=" + merchantId + "&requestId=" + requestId
				           + "&signType=" + signType + "&type=" + type
				           + "&version=" + version + "&orderId=" + orderId
				           + "&amount=" + amount;
            buf = "hmac=" + hmac + "&" + buf;
				
            //����http���󣬲���ȡ��Ӧ����
				String res = util.sendAndRecv(req_url, buf, characterSet);

            //�ֻ�֧�����ر��ĵ���ϢժҪ�������̻���ǩ
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

				//��ȡ������
				String code = util.getValue(res, "returnCode");
				if (!code.equals("000000")) {
					out.println("�˿�ʧ�ܣ�"+code+" "+URLDecoder.decode(util.getValue(res,"message"),"UTF-8"));
					return;
				}

				// -- ��֤ǩ��
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);

				if (!flag) {
					out.println("��ǩ����ƽ̨ʧ��");
					return;
				}

				out.println("================");
				out.println("</br>");
				out.println("���׳ɹ�");
				out.println("</br>");
				out.println("================");
				out.println("</br>");
				out.println("�˿���:" + util.getValue(res, "amount"));
				out.println("</br>");
				out.println("�̻�������:" + util.getValue(res, "orderId"));
				out.println("</br>");
				out.println("�˿���:" + util.getValue(res, "status"));
				out.println("</br>");
			} catch (Exception e) {
				out.println("�����쳣:" + e.getMessage());
			}
		%>

	</body>
</html>
