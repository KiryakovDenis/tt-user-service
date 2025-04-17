package ru.kdv.study.taskTrackerUser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="task-service")
@Data
public class TaskServiceConfigurationProperties {
    private String baseUrl;
}
