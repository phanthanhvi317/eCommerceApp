package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void init() {
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
    }

    @Test
    @DisplayName("createUser")
    public void createUser() throws Exception {
        try {
            // MOCK BCryptPasswordEncoder
            Mockito.doReturn("Tmd1eWVuQDEyMw==").when(bCryptPasswordEncoder).encode(Mockito.anyString());
            // SETUP INPUT
            CreateUserRequest request = new CreateUserRequest();

            request.setUsername("NguyenT4");
            request.setPassword("Nguyen@123");
            request.setConfirmPassword("Nguyen@123");

            // EXECUTE METHOD
            ResponseEntity<User> responseActual = userController.createUser(request);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());

            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(0, responseActual.getBody().getId());
            Assert.assertEquals("NguyenT4", responseActual.getBody().getUsername());
            Assert.assertEquals("Tmd1eWVuQDEyMw==", responseActual.getBody().getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("createUserWithLengthPassword")
    public void createUserWithLengthPassword() throws Exception {
        try {
            // SETUP INPUT
            CreateUserRequest request = new CreateUserRequest();

            request.setUsername("NguyenT4");
            request.setPassword("Nguyen");
            request.setConfirmPassword("Nguyen");

            // EXECUTE METHOD
            ResponseEntity<User> responseActual = userController.createUser(request);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(400, responseActual.getStatusCodeValue());

            Assert.assertNull(responseActual.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("findById")
    public void findById() throws Exception {
        try {
            Optional<User> user = createUserTest();
            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findById(Mockito.anyLong());

            // EXECUTE METHOD
            ResponseEntity<User> responseActual = userController.findById(user.get().getId());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(1, responseActual.getBody().getId());
            Assert.assertEquals("NguyenT4", responseActual.getBody().getUsername());
            Assert.assertEquals("Nguyen@123", responseActual.getBody().getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("findByUserName")
    public void findByUserName() throws Exception {
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("NguyenT4");
            user.setPassword("Nguyen@123");
            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());

            // EXECUTE METHOD
            ResponseEntity<User> responseActual = userController.findByUserName(user.getUsername());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(1, responseActual.getBody().getId());
            Assert.assertEquals("NguyenT4", responseActual.getBody().getUsername());
            Assert.assertEquals("Nguyen@123", responseActual.getBody().getPassword());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("findByUserNameNotFound")
    public void findByUserNameNotFound() throws Exception {
        try {

            // MOCK UserRepository
            Mockito.doReturn(null).when(userRepository).findByUsername(Mockito.anyString());

            // EXECUTE METHOD
            ResponseEntity<User> responseActual = userController.findByUserName(createUserTest().get().getUsername());

            // COMPARE OUTPUT
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Optional<User> createUserTest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("NguyenT4");
        user.setPassword("Nguyen@123");
        return Optional.of(user);
    }
}
