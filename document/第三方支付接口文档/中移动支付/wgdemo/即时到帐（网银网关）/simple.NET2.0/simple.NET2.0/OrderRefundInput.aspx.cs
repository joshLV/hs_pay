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

namespace MCGASPDOTNET
{

    public partial class OrderRefundInput : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        public void Button1_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(amount.Text))
            {
                orderId.Text = "退款不能为空";
                return;
            }
            if (String.IsNullOrEmpty(orderId.Text))
            {
                orderId.Text = "原始订单号不能为空";
                return;
            }
            Server.Transfer("OrderRefund.aspx", true);
        }
    }
}
