package ru.akudinov.test;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
/**
 * This class used for calculate country by user IP
 */
public class Ip2CountryService {
    @Setter
    private RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

    @Value("${ip2country.url}")
    private String url;
    @Value("${ip2country.timeout}")
    private int timeout;
    @Value("${ip2country.default_country}")
    private String defaultCountryCode;

    @Data
    @RequiredArgsConstructor
    final static class Country{
        private final String country_code;
    }

    public String takeCountry(String address){
        return Optional.ofNullable(restTemplate.getForObject(url, Country.class, address))
                .filter(s -> !Strings.isNullOrEmpty(s.getCountry_code()))
                .map(Country::getCountry_code)
                .orElse(defaultCountryCode);
    }


    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

}
