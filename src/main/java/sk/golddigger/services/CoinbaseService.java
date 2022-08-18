package sk.golddigger.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sk.golddigger.pojo.Account;
import sk.golddigger.pojo.Fill;
import sk.golddigger.pojo.Order;
import sk.golddigger.pojo.Transaction;

@Service
public class CoinbaseService {

	private static final SecureRandom random = new SecureRandom();

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
		account.setHold("0.00");
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
		Fill btcFill = createBaseFill();
		btcFill.setProductId("BTC-EUR");
		btcFill.setPrice("56000");
		btcFill.setSize("12700");
		btcFill.setSide("buy");

		Fill dotFill = createBaseFill();
		dotFill.setProductId("DOT-EUR");
		dotFill.setPrice("4700");
		dotFill.setSize("2500");
		dotFill.setSide("buy");

		fills = new ArrayList<>(50);
		fills.add(btcFill);
		fills.add(dotFill);
	}

	private static Fill createBaseFill() {
		Fill fill = new Fill();
		fill.setProfileId(UUID.randomUUID().toString());
		fill.setOrderId(UUID.randomUUID().toString());
		fill.setTradeId(random.nextInt(100_000) + 2564);
		fill.setLiquidity("T");
		fill.setUsdVolume("0.00");
		fill.setFee("1.50");
		fill.setSettled(true);

		return fill;
	}

	public void ping() {

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
			} else if ("OUT".equals(tx.getDirection())) {
				if (currentBalance >= tx.getAmount()) {
					account.setBalance(String.valueOf(currentBalance - tx.getAmount()));
				} else {
					throw new IllegalStateException("cannot make the withdrawal");
				}
			}
			account.setAvailable(account.getBalance());
		}
	}

	public List<Fill> getOrderFills(String productId, String profileId, Integer limit) {
		return new ArrayList<>(fills);
	}

	public Map<String, Object> placeOrder(Order order) {
		if (order == null) {
			return null;
		}

		String productId = order.getProductId();
		if (productId == null) {
			return null;
		}

		Double size = order.getSize();
		if (size == null) {
			size = order.getFunds();
		}

		String accountCurrency = productId.substring(productId.indexOf('-') + 1);
		String tradingCurrency = productId.substring(0, productId.indexOf('-'));

//		Optional<Account> baseAccount = accounts.stream()
//			.filter(a -> a.getCurrency().equals(accountCurrency))
//			.findFirst();

//		Optional<Account> tradingAccount = accounts.stream()
//			.filter(a -> a.getCurrency().equals(tradingCurrency))
//			.findFirst();

		Map<String, Account> counterAccounts = accounts.stream()
			.filter(a -> hasAtLeastOneCurrency(a, accountCurrency, tradingCurrency))
			.collect(Collectors.toMap(Account::getCurrency, a -> a));

		if (counterAccounts.size() < 2) {
			return null;
		}

		Account baseAccount = counterAccounts.get(accountCurrency);
		Account tradingAccount = counterAccounts.get(tradingCurrency);

		double currentBaseAccountBalance = Double.parseDouble(baseAccount.getBalance());
		double currentTradingAccountBalance = Double.parseDouble(tradingAccount.getBalance());

		// determine rate
		int rate = random.nextInt(789_000);

		switch(order.getSide()) {
		case "buy":
			tradingAccount.setBalance(String.valueOf(currentBaseAccountBalance / rate));
			baseAccount.setBalance("0.00");
			break;
		case "sell":
			baseAccount.setBalance(String.valueOf(currentTradingAccountBalance * rate));
			tradingAccount.setBalance("0.0000");
			break;
		default:
			break;
		}

		Fill fill = createBaseFill();
		fill.setProductId(productId);
		fill.setPrice(String.valueOf(rate));
		fill.setSize(String.valueOf(size));
		fill.setSide(order.getSide());

		Map<String, Object> result = new HashMap<>();
		result.put("id", fill.getOrderId());

		return result;
	}

	private boolean hasAtLeastOneCurrency(Account account, String... currencies) {
		String accountSupportedCurrency = account.getCurrency();
		for (String currency : currencies) {
			if (currency.equals(accountSupportedCurrency)) {
				return true;
			}
		}
		return false;
	}

	// could return Fill but check it out first - this is not even called by gold-digger
	public Order getOrder(String id) {
		return null;
	}

}
