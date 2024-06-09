package com.exercise.api.entity;

import com.exercise.api.utils.CustomBigDecimalDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class JobData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore // ignore because it not include in json
    private Long id;

    //use json property for mapping with json filed
    @JsonProperty("Timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yyyy H:mm:ss")
    private LocalDateTime timeStamp;

    @JsonProperty("Employer")
    private String employer;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("Job Title")
    private String jobTitle;

    @JsonProperty("Years at Employer")
    private String employerYear;

    @JsonProperty("Years of Experience")
    private String experienceYear;

    @JsonProperty("Salary")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal salary;

    @JsonProperty("Signing Bonus")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal signingBonus;

    @JsonProperty("Annual Bonus")
    @JsonDeserialize(using = CustomBigDecimalDeserializer.class)
    private BigDecimal annualBonus;

    @JsonProperty("Annual Stock Value/Bonus")
    private String annualStock;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Additional Comments")
    @Column(length = 1000)
    private String additionalComments;

}
