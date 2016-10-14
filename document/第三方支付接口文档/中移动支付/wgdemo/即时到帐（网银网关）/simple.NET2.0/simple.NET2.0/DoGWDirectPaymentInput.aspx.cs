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
using System.Net;
using Com.HisunCmpay;

namespace MCGASPDOTNET
{
    public partial class DoGWDirectPaymentInput : System.Web.UI.Page
    {
        log4net.ILog log = log4net.LogManager.GetLogger("MCGASPDOTNET.DoGWDirectPaymentInput");
        protected void Page_Load(object sender, EventArgs e)
        {
            ///页面初始化
            amount.Text = "100";
            period.Text = "7";
            productDesc.Text = "1";
            productId.Text = "2";
            productName.Text = "2";
            reserved1.Text = "保留1";
            reserved2.Text = "字段2";
            userToken.Text = "15116410263";
            orderDate.Text = string.Format("{0:yyyyMMdd}", DateTime.Now);
            merAcDate.Text = string.Format("{0:yyyyMMdd}",DateTime.Now);
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
            String type = "GWDirectPay";
            String version = GlobalParam.getInstance().version;
            String signKey = GlobalParam.getInstance().signKey;
            String reqUrl = GlobalParam.getInstance().reqUrl;

            
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
                        + "&periodUnit=" + periodUnit + "&merchantAbbr=" + merchantAbbr + "&productDesc="
                        + productDesc + "&productId=" + productId
                        + "&productName=" + productName + "&productNum="
                        + productNum + "&reserved1=" + reserved1
                        + "&reserved2=" + reserved2 + "&userToken=" + userToken
                        +  "&showUrl=" + showUrl + "&couponsFlag=" + couponsFlag 
                        + "&hmac=" + reqHmac;
            log.Info(reqData);
            Server.Transfer("DoGWDirectPayment.aspx?"+reqData, true);
        }
    }
}
