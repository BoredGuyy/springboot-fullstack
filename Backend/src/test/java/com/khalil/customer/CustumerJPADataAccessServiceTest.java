package com.khalil.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustumerJPADataAccessServiceTest {

    private CustumerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustumerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        Long id = 5L;
        underTest.selectCustomerById(id);
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCostumer() {
        //Given
        Customer customer = new Customer(1L, "khalil", "khalil@email", 25);
        //When
        underTest.insertCostumer(customer);
        //Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        var email = "email.com";
        //When
        underTest.existsCustomerWithEmail(email);
        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCurstomerWithId() {
        //Given
        var id = 1L;
        //When
        underTest.deleteCurstomerWithId(id);
        //Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomerWithId() {
        Customer customer = new Customer("khalil", "khalil@email", 20);
        underTest.updateCustomerWithId(customer);
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerById() {
        var id = 1L;
        underTest.existsCustomerById(id);
        Mockito.verify(customerRepository).existsCustomerById(id);
    }
}