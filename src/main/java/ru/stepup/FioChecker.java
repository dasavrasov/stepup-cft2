package ru.stepup;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@LogTransformation(logFile = "LOG/fioChecker.txt")
public class FioChecker implements Checker<User>{
    public List<User> check(List<User> users) {
        users.forEach(user -> {
            if (!Character.isUpperCase(user.getFio().charAt(0))) {
                user.setFio(user.getFio().substring(0, 1).toUpperCase() + user.getFio().substring(1));
            }
        });
        return users;
    }
}
