package by.zemich.userms.controller;

import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.service.UserRestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRestService userRestService;

    @Test
    void register_validRequest_ReturnsValidResponseEntity() throws Exception {
        // given
        String newUserRequest = getValidRegisterRequest();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserRequest);
        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void register_invalidRequest_ReturnsValidResponseEntity() throws Exception {
        // given
        String newUserRequest = getInvalidRegisterRequest();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserRequest);
        // when
        mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isInternalServerError(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    public String getValidRegisterRequest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("someemail@gmail.com")
                .firstName("Eugene")
                .lastName("Suboch")
                .password("password")
                .role("ADMIN")
                .build();
        return objectMapper.writeValueAsString(request);
    }

    public String getInvalidRegisterRequest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("someemail@gmail.com")
                .firstName("Eugene")
                .lastName("Suboch")
                .password("1234")
                .role("ADMIN")
                .build();
        return objectMapper.writeValueAsString(request);
    }

}