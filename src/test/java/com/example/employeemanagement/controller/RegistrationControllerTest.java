package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BindingResult bindingResult;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setup() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("test@example.com");
        employeeDTO.setName("Test");
        employeeDTO.setPassword("password123");
        employeeDTO.setDepartment("IT");
    }

    @Test
    public void testRegisterEmployeeSuccess() throws EmployeeNotFoundException {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.EmployeeDtoToEmployee(employeeDTO)).thenReturn(new Employee());
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(new Employee());
        String viewName = registrationController.registerEmployee(employeeDTO, bindingResult);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testRegisterEmployeeWithErrors() throws EmployeeNotFoundException {
        when(bindingResult.hasErrors()).thenReturn(true);
        String viewName = registrationController.registerEmployee(employeeDTO, bindingResult);
        assertEquals("register", viewName);
    }

    @Test
    public void testRegisterEmployeeException() throws EmployeeNotFoundException {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employeeService.EmployeeDtoToEmployee(employeeDTO)).thenReturn(new Employee());
        when(employeeService.saveEmployee(any(Employee.class)))
                .thenThrow(new EmployeeNotFoundException("Employee registration failed"));
        EmployeeNotFoundException thrown = assertThrows(
                EmployeeNotFoundException.class,
                () -> registrationController.registerEmployee(employeeDTO, bindingResult),
                "Expected registerEmployee() to throw, but it didn't"
        );
        assertEquals("Employee registration failed", thrown.getMessage());
    }
}
