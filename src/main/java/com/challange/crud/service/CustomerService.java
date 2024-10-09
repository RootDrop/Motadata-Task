package com.challange.crud.service;

import com.challange.crud.dto.request.DeleteCustomerRequest;
import com.challange.crud.dto.request.GetCustomerRequest;
import com.challange.crud.dto.request.SaveCustomerRequest;
import com.challange.crud.dto.request.UpdateCustomerRequest;
import com.challange.crud.dto.response.DeleteCustomerResponse;
import com.challange.crud.dto.response.GetAllCustomerResponse;
import com.challange.crud.dto.response.GetCustomerResponse;
import com.challange.crud.dto.response.SaveCustomerResponse;
import com.challange.crud.dto.response.UpdateCustomerResponse;

public interface CustomerService {

  SaveCustomerResponse saveCustomer(SaveCustomerRequest saveCustomerRequest);

  GetAllCustomerResponse getAllCustomer();

  GetCustomerResponse getCustomer(GetCustomerRequest getCustomerRequest);

  UpdateCustomerResponse updateCustomer(UpdateCustomerRequest updateCustomerRequest);

  DeleteCustomerResponse deleteCustomer(DeleteCustomerRequest deleteCustomerRequest);
}
