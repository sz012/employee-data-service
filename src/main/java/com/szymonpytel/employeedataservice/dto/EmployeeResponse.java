package com.szymonpytel.employeedataservice.dto;

import com.szymonpytel.employeedataservice.entity.Employee;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender
) {
    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDateOfBirth(),
                employee.getGender()
        );
    }
}