package ru.stepup;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logins")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "application")
    private String application;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "access_date")
    private Date accessDate;
}
