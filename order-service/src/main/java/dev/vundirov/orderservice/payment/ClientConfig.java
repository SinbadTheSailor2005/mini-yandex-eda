package dev.vundirov.orderservice.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {
  @Bean
  public RestClient warehouseRestClient(RestClient.Builder builder,
                                        @Value("${services.warehouse.url}") String baseUrl) {
    return builder.baseUrl(baseUrl).requestInterceptor(new LoggingInterceptor()).build();

  }
}
