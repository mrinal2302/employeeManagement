package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EditEmployeeDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

public class EditEmployeeControllerTest {

    @InjectMocks
    private EditEmployeeController editEmployeeController;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private Model model;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowEditEmployeeForm_EmployeeExists() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        EditEmployeeDTO editEmployeeDTO = new EditEmployeeDTO();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
        when(employeeService.convertEditToDTO(employee)).thenReturn(editEmployeeDTO);
        String viewName = editEmployeeController.showEditEmployeeForm(employeeId, model);
        assertEquals("edit-employee", viewName);
    }

    @Test
    public void testShowEditEmployeeForm_EmployeeDoesNotExist() {
        Long employeeId = 1L;
        when(employeeService.getEmployeeById(employeeId)).thenReturn(null);
        String viewName = editEmployeeController.showEditEmployeeForm(employeeId, model);
        assertEquals("redirect:/employees", viewName);
    }

    @Test
    public void testUpdateEmployee_Success() {
        EditEmployeeDTO editEmployeeDTO = new EditEmployeeDTO();
        String viewName = editEmployeeController.updateEmployee(editEmployeeDTO, model);
        assertEquals("redirect:/employees", viewName);
    }

    @Test
    public void testUpdateEmployee_Failure() {
        EditEmployeeDTO editEmployeeDTO = new EditEmployeeDTO();
        Exception exception = new RuntimeException("Update failed");
        doThrow(exception).when(employeeService).EditEmployeeDtoToEmployee(any(EditEmployeeDTO.class));
        String viewName = editEmployeeController.updateEmployee(editEmployeeDTO, model);
        assertEquals("edit-employee", viewName);
    }
}
