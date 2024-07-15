package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void init() {
        itemController = new ItemController(itemRepository);
    }

    @Test
    @DisplayName("getItems")
    public void getItems() throws Exception {
        try {
            // SETUP INPUT
            List<Item> itemList = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("Iphone 14 Pro Max");
            item.setDescription("Takes very nice pictures");
            item.setPrice(new BigDecimal("2000"));
            itemList.add(item);
            // MOCK ItemRepository
            Mockito.doReturn(itemList).when(itemRepository).findAll();
            // EXECUTE METHOD
            ResponseEntity<List<Item>> responseActual = itemController.getItems();

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(1,responseActual.getBody().size());
            for (int i = 0; i < responseActual.getBody().size(); i++) {
                Assert.assertEquals(BigDecimal.valueOf(2000), responseActual.getBody().get(i).getPrice());
                Assert.assertEquals("Iphone 14 Pro Max", responseActual.getBody().get(i).getName());
                Assert.assertEquals("Takes very nice pictures", responseActual.getBody().get(i).getDescription());

            }

        } catch (Exception ex) {
            throw ex;
        }
    }


    @Test
    @DisplayName("getItemById")
    public void getItemById() throws Exception {
        try {
            // SETUP INPUT
            Item item = new Item();
            item.setId(1L);
            item.setName("Iphone 14 Pro Max");
            item.setDescription("Takes very nice pictures");
            item.setPrice(new BigDecimal("2000"));

            // MOCK ItemRepository
            Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyLong());
            // EXECUTE METHOD
            ResponseEntity<Item> responseActual = itemController.getItemById(item.getId());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            Assert.assertEquals(BigDecimal.valueOf(2000), responseActual.getBody().getPrice());
            Assert.assertEquals("Iphone 14 Pro Max", responseActual.getBody().getName());
            Assert.assertEquals("Takes very nice pictures", responseActual.getBody().getDescription());

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("getItemsByName")
    public void getItemsByName() throws Exception {
        try {
            // SETUP INPUT
            List<Item> itemList = new ArrayList<>();
            Item item = new Item();
            item.setId(1L);
            item.setName("Iphone 14 Pro Max");
            item.setDescription("Takes very nice pictures");
            item.setPrice(new BigDecimal("2000"));
            itemList.add(item);
            // MOCK ItemRepository
            Mockito.doReturn(itemList).when(itemRepository).findByName(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<List<Item>> responseActual = itemController.getItemsByName(item.getName());

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(200, responseActual.getStatusCodeValue());
            Assert.assertNotNull(responseActual.getBody());
            for (int i = 0; i < responseActual.getBody().size(); i++) {
                Assert.assertEquals(BigDecimal.valueOf(2000), responseActual.getBody().get(i).getPrice());
                Assert.assertEquals("Iphone 14 Pro Max", responseActual.getBody().get(i).getName());
                Assert.assertEquals("Takes very nice pictures", responseActual.getBody().get(i).getDescription());

            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("getItemsByNameNotFound")
    public void getItemsByNameNotFound() throws Exception {
        try {
            // MOCK ItemRepository
            Mockito.doReturn(null).when(itemRepository).findByName(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<List<Item>> responseActual = itemController.getItemsByName("Iphone 14 Pro Max");

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Test
    @DisplayName("getItemsByNameEmpty")
    public void getItemsByNameEmpty() throws Exception {
        try {
            // MOCK ItemRepository
            Mockito.doReturn(new ArrayList<>()).when(itemRepository).findByName(Mockito.anyString());
            // EXECUTE METHOD
            ResponseEntity<List<Item>> responseActual = itemController.getItemsByName("Iphone 14 Pro Max");

            // COMPARE OUTPUT
            Assert.assertNotNull(responseActual);
            Assert.assertEquals(404, responseActual.getStatusCodeValue());
            Assert.assertNull(responseActual.getBody());

        } catch (Exception ex) {
            throw ex;
        }
    }
}
