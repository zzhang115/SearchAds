package servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zzc on 8/13/17.
 */
// http://localhost:8080/SearchAds_war_exploded/SearchAdsServlet
@WebServlet("/SearchAds")
public class SearchAdsServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext application = config.getServletContext();
        String adsDataFilePath = application.getInitParameter("adsDataFilePath");
        String budgetDataFilePath = application.getInitParameter("budgetDataFilePath");
        String uiTemplateFilePath = application.getInitParameter("uiTemplateFilePath");
        String adTemplateFilePath = application.getInitParameter("adTemplateFilePath");
        String memcachedServer = application.getInitParameter("memcachedServer");
        String mysqlHost = application.getInitParameter("mysqlHost");
        String mysqlDb = application.getInitParameter("mysqlDB");
        String mysqlUser = application.getInitParameter("mysqlUser");
        String mysqlPass = application.getInitParameter("mysqlPass");
        int memcachedPortal = Integer.parseInt(application.getInitParameter("memcachedPortal"));

//        this.adsEngine = new AdsEngine(adsDataFilePath,budgetDataFilePath,memcachedServer,memcachedPortal,mysqlHost,mysqlDb,mysqlUser,mysqlPass);
//        this.adsEngine.init();
//        System.out.println("adsEngine initilized");
//        //load UI template
//        try {
//            byte[] uiData;
//            byte[] adData;
//            uiData = Files.readAllBytes(Paths.get(uiTemplateFilePath));
//            uiTemplate = new String(uiData, StandardCharsets.UTF_8);
//            adData = Files.readAllBytes(Paths.get(adTemplateFilePath));
//            adTemplate = new String(adData, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("this is test web page");
    }
}
