package com.example.libraryManagement.controller;

import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PatronControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PatronRepository patronRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/patrons";
    }

    @Test
    public void testGetAllPatrons() {
        // Save some patrons for testing
        Patron patron1 = new Patron();
        patron1.setName("Patron 1");
        patron1.setContactInfo("contact1@example.com");
        patronRepository.save(patron1);

        Patron patron2 = new Patron();
        patron2.setName("Patron 2");
        patron2.setContactInfo("contact2@example.com");
        patronRepository.save(patron2);

        // Call the API to get all patrons
        ResponseEntity<Patron[]> response = restTemplate.getForEntity(baseUrl(), Patron[].class);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0); // Ensure there are patrons returned
    }


    @Test
    void testGetPatronById() {
        // Arrange
        Patron patron = new Patron();
        patron.setName("Test Patron");
        patron.setContactInfo("test@patron.com");
        patron = patronRepository.save(patron);

        // Act
        ResponseEntity<Patron> response = restTemplate.getForEntity(baseUrl() + "/" + patron.getId(), Patron.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Patron retrievedPatron = response.getBody();
        assertNotNull(retrievedPatron);
        assertEquals("Test Patron", retrievedPatron.getName());
    }

    @Test
    void testAddPatron() {
        // Arrange
        Patron patron = new Patron();
        patron.setName("New Patron");
        patron.setContactInfo("new@patron.com");
        HttpEntity<Patron> request = new HttpEntity<>(patron);

        // Act
        ResponseEntity<Patron> response = restTemplate.postForEntity(baseUrl(), request, Patron.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Patron createdPatron = response.getBody();
        assertNotNull(createdPatron);
        assertEquals("New Patron", createdPatron.getName());
    }

    @Test
    void testUpdatePatron() {
        // Arrange
        Patron patron = new Patron();
        patron.setName("Old Name");
        patron.setContactInfo("old@patron.com");
        patron = patronRepository.save(patron);

        Patron updatedPatron = new Patron();
        updatedPatron.setName("Updated Name");
        updatedPatron.setContactInfo("updated@patron.com");
        HttpEntity<Patron> request = new HttpEntity<>(updatedPatron);

        // Act
        restTemplate.put(baseUrl() + "/" + patron.getId(), request);
        ResponseEntity<Patron> response = restTemplate.getForEntity(baseUrl() + "/" + patron.getId(), Patron.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Patron resultPatron = response.getBody();
        assertNotNull(resultPatron);
        assertEquals("Updated Name", resultPatron.getName());
    }

    @Test
    public void testDeletePatron() {
        // Create and save a patron
        Patron patron = new Patron();
        patron.setName("Test Patron");
        patron.setContactInfo("test@example.com");
        Patron savedPatron = patronRepository.save(patron);

        // Delete the patron
        String url = baseUrl() + "/" + savedPatron.getId();
        restTemplate.delete(url);

        // Verify the patron is deleted
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Expecting 404 Not Found
    }



}

