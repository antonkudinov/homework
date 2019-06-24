package ru.akudinov.test.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@Accessors(chain =  true)
@Entity
@Data
@ToString
public class Loan {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private Long personalId;
    private String name;
    private String surname;
}
