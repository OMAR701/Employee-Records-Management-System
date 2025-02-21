package com.omar.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.omar.models.Employee;
import com.omar.models.LoginResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.objectMapper = new ObjectMapper();
    }
    private static String buildUrl(String endpoint) {
        return BASE_URL + (endpoint.startsWith("/") ? endpoint : "/" + endpoint);
    }

    public static String login(String username, String password) throws Exception {
        String url = BASE_URL + "/auth/login";
        URL loginUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        String payload = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(response.toString(), Map.class);
            String token = (String) responseMap.get("token");
            if (token != null && !token.isEmpty()) {
                return token;
            } else {
                throw new Exception("Token not found in the response.");
            }
        } else {
            throw new Exception("Login failed with HTTP code: " + responseCode);
        }
    }

    public static List<Employee> getAllEmployees(String token) throws Exception {
        String url = BASE_URL + "/employees/list";
        URL allEmployeesUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) allEmployeesUrl.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Accept", "application/json");

        System.out.println("Authorization Header: Bearer " + token);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return parseEmployeeList(response.toString());
        } else {
            throw new Exception("Failed to retrieve employees. Response code: " + responseCode);
        }
    }



    public static Employee addEmployee(String token, Employee employee) throws Exception {
        URL url = new URL(BASE_URL + "/employees/create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(employee);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return objectMapper.readValue(response.toString(), Employee.class);
            }
        } else if (responseCode == 403) {
            throw new Exception("You don't have permission to perform this action.");
        } else {
            throw new Exception("Failed to add employee. Response code: " + responseCode);
        }
    }

    public static Employee updateEmployee(String token, int id, Employee employee) throws Exception {
        URL url = new URL(BASE_URL + "/employees/update/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(employee);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    return objectMapper.readValue(response.toString(), Employee.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new Exception("Unauthorized: Invalid or expired token.");
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Employee not found. ID: " + id);
            } else {
                throw new Exception("Failed to update employee. Response code: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }

    public static int deleteEmployee(String token, int id) throws Exception {
        String url = BASE_URL + "/employees/delete/" + id;
        URL deleteUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) deleteUrl.openConnection();

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();
            return Integer.parseInt(response.trim());
        } else if (responseCode == 403) {
            throw new Exception("You don't have permission to perform this action.");
        } else {
            throw new Exception("Failed to delete employee with ID: " + id + ". Response code: " + responseCode);
        }
    }

    public static List<Employee> searchEmployees(String token, Integer id, String name, String department, String jobTitle) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/employees/search?");
        if (id != null) urlBuilder.append("id=").append(id).append("&");
        if (name != null) urlBuilder.append("name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8)).append("&");
        if (department != null) urlBuilder.append("department=").append(URLEncoder.encode(department, StandardCharsets.UTF_8)).append("&");
        if (jobTitle != null) urlBuilder.append("jobTitle=").append(URLEncoder.encode(jobTitle, StandardCharsets.UTF_8)).append("&");

        String url = urlBuilder.toString();
        URL searchUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) searchUrl.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return parseEmployeeList(response.toString());
        } else {
            throw new Exception("Failed to search employees. Response code: " + responseCode);
        }
    }


    private static List<Employee> parseEmployeeList(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Employee getEmployeeById(String token, int id) throws Exception {
        URL url = new URL(BASE_URL + "/employees/details/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(response.toString(), Employee.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                throw new Exception("Unauthorized: Invalid or expired token.");
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Employee not found. ID: " + id);
            } else {
                throw new Exception("Failed to fetch employee. Response code: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
    }

}
