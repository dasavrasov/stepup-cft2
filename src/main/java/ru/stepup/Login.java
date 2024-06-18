package ru.stepup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
public class Login {
    private long id;
    private String userId;
    private String application;
    private Date accessDate;
}
