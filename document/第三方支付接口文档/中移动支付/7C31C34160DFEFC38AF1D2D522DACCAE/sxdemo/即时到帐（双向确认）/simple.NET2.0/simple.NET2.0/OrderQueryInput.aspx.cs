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
using Com.HisunCmpay;
namespace MCGASPDOTNET
{

    public partial class OrderQueryInput : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
           
        }
        public void Button1_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(orderId.Text))
            {
                orderId.Text = "订单号不能为空";
                return;
            }
            Server.Transfer("OrderQuery.aspx", true);
        }
    }
}
