package ru.kdv.study.taskTrackerUser.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.kdv.study.taskTrackerUser.config.TaskServiceConfigurationProperties;
import ru.kdv.study.taskTrackerUser.exception.ExternalServiceException;

@Service
@AllArgsConstructor
public class TaskService {
    private final RestTemplate restTemplate;
    private final TaskServiceConfigurationProperties taskServiceConfigurationProperties;

    private static final String CHECK_ACTUAL_TASKS = "/task/checkActualTaskByUser/{id}";

    public boolean checkActualTask(Long id) {
        try {
            Boolean retval = restTemplate.getForObject(
                    taskServiceConfigurationProperties.getBaseUrl() + CHECK_ACTUAL_TASKS,
                    Boolean.class,
                    id
            );
            return Boolean.TRUE.equals(retval);
        } catch (Exception e) {
            throw ExternalServiceException.create(e.getMessage());
        }
    }
}
