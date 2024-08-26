package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class AddEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employeeDTO", new EmployeeDTO());
        return "add-employee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute EmployeeDTO employeeDTO, Model model) {
        try {
            Employee employee = employeeService.EmployeeDtoToEmployee(employeeDTO);
            employeeService.saveEmployee(employee);
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "add-employee";
        }
    }
}
