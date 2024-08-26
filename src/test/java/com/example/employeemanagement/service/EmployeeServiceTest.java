package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EditEmployeeDTO;
import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetAllEmployees() {
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setName("Test");
        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setName("Test");
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));
        List<Employee> result = employeeService.getAllEmployees();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Employee result = employeeService.getEmployeeById(1L);
        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        Employee result = employeeService.getEmployeeById(1L);
        assertNull(result);
    }

    @Test
    public void testSaveEmployee() throws EmployeeNotFoundException {
        Employee employee = new Employee();
        employee.setEmail("test@gmail.com");
        when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(null);
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee result = employeeService.saveEmployee(employee);
        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
    }

    @Test
    public void testSaveEmployeeEmailAlreadyInUse() {
        Employee employee = new Employee();
        employee.setEmail("test@gmail.com");
        when(employeeRepository.findByEmail("test@gmail.com")).thenReturn(new Employee());
        EmployeeNotFoundException thrown = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.saveEmployee(employee)
        );
        assertEquals("Email is already in use", thrown.getMessage());
    }

    @Test
    public void testEmployeeDtoToEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Test");
        employeeDTO.setEmail("test@gmail.com");
        employeeDTO.setPassword("password123");
        employeeDTO.setDepartment("HR");
        Department department = new Department();
        department.setName("HR");
        when(departmentService.getOrCreateDepartment("HR")).thenReturn(department);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        Employee result = employeeService.EmployeeDtoToEmployee(employeeDTO);
        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals("test@gmail.com", result.getEmail());
    }

    @Test
    public void testConvertEditToDTO() {
        Department department = new Department();
        department.setId(1L);
        department.setName("HR");
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setDepartment(department);
        EditEmployeeDTO result = employeeService.convertEditToDTO(employee);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("HR", result.getDepartment().getName());
    }

    @Test
    public void testConvertToDTO() {
        Department department = new Department();
        department.setName("HR");
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Test");
        employee.setEmail("test@gmail.com");
        employee.setDepartment(department);
        EmployeeDTO result = employeeService.convertToDTO(employee);
        assertNotNull(result);
        assertEquals("HR", result.getDepartment());
    }
}

