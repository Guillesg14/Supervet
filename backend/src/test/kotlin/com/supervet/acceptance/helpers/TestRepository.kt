package com.supervet.acceptance.helpers

import at.favre.lib.crypto.bcrypt.BCrypt
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import java.util.*
import kotlin.random.Random

class TestRepository(
    private val jdbi: Jdbi
) {
    fun createClinic(): Clinic {
        val clinic = Clinic()

        jdbi.inTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    insert into users(id, email, password, type)
                    values(:id, :email, :password, :type)
                """.trimIndent()
            )
                .bind("id", clinic.userId)
                .bind("email", clinic.email)
                .bind("password", BCrypt.withDefaults().hashToString(12, clinic.password.toCharArray()))
                .bind("type", "CLINIC")
                .execute()

            handle.createUpdate(
                """
                        insert into clinics(id, user_id, phone, address)
                        values (:id, :user_id, :phone, :address)
                    """.trimIndent()
            )
                .bind("id", clinic.id)
                .bind("user_id", clinic.userId)
                .bind("phone",clinic.phone)
                .bind("address",clinic.address)
                .execute()
        }

        return clinic
    }

    fun createClient(clinic: Clinic): Client {
        val client = Client()

        jdbi.inTransactionUnchecked { handle ->
            handle.createUpdate(
                """
            INSERT INTO users (id, email, password, type)
            VALUES (:id, :email, :password, :type);
            """.trimIndent()
            )
                .bind("id", client.userId)
                .bind("email", client.email)
                .bind("password", BCrypt.withDefaults().hashToString(12, client.password.toCharArray()))
                .bind("type", "CLIENT")
                .execute()

            handle.createUpdate(
                """
            INSERT INTO clients (id, user_id, clinic_id, name, surname, phone)
            VALUES (:id, :user_id, :clinic_id, :name, :surname, :phone);
            """.trimIndent()
            )
                .bind("id", client.id)
                .bind("user_id", client.userId)
                .bind("clinic_id", clinic.id)
                .bind("name", client.name)
                .bind("surname", client.surname)
                .bind("phone", client.phone)
                .execute()
        }

        return client
    }

    fun createPatient(client: Client): Patient {
        val patient = Patient()

        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    INSERT INTO patients (id, client_id, name, breed, age, weight)
                    VALUES (:id, :clientId, :name, :breed, :age, :weight)
                    """.trimIndent()
            )
                .bind("id", patient.id)
                .bind("clientId", client.id)
                .bind("name", patient.name)
                .bind("breed", patient.breed)
                .bind("age", patient.age)
                .bind("weight", patient.weight)
                .execute()
        }
        return patient
    }

    fun createAppointment(patient: Patient) : Appointment{
        val appointment = Appointment()
        jdbi.useTransactionUnchecked { handle ->
            handle.createUpdate(
                """
                    INSERT INTO appointments (id, patient_id, appointment, created_at)
                    VALUES (:id, :patientId, :appointment, now())
                    """.trimIndent()
            )
                .bind("id", appointment.id)
                .bind("patientId", patient.id)
                .bind("appointment", appointment.appointment)
                .execute()
        }
        return appointment
    }

}

data class Clinic(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID = UUID.randomUUID(),
    val email: String = "${UUID.randomUUID()}@test.test",
    val password: String = UUID.randomUUID().toString(),
    val phone:  Int = Random.nextInt(100_000_000, 1_000_000_000),
    val address: String = UUID.randomUUID().toString(),
)

data class Client(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID = UUID.randomUUID(),
    val email: String = "${UUID.randomUUID()}@test.test",
    val password: String = UUID.randomUUID().toString(),
    val name: String = UUID.randomUUID().toString(),
    val surname: String = UUID.randomUUID().toString(),
    val phone: String = Random.nextInt(100_000_000, 1_000_000_000).toString(),
)

data class Patient(
    val id: UUID = UUID.randomUUID(),
    val name: String = UUID.randomUUID().toString(),
    val breed: String = UUID.randomUUID().toString(),
    val age: Int = Random.nextInt(0, 100),
    val weight: Int = Random.nextInt(0, 100),
)

data class Appointment(
    val id: UUID = UUID.randomUUID(),
    val patientId:  UUID = UUID.randomUUID(),
    val appointment: String = UUID.randomUUID().toString(),
)