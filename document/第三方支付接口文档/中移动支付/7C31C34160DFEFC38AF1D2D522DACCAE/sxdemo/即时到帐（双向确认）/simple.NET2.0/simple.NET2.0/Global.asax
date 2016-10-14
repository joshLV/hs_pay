<%@ Application Language="C#" %>

<script runat="server">

    void Application_Start(object sender, EventArgs e) 
    {
        // 在应用程序启动时运行的代码
        log4net.Config.XmlConfigurator.Configure();

    }
        
</script>
