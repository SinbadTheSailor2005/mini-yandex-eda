package dev.vundirov.orderservice.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
  
  @Override
  public ClientHttpResponse intercept(
          HttpRequest request, byte[] body,
          ClientHttpRequestExecution execution) throws IOException {
    logger.info("Requst: {} {}", request.getMethod(), request.getURI());
    logger.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
    return execution.execute(request, body);
  }
}
