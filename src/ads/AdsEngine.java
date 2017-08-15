package ads;

/**
 * Created by zzc on 8/14/17.
 */
public class AdsEngine {
    private String mAdsDataFilePath;
    private String mBudgetFilePath;
    private IndexBuilder indexBuilder;
    private String mMemcachedServer;
    private int mMemcachedPortal;
    private String mysql_host;
    private String mysql_db;
    private String mysql_user;
    private String mysql_pass;

    public AdsEngine(String mAdsDataFilePath, String mBudgetFilePath, String mMemcachedServer, int mMemcachedPortal, String mysql_host, String mysql_db, String mysql_user, String mysql_pass) {
        this.mAdsDataFilePath = mAdsDataFilePath;
        this.mBudgetFilePath = mBudgetFilePath;
        this.mMemcachedServer = mMemcachedServer;
        this.mMemcachedPortal = mMemcachedPortal;
        this.mysql_host = mysql_host;
        this.mysql_db = mysql_db;
        this.mysql_user = mysql_user;
        this.mysql_pass = mysql_pass;
        indexBuilder = new IndexBuilder();
    }
}
