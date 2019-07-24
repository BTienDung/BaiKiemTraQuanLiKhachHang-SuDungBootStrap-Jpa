package com.codegym.controller;

import com.codegym.model.Department;
import com.codegym.model.Employee;
import com.codegym.model.EmployeeForm;
import com.codegym.service.DepartmentService;
import com.codegym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;


@Controller
@PropertySource("classpath:global_config_app.properties")
public class EmployeeController {
    @Autowired
    Environment env;

    @ModelAttribute("departments")
    public Page<Department> categories(Pageable pageable){
        return departmentService.findAll(pageable);
    }
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;
    @RequestMapping("/list-employee")
    public ModelAndView showAll(@PageableDefault(size = 10) Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees", employeeService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/create-employee")
    public ModelAndView showFormCreate(){
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        return modelAndView;
    }

    @PostMapping("/create-employee")
    public ModelAndView createEmployee(@ModelAttribute EmployeeForm employeeForm, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        // lay ten file
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        // luu file len server
        try {
            //multipartFile.transferTo(imageFile);
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Employee employee = new Employee(employeeForm.getId(),employeeForm.getName(),employeeForm.getBirthDate() ,employeeForm.getAddress(),employeeForm.getSalary(), fileName, employeeForm.getDepartment());

        employeeService.save(employee);
        modelAndView.addObject("message", "Save Success!");
        modelAndView.addObject("employeeForm", new EmployeeForm());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        Employee employeeFind = employeeService.findById(id);
        EmployeeForm employeeForm = new EmployeeForm(employeeFind.getId(), employeeFind.getName(), employeeFind.getBirthDate(), employeeFind.getAddress(), null, employeeFind.getSalary(), employeeFind.getDepartment());
        modelAndView.addObject("employee", employeeFind);
        modelAndView.addObject("employeeForm", employeeForm);
        return modelAndView;
    }

    @PostMapping("/edit-employee")
    public ModelAndView edit(@ModelAttribute EmployeeForm employeeForm, BindingResult result, Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        // lay ten file
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();
        // luu file len server
        try {
            //multipartFile.transferTo(imageFile);
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Employee employeeOld = employeeService.findById(employeeForm.getId());
        if (fileName== ""){
            fileName = employeeOld.getImage();
        }

        Employee employee = new Employee(employeeForm.getId(),employeeForm.getName(),employeeForm.getBirthDate() ,employeeForm.getAddress(),employeeForm.getSalary(), fileName, employeeForm.getDepartment());
        employeeService.save(employee);
        Page<Employee> employees = employeeService.findAll(pageable);
        modelAndView.addObject("employees", employees);
        modelAndView.addObject("message", "Edit Success");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/employee/delete");
        Employee employeeFind = employeeService.findById(id);
        EmployeeForm employeeForm = new EmployeeForm(employeeFind.getId(), employeeFind.getName(), employeeFind.getBirthDate(), employeeFind.getAddress(), null, employeeFind.getSalary(), employeeFind.getDepartment());
        modelAndView.addObject("employee", employeeFind);
        modelAndView.addObject("employeeForm", employeeForm);
        return modelAndView;
    }
    @PostMapping("/delete-employee")
    public ModelAndView deleteEmployee(@ModelAttribute Employee employee, BindingResult result, Pageable pageble){
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        employeeService.delete(employee.getId());
        Page<Employee> employees =  employeeService.findAll(pageble);
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView showDetail(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/employee/detail");
        Employee employeeFind = employeeService.findById(id);
        EmployeeForm employeeForm = new EmployeeForm(employeeFind.getId(), employeeFind.getName(), employeeFind.getBirthDate(), employeeFind.getAddress(), null, employeeFind.getSalary(), employeeFind.getDepartment());
        modelAndView.addObject("employeeForm", employeeForm);
        modelAndView.addObject("employeeFind", employeeFind);
        return modelAndView;
    }
    @PostMapping("/search-employee")
    public ModelAndView searchEmployee(@RequestParam("search") String search, Pageable pageable){
        Department department = departmentService.findByName(search);
        Page<Employee> employees = employeeService.findByDepartment(department, pageable);
        ModelAndView modelAndView = new ModelAndView("/employee/search");
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }

}
