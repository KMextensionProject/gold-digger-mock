package sk.golddigger.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import sk.golddigger.pojo.Account;

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
			return new Account();
		}

		Optional<Account> account = accounts.stream()
			.filter(a -> id.equals(a.getId()))
			.findFirst();

		if (!account.isPresent()) {
			return new Account();
		}

		return account.get();
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
