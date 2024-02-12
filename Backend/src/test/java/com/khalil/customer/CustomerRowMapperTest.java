package com.khalil.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private CustomerRowMapper rowMapper;


    @Test
    void mapRow() throws SQLException {
        // Arrange
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getInt("age")).thenReturn(25);

        // Act
        Customer customer = rowMapper.mapRow(resultSet, 1);

        // Assert
        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals(25, customer.getAge());
    }
}