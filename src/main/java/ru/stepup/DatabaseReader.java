package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DatabaseReader {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void readData() {
        String usersSql = "SELECT * FROM users";
        jdbcTemplate.query(usersSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println("User - ID: " + rs.getInt("id") + ", Username: " + rs.getString("username") + ", FIO: " + rs.getString("fio"));
            }
        });

        String loginsSql = "SELECT * FROM logins";
        jdbcTemplate.query(loginsSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println("Login - ID: " + rs.getInt("id") + ", Access Date: " + rs.getDate("access_date") + ", User ID: " + rs.getInt("user_id") + ", Application: " + rs.getString("application"));
            }
        });
    }
}