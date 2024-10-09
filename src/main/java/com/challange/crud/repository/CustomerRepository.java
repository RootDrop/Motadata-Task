package com.challange.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challange.crud.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findByName(String name);
}
