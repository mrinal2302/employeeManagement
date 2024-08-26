package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    public void testGetOrCreateDepartmentWhenExists() {
        String departmentName = "IT";
        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName(departmentName);
        when(departmentRepository.findByName(departmentName)).thenReturn(Optional.of(existingDepartment));
        Department result = departmentService.getOrCreateDepartment(departmentName);
        assertNotNull(result);
        assertEquals(departmentName, result.getName());
        verify(departmentRepository, times(1)).findByName(departmentName);
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    public void testGetOrCreateDepartmentWhenNotExists() {
        String departmentName = "HR";
        Department newDepartment = new Department();
        newDepartment.setName(departmentName);
        when(departmentRepository.findByName(departmentName)).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Department result = departmentService.getOrCreateDepartment(departmentName);
        assertNotNull(result);
        assertEquals(departmentName, result.getName());
        verify(departmentRepository, times(1)).findByName(departmentName);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    public void testGetAllDepartments() {
        Department dept1 = new Department();
        dept1.setId(1L);
        dept1.setName("IT");
        Department dept2 = new Department();
        dept2.setId(2L);
        dept2.setName("HR");
        when(departmentRepository.findAll()).thenReturn(List.of(dept1, dept2));
        List<DepartmentDTO> result = departmentService.getAllDepartments();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("IT", result.get(0).getName());
    }

    @Test
    public void testGetDepartmentByIdFound() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Finance");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        DepartmentDTO result = departmentService.getDepartmentById(1L);
        assertNotNull(result);
        assertEquals("Finance", result.getName());
    }

    @Test
    public void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentService.getDepartmentById(1L)
        );
        assertEquals("Department not found with id: 1", thrown.getMessage());
    }

    @Test
    public void testAddDepartment() {
        String departmentName = "Marketing";
        departmentService.addDepartment(departmentName);
        Department department = new Department();
        department.setName(departmentName);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    public void testUpdateDepartment() {
        Long departmentId = 1L;
        String newName = "Operations";
        Department existingDepartment = new Department();
        existingDepartment.setId(departmentId);
        existingDepartment.setName("Old Name");
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(existingDepartment));
        departmentService.updateDepartment(departmentId, newName);
        assertEquals(newName, existingDepartment.getName());
        verify(departmentRepository, times(1)).save(existingDepartment);
    }

    @Test
    public void testUpdateDepartmentNotFound() {
        Long departmentId = 1L;
        String newName = "Logistics";
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> departmentService.updateDepartment(departmentId, newName)
        );
        assertEquals("Department not found with id: 1", thrown.getMessage());
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    public void testDeleteDepartment() {
        Long departmentId = 1L;
        departmentService.deleteDepartment(departmentId);
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }
}
