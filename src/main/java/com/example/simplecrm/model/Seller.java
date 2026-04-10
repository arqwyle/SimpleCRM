package com.example.simplecrm.model;

import com.example.simplecrm.model.base.BaseModel;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Audited
@Entity
@Getter
@Setter
public class Seller extends BaseModel {
    private String name;
    private String contactInfo;
    private LocalDateTime registrationDate;
}
