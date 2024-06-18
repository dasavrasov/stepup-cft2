package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void loadData(List<String> lines) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for (String line : lines) {
            String[] data = line.split(";");
            jdbcTemplate.update("INSERT INTO users (username, fio) VALUES (?, ?)", data[0], data[1]);
            try {
                java.util.Date parsedDate = format.parse(data[2]);
                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
                jdbcTemplate.update("INSERT INTO logins (access_date, user_id, application) VALUES (?, (SELECT id FROM users WHERE username = ?), ?)", sqlDate, data[0], data[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET username = ?, fio = ? WHERE id = ?", user.getUsername(), user.getFio(), user.getId());
    }

    public void updateLogin(Login login) {
        jdbcTemplate.update("UPDATE logins SET access_date = ?, user_id = ?, application = ? WHERE id = ?", login.getAccessDate(), login.getUserId(), login.getApplication(), login.getId());
    }
}