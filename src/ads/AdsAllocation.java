package ads;

import java.util.List;

/**
 * Created by zzc on 8/17/17.
 */
public class AdsAllocation {
    private static double mainLinePriceThreshold = 5;

    public void allocateAds(List<Ad> adList) {
        for (Ad ad : adList) {
            if (ad.costPerClick >= mainLinePriceThreshold) {
                ad.position = 1;
            } else {
                ad.position = 2;
            }
        }
    }
}
