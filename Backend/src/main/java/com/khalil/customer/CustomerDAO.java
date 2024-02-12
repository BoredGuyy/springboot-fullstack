package com.khalil.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCostumer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerById(Long id);
    void deleteCurstomerWithId(Long id);
    void updateCustomerWithId(Customer customer);
}
