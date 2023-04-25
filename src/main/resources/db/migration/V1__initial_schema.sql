drop table if exists users;
drop table if exists users;

create table user_details
(
    id         bigserial primary key,
    first_name varchar(32) not null,
    last_name  varchar(32) not null,
    student_group      varchar(32)
);

create table users
(
    id       serial primary key,
    username varchar(32)  not null,
    password varchar(255) not null,
    role     varchar(255) not null,
    details_id integer unique references user_details(id)
);