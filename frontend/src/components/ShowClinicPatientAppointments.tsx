import { cookies } from "next/headers";
import { redirect } from "next/navigation";


interface Appointment {
    id: string;
    appointment: string;
    createdAt: string;
}

export default async function ShowClinicPatientsAppointments({patientId,clientId,}: {patientId: string;clientId: string;}) {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    if (!token) {
        redirect("/log-in");
    }

    async function fetchAppointmentsInfo(): Promise<Appointment[]> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clinics/clients/${clientId}/patients/${patientId}/appointments`,
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

    const appointments = await fetchAppointmentsInfo();

    if (appointments.length == 0) {
        return (
            <div className="container mx-auto p-4">
                <p className="text-red-500">Este paciente aún no tiene citas programadas</p>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Citas de la Mascota</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Cita
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Fecha
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white">
                {appointments.map((appointment) => (
                    <tr key={appointment.id}>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                            {appointment.appointment}
                        </td>
                        <td className="px-6 py-4 text-center whitespace-nowrap text-sm text-gray-900">
                            {appointment.createdAt}

                        </td>

                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
