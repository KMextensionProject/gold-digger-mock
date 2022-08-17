package sk.golddigger.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import sk.golddigger.pojo.Account;
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
		// TODO: should return 404 ?
		return coinbaseService.getAccount(id);
	}

}
