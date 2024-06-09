package com.exercise.api.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.HashMap;
import java.util.Map;

//create Data Transfer Object for use in filtered
public class JobDataDTO {

    private Map<String, Object> fields = new HashMap<>();

    public void addField(String key, Object value) {
        fields.put(key, value);
    }

    //to prevent fields will display in json....
    @JsonAnyGetter
    public Map<String, Object> getFields() {
        return fields;
    }
}
