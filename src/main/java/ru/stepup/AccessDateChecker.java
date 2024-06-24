package ru.stepup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessDateChecker implements Checker<Login>{

    private static final Logger logger = LoggerFactory.getLogger(AccessDateChecker.class);

    public List<Login> check(List<Login> logins) {
        return logins.stream()
                .peek(login -> {
                    if (login.getAccessDate() == null) {
                        logger.error("Поле access_date пустое " + login.getId());
                    }
                })
                .filter(login -> login.getAccessDate() != null)
                .collect(Collectors.toList());
    }
}