package query;

import ads.Utility;

import java.util.List;

/**
 * Created by zzc on 8/16/17.
 */
public class QueryHandling {
    private static QueryHandling instance = null;

    public static QueryHandling getInstance() {
        if (instance == null) {
            return new QueryHandling();
        }
        return instance;
    }

    public List<String> QueryStringHandling(String query) {
        List<String> tokens = Utility.cleanUselessTokens(query);
        return tokens;
    }
}
