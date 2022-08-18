package sk.golddigger.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import sk.golddigger.pojo.Account;
import sk.golddigger.pojo.Fill;
import sk.golddigger.pojo.Order;
import sk.golddigger.pojo.Transaction;

@Service
public class CoinbaseService {

	private static final Logger logger = Logger.getAnonymousLogger();

	private static List<Account> accounts;
	private static List<Fill> fills;

	static {
		initAccounts();
		initFills();
	}

	private static void initAccounts() {
		accounts = new ArrayList<>();

		Account account = new Account();
		account.setCurrency("EUR");
		account.setBalance("200.00");
		account.setAvailable(account.getBalance());
		account.setHold("0.0000");
		accounts.add(account);

		account = new Account();
		account.setCurrency("DOT");
		account.setBalance("251.17");
		account.setAvailable(account.getBalance());
		account.setHold("0.0000");
		accounts.add(account);

		account = new Account();
		account.setCurrency("BTC");
		account.setBalance("0.2164890");
		account.setAvailable(account.getBalance());
		account.setHold("0.0000");
		accounts.add(account);

		account = new Account();
		account.setCurrency("ETH");
		account.setBalance("0.00");
		account.setAvailable(account.getBalance());
		account.setHold("0.0000");
		accounts.add(account);

		account = new Account();
		account.setCurrency("XRP");
		account.setBalance("0.00");
		account.setAvailable(account.getAvailable());
		account.setHold("0.0000");
		accounts.add(account);

		account = new Account();
		account.setCurrency("LTC");
		account.setBalance("0.00");
		account.setAvailable(account.getAvailable());
		account.setHold("0.0000");
		accounts.add(account);
	}

	private static void initFills() {
		Fill fill = createBaseFill();
		fill.setOrderId(UUID.randomUUID().toString());
		fill.setProductId("BTC-EUR");
		fill.setPrice("56000");
		fill.setSize("1000");
		fill.setSide("buy");

		fills = new ArrayList<>(50);
		fills.add(fill);
	}

	private static Fill createBaseFill() {
		Fill fill = new Fill();
		fill.setProfileId(UUID.randomUUID().toString());
		fill.setUserId(UUID.randomUUID().toString());
		fill.setTradeId(15648929); // generate random
		fill.setLiquidity("T");
		fill.setUsdVolume("0.00");
		fill.setFee("1.50");
		fill.setSettled(true);

		return fill;
	}

	public void ping() {
		logger.info(() -> "ping() [mock status: OK]");
	}

	public List<Account> getAccounts() {	
		return new ArrayList<>(accounts);
	}

	public Account getAccount(String id) {
		if (id == null) {
			return null;
		}

		Optional<Account> account = accounts.stream()
			.filter(a -> id.equals(a.getId()))
			.findFirst();

		if (!account.isPresent()) {
			return null;
		}

		return account.get();
	}

	public void addDeposit(Transaction depositTransaction) {
		depositTransaction.setDirection("IN");
		processTransaction(depositTransaction);
	}

	// TODO: fuck that up when there is no sufficient amount of money to withdraw
	public void makeWithdrawal(Transaction withdrawalTransaction) {
		withdrawalTransaction.setDirection("OUT");
		processTransaction(withdrawalTransaction);
	}
	
	private void processTransaction (Transaction tx) {
		Optional<Account> oAccount = accounts.stream()
				.filter(a -> a.getId().equals(tx.getAccountId()))
				.findFirst();

		if (oAccount.isPresent()) {
			Account account = oAccount.get();
			double currentBalance = Double.parseDouble(account.getBalance());

			if ("IN".equals(tx.getDirection())) {
				account.setBalance(String.valueOf(currentBalance + tx.getAmount()));
			} else {
				account.setBalance(String.valueOf(currentBalance - tx.getAmount()));
			}
			account.setAvailable(account.getBalance());
		}
	}

	public List<Fill> getOrderFills(String productId, String profileId, Integer limit) {
		return new ArrayList<>(fills);
	}

	public Map<String, Object> placeBuyOrder(Order order) {
		Map<String, Object> result = new HashMap<>();
		result.put("id", UUID.randomUUID().toString());
		// TODO: add to fills
		return result;
	}

	// could return Fill but check it out first - this is not even called by gold-digger
	public Order getOrder(String id) {
		return null;
	}

}
