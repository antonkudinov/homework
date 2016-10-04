package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akudinov.test.exception.PersonIsBlockedException;
import ru.akudinov.test.model.Loan;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Component
@Path("/loan")
@Slf4j
/**
 * Endpoint for loan web service
 */
public class LoanEndpoint {

	public static final int TO_MANY_REQUESTS = 429;
	private final LoanService loanService;
	@Autowired
	private RateLimit rateLimit;
	@Autowired
	private Ip2CountryService countryService;

	@Autowired
	public LoanEndpoint(LoanService loanService) {
		this.loanService = loanService;
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("apply")
	public Response apply(Loan loan, @Context HttpServletRequest request) {
		String country = countryService.takeCountry(request.getRemoteAddr());
		log.info("Take country : {}", country);
		if (!rateLimit.acquire(country)){
			log.info("Too many requests from {}", country);
			return Response.status(TO_MANY_REQUESTS).build();
		}

		try {
			return Response.ok(loanService.apply(loan)).build();
		} catch (PersonIsBlockedException e) {
			return Response.serverError().entity("Person is blocked").build();
		}
	}

	@GET
	@Produces("application/json")
	@Path("list/{personalId}")
	public Response list(@PathParam("personalId") Long personalId) {
		return Response.ok(loanService.list(personalId)).build();
	}

	@GET
	@Produces("application/json")
	@Path("list")
	public Response list() {
		return Response.ok(loanService.list(null)).build();
	}



}
