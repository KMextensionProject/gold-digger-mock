package sk.golddigger.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

	@JsonProperty("profile_id")
	private String profileId;

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("time_in_force")
	private String timeInForce;

	@JsonProperty("post_only")
	private Boolean postOnly;

	@JsonProperty("cancel_after")
	private String cancelAfter;

	private String type;
	private String side;
	private Double price;
	private Double size;
	private Double funds;
	private String stp;

	public Order() {

	}

	public String getStp() {
		return stp;
	}

	public void setStp(String stp) {
		this.stp = stp;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public void setPostOnly(Boolean postOnly) {
		this.postOnly = postOnly;
	}

	public void setCancelAfter(String cancelAfter) {
		this.cancelAfter = cancelAfter;
	}

	public void setFunds(Double funds) {
		this.funds = funds;
	}

	public String getProfileId() {
		return profileId;
	}

	public String getProductId() {
		return productId;
	}

	public String getType() {
		return type;
	}

	public String getSide() {
		return side;
	}

	public Double getPrice() {
		return price;
	}

	public Double getSize() {
		return size;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public Boolean getPostOnly() {
		return postOnly;
	}

	public String getCancelAfter() {
		return cancelAfter;
	}

	public Double getFunds() {
		return funds;
	}

	public String getSelfTradePrevention() {
		return stp;
	}

}
