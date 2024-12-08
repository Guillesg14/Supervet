import { cookies } from "next/headers";

interface Appointment {
    appointmentId: string;
    appointment: string;
    patientId: string;
}

interface Patient {
    id: string;
    name: string;
    breed: string;
    age: string;
    weight: string;
    clientId: string;
    appointments: Appointment[]; // Nuevo campo para almacenar las citas
}

export default async function ShowClientPatients() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    async function fetchPatientsInfo(): Promise<Patient[]> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clients/patients`, // Ajusta la URL si es necesario
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(
                    `Error fetching patients. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch patients: ${response.status} ${errorDetails}`);
            }

            return await response.json();
        } catch (err) {
            console.error("Error fetching client:", err);
            return [];
        }
    }

    async function fetchAppointments(): Promise<Appointment[]> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clients/appointments`, // Ajusta la URL si es necesario
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (!response.ok) {
                const errorDetails = await response.text();
                console.error(
                    `Error fetching appointments. Status: ${response.status}, Details: ${errorDetails}`
                );
                throw new Error(`Failed to fetch appointments: ${response.status} ${errorDetails}`);
            }

            return await response.json();
        } catch (err) {
            console.error("Error fetching appointments:", err);
            return [];
        }
    }

    const patients = await fetchPatientsInfo();

    // Obtener las citas para cada paciente
    const appointments = await fetchAppointments();

    for (const patient of patients) {
        patient.appointments = appointments.filter(appointment =>
            appointment.patientId === patient.id
        );
    }

    if (patients.length === 0) {
        return (
            <div className="container mx-auto p-4">
                <p className="text-red-500">Aún no tienes mascotas.</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Tus Mascotas</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nombre
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Edad
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Raza
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Peso
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white">
                {patients.map((patient) => (
                    <tr key={patient.id}>
                        {/* Fila de los datos del paciente */}
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.name}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.age}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.breed}</td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{patient.weight}</td>
                    </tr>
                ))}
                {/* Fila con el encabezado de "Historial" y las citas debajo de los datos del paciente */}
                {patients.map((patient) => (
                    <tr key={`appointments-${patient.id}`}>
                        <td colSpan={4} className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            <h3 className="font-semibold text-lg text-gray-700">Historial</h3>
                            {patient.appointments.length > 0 ? (
                                <ul className="list-inside">
                                    {patient.appointments.map((appointment) => (
                                        <li key={appointment.appointmentId}>
                                            {appointment.appointment}
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <span>No hay citas</span>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
