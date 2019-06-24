package ru.akudinov.test;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.akudinov.test.exception.PersonIsBlockedException;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.service.impl.Ip2CountryService;
import ru.akudinov.test.service.LoanService;
import ru.akudinov.test.service.RateLimit;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/loan")
@Slf4j
@AllArgsConstructor
public class LoanEndpoint {
    private final LoanService loanService;
    private RateLimit rateLimit;
    private Ip2CountryService countryService;


    @PostMapping(path = "apply")
    public Loan apply(@RequestBody Loan loan, HttpServletRequest request) {
        final String country = countryService.takeCountry(request.getRemoteAddr());
        log.info("Take country : {}", country);
        if (!rateLimit.acquire(country)) {
            log.info("Too many requests from {}", country);
            throw new RuntimeException("Too many requests");
        }

        try {
            return loanService.apply(loan);
        } catch (PersonIsBlockedException e) {
            throw new RuntimeException("Person is blocked");
        }
    }

    @GetMapping("list/{personalId}")
    public List<Loan> list(@PathVariable("personalId") Long personalId) {
        return loanService.list(personalId);
    }

    @GetMapping("list")
    public List<Loan> list() {
        return loanService.list(null);
    }
}
