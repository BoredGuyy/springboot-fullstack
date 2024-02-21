package com.khalil;

import com.github.javafaker.Faker;
import com.khalil.customer.Customer;
import com.khalil.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            String firstname = faker.name().firstName();
            String lastName = faker.name().lastName();
            Customer customer = new Customer(
                    firstname + " " + lastName,
                    firstname.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                    random.nextInt(18, 80));

            customerRepository.save(customer);
        };
    }
}
