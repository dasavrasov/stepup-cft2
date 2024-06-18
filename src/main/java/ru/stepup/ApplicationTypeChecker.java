package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationTypeChecker {
    @Autowired
    private DatabaseReader databaseReader;

    @Autowired
    private DatabaseWriter databaseWriter;

    public void check() {
        List<Login> logins = databaseReader.readLogin();
        for (Login login : logins) {
            String application = login.getApplication();
            if (!"web".equals(application) && !"mobile".equals(application)) {
                login.setApplication("other:" + application);
                databaseWriter.updateLogin(login);
            }
        }
    }
}
