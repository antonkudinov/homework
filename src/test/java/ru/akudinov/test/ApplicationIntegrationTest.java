package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.repository.BlockedPersonRepository;
import ru.akudinov.test.repository.LoanRepository;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.akudinov.test.LoanHelper.loan;

/**
 * Created by akudinov on 03.10.16.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationIntegrationTest {
    @Autowired LoanRepository loanRepository;
    @Autowired BlockedPersonRepository blockedPersonRepository;

    @Test
    public void useRepositories() {

        loanRepository.save(loan(BigDecimal.valueOf(100), 10, 1L, "Frodo", "Baggins"));
        loanRepository.save(loan(BigDecimal.valueOf(130), 1, 2L, "Bilbo", "Baggins"));

        for (Loan person : loanRepository.findAll()) {
            log.info("Hello " + person.toString());
        }
    }

    @Test
    public void useBlockedPersonRepositories() {
        assertTrue(blockedPersonRepository.isPersonBlocked(1L));
        assertFalse(blockedPersonRepository.isPersonBlocked(10L));
    }



}
