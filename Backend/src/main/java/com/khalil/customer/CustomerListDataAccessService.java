package com.khalil.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{
    private static List<Customer> customers;

//    static {
//        customers = new ArrayList<>();
//        Customer khalil = new Customer(1, "Khalil", "khalil@gmail.com", 25);
//        customers.add(khalil);
//        Customer ayoub = new Customer(2, "Ayoub", "Ayoub@gmail.com", 25);
//        customers.add(ayoub);
//    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public void insertCostumer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Long id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCurstomerWithId(Long id) {
    }

    @Override
    public void updateCustomerWithId(Customer customer) {

    }


}
