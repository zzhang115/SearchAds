package driver;

import ads.Ad;
import ads.AdsEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by zzc on 8/14/17.
 */
public class Main {
    private static final String MYSQL_HOST_PORT = "127.0.0.1:3306";
    private static final String MYSQL_DB = "searchads";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASS = "238604";

    public static void main(String[] args) {
        if(args.length < 4)
        {
            System.out.println("Usage: AdsServer <adsDataFilePath> <budgetDataFilePath> <memcachedServer> <memcachedPortal>");
            System.exit(0);
        }
        String adsDataFilePath = args[0];
        String budgetDataFilePath = args[1];
        String memcachedServer = args[2];
        int memcachedPortal = Integer.parseInt(args[3]);

        AdsEngine adsEngine = new AdsEngine(adsDataFilePath, budgetDataFilePath, memcachedServer,
                memcachedPortal, MYSQL_HOST_PORT, MYSQL_DB, MYSQL_USER, MYSQL_PASS);
        adsEngine.initEngine();
//        if(adsEngine.init())
//        {
//            System.out.println("Ready to take quey");
//            try{
//                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//                String query;
//                while((query=br.readLine())!=null){
//                    System.out.println(query);
//                    List<Ad> adsCandidates = adsEngine.selectAds(query);
//                    for(Ad ad : adsCandidates)
//                    {
//                        System.out.println("final selected ad id = " + ad.adId);
//                        System.out.println("final selected ad rank score = " + ad.rankScore);
//                    }
//                }
//
//            }catch(IOException io){
//                io.printStackTrace();
//            }
//        } else {
//            System.err.println("Ads Engine initilize failed!");
//        }
    }
}
