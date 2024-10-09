package com.challange.crud.service;

import com.challange.crud.constant.ResponseMessage;
import com.challange.crud.dto.request.DeleteCustomerRequest;
import com.challange.crud.dto.request.GetCustomerRequest;
import com.challange.crud.dto.request.SaveCustomerRequest;
import com.challange.crud.dto.request.UpdateCustomerRequest;
import com.challange.crud.dto.response.*;
import com.challange.crud.model.Customer;
import com.challange.crud.model.CustomerDetails;
import com.challange.crud.model.Sex;
import com.challange.crud.repository.CustomerRepository;
import com.challange.crud.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private SaveCustomerRequest saveCustomerRequest;
    private GetCustomerRequest getCustomerRequest;
    private UpdateCustomerRequest updateCustomerRequest;
    private DeleteCustomerRequest deleteCustomerRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        initializeSaveCustomerRequest();
        initializeGetCustomerRequest();
        initializeUpdateCustomerRequest();
        initializeDeleteCustomerRequest();
    }

    private void initializeSaveCustomerRequest() {
        saveCustomerRequest = new SaveCustomerRequest();
        saveCustomerRequest.setName("Dhruv Vyas");
        saveCustomerRequest.setAccountType("Savings");
        saveCustomerRequest.setContractType("fulltime");
        SaveCustomerRequest.Details details = new SaveCustomerRequest.Details();
        details.setSex("M");
        details.setDob("17-06-2005");
        details.setNativePlace("Ahmedabad");
        saveCustomerRequest.setDetails(details);
    }

    private void initializeGetCustomerRequest() {
        getCustomerRequest = new GetCustomerRequest();
        getCustomerRequest.setId(1L);
    }

    private void initializeUpdateCustomerRequest(){
        updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setId(1L);
        updateCustomerRequest.setName("Dhruv Updated");
        updateCustomerRequest.setAccountType("Current");
        updateCustomerRequest.setContractType("parttime");
        UpdateCustomerRequest.Details detailsRequest = new UpdateCustomerRequest.Details();
        detailsRequest.setSex("M");
        detailsRequest.setDob("01-01-1991");
        detailsRequest.setNativePlace("Los Angeles");
        updateCustomerRequest.setDetails(detailsRequest);
    }

    private void initializeDeleteCustomerRequest() {
        deleteCustomerRequest = new DeleteCustomerRequest();
    }

    @Test
    public void shouldSaveCustomerSuccessfully() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);
        doNothing().when(kafkaProducerService).sendMessage(anyString());

        SaveCustomerResponse response = customerService.saveCustomer(saveCustomerRequest);

        assertNotNull(response);
        assertEquals("Dhruv Vyas", response.getName());
        assertEquals(ResponseMessage.SUCCESS_COUNT, response.getSuccess());
    }

    @Test
    public void shouldReturnInvalidSexErrorWhenSavingCustomer() {
        saveCustomerRequest.getDetails().setSex("T");

        SaveCustomerResponse response = customerService.saveCustomer(saveCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.INVALID_SEX, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }

    @Test
    public void shouldReturnInvalidDobErrorWhenSavingCustomer() {
        saveCustomerRequest.getDetails().setDob("17-06-2025");

        SaveCustomerResponse response = customerService.saveCustomer(saveCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.INVALID_DOB, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }

    @Test
    public void shouldReturnInvalidContractTypeErrorWhenSavingCustomer() {
        saveCustomerRequest.setContractType("Part-Time");

        SaveCustomerResponse response = customerService.saveCustomer(saveCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.INVALID_CONTRACT, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }

    @Test
    public void shouldHandleSaveCustomerException() {
        when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("Database error"));

        SaveCustomerResponse response = customerService.saveCustomer(saveCustomerRequest);

        assertNotNull(response);
        assertEquals("Database error", response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }

    @Test
    public void shouldReturnAllCustomersSuccessfully() throws ParseException {
        List<Customer> mockCustomers = createMockCustomers();

        when(customerRepository.findAll()).thenReturn(mockCustomers);

        GetAllCustomerResponse response = customerService.getAllCustomer();

        assertNotNull(response);
        assertEquals(ResponseMessage.SUCCESS_COUNT, response.getSuccess());
        assertEquals(ResponseMessage.GET_ALL_CUSTOMER, response.getMessage());
        assertEquals(1, response.getCustomers().size());

        GetAllCustomerResponse.Customers returnedCustomer = response.getCustomers().get(0);
        assertCustomerDetails(returnedCustomer);
    }

    private List<Customer> createMockCustomers() throws ParseException {
        List<Customer> customers = new ArrayList<>();

        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Dhruv Vyas");
        customer1.setAccountType("Savings");
        customer1.setContractType("fulltime");

        CustomerDetails details1 = new CustomerDetails();
        details1.setSex(Sex.M);
        details1.setDob(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1990"));
        details1.setNativePlace("Ahmedabad");
        customer1.setDetails(details1);

        customers.add(customer1);
        return customers;
    }

    private void assertCustomerDetails(GetAllCustomerResponse.Customers customer) {
        assertEquals(1L, customer.getId());
        assertEquals("Dhruv Vyas", customer.getName());
        assertEquals("Savings", customer.getAccountType());
        assertEquals("fulltime", customer.getContractType());
        assertEquals("M", customer.getDetails().getSex());
        assertEquals("01-01-1990", customer.getDetails().getDob());
        assertEquals("Ahmedabad", customer.getDetails().getNativePlace());
    }

    @Test
    public void shouldReturnNoRecordsFoundWhenGettingAllCustomers() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());

        GetAllCustomerResponse response = customerService.getAllCustomer();

        assertNotNull(response);
        assertEquals(ResponseMessage.NO_RECORD_FOUND, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
        assertTrue(response.getCustomers() == null || response.getCustomers().isEmpty());
    }

    @Test
    public void shouldReturnCustomerSuccessfully() throws ParseException {
        Customer mockCustomer = createMockCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        GetCustomerResponse response = customerService.getCustomer(getCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.SUCCESS_COUNT, response.getSuccess());
        assertEquals(ResponseMessage.GET_CUSTOMER, response.getMessage());
        assertCustomerResponseDetails(response);
    }

    private Customer createMockCustomer() throws ParseException {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Dhruv Vyas");
        customer.setAccountType("Savings");
        customer.setContractType("fulltime");

        CustomerDetails details = new CustomerDetails();
        details.setSex(Sex.M);
        details.setDob(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1990"));
        details.setNativePlace("Ahmedabad");
        customer.setDetails(details);

        return customer;
    }

    private void assertCustomerResponseDetails(GetCustomerResponse response) {
        assertEquals(1L, response.getCustomers().getId());
        assertEquals("Dhruv Vyas", response.getCustomers().getName());
        assertEquals("M", response.getCustomers().getDetails().getSex());
        assertEquals("01-01-1990", response.getCustomers().getDetails().getDob());
        assertEquals("Ahmedabad", response.getCustomers().getDetails().getNativePlace());
    }

    @Test
    public void shouldReturnCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        GetCustomerResponse response = customerService.getCustomer(getCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.NO_RECORD_FOUND, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
        assertNull(response.getCustomers());
    }

    @Test
    public void shouldUpdateCustomerSuccessfully() throws Exception {
        Customer mockCustomer = createMockCustomer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        UpdateCustomerResponse response = customerService.updateCustomer(updateCustomerRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Dhruv Updated", response.getName());
        assertEquals(ResponseMessage.SUCCESS_COUNT, response.getSuccess());
        assertEquals(ResponseMessage.UPDATE_CUSTOMER, response.getMessage());

        verify(customerRepository).save(mockCustomer);
    }

    @Test
    public void shouldReturnCustomerNotFoundWhenUpdating() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        UpdateCustomerResponse response = customerService.updateCustomer(updateCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.NO_RECORD_FOUND, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }

    @Test
    public void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;
        deleteCustomerRequest.setId(customerId);

        Customer customer = createMockCustomerForDeletion(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        DeleteCustomerResponse response = customerService.deleteCustomer(deleteCustomerRequest);

        verify(customerRepository, times(1)).deleteById(customerId);
        assertDeleteCustomerResponse(response, customerId, customer);
    }

    private Customer createMockCustomerForDeletion(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Dhruv Vyas");

        return customer;
    }

    private void assertDeleteCustomerResponse(DeleteCustomerResponse response, Long customerId, Customer customer) {
        assertNotNull(response);
        assertEquals(customerId, response.getId());
        assertEquals("Dhruv Vyas", response.getName());
        assertEquals(ResponseMessage.SUCCESS_COUNT, response.getSuccess());
        assertEquals(ResponseMessage.DELETE_CUSTOMER, response.getMessage());
    }

    @Test
    public void shouldReturnCustomerNotFoundWhenDeleting() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        deleteCustomerRequest.setId(1L);
        DeleteCustomerResponse response = customerService.deleteCustomer(deleteCustomerRequest);

        assertNotNull(response);
        assertEquals(ResponseMessage.NO_RECORD_FOUND, response.getError());
        assertEquals(ResponseMessage.ERROR_COUNT, response.getSuccess());
    }


}
