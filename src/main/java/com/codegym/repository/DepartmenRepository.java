package com.codegym.repository;

import com.codegym.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmenRepository extends PagingAndSortingRepository<Department, Long> {
    Department findByName(String name);
}
