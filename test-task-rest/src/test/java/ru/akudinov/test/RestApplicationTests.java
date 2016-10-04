package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.repository.LoanRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class RestApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private LoanRepository repository;

	@Before
	public void init(){
		repository.deleteAll();
	}

	@Test
	public void testApply(){
		Loan loan = new Loan(BigDecimal.valueOf(100), 10, 10L , "Ivan", "Ivanov");

		ResponseEntity<Loan> entity = this.restTemplate.postForEntity("/loan/apply", loan,
				Loan.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assert.assertNotNull(entity.getBody().getId());
	}

	@Test
	public void testListByPersonId(){
		Arrays.asList(
			new Loan(BigDecimal.valueOf(100), 10, 11L , "Ivan", "Ivanov1"),
			new Loan(BigDecimal.valueOf(200), 10, 12L , "Ivan", "Ivanov2"),
			new Loan(BigDecimal.valueOf(300), 10, 13L , "Ivan", "Ivanov3")
		).forEach(loan -> {
					ResponseEntity<Loan> o = this.restTemplate.postForEntity("/loan/apply", loan, Loan.class);
					log.info("Applying loan: {}", o.toString());
				}
			);

		ResponseEntity<List> entity = restTemplate.getForEntity("/loan/list/{pesonalId}", List.class, 11L);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertTrue(java.util.Optional.ofNullable(entity.getBody().size()).get() == 1);
	}

	@Test
	public void testList(){
		Arrays.asList(
				new Loan(BigDecimal.valueOf(100), 10, 11L , "Ivan", "Ivanov1"),
				new Loan(BigDecimal.valueOf(200), 10, 12L , "Ivan", "Ivanov2"),
				new Loan(BigDecimal.valueOf(300), 10, 13L , "Ivan", "Ivanov3")
		).forEach(loan ->
				this.restTemplate.postForEntity("/loan/apply", loan, Loan.class));

		ResponseEntity<List> entity = restTemplate.getForEntity("/loan/list", List.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(3, java.util.Optional.ofNullable(entity.getBody()).get().size());
	}


	@Test
	public void testRejectedApplyByBlockedUser(){
		Loan loan = new Loan(BigDecimal.valueOf(100), 10, 1L , "Ivan", "Ivanov");

		ResponseEntity<String> entity = this.restTemplate.postForEntity("/loan/apply", loan, String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
