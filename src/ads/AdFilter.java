package ads;

import java.util.*;

/**
 * Created by zzc on 8/17/17.
 */
public class AdFilter {
    private static AdFilter instance = null;
    private static int minNumOfAds = 5;
    private static double pClickThreshold = 0.0;
    private static double relevanceThreshold = 0.1;

    public static AdFilter getInstance() {
        if (instance == null) {
            instance = new AdFilter();
            return instance;
        }
        return instance;
    }

    public List<Ad> filterLevelOne(List<Ad> candidateAds) {
        if (candidateAds.size() < minNumOfAds) {
            return candidateAds;
        }
        List<Ad> firstFilterRestAds = new ArrayList<Ad>();
        for (Ad candidate: candidateAds) {
            if (candidate.pClick >= pClickThreshold && candidate.relevanceScore > relevanceThreshold ) {
                firstFilterRestAds.add(candidate);
            }
        }
        return firstFilterRestAds;
    }

    public List<Ad> filterLevelTwo(List<Ad> candidateAds) {
        if (candidateAds.size() < minNumOfAds) {
            return candidateAds;
        }
        List<Ad> secondFilterRestAds = new ArrayList<Ad>();
        Collections.sort(candidateAds, new Comparator<Ad>() {
            public int compare(Ad a, Ad b) {
                return (int) ((b.relevanceScore - a.relevanceScore) * 100);
            }
        });
        for (Ad ad : candidateAds) {
            System.out.println("AdId:"+ad.adId+" sorted AdScore:"+ad.relevanceScore);
        }
        return secondFilterRestAds;
    }
}
