CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE logins_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
                       id integer DEFAULT NEXTVAL('users_id_seq'),
                       username varchar,
                       fio varchar,
                       PRIMARY KEY (id)
);

CREATE TABLE logins (
                        id integer DEFAULT NEXTVAL('logins_id_seq'),
                        access_date date,
                        user_id integer,
                        application varchar,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);