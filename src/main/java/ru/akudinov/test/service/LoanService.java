package ru.akudinov.test.service;

import ru.akudinov.test.model.Loan;
import java.util.List;

public interface LoanService {
    Loan apply(Loan loan);
    List<Loan> list(Long personalId);
}
