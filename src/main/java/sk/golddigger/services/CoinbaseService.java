package sk.golddigger.services;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

@Service
public class CoinbaseService {

	private static final Logger logger = Logger.getAnonymousLogger();

	public void ping() {
		logger.info(() -> "ping() [mock status: OK]");
	}
}
