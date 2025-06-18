package com.eazybytes.eazyschool.model;

import com.eazybytes.eazyschool.Annotations.FieldsValueMatch;
import com.eazybytes.eazyschool.Annotations.PasswordValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@FieldsValueMatch.List({
       @FieldsValueMatch(
               field = "pwd",
               fieldMatch = "confirmPwd",
               message = "Password Must be match"
       ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email address must be match"
        )
})
public class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int personId;

    @NotBlank(message = "name must not be blank")
    @Size(min=3, message = "name must be atleast three charcter long")
    private String name;

    @NotBlank(message = "phone number must be not blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Email must be not blank")
    @Email(message = "please provide valid email address")
    private String email;

    @Transient // does not consider the any database related operations
    @JsonIgnore
    private String confirmEmail;

    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
    @PasswordValidator
    @JsonIgnore// not share unnesery information to the as part of rest api response, doest show the pass and confpass and confrmemail
    private String pwd;

    @NotBlank(message="Confirm Password must not be blank")
    @Size(min=5, message="Confirm Password must be at least 5 characters long")
    @Transient // does not consider the any database related operations
    @JsonIgnore
    private String confirmPwd;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, targetEntity = Address.class)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId",nullable = true)
    private Address address;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    private Roles roles;

    // not use cascade because we can not try to save the eazyclass information from the person  entity class
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "class_id", referencedColumnName = "classId", nullable = true)
    private EazyClass eazyClass;

      // for courses and intermideate table person_courses
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "person_courses", // intermideate table name
            // joining the column from person table to person_courses table by taking person_id present in the person_courses and person entity
            joinColumns = {
            @JoinColumn(name = "person_id" , referencedColumnName = "personId")},

            // joining the column from course table to person_courses table by taking course_id present in the person_courses and course  entity
            inverseJoinColumns = {
             @JoinColumn(name = "course_id", referencedColumnName = "courseId")})
    private Set<Courses> courses = new HashSet<>(); // set because person can enroll multiple courses

}

