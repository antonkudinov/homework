package ru.akudinov.test.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Entity
@Data
@ToString
/**
 * Loan entity
 */
public class Loan {
    @Id
    @GeneratedValue
    private Long id;
    private final BigDecimal amount;
    private final Integer term;
    private final Long personalId;
    private final String name;
    private final String surname;

    public Loan(){
        this(null, null, null, null, null);
    }
}
