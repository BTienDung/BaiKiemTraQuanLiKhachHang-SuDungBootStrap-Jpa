package com.codegym.controller;

import com.codegym.model.Department;
import com.codegym.model.Employee;
import com.codegym.service.DepartmentService;
import com.codegym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class DepartmentController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;
    @GetMapping("/list-department")
    public ModelAndView listDepartment(Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("/department/list");
        Page<Department> page = departmentService.findAll(pageable);
        modelAndView.addObject("departments", page);
        return modelAndView;
    }

    @GetMapping("/create-department")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/department/create");
        modelAndView.addObject("department", new Department());
        return modelAndView;
    }

    @PostMapping("/create-department")
    public ModelAndView createDepartment(@ModelAttribute Department department){
        ModelAndView modelAndView = new ModelAndView("/department/create");
        departmentService.save( department);
        modelAndView.addObject("message","Them thanh cong!");
        return modelAndView;
    }

    @GetMapping("/edit-department/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/department/edit");
        Department department = departmentService.findById(id);
        modelAndView.addObject("department", department);
        return modelAndView;
    }
    @PostMapping("/edit-department")
    public ModelAndView editDepartment(@ModelAttribute Department department){
        ModelAndView modelAndView = new ModelAndView("/department/edit");
        departmentService.save(department);
        modelAndView.addObject("message", "Sua thanh cong!");
        return modelAndView;
    }

    @GetMapping("delete-department/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/department/delete");
        Department department = departmentService.findById(id);
        modelAndView.addObject("department", department);
        return modelAndView;
    }
    @PostMapping("/delete-department")
    public ModelAndView deleteDepartment(@ModelAttribute Department department){
        ModelAndView modelAndView = new ModelAndView("/department/delete");
        departmentService.delete(department.getId());
        modelAndView.addObject("message", "Xoa thanh cong!");
        modelAndView.addObject("department", new Department());
        return modelAndView;
    }

    @GetMapping("/view-department/{id}")
    public ModelAndView viewAll(@PathVariable Long id, Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("/department/view");
        Department department = departmentService.findById(id);
        Page<Employee> employees = employeeService.findByDepartment(department, pageable);
        modelAndView.addObject("departments", department);
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }
}
