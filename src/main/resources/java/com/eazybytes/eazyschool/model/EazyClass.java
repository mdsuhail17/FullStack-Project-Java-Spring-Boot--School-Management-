package com.eazybytes.eazyschool.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Collection;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name ="class") // table name is different from the class name so we are using @Table annotation to match with the database table name
public class EazyClass extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private  int classId;

    @NotBlank(message = "Name must not be Blank")
    @Size(min = 3, message = "Name must be at least 3 character Long")
    private String name;

        //mappedBy = "eazyClass" used in parent entity calss
    @OneToMany(mappedBy = "eazyClass", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST,targetEntity = Person.class)
    private Set<Person> persons;// class can have multiple persons so we created colleaction set


}
