using System;
using System.Data;
using System.Configuration;
using System.Collections;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using System.Text;
using System.Net;

using Com.HisunCmpay;
using log4net;

namespace MCGASPDOTNET
{

    public partial class DoDirectPaymentConfirmInput : System.Web.UI.Page
    {
        log4net.ILog log = log4net.LogManager.GetLogger("MCGASPDOTNET.DoDirectPaymentConfirmInput");

        protected void Page_Load(object sender, EventArgs e)
        {
            ///页面初始化
            amount.Text = "100";
            period.Text = "7";
            productDesc.Text = "1";
            productId.Text = "2";
            productName.Text = "盛唐食府";
            reserved1.Text = "保留字段1";
            reserved2.Text = "保留字段2";
            userToken.Text = "15116410263";
            showUrl.Text = "www.sliver_fg.com";
            orderDate.Text = string.Format("{0:yyyyMMdd}", DateTime.Now);
            merAcDate.Text = string.Format("{0:yyyyMMdd}", DateTime.Now);
            orderId.Text = IPosMUtil.getTicks();
        }
        public void Button1_Click(object sender, EventArgs e)
        {
            ///获取客户端IP
            String ipAddress = IPosMUtil.getIpAddress();
            
            ///获取参数输入
            String characterSet = GlobalParam.getInstance().characterSet;
            String callbackUrl = GlobalParam.getInstance().callbackUrl;
            String notifyUrl = GlobalParam.getInstance().notifyUrl;
            String merchantId = GlobalParam.getInstance().merchantId;
            String requestId = IPosMUtil.getTicks();
            String signType = GlobalParam.getInstance().signType;
            String type = "DirectPayConfirm";
            String version = GlobalParam.getInstance().version;
            String signKey = GlobalParam.getInstance().signKey;
            String reqUrl = GlobalParam.getInstance().reqUrl;


            ///获取页面输入
            String amount = (String)Request.Form["amount"];
            String bankAbbr = (String)Request.Form["bankAbbr"];
            String currency = (String)Request.Form["currency"];
            String orderDate = (String)Request.Form["orderDate"];
            String merAcDate = (String)Request.Form["merAcDate"];
            String orderId = (String)Request.Form["orderId"];
            String period = (String)Request.Form["period"];
            String periodUnit = (String)Request.Form["periodUnit"];
            String merchantAbbr = (String)Request.Form["merchantAbbr"];
            String productDesc = (String)Request.Form["productDesc"];
            String productId = (String)Request.Form["productId"];
            String productName = (String)Request.Form["productName"];
            String productNum = (String)Request.Form["productNum"];
            String reserved1 = (String)Request.Form["reserved1"];
            String reserved2 = (String)Request.Form["reserved2"];
            String userToken = (String)Request.Form["userToken"];
            String showUrl = (String)Request.Form["showUrl"];
            String couponsFlag = (String)Request.Form["couponsFlag"];
            ///组织发送报文的签名原文
            String signData = characterSet + callbackUrl + notifyUrl
                        + ipAddress + merchantId + requestId + signType + type
                        + version + amount + bankAbbr + currency
                        + orderDate + orderId + merAcDate + period + periodUnit + merchantAbbr
                        + productDesc + productId + productName + productNum
                        + reserved1 + reserved2 + userToken 
                        + showUrl + couponsFlag;
            
            ///生成发送报文的签名
            String reqHmac1 = SignUtil.HmacSign(signData);
            
            String reqHmac = SignUtil.HmacSign(reqHmac1, signKey);
            ///组织支付请求原始报文
            String reqData = "characterSet=" + characterSet + "&callbackUrl="
                        + callbackUrl + "&notifyUrl=" + notifyUrl
                        + "&ipAddress=" + ipAddress + "&merchantId="
                        + merchantId + "&requestId=" + requestId + "&signType="
                        + signType + "&type=" + type + "&version=" + version
                        + "&amount=" + amount + "&bankAbbr=" + bankAbbr
                        + "&currency=" + currency + "&orderDate=" + orderDate
                        + "&orderId=" + orderId + "&merAcDate=" + merAcDate + "&period=" + period
                        + "&periodUnit=" + periodUnit + "&merchantAbbr=" + merchantAbbr 
                        + "&productDesc=" + productDesc + "&productId=" + productId
                        + "&productName=" + productName + "&productNum="
                        + productNum + "&reserved1=" + reserved1
                        + "&reserved2=" + reserved2 + "&userToken=" + userToken
                        + "&showUrl=" + showUrl + "&couponsFlag=" + couponsFlag + "&hmac="+reqHmac;
            
            ///发送支付请求，并接收手机支付平台返回的支付地址
            String recData = IPosMUtil.httpRequest(reqUrl, reqData);
            
            Hashtable ht = IPosMUtil.parseStringToMap(recData);
           
            String recHmac = (String)ht["hmac"];
            String recReturnCode = (String)ht["returnCode"];
            String message = (String)ht["message"];
 
            if ("000000".Equals(recReturnCode))
            {
                ///组织接收报文的签名原文
                String verData = (String)ht["merchantId"] +
                    (String)ht["requestId"] +
                    (String)ht["signType"] +
                    (String)ht["type"] +
                    (String)ht["version"] +
                    recReturnCode +
                    message +
                    (String)ht["payUrl"];
                
                ///验签
                
                Boolean flag = SignUtil.verifySign(verData,signKey,recHmac);
                
                if (flag)
                {
                    String recPayUrl = (String)ht["payUrl"];
                    Response.Redirect(IPosMUtil.getRedirectUrl(recPayUrl));
                }
                else
                {
                    Response.Write("验签失败:");
                    Response.Write("returnCode = " + recReturnCode);
                    Response.Write("&message = " + HttpUtility.UrlDecode(message,Encoding.UTF8));
                }
            }
            else
            {
                Response.Write("下单失败:");
                Response.Write("returnCode = " + recReturnCode + "&");
                Response.Write("message = " + HttpUtility.UrlDecode(message,Encoding.UTF8));
                
            }

         }
    }
}
