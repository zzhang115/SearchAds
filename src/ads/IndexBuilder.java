package ads;

import SQLAccess.SQLAccess;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zzc on 8/14/17.
 */
public class IndexBuilder {
    private static final int EXPIRE = 72000;
    private static final String MEMCACHEDSERVER = "127.0.0.1";
    private static final int MEMCACHEDPORT = 11211;
    private MemcachedClient client;
    private SQLAccess sqlAccess;


    public IndexBuilder() {
        try {
            this.client = new MemcachedClient(new InetSocketAddress(MEMCACHEDSERVER, MEMCACHEDPORT));
            this.sqlAccess = new SQLAccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildTokenToAdId(Ad ad) {
        String keyWords = Utility.strJoin(ad.keyWords, ",");
        List<String> tokens = Utility.cleanUselessTokens(keyWords);

        // use client to set key is item's token and its correspond adIdList, it seems like a map(token, adIdList)
        for (String token : tokens) {
            if (client.get(token) instanceof Set) {
                @SuppressWarnings("Unchecked")
                Set<Long> adIdList = (Set<Long>) client.get(token);
                adIdList.add(ad.adId);
                client.set(token, EXPIRE, adIdList);
            } else {
                Set<Long> adIdList = new HashSet<Long>();
                adIdList.add(ad.adId);
                client.set(token, EXPIRE, adIdList);
            }
        }
    }

    public void addAdsToDataBase(Ad ad) {

    }

    public void cleanup() {

    }
}
