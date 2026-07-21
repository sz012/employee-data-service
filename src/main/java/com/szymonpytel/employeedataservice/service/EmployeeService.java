package com.szymonpytel.employeedataservice.service;

import com.szymonpytel.employeedataservice.entity.Employee;
import com.szymonpytel.employeedataservice.exception.EmployeeNotFoundException;
import com.szymonpytel.employeedataservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}