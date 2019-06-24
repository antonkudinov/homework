package ru.akudinov.test;

import ru.akudinov.test.model.Loan;

import java.math.BigDecimal;

public class LoanHelper{
    public static Loan loan(BigDecimal amount, int term, long personalId, String name, String surname) {
        return new Loan()
                .setAmount(amount)
                .setTerm(term)
                .setPersonalId(personalId)
                .setName(name)
                .setSurname(surname);
    }

}
