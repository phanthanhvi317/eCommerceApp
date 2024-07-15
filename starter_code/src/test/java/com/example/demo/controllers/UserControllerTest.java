package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById_exist() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        ResponseEntity<User> response = userController.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().getId());
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindById_notExist() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<User> response = userController.findById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindByUserName_exist() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(username, response.getBody().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindByUserName_notExist() {
        String username = "userNotExist";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testCreateUser_success() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("newUser");
        createUserRequest.setPassword("password123");
        createUserRequest.setConfirmPassword("password123");

        when(bCryptPasswordEncoder.encode(any())).thenReturn("hashedPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createUserRequest.getUsername(), response.getBody().getUsername());
        assertEquals("hashedPassword", response.getBody().getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(bCryptPasswordEncoder, times(1)).encode(createUserRequest.getPassword());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(cartRepository);
        verifyNoMoreInteractions(bCryptPasswordEncoder);
    }

    @Test
    public void testCreateUser_invalidPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("newUser");
        createUserRequest.setPassword("pass");
        createUserRequest.setConfirmPassword("password");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(bCryptPasswordEncoder);
    }
}
