package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
    	logger.info("Fetching employee with id: {}", id);
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) throws EmployeeNotFoundException {
    	logger.info("Creating new employee with email: {}", employee.getEmail());
        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) throws EmployeeNotFoundException {
    	logger.info("Updating employee with id: {}", id);
        employee.setId(id);
        return employeeService.saveEmployee(employee);
    }

}
