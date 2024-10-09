package com.challange.crud.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Name is mandatory")
  @Column(name = "name")
  private String name;

  @NotBlank(message = "Account type is mandatory")
  @Column(name = "account_type")
  private String accountType;

  @NotBlank(message = "Contract type is mandatory")
  @Column(name = "contract_type")
  private String contractType;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
  private CustomerDetails details;

}
