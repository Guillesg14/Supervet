create table clients
(
    id        uuid primary key,
    user_id   uuid unique not null,
    clinic_id uuid        not null,
    name      text        not null,
    surname   text        not null,
    phone     text        not null,
    created_at timestamp default current_timestamp,
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (clinic_id) references users (id) on delete cascade
);