package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitOrder_success() {
        User user = new User();
        user.setUsername("testUser");

        Cart cart = new Cart();
        cart.setItems(Arrays.asList());
        user.setCart(cart);
        when(userRepository.findByUsername("superman")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("superman");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderRepository, times(1)).save(any(UserOrder.class));
        verify(userRepository, times(1)).findByUsername("superman");
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testSubmitOrder_userNotFound() {
        String username = "user_not_found";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetOrdersForUser_success() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setCart(new Cart());
        List<UserOrder> orders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(new BigDecimal("10.0"));
        orders.add(userOrder);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders.size(), response.getBody().size());
        verify(userRepository, times(1)).findByUsername(username);
        verify(orderRepository, times(1)).findByUser(user);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetOrdersForUser_userNotFound() {
        String username = "userNotFound";
        when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userRepository);
    }
}
