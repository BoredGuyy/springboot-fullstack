package com.khalil.customer;

import com.khalil.exception.BadRequestException;
import com.khalil.exception.DuplicateResourceException;
import com.khalil.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomerById(Long id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID: " + id + " does not exist!"));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        String email = customerRegistrationRequest.email();
        if(customerDAO.existsCustomerWithEmail(email)){
            throw new DuplicateResourceException("Email already taken!");
        }
        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age());

        customerDAO.insertCostumer(customer);
    }

    public void deleteCustomerById(Long id){
        //check if ID exists
        if (!customerDAO.existsCustomerById(id)){
            throw new ResourceNotFoundException("Customer with ID: " + id + " does not exist!");
        }
        //delete
        customerDAO.deleteCurstomerWithId(id);
    }

    public void updateCustomerWithId(Long id, CustomerUpdateRequest request){
        Customer customer = getCustomerById(id);
        boolean change =false;

        if (request.name() != null && !customer.getName().equals(request.name())){
            customer.setName(request.name());
            change = true;
        }

        if (request.email() != null && !customer.getEmail().equals(request.email())){
            if(customerDAO.existsCustomerWithEmail(request.email())) {
                throw new DuplicateResourceException("Email already taken!");
            }
            customer.setEmail(request.email());
            change = true;
        }

        if (request.age() != null && !customer.getAge().equals(request.age())){
            customer.setAge(request.age());
            change = true;
        }

        if (!change){
            throw new BadRequestException("No Changes were commited!");
        }

        customerDAO.insertCostumer(customer);
    }
}
