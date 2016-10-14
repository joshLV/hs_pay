using System;
using System.Data;
using System.Text;
using System.Configuration;
using System.Collections;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using Com.HisunCmpay;

namespace MCGASPDOTNET
{

    public partial class OrderQuery : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            try
            {
                PerrMsg.Text = "";
                //获取页面输入
                String orderId = Request.Form["orderId"];
                ///获取参数输入
                
                String callbackUrl = GlobalParam.getInstance().callbackUrl;
                String notifyUrl = GlobalParam.getInstance().notifyUrl;
                String merchantId = GlobalParam.getInstance().merchantId;
                String requestId = IPosMUtil.getTicks();
                String signType = GlobalParam.getInstance().signType;
                String type = "OrderQuery";
                String version = GlobalParam.getInstance().version;
                String signKey = GlobalParam.getInstance().signKey;
                String reqUrl = GlobalParam.getInstance().reqUrl;

                ///组织发送报文的签名原文
                String signData = merchantId + requestId + signType + type
                            + version + orderId;
                ///生成发送报文的签名
                String reqHmac1 = SignUtil.HmacSign(signData);

                String reqHmac = SignUtil.HmacSign(reqHmac1, signKey);

                ///组织支付请求原始报文
                String reqData = "merchantId=" + merchantId +
                                  "&requestId=" + requestId +
                                  "&signType=" + signType +
                                  "&type=" + type +
                                  "&version=" + version +
                                  "&orderId=" + orderId +
                                  "&hmac=" + reqHmac;

                ///发送支付请求，并接收手机支付平台返回的支付地址
                String recData = IPosMUtil.httpRequest(reqUrl, reqData);

                Hashtable ht = IPosMUtil.parseStringToMap(recData);
                String recHmac = (String)ht["hmac"];
                String recReturnCode = (String)ht["returnCode"];

                if ("000000".Equals(recReturnCode))
                {
                    ///组织接收报文的签名原文
                    String verData = (String)ht["merchantId"] +
                        (String)ht["payNo"] +
                        (String)ht["returnCode"] +
                        (String)ht["message"] +
                        (String)ht["signType"] +
                        (String)ht["type"] +
                        (String)ht["version"] +
                        (String)ht["amount"] +
                        (String)ht["amtItem"] +
                        (String)ht["bankAbbr"] +
                        (String)ht["mobile"] +
                        (String)ht["orderId"] +
                        (String)ht["payDate"] +
                        (String)ht["reserved1"] +
                        (String)ht["reserved2"] +
                        (String)ht["status"] +
                        (String)ht["orderDate"] +
                        (String)ht["fee"];
                    ///验签

                    Boolean flag = SignUtil.verifySign(verData, signKey, recHmac);
                    if (flag)
                    {
                        Pamount.Text = (String)ht["amount"];
                        PamtItem.Text = (String)ht["amtItem"];
                        PbankAbbr.Text = (String)ht["bankAbbr"];
                        Pmobile.Text = (String)ht["mobile"];
                        PorderId.Text = (String)ht["orderId"];
                        PpayDate.Text = (String)ht["payDate"];
                        Preserved1.Text = HttpUtility.UrlDecode((String)ht["reserved1"],Encoding.UTF8);
                        Preserved2.Text = HttpUtility.UrlDecode((String)ht["reserved2"],Encoding.UTF8);
                        Pstatus.Text = (String)ht["status"];
                        PorderDate.Text = (String)ht["orderDate"];
                        Pfee.Text = (String)ht["fee"];

                    }
                    else
                    {
                        PerrMsg.Text = "验签失败";
                        PreturnCode.Text = recReturnCode;
                        Pmessage.Text = HttpUtility.UrlDecode((String)ht["message"], Encoding.UTF8);
                    }
                }
                else
                {
                    PerrMsg.Text = "查询订单失败";
                    PreturnCode.Text = recReturnCode;
                    Pmessage.Text = HttpUtility.UrlDecode((String)ht["message"], Encoding.UTF8);
                }

            }
            catch (Exception el)
            {
                PerrMsg.Text = el.Message;
            }
        }
    }
}
