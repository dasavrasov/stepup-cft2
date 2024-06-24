package ru.stepup;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationTypeChecker implements Checker<Login>{

    public List<Login> check(List<Login> logins) {
        logins.forEach(login -> {
            String application = login.getApplication();
            if (!"web".equals(application) && !"mobile".equals(application)) {
                login.setApplication("other:" + login.getApplication());
            }
        });
        return logins;
    }
}