package sk.golddigger.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import sk.golddigger.pojo.Account;
import sk.golddigger.pojo.Fill;
import sk.golddigger.pojo.Transaction;

@Service
public class CoinbaseService {

	private static final Logger logger = Logger.getAnonymousLogger();

	private static List<Account> accounts;
	static {
		initAccounts();
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

	// TODO: clean this shit up
	public void addDeposit(Transaction depositTransaction) {
		Optional<Account> oAccount = accounts.stream()
			.filter(a -> a.getId().equals(depositTransaction.getAccountId()))
			.findFirst();
		
		if (oAccount.isPresent()) {
			Account account = oAccount.get();
			double currentBalance = Double.parseDouble(account.getBalance());
			account.setBalance(String.valueOf(currentBalance + depositTransaction.getAmount()));
		}
	}

	public void makeWithdrawal(Transaction withdrawalTransaction) {
		Optional<Account> oAccount = accounts.stream()
				.filter(a -> a.getId().equals(withdrawalTransaction.getAccountId()))
				.findFirst();

		if (oAccount.isPresent()) {
			Account account = oAccount.get();
			double currentBalance = Double.parseDouble(account.getBalance());
			account.setBalance(String.valueOf(currentBalance - withdrawalTransaction.getAmount()));
		}
	}

	public List<Fill> getOrderFills(String productId, String profileId, Integer limit) {
		
		
		return Collections.emptyList();
	}

	private static void initAccounts() {
		accounts = new ArrayList<>();

		Account account = new Account();
		account.setBalance("200.00");
		account.setCurrency("EUR");
		accounts.add(account);

		account = new Account();
		account.setBalance("251.17");
		account.setCurrency("DOT");
		accounts.add(account);

		account = new Account();
		account.setBalance("0.2164890");
		account.setCurrency("BTC");
		accounts.add(account);
	}
}
