package com.employee.controller;

import com.employee.entity.Department;
import com.employee.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping(value = "findAll")
    public Iterable<Department> findAll() {
        return departmentRepository.findAll();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Department save(@Valid Department department) {
        return departmentRepository.save(department);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void delete(Long departmentId) {
        departmentRepository.delete(departmentId);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Department update(@Valid Department department) {
        return departmentRepository.save(department);
    }
}
