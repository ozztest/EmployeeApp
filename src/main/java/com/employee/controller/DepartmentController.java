package com.employee.controller;

import com.employee.entity.Department;
import com.employee.entity.Meeting;
import com.employee.exception.ContentNotFoundException;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;

@RestController @RequestMapping(value = "department") public class DepartmentController {

	@Autowired private DepartmentRepository departmentRepository;
	@Autowired private MeetingRepository meetingRepository;
	@RequestMapping(value = "findAll") public Iterable<Department> findAll() {
		return departmentRepository.findAll();
	}

	@RequestMapping(value = "find/{id}") public Department findOne(Long id) {
		try {
			Department department = departmentRepository.findOne(id);
			return department;
		} catch (Exception e) {
			throw new ContentNotFoundException(String.format("Meeting not found with meeting id : %s", id));
		}
	}

	@RequestMapping(value = "save/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Department save(@RequestBody Department department, @PathVariable("id") Long meetingId) {
		try{
			Meeting meeting = meetingRepository.findOne(meetingId);
			department.setMeetings(new HashSet<Meeting>(){
				{add(meeting);}
			});
		}catch (Exception e){
			throw new ContentNotFoundException(String.format("Meeting not found with meeting id : %s", meetingId));
		}
		return departmentRepository.save(department);
	}

	@RequestMapping(value = "delete", method = RequestMethod.DELETE) public void delete(Long departmentId) {
		departmentRepository.delete(departmentId);
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT) public Department update(@RequestBody Department department) {
		return departmentRepository.save(department);
	}
}
