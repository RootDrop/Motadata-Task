package com.challange.crud.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCustomerRequest {

  @JsonProperty("id")
  @NotNull(message = "Id should not be null")
  private Long id;
}
