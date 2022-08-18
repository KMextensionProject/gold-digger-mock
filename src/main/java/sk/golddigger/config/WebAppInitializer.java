package sk.golddigger.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import sk.golddigger.interceptors.LoggingInterceptor;

@Component
public class WebAppInitializer implements WebMvcConfigurer {

	@Autowired
	private LoggingInterceptor loggingInterceptor;

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, getJackson2HttpMessageConverter());
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor);
	}

	@Bean
	public MappingJackson2HttpMessageConverter getJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jackson2HttpMsgConverter = new MappingJackson2HttpMessageConverter();
		jackson2HttpMsgConverter.setObjectMapper(getObjectMapper());
		return jackson2HttpMsgConverter;
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		return objectMapper;
	}
}
