package ads;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

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
            System.out.println(keyWords);
            MemcachedClient client = new MemcachedClient(new InetSocketAddress(memcachedServer, memcachedPort));
            List<String> tokens = Utility.cleanUselessTokens(keyWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
