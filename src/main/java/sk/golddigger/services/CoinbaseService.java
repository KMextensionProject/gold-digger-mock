package sk.golddigger.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static java.util.stream.Collectors.*;

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
		dotFill.setPrice("34700");
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
		return fills.stream()
			.filter(f -> f.getProductId().equals(productId))
			.collect(toList());
	}

	public Map<String, Object> placeOrder(Order order) {
		if (order == null) {
			return null;
		}

		String productId = order.getProductId();
		if (productId == null) {
			return null;
		}

		String orderSide = order.getSide();
		if (orderSide == null) {
			return null;
		}

		Double size = order.getSize();
		if (size == null) {
			size = order.getFunds();
		}

		// BTC-EUR
		// EUR
		// BTC
		String accountCurrency = productId.substring(productId.indexOf('-') + 1); // EUR
		String tradingCurrency = productId.substring(0, productId.indexOf('-')); // BTC

//		Optional<Account> baseAccount = accounts.stream()
//			.filter(a -> a.getCurrency().equals(accountCurrency))
//			.findFirst();

//		Optional<Account> tradingAccount = accounts.stream()
//			.filter(a -> a.getCurrency().equals(tradingCurrency))
//			.findFirst();

		Map<String, Account> counterAccounts = accounts.stream()
			.filter(a -> hasAtLeastOneCurrency(a, accountCurrency, tradingCurrency))
			.collect(toMap(Account::getCurrency, a -> a));

		if (counterAccounts.size() < 2) {
			return null;
		}

		Account baseAccount = counterAccounts.get(accountCurrency);
		Account tradingAccount = counterAccounts.get(tradingCurrency);

		double currentBaseAccountBalance = Double.parseDouble(baseAccount.getBalance());
		double currentTradingAccountBalance = Double.parseDouble(tradingAccount.getBalance());

		// determine rate
		int rate = random.nextInt(789_000);

		switch(orderSide) {
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
		fills.add(fill);

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
//		id
//		string
//		required
//
//		uuid
//		price
//		string
//
//		price per unit of base currency
//		size
//		string
//
//		amount of base currency to buy/sell
//		product_id
//		string
//		required
//
//		book the order was placed on
//		profile_id
//		string
//
//		profile_id that placed the order
//		side
//		string
//		required
//
//		buy sell
//		funds
//		string
//
//		amount of quote currency to spend (for market orders)
//		specified_funds
//		string
//
//		funds with fees
//		type
//		string
//		required
//
//		limit market stop
//		time_in_force
//		string
//
//		GTC GTT IOC FOK
//		expire_time
//		date-time
//
//		timestamp at which order expires
//		post_only
//		boolean
//		required
//
//		if true, forces order to be maker only
//		created_at
//		date-time
//		required
//
//		timestamp at which order was placed
//		done_at
//		date-time
//
//		timestamp at which order was done
//		done_reason
//		string
//
//		reason order was done (filled, rejected, or otherwise)
//		reject_reason
//		string
//
//		reason order was rejected by engine
//		fill_fees
//		string
//		required
//
//		fees paid on current filled amount
//		filled_size
//		string
//		required
//
//		amount (in base currency) of the order that has been filled
//		executed_value
//		string
//		status
//		string
//		required
//
//		open pending rejected done active received all
//		settled
//		boolean
//		required
//
//		true if funds have been exchanged and settled
//		stop
//		string
//
//		loss entry
//		stop_price
//		string
//
//		price (in quote currency) at which to execute the order
//		funding_amount
//		string
//		client_oid
//		string
//
//		client order id
//		string
		
		Order o = new Order();
		return o;
	}
}
