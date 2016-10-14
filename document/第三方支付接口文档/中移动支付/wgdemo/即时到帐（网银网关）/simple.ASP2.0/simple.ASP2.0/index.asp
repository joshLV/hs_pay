<%@LANGUAGE="VBSCRIPT" CODEPAGE="936"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>无标题文档</title>
</head>

<body>
<%
		Dim merchantId
		merchantId = Request("merchantId") 

		If merchantId = "" Then 
			merchantId = "888073157340001"
		End If
		Session("merchantId") = merchantId
%>
<table width = "75%" border = "0" align = "center">
   <tr>
       <td>
           <div align = "center">
               <p>
                   <font color = "#0000FF">Hisun IPOS</font>
               </p>
   
               <p>
                   <font color = "#0000FF">商户[<%Response.Write(merchantId)%>]联机交易演示</font>
               </p>
   
               <hr>
           </div>
       </td>
   </tr>
   <tr>
      <td align = "center">
          <form action="index.asp">
          商户号: <input name="merchantId" value="<%=merchantId%>"/>
                  <input type="submit" value="设置商户号"/>
          </form>
      </td>
   </tr>
       <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dogwdirectpayment_input.asp">直接支付(银行网关)</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dodirectpayment_token_input.asp">直接支付(双向确认)</a></font>
           </div>
       </td>
   </tr>
       <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dowapdirectpayment_token_input.asp">直接支付(WAP)</a></font>
           </div>
       </td>
   </tr>
    <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "dodirectpayment_sms_input.asp">直接支付(短信)</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
               <font color = "#000000"><a href = "ordersearch_input.asp">订单查询</a></font>
           </div>
       </td>
   </tr>
   <tr>
       <td>
           <div align = "center">
                <font color = "#000000"><a href = "orderrefund_input.asp">退款</a></font>
           </div>
       </td>
   </tr> 
</table> 
</body>
</html>