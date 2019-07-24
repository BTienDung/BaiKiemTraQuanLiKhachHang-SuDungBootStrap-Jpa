package com.codegym.service;

import com.codegym.model.Department;
import com.codegym.repository.DepartmenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmenRepository departmenRepository;

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmenRepository.findAll(pageable);
    }

    @Override
    public void save(Department department) {
        departmenRepository.save(department);
    }

    @Override
    public Department findById(Long id) {
        return departmenRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        departmenRepository.delete(id);
    }

    @Override
    public Department findByName(String name) {
        return departmenRepository.findByName(name);
    }

}
