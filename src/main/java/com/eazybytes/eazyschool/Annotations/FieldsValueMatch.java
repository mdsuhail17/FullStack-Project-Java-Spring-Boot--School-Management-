package com.eazybytes.eazyschool.Annotations;


import com.eazybytes.eazyschool.Validations.FeildValueMatchValiditor;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FeildValueMatchValiditor.class)
public @interface FieldsValueMatch {

  String message() default "Fields Value dont match";
  String field();
  String fieldMatch();

  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default{};

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @interface List{
      FieldsValueMatch[]  value();
  }
}
