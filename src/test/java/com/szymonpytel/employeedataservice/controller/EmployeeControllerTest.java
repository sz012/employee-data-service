package com.szymonpytel.employeedataservice.controller;

import tools.jackson.databind.ObjectMapper;
import com.szymonpytel.employeedataservice.dto.EmployeeRequest;
import com.szymonpytel.employeedataservice.entity.Employee;
import com.szymonpytel.employeedataservice.exception.EmployeeNotFoundException;
import com.szymonpytel.employeedataservice.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void createEmployeeWithBlankFirstNameReturnsBadRequest() throws Exception {
        EmployeeRequest request = new EmployeeRequest("", "Kowalski", LocalDate.of(1990, 5, 15), "male", "123-45-6789");

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").exists());
    }

    @Test
    void createEmployeeWithValidDataReturnsCreated() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Jan", "Kowalski", LocalDate.of(1990, 5, 15), "male", "123-45-6789");
        Employee saved = new Employee("Jan", "Kowalski", LocalDate.of(1990, 5, 15), "male", "123-45-6789");
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.socialSecurityNumber").doesNotExist());
    }

    @Test
    void getEmployeeByIdWhenNotFoundReturnsNotFound() throws Exception {
        when(employeeService.getEmployeeById(999L)).thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/employees/999"))
                .andExpect(status().isNotFound());
    }
}