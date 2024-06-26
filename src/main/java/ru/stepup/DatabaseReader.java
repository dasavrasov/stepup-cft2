package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseReader {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;

    public List<User> readAllUsers() {
        List<User> users = (List<User>)userRepository.findAll();
        return users;
    }

    public List<Login> readAllLogins() {
        List<Login> logins = (List<Login>)loginRepository.findAll();
        return logins;
    }
}