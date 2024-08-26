package com.example.employeemanagement.converter;

import com.example.employeemanagement.dto.DepartmentDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.service.DepartmentService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
@Component
public class StringConverter implements Converter<String, DepartmentDTO> {

    private final DepartmentService departmentService; 
    public StringConverter(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public DepartmentDTO convert(String source) {
        Department department = departmentService.findOrCreateDepartment(source);
        return new DepartmentDTO(department.getId(), department.getName());
    }
}

