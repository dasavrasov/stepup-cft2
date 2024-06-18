package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class DatabaseWriter {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveData(List<String> lines) {
        for (String line : lines) {
            String[] data = line.split(";");
            jdbcTemplate.update("INSERT INTO users (id, username, fio) VALUES (?, ?, ?)", Integer.parseInt(data[0]), data[1], data[2]);
            jdbcTemplate.update("INSERT INTO logins (id, access_date, user_id, application) VALUES (?, ?, ?, ?)", Integer.parseInt(data[0]), Date.valueOf(data[3]), Integer.parseInt(data[0]), data[4]);
        }
    }

}
