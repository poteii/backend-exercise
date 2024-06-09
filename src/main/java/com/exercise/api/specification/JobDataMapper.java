package com.exercise.api.specification;

import com.exercise.api.dto.JobDataDTO;
import com.exercise.api.entity.JobData;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JobDataMapper {

    private static final Map<String, String> hyphenatedToJsonPropertyMap = new HashMap<>();

    static {
        initializeMapping();
    }

    private static void initializeMapping() {
        Field[] fields = JobData.class.getDeclaredFields();
        for (Field field : fields) {
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                String jsonPropertyName = jsonProperty.value();
                String hyphenatedFieldName = convertToHyphenated(field.getName());
                hyphenatedToJsonPropertyMap.put(hyphenatedFieldName, jsonPropertyName);
            }
        }
    }

    public static String getJsonPropertyName(String hyphenatedFieldName) {
        return hyphenatedToJsonPropertyMap.getOrDefault(hyphenatedFieldName, hyphenatedFieldName);
    }

    public static List<JobDataDTO> mapToJobDataDTO(List<JobData> jobDataList, Set<String> fields) {
        return jobDataList.stream()
                .map(jobData -> {
                    JobDataDTO dto = new JobDataDTO();
                    fields.forEach(field -> {
                        try {
                            String jsonFieldName = getJsonPropertyName(field);
                            Field entityField = JobData.class.getDeclaredField(convertToCamelCase(field));
                            entityField.setAccessible(true);
                            dto.addField(jsonFieldName, entityField.get(jobData));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException("Error accessing field: " + field, e);
                        }
                    });
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public static String convertToCamelCase(String hyphenated) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : hyphenated.toCharArray()) {
            if (c == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String convertToHyphenated(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append('-').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
