<%@  language="VBSCRIPT" codepage="936" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
    <link rel="stylesheet" type="text/css" href="css/sdk.css" />
    <title>直接支付(银行网关)：GWDirectPay</title>
</head>
<%
	Dim currDate
	currYear = Year(Date()) 
	currMonth=Month(Date())
	If(Len(currMonth)=1) Then
		currMonth = "0"&currMonth
	End If
	currDay = Day(Date())
	If(Len(currDay)=1) Then
		currDay = "0"&currDay
	End If
  currDate = currYear&currMonth&currDay
%>
<body>
    <form name="form1" method="post" action="dogwdirectpayment.asp">
        <center>
            <p>
                <input type="hidden" name="merCert" value="" />
                <span class="title">直接支付(银行网关)：GWDirectPay</span>
            </p>
            <table class="api">
               <tr>
						      <td class="field">
						    	订单金额
						      </td>
						      <td>
							       <input type="text" name="amount" maxlength='20' value="1000">
							       <font color="red">*订单金额，以分为单位</font>
						      </td>
					     </tr>
					     <tr>
						      <td class="field">
							    银行代码
						      </td>
						      <td>
							       <select name="bankAbbr">
								        <option value="ICBC">工商银行</option>
								        <option value="CMB">招商银行</option>
								        <option value="CCB">建设银行</option>
								        <option value="ABC">农业银行</option>
								        <option value="BOC">中国银行</option>
								        <option value="SPDB">上海浦东发展银行</option>
								        <option value="BCOM">交通银行</option>
								        <option value="CMBC">民生银行</option>
								        <option value="CEBB">光大银行</option>
								        <option value="GDB">广东发展银行</option>
								        <option value="ECITIC">中信银行</option>
								        <option value="HXB">华夏银行</option>
								        <option value="CIB">兴业银行</option>
						
							       </select>
						     </td>
					   </tr>
					   <tr>
						   <td class="field">
							  币种
						   </td>
						   <td>
							    <select name="currency2">
								     <option value="00">
									   可提现
								     </option>
								     
							    </select>
						   </td>
					  </tr>
					  <tr>
						   <td class="field">
							 订单日期
						   </td>
						   <td>
							   <input type="text" name="orderDate" value="<%=currDate%>">
							   <font color="red">*商户发起请求的时间; 年年年年月月日日</font>
               </td>
					  </tr>
					  <tr>
						   <td class="field">
							 商户订单号
						   </td>
						   <td>
							    <input type="text" name="orderId" value = "<%=currDate&Int(Timer())%>">
							    <font color="red">*</font>
						   </td>
					 </tr>
					  <tr>
               <td class="field">
                  商户会计日期
               </td>
               <td >
                   <input type = "text" name = "merAcDate" value = "<%=currDate%>"> <font color="red">*</font>
               </td>
           </tr>
					 <tr>
						  <td class="field">
							有效期数量
						  </td>
						  <td>
							   <input type="text" name="period" value="7">
							   <font color="red">*数字</font>
						  </td>
					</tr>
          <tr>
             <td class="field">有效期单位</td>
             <td>
                <select name="periodUnit">
                   <option value="00">
                      00-分
                   </option>
                   <option value="01">
                      01-小时
                   </option>
                   <option value="02" selected="true">
                      02-日
                   </option>
                   <option value="03">
                      03-月
                   </option>
                </select>
              </td>
          </tr>
          <tr>
             <td class="field">
                商品展示名称
             </td>

             <td >
                 <input type = "text" name = "merchantAbbr" value = "商品展示名称01"  >
             </td>
          </tr>
					<tr>
						<td class="field">
							商品描述
						</td>
						<td>
							<input type="text" name="productDesc" value="商品描述01">
						</td>
					</tr>
					<tr>
						<td class="field">
							商品编号
						</td>
						<td>
							<input type="text" name="productId" value="商品编号01">
						</td>
					</tr>
					<tr>
						<td class="field">
							商品名称
						</td>
						<td>
							<input type="text" name="productName" value="测试商品01">
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
					   <td class="field">
					     商品数量
					   </td>
					   <td>
					      <input type="text" name="productNum" value="1"/>
					   </td>
					</tr>
					<tr>
						<td class="field">
							保留字段1
						</td>
						<td>
							<input type="text" name="reserved1" value="保留数据1">
						</td>
					</tr>
          <tr>
             <td class="field">
             保留字段2
             </td>
             <td>
                <input type="text" name="reserved2" value="保留数据2">
             </td>
          </tr>
					<tr>
						<td class="field">
							用户标识
						</td>
						<td>
							<input type="text" name="userToken" value="15116410263">
							<font color="red">*</font>
						</td>
					</tr>
          <tr>
             <td class="field">商品展示地址</td>
             <td>
                <input type="text" name="showUrl" value=""/>
             </td>
          </tr>
          <tr>
             <td class="field">营销工具使用控制</td>
             <td>
                 <select name="couponsFlag">
                    <option value="00">00-使用全部营销工具 </option>
                    <option value="10">10-不支持使用电子券 </option>
                    <option value="20">20-不支持代金券 </option>
                    <option value="30">30-不支持积分</option>
                    <option value="40">40-不支持所有营销工具 </option>
                 </select>
             </td>
          </tr>
					<tr>
						<td class="field">
						</td>
						<td>
							<input type="Submit" value="提交" id="Submit" name="submit" />
						</td>
					</tr>
       </table>
    </center>
    <a id="HomeLink" class="home" href="index.asp">首页</a>
    </form>
</body>
</html>
