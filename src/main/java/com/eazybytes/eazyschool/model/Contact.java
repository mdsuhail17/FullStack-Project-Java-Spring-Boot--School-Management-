package com.eazybytes.eazyschool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/*
@Data annotation is provided by Lombok library which generates getter, setter,
equals(), hashCode(), toString() methods & Constructor at compile time.
This makes our code short and clean.
* */
@Entity // represent the have relationship betwwen dataBase table
@Data
@Table(name = "contact_msg") // if table name is not matching withe claas name we use table annotataion
public class Contact extends BaseEntity{

    @Id  // indicates that igt is primary  Key
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native") // for genarating the primary key automatically
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "contact_Id") // If column name is not matching with datbase columns
    private int contactId;
    /*
    * @NotNull: Checks if a given field is not null but allows empty values & zero elements inside collections.
      @NotEmpty: Checks if a given field is not null and its size/length is greater than zero.
      @NotBlank: Checks if a given field is not null and trimmed length is greater than zero.
    * */
    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
//    @JsonProperty("Person_name") // dint disply the actul field name inseated of that is send the data with thse name  @JsonProperty("Person_name")
    private String name;

    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNum;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;

    @NotBlank(message="Subject must not be blank")
    @Size(min=5, message="Subject must be at least 5 characters long")
    private String subject;

    @NotBlank(message="Message must not be blank")
    @Size(min=10, message="Message must be at least 10 characters long")
    private String message;

    private String status;
}
