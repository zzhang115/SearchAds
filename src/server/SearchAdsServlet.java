package server;

import ads.Ad;
import ads.AdsEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by zzc on 8/13/17.
 */
// http://localhost:8080/SearchAds_war_exploded/SearchAdsServlet
@WebServlet("/SearchAds")
public class SearchAdsServlet extends HttpServlet {
    private ServletConfig config = null;
    private AdsEngine adsEngine = null;
    private String uiTemplate = "";
    private String adTemplate = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext application = config.getServletContext();
        String adsDataFilePath = application.getInitParameter("adsDataFilePath");
        System.out.println("Path:"+adsDataFilePath);
        String budgetDataFilePath = application.getInitParameter("budgetDataFilePath");
        String uiTemplateFilePath = application.getInitParameter("uiTemplateFilePath");
        String adTemplateFilePath = application.getInitParameter("adTemplateFilePath");

        adsEngine = new AdsEngine(adsDataFilePath, budgetDataFilePath);
        adsEngine.initEngine();
        System.out.println("adsEngine initilized");
        //load UI template
        try {
            byte[] uiData;
            byte[] adData;
            uiData = Files.readAllBytes(Paths.get(uiTemplateFilePath));
            uiTemplate = new String(uiData, StandardCharsets.UTF_8);
            adData = Files.readAllBytes(Paths.get(adTemplateFilePath));
            adTemplate = new String(adData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        List<Ad> adsCandidates = adsEngine.selectAds(query);
        String result = uiTemplate;
        String list = "";
        for(Ad ad : adsCandidates)
        {
            System.out.println("final selected ad id = " + ad.adId);
            System.out.println("final selected ad rank score = " + ad.rankScore);
            String adContent = adTemplate;
            adContent = adContent.replace("$title$", ad.title);
            adContent = adContent.replace("$brand$", ad.brand);
            adContent = adContent.replace("$img$", ad.thumbnail);
            adContent = adContent.replace("$link$", ad.detailUrl);
            adContent = adContent.replace("$price$", Double.toString(ad.price));
            //System.out.println("adContent: " + adContent);
            list = list + adContent;
        }
        result = result.replace("$list$", list);
        //System.out.println("list: " + list);
        //System.out.println("RESULT: " + result);
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().write(result);
    }
}
