package ads;

import sqlaccess.SQLAccess;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zzc on 8/16/17.
 */
public class AdsSelector {
    private static AdsSelector instance = null;
    private static final String MEMCACHEDSERVER = "127.0.0.1";
    private static final int MEMCACHEDPORT = 11211; // we can use differenct port for different memecached
    private static final int MEMCACHED_FEATURE_PORT = 11213; // we can use differenct port for different memecached

    public List<Ad> selectAds(String deviceId, String deviceIp, String queryCategory, List<String> queryTokens) {
        SQLAccess sqlAccess = new SQLAccess();
        List<Ad> adList = new ArrayList<Ad>();
        HashMap<Long, Integer> matchedAdsMap = new HashMap<Long, Integer>();

        try {
            MemcachedClient client = new MemcachedClient(new InetSocketAddress(MEMCACHEDSERVER, MEMCACHEDPORT));
            for (String token : queryTokens) {
                System.out.println("Select Ads queryToken: " + token);
                Set<Long> adIdList = (Set<Long>) client.get(token);
                if (adIdList != null && adIdList.size() > 0) {
                    for (Object adId : adIdList) {
                        if (matchedAdsMap.keySet().contains(adId)) {
                            matchedAdsMap.put((Long) adId, matchedAdsMap.get((Long) adId) + 1);
                        } else {
                            matchedAdsMap.put((Long) adId, 1);
                        }
                    }
                }
            }
            for (Long adId : matchedAdsMap.keySet()) {
                System.out.println("Selected AdID: " + adId + "token selected: "+ matchedAdsMap.get(adId) + " times");
                Ad ad = sqlAccess.getAdData(adId);
                double relevanceScore = (double) (matchedAdsMap.get(adId) * 1.0 / ad.keyWords.size());
                ad.relevanceScore = relevanceScore;
                adList.add(ad);
            }
            sqlAccess.connectionClose();

            MemcachedClient client2 = new MemcachedClient(new InetSocketAddress(MEMCACHEDSERVER, MEMCACHED_FEATURE_PORT));
            for (Ad ad : adList) {
                ArrayList<Double> features = new ArrayList<Double>();

                // device ip click
                String deviceIpClickKey = "dipc_" + deviceIp;
                String deviceIpClickVal = (String) client2.get(deviceIpClickKey);
                Double deviceIpClickValue = 0.0;
                if (deviceIpClickVal != null && deviceIpClickVal != "") {
                    deviceIpClickValue = Double.parseDouble(deviceIpClickVal);
                }
                features.add(deviceIpClickValue);
                System.out.println("deviceIpClickKey:"+deviceIpClickKey + " deviceIpClickValue:" + deviceIpClickValue);

                // device ip impression
                String deviceIpImpressionKey = "dipi_" + deviceIp;
                String deviceIpImpressionVal = (String) client2.get(deviceIpImpressionKey);
                Double deviceIpImpressionValue = 0.0;
                if (deviceIpImpressionVal != null && deviceIpImpressionVal != "") {
                    deviceIpImpressionValue = Double.parseDouble(deviceIpImpressionVal);
                }
                features.add(deviceIpImpressionValue);
                System.out.println("deviceIpImpressionKey:"+ deviceIpImpressionKey + " deviceIpImpressionValue:" + deviceIpImpressionValue);

                // ad id click
                String adIdClickKey = "aidc_" + String.valueOf(ad.adId);
                String adIdClickVal = (String) client2.get(adIdClickKey);
                Double adIdClickValue = 0.0;
                if (adIdClickVal != null && adIdClickVal != "") {
                    adIdClickValue = Double.parseDouble(adIdClickVal);
                }
                features.add(adIdClickValue);
                System.out.println("adIdClickKey:"+ adIdClickKey + " adIdClickValue:" + adIdClickValue);

                // ad imp
                String adIdImpressionKey = "aidi" + String.valueOf(ad.adId);
                String adIdImpressionVal = (String) client2.get(adIdImpressionKey);
                Double adIdImpressionValue = 0.0;
                if (adIdImpressionVal != null && adIdImpressionVal != "") {
                    adIdImpressionValue = Double.parseDouble(adIdImpressionVal);
                }
                features.add(adIdImpressionValue);
                System.out.println("adIdImpressionKey:" + adIdImpressionKey + " adIdImpressionValue:" + adIdImpressionValue);

                String query = Utility.strJoin(queryTokens, "_");
                //query campaignId click
                String queryCampaignIdClickKey = "qcidc_" + query + "_" + ad.campaignId;
                String queryCampaignIdClickVal = (String) client2.get(queryCampaignIdClickKey);
                Double queryCampaignIdClickValue = 0.0;
                if (queryCampaignIdClickVal != null && queryCampaignIdClickVal != "") {
                    queryCampaignIdClickValue = Double.parseDouble(queryCampaignIdClickVal);
                }
                features.add(queryCampaignIdClickValue);

                // query campaignIdImpression
                String queryCampaignIdImpressionKey = "qcidi_" + query + "_" + ad.campaignId;
                String queryCampaignIdImpressionVal = (String) client2.get(queryCampaignIdImpressionKey);
                Double queryCampaignIdImpressionValue = 0.0;
                if (queryCampaignIdImpressionVal != null && queryCampaignIdImpressionVal != "") {
                    queryCampaignIdImpressionValue = Double.parseDouble(queryCampaignIdImpressionVal);
                }
                features.add(queryCampaignIdImpressionValue);

                // queryAdIdClick
                String queryAdIdClickKey = "qaidc_" + query + "_" + ad.adId;
                String queryAdIdClickVal = (String) client2.get(queryAdIdClickKey);
                Double queryAdIdClickValue = 0.0;
                if (queryAdIdClickVal != null && queryAdIdClickVal != "") {
                    queryAdIdClickValue = Double.parseDouble(queryAdIdClickVal);
                }
                features.add(queryAdIdClickValue);

                // queryAdIdImpression
                String queryAdIdImpressionKey = "qaidi" + query + "_" + ad.adId;
                String queryAdIdImpressionVal = (String) client2.get(queryAdIdImpressionKey);
                Double queryAdIdImpressionValue = 0.0;
                if (queryAdIdImpressionVal != null && queryAdIdImpressionVal != "") {
                    queryAdIdImpressionValue = Double.parseDouble(queryAdIdImpressionVal);
                }
                features.add(queryAdIdImpressionValue);

                // query ad category match scale to 1000000 if match
                double queryAdCategoryMatch = 0.0;
                if (queryCategory == ad.category) {
                    queryAdCategoryMatch = 1000000.0;
                }
                features.add(queryAdCategoryMatch);

//                ad.pClick =
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adList;
    }
}

