package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentDTO;
import com.example.employeemanagement.dto.EditEmployeeDTO;
import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exceptions.EmployeeNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<Employee> getAllEmployees() {
    	logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
    	logger.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee saveEmployee(Employee employee) throws EmployeeNotFoundException {
    	logger.info("Saving new employee with email: {}", employee.getEmail());
        if (employeeRepository.findByEmail(employee.getEmail()) != null) {
        	logger.error("Email {} is already in use", employee.getEmail());
            throw new EmployeeNotFoundException("Email is already in use");
        }
        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee saved with id: {}", savedEmployee.getId());
        return savedEmployee;
    }


    public Employee EmployeeDtoToEmployee(EmployeeDTO employeeDTO) {
    	logger.info("Converting EmployeeDTO to Employee for email: {}", employeeDTO.getEmail());
        Department department = departmentService.getOrCreateDepartment(employeeDTO.getDepartment());

        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .email(employeeDTO.getEmail())
                .password(passwordEncoder.encode(employeeDTO.getPassword()))
                .department(department)
                .build();

        return employee;
    }


    public void editEmployee(EditEmployeeDTO editEmployeeDTO) throws EmployeeNotFoundException {
    	logger.info("Editing employee with id: {}", editEmployeeDTO.getId());
        Employee updatedEmployee = EditEmployeeDtoToEmployee(editEmployeeDTO);
        Employee existingEmployee = employeeRepository.findById(updatedEmployee.getId())
        		.orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", updatedEmployee.getId());
                    return new EmployeeNotFoundException("Employee not found with id: " + updatedEmployee.getId());
                });
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        
        if (editEmployeeDTO.getPassword() != null && !editEmployeeDTO.getPassword().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(editEmployeeDTO.getPassword()));
        }

        employeeRepository.save(existingEmployee);
        logger.info("Employee with id: {} has been updated", updatedEmployee.getId());
    }



    public EditEmployeeDTO convertEditToDTO(Employee employee) {
    	logger.info("Converting Employee to EditEmployeeDTO for id: {}", employee.getId());
        Department department = employee.getDepartment(); 

        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .build();

        return EditEmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(departmentDTO) 
                .build();
    }


    public EmployeeDTO convertToDTO(Employee employee) {
    	 logger.info("Converting Employee to EmployeeDTO for id: {}", employee.getId());
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment().getName())
                .build();
    }

    public Employee EditEmployeeDtoToEmployee(EditEmployeeDTO employeeDTO) {
    	logger.info("Converting EditEmployeeDTO to Employee for id: {}", employeeDTO.getId());
        Department department = departmentService.findOrCreateDepartment(employeeDTO.getDepartment().getName());

        Employee employee = Employee.builder()
                .id(employeeDTO.getId()) 
                .name(employeeDTO.getName())
                .email(employeeDTO.getEmail())
                .department(department)
                .build();

        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
        	employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }
        
        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Employee with id: {} has been updated", updatedEmployee.getId());
        return updatedEmployee;
    }

    public void deleteEmployees(Long id) throws EmployeeNotFoundException {
    	logger.info("Deleting employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
        employeeRepository.delete(employee);
        logger.info("Employee with id: {} has been deleted", id);
    }
}
