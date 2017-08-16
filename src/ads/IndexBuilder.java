package ads;

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
    private String memcachedServer;
    private int memcachedPort;
    private String mysqlHost;
    private String mysqlDB;
    private String mysqlUser;
    private String mysqlPassword;

    public IndexBuilder(String memcachedServer, int memcachedPort, String mysqlHost, String mysqlDB, String mysqlUser, String mysqlPassword) {
        this.memcachedServer = memcachedServer;
        this.memcachedPort = memcachedPort;
        this.mysqlHost = mysqlHost;
        this.mysqlDB = mysqlDB;
        this.mysqlUser = mysqlUser;
        this.mysqlPassword = mysqlPassword;
    }

    public void buildInvertIndex(Ad ad) {
        try {
            String keyWords = Utility.strJoin(ad.keyWords, ",");
            MemcachedClient client = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
