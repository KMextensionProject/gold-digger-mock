package sk.golddigger.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fill {

	// ziskat cez golddigger nejaku historicku transakciu a ak sa vrati to co ma v sebe fill, tak vratim fill z kontrolera aj tam
	
//	{
//		"created_at": "2022-08-16T09:27:23.888354Z",
//		"trade_id": 67282731,
//		"product_id": "BTC-EUR",
//		"order_id": "4f1844e7-eab6-4cc6-9b0a-9bb5506051a7",
//		"user_id": "595247a3feff9f01bd7372bb",
//		"profile_id": "c28a12e0-9869-48a6-98d3-17adfa455555",
//		"liquidity": "T",
//		"price": "23776.87000000",
//		"size": "0.00836136",
//		"fee": "1.1928418184592000",
//		"side": "buy",
//		"settled": true,
//		"usd_volume": "201.4679725632000000"
//	}

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("trade_id")
	private Integer tradeId;

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("profile_id")
	private String profileId;

	@JsonProperty("usd_volume")
	private String usdVolume;

	private String liquidity;
	private String price;
	private String size;
	private String fee;
	private String side;
	private Boolean settled;

	public Fill() {
		this.createdAt = LocalDateTime.now().toString();
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getUsdVolume() {
		return usdVolume;
	}

	public void setUsdVolume(String usdVolume) {
		this.usdVolume = usdVolume;
	}

	public String getLiquidity() {
		return liquidity;
	}

	public void setLiquidity(String liquidity) {
		this.liquidity = liquidity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public Boolean getSettled() {
		return settled;
	}

	public void setSettled(Boolean settled) {
		this.settled = settled;
	}

	@Override
	public String toString() {
		return "Fill [createdAt=" + createdAt + ", tradeId=" + tradeId + ", productId=" + productId + ", orderId="
				+ orderId + ", userId=" + userId + ", profileId=" + profileId + ", usdVolume=" + usdVolume
				+ ", liquidity=" + liquidity + ", price=" + price + ", size=" + size + ", fee=" + fee + ", side=" + side
				+ ", settled=" + settled + "]";
	}

}
