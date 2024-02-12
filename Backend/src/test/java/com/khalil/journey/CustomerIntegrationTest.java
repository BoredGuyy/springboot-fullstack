package com.khalil.journey;

import com.github.javafaker.Faker;
import com.khalil.customer.Customer;
import com.khalil.customer.CustomerRegistrationRequest;
import com.khalil.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random number = new Random();

    @Test
    void canRegisterACustomer() {
        //Create a registration request
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress() + "@khalil.com";
        int age = number.nextInt(19, 69);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        //send a post request
        webTestClient.post()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present
        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //get customer by id
        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                                .findFirst()
                                        .orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        //Create a registration request
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress() + "@khalil.com";
        int age = number.nextInt(19, 69);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        //send a post request
        webTestClient.post()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //get id
        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //Delete a customer
        webTestClient.delete()
                .uri("/api/v1/customers/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                        .expectStatus()
                                                .isOk();

        //get customer by id
        webTestClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

//    @Test
//    void canUpdateCustomer() {
//        //Create a registration request
//        Faker faker = new Faker();
//        String name = faker.name().firstName();
//        String email = faker.internet().emailAddress() + "@khalil.com";
//        int age = number.nextInt(19, 69);
//        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
//
//        //send a post request
//        webTestClient.post()
//                .uri("/api/v1/customers")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), CustomerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get all customers
//        List<Customer> allCustomers = webTestClient.get()
//                .uri("/api/v1/customers")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        //get id
//        Long id = allCustomers.stream().filter(c -> c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        //crate a Customer Update Request
//        String updatedName= "updatedName";
//        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(updatedName, null, null);
//
//        //update customer
//        webTestClient.put()
//                    .uri("/api/v1/customers/{id}", id, updateRequest)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
//                    .exchange()
//                    .expectStatus()
//                    .isOk();
//
//        Customer expectedCustomer = new Customer(id, updatedName, email, age);
//        //get Customer by id
//        Customer updatedCustomer = webTestClient.get()
//                .uri("/api/v1/customers/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Customer.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
//    }
}
