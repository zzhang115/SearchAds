package ads;

import java.io.Serializable;
import java.util.List;

public class Ad implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long adId;
	public long campaignId;
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

	public Ad(long adId, long campaignId, String brand, double price,
			  String thumbnail, String title, String detail_url,
			  double bidPrice, double pClick, String category,
			  String description, List<String> keyWords) {
		this.adId = adId;
		this.campaignId = campaignId;
		this.keyWords = keyWords;
		this.pClick = pClick;
		this.bidPrice = bidPrice;
		this.title = title;
		this.price = price;
		this.thumbnail = thumbnail;
		this.description = description;
		this.brand = brand;
		this.detail_url = detail_url;
		this.category = category;
	}
}
