package com.example.employeemanagement.controller;

import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
public class DeleteControllerTest {

    @InjectMocks
    private DeleteController deleteController;

    @Mock
    private EmployeeService employeeService;

    private Long employeeId;

    @BeforeEach
    public void setup() {
        employeeId = 1L;
    }

    @Test
    public void testDeleteEmployeeSuccess() throws EmployeeNotFoundException {
        String viewName = deleteController.deleteEmployee(employeeId);
        assertEquals("redirect:/employees", viewName);
    }

    @Test
    public void testDeleteEmployeeNotFound() throws EmployeeNotFoundException {
        doThrow(new EmployeeNotFoundException("Employee not found")).when(employeeService).deleteEmployees(employeeId);
        try {
            deleteController.deleteEmployee(employeeId);
        } catch (EmployeeNotFoundException e) {
            assertEquals("Employee not found", e.getMessage());
        }
    }
}
