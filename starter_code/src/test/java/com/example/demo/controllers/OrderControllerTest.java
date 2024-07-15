package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void init() {
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    @DisplayName("submit")
    public void submit() throws Exception {
        try {
            // SETUP INPUT
            User user = new User();
            user.setId(1L);
            user.setUsername("NguyenT4");
            user.setPassword("Nguyen@123");

            Cart cart = new Cart();
            cart.setId(1L);
            cart.setUser(user);
            List<Item> items = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("Iphone 14 Pro Max");
            item.setDescription("Takes very nice pictures");
            item.setPrice(new BigDecimal("2000"));
            items.add(item);
            cart.setItems(items);
            cart.setTotal(new BigDecimal("1"));
            user.setCart(cart);

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<UserOrder> responseActual = orderController.submit(user.getUsername());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(BigDecimal.valueOf(1), responseActual.getBody().getTotal());
            Assert.assertEquals(user, responseActual.getBody().getUser());
            for(int i = 1; i < responseActual.getBody().getItems().size(); i++){
                Assert.assertEquals(items.get(i), responseActual.getBody().getItems().get(i));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("submitNotFound")
    public void submitNotFound() throws Exception {
        try {

            Mockito.doReturn(null).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<UserOrder> responseActual = orderController.submit("NguyenT4");

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("getOrdersForUser")
    public void getOrdersForUser() throws Exception {
        try {
            // SETUP INPUT
            User user = new User();
            user.setId(1L);
            user.setUsername("NguyenT4");
            user.setPassword("Nguyen@123");

            Cart cart = new Cart();
            cart.setId(1L);
            cart.setUser(user);
            List<Item> items = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("Iphone 14 Pro Max");
            item.setDescription("Takes very nice pictures");
            item.setPrice(new BigDecimal("2000"));
            items.add(item);
            cart.setItems(items);
            cart.setTotal(new BigDecimal("1"));
            user.setCart(cart);

            List<UserOrder> userOrders = new ArrayList<>();
            UserOrder userOrder = new UserOrder();
            userOrder.setId(1L);
            userOrder.setUser(user);
            userOrder.setItems(items);
            userOrder.setTotal(new BigDecimal("33"));
            userOrders.add(userOrder);

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // MOCK OrderRepository
            Mockito.doReturn(userOrders).when(orderRepository).findByUser(Mockito.any());
            // EXECUTE METHOD
            ResponseEntity<List<UserOrder>> responseActual = orderController.getOrdersForUser(user.getUsername());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(1,responseActual.getBody().size());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("getOrdersForUserNotFound")
    public void getOrdersForUserNotFound() throws Exception {
        try {
            // MOCK UserRepository
            Mockito.doReturn(null).when(userRepository).findByUsername(Mockito.anyString());

            // EXECUTE METHOD
            ResponseEntity<List<UserOrder>> responseActual = orderController.getOrdersForUser("NguyenT4");

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        } catch (Exception ex) {
            throw ex;
        }
    }
}
