package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AuthController {
    @Autowired
    EmployeeService employeeService;
    
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "register"; 
    }


    @GetMapping("/employees")
    public String listEmployees(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("currentUser", userDetails.getUsername());

        return "employees";
    }
}
