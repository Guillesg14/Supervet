create table users
(
    id         uuid primary key,
    email      text unique not null,
    password   text not null,
    type       text not null,
    created_at timestamp default current_timestamp
);