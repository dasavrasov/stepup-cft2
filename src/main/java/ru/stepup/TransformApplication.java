package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class TransformApplication implements ApplicationRunner {
    @Autowired
    DatabaseWriter databaseWriter;
    @Autowired
    FileReader fileReader;
    @Autowired
    List<Checker<User>> checkerUser;
    @Autowired
    List<Checker<Login>> checkerLogin;

    public void run(ApplicationArguments args) throws java.lang.Exception{
        List<String> lines = fileReader.readFile();
        List<User> users = fileReader.readUsers(lines);
        List<Login> logins = fileReader.readLogins(lines, users);
        for(Checker<User> fioChecker:checkerUser){
            users=fioChecker.check(users);
        }
        for(Checker<Login> applicationTypeChecker:checkerLogin){
            logins=applicationTypeChecker.check(logins);
        }
        databaseWriter.writeUsers(users);
        databaseWriter.writeLogins(logins);
    }
}
