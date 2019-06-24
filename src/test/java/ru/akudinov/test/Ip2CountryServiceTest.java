package ru.akudinov.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.akudinov.test.service.impl.Ip2CountryService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by akudinov on 04.10.16.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class Ip2CountryServiceTest {
    @Autowired
    Ip2CountryService ip2CountryService;

    @Value("${ip2country.url}")
    private String url;

    @Test
    public void testGetCountry() {
        String s = ip2CountryService.takeCountry("127.0.0.1");
        assertNotNull(s);
        assertEquals("LV", s);
    }

    @Test
    public void testGetNotDefaultCountry() {
        RestTemplate mock = mock(RestTemplate.class);
        when(mock.getForObject(url, Ip2CountryService.Country.class, ""))
                .thenReturn(new Ip2CountryService.Country("TEST"));
        ip2CountryService.setRestTemplate(mock);
        String s = ip2CountryService.takeCountry("");
        assertNotNull(s);
        assertEquals("TEST", s);
    }


}
