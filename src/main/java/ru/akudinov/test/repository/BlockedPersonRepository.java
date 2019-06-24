package ru.akudinov.test.repository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic repository for blocked persons
 */
@Component
public class BlockedPersonRepository implements InitializingBean{
    private Set<Long> personDb = new HashSet<>();

    @Value("${blockedlist.filename}")
    private String blockedListFileName;

    private final ResourceLoader resourceLoader;

    public BlockedPersonRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public boolean isPersonBlocked(Long personalId){
        return personDb.contains(personalId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        URI uri = resourceLoader.getResource(blockedListFileName).getURI();
        Files.lines(Paths.get(uri)).forEach(s -> personDb.add(Long.valueOf(s)));
    }
}
