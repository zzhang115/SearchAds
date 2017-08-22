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

    public static void main(String[] args) {
        if(args.length < 2)
        {
            System.out.println("Usage: AdsServer <adsDataFilePath> <budgetDataFilePath>");
            System.exit(0);
        }
        String adsDataFilePath = args[0];
        String budgetDataFilePath = args[1];

        AdsEngine adsEngine = new AdsEngine(adsDataFilePath, budgetDataFilePath);
        adsEngine.initEngine();
        System.out.println("Ready to take query, please input tokens:");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String query;
            while((query=br.readLine())!=null){
                System.out.println("Search: " + query);
                List<Ad> adsCandidates = adsEngine.selectAds(query);
                for(Ad ad : adsCandidates) {
                    System.out.println("final selected ad id = " + ad.adId);
                    System.out.println("final selected ad rank score = " + ad.rankScore);
                }
            }
        }catch(IOException io){
                io.printStackTrace();
        }
    }
}
