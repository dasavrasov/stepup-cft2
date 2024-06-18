package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FioChecker {
    @Autowired
    private DatabaseReader databaseReader;

    @Autowired
    private DatabaseWriter databaseWriter;

    public void check() {
        List<User> users = databaseReader.readUser();
        for (User user : users) {
            String fio = user.getFio();
            if (!Character.isUpperCase(fio.charAt(0))) {
                user.setFio(fio.substring(0, 1).toUpperCase() + fio.substring(1));
                databaseWriter.updateUser(user);
            }
        }
    }
}
