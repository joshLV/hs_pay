<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.hisun.iposm.*"%>
<%@ page import="javax.servlet.RequestDispatcher"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.Date,java.text.*"%>
<%@ page import="java.util.HashMap"%>
<%@ include file="globalParam.jsp"%>
<html>
	<head>
		<title>API������ѯ</title>
		<link href="sdk.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	</head>

	<body>
		<%
			String type = "OrderQuery";

			request.setCharacterEncoding("GBK");
			try {
				String orderId = request.getParameter("orderId");

				//-- ǩ��
				String signData = merchantId + requestId + signType + type
						+ version + orderId;

				HiiposmUtil util = new HiiposmUtil();
				String hmac = util.MD5Sign(signData, signKey);

				//-- ������
				String buf = "merchantId=" + merchantId + "&requestId="
						+ requestId + "&signType=" + signType + "&type=" + type
						+ "&version=" + version + "&orderId=" + orderId;
				buf = "hmac=" + hmac + "&" + buf;

				//����http���󣬲���ȡ��Ӧ����
				String res = util.sendAndRecv(req_url, buf, characterSet);

				//��ȡ�ֻ�֧��ƽ̨���ص�ǩ����ϢժҪ��������ǩ
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
					out.println("�����룺" + code);
					out.println("������Ϣ��"
							+ URLDecoder.decode(util.getValue(res, "message"),
									"UTF-8"));
					return;
				}

				// -- ��֤ǩ��
				boolean flag = false;
				flag = util.MD5Verify(vfsign, hmac1, signKey);

				if (!flag) {
					out.println("�����룺" + code);
					out.println("������Ϣ��"
							+ URLDecoder.decode(util.getValue(res, "message"),
									"UTF-8"));
					return;
				}

				out.println("================");
				out.println("<br/>");
				out.println("���׳ɹ�");
				out.println("<br/>");
				out.println("================");
				out.println("�̻���ţ�" + util.getValue(res, "merchantId"));
				out.println("<br/>");
				out.println("��ˮ��:" + util.getValue(res, "payNo"));
				out.println("<br/>");
				out.println("������:" + util.getValue(res, "returnCode"));
				out.println("<br/>");
				out.println("������������Ϣ:" + util.getValue(res, "message"));
				out.println("<br/>");
				out.println("ǩ����ʽ��<font color='red'>"
						+ util.getValue(res, "signType") + "</font>");
				out.println("<br/>");
				out.println("�ӿ����ͣ�" + util.getValue(res, "type"));
				out.println("<br/>");
				out.println("�汾�ţ�" + util.getValue(res, "version"));
				out.println("<br/>");
				out.println("֧����" + util.getValue(res, "amount"));
				out.println("<br/>");
				out.println("��� ��ϸ��" + util.getValue(res, "amtItem"));
				out.println("<br/>");
				out.println("֧�����У�" + util.getValue(res, "bankAbbr"));
				out.println("<br/>");
				out.println("֧���ֻ��ţ�" + util.getValue(res, "mobile"));
				out.println("<br/>");
				out.println("�̻������ţ�" + util.getValue(res, "orderId"));
				out.println("<br/>");
				out.println("֧��ʱ�䣺" + util.getValue(res, "payDate"));
				out.println("<br/>");
				out.println("�����ֶ�1��" + util.getValue(res, "reserved1"));
				out.println("<br/>");
				out.println("�����ֶ�2��" + util.getValue(res, "reserved2"));
				out.println("<br/>");
				out.println("֧�������" + util.getValue(res, "status"));
				out.println("<br/>");
				out.println("֧����ʽ��" + util.getValue(res, "payType"));
				out.println("<br/>");
				out.println("�������ڣ�" + util.getValue(res, "orderDate"));
				out.println("<br/>");
				out.println("���ã�" + util.getValue(res, "fee"));
				out.println("<br/>");
				out.println("ǩ�����ݣ�" + util.getValue(res, "hmac"));
				out.println("<br/>");
				out.println("" + util.getValue(res, ""));
				out.println("<br/>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>

	</body>
</html>
