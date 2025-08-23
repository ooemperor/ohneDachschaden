package ch.ohne.dachschaden.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "geo.admin")
public record GeoAdminProperties(
        String baseUrl,
        Endpoint search,
        Endpoint feature
) {
    public record Endpoint(String path) {}
}
