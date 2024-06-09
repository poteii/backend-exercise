package com.exercise.api.specification;

import com.exercise.api.entity.JobData;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.exercise.api.specification.JobDataMapper.convertToCamelCase;

public class JobDataSpecifications {

    public static Specification<JobData> buildSpecificationFromSearch(String search) {
        if (search == null || search.isEmpty()) {
            return Specification.where(null);
        }

        List<Specification<JobData>> specifications = new ArrayList<>();
        String[] filters = search.split("&");

        for (String filter : filters) {
            String[] filterParts = filter.split("=");
            if (filterParts.length != 2) {
                continue;
            }
            String key = filterParts[0].trim();
            String value = filterParts[1].trim();

            try {
                if (key.endsWith("-gte")) {
                    specifications.add(numberGreaterThanOrEqualSpecification(key.replace("-gte", ""), new BigDecimal(value)));
                } else if (key.endsWith("-lte")) {
                    specifications.add(numberLessThanOrEqualSpecification(key.replace("-lte", ""), new BigDecimal(value)));
                } else if (key.endsWith("-equal")) {
                    specifications.add(numberEqualsSpecification(key.replace("-equal", ""), new BigDecimal(value)));
                } else {
                    Field field = JobData.class.getDeclaredField(convertToCamelCase(key));
                    Class<?> fieldType = field.getType();

                    if (fieldType == String.class) {
                        specifications.add(stringContainsSpecification(key, value));
                    } else if (fieldType == BigDecimal.class) {
                        specifications.add(numberEqualsSpecification(key, new BigDecimal(value)));
                    }
                }
                // Add more type checks as needed
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Error accessing field: " + key, e);
            }
        }

        Specification<JobData> result = Specification.where(null);
        for (Specification<JobData> spec : specifications) {
            result = result.and(spec);
        }
        return result;
    }


    public static Specification<JobData> hasJobTitle(String jobTitle) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("jobTitle"), jobTitle);
    }

    public static Specification<JobData> salaryGreaterThan(BigDecimal salary) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), salary);
    }

    public static Specification<JobData> salaryLessThan(BigDecimal salary) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("salary"), salary);
    }

    public static Specification<JobData> salaryEqualTo(BigDecimal salary) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("salary"), salary);
    }

    public static Specification<JobData> hasGender(String gender) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gender"), gender);
    }

    private static Specification<JobData> stringContainsSpecification(String key, String value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(convertToCamelCase(key))), "%" + value.toLowerCase() + "%");
    }

    private static Specification<JobData> numberEqualsSpecification(String key, BigDecimal value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(convertToCamelCase(key)), value);
    }

    private static Specification<JobData> numberGreaterThanOrEqualSpecification(String key, BigDecimal value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(convertToCamelCase(key.replace("-gte", ""))), value);
    }

    private static Specification<JobData> numberLessThanOrEqualSpecification(String key, BigDecimal value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(convertToCamelCase(key.replace("-lte", ""))), value);
    }

}
