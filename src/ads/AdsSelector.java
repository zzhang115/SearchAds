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
                System.out.println("deviceIpKey:"+deviceIpClickKey + " deviceIpValue:" + deviceIpClickValue);

                // device ip impression
                String deviceIpImpressionKey = "dipi_" + deviceIp;
                String deviceIpImpressionVal = (String) client2.get(deviceIpImpressionKey);
                Double deviceIpImpressionValue = 0.0;
                if (deviceIpImpressionVal != null && deviceIpImpressionVal != "") {
                    deviceIpImpressionValue = Double.parseDouble(deviceIpImpressionVal);
                }
                features.add(deviceIpImpressionValue);
                System.out.println("deviceIpImpressionKey:"+ deviceIpImpressionKey + " deviceIpImpressionValue:" + deviceIpImpressionValue);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return adList;
    }
}

