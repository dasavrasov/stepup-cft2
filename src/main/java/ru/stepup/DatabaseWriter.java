package ru.stepup;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class DatabaseWriter {
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;
    public DatabaseWriter(MyConnection myConnection) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(myConnection.getUrl());
        dataSource.setUsername(myConnection.getUsername());
        dataSource.setPassword(myConnection.getPassword());
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void writeUsers(List<User> users) {
        for (User user : users) {
            jdbcTemplate.update("INSERT INTO users (id,username, fio) VALUES (?,?, ?)", user.getId(),user.getUsername(), user.getFio());
        }
    }

    public void writeLogins(List<Login> logins) {
        for (Login login : logins) {
            jdbcTemplate.update("INSERT INTO logins (id,access_date, user_id, application) VALUES (?,?, ?, ?)", login.getId(),login.getAccessDate(), login.getUserId(), login.getApplication());
        }
    }
}