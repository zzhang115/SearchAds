package ads;

import campaign.Campaign;
import campaign.CampaignManager;
import org.json.JSONArray;
import org.json.JSONObject;
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
        List<String> queryTokens= QueryHandling.getInstance().QueryStringHandling(query);

        AdsSelector adsSelector = new AdsSelector();
        List<Ad> adList = adsSelector.selectAds(queryTokens);
        System.out.println("Ad Searched size is: " + adList.size());

        List<Ad> firstFilterRestedAds = AdFilter.getInstance().filterLevelOne(adList);
        System.out.println("After First Filter, Ad size is: " + firstFilterRestedAds.size());
        List<Ad> secondFilterRestedAds = AdFilter.getInstance().filterLevelTwo(firstFilterRestedAds);
        System.out.println("After Second Filter, Ad size is: " + secondFilterRestedAds.size());

        CampaignManager campaignManager = new CampaignManager();
        List<Ad> noDuplicateCampaignAdList = campaignManager.getNoDuplicateCampaignIdList(secondFilterRestedAds);
        System.out.println("Remove Duplicate CampaignId, Ad size is: " + noDuplicateCampaignAdList.size());
        List<Ad> finalAds = campaignManager.UpdateBudgetInfo(noDuplicateCampaignAdList);
        System.out.println("Final Ad size is: " + finalAds.size());

        AdsAllocation adsAllocation = new AdsAllocation();
        adsAllocation.allocateAds(finalAds);
        return finalAds;

    }
}
