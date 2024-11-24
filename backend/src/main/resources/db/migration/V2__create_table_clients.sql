CREATE TABLE clients(
    id uuid primary key,
    clinicId text not null,
    name text not null,
    surname text not null,
    phone numeric not null,
    email text unique not null,
    password text not null,
    created_at timestamp not null
)