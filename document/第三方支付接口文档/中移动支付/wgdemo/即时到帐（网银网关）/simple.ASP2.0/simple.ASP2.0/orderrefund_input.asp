
<html>
    <head>
        <title>退款</title>
        <link rel="stylesheet" type="text/css" href="css/sdk.css" />
        <meta http-equiv = "Content-Type" content = "text/html; charset=gb2312"/>
    </head>

    <body>
        <form name = "form1" method = "post" action = "orderrefund.asp">
          	<center>
          	<p>
                <input type = "hidden" name = "merCert" value = ""/> 
                <span class="title">退款</span>
            </p>

            <table  class="api">
                 <tr>
                    <td class="field">
                      退款金额
                    </td>

                    <td >
                        <input type = "text" name = "amount" maxlength = '20' value = "10"> <font color="red">*</font>
                    </td>
                </tr>
                <tr>
                    <td class="field">
                      原交易订单号
                    </td>

                    <td >
                        <input type = "text" name = "orderId" maxlength = '20' value = ""> <font color="red">*</font>
                    </td>
                </tr>
                <tr>
      	           <td class="field">
                   </td>
				           <td>
				              <input type = "submit" id = "Submit" value = "提交" name = "submit">
				           </td>
				        </tr>
            </table>
         </center>
         <a id="HomeLink" class="home" href="index.asp">首页</a>
        </form>
    </body>
</html>
