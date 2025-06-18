package com.eazybytes.eazyschool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Struct;
@Data
@Entity
public class Address  extends  BaseEntity{


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private int addressId;

  @NotBlank(message = "Address must be not blank")
  @Size(min=5, message = "Address must be contains minimum five charecter")
  private String address1;

  private String address2;

    @NotBlank(message = "City must be not blank")
    @Size(min=5, message = "City must be contains minimum five charecter")
    private String city;

    @NotBlank(message = "State must be not blank")
    @Size(min=5, message = "State must be contains minimum five charecter")
    private String state;

    @NotBlank(message = "pin code must be not blank")
    @Pattern(regexp="(^$|[0-9]{5})",message = "Zip Code must be 5 digits")
    private String zipCode;
}
