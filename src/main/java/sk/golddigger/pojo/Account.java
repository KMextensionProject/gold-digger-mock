package sk.golddigger.pojo;

import java.util.UUID;

public final class Account {

	private static final String profileId = UUID.randomUUID().toString();

	private String id;
	private String currency;
	private String balance;
	private String hold;
	private String available;
	private Boolean tradingEnabled;

	public Account() {
		this.id = UUID.randomUUID().toString();
		this.tradingEnabled = true;
	}

	public String getId() {
		return id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getHold() {
		return hold;
	}

	public void setHold(String hold) {
		this.hold = hold;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getProfileId() {
		return profileId;
	}

	public Boolean getTradingEnabled() {
		return tradingEnabled;
	}

	public void setTradingEnabled(Boolean tradingEnabled) {
		this.tradingEnabled = tradingEnabled;
	}

}
