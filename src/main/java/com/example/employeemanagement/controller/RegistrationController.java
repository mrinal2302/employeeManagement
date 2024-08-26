package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private EmployeeService employeeService;
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping("/register")
    public String registerEmployee(@ModelAttribute("employee") EmployeeDTO employeeDTO, BindingResult result) throws EmployeeNotFoundException {
    	logger.info("Starting registration process for employee with email: {}", employeeDTO.getEmail());
        if (result.hasErrors()) {
        	 logger.warn("Registration form has errors for employee with email: {}", employeeDTO.getEmail());
            return "register";
        }

        Employee employee = employeeService.EmployeeDtoToEmployee(employeeDTO);

        try {
            employeeService.saveEmployee(employee);
            logger.info("Employee with email: {} successfully registered", employeeDTO.getEmail());
        } catch (EmployeeNotFoundException e) {
            logger.error("Failed to register employee with email: {}", employeeDTO.getEmail(), e);
            throw e;
        }

        return "redirect:/login";
    }



}

