package ch.ohne.dachschaden.client.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GeoAdminProperties.class)
public class ClientConfig {

    @Bean
    RestClient geoAdminRestClient(GeoAdminProperties props) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
