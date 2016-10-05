package ru.akudinov.test.repository;

import org.springframework.data.repository.CrudRepository;
import ru.akudinov.test.model.Loan;

/**
 * Basic CRUD repository for loans
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {
    Loan findByPersonalId(Long personalId);
}
