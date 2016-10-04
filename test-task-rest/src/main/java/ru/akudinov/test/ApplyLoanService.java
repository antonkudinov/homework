package ru.akudinov.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akudinov.test.exception.PersonIsBlockedException;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.repository.BlockedPersonRepository;
import ru.akudinov.test.repository.LoanRepository;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
/**
 * This service implements basic functionality of loan service
 */
public class ApplyLoanService implements LoanService {
	@Autowired private LoanRepository loanRepository;
	@Autowired private BlockedPersonRepository blockedPersonRepository;

	public Loan apply(Loan loan) {
		if (blockedPersonRepository.isPersonBlocked(loan.getPersonalId())){
			throw new PersonIsBlockedException();
		}
		return loanRepository.save(loan);
	}

	@Override
	public List<Loan> list(Long personalId) {
		return Optional.ofNullable(personalId)
				.map(p -> Arrays.asList(loanRepository.findByPersonalId(personalId)))
				.orElse((List<Loan>) loanRepository.findAll());
	}

}
