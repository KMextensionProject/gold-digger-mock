package sk.golddigger.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

	@JsonProperty("account_id")
	private String accountId;
	private double amount;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
