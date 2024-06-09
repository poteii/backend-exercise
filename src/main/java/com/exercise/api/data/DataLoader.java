package com.exercise.api.data;

import com.exercise.api.entity.JobData;
import com.exercise.api.repository.JobDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final JobDataRepository repo;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        List<JobData> jobData = Arrays.asList(objectMapper.readValue(
                Paths.get("src/main/resources/salary_survey.json").toFile(), JobData[].class));
        repo.saveAll(jobData);
    }
}
