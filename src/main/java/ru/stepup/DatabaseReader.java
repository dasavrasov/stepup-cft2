package ru.stepup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DatabaseReader {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> readUser() {
        String usersSql = "SELECT id,username,fio FROM users";
        List<User> users = jdbcTemplate.query(usersSql, new UserRowMapper());
        return users;
    }

    public List<Login> readLogin() {
        String loginsSql = "SELECT id,user_id,application,access_date FROM logins";
        List<Login> logins = jdbcTemplate.query(loginsSql, new LoginRowMapper());
        return logins;
    }

    class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getLong("id"), rs.getString("username"), rs.getString("fio"));
            return user;
        }
    }

    class LoginRowMapper implements RowMapper<Login> {
        @Override
        public Login mapRow(ResultSet rs, int rowNum) throws SQLException {
            Login login = new Login(rs.getLong("id"), rs.getString("user_id"), rs.getString("application"), rs.getDate("access_date"));
            return login;
        }
    }
}