CREATE TABLE patients (
  id            uuid primary key,
  owner_id      text            not null,
  owner_name    text            not null,
  patient_id    uuid            not null unique,
  name          text            not null,
  breed         text,
  age           text,
  weight        int,
  status        text,
  appointment   TIMESTAMP
);