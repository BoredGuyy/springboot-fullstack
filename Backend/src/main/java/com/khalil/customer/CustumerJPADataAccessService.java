package com.khalil.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustumerJPADataAccessService implements CustomerDAO{

    private final CustomerRepository customerRepository;

    public CustumerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCostumer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void deleteCurstomerWithId(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomerWithId(Customer customer) {
        customerRepository.save(customer);
    }

    public boolean existsCustomerById(Long id){
        return customerRepository.existsCustomerById(id);
    }
}
