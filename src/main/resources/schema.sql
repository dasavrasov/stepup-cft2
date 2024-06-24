CREATE TABLE users (
                       id integer,
                       username varchar,
                       fio varchar,
                       PRIMARY KEY (id)
);

CREATE TABLE logins (
                        id integer,
                        access_date date,
                        user_id integer,
                        application varchar,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);