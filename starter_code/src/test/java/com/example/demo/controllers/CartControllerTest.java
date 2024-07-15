package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Test
    @DisplayName("addTocartUserNull")
    public void addTocartUserNull() throws Exception {
        try {
            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(null).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.addTocart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }

    @Test
    @DisplayName("addTocartItemNull")
    public void addTocartItemNull() throws Exception {
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("NguyenT4");
            user.setPassword("Nguyen@123");

            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.addTocart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }

    @Test
    @DisplayName("addTocart")
    public void addTocart() throws Exception {
        try {
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

            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // MOCK ItemRepository
            Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyLong());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.addTocart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(cart, responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }

    @Test
    @DisplayName("removeFromcarttUserNull")
    public void removeFromcarttUserNull() throws Exception {
        try {
            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(null).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.removeFromcart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }

    @Test
    @DisplayName("removeFromcartItemNull")
    public void removeFromcartItemNull() throws Exception {
        try {
            User user = new User();
            user.setId(1L);
            user.setUsername("NguyenT4");
            user.setPassword("Nguyen@123");

            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.removeFromcart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }

    @Test
    @DisplayName("removeFromcart")
    public void removeFromcart() throws Exception {
        try {
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

            // SETUP INPUT
            ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
            modifyCartRequest.setItemId(1L);
            modifyCartRequest.setQuantity(1);
            modifyCartRequest.setUsername("NguyenT4");

            // MOCK UserRepository
            Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.anyString());
            // MOCK ItemRepository
            Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyLong());
            // EXECUTE METHOD
            ResponseEntity<Cart> responseActual = cartController.removeFromcart(modifyCartRequest);

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(cart, responseActual.getBody());
        }catch (Exception ex){
            throw ex;
        }
    }
}
