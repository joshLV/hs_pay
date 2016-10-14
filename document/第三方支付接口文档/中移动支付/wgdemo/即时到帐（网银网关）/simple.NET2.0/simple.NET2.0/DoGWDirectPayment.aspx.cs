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
    public partial class DoGWDirectPayment : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            //页面初始化
            
            ///获取参数输入
            form1.Attributes.Add("action", GlobalParam.getInstance().reqUrl);
            characterSet.Value = Request.QueryString["characterSet"];
            callbackUrl.Value = Request.QueryString["callbackUrl"];
            notifyUrl.Value = Request.QueryString["notifyUrl"];
            ipAddress.Value = Request.QueryString["ipAddress"];
            merchantId.Value = Request.QueryString["merchantId"];
            requestId.Value = Request.QueryString["requestId"];
            signType.Value = Request.QueryString["signType"];
            type.Value = Request.QueryString["type"];
            version.Value = Request.QueryString["version"];
            amount.Value = Request.QueryString["amount"];
            bankAbbr.Value = Request.QueryString["bankAbbr"];
            currency.Value = Request.QueryString["currency"];
            orderDate.Value = Request.QueryString["orderDate"];
            orderId.Value = Request.QueryString["orderId"];
            merAcDate.Value = Request.QueryString["merAcDate"];
            period.Value = Request.QueryString["period"];
            periodUnit.Value = Request.QueryString["periodUnit"];
            merchantAbbr.Value = Request.QueryString["merchantAbbr"];
            productDesc.Value = Request.QueryString["productDesc"];
            productId.Value = Request.QueryString["productId"];
            productName.Value = Request.QueryString["productName"];
            productNum.Value = Request.QueryString["productNum"];
            reserved1.Value = Request.QueryString["reserved1"];
            reserved2.Value = Request.QueryString["reserved2"];
            userToken.Value = Request.QueryString["userToken"];

            showUrl.Value = Request.QueryString["showUrl"];
            couponsFlag.Value = Request.QueryString["couponsFlag"];
            hmac.Value = Request.QueryString["hmac"];

        }
    
    }
}
