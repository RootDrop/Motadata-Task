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
public class CommonResponse {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("success")
  private Integer success;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("error")
  private String error;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("message")
  private String message;
}
