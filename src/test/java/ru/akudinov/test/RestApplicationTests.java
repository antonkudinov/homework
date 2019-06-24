package ru.akudinov.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.akudinov.test.model.Loan;
import ru.akudinov.test.repository.LoanRepository;
import ru.akudinov.test.service.impl.Ip2CountryService;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.akudinov.test.LoanHelper.loan;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RestApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
@Slf4j
public class RestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Ip2CountryService service;

    @Autowired
    private LoanRepository repository;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        given(service.takeCountry("127.0.0.1")).willReturn("RU");
        objectMapper = new ObjectMapper();

        repository.saveAll(
                asList(loan(BigDecimal.valueOf(100), 10, 11L, "Ivan", "Ivanov1"),
                loan(BigDecimal.valueOf(200), 10, 12L, "Ivan", "Ivanov2"),
                loan(BigDecimal.valueOf(300), 10, 13L, "Ivan", "Ivanov3"))
        );
    }

    @Before
    public void after() {
        repository.deleteAll();
    }


    @Test
    public void testApply() throws Exception {
        final Loan loan = loan(BigDecimal.valueOf(100), 10, 10L, "Ivan", "Ivanov");

        mockMvc.perform(
                post("/loan/apply").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.term", Matchers.is(10)));
    }

    @Test
    public void testListByPersonId() throws Exception {
        mockMvc.perform(
                get("/loan/list/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId", Matchers.is(11)));
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(
                get("/loan/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }


    @Test
    public void testRejectedApplyByBlockedUser()  throws Exception {
        Loan loan = loan(BigDecimal.valueOf(100), 10, 1L, "Ivan", "Ivanov");
        mockMvc.perform(
                post("/loan/apply").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().is5xxServerError());
    }


}
