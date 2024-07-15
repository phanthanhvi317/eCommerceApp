package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private CartRepository cartRepository = mock(CartRepository.class);

    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart() {

        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setPrice(new BigDecimal("2.5"));

        User user = new User();
        user.setUsername("superman");
        Cart cart = new Cart();
        user.setCart(cart);


        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("superman");
        request.setItemId(1L);
        request.setQuantity(3);

        when(userRepository.findByUsername("superman")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user.getCart(), response.getBody());

        verify(userRepository, times(1)).findByUsername("superman");
        verify(itemRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(user.getCart());
        verifyNoMoreInteractions(userRepository, itemRepository, cartRepository);
    }

    @Test
    public void testRemoveFromCart() {

        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setPrice(new BigDecimal("2.5"));

        User user = new User();
        user.setUsername("superman");
        Cart cart = new Cart();
        user.setCart(cart);


        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("superman");
        request.setItemId(1L);
        request.setQuantity(3);

        when(userRepository.findByUsername("superman")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userRepository, times(1)).findByUsername("superman");
        verify(itemRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(user.getCart());
        verifyNoMoreInteractions(userRepository, itemRepository, cartRepository);
    }

    @Test
    public void testAddToCart_userNotFound() {

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username_not_found");

        when(userRepository.findByUsername("username_not_found")).thenReturn(null);

        ResponseEntity<?> response = cartController.addTocart(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, times(1)).findByUsername("username_not_found");
        verifyNoMoreInteractions(userRepository, itemRepository, cartRepository);
    }
}
