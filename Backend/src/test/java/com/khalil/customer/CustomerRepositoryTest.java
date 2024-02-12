package com.khalil.customer;

import com.github.javafaker.Faker;
import com.khalil.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    private static Faker Faker = new Faker();

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                25
        );
        underTest.save(customer);
        //When
        var test = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(test).isTrue();
    }

    @Test
    void existsCustomerByEmailReturnsFalseWhenDoesNotExist() {
        //Given
        var email = Faker.internet().emailAddress();
        //When
        var test = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(test).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                25
        );
        underTest.save(customer);
        
        Long id = underTest.findAll().stream().filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId()).findFirst().orElseThrow();
        //When
        var test = underTest.existsCustomerById(id);
        //Then
        assertThat(test).isTrue();
    }

    @Test
    void existsCustomerByIdReturnsFalseWhenDoesNotExist() {
        //Given
        Long id = -1L;
        //When
        var test = underTest.existsCustomerById(id);
        //Then
        assertThat(test).isFalse();
    }
}