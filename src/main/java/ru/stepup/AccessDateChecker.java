package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccessDateChecker {
    @Autowired
    private DatabaseReader databaseReader;

    @Autowired
    private DatabaseWriter databaseWriter;

    public void check() {
        List<Login> logins = databaseReader.readLogin();
        for (Login login : logins) {
            if (login.getAccessDate() == null) {
                System.out.println("Access date is null for login with id " + login.getId());
            }
        }
    }
}
