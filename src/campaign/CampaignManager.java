package campaign;

import sqlaccess.SQLAccess;
import ads.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 8/17/17.
 */
public class CampaignManager {
    private static double minPriceThreshold = 0.0;

    public List<Ad> getNoDuplicateCampaignIdList(List<Ad> adCandidates) {
        List<Ad> campaignAds = new ArrayList<Ad>();
        for (Ad ad : adCandidates) {
            if (!campaignAds.contains(ad)) {
                campaignAds.add(ad);
            }
        }
        return campaignAds;
    }

    public List<Ad> UpdateBudgetInfo(List<Ad> campaignAds) {
        List<Ad> adList = new ArrayList<Ad>();
        SQLAccess sqlAccess = new SQLAccess();
        for (Ad campaignAd : campaignAds) {
            System.out.println("campaginId: " + campaignAd.campaignId + " ad cost per click: " +
                    + campaignAd.costPerClick);
            Double budget= sqlAccess.getBudgetData(campaignAd.campaignId);
            if (campaignAd.costPerClick <= budget && campaignAd.costPerClick >= minPriceThreshold) {
                adList.add(campaignAd);
                budget = budget - campaignAd.costPerClick - 10;
                sqlAccess.updateBudgetData(campaignAd.campaignId, budget);
            }
        }
        sqlAccess.connectionClose();
        return adList;
    }
}
