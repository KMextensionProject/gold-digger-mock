package sk.golddigger.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import sk.golddigger.services.CoinbaseService;

@Controller
public class CoinbaseController {

	@Autowired
	private CoinbaseService coinbaseService;
	
	@RequestMapping(path = "/mock/ping", method = RequestMethod.GET, produces = "text/plain")
	@ResponseStatus(code = HttpStatus.OK)
	public void ping() {
		coinbaseService.ping();
	}

}
