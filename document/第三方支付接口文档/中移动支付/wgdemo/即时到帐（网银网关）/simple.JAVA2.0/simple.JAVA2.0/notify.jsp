<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ page import="java.net.*,com.hisun.iposm.HiiposmUtil"%>

		<%
			String signKey = "商户密钥";
				
			try {
				//获取通知请求request中的参数
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
				
				//必输字段非空验证
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


				//组织验签报文
			   String signData = merchantId + payNo + returnCode + message + signType
						+ type + version + amount + amtItem + bankAbbr + mobile
						+ orderId + payDate + accountDate + reserved1 + reserved2 + status
						+ orderDate + fee;
	         
            HiiposmUtil util = new HiiposmUtil();
			   //out.println("验签报文："+signData+"<br/>");
				String hmac1 = util.MD5Sign(signData, signKey);
				//out.println("消息摘要（PAGE）："+hmac1+"<br/>");
				//out.println("传来的摘要："+hmac+"<br/>");
				
				//验签
				boolean sign_flag = util.MD5Verify(signData,hmac,signKey);


				if (sign_flag) {
					//验签成功,商户的业务逻辑处理...
               //......
               //处理成功，向手机支付平台发送接收到后台通知成功的信息；请执行如下：（注：请不要在out.println其他的信息）
					response.getWriter().print("SUCCESS");	
				} else {
					System.out.println("验签失败！");//
				}
				
			} catch (Exception e) {
				System.out.println("交易异常:" + e.getMessage());//
			}
		%>

