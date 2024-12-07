CREATE TABLE patients
(
    id        uuid primary key,
    client_id uuid not null,
    name      text not null,
    breed     text,
    age       text,
    weight    int,
    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);