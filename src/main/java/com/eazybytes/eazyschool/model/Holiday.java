package com.eazybytes.eazyschool.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Entity
@Data
@Table(name = "holidays")
public class Holiday extends BaseEntity {

    @Id
    private String day;

    private String reason;

    @Enumerated(EnumType.STRING) // The data type is enum but in data base we define as varchar, so this converts to varchar
    private Type type;

    public enum Type {
        FESTIVAL, FEDERAL
    }
}
