package ru.akudinov.test.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akudinov.test.exception.PersonIsBlockedException;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.repository.BlockedPersonRepository;
import ru.akudinov.test.repository.LoanRepository;
import ru.akudinov.test.service.LoanService;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
	private LoanRepository loanRepository;
	private BlockedPersonRepository blockedPersonRepository;

	public Loan apply(Loan loan) {
		if (blockedPersonRepository.isPersonBlocked(loan.getPersonalId())){
			throw new PersonIsBlockedException();
		}
		return loanRepository.save(loan);
	}

	@Override
	public List<Loan> list(Long personalId) {
		return Optional.ofNullable(personalId)
				.map(p -> singletonList(loanRepository.findByPersonalId(personalId)))
				.orElse((List<Loan>) loanRepository.findAll());
	}

}
