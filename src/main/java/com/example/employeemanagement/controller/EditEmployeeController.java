package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EditEmployeeDTO;
import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/update")
public class EditEmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    private static final Logger logger = LoggerFactory.getLogger(EditEmployeeController.class);

    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
    	logger.info("Showing edit form for employee with id: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
        	logger.warn("Employee with id: {} not found, redirecting to employee list", id);
            return "redirect:/employees";
        }
        EditEmployeeDTO editEmployeeDTO = employeeService.convertEditToDTO(employee);
        model.addAttribute("employee", editEmployeeDTO);
        logger.info("Edit form populated for employee with id: {}", id);
        return "edit-employee";
    }

    @PostMapping("/edit")
    public String updateEmployee(@ModelAttribute EditEmployeeDTO editEmployeeDTO, Model model) {
    	logger.info("Updating employee with id: {}", editEmployeeDTO.getId());
        try {
            employeeService.EditEmployeeDtoToEmployee(editEmployeeDTO);
            logger.info("Employee with id: {} successfully updated", editEmployeeDTO.getId());
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            logger.error("Error updating employee with id: {}", editEmployeeDTO.getId(), e);
            return "edit-employee";
        }
    }
}
