package com.omar.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omar.backend.dto.EmployeeRequest;
import com.omar.backend.models.Employee;
import com.omar.backend.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Value("${api.base-path}")
    private String apiBasePath;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(employeeController, "apiBasePath", "/api/v1");

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testCreateEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");
        Employee response = new Employee();

        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeRequest.class));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");

        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(request));

        mockMvc.perform(get("/api/v1/employees/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");

        when(employeeService.getEmployeeById(1)).thenReturn(request);

        mockMvc.perform(get("/api/v1/employees/details/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));

        verify(employeeService, times(1)).getEmployeeById(1);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe Updated", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");
        Employee response = new Employee();

        when(employeeService.updateEmployee(eq(1), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe Updated"));

        verify(employeeService, times(1)).updateEmployee(eq(1), any(EmployeeRequest.class));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1);

        mockMvc.perform(delete("/api/v1/employees/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1);
    }

    @Test
    public void testSearchEmployees() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");

        when(employeeService.searchEmployees(1, null, null , null)).thenReturn(Collections.singletonList(request));

        mockMvc.perform(get("/api/v1/employees/search?name=john")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));

        verify(employeeService, times(1)).searchEmployees(null,null,null, null);
    }

    @Test
    public void testFilterEmployees() throws Exception {
        EmployeeRequest request = new EmployeeRequest(null, "John Doe", "Developer", "IT", LocalDate.now(), "Full-time", "john.doe@example.com", "123 Street");

        when(employeeService.filterEmployees("IT", "Full-time", null)).thenReturn(Collections.singletonList(request));

        mockMvc.perform(get("/api/v1/employees/filter?department=IT&employmentStatus=Full-time")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));

        verify(employeeService, times(1)).filterEmployees("IT", "Full-time", null);
    }
}
