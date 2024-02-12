package com.khalil.customer;

import com.khalil.exception.BadRequestException;
import com.khalil.exception.DuplicateResourceException;
import com.khalil.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }


    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        Mockito.verify(customerDAO).selectAllCustomers();
    }

    @Test
    void willGetCustomerById() {
        var id = 1L;
        Customer customer= new Customer(id, "test", "test@email", 2);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
        Customer result = underTest.getCustomerById(id);
        assertThat(result).isEqualTo(customer);
    }

    @Test
    void willThrowWhenAddCustomerReturnsEmptyOptional() {
        //Given
        Long id = 1L;
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());
        //Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID: " + id + " does not exist!");
    }

    @Test
    void canAddCustomer() {
        //Given
        String email = "test@email";
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "khalil", "test@email", 25);
        //When
        underTest.addCustomer(request);
        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDAO).insertCostumer(customerArgumentCaptor.capture());
        Customer argumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(argumentCaptorValue.getId()).isNull();
        assertThat(argumentCaptorValue.getName()).isEqualTo(request.name());
        assertThat(argumentCaptorValue.getEmail()).isEqualTo(request.email());
        assertThat(argumentCaptorValue.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenAddCustomerReturnsTrue() {
        //Given
        String email = "test@email";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "khalil", "test@email", 25);
        //When
        when(customerDAO.existsCustomerWithEmail(email)).thenReturn(true);
        //Then
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken!");

        Mockito.verify(customerDAO, never()).insertCostumer(any());
    }

    @Test
    void deleteCustomerById() {
        //Given
        Long id = 1L;
        when(customerDAO.existsCustomerById(id)).thenReturn(true);
        //When
        underTest.deleteCustomerById(id);
        //Then
        Mockito.verify(customerDAO).deleteCurstomerWithId(id);
    }

    @Test
    void willThrowWhendeleteCustomerByIdReturnsFalse() {
        //Given
        Long id = 1L;
        when(customerDAO.existsCustomerById(id)).thenReturn(false);
        //When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Customer with ID: " + id + " does not exist!");
        //Then
        Mockito.verify(customerDAO, never()).deleteCurstomerWithId(id);
    }

//    @Test
//    void updateCustomerWithId() {
//        //Given
//        Long id = 1L;
//        Customer customer = new Customer("khalil", "khalil@email", 24);
//        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
//
//        CustomerUpdateRequest updateRequest =
//                new CustomerUpdateRequest("Ahmed", "ahmed@email", 24);
//
//        when(customerDAO.existsCustomerById(id)).thenReturn(true);
//        when(customerDAO.existsCustomerWithEmail(updateRequest.email())).thenReturn(false);
//        //When
//        underTest.updateCustomerWithId(id, updateRequest);
//        //Then
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        Mockito.verify(customerDAO).updateCustomerWithId(customerArgumentCaptor.capture());
//        Customer capturedValue = customerArgumentCaptor.getValue();
//
//        assertThat(capturedValue.getName()).isEqualTo(updateRequest.name());
//        assertThat(capturedValue.getEmail()).isEqualTo(updateRequest.email());
//        assertThat(capturedValue.getAge()).isEqualTo(updateRequest.age());
//    }

//    @Test
//    void canUpdateOnlyCustomerName() {
//        // Given
//        Long id = 10L;
//        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
//        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
//
//        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", null, null);
//
//        // When
//        underTest.updateCustomerWithId(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//
//        Mockito.verify(customerDAO).updateCustomerWithId(customerArgumentCaptor.capture());
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
//        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
//    }

//    @Test
//    void canUpdateOnlyCustomerEmail() {
//        // Given
//        var id = 10L;
//        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
//        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
//
//        String newEmail = "alexandro@amigoscode.com";
//
//        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);
//
//        when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(false);
//
//        // When
//        underTest.updateCustomerWithId(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//
//        Mockito.verify(customerDAO).updateCustomerWithId(customerArgumentCaptor.capture());
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
//        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
//    }

//    @Test
//    void canUpdateOnlyCustomerAge() {
//        // Given
//        var id = 10L;
//        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
//        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));
//
//        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 22);
//
//        // When
//        underTest.updateCustomerWithId(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//
//        Mockito.verify(customerDAO).updateCustomerWithId(customerArgumentCaptor.capture());
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
//        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
//    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        var id = 10L;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDAO.existsCustomerWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomerWithId(id, updateRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("Email already taken!");

        // Then
        Mockito.verify(customerDAO, never()).updateCustomerWithId(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        var id = 10L;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateCustomerWithId(id, updateRequest)).isInstanceOf(BadRequestException.class).hasMessage("No Changes were commited!");

        // Then
        Mockito.verify(customerDAO, never()).updateCustomerWithId(any());
    }
}