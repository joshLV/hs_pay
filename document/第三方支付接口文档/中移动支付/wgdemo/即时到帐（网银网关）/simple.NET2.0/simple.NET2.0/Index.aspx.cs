using System;
using System.Data;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;
using Com.HisunCmpay;

namespace MCGASPDOTNET
{

    public partial class Index : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
           
           string merchantId = GlobalParam.getInstance().merchantId;
           label1.Text = merchantId;
           merchantId1.Value = merchantId;  
        }
    }
}
