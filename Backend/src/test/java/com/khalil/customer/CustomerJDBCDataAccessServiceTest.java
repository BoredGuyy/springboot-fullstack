package com.khalil.customer;

import com.khalil.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                rowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                Faker.name().fullName(),
                Faker.internet().emailAddress(),
                20
        );
        underTest.insertCostumer(customer);
        //When
        List<Customer> customers = underTest.selectAllCustomers();
        //Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCostumer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();
        //When
        Optional<Customer> result = underTest.selectCustomerById(id);
        //Then
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId().equals(id));
            assertThat(c.getName().equals(customer.getName()));
            assertThat(c.getEmail().equals(email));
            assertThat(c.getAge().equals(customer.getAge()));
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        Long id = -1L;

        var test = underTest.selectCustomerById(id);

        assertThat(test).isEmpty();
    }

    @Test
    void insertCostumer() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCostumer(customer);

        var id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId()).findFirst().orElseThrow();
        //When
        var test = underTest.existsCustomerById(id);
        //Then
        assertThat(test).isTrue();
    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCostumer(customer);
        //When
        var result = underTest.existsCustomerWithEmail(email);
        //Then
        assertThat(result).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenDoesNotExist() {
        var email = Faker.internet().emailAddress();

        var test = underTest.existsCustomerWithEmail(email);

        assertThat(test).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCostumer(customer);

        var id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId()).findFirst().orElseThrow();
        //when
        var test = underTest.existsCustomerById(id);
        //Then
        assertThat(test).isTrue();
    }

    @Test
    void existsCustomerByIdReturnsFalseWhenDoesNotExist() {
        Long id = -1L;

        var test = underTest.existsCustomerById(id);

        assertThat(test).isFalse();
    }

    @Test
    void deleteCurstomerWithId() {
        //Given
        String email = Faker.internet().emailAddress();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCostumer(customer);

        var id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId()).findFirst().orElseThrow();
        //When
        underTest.deleteCurstomerWithId(id);
        //Then
        var test = underTest.selectCustomerById(id);
        assertThat(test).isNotPresent();
    }

//    @Test
//    void updateCustomerName() {
//        // Given
//        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.name().fullName(),
//                email,
//                20);
//
//        underTest.insertCostumer(customer);
//
//        var id = underTest.selectAllCustomers()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        var newName = "foo";
//
//        // When age is name
//        Customer update = new Customer();
//        update.setId(id);
//        update.setName(newName);
//
//        underTest.updateCustomerWithId(update);
//
//        // Then
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getName()).isEqualTo(newName); // change
//            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
//            assertThat(c.getAge()).isEqualTo(customer.getAge());
//        });
//    }
//
//    @Test
//    void updateCustomerEmail() {
//        // Given
//        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.name().fullName(),
//                email,
//                20);
//
//        underTest.insertCostumer(customer);
//
//        var id = underTest.selectAllCustomers()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        var newEmail = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();;
//
//        // When email is changed
//        Customer update = new Customer();
//        update.setId(id);
//        update.setEmail(newEmail);
//
//        underTest.updateCustomerWithId(update);
//
//        // Then
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getEmail()).isEqualTo(newEmail); // change
//            assertThat(c.getName()).isEqualTo(customer.getName());
//            assertThat(c.getAge()).isEqualTo(customer.getAge());
//        });
//    }
//
//    @Test
//    void updateCustomerAge() {
//        // Given
//        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.name().fullName(),
//                email,
//                20);
//
//        underTest.insertCostumer(customer);
//
//        var id = underTest.selectAllCustomers()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        var newAge = 100;
//
//        // When age is changed
//        Customer update = new Customer();
//        update.setId(id);
//        update.setAge(newAge);
//
//        underTest.updateCustomerWithId(update);
//
//        // Then
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getAge()).isEqualTo(newAge); // change
//            assertThat(c.getName()).isEqualTo(customer.getName());
//            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
//        });
//    }
//
//    @Test
//    void willUpdateAllPropertiesCustomer() {
//        // Given
//        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.name().fullName(),
//                email,
//                20);
//
//        underTest.insertCostumer(customer);
//
//        var id = underTest.selectAllCustomers()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        // When update with new name, age and email
//        Customer update = new Customer();
//        update.setId(id);
//        update.setName("foo");
//        String newEmail = UUID.randomUUID().toString();
//        update.setEmail(newEmail);
//        update.setAge(22);
//
//        underTest.updateCustomerWithId(update);
//
//        // Then
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
//            assertThat(updated.getId()).isEqualTo(id);
//            assertThat(updated.getName()).isEqualTo("foo");
//            assertThat(updated.getEmail()).isEqualTo(newEmail);
//            assertThat(updated.getAge()).isEqualTo(22);
//        });
//    }
//
//    @Test
//    void willNotUpdateWhenNothingToUpdate() {
//        // Given
//        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.name().fullName(),
//                email,
//                20);
//
//        underTest.insertCostumer(customer);
//
//        var id = underTest.selectAllCustomers()
//                .stream()
//                .filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        // When update without no changes
//        Customer update = new Customer();
//        update.setId(id);
//
//        underTest.updateCustomerWithId(update);
//
//        // Then
//        Optional<Customer> actual = underTest.selectCustomerById(id);
//
//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getAge()).isEqualTo(customer.getAge());
//            assertThat(c.getName()).isEqualTo(customer.getName());
//            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
//        });
//    }
}