package com.exercise.api.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class JobDataFilter {

    private String jobTitle;
    private BigDecimal salary;
    private String gender;


}
