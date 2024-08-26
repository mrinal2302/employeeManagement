package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddEmployeeControllerTest {

    @InjectMocks
    private AddEmployeeController addEmployeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Model model;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setup() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("test@gmail.com");
        employeeDTO.setName("Test");
        employeeDTO.setPassword("password123");
        employeeDTO.setDepartment("IT");
    }

    @Test
    public void testShowAddEmployeeForm() {
        String viewName = addEmployeeController.showAddEmployeeForm(model);
        assertEquals("add-employee", viewName);
    }

    @Test
    public void testAddEmployeeSuccess() throws Exception {
        Employee employee = new Employee();
        when(employeeService.EmployeeDtoToEmployee(employeeDTO)).thenReturn(employee);
        when(employeeService.saveEmployee(employee)).thenReturn(employee);
        String viewName = addEmployeeController.addEmployee(employeeDTO, model);
        assertEquals("redirect:/employees", viewName);
    }

    @Test
    public void testAddEmployeeFailure() throws Exception {
        when(employeeService.EmployeeDtoToEmployee(employeeDTO)).thenReturn(new Employee());
        when(employeeService.saveEmployee(any(Employee.class))).thenThrow(new RuntimeException("Database error"));
        String viewName = addEmployeeController.addEmployee(employeeDTO, model);
        assertEquals("add-employee", viewName);
    }
}
