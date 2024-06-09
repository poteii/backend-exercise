package com.exercise.api.controller;

import com.exercise.api.dto.JobDataDTO;
import com.exercise.api.entity.JobData;
import com.exercise.api.repository.JobDataRepository;
import com.exercise.api.specification.JobDataFilter;
import com.exercise.api.specification.JobDataMapper;
import com.exercise.api.specification.JobDataSpecifications;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job_data")
@AllArgsConstructor
public class JobDataController {

    private final JobDataRepository repository;


    /**
     * One api have every require function
     * Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000) (Show only filtered row. Expected filter able column: job title, salary, gender )
     * Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary) (Show only filtered column)
     * Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)
     */
    @Operation(summary = "Get Job Data by criteria")
    @Parameter()
    @GetMapping
    public List<JobDataDTO> getJobDataFiltered(
            @Parameter(description = "to find salary use -gte for greater than/equal, -lte for less than/equal and equal")
            @RequestParam(value = "search", required = false, defaultValue = "job-title=Software Engineer&salary-gte=120000") String search,
            @RequestParam(value = "fields", required = false,defaultValue = "gender,location") String fields,
            @RequestParam(value = "sort", required = false, defaultValue = "salary") String sort,
            @RequestParam(value = "sort_type", required = false) String sortType) {

        Specification<JobData> spec = JobDataSpecifications.buildSpecificationFromSearch(search);
        Sort.Direction direction = (sortType != null && sortType.equalsIgnoreCase("DESC")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create the sorting object
        Sort sortOrder = (sort != null && !sort.isEmpty()) ? Sort.by(direction, (sort)) : Sort.unsorted();

        List<JobData> all = repository.findAll(spec, sortOrder);

        Set<String> fieldSet = new HashSet<>();
        if (fields != null && !fields.isEmpty()) {
            fieldSet.addAll(Arrays.asList(fields.split(",")));
        }
        if (search != null && !search.isEmpty()) {
            List<String> searchKeys = Arrays.stream(search.split("&"))
                    .map(filter -> filter.split("=")[0])
                    .map(key -> key.replaceAll("(-gte|-lte|-equal)$", ""))
                    .toList();
            fieldSet.addAll(searchKeys);
        }

        return JobDataMapper.mapToJobDataDTO(all, fieldSet);
    }

    private Specification<JobData> buildSpecificationFromSearch(String search) {
        if (search == null || search.isEmpty()) {
            return Specification.where(null);
        }

        List<String> filters = Arrays.asList(search.split(","));
        Specification<JobData> spec = Specification.where(null);

        for (String filter : filters) {
            String[] filterParts = filter.split("=");
            if (filterParts.length != 2) {
                continue;
            }
            String key = filterParts[0].trim();
            String value = filterParts[1].trim();

            switch (key) {
                case "job-title":
                    spec = spec.and(JobDataSpecifications.hasJobTitle(value));
                    break;
                case "gender":
                    spec = spec.and(JobDataSpecifications.hasGender(value));
                    break;
                case "salary-lessthan":
                    spec = spec.and(JobDataSpecifications.salaryLessThan(new BigDecimal(value)));
                    break;
                case "salary-equal":
                    spec = spec.and(JobDataSpecifications.salaryEqualTo(new BigDecimal(value)));
                    break;
                case "salary-gte":
                    spec = spec.and(JobDataSpecifications.salaryGreaterThan(new BigDecimal(value)));
                    break;
            }
        }

        return spec;
    }


    // Filter by one or more fields/attributes (e.g. /job_data?salary[gte]=120000)
    // (Show only filtered row. Expected filter able column: job title, salary, gender )
    @GetMapping("/all")
    public List<JobDataFilter> getFilteredJobData(
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestParam(value = "salaryMoreThan", required = false) BigDecimal salaryGte,
            @RequestParam(value = "salaryLessThan", required = false) BigDecimal salaryLte,
            @RequestParam(value = "gender", required = false) String gender) {

        Specification<JobData> spec = Specification.where(null);
        if (jobTitle != null) {
            spec = spec.and(JobDataSpecifications.hasJobTitle(jobTitle));
        }
        if (salaryGte != null) {
            spec = spec.and(JobDataSpecifications.salaryGreaterThan(salaryGte));
        }
        if (salaryLte != null) {
            spec = spec.and(JobDataSpecifications.salaryLessThan(salaryLte));
        }
        if (gender != null) {
            spec = spec.and(JobDataSpecifications.hasGender(gender));
        }

        return mapToJobDataFilter(repository.findAll(spec));


    }

    /**
     *  Filter by a sparse fields/attributes (e.g. /job_data?fields=job_title,gender,salary)
     *  (Show only filtered column)
     */
    @GetMapping("/filtered")
    public List<JobDataDTO> getJobDataFiltered(
            @RequestParam(value = "fields") String fields) {
        List<JobData> jobDataList = repository.findAll();
        Set<String> fieldSet = new HashSet<>();
        if (fields != null && !fields.isEmpty()) {
            fieldSet.addAll(Arrays.asList(fields.split(",")));
        }

        return JobDataMapper.mapToJobDataDTO(jobDataList, fieldSet);
    }

    /**
     * Sort by one or more fields/attributes (e.g. /job_data?sort=job_title&sort_type=DESC)
     * */
    @GetMapping("sorted")
    public List<JobData> getJobDataSort(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sort_type", required = false) String sortType) {

        Sort.Direction direction = (sortType != null && sortType.equalsIgnoreCase("DESC")) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create the sorting object
        Sort sortOrder = (sort != null && !sort.isEmpty()) ? Sort.by(direction, (sort)) : Sort.unsorted();

        return repository.findAll(sortOrder);
    }


    private List<JobDataFilter> mapToJobDataFilter(List<JobData> jobDataList) {
        return jobDataList.stream()
                .map(jobData -> new JobDataFilter(
                        jobData.getJobTitle(),
                        jobData.getSalary(),
                        jobData.getGender()))
                .collect(Collectors.toList());
    }

}
