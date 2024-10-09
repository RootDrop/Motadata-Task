package com.challange.crud.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateCustomerResponse extends CommonResponse {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("id")
  private Long id;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("name")
  private String name;
}
