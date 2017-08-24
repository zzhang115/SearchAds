package query;

import ads.Utility;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 8/16/17.
 */
public class QueryHandling {
    private static final String MEMCACHEDSERVER = "127.0.0.1";
    private static final int MEMCACHED_SYNON_PORT = 11212; // we can use differenct port for different memecached
    private static QueryHandling instance = null;

    public static QueryHandling getInstance() {
        if (instance == null) {
            return new QueryHandling();
        }
        return instance;
    }

    public List<String> queryStringHandling(String query) {
        List<String> tokens = Utility.cleanUselessTokens(query);
        return tokens;
    }

    public List<List<String>> queryRewrite(String query) {
        List<List<String>> tokensList = new ArrayList<List<String>>();
        List<String> tokens = Utility.cleanUselessTokens(query);
        String queryKey = Utility.strJoin(tokens, "_");
        try {
            MemcachedClient memcachedClient = new MemcachedClient(new InetSocketAddress(MEMCACHEDSERVER, MEMCACHED_SYNON_PORT));
            if (memcachedClient.get(queryKey) instanceof List) {
                List<String> synonyms = (ArrayList<String>)memcachedClient.get(queryKey); // list: a_b_c, a_b_d...
                for (String synonym : synonyms) {
                    List<String> token = new ArrayList<String>();
                    String[] synonymArray = synonym.split("_");
                    for (String subSynonym : synonymArray) {
                        token.add(subSynonym);
                    }
                    tokensList.add(token);
                }
            } else {
                tokensList.add(tokens);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokensList;
    }

}
