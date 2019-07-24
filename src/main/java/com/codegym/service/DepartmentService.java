package com.codegym.service;

import com.codegym.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Page<Department> findAll(Pageable pageable);
    void save(Department department);
    Department findById(Long id);
    void delete(Long id);
    Department findByName(String name);
}
