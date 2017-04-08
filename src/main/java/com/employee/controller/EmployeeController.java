package com.employee.controller;

import com.employee.entity.Department;
import com.employee.entity.Employee;
import com.employee.exception.ContentNotFoundException;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping(value = "findAll")
    public Iterable<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Employee save(@Valid Employee employee, Long departmentId) {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null) {
            throw new ContentNotFoundException(String.format("Department not found with department id : %s", departmentId));
        }
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }
}
