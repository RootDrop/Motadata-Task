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
public class GetCustomerResponse extends CommonResponse {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @JsonProperty("customer")
  private Customers customers;

  @Getter
  @Setter
  @NoArgsConstructor
  @SuperBuilder
  public static class Customers {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("id")
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("name")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("details")
    private Details details;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("account_type")
    private String accountType;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("business_requirements")
    private String[] businessRequirements;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("contract_type")
    private String contractType;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class Details {

      @JsonInclude(JsonInclude.Include.NON_EMPTY)
      @JsonProperty("sex")
      private String sex;

      @JsonInclude(JsonInclude.Include.NON_EMPTY)
      @JsonProperty("dob")
      private String dob;

      @JsonInclude(JsonInclude.Include.NON_EMPTY)
      @JsonProperty("native_place")
      private String nativePlace;

    }
  }

}
