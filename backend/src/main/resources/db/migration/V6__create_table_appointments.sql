CREATE TABLE appointments
(
    id uuid primary key,
    patient_id uuid not null,
    appointment text not null,
    created_at timestamp not null,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE
)