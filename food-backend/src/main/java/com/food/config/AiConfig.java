package com.food.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {
    private CozeConfig coze = new CozeConfig();
    private VolcConfig volc = new VolcConfig();
    private DeepseekConfig deepseek = new DeepseekConfig();

    @Data
    public static class CozeConfig {
        private String apiUrl;
        private String botId;
        private String apiKey;
    }

    @Data
    public static class VolcConfig {
        private String apiKey;
        private String imageApiUrl;
    }

    @Data
    public static class DeepseekConfig {
        private String apiUrl;
        private String apiKey;
    }
}
