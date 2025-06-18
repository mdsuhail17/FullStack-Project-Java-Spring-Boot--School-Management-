package com.eazybytes.eazyschool.Validations;

import com.eazybytes.eazyschool.Annotations.FieldsValueMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FeildValueMatchValiditor implements ConstraintValidator<FieldsValueMatch, Object> {

    private  String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);

        Object  FieldValueMatch = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

               // commented for we are eliminating spring jpa validations usig this spring.jpa.properties.javax.persistence.validation.mode=none
//         if (fieldValue != null){
//            if(fieldValue.toString().startsWith("$2a")){
//               return true;
//            }else{
//                return fieldValue.equals(FieldValueMatch);
//            }
//        }else{
//            return FieldValueMatch == null;
//        }

        if(fieldValue != null){
            return fieldValue.equals(FieldValueMatch);
        }else{
            return FieldValueMatch == null;
        }
    }


}
