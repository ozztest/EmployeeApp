package com.employee.controller;

import com.employee.entity.Department;
import com.employee.entity.Meeting;
import com.employee.exception.ContentNotFoundException;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "meeting")
public class MeetingController {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping(value = "findAll")
    public Iterable<Meeting> findAll() {
        return meetingRepository.findAll();
    }

    @RequestMapping(value = "joinMeeting", method = RequestMethod.POST)
    public void joinMeeting(Long meetingId, Long departmentId) {
        Meeting meeting = meetingRepository.findOne(meetingId);
        Department department = departmentRepository.findOne(departmentId);
        if (department == null || meeting == null) {
            throw new ContentNotFoundException(String.format("Department or meeting not found with department id : %s meeting id : %s", departmentId, meetingId));
        }

        meeting.getDepartments().add(department);

        meetingRepository.save(meeting);
    }
}
