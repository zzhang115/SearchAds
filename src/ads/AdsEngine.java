package ads;

import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.json.JSONArray;
import org.json.JSONObject;
import query.AdsSelector;
import query.QueryHandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 8/14/17.
 */

public class AdsEngine {
    private String adsDataFilePath;
    private String budgetFilePath;
    private IndexBuilder indexBuilder;

    public AdsEngine(String mAdsDataFilePath, String mBudgetFilePath) {
        this.adsDataFilePath = mAdsDataFilePath;
        this.budgetFilePath = mBudgetFilePath;
        indexBuilder = new IndexBuilder();
    }

    public void initEngine() {
//       buildAdsData();
//        buildBudgetData();
    }

    public void buildAdsData() {
        indexBuilder.indexBuilderSQLInit();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(adsDataFilePath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);

                if (jsonObject.isNull("adId") || jsonObject.isNull("campaignId")) {
                    continue;
                }
                long adId = jsonObject.getLong("adId");
                long campaignId = jsonObject.getLong("campaignId");
                String brand = jsonObject.isNull("brand") ? "" : jsonObject.getString("brand");
                double price = jsonObject.isNull("price") ? 100.0 : jsonObject.getDouble("price");
                String thumbnail= jsonObject.isNull("thumbnail") ? "" : jsonObject.getString("thumbnail");
                String title = jsonObject.isNull("title") ? "" : jsonObject.getString("title");
                String detailUrl= jsonObject.isNull("detailUrl") ? "" : jsonObject.getString("detailUrl");
                double bidPrice = jsonObject.isNull("bidPrice") ? 1.0 : jsonObject.getDouble("bidPrice");
                double pClick = jsonObject.isNull("pClick") ? 0.0 : jsonObject.getDouble("pClick");
                String category = jsonObject.isNull("category") ? "" : jsonObject.getString("category");
                String description = jsonObject.isNull("description") ? "" : jsonObject.getString("description");
                JSONArray keyWords = jsonObject.isNull("keyWords") ? null : jsonObject.getJSONArray("keyWords");

                List<String> keyWordsList = new ArrayList<String>();
                for (Object keyWord : keyWords) {
                    keyWordsList.add(String.valueOf(keyWord));
                }
                Ad ad = new Ad(adId, campaignId, brand, price, thumbnail, title, detailUrl, bidPrice, pClick, category, description, keyWordsList);
                indexBuilder.buildTokenToAdId(ad);
                indexBuilder.addAdsToDatabase(ad);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            indexBuilder.sqlAccessClose();
        }
    }

    public void buildBudgetData() {
        indexBuilder.indexBuilderSQLInit();
        try {
            BufferedReader budgetReader = new BufferedReader(new FileReader(budgetFilePath));
            String line;
            while ((line = budgetReader.readLine()) != null) {
                JSONObject campaignJson = new JSONObject(line);
                Long campaignId = campaignJson.getLong("campaignId");
                double budget = campaignJson.getDouble("budget");
                Campaign campaign = new Campaign(campaignId, budget);
                indexBuilder.addBudgetsToDatabase(campaign);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            indexBuilder.sqlAccessClose();
        }
    }

    public List<Ad> selectAds(String query) {
        //get query string keyword
        List<String> queryTokens= QueryHandling.getInstance().QueryStringHandling(query);
        AdsSelector adsSelector = new AdsSelector();
        adsSelector.adsSelectorSQLInit();
        List<Ad> adList = adsSelector.selectAds(queryTokens);
        adsSelector.adsSelectorSQLClose();

        List<Ad> firstFilterRestedAds = AdFilter.getInstance().filterAdsLevelOne(adList);

        for (Ad ad : firstFilterRestedAds) {
            System.out.println("adId:"+ad.adId+" relevance:"+ad.relevanceScore);
        }
        return adList;

    }
}
