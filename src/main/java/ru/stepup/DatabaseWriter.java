package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseWriter {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;

    public void writeUsers(List<User> users) {
        userRepository.saveAll(users);
    }

    public void writeLogins(List<Login> logins) {
        loginRepository.saveAll(logins);
    }
}