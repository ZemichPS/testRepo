package by.zemich.userms.controller;

import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.dao.entity.Role;
import by.zemich.userms.dao.entity.User;
import by.zemich.userms.service.UserRestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserRestService userRestService;

    @InjectMocks
    UserController userController;

    @Test
        // названиеметода_условие_ожидаемыйрезультат
    void register_validRequest_ReturnsValidResponseEntity() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        URI uri = URI.create("http://localhost:64102/api/v1/users/");
        RegisterRequest registerRequest = getValidRegisterRequest();
        User user = getValidUser();

        // when
        when(userRestService.register(registerRequest)).thenReturn(user);
        var responseEntity = userController.register(registerRequest);

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }

    public RegisterRequest getValidRegisterRequest(){
        return RegisterRequest.builder()
                .email("someemail@gmail.com")
                .firstName("Eugene")
                .lastName("Suboch")
                .password("password")
                .role("Admin")
                .build();
    }

    public User getValidUser(){
        return User.builder()
                .id(UUID.fromString("864ed30f-44d5-4352-9644-f58f5c6b3d56"))
                .email("someemail@gmail.com")
                .firstName("Eugene")
                .lastName("Suboch")
                .password("password")
                .role(Role.ADMIN)
                .registerAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
    }

}