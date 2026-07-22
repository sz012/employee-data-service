package com.szymonpytel.employeedataservice.controller;

import com.szymonpytel.employeedataservice.dto.EmployeeRequest;
import com.szymonpytel.employeedataservice.dto.EmployeeResponse;
import com.szymonpytel.employeedataservice.entity.Employee;
import com.szymonpytel.employeedataservice.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee saved = employeeService.createEmployee(request.toEmployee());
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeResponse.from(saved));
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return EmployeeResponse.from(employee);
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(EmployeeResponse::from)
                .toList();
    }
}