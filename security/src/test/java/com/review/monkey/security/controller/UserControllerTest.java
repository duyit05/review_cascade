package com.review.monkey.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.monkey.security.request.UserRequest;
import com.review.monkey.security.response.UserResponse;
import com.review.monkey.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
// Mock request to controller
@AutoConfigureMockMvc
// request to test.properties to using unit test
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequest request;
    private UserResponse response;
    private LocalDate dob;

    // start befor test
    @BeforeEach
    public void intData() {
        dob = LocalDate.of(1990, 1, 1);
        request = UserRequest.builder()
                .username("john")
                .firstName("john")
                .lastName("john")
                .password("12345678")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .userId("46f0-b7a8")
                .username("john")
                .firstName("john")
                .lastName("john")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVE (request , response)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // convert string to json
        String content = objectMapper.writeValueAsString(request);

        // request api and resopnse data in controller , avoid call floor service
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);

        // WHEN (request api)
        mockMvc.perform(MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                //THEN (what expected)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.userId").value("46f0-b7a8"));
    }

    // test case for validation (username must be 4 character)
    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVE (request , response)
        request.setUsername("jo");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // convert string to json
        String content = objectMapper.writeValueAsString(request);

        // WHEN (request api)
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                //THEN (what expected)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("USERNAME MUST BE AT LEAST 4 CHARACTERS"));
        ;

    }
}
