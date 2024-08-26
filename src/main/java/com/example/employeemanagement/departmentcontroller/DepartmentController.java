package com.example.employeemanagement.departmentcontroller;

import com.example.employeemanagement.dto.DepartmentDTO;
import com.example.employeemanagement.exceptions.DepartmentNotFoundException;
import com.example.employeemanagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments";
    }
    @GetMapping("/add")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("departmentDTO", new DepartmentDTO()); 
        return "add_department"; 
    }

    @PostMapping("/add")
    public String addDepartment(@RequestParam("name") String name) {
        departmentService.getOrCreateDepartment(name);
        return "redirect:/departments";
    }
    @GetMapping("/edit/{id}")
    public String showEditDepartmentForm(@PathVariable("id") Long id, Model model) throws DepartmentNotFoundException {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        model.addAttribute("departmentDTO", departmentDTO);
        return "edit_department"; 
    }
    @PutMapping("/edit/{id}")
    public String updateDepartment(@PathVariable("id") Long id, @RequestParam("name") String name) throws DepartmentNotFoundException {
        departmentService.updateDepartment(id, name);
        return "redirect:/departments";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}
