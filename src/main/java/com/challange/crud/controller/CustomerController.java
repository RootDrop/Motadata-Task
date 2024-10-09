package com.challange.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challange.crud.dto.request.DeleteCustomerRequest;
import com.challange.crud.dto.request.GetCustomerRequest;
import com.challange.crud.dto.request.SaveCustomerRequest;
import com.challange.crud.dto.request.UpdateCustomerRequest;
import com.challange.crud.dto.response.DeleteCustomerResponse;
import com.challange.crud.dto.response.GetAllCustomerResponse;
import com.challange.crud.dto.response.GetCustomerResponse;
import com.challange.crud.dto.response.SaveCustomerResponse;
import com.challange.crud.dto.response.UpdateCustomerResponse;
import com.challange.crud.service.CustomerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/customer")
@AllArgsConstructor
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping
  private ResponseEntity<SaveCustomerResponse> saveCustomer(
      @Valid @RequestBody SaveCustomerRequest saveCustomerRequest) {
    SaveCustomerResponse saveCustomerResponse = customerService.saveCustomer(saveCustomerRequest);
    return new ResponseEntity<>(saveCustomerResponse, HttpStatus.OK);
  }

  @GetMapping
  private ResponseEntity<GetAllCustomerResponse> getAllCustomer() {
    GetAllCustomerResponse getAllCustomerResponse = customerService.getAllCustomer();
    return new ResponseEntity<>(getAllCustomerResponse, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  private ResponseEntity<GetCustomerResponse> getCustomer(@ModelAttribute GetCustomerRequest getCustomerRequest) {
    GetCustomerResponse getCustomerResponse = customerService.getCustomer(getCustomerRequest);
    return new ResponseEntity<>(getCustomerResponse, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<UpdateCustomerResponse> updateCustomer(
      @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {
    UpdateCustomerResponse updateCustomerResponse = customerService.updateCustomer(updateCustomerRequest);
    return new ResponseEntity<>(updateCustomerResponse, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteCustomerResponse> deleteCustomer(
      @ModelAttribute DeleteCustomerRequest deleteCustomerRequest) {
    DeleteCustomerResponse deleteCustomerResponse = customerService.deleteCustomer(deleteCustomerRequest);
    return new ResponseEntity<>(deleteCustomerResponse, HttpStatus.OK);
  }

}
