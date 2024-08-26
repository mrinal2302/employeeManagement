package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Test");
        employee.setEmail("test@example.com");
    }

    @Test
    public void testGetEmployeeById() {
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        Employee result = employeeController.getEmployeeById(1L);
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testCreateEmployee() throws EmployeeNotFoundException {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
        Employee result = employeeController.createEmployee(employee);
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testUpdateEmployee() throws EmployeeNotFoundException {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
        Employee result = employeeController.updateEmployee(1L, employee);
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

}
