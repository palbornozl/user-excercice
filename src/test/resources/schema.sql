CREATE SCHEMA IF NOT EXISTS EXERCISE;

CREATE TABLE IF NOT EXISTS EXERCISE.TBL_USER
(
    id         uuid         not null DEFAULT RANDOM_UUID() primary key,
    name       varchar(250) not null,
    email      varchar(100) not null,
    password   varchar(16)  not null,
    token      text         not null,
    is_active  boolean               default true,
    created_at timestamp,
    last_login timestamp,
    modified   timestamp
);

CREATE TABLE IF NOT EXISTS EXERCISE.TBL_USER_PHONE
(
    id           uuid primary key,
    id_user      uuid      not null,
    number       integer   not null,
    city_code    integer   not null,
    country_code integer   not null,
    created_at   timestamp not null default current_timestamp(),
    updated_at   timestamp not null default current_timestamp(),
    FOREIGN KEY (id_user) REFERENCES EXERCISE.TBL_USER (id)
);
