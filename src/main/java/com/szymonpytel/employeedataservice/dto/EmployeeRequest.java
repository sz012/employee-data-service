package com.szymonpytel.employeedataservice.dto;

import com.szymonpytel.employeedataservice.entity.Employee;
import java.time.LocalDate;

public record EmployeeRequest(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String socialSecurityNumber
) {
    public Employee toEmployee() {
        return new Employee(firstName, lastName, dateOfBirth, gender, socialSecurityNumber);
    }
}