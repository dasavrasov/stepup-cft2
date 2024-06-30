package ru.stepup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@LogTransformation(logFile = "LOG/accessDateChecker.txt")
public class AccessDateChecker implements Checker<Login>{

    private static final Logger logger = LoggerFactory.getLogger(AccessDateChecker.class);

    public List<Login> check(List<Login> logins) {
        List<Login> result = new ArrayList<>();
        for (Login login : logins) {
            if (login.getAccessDate() == null) {
                logger.error("Поле access_date пустое " + login.getId());
            } else {
                result.add(login);
            }
        }
        return result;
    }
}