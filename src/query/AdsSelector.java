package query;

import ads.Ad;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zzc on 8/16/17.
 */
public class AdsSelector {
    private static AdsSelector instance = null;
    private static final String MEMCACHEDSERVER = "127.0.0.1";
    private static final int MEMCACHEDPORT = 11211;

    public static AdsSelector getInstance() {
        if (instance == null) {
            return new AdsSelector();
        }
        return instance;
    }

    public List<Ad> selectAds(List<String> queryTokens) {
        List<Ad> adList = new ArrayList<Ad>();
        HashMap<Long, Integer> matchedAdsMap = new HashMap<Long, Integer>();

        try {
            MemcachedClient client = new MemcachedClient(new InetSocketAddress(MEMCACHEDSERVER, MEMCACHEDPORT));
            for (String token : queryTokens) {
                System.out.println("Select Ads queryToken: " + token);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

