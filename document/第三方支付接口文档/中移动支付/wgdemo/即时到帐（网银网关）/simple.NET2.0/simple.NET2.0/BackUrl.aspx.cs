using System;
using System.Collections;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Web;
using System.Text;
using System.Web.SessionState;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.HtmlControls;
using Com.HisunCmpay;
using log4net;

namespace MCGASPDOTNET
{
	/// <summary>
	/// Notify 的摘要说明。
	/// </summary>
	public partial class BackUrl : System.Web.UI.Page
	{
        log4net.ILog log = LogManager.GetLogger("MCGASPDOTNET.BackUrl");
	
		private void Page_Load(object sender, System.EventArgs e)
		{
            Request.ContentEncoding = Encoding.GetEncoding("GBK");
            //后台接收支付结果通知
            NameValueCollection param = Context.Request.Form;
            try
            {
                PerrMsg.Text = "";
                String reqData = IPosMUtil.keyValueToString(param);
                log.Info("reqData = " + reqData);
                Hashtable ht = IPosMUtil.parseStringToMap(reqData);

                // 取得singKey值
                String signKey = GlobalParam.getInstance().signKey;
                //支付结果通知
                String merchantId = (String)ht["merchantId"];
                String payNo = (String)ht["payNo"];
                String returnCode = (String)ht["returnCode"];
                String message = (String)ht["message"];
                String signType = (String)ht["signType"];
                String type = (String)ht["type"];
                String version = (String)ht["version"];
                String amount = (String)ht["amount"];
                String amtItem = (String)ht["amtItem"];
                String bankAbbr = (String)ht["bankAbbr"];
                String mobile = (String)ht["mobile"];
                String orderId = (String)ht["orderId"];
                String payDate = (String)ht["payDate"];
                String accountDate = (String)ht["accountDate"];
                String reserved1 = (String)ht["reserved1"];
                String reserved2 = (String)ht["reserved2"];
                String status = (String)ht["status"];
                String orderDate = (String)ht["orderDate"];
                String fee = (String)ht["fee"];
                String hmac = (String)ht["hmac"];
               

                //进行验签的原文
                String signData = merchantId + payNo + returnCode + message + signType
                      + type + version + amount + amtItem + bankAbbr + mobile
                      + orderId + payDate + accountDate + reserved1 + reserved2 + status
                      + orderDate + fee;

                
                if ("000000".Equals(returnCode))
                {
                    if ("MD5".Equals(GlobalParam.getInstance().signType))
                    {
                        if (SignUtil.verifySign(signData, signKey, hmac))
                        {
                            Pamount.Text = amount;
                            PamtItem.Text = amtItem;
                            PbankAbbr.Text = bankAbbr;
                            Pmobile.Text = mobile;
                            PorderId.Text = orderId;
                            PpayDate.Text = payDate;
                            Preserved1.Text = HttpUtility.UrlDecode(reserved1,Encoding.UTF8);
                            Preserved2.Text = HttpUtility.UrlDecode(reserved2,Encoding.UTF8);
                            Pstatus.Text = status;

                            PorderDate.Text = orderDate;
                            Pfee.Text = fee;

                        }
                        else 
                        {
                            PerrMsg.Text = "签约失败";
                            PreturnCode.Text = returnCode;
                            Pmessage.Text = HttpUtility.UrlDecode(message,Encoding.UTF8);
                        }
                    }
                    
                }
                else
                {
                    PerrMsg.Text = "页面通知失败";
                    PreturnCode.Text = returnCode;
                    Pmessage.Text = HttpUtility.UrlDecode(message,Encoding.UTF8);
                }
            }
            catch(Exception el)
            {
                log.Error("Received Notify failed:" + el.Message);
                PerrMsg.Text = HttpUtility.UrlDecode(el.Message,Encoding.UTF8);
            }
            
		}
	}
}
