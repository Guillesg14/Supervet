CREATE TABLE users(
    id uuid primary key,
    email text unique not null,
    password text not null,
    created_at timestamp not null
)