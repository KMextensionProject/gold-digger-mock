package sk.golddigger.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger(LoggingInterceptor.class);

	private long requestStartTime;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		this.requestStartTime = System.currentTimeMillis();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String url = request.getRequestURI().toString();
		long time = System.currentTimeMillis() - requestStartTime;
		int code = response.getStatus();
		String status = "OK";

		switch(code) {
		case 201:
			status = "CREATED";
			break;
		case 204:
			status = "NO_CONTENT";
			break;
		case 400:
			status = "BAD_REQUEST";
			break;
		case 404:
			status = "NOT_FOUND";
			break;
		case 405:
			status = "METHOD_NOT_ALLOWED";
			break;
		case 500:
			status = "SERVER_ERROR";
			break;
		}
		logger.info(url + " " + code + "-" + status + " (" + time + "ms)");
	}

}
