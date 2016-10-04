package ru.akudinov.test;

import ru.akudinov.test.model.Loan;

import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

public interface LoanService {
    Loan apply(Loan loan);

    List<Loan> list(Long personalId);
}
