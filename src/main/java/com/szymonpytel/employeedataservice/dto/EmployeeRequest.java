package com.szymonpytel.employeedataservice.dto;

import com.szymonpytel.employeedataservice.entity.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "SSN is required")
        @Pattern(regexp = "\\d{3}-\\d{2}-\\d{4}", message = "SSN must match format XXX-XX-XXXX")
        String socialSecurityNumber
) {
    public Employee toEmployee() {
        return new Employee(firstName, lastName, dateOfBirth, gender, socialSecurityNumber);
    }
}