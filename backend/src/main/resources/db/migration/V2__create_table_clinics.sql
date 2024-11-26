create table clinics
(
    id      uuid primary key,
    user_id uuid unique not null,
    created_at timestamp default current_timestamp,
    foreign key (user_id) references users (id) on delete cascade
);