<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ page import="java.net.*,com.hisun.iposm.HiiposmUtil"%>

		<%
			String signKey = "�̻���Կ";
				
			try {
				//��ȡ֪ͨ����request�еĲ���
				String merchantId = request.getParameter("merchantId");
				String payNo = request.getParameter("payNo");
				String returnCode = request.getParameter("returnCode");
				String message = request.getParameter("message");
				String signType = request.getParameter("signType");
				String type = request.getParameter("type");
				String version = request.getParameter("version");
				String amount = request.getParameter("amount");
				String amtItem = request.getParameter("amtItem");
				String bankAbbr = request.getParameter("bankAbbr");
				String mobile = request.getParameter("mobile");
				String orderId = request.getParameter("orderId");
				String payDate = request.getParameter("payDate");
				String reserved1 = request.getParameter("reserved1");
				String reserved2 = request.getParameter("reserved2");
				String status = request.getParameter("status");
				String orderDate = request.getParameter("orderDate");
				String fee = request.getParameter("fee");
				String hmac = request.getParameter("hmac");
				String accountDate = request.getParameter("accountDate");
				
				//�����ֶηǿ���֤
				if (merchantId == null) {
					merchantId = "";
				}
				if (payNo == null) {
					payNo = "";
				}
				if (returnCode == null) {
					returnCode = "";
				}
				if (message == null) {
					message = "";
				}
				if (signType == null ) {
					signType = "MD5";
				}
				if (type == null) {
					type = "";
				}
				if (version == null) {
					version = "";
				}
				if (amount == null) {
					amount = "";
				}
				if (amtItem == null) {
					amtItem = "";
				}
				if (bankAbbr == null) {
					bankAbbr = "";
				}
				if (mobile == null) {
					mobile = "";
				}
				if (orderId == null) {
					orderId = "";
				}
				if (payDate == null) {
					payDate = "";
				}
				if (reserved1 == null) {
					reserved1 = "";
				}
				if (reserved2 == null) {
					reserved2 = "";
				}
				if (status == null) {
					status = "";
				}
				if (orderDate == null) {
					orderDate = "";
				}
				if (fee == null) {
					fee = "";
				}
				if (hmac == null) {
					hmac = "";
				}
				if (accountDate == null){
               accountDate = "";
            }


				//��֯��ǩ����
			   String signData = merchantId + payNo + returnCode + message + signType
						+ type + version + amount + amtItem + bankAbbr + mobile
						+ orderId + payDate + accountDate + reserved1 + reserved2 + status
						+ orderDate + fee;
	         
            HiiposmUtil util = new HiiposmUtil();
			   //out.println("��ǩ���ģ�"+signData+"<br/>");
				String hmac1 = util.MD5Sign(signData, signKey);
				//out.println("��ϢժҪ��PAGE����"+hmac1+"<br/>");
				//out.println("������ժҪ��"+hmac+"<br/>");
				
				//��ǩ
				boolean sign_flag = util.MD5Verify(signData,hmac,signKey);


				if (sign_flag) {
					//��ǩ�ɹ�,�̻���ҵ���߼�����...
               //......
               //����ɹ������ֻ�֧��ƽ̨���ͽ��յ���̨֪ͨ�ɹ�����Ϣ����ִ�����£���ע���벻Ҫ��out.println��������Ϣ��
					response.getWriter().print("SUCCESS");	
				} else {
					System.out.println("��ǩʧ�ܣ�");//
				}
				
			} catch (Exception e) {
				System.out.println("�����쳣:" + e.getMessage());//
			}
		%>

