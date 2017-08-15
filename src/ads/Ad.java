package ads;

import java.io.Serializable;
import java.util.List;

public class Ad implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long adId;
	public Long campaignId;
	public List<String> keyWords;
	public double relevanceScore;
	public double pClick;	
	public double bidPrice;
	public double rankScore;
	public double qualityScore;
	public double costPerClick;
	public int position;//1: top , 2: bottom
    public String title; // required
    public double price; // required
    public String thumbnail; // required
    public String description; // required
    public String brand; // required
    public String detail_url; // required
    public String query; //required
    public String category;

	public Ad(Long adId, Long campaignId, List<String> keyWords, double relevanceScore,
			  double pClick, double bidPrice, double rankScore, double qualityScore,
			  double costPerClick, int position, String title, double price, String thumbnail,
			  String description, String brand, String detail_url, String query, String category) {
		this.adId = adId;
		this.campaignId = campaignId;
		this.keyWords = keyWords;
		this.relevanceScore = relevanceScore;
		this.pClick = pClick;
		this.bidPrice = bidPrice;
		this.rankScore = rankScore;
		this.qualityScore = qualityScore;
		this.costPerClick = costPerClick;
		this.position = position;
		this.title = title;
		this.price = price;
		this.thumbnail = thumbnail;
		this.description = description;
		this.brand = brand;
		this.detail_url = detail_url;
		this.query = query;
		this.category = category;
	}
}
