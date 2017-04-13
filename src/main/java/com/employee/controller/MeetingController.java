package com.employee.controller;

import com.employee.entity.Department;
import com.employee.entity.Meeting;
import com.employee.exception.ContentNotFoundException;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController @RequestMapping(value = "meeting") public class MeetingController {

	@Autowired private MeetingRepository meetingRepository;

	@Autowired private DepartmentRepository departmentRepository;

	@RequestMapping(value = "findAll") public Iterable<Meeting> findAll() {
		return meetingRepository.findAll();
	}

	@RequestMapping(value = "find/{id}") public Meeting findOne(Long id) {
		try {
			Meeting meeting = meetingRepository.findOne(id);
			return meeting;
		} catch (Exception e) {
			throw new ContentNotFoundException(String.format("Meeting not found with meeting id : %s", id));
		}
	}

	@RequestMapping(value = "save/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json") public Meeting save(@RequestBody Meeting meeting, @PathVariable("id") Long departmentId) {
		try {
			Department department = departmentRepository.findOne(departmentId);
			meeting.setDepartments(new HashSet<Department>() {

				{
					add(department);
				}
			});
		} catch (Exception e) {
			throw new ContentNotFoundException(String.format("Department not found with department id : %s", departmentId));
		}
		return meetingRepository.save(meeting);
	}

	@RequestMapping(value = "joinMeeting", method = RequestMethod.POST) public void joinMeeting(Long meetingId, Long departmentId) {
		Meeting meeting = meetingRepository.findOne(meetingId);
		Department department = departmentRepository.findOne(departmentId);
		if (department == null || meeting == null) {
			throw new ContentNotFoundException(String.format("Department or meeting not found with department id : %s meeting id : %s", departmentId, meetingId));
		}

		meeting.getDepartments().add(department);

		meetingRepository.save(meeting);
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT) public Meeting update(@RequestBody Meeting meeting) {
		return meetingRepository.save(meeting);
	}

}
