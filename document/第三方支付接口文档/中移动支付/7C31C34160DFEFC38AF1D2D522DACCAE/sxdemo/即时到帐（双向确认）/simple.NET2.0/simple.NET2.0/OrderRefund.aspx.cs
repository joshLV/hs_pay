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
using Com.HisunCmpay;

namespace MCGASPDOTNET
{
    public partial class OrderRefund : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            try{
            //页面初始化
            PerrMsg.Text = "";
            //页面传输参数
            String orderId = (String)Request.Form["orderId"];
            String amount = (String)Request.Form["amount"];
           
            ///获取参数输入
            String merchantId = GlobalParam.getInstance().merchantId;
            String requestId = IPosMUtil.getTicks();
            String signType = GlobalParam.getInstance().signType;
            String type = "OrderRefund";
            String version = GlobalParam.getInstance().version;
            String signKey = GlobalParam.getInstance().signKey;
            String reqUrl = GlobalParam.getInstance().reqUrl;

            ///组织发送报文的签名原文
            String signData = merchantId + requestId + signType + type
                        + version + orderId + amount;
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
                              "&amount=" + amount +
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
                        (String)ht["orderId"] +
                        (String)ht["status"];
                        
                    ///验签

                    Boolean flag = SignUtil.verifySign(verData, signKey, recHmac);
                    if (flag)
                    {
                        Pamount.Text = (String)ht["amount"];
                        PorderId.Text = (String)ht["orderId"];
                        Pstatus.Text = (String)ht["status"];
                    }
                    else
                    {
                        PerrMsg.Text = "验签失败";
                        PreturnCode.Text = recReturnCode;
                        Pmessage.Text = HttpUtility.UrlDecode((String)ht["message"], Encoding.GetEncoding("UTF-8"));
                    }
                }
                else
                {
                    PerrMsg.Text = "退款失败";
                    PreturnCode.Text = recReturnCode;
                    Pmessage.Text = HttpUtility.UrlDecode((String)ht["message"], Encoding.GetEncoding("UTF-8"));
                }

            }
            catch (Exception el)
            {
                PerrMsg.Text = el.Message;
            }
        }

       }
    }
