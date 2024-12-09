import { cookies } from "next/headers";

interface Appointment {
    appointmentId: string;
    patientId: string;
    appointment: string;
    patientName: string;
}

export default async function ShowClientAppointments() {
    const cookieStore = await cookies();
    const token = cookieStore.get("session")?.value;

    async function fetchPatientsAppointments(): Promise<Appointment[]> {
        try {
            const response = await fetch(
                `https://${process.env.API_URL}.onrender.com/clients/appointments`,
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
            console.error("Error fetching client:", err);
            return [];
        }
    }

    const appointments = await fetchPatientsAppointments();

    if (appointments.length === 0) {
        return (
            <div className="container mx-auto p-4">
                <p className="text-red-500">Aún no tienes citas.</p>
            </div>
        );
    }

    // Agrupar citas por el nombre del paciente
    const groupedAppointments = appointments.reduce((acc, appointment) => {
        if (!acc[appointment.patientName]) {
            acc[appointment.patientName] = [];
        }
        acc[appointment.patientName].push(appointment);
        return acc;
    }, {} as { [key: string]: Appointment[] });

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">Tus Citas</h1>
            <table className="min-w-full divide-y divide-gray-200 border border-gray-300">
                <thead className="bg-blue1">
                <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nombre de la Mascota
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Información
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white">
                {Object.keys(groupedAppointments).map((patientName) => (
                    <>
                        <tr key={`header-${patientName}`}>
                            <td
                                colSpan={2}
                                className="px-6 py-3 text-sm font-semibold text-gray-900 bg-gray-100"
                            >
                                {patientName}
                            </td>
                        </tr>
                        {groupedAppointments[patientName].map((appointment) => (
                            <tr key={appointment.appointmentId}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">

                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {appointment.appointment}
                                </td>
                            </tr>
                        ))}
                    </>
                ))}
                </tbody>
            </table>
        </div>
    );
}
