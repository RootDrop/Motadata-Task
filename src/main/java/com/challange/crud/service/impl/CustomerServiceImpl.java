package com.challange.crud.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.challange.crud.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challange.crud.constant.ResponseMessage;
import com.challange.crud.dto.request.DeleteCustomerRequest;
import com.challange.crud.dto.request.GetCustomerRequest;
import com.challange.crud.dto.request.SaveCustomerRequest;
import com.challange.crud.dto.request.UpdateCustomerRequest;
import com.challange.crud.dto.response.DeleteCustomerResponse;
import com.challange.crud.dto.response.GetAllCustomerResponse;
import com.challange.crud.dto.response.GetCustomerResponse;
import com.challange.crud.dto.response.SaveCustomerResponse;
import com.challange.crud.dto.response.UpdateCustomerResponse;
import com.challange.crud.model.Customer;
import com.challange.crud.model.CustomerDetails;
import com.challange.crud.model.Sex;
import com.challange.crud.repository.CustomerRepository;
import com.challange.crud.service.CustomerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private KafkaProducerService kafkaProducerService;

  @Override
  public SaveCustomerResponse saveCustomer(SaveCustomerRequest saveCustomerRequest) {

    if (saveCustomerRequest.checkSex()) {
      return SaveCustomerResponse.builder()
          .error(ResponseMessage.INVALID_SEX)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    if (saveCustomerRequest.checkDob()) {
      return SaveCustomerResponse.builder()
          .error(ResponseMessage.INVALID_DOB)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    if (saveCustomerRequest.checkContractType()) {
      return SaveCustomerResponse.builder()
          .error(ResponseMessage.INVALID_CONTRACT)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    try {

      CustomerDetails customerDetails = CustomerDetails.builder()
          .sex(Sex.valueOf(saveCustomerRequest.getDetails().getSex()))
          .dob(new SimpleDateFormat("dd-MM-yyyy").parse(saveCustomerRequest.getDetails().getDob()))
          .nativePlace(saveCustomerRequest.getDetails().getNativePlace())
          .build();

      Customer customer = Customer.builder()
          .name(saveCustomerRequest.getName())
          .accountType(saveCustomerRequest.getAccountType())
          .contractType(saveCustomerRequest.getContractType())
          .details(customerDetails)
          .build();

      customerDetails.setCustomer(customer);

      customerRepository.save(customer);

//      kafkaProducerService.sendMessage("Created entity with ID: " + customer.getId());

      return SaveCustomerResponse.builder()
          .id(customer.getId())
          .name(customer.getName())
          .success(ResponseMessage.SUCCESS_COUNT)
          .message(ResponseMessage.SAVE_CUSTOMER)
          .build();
    } catch (Exception e) {
      e.printStackTrace();
      return SaveCustomerResponse.builder()
          .success(ResponseMessage.ERROR_COUNT)
          .error(e.getMessage())
          .build();

    }
  }

  @Override
  public GetAllCustomerResponse getAllCustomer() {

    List<Customer> customerList = customerRepository.findAll();

    if (customerList.isEmpty()) {
      return GetAllCustomerResponse.builder()
          .error(ResponseMessage.NO_RECORD_FOUND)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    List<GetAllCustomerResponse.Customers> customers = new ArrayList<>();

    customerList.forEach(data -> {
      Date dob = data.getDetails().getDob();
      SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
      String formattedDobDate = outputFormatter.format(dob);

      GetAllCustomerResponse.Customers.Details details = GetAllCustomerResponse.Customers.Details.builder()
          .sex(data.getDetails().getSex().toString())
          .dob(formattedDobDate)
          .nativePlace(data.getDetails().getNativePlace())
          .build();

      GetAllCustomerResponse.Customers customer = GetAllCustomerResponse.Customers.builder()
          .id(data.getId())
          .name(data.getName())
          .accountType(data.getAccountType())
          .contractType(data.getContractType())
          .details(details)
          .build();

      customers.add(customer);

    });

    return GetAllCustomerResponse.builder()
        .customers(customers)
        .success(ResponseMessage.SUCCESS_COUNT)
        .message(ResponseMessage.GET_ALL_CUSTOMER)
        .build();

  }

  @Override
  public GetCustomerResponse getCustomer(GetCustomerRequest getCustomerRequest) {

    Optional<Customer> optionalCustomer = customerRepository.findById(getCustomerRequest.getId());

    if (optionalCustomer.isEmpty()) {
      return GetCustomerResponse.builder()
          .error(ResponseMessage.NO_RECORD_FOUND)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    Customer customer = optionalCustomer.get();

    Date dob = customer.getDetails().getDob();
    SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDobDate = outputFormatter.format(dob);

    GetCustomerResponse.Customers.Details details = GetCustomerResponse.Customers.Details.builder()
        .sex(customer.getDetails().getSex().toString())
        .dob(formattedDobDate)
        .nativePlace(customer.getDetails().getNativePlace())
        .build();

    GetCustomerResponse.Customers customers = GetCustomerResponse.Customers.builder()
        .id(customer.getId())
        .name(customer.getName())
        .details(details)
        .accountType(customer.getAccountType())
        .contractType(customer.getContractType())
        .build();

    return GetCustomerResponse.builder()
        .customers(customers)
        .success(ResponseMessage.SUCCESS_COUNT)
        .message(ResponseMessage.GET_CUSTOMER)
        .build();
  }

  @Override
  public UpdateCustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest) {

    if (updateCustomerRequest.checkSex()) {
      return UpdateCustomerResponse.builder()
          .error(ResponseMessage.INVALID_SEX)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    if (updateCustomerRequest.checkDob()) {
      return UpdateCustomerResponse.builder()
          .error(ResponseMessage.INVALID_DOB)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    if (updateCustomerRequest.checkContractType()) {
      return UpdateCustomerResponse.builder()
          .error(ResponseMessage.INVALID_CONTRACT)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    Optional<Customer> optionalCustomer = customerRepository.findById(updateCustomerRequest.getId());

    if (optionalCustomer.isEmpty()) {
      return UpdateCustomerResponse.builder()
          .error(ResponseMessage.NO_RECORD_FOUND)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    Customer customer = optionalCustomer.get();

    try {
      CustomerDetails details = customer.getDetails();
      details.setSex(Sex.valueOf(updateCustomerRequest.getDetails().getSex()));
      details.setDob(new SimpleDateFormat("dd-MM-yyyy").parse(updateCustomerRequest.getDetails().getDob()));
      details.setNativePlace(updateCustomerRequest.getDetails().getNativePlace());

      customer.setName(updateCustomerRequest.getName());
      customer.setDetails(details);
      customer.setAccountType(updateCustomerRequest.getAccountType());
      customer.setContractType(updateCustomerRequest.getContractType());

      details.setCustomer(customer);

      customerRepository.save(customer);

      kafkaProducerService.sendMessage("Updated entity with ID: " + customer.getId());

      return UpdateCustomerResponse.builder()
          .id(customer.getId())
          .name(customer.getName())
          .success(ResponseMessage.SUCCESS_COUNT)
          .message(ResponseMessage.UPDATE_CUSTOMER)
          .build();
    } catch (ParseException e) {
      return UpdateCustomerResponse
          .builder()
          .success(ResponseMessage.ERROR_COUNT)
          .error(e.getMessage())
          .build();
    }
  }

  @Override
  public DeleteCustomerResponse deleteCustomer(DeleteCustomerRequest deleteCustomerRequest) {

    Optional<Customer> optionalCustomer = customerRepository.findById(deleteCustomerRequest.getId());

    if (optionalCustomer.isEmpty()) {
      return DeleteCustomerResponse.builder()
          .error(ResponseMessage.NO_RECORD_FOUND)
          .success(ResponseMessage.ERROR_COUNT)
          .build();
    }

    Customer customer = optionalCustomer.get();

    Long id = customer.getId();

    customerRepository.deleteById(id);

    kafkaProducerService.sendMessage("Deleted entity with ID: " + customer.getId());

    return DeleteCustomerResponse.builder()
        .id(customer.getId())
        .name(customer.getName())
        .success(ResponseMessage.SUCCESS_COUNT)
        .message(ResponseMessage.DELETE_CUSTOMER)
        .build();
  }
}

