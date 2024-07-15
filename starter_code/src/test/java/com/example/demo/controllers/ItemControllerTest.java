package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("book");
        item1.setPrice(new BigDecimal("10.0"));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("pencil");
        item2.setPrice(new BigDecimal("5.0"));

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("airpod");
        item3.setPrice(new BigDecimal("100.0"));

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2, item3));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        assertEquals(item1, response.getBody().get(0));
        assertEquals(item2, response.getBody().get(1));
        assertEquals(item3, response.getBody().get(2));
        verify(itemRepository, times(1)).findAll();
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void testGetItemById_found() {
        Item item = new Item();
        item.setId(1L);
        item.setName("book");
        item.setPrice(new BigDecimal("10.0"));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
        verify(itemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void testGetItemById_notFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(itemRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void testGetItemsByName_found() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("book");
        item1.setPrice(new BigDecimal("10.0"));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("pencil");
        item2.setPrice(new BigDecimal("5.0"));
        when(itemRepository.findByName("book")).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("book");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(item1, response.getBody().get(0));
        assertEquals(item2, response.getBody().get(1));
        verify(itemRepository, times(1)).findByName("book");
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void testGetItemsByName_notFound() {
        when(itemRepository.findByName(anyString())).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("AAAAAAAAAA");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(itemRepository, times(1)).findByName("AAAAAAAAAA");
        verifyNoMoreInteractions(itemRepository);
    }
}