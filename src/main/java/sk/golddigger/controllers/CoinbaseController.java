package sk.golddigger.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import sk.golddigger.pojo.Account;
import sk.golddigger.pojo.Fill;
import sk.golddigger.pojo.Order;
import sk.golddigger.pojo.Transaction;
import sk.golddigger.services.CoinbaseService;

@Controller
public class CoinbaseController {

	@Autowired
	private CoinbaseService coinbaseService;
	
	@RequestMapping(path = "/ping", method = RequestMethod.GET, produces = "text/plain")
	@ResponseStatus(code = HttpStatus.OK)
	public void ping() {
		coinbaseService.ping();
	}

	@RequestMapping(path = "/accounts", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Account> getAccounts() {
		return coinbaseService.getAccounts();
	}

	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Account getAccount(@PathVariable String id) {
		return coinbaseService.getAccount(id);
	}

	@RequestMapping(path = "/deposit", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public void deposit(@RequestBody Transaction depositTransaction) {
		coinbaseService.addDeposit(depositTransaction);
	}

	@RequestMapping(path = "/withdraw", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(code = HttpStatus.ACCEPTED) // toto by malo vratit nie void ale response entity
	public void withdraw(@RequestBody Transaction withdrawalTransaction) {
		coinbaseService.makeWithdrawal(withdrawalTransaction);
	}

	@RequestMapping(path = "/fills", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Fill> getOrderFills(
			@RequestParam(name = "product_id") String productId,
			@RequestParam(name = "profile_id") String profileId,
			@RequestParam Integer limit) {

		return coinbaseService.getOrderFills(productId, profileId, limit);
	}

	@RequestMapping(path = "/orders", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Map<String, Object> placeBuyOrder(Order order) {
		return coinbaseService.placeBuyOrder(order);
	}

	// TENTO ENDPOINT SA ANI NEPOUZIVA:
	// Order tym padom by mal jebat na tie ktore su null
	@RequestMapping(path = "/orders/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Order getOrder(@PathVariable String id) {
		return coinbaseService.getOrder(id);
	}

}
