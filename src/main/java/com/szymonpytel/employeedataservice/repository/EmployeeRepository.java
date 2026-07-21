package com.szymonpytel.employeedataservice.repository;

import com.szymonpytel.employeedataservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}