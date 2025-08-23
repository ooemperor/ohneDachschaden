package ch.ohne.dachschaden.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gvb")
public record GvbProperties(
        String baseUrl,
        String servicePath,
        Integer layerId,
        String outFields,
        Boolean returnGeometry,
        String format,
        Integer connectTimeoutMs,
        Integer readTimeoutMs
) {}
