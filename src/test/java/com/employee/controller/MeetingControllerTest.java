package com.employee.controller;

import com.employee.EmployeeApplication;
import com.employee.entity.Department;
import com.employee.entity.Employee;
import com.employee.entity.Meeting;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.MeetingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeApplication.class)
@WebAppConfiguration
public class MeetingControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    private void generateObjects() {
        Department department = new Department();
        department.setDescription("desc1");
        department.setName("name1");

        Meeting meeting = new Meeting();
        meeting.setDescription("desc2");
        meeting.setName("name2");

        departmentRepository.save(department);
        meetingRepository.save(meeting);
    }

    @Test
    public void shouldThrowContentNotFoundExceptionWhenDepartmentIdIsNull() throws Exception {
        mockMvc.perform(post("/meeting/joinMeeting")
                .param("departmentId", "2")
                .param("meetingId", "1")
                .contentType(contentType)

        ).andExpect(status().isNotFound());
    }

    @Test
    public void shouldThrowContentNotFoundExceptionWhenMeetingIdIsNull() throws Exception {
        mockMvc.perform(post("/meeting/joinMeeting")
                .param("departmentId", "1")
                .param("meetingId", "2")
                .contentType(contentType)

        ).andExpect(status().isNotFound());
    }

    @Test
    public void shouldJoinMeeting() throws Exception {
        generateObjects();

        mockMvc.perform(post("/meeting/joinMeeting")
                .param("departmentId", "1")
                .param("meetingId", "1")
                .contentType(contentType)

        ).andExpect(status().isOk());
    }

    @Test
    public void shouldSaveEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("desc");

        this.mockMvc.perform(
                post("/employee/save")
                .content(this.json(employee))
                .param("departmentId", "1")
        ).andExpect(status().isOk());
    }
}